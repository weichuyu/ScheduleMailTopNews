package com.yu.tools.service.google;

import java.util.Map;

/**
 *
 */
public interface IFetchGoogleNewUSService {
    /**
     *
     * @param writeDataBase
     * @param batchId
     * @return
     */
    public String getNewsHtml(Boolean writeDataBase, String batchId);

    /**
     * 比较最后两次数据差
     * @return
     */
    public Map<String,Object> getCurrentCompareMailHtml();
}
