package com.jmtc.file2chain.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Chris
 * @date 2021/6/1 21:20
 * @Email:gang.wu@nexgaming.com
 */
public class LogisticInfo {
    @ApiModelProperty(notes = "Logistic id")
    private String id;

    @ApiModelProperty(notes = "status",required = true)
    private Integer status;

    @ApiModelProperty(notes = "date",required = true)
    private String date;

    @ApiModelProperty(notes = "desc",required = true)
    private String desc;

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
