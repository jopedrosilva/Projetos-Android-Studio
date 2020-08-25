package com.withconnection.joope.projetoone.Model;

public class Car {
    private String models;
    private String brands;
    private int photos;

    public Car(){}
    public Car(String models, String brands, int photos) {
        this.models = models;
        this.brands = brands;
        this.photos = photos;
    }

    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public int getPhotos() {
        return photos;
    }

    public void setPhotos(int photos) {
        this.photos = photos;
    }
}
