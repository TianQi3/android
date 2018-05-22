package com.humming.asc.sales.model;

/**
 * Created by Zhtq on 16/2/16.
 */
public class ConditionItem {
    private String name;
    private boolean select;

    public ConditionItem() {
        super();
    }
    public ConditionItem(String name, boolean select) {
        this.name = name;
        this.select = select;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
