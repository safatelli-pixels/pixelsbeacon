package com.pixelstrade.audioguide.ui.adapters;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pixelstrade.audioguide.R;
import com.pixelstrade.audioguide.models.ArticlePhoto;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class ViewPagerImageAdapter extends PagerAdapter {

    public static final String BASE_URL = "http://vps394514.ovh.net:86/";

    // Declare Variables
    Context context;
    RealmList<ArticlePhoto> photos;
    LayoutInflater inflater;

    public ViewPagerImageAdapter(Context context, RealmList<ArticlePhoto> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables
        ImageView imgflag;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,
                false);

        // Locate the ImageView in viewpager_item.xml
        imgflag = (ImageView) itemView.findViewById(R.id.flag);
        // Capture position and set to the ImageView

        Picasso.with(context)
                .load(BASE_URL+photos.get(position).getImage())
                .into(imgflag);

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((ImageView) object);

    }
}
