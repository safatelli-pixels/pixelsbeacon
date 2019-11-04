package com.pixelstrade.audioguide.ui.activitys;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.pixelstrade.audioguide.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoiceLanguageActivity extends AppCompatActivity {

    @BindView(R.id.changeLanguage)
    Button language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_language);

        ButterKnife.bind(this);

    }


    @OnClick(R.id.changeLanguage)
    void changeLanguage()
    {
        language.setText(language.getText().toString().equals(getString(R.string.french))?getString(R.string.english):getString(R.string.french));

    }
    @OnClick(R.id.next)
    void saveLanguage()
    {
        startActivity(new Intent(this,MainActivity.class));


    }
}
