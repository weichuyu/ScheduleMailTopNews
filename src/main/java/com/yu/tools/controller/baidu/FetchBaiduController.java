package com.yu.tools.controller.baidu;

import com.alibaba.fastjson.JSONObject;
import com.yu.tools.service.baidu.IFetchBaiduService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/baidu")
public class FetchBaiduController {
    @Resource
    private IFetchBaiduService fetchBaiduService;

    @RequestMapping(value = "/fetchTopBaidu")
    @ResponseBody
    public String fetchTopBaidu(){
        return fetchBaiduService.getTodayTop(false,null);
    }

    @RequestMapping(value = "/fetchCompare")
    @ResponseBody
    public String fetchCompare(){
        return JSONObject.toJSONString(fetchBaiduService.getCurrentCompareMailHtml());
    }
}
