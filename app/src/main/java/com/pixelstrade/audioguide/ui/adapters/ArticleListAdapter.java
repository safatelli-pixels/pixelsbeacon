package com.pixelstrade.audioguide.ui.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixelstrade.audioguide.R;
import com.pixelstrade.audioguide.models.Article;
import com.pixelstrade.audioguide.ui.activitys.DetailsArticleActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pixelstrade.audioguide.rest.ApiClient.BASE_URL;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder>{

    private Context context;
    private List<Article> articleList;

    public ArticleListAdapter(Context context, List<Article> articleList) {
        this.context = context;
        //initialize local article list
        this.articleList = articleList;
    }

    @Override
    public ArticleListAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArticleListAdapter.ArticleViewHolder holder, int position) {


        //set Data to views
        holder.title.setText(articleList.get(position).getName());
        //set article description
       if( articleList.get(position).getiBeacon().getDistance()!= null && !articleList.get(position).getiBeacon().getDistance().equals("0") )
            holder.distance.setText(articleList.get(position).getiBeacon().getDistance().substring(0,5)+"m");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.description.setText(Html.fromHtml(articleList.get(position).getDescription(),Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.description.setText(Html.fromHtml(articleList.get(position).getDescription()));
        }

        if (articleList.get(position).getPhotos().size()>0)
        Picasso.with(context)
                .load(BASE_URL+articleList.get(position).getPhotos().get(0).getImage())
                .into(holder.articleImage);


        //click to articale to show details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailsArticleActivity.class);
                //put code article
                intent.putExtra("id",articleList.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void refresh()
    {
        //clear all previous data
       // this.articleList.clear();
        //re assign data
        //refresh recyclerView
        notifyDataSetChanged();
    }
    public class ArticleViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.distance)
        TextView distance;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.articleImage)
        ImageView articleImage;
        public ArticleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
