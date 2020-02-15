package com.example.ribbonlauncher;

import android.graphics.drawable.Drawable;

public class WallpaperObject {

    private String package_name, URL_image;
    private Drawable image;

    public WallpaperObject(String package_name, String URL_image, Drawable image) {
        this.package_name = package_name;
        this.URL_image = URL_image;
        this.image = image;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getURL_image() {
        return URL_image;
    }

    public void setURL_image(String URL_image) {
        this.URL_image = URL_image;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
