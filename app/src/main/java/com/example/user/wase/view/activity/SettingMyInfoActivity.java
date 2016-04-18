package com.example.user.wase.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.user.wase.R;

/**
 * Created by ymbaek on 2016-04-18.
 */
public class SettingMyInfoActivity extends AppCompatActivity {

    public static final String TAG = "SettingMyInfoActivity";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_myinfo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("내 정보 설정");
        toolbar.setNavigationIcon(R.mipmap.ic_menu_arrow_back);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("내 정보 설정");
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
