package com.yu.tools.schedule;

import com.yu.tools.model.Schedule;
import com.yu.tools.repo.ScheduleRepo;
import com.yu.tools.service.google.IFetchGoogleNewUSService;
import com.yu.tools.service.google.IFetchGoogleService;
import com.yu.tools.util.MyEmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class GoogleScheduler {
    private final Logger logger = LoggerFactory.getLogger(GoogleScheduler.class);
    @Resource
    private ScheduleRepo scheduleRepo;

    @Resource
    private MyEmailUtil myEmailUtil;

    @Resource
    private IFetchGoogleService fetchGoogleService;

    @Resource
    private IFetchGoogleNewUSService fetchGoogleNewUSService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Scheduled(cron="0 0 0/1 * * *")
    public void statusCheck() {
        Date batchCreateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")).getTime();
        String batchId = UUID.randomUUID().toString().replace("-", "");
        try{
            Schedule s = new Schedule();
            s.setBatchcreatetime(batchCreateTime);
            s.setBatchdate(sdf.format(batchCreateTime));
            s.setBatchId(batchId);
            s.setBatchtype("GoogleNews_TW");
            scheduleRepo.save(s);
            fetchGoogleService.getNewsHtml(true,batchId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
        //报表
        try{
            Map<String,Object> a = fetchGoogleService.getCurrentCompareMailHtml();
            String now = (String)a.get("now");
            String before = (String)a.get("before");
            String content = (String)a.get("content");
            myEmailUtil.sendSimpleMail("yutest20180821@outlook.com","yutest20180821@outlook.com","GoogleNews_TW_Compare"+now+"_"+before,content);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    @Scheduled(cron="0 0 0/1 * * *")
    public void statusCheck2() {
        Date batchCreateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")).getTime();
        String batchId = UUID.randomUUID().toString().replace("-", "");
        try{
            Schedule s = new Schedule();
            s.setBatchcreatetime(batchCreateTime);
            s.setBatchdate(sdf.format(batchCreateTime));
            s.setBatchId(batchId);
            s.setBatchtype("GoogleNews_US");
            scheduleRepo.save(s);
            fetchGoogleNewUSService.getNewsHtml(true,batchId);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
        //报表
        try{
            Map<String,Object> a = fetchGoogleNewUSService.getCurrentCompareMailHtml();
            String now = (String)a.get("now");
            String before = (String)a.get("before");
            String content = (String)a.get("content");
            myEmailUtil.sendSimpleMail("yutest20180821@outlook.com","yutest20180821@outlook.com","GoogleNews_US_Compare"+now+"_"+before,content);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
    }
}
