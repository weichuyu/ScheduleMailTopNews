package com.yu.tools.service.weibo.impl;

import com.alibaba.fastjson.JSONObject;
import com.yu.tools.model.BatchWeiboTop;
import com.yu.tools.model.Schedule;
import com.yu.tools.repo.BatchWeiboTopRepo;
import com.yu.tools.repo.ScheduleRepo;
import com.yu.tools.service.weibo.IFetchWeiboService;
import com.yu.tools.util.MyHttpClientUtil;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * yuweichu 2019/10/16 拉取新浪热搜的返回
 */
@Service
public class FetchWeiboServiceImpl implements IFetchWeiboService {
    private static Logger logger = Logger.getLogger(FetchWeiboServiceImpl.class);

    private static String[] ten = {"1","2","3","4","5","6","7","8","9","10"};
    @Resource
    private MyHttpClientUtil myHttpClientUtil;

    @Resource
    private BatchWeiboTopRepo batchWeiboTopRepo;

    @Resource
    private ScheduleRepo scheduleRepo;

    private class ModelWrapper {
        public String rank;
        public String keyWord;
        public String href;
        public String index;
    }

    private class ResultWrapper {
        public String errorCode;
        public List<ModelWrapper> data;
        public String errorMsg;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getTodayTop(Boolean writeDataBase, String batchId) {
        ResultWrapper result = new ResultWrapper();
        result.errorCode = "0000";
        result.errorMsg = "";
        List<ModelWrapper> data = null;
        {
            //http://top.baidu.com/buzz?b=341&c=513&fr=topbuzz_b1
            String html = myHttpClientUtil.getDataFromURL("https://s.weibo.com/top/summary?cate=realtimehot", "GBK",false);
            if (html != null && html.length() > 0) {
                data = getDataFromHtml(html);
                result.data = data;
            }
        }
        if (writeDataBase && data != null) {
            for (ModelWrapper ob : data) {
                BatchWeiboTop s = new BatchWeiboTop();
                s.setBatchid(batchId);
                s.setId(UUID.randomUUID().toString().replace("-", ""));
                s.setIndex(ob.index);
                s.setKeyword(ob.keyWord);
                s.setRank(ob.rank);
                batchWeiboTopRepo.save(s);
            }
        }
        return JSONObject.toJSONString(result);
    }

    @Override
    public Map<String,Object> getCurrentCompareMailHtml() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Sort sort = new Sort(Sort.Direction.DESC, "batchcreatetime"); //创建时间降序排序
        Pageable pageable = new PageRequest(0, 2, sort);
        Schedule query = new Schedule();
        query.setBatchtype("WeiboTopRealTime");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.ENDING);
                //.withIgnorePaths("batchtype");
        Example<Schedule> example = Example.of(query, matcher);
        Page<Schedule> list = scheduleRepo.findAll(example, pageable);
        List<Schedule> result = list.getContent();
        if (result == null || result.size() < 2) {
            return null;
        }
        String uuid1 = null;
        String uuid2 = null;
        List<BatchWeiboTop> list1 = null;
        List<BatchWeiboTop> list2 = null;
        ZoneId zoneId = ZoneId.systemDefault();
        String nowDateStr = null;
        String beforeDateStr = null;

