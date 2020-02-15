package com.example.ribbonlauncher;

import android.graphics.drawable.Drawable;

public class AppObject {

    private String name, packageName;
    private Drawable image;
    private int isFavorite;

    public AppObject(String name, String packageName, Drawable image, int isFavorite) {
        this.name = name;
        this.packageName = packageName;
        this.image = image;
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }
}
