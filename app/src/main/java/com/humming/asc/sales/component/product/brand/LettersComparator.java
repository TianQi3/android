package com.humming.asc.sales.component.product.brand;


import com.humming.asc.sales.model.product.ProductDCListEntity;

import java.util.Comparator;

public class LettersComparator implements Comparator<ProductDCListEntity> {

    public int compare(ProductDCListEntity o1, ProductDCListEntity o2) {
        if (o1.getUdcList().getInitials() != null && !"".equals(o1.getUdcList().getInitials())) {
            if (o1.getUdcList().getInitials().equals("@")
                    || o2.getUdcList().getInitials().equals("#")) {
                return 1;
            } else if (o1.getUdcList().getInitials().equals("#")
                    || o2.getUdcList().getInitials().equals("@")) {
                return -1;
            } else {
                return o1.getUdcList().getInitials().compareTo(o2.getUdcList().getInitials());
            }
        } else
            return -1;
    }

}
