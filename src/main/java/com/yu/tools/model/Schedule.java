package com.yu.tools.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_SCHEDULE", uniqueConstraints = {},
        indexes = {@Index(name = "index_batchtype", columnList = "batchtype"),
                @Index(name = "index_batchdate", columnList = "batchdate")
        })
public class Schedule {
    @Id
    @Column(name = "batchid")
    private String batchId;

    @Column(name = "batchdate")
    private String  batchdate;

    @Column(name = "batchcreatetime")
    private Date batchcreatetime;

    @Column(name = "batchtype")
    private String batchtype;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchdate() {
        return batchdate;
    }

    public void setBatchdate(String batchdate) {
        this.batchdate = batchdate;
    }

    public Date getBatchcreatetime() {
        return batchcreatetime;
    }

    public void setBatchcreatetime(Date batchcreatetime) {
        this.batchcreatetime = batchcreatetime;
    }

    public String getBatchtype() {
        return batchtype;
    }

    public void setBatchtype(String batchtype) {
        this.batchtype = batchtype;
    }
}
