package com.yu.tools.controller.google;

import com.alibaba.fastjson.JSONObject;
import com.yu.tools.service.google.IFetchGoogleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/google")
public class FetchGoogleNewsController {
    @Resource
    private IFetchGoogleService fetchGoogleService;

    @RequestMapping(value = "/fetchTopGoogle")
    @ResponseBody
    public String fetchTopGoogle(){
        return fetchGoogleService.getNewsHtml(false,null);
    }

    @RequestMapping(value = "/fetchCompare")
    @ResponseBody
    public String fetchCompare(){
        return JSONObject.toJSONString(fetchGoogleService.getCurrentCompareMailHtml());
    }
}
