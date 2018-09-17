package com.example.cy.cody_;


import android.graphics.drawable.Drawable;

public class ListViewItem {

    private Drawable resId;
    private String title;
    private String content;

    public Drawable getIcon() {
        return resId;
    }

    public void setIcon(Drawable resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return content;
    }

    public void setDesc(String content) {
        this.content = content;
    }
}

