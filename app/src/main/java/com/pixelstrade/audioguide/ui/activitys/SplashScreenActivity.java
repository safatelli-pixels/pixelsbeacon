package com.pixelstrade.audioguide.ui.activitys;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pixelstrade.audioguide.R;
import com.pixelstrade.audioguide.models.Article;
import com.pixelstrade.audioguide.models.ArticleResponse;
import com.pixelstrade.audioguide.realm.RealmController;
import com.pixelstrade.audioguide.rest.ApiClient;
import com.pixelstrade.audioguide.rest.ApiInterface;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {


    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        //initialize realm instance
        realm = RealmController.with(this).getRealm();
        ///get Articles

    /*    Log.d("aaa","aa");
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        Log.d("aaa","aa");*/
        getArticles();
       // Log.d("aaa","aa");
        //  RealmController.getInstance().getArticles()



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();


            }
        }, 5000);
    }


    private void getArticles() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        //create call request
        Call<ArticleResponse> call = apiInterface.getArticles();
        //execute request
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(@NonNull Call<ArticleResponse> call, @NonNull Response<ArticleResponse> response) {


                //get code response
                if (response.body() != null && response.body().getCode() == 200) {

                    realm.beginTransaction();
                    realm.deleteAll();
                    realm.commitTransaction();
                    //save all article in DB
                    for (Article article : response.body().getData()) {


                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(article);
                        realm.commitTransaction();

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<ArticleResponse> call, @NonNull Throwable t) {

            }
        });
    }
}
