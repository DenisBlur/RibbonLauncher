package com.example.ribbonlauncher;

import android.graphics.drawable.Drawable;

public class AppObject {

    private String name, packageName;
    private Drawable image;
    private int type, isFavorite, position, positionFav;

    AppObject(String name, String packageName, Drawable image, int type, int position) {
        this.name = name;
        this.packageName = packageName;
        this.image = image;
        this.type = type;
        this.position = position;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPositionFav() {
        return positionFav;
    }

    public void setPositionFav(int positionFav) {
        this.positionFav = positionFav;
    }
}
