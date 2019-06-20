package com.humming.asc.sales.RequestData;


import java.io.Serializable;

/**
 * Created by Zhtq on 16/8/5.
 */
public class FilterEntity implements Serializable {
    private String[] country;
    private String[] type;
    private String[] brand;
    private String[] area;
    private String[] sweet;
    private String[] color;
    private String[] body;
    private String[] volumn;
    private String[] colour;
    private String[] vintage;
    private String[] channelML;
    private String[] channelHK;
    private String[] subArea;
    private String[] price;
    private String[] grape;

    public FilterEntity() {
    }

    public String[] getCountry() {
        return country;
    }

    public void setCountry(String[] country) {
        this.country = country;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public String[] getBrand() {
        return brand;
    }

    public void setBrand(String[] brand) {
        this.brand = brand;
    }

    public String[] getArea() {
        return area;
    }

    public void setArea(String[] area) {
        this.area = area;
    }

    public String[] getSweet() {
        return sweet;
    }

    public void setSweet(String[] sweet) {
        this.sweet = sweet;
    }

    public String[] getColor() {
        return color;
    }

    public void setColor(String[] color) {
        this.color = color;
    }

    public String[] getBody() {
        return body;
    }

    public void setBody(String[] body) {
        this.body = body;
    }

    public String[] getVolumn() {
        return volumn;
    }

    public void setVolumn(String[] volumn) {
        this.volumn = volumn;
    }

    public String[] getColour() {
        return colour;
    }

    public void setColour(String[] colour) {
        this.colour = colour;
    }

    public String[] getVintage() {
        return vintage;
    }

    public void setVintage(String[] vintage) {
        this.vintage = vintage;
    }

    public String[] getChannelML() {
        return channelML;
    }

    public void setChannelML(String[] channelML) {
        this.channelML = channelML;
    }

    public String[] getChannelHK() {
        return channelHK;
    }

    public void setChannelHK(String[] channelHK) {
        this.channelHK = channelHK;
    }

    public String[] getSubArea() {
        return subArea;
    }

    public void setSubArea(String[] subArea) {
        this.subArea = subArea;
    }

    public String[] getPrice() {
        return price;
    }

    public void setPrice(String[] price) {
        this.price = price;
    }

    public String[] getGrape() {
        return grape;
    }

    public void setGrape(String[] grape) {
        this.grape = grape;
    }
}
