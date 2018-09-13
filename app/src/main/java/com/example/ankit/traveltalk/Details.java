package com.example.ankit.traveltalk;

/**
 * Created by ANKIT on 14-03-2018.
 */

public class Details {
    public String name;
    public String image;
    public String status;
    public String thumbnail_image;



    public Details(){

    }

    public Details(String name, String image, String status, String thumbnail_image) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumbnail_image = thumbnail_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb_image() {
        return thumbnail_image;
    }

    public void setThumb_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

}
