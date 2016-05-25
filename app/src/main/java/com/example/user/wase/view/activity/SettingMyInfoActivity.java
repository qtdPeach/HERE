package com.example.user.wase.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.wase.R;
import com.example.user.wase.model.MyInformation;

/**
 * Created by ymbaek on 2016-04-18.
 */
public class SettingMyInfoActivity extends AppCompatActivity {

    public static final String TAG = "SettingMyInfoActivity";

    private Toolbar toolbar;

    ImageView settingMyInfo_iv_img;

    TextView settingMyInfo_tv_nick;
    TextView settingMyInfo_tv_id;
    TextView settingMyInfo_tv_name;

    EditText settingMyInfo_et_id;
    EditText settingMyInfo_et_nick;
    EditText settingMyInfo_et_name;
    EditText settingMyInfo_et_age;
    EditText settingMyInfo_et_sex;
    EditText settingMyInfo_et_height;
    EditText settingMyInfo_et_weight;

    TextView settingMyInfo_tv_deviceid;

    Button settingMyInfo_btn_save;


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

        initWidgets();


    }


    private void initWidgets() {
        settingMyInfo_iv_img = (ImageView) findViewById(R.id.setting_myinfo_iv_character);

        settingMyInfo_tv_nick = (TextView) findViewById(R.id.setting_myinfo_tv_nick);
        settingMyInfo_tv_id = (TextView) findViewById(R.id.setting_myinfo_tv_id);
        settingMyInfo_tv_name = (TextView) findViewById(R.id.setting_myinfo_tv_name);

        settingMyInfo_et_id = (EditText) findViewById(R.id.setting_myinfo_et_id);
        settingMyInfo_et_nick = (EditText) findViewById(R.id.setting_myinfo_et_nick);
        settingMyInfo_et_name = (EditText) findViewById(R.id.setting_myinfo_et_name);
        settingMyInfo_et_age = (EditText) findViewById(R.id.setting_myinfo_et_age);
        settingMyInfo_et_sex = (EditText) findViewById(R.id.setting_myinfo_et_sex);
        settingMyInfo_et_height = (EditText) findViewById(R.id.setting_myinfo_et_height);
        settingMyInfo_et_weight = (EditText) findViewById(R.id.setting_myinfo_et_weight);

        settingMyInfo_tv_deviceid = (TextView) findViewById(R.id.setting_myinfo_tv_deviceid);

        settingMyInfo_btn_save = (Button) findViewById(R.id.setting_myinfo_btn_save);
    }

    private void clearEditText() {
        settingMyInfo_et_id.setText("");
        settingMyInfo_et_nick.setText("");
        settingMyInfo_et_name.setText("");
        settingMyInfo_et_age.setText("");
        settingMyInfo_et_sex.setText("");
        settingMyInfo_et_height.setText("");
        settingMyInfo_et_weight.setText("");
    }


    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.setting_myinfo_btn_save:
                MyInformation myInfo = new MyInformation();

                String userId = settingMyInfo_et_id.getText().toString();
                String userNick = settingMyInfo_et_nick.getText().toString();
                String userName = settingMyInfo_et_name.getText().toString();
                int userAge = Integer.parseInt(settingMyInfo_et_age.getText().toString());
                int userSex = Integer.parseInt(settingMyInfo_et_sex.getText().toString());
                int userHeight = Integer.parseInt(settingMyInfo_et_height.getText().toString());
                int userWeight = Integer.parseInt(settingMyInfo_et_weight.getText().toString());

                myInfo.setUserId(userId);
                myInfo.setUserNick(userNick);
                myInfo.setUserName(userName);
                myInfo.setUserAge(userAge);
                myInfo.setUserSex(userSex);
                myInfo.setUserHeight(userHeight);
                myInfo.setUserWeight(userWeight);

                String storedInfo = "";

                storedInfo = "USER INFO\n===============\n" +
                        "- user_id: " + userId + "\n" +
                        "- user_nick: " + userNick + "\n" +
                        "- user_name: " + userName + "\n" +
                        "- user_age: " + userAge + "\n" +
                        "- user_sex: " + userSex + "\n" +
                        "- user_height: " + userHeight + "\n" +
                        "- user_weight: " + userWeight;

                Toast.makeText(getApplicationContext(), storedInfo, Toast.LENGTH_SHORT).show();


                break;
        }
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
