package com.pixelstrade.audioguide.realm;


import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.pixelstrade.audioguide.models.Article;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {


    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm instance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Article.class
    public void clearAll() {

        realm.beginTransaction();
        realm.delete(Article.class);
        realm.commitTransaction();
    }

    //find all objects in the Article.class
    public RealmResults<Article> getArticles() {

        return realm.where(Article.class).findAll();
    }

    //query a single item with the given id
    public Article getArticleByCode(String code) {

        return realm.where(Article.class).equalTo("code", code).findFirst();
    }

    public Article getArticle(int id) {

        return realm.where(Article.class).equalTo("id", id).findFirst();
    }

    public Article getArticleByBeacon(String uuid,String major,String minor)
    {
        return realm.where(Article.class)
                .equalTo("iBeacon.uuid", uuid)
                .equalTo("iBeacon.major", major)
                .equalTo("iBeacon.minor", minor)
                .findFirst();
    }





    //query example
//    public RealmResults<Article> queryedBooks() {
//
//        return realm.where(Article.class)
//                .contains("author", "Author 0")
//                .or()
//                .contains("title", "Realm")
//                .findAll();
//
//    }
}
