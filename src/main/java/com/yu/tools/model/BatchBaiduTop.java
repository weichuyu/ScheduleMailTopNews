package com.yu.tools.model;

import javax.persistence.*;

@Entity
@Table(name = "TBL_BATCH_BAIDU_TOP", uniqueConstraints = {},
        indexes = {@Index(name = "index_batchid", columnList = "batchid")})
public class BatchBaiduTop {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "rank")
    private String rank;

    @Column(name = "ind")
    private String index;

    @Column(name = "batchid")
    private String batchid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }
}
