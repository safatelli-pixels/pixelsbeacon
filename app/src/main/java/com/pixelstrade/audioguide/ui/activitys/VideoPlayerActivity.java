package com.pixelstrade.audioguide.ui.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pixelstrade.audioguide.R;
import com.pixelstrade.audioguide.tools.YouTubeHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.jakelee.vidsta.VidstaPlayer;
import uk.co.jakelee.vidsta.listeners.VideoStateListeners;

public class VideoPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private String videoUrl;

    @BindView(R.id.youtube_view)
    YouTubePlayerView youTubePlayerView;

    private static final int RECOVERY_REQUEST = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        ButterKnife.bind(this);

        youTubePlayerView.initialize(getString(R.string.youtube_key), this);

        if (getIntent().hasExtra("url"))
        {
            videoUrl = getIntent().getExtras().getString("url");




        }
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        if (!b) {
            youTubePlayer.cueVideo(YouTubeHelper.extractVideoIdFromUrl(videoUrl)); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            Toast.makeText(this, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(getString(R.string.youtube_key), this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubePlayerView;
    }
}
