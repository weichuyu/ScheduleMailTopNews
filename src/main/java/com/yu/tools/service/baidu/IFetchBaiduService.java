package com.yu.tools.service.baidu;

import java.util.Date;
import java.util.Map;

/**
 * yuweichu 2018/6/5
 */
public interface IFetchBaiduService {
    /**
     * 从
     * http://top.baidu.com/buzz?b=341&c=513&fr=topbuzz_b42_c513
     * 获取今日热点列表
     * @return
     */
    public String getTodayTop(Boolean writeDataBase,String batchId);

    /**
     * 比较最后两次数据差
     * @return
     */
    public Map<String,Object> getCurrentCompareMailHtml();
}
