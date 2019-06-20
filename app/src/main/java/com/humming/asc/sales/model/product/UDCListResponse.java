package com.humming.asc.sales.model.product;

import com.humming.dto.ecatalogResponse.bean.search.UDCList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhtq on 19/4/11.
 */

public class UDCListResponse implements Serializable {
    private List<UDCList> countryValues = new ArrayList<>();
    private List<UDCList> typeValues = new ArrayList<>();
    private List<UDCList> brandValues = new ArrayList<>();
    private List<UDCList> areaValues = new ArrayList<>();
    private List<UDCList> subAreaValues = new ArrayList<>();
    private List<UDCList> sweetValues = new ArrayList<>();
    private List<UDCList> colorValues = new ArrayList<>();
    private List<UDCList> bodyValues = new ArrayList<>();
    private List<UDCList> volumnValues = new ArrayList<>();
    private List<UDCList> colourValues = new ArrayList<>();
    private List<UDCList> vintageValues = new ArrayList<>();
    private List<UDCList> channelMLValues = new ArrayList<>();
    private List<UDCList> channelHKValues = new ArrayList<>();
    private List<UDCList> grapeValues = new ArrayList<>();
    private String priceValue = "";
    private boolean isUpdate = false;

    public UDCListResponse() {
    }

    public List<UDCList> getCountryValues() {
        return countryValues;
    }

    public void setCountryValues(List<UDCList> countryValues) {
        this.countryValues = countryValues;
    }

    public List<UDCList> getTypeValues() {
        return typeValues;
    }

    public void setTypeValues(List<UDCList> typeValues) {
        this.typeValues = typeValues;
    }

    public List<UDCList> getBrandValues() {
        return brandValues;
    }

    public void setBrandValues(List<UDCList> brandValues) {
        this.brandValues = brandValues;
    }

    public List<UDCList> getAreaValues() {
        return areaValues;
    }

    public void setAreaValues(List<UDCList> areaValues) {
        this.areaValues = areaValues;
    }

    public List<UDCList> getSweetValues() {
        return sweetValues;
    }

    public void setSweetValues(List<UDCList> sweetValues) {
        this.sweetValues = sweetValues;
    }

    public List<UDCList> getColorValues() {
        return colorValues;
    }

    public void setColorValues(List<UDCList> colorValues) {
        this.colorValues = colorValues;
    }

    public List<UDCList> getBodyValues() {
        return bodyValues;
    }

    public void setBodyValues(List<UDCList> bodyValues) {
        this.bodyValues = bodyValues;
    }

    public List<UDCList> getVolumnValues() {
        return volumnValues;
    }

    public void setVolumnValues(List<UDCList> volumnValues) {
        this.volumnValues = volumnValues;
    }

    public List<UDCList> getColourValues() {
        return colourValues;
    }

    public void setColourValues(List<UDCList> colourValues) {
        this.colourValues = colourValues;
    }

    public List<UDCList> getVintageValues() {
        return vintageValues;
    }

    public void setVintageValues(List<UDCList> vintageValues) {
        this.vintageValues = vintageValues;
    }

    public List<UDCList> getChannelMLValues() {
        return channelMLValues;
    }

    public void setChannelMLValues(List<UDCList> channelMLValues) {
        this.channelMLValues = channelMLValues;
    }

    public List<UDCList> getChannelHKValues() {
        return channelHKValues;
    }

    public void setChannelHKValues(List<UDCList> channelHKValues) {
        this.channelHKValues = channelHKValues;
    }

    public String getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(String priceValue) {
        this.priceValue = priceValue;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public List<UDCList> getGrapeValues() {
        return grapeValues;
    }

    public void setGrapeValues(List<UDCList> grapeValues) {
        this.grapeValues = grapeValues;
    }

    public List<UDCList> getSubAreaValues() {
        return subAreaValues;
    }

    public void setSubAreaValues(List<UDCList> subAreaValues) {
        this.subAreaValues = subAreaValues;
    }
}
