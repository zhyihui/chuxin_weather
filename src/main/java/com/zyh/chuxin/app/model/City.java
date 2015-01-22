package com.zyh.chuxin.app.model;

/**
 * 市级行政单位实体类
 * Created by zhyh on 2015/1/22.
 */
public class City {
    private int id;
    private String name;
    private String code;
    private int provinceId;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getProvinceId() {
        return provinceId;
    }
}
