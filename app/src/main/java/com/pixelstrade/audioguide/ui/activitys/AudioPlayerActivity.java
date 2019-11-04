package com.pixelstrade.audioguide.ui.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pixelstrade.audioguide.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.jakelee.vidsta.VidstaPlayer;
import uk.co.jakelee.vidsta.listeners.VideoStateListeners;

public class AudioPlayerActivity extends AppCompatActivity {

    private String videoUrl;

    @BindView(R.id.player)
    VidstaPlayer vidstaPlayer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        ButterKnife.bind(this);
        if (getIntent().hasExtra("url"))
        {
            videoUrl = getIntent().getExtras().getString("url");

            vidstaPlayer.setVideoSource(videoUrl);
            vidstaPlayer.setAutoPlay(false);


            vidstaPlayer.setFullScreen(true);


        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        vidstaPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        vidstaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vidstaPlayer.stop();
    }
}
