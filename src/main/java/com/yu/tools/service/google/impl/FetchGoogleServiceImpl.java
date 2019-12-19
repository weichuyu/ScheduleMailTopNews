package com.yu.tools.service.google.impl;

import com.alibaba.fastjson.JSONObject;
import com.yu.tools.model.BatchGoogleNews_TW;
import com.yu.tools.model.Schedule;
import com.yu.tools.repo.BatchGoogleNews_TWRepo;
import com.yu.tools.repo.ScheduleRepo;
import com.yu.tools.service.google.IFetchGoogleService;
import com.yu.tools.util.MyHttpClientUtil;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * yuweichu 2019/6/4 拉取google页面或接口的返回
 */
@Service
public class FetchGoogleServiceImpl implements IFetchGoogleService {
    private static Logger logger = Logger.getLogger(FetchGoogleServiceImpl.class);
    @Resource
    private MyHttpClientUtil myHttpClientUtil;

    @Resource
    private BatchGoogleNews_TWRepo batchGoogleNews_TWRepo;

    @Resource
    private ScheduleRepo scheduleRepo;

    private class ModelWrapper {
        public String rank;
        public String keyWord;
        public String brief;
        public String publish;
        public Date dateTime;
        public String dateTimeOrigin;
    }

    private class ResultWrapper {
        public String errorCode;
        public List<ModelWrapper> data;
        public String errorMsg;
    }

    /**
     * https://news.google.com/topics/CAAqKggKIiRDQkFTRlFvSUwyMHZNRFZxYUdjU0JYcG9MVlJYR2dKVVZ5Z0FQAQ?hl=zh-TW&gl=TW&ceid=TW%3Azh-Hant
     * @param writeDataBase
     * @param batchId
     * @return
     */
    @Override
    public String getNewsHtml(Boolean writeDataBase, String batchId) {
        String html = myHttpClientUtil.getDataFromURL_GET("https://news.google.com/topics/CAAqKggKIiRDQkFTRlFvSUwyMHZNRFZxYUdjU0JYcG9MVlJYR2dKVVZ5Z0FQAQ?hl=zh-TW&gl=TW&ceid=TW%3Azh-Hant", "GBK",false);

        //String patt = "<h3 class=\"ipQwMb ekueJc RD0gLb\">[\\s\\S]*?</h3>";

        String patt = "<article [\\s\\S]*? >[\\s\\S]*?</article>";

        Pattern r = Pattern.compile(patt);
        String line = html;
        Matcher m = r.matcher(line);
        List<String> tableHtmls = new ArrayList<String>();
        //String tableHtml = "";
        while (m.find()) {
            //tableHtml += m.group(0) + "\r\n";
            tableHtmls.add(m.group(0));
            //logger.info(tableHtml);
        }
        ResultWrapper result = this.parseHtmlToJson(tableHtmls);

        if (writeDataBase && result != null && result.data!=null) {
            for (FetchGoogleServiceImpl.ModelWrapper ob : result.data) {
                BatchGoogleNews_TW s = new BatchGoogleNews_TW();
                s.setBatchid(batchId);
                s.setId(UUID.randomUUID().toString().replace("-", ""));
                s.setKeyword(ob.keyWord);
                s.setRank(ob.rank);
                s.setDatetime(ob.dateTime);
                s.setPublish(ob.publish);
                s.setDatestrutc(ob.dateTimeOrigin);
                batchGoogleNews_TWRepo.save(s);
            }
        }
        return JSONObject.toJSONString(result);
    }
    private ResultWrapper parseHtmlToJson(List<String> tableHtmls){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        ResultWrapper result = new ResultWrapper();
        List<ModelWrapper> list = new ArrayList<ModelWrapper>();
        int i = 1;
        for(String aHtml : tableHtmls){
            ModelWrapper vo = new ModelWrapper();
            Document doc = Jsoup.parse(aHtml);
            //获取文章时间
            {
                Elements times = doc.getElementsByTag("time");
                for (Element link : times) {
                    //String linkHref = link.attr("href");
                    String linkText = link.attr("datetime");
                    vo.dateTimeOrigin = linkText;
                    try{
                        Date a = df.parse(linkText);
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
                        calendar.setTime(a);
                        vo.dateTime = calendar.getTime();
                    }catch(Exception e){
                        logger.error(linkText+":"+e.getMessage(),e);
                    }
                    break;
                }
            }
            //获取文章摘要
            {
                Elements times = doc.getElementsByClass("xBbh9");
                for (Element link : times) {
                    //String linkHref = link.attr("href");
                    String linkText = link.text();
                    //vo.brief = linkText;
                    break;
                }
            }
            //获取文章标题
            {
                Elements times = doc.getElementsByClass("DY5T1d");
                for (Element link : times) {
                    //String linkHref = link.attr("href");
                    String linkText = link.text();
                    vo.keyWord = linkText;
                    break;
                }
            }
            //获取文章发布人
            {
                Elements times = doc.getElementsByClass("wEwyrc AVN2gc uQIVzc Sksgp");
                for (Element link : times) {
                    //String linkHref = link.attr("href");
                    String linkText = link.text();
                    vo.publish = linkText;
                    break;
                }
            }
            vo.rank = i+"";
            list.add(vo);
            i++;
        }
        result.data = list;
        result.errorCode = "0000";
        result.errorMsg = "";
        return result;
    }

