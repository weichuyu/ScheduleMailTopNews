package com.yu.tools.schedule;

import com.yu.tools.model.Schedule;
import com.yu.tools.repo.ScheduleRepo;
import com.yu.tools.service.baidu.IFetchBaiduService;
import com.yu.tools.util.MyEmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class BaiduScheduler {
    private final Logger logger = LoggerFactory.getLogger(BaiduScheduler.class);
    @Resource
    private ScheduleRepo scheduleRepo;

    @Resource
    private MyEmailUtil myEmailUtil;

    @Resource
    private IFetchBaiduService fetchBaiduService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Scheduled(cron="0 0 0/1 * * *")
//    @Scheduled(cron="0 */1 * * * ?")
    public void statusCheck() {
        Date batchCreateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")).getTime();
        String batchId = UUID.randomUUID().toString().replace("-", "");
        try{
            Schedule s = new Schedule();
            s.setBatchcreatetime(batchCreateTime);
            s.setBatchdate(sdf.format(batchCreateTime));
            s.setBatchId(batchId);
            s.setBatchtype("BaiduTopRealTime");
            scheduleRepo.save(s);

            fetchBaiduService.getTodayTop(true,batchId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
        //报表
        try{
            Map<String,Object>  a = fetchBaiduService.getCurrentCompareMailHtml();
            String now = (String)a.get("now");
            String before = (String)a.get("before");
            String content = (String)a.get("content");
            myEmailUtil.sendSimpleMail("yutest20180821@outlook.com","yutest20180821@outlook.com","BaiduTopRealTime_Compare"+now+"_"+before,content);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
    }
}
