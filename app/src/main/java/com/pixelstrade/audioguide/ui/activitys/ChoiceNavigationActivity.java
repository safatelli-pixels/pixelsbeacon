package com.pixelstrade.audioguide.ui.activitys;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.pixelstrade.audioguide.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoiceNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_navigation);


        ButterKnife.bind(this);
    }


    @OnClick(R.id.visitAutonome)
    void clickAutonome()
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @OnClick(R.id.visitWithGuide)
    void clickGuide()
    {

        Toast.makeText(this, "Rubrique en cours de développement", Toast.LENGTH_SHORT).show();
    }
}
