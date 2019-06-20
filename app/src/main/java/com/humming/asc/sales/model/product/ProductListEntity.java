package com.humming.asc.sales.model.product;

import com.humming.dto.ecatalogResponse.bean.product.ProductList;
import com.humming.dto.ecatalogResponse.bean.product.StockDetailList;

import java.util.List;

/**
 * Created by Zhtq on 19/4/11.
 */

public class ProductListEntity {
    private ProductList productList;
    private List<StockDetailList> stockLists;
    private Boolean collection;
    private int checkSelect;//1选中 2未选中

    public ProductList getProductList() {
        return productList;
    }

    public void setProductList(ProductList productList) {
        this.productList = productList;
    }

    public int getCheckSelect() {
        return checkSelect;
    }

    public void setCheckSelect(int checkSelect) {
        this.checkSelect = checkSelect;
    }

    public ProductListEntity() {
    }

    public List<StockDetailList> getStockLists() {
        return stockLists;
    }

    public void setStockLists(List<StockDetailList> stockLists) {
        this.stockLists = stockLists;
    }

    public Boolean getCollection() {
        return collection;
    }

    public void setCollection(Boolean collection) {
        this.collection = collection;
    }
}
