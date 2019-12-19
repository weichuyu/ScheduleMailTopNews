package com.yu.tools.controller.weibo;

import com.alibaba.fastjson.JSONObject;
import com.yu.tools.service.weibo.IFetchWeiboService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/weibo")
public class FetchWeiboController {
    @Resource
    private IFetchWeiboService fetchWeiboService;

    @RequestMapping(value = "/fetchTop")
    @ResponseBody
    public String fetchTop(){
        return fetchWeiboService.getTodayTop(false,null);
    }

    @RequestMapping(value = "/fetchCompare")
    @ResponseBody
    public String fetchCompare(){
        return JSONObject.toJSONString(fetchWeiboService.getCurrentCompareMailHtml());
    }
}
