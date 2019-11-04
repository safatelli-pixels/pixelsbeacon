package com.pixelstrade.audioguide.models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ArticlePhoto extends RealmObject{

    @PrimaryKey
    private int id;
    private String image;
    private String ordre;
    private boolean visible;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrdre() {
        return ordre;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
