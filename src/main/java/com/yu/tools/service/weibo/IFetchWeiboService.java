package com.yu.tools.service.weibo;

import java.util.Map;

/**
 * yuweichu 2019/10/16
 */
public interface IFetchWeiboService {
    /**
     * 从
     * https://s.weibo.com/top/summary?cate=realtimehot
     * 获取今日热点列表
     * @return
     */
    public String getTodayTop(Boolean writeDataBase, String batchId);

    /**
     * 比较最后两次数据差
     * @return
     */
    public Map<String,Object> getCurrentCompareMailHtml();
}
