package com.humming.asc.sales.model.product;

import com.humming.dto.ecatalogResponse.bean.search.UDCList;

/**
 * Created by Zhtq on 19/4/11.
 */

public class ProductDCListEntity {
    private UDCList udcList;
    private boolean checkSelect = false;

    public UDCList getUdcList() {
        return udcList;
    }

    public void setUdcList(UDCList udcList) {
        this.udcList = udcList;
    }

    public boolean isCheckSelect() {
        return checkSelect;
    }

    public void setCheckSelect(boolean checkSelect) {
        this.checkSelect = checkSelect;
    }
}