        for (int i = 0; i < 2; i++) {
            if (result.get(i) == null || result.get(i).getBatchId() == null || result.get(i).getBatchId().length() <= 0) {
                return null;
            }
            Instant instant = result.get(i).getBatchcreatetime().toInstant();
            LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
            String str = df.format(localDateTime);
            List<BatchWeiboTop> temp = batchWeiboTopRepo.findByBatchid(result.get(i).getBatchId());
            if (i == 0) {
                list1 = temp;
                nowDateStr = str;
                uuid1 = result.get(i).getBatchId();
            } else {
                list2 = temp;
                beforeDateStr = str;
                uuid2 = result.get(i).getBatchId();
            }
        }
        //报表
        List<String> keywordsNew = new ArrayList<String>();
        List<String> keywordsLeave = new ArrayList<String>();
        List<BatchWeiboTop> list1ten = new ArrayList<BatchWeiboTop>();
        List<BatchWeiboTop> list2ten = new ArrayList<BatchWeiboTop>();
        Map<String,Boolean> map1 = new HashMap<String,Boolean>();
        Map<String,Boolean> map2 = new HashMap<String,Boolean>();
        Map<String,Boolean> map1ten = new HashMap<String,Boolean>();
        Map<String,Boolean> map2ten = new HashMap<String,Boolean>();
        List<String> tens = Arrays.asList(ten);
        for(BatchWeiboTop ob : list1){
            map1.put(ob.getKeyword(),true);
            if(tens.contains(ob.getRank())){
                list1ten.add(ob);
                map1ten.put(ob.getKeyword(),true);
            }
        }
        for(BatchWeiboTop ob : list2){
            map2.put(ob.getKeyword(),true);
            if(tens.contains(ob.getRank())){
                list2ten.add(ob);
                map2ten.put(ob.getKeyword(),true);
            }
        }
        for(BatchWeiboTop ob : list1){
            if(!map2.containsKey(ob.getKeyword())){
                keywordsNew.add(ob.getKeyword()+"("+ob.getRank()+")");
            }
        }
        for(BatchWeiboTop ob : list2){
            if(!map1.containsKey(ob.getKeyword())){
                keywordsLeave.add(ob.getKeyword()+"("+ob.getRank()+")");
            }
        }
        List<String> keywordsNew10 = new ArrayList<String>();
        List<String> keywordsLeave10 = new ArrayList<String>();
        for(BatchWeiboTop ob : list1ten){
            if(!map2ten.containsKey(ob.getKeyword())){
                keywordsNew10.add(ob.getKeyword()+"("+ob.getRank()+")");
            }
        }
        for(BatchWeiboTop ob : list2ten){
            if(!map1ten.containsKey(ob.getKeyword())){
                keywordsLeave10.add(ob.getKeyword()+"("+ob.getRank()+")");
            }
        }
        //拼html
        StringBuilder sb = new StringBuilder();
        sb.append("<div>").append("Current:").append(nowDateStr).append("|").append("Before:")
                .append(beforeDateStr).append("</div>")
                .append("<div>新进50:(").append(uuid1).append(")</div>");
        sb.append("<table>");
        for(String word : keywordsNew){
            sb.append("<tr>").append("<td>").append(word).append("</td>").append("</tr>");
        }
        sb.append("</table>");
        //
        sb.append("<div>退出50:(").append(uuid2).append(")</div>");
        sb.append("<table>");
        for(String word : keywordsLeave){
            sb.append("<tr>").append("<td>").append(word).append("</td>").append("</tr>");
        }
        sb.append("</table>");
        //
        sb.append("<div>新进10:(").append(uuid2).append(")</div>");
        sb.append("<table>");
        for(String word : keywordsNew10){
            sb.append("<tr>").append("<td>").append(word).append("</td>").append("</tr>");
        }
        sb.append("</table>");
        //
        sb.append("<div>退出10:(").append(uuid2).append(")</div>");
        sb.append("<table>");
        for(String word : keywordsLeave10){
            sb.append("<tr>").append("<td>").append(word).append("</td>").append("</tr>");
        }
        sb.append("</table>");

        Map<String,Object> a = new HashMap<String,Object>();
        a.put("now",nowDateStr);
        a.put("before",beforeDateStr);
        a.put("content",sb.toString());
//        a.put("new50",keywordsNew);
//        a.put("leave50",keywordsLeave);
        return a;
    }

    private List<ModelWrapper> getDataFromHtml(String html) {
        List<ModelWrapper> result = new ArrayList<ModelWrapper>();
        String patt = "<tbody>[\\s\\S]*</tbody>";
        Pattern r = Pattern.compile(patt);
        String line = html;
        Matcher m = r.matcher(line);
        String tableHtml = null;
        while (m.find()) {
            tableHtml = m.group(0);
            break;
        }
        if (tableHtml != null && tableHtml.length() > 0) {
            //找所有以<tr [\s\S]*>[\s\S]*</tr>
            String patt1 = "<tr[\\s\\S]*?>[\\s\\S]*?</tr>";
            Pattern r1 = Pattern.compile(patt1);
            Matcher m1 = r1.matcher(tableHtml);

            String patt2 = "<a href=[\\s\\S]*?>[\\s\\S]*?</a>";
            Pattern r2 = Pattern.compile(patt2);

            String patt3 = "<td class=\"td-01 ranktop\">.*?</td>";
            Pattern r3 = Pattern.compile(patt3);

            String patt4 = "<span>.*?</span>";
            Pattern r4 = Pattern.compile(patt4);

            int i = 0;
            while (m1.find()) {
                String tdHtml = m1.group(0);
                String tdHtml2 = tdHtml.replaceAll("[\\r\\n\\t]", "");
                if (i == 0) {
                    i++;
                    continue;
                }
                Matcher m2 = r2.matcher(tdHtml2);
                Matcher m3 = r3.matcher(tdHtml2);
                Matcher m4 = r4.matcher(tdHtml2);

                ModelWrapper obj = new ModelWrapper();
                if (m2.find()) {
                    String aHtml = m2.group(0);
                    Document doc = Jsoup.parse(aHtml);
                    Elements links = doc.getElementsByTag("a");
                    for (Element link : links) {
                        //String linkHref = link.attr("href");
                        String linkText = link.text();
                        obj.keyWord = linkText;
                        //obj.href = linkHref;
                        break;
                    }
                }
                if (m3.find()) {
                    String aHtml = m3.group(0);
                    obj.rank = aHtml.replaceAll("<td class=\"td-01 ranktop\">","").replace("</td>","");
                }
                if (m4.find()) {
                    String aHtml = m4.group(0);
                    Document doc = Jsoup.parse(aHtml);
                    Elements links = doc.getElementsByTag("span");
                    for (Element link : links) {
                        //String linkHref = link.attr("href");
                        String linkText = link.text();
                        obj.index = linkText;
                        //obj.href = linkHref;
                        break;
                    }
                }
                if (obj.keyWord != null && obj.keyWord.length() > 0) {
                    result.add(obj);
                }
            }
        }
        return result;
    }
}