    @Override
    public Map<String,Object> getCurrentCompareMailHtml() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
        Sort sort = new Sort(Sort.Direction.DESC, "batchcreatetime"); //创建时间降序排序
        Pageable pageable = new PageRequest(0, 2, sort);
        Schedule query = new Schedule();
        query.setBatchtype("GoogleNews_TW");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.ENDING);
        Example<Schedule> example = Example.of(query, matcher);
        Page<Schedule> list = scheduleRepo.findAll(example, pageable);
        List<Schedule> result = list.getContent();
        if (result == null || result.size() < 2) {
            return null;
        }
        String uuid1 = null;
        String uuid2 = null;
        List<BatchGoogleNews_TW> list1 = null;
        List<BatchGoogleNews_TW> list2 = null;
        ZoneId zoneId = ZoneId.systemDefault();
        String nowDateStr = null;
        String beforeDateStr = null;
        Date now = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")).getTime();
        for (int i = 0; i < 2; i++) {
            if (result.get(i) == null || result.get(i).getBatchId() == null || result.get(i).getBatchId().length() <= 0) {
                return null;
            }
            Instant instant = result.get(i).getBatchcreatetime().toInstant();
            LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
            String str = df.format(localDateTime);
            List<BatchGoogleNews_TW> temp = batchGoogleNews_TWRepo.findByBatchid(result.get(i).getBatchId());
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
        Map<String,Boolean> map1 = new HashMap<String,Boolean>();
        Map<String,Boolean> map2 = new HashMap<String,Boolean>();
        for(BatchGoogleNews_TW ob : list1){
            map1.put(ob.getKeyword(),true);
        }
        for(BatchGoogleNews_TW ob : list2){
            map2.put(ob.getKeyword(),true);
        }

        for(BatchGoogleNews_TW ob : list1){
            if(!map2.containsKey(ob.getKeyword())){
                if (myHttpClientUtil.diffDate(now,ob.getDatetime())>6) {
                    //超过12小时，不再收录
                    continue;
                }
                keywordsNew.add(ob.getKeyword()+"("+ob.getRank()+") "+ob.getPublish()+" ["+df2.format(ob.getDatetime())+"]");
            }
        }
        for(BatchGoogleNews_TW ob : list2){
            if(!map1.containsKey(ob.getKeyword())){
                if (myHttpClientUtil.diffDate(now,ob.getDatetime())>6) {
                    //超过12小时，不再收录
                    continue;
                }
                keywordsLeave.add(ob.getKeyword()+"("+ob.getRank()+") "+ob.getPublish()+" ["+df2.format(ob.getDatetime())+"]");
            }
        }
        //拼html
        StringBuilder sb = new StringBuilder();
        sb.append("<div>").append("Current:").append(nowDateStr).append("|").append("Before:")
                .append(beforeDateStr).append("</div>")
                .append("<div style=color:#F00>新进（全量，剔除发布时间超过6小时的）["+keywordsNew.size()+"条]:(").append(uuid1).append(")</div>");
        sb.append("<table>");
        for(String word : keywordsNew){
            sb.append("<tr>").append("<td>").append(word).append("</td>").append("</tr>");
        }
        sb.append("</table>");
        //
        sb.append("<div style=color:#F00 >退出（全量，剔除发布时间超过6小时的）["+keywordsLeave.size()+"]:(").append(uuid2).append(")</div>");
        sb.append("<table>");
        for(String word : keywordsLeave){
            sb.append("<tr>").append("<td>").append(word).append("</td>").append("</tr>");
        }
        sb.append("</table>");
        //
        Map<String,Object> a = new HashMap<String,Object>();
        a.put("now",nowDateStr);
        a.put("before",beforeDateStr);
        a.put("content",sb.toString());
        return a;
    }
}
