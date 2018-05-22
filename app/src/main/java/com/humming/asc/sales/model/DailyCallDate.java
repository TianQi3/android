package com.humming.asc.sales.model;

import java.util.List;

/**
 * Created by Zhtq on 16/3/25.
 */
public class DailyCallDate {
    private Integer dateFalg;
    private List<Integer> dateLists;

    public DailyCallDate() {
    }

    public DailyCallDate(Integer dateFalg, List<Integer> dateLists) {
        this.dateFalg = dateFalg;
        this.dateLists = dateLists;
    }

    public Integer getDateFalg() {
        return dateFalg;
    }

    public void setDateFalg(Integer dateFalg) {
        this.dateFalg = dateFalg;
    }

    public List<Integer> getDateLists() {
        return dateLists;
    }

    public void setDateLists(List<Integer> dateLists) {
        this.dateLists = dateLists;
    }
}
