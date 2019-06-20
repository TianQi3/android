package com.humming.asc.sales.model.product;

import com.humming.dto.ecatalogResponse.bean.product.WhouseList;

/**
 * Created by Zhtq on 19/4/11.
 */

public class WhouseListEntity {
    private WhouseList whouseList;
    private boolean checkSelect = false;

    public WhouseListEntity() {
    }

    public WhouseListEntity(WhouseList whouseList, boolean checkSelect) {
        this.whouseList = whouseList;
        this.checkSelect = checkSelect;
    }

    public WhouseList getWhouseList() {
        return whouseList;
    }

    public void setWhouseList(WhouseList whouseList) {
        this.whouseList = whouseList;
    }

    public boolean isCheckSelect() {
        return checkSelect;
    }

    public void setCheckSelect(boolean checkSelect) {
        this.checkSelect = checkSelect;
    }
}
