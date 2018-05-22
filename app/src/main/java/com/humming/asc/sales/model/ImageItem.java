package com.humming.asc.sales.model;

/**
 * Created by Zhtq on 16/2/16.
 */
public class ImageItem {
    private String path;
    private boolean select;

    public ImageItem() {
        super();
    }
    public ImageItem(String path,boolean select) {
        this.path = path;
        this.select = select;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
