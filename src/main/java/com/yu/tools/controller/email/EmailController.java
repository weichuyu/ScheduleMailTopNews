package com.yu.tools.controller.email;

import com.yu.tools.service.baidu.IFetchBaiduService;
import com.yu.tools.service.google.IFetchGoogleNewUSService;
import com.yu.tools.service.google.IFetchGoogleService;
import com.yu.tools.service.weibo.IFetchWeiboService;
import com.yu.tools.util.MyEmailUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping(value = "/email")
public class EmailController {
    @Resource
    private IFetchBaiduService fetchBaiduService;

    @Resource
    private MyEmailUtil myEmailUtil;

    @Resource
    private IFetchGoogleService fetchGoogleService;

    @Resource
    private IFetchGoogleNewUSService fetchGoogleNewUSService;

    @Resource
    private IFetchWeiboService fetchWeiboService;

    @RequestMapping(value = "/sendSimpleEmail")
    @ResponseBody
    public String sendSimpleEmail(){
        return myEmailUtil.sendSimpleMail("yutest20180821@outlook.com","yutest20180821@outlook.com","Test","<div>测试</div>").toString();
    }

    @RequestMapping(value = "/sendSimpleEmailBaidu")
    @ResponseBody
    public String sendSimpleEmail2(){
        Map<String,Object> a = fetchBaiduService.getCurrentCompareMailHtml();
        String now = (String)a.get("now");
        String before = (String)a.get("before");
        String content = (String)a.get("content");
        return myEmailUtil.sendSimpleMail("yutest20180821@outlook.com","yutest20180821@outlook.com","BaiduTopRealTime_Compare"+now+"_"+before,content).toString();
    }

    @RequestMapping(value = "/sendSimpleEmailGoogleTW")
    @ResponseBody
    public String sendSimpleEmail3(){
        Map<String,Object> a = fetchGoogleService.getCurrentCompareMailHtml();
        String now = (String)a.get("now");
        String before = (String)a.get("before");
        String content = (String)a.get("content");
        return myEmailUtil.sendSimpleMail("yutest20180821@outlook.com","yutest20180821@outlook.com","GoogleNews_TW_Compare"+now+"_"+before,content).toString();
    }

    @RequestMapping(value = "/sendSimpleEmailUS")
    @ResponseBody
    public String sendSimpleEmail4(){
        Map<String,Object> a = fetchGoogleNewUSService.getCurrentCompareMailHtml();
        String now = (String)a.get("now");
        String before = (String)a.get("before");
        String content = (String)a.get("content");
        return myEmailUtil.sendSimpleMail("yutest20180821@outlook.com","yutest20180821@outlook.com","GoogleNews_US_Compare"+now+"_"+before,content).toString();
    }

    @RequestMapping(value = "/sendSimpleEmailWeibo")
    @ResponseBody
    public String sendSimpleEmail5(){
        Map<String,Object> a = fetchWeiboService.getCurrentCompareMailHtml();
        String now = (String)a.get("now");
        String before = (String)a.get("before");
        String content = (String)a.get("content");
        return myEmailUtil.sendSimpleMail("yutest20180821@outlook.com","yutest20180821@outlook.com","WeiboTopRealTime_Compare"+now+"_"+before,content).toString();
    }
}
