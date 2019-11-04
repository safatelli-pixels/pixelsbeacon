package com.pixelstrade.audioguide.models;


import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Article extends RealmObject{

    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String code;
    private String codeBar;
    private String sound;
    private String youtube;
    private Ibeacon iBeacon;
    private RealmList<ArticlePhoto> photos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeBar() {
        return codeBar;
    }

    public void setCodeBar(String codeBar) {
        this.codeBar = codeBar;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public Ibeacon getiBeacon() {
        return iBeacon;
    }

    public void setiBeacon(Ibeacon iBeacon) {
        this.iBeacon = iBeacon;
    }

    public RealmList<ArticlePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(RealmList<ArticlePhoto> photos) {
        this.photos = photos;
    }
}
