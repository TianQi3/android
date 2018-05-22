package com.humming.asc.sales.model;

/**
 * Created by Zhtq on 16/2/16.
 */
public class PositionCoditionItem {
    private String rowId;
    private boolean select;
    private String positionName;


    public PositionCoditionItem() {
        super();
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
