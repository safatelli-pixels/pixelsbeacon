package com.pixelstrade.audioguide.ui.activitys;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.pixelstrade.audioguide.R;
import com.pixelstrade.audioguide.models.Article;
import com.pixelstrade.audioguide.realm.RealmController;
import com.pixelstrade.audioguide.ui.adapters.ViewPagerImageAdapter;
import com.rd.PageIndicatorView;
import com.rd.animation.AnimationType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.pixelstrade.audioguide.rest.ApiClient.BASE_URL;

public class DetailsArticleActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager mvViewPager;
    @BindView(R.id.pageIndicator)
    PageIndicatorView pageIndicatorView;

    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.fabMenu)
    FloatingActionMenu fabMenue;


    private Article article;

    //find toolbar & title bar
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.titleToolbar)
    TextView mTitleToolbar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_article);

        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);



        //enable return arrow button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //get article from Intent
        if (getIntent().hasExtra("code"))
        {
            String code = getIntent().getExtras().getString("code");

            //get Article by id from DB

            article = RealmController.with(this).getArticleByCode(code);

            setData();



        }else if (getIntent().hasExtra("id"))
        {
            int id = getIntent().getExtras().getInt("id");

            //get Article by id from DB

            article = RealmController.with(this).getArticle(id);


            setData();
        }

        else {

            Toast.makeText(this, "Article non trouvé veuillez réesayer !", Toast.LENGTH_SHORT).show();
        }



    }

    private void setData()
    {

        if (article != null) {


            //set title toolbar
            mTitleToolbar.setText(article.getName());

            //create view pager adapter
            ViewPagerImageAdapter viewPagerImageAdapter = new ViewPagerImageAdapter(this, article.getPhotos());


            mvViewPager.setAdapter(viewPagerImageAdapter);

            //set up viewPager indicator
            pageIndicatorView.setViewPager(mvViewPager);

            pageIndicatorView.setSelectedColor(ContextCompat.getColor(this, R.color.colorPrimary));
            pageIndicatorView.setUnselectedColor(ContextCompat.getColor(this, R.color.grayItemMenu));
            pageIndicatorView.setAnimationType(AnimationType.WORM);


            //set article description
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                description.setText(Html.fromHtml(article.getDescription(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                description.setText(Html.fromHtml(article.getDescription()));
            }

        }else {

            fabMenue.setVisibility(View.GONE);
            Toast.makeText(this, "Veuillez verifier le code de l'article", Toast.LENGTH_SHORT).show();

        }


    }

    @OnClick(R.id.readAudio)
    void readSound()
    {

        if (article !=  null)
        {
            //Read sound in article
            //Read video in article
            Intent intent = new Intent(this,AudioPlayerActivity.class);
            intent.putExtra("url",BASE_URL+article.getSound());
            intent.putExtra("sound","");

            startActivity(intent);

        }
    }

    @OnClick(R.id.readVideo)
    void readVideo()
    {

        if (article !=  null)
        {
            //Read video in article
            Intent intent = new Intent(this,VideoPlayerActivity.class);
            intent.putExtra("url",article.getYoutube());
            startActivity(intent);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
