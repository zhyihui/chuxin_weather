package com.zyh.chuxin.app.model;

/**
 * 城市实体类, 对应数据库中的city表
 * Created by zhyh on 2015/1/24.
 */
public class CityModel {
    private int id;
    private String name;
    private String code;
    private String provinceName;
    private String allPinYin;
    private String firstPinYin;
    private String allFirstPinYin;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setAllPinYin(String allPinYin) {
        this.allPinYin = allPinYin;
    }

    public void setFirstPinYin(String firstPinYin) {
        this.firstPinYin = firstPinYin;
    }

    public void setAllFirstPinYin(String allFirstPinYin) {
        this.allFirstPinYin = allFirstPinYin;
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

    public String getProvinceName() {
        return provinceName;
    }

    public String getAllPinYin() {
        return allPinYin;
    }

    public String getFirstPinYin() {
        return firstPinYin;
    }

    public String getAllFirstPinYin() {
        return allFirstPinYin;
    }
}
