package com.pixelstrade.audioguide.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pixelstrade.audioguide.R;
import com.pixelstrade.audioguide.models.Article;
import com.pixelstrade.audioguide.models.ArticleResponse;
import com.pixelstrade.audioguide.realm.RealmController;
import com.pixelstrade.audioguide.rest.ApiClient;
import com.pixelstrade.audioguide.rest.ApiInterface;
import com.pixelstrade.audioguide.ui.adapters.ArticleListAdapter;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MuseumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {



    @BindView(R.id.articleRecyclerView)
    RecyclerView articleRecycler;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;


    //create Realm instance
    private Realm realm;

    //adapter oflist article
    private ArticleListAdapter articleListAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize realm Object
        realm = RealmController.with(this).getRealm();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_museum, container, false);

        ButterKnife.bind(this,rootView);


        //create Article list adapter
        articleListAdapter = new ArticleListAdapter(getActivity(), RealmController.getInstance().getArticles());

        //create layout manager for RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        articleRecycler.setLayoutManager(linearLayoutManager);
        //set adapter to recyclerView
        articleRecycler.setAdapter(articleListAdapter);
        int count = 100; //Declare as inatance variable

        if(getActivity() != null)
        {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if(getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {


                            articleListAdapter = new ArticleListAdapter(getActivity(), RealmController.getInstance().getArticles());
                            articleRecycler.setAdapter(articleListAdapter);
                            articleListAdapter.notifyDataSetChanged();


                            Toast.makeText(getActivity(),"distance updated",Toast.LENGTH_LONG).show();


                        }
                    });
                }
            }, 0, 18120);
        }





        //create swipe Refresh listener
        refreshLayout.setOnRefreshListener(this);

        return rootView;
    }


    @Override
    public void onRefresh() {
        //show refresh indicator
        refreshLayout.setRefreshing(true);
        //get list article

        getArticles();
    }

    private void getArticles() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        //create call request
        Call<ArticleResponse> call = apiInterface.getArticles();
        //execute request
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(@NonNull Call<ArticleResponse> call, @NonNull Response<ArticleResponse> response) {


                refreshLayout.setRefreshing(false);





                //get code response
                if (response.body().getCode() == 200) {


                    //save all article in DB
                    for (Article article :
                            response.body().getData()) {

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(article);
                        realm.commitTransaction();

                    }

                    articleListAdapter.notifyDataSetChanged();


                }
                else
                    Toast.makeText(getActivity(), "Une erreur se produite veuillez réesayer", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(@NonNull Call<ArticleResponse> call, @NonNull Throwable t) {


                refreshLayout.setRefreshing(false);
                Log.e("error",t.getMessage());

                Toast.makeText(getActivity(), "Echec de refresh la liste des Oeuvres ,veuillez réessayer "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
