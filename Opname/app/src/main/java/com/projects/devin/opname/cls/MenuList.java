package com.projects.devin.opname.cls;

import android.graphics.Bitmap;

/**
 * Created by devin on 1/21/2017.
 */

public class MenuList {

    public MenuList(Bitmap image, String title){
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private Bitmap image;
    private String title;
}
