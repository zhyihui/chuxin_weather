package com.zyh.chuxin.app.model;

/**
 * 县级行政单位实体类
 * Created by zhyh on 2015/1/22.
 */
public class Country {
    private int id;
    private String name;
    private String code;
    private int cityId;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
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

    public int getCityId() {
        return cityId;
    }
}
