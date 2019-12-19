package com.yu.tools.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_BATCH_GOOGLENEWS_US", uniqueConstraints = {},
        indexes = {@Index(name = "index_batchid", columnList = "batchid")})
public class BatchGoogleNews_US {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "rank")
    private String rank;

    @Column(name = "publish")
    private String publish;

    @Column(name = "batchid")
    private String batchid;

    @Column(name = "datetime")
    private Date datetime;

    @Column(name = "datestrutc")
    private String datestrutc;

    public String getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getRank() {
        return rank;
    }

    public String getPublish() {
        return publish;
    }

    public String getBatchid() {
        return batchid;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getDatestrutc() {
        return datestrutc;
    }

    public void setDatestrutc(String datestrutc) {
        this.datestrutc = datestrutc;
    }
}
