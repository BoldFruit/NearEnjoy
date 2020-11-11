package com.example.lib_common.imageview.gallery.model;


import androidx.annotation.DrawableRes;

public class GalleryPhotoModel {

    public Object photoSource;

    public GalleryPhotoModel(@DrawableRes int drawableRes) {
        this.photoSource = drawableRes;
    }

    public GalleryPhotoModel(String path) {
        this.photoSource = path;
    }

}
