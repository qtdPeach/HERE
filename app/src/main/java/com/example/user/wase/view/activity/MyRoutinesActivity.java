package com.example.user.wase.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.user.wase.R;
import com.example.user.wase.model.MyHereAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymbaek on 2016-04-18.
 */
public class MyRoutinesActivity extends AppCompatActivity {

    public static final String TAG = "MyRoutinesActivity";

    private Toolbar toolbar;

    NumberPicker np_set;
    NumberPicker np_count;
    NumberPicker np_time;

    //public static List<MyHereAgent> currentAgents = MainActivity.hereDB.getAllMyHereAgents();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myroutines);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("My Exercise Routines");
        toolbar.setNavigationIcon(R.mipmap.ic_menu_arrow_back);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("My Exercise Routines");
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView) findViewById(R.id.setting_myroutine_list_myroutines);
        ListViewAdapter adapter = new ListViewAdapter();
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //TODO: 임시 구현 리스너
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.dialog_agentgoal, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(MyRoutinesActivity.this);
                builder.setTitle("Set Exercise Goals");
                builder.setView(dialogView);
                builder.setPositiveButton("Add to routine", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Routine을 DB에 추가
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                np_set = (NumberPicker) dialogView.findViewById(R.id.dialog_agentgoal_picker_set);
                np_set.setMaxValue(20);
                np_set.setMinValue(1);
                np_set.setValue(1);
                np_count = (NumberPicker) dialogView.findViewById(R.id.dialog_agentgoal_picker_count);
                np_count.setMaxValue(100);
                np_count.setMinValue(0);
                np_count.setValue(0);
                np_time = (NumberPicker) dialogView.findViewById(R.id.dialog_agentgoal_picker_time);
                np_time.setMaxValue(600);
                np_time.setMinValue(0);
                np_time.setValue(0);

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }


    private AlertDialog askDeletion() {
        AlertDialog myDeleteDialogBox = new AlertDialog.Builder(this)
                .setTitle("Delete a routine")
                .setMessage("Are you sure you want to delete the selected routine(s)?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Delete selected routines
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myDeleteDialogBox;
    }


    public void mOnClick(View v) {
        switch (v.getId()) {
//            case R.id.setting_myroutine_btn_newroutine:
//                LayoutInflater inflater = getLayoutInflater();
//
//                final View dialogView = inflater.inflate(R.layout.dialog_routine, null);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("New Routine");
//                builder.setIcon(R.drawable.nav_icon_myroutines);
//                builder.setView(dialogView);
//                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//                AlertDialog dialog = builder.create();
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.show();
//
//                break;
//            case R.id.setting_myroutine_btn_modifyroutine:
//                break;
            case R.id.setting_myroutine_iv_deleteroutine:
                AlertDialog deleteAlert = askDeletion();
                deleteAlert.setCanceledOnTouchOutside(false);
                deleteAlert.show();
                break;
            case R.id.setting_myroutine_iv_addroutine:
                LayoutInflater inflater = getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.dialog_routine, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add your new routine");
                builder.setView(dialogView);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Routine을 DB에 추가
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
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

    public class ListViewAdapter extends BaseAdapter {

        public List<MyHereAgent> myHereAgents = new ArrayList<MyHereAgent>();

        public ListViewAdapter () {
            super();
            if(MainActivity.hereDB.getAllMyHereAgents() !=null)
                myHereAgents = MainActivity.hereDB.getAllMyHereAgents();
        }

        @Override
        public int getCount() {
                return myHereAgents.size();
        }

        @Override
        public Object getItem(int position) {
            return myHereAgents.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listitem_equipment,parent,false);
            }
            ImageView eqTypeImage = (ImageView) convertView.findViewById(R.id.equiplist_img);
            TextView eqName = (TextView) convertView.findViewById(R.id.equiplist_name);
            TextView eqId = (TextView) convertView.findViewById(R.id.equiplist_id);
            TextView eqSensorType = (TextView) convertView.findViewById(R.id.equiplist_sensorid);

            switch (myHereAgents.get(pos).getMyeqType()) {
                case 0:
                    eqTypeImage.setImageResource(R.mipmap.ic_setting_update_alarm);
                    break;
                case 1:
                    eqTypeImage.setImageResource(R.mipmap.ic_setting_best_interest);
                    break;
                case 2:
                    eqTypeImage.setImageResource(R.mipmap.ic_setting_user_information);
                    break;
                case 3:
                    break;
                default:
                    break;
            }

            eqName.setText(myHereAgents.get(pos).getMyeqName());
            eqId.setText(myHereAgents.get(pos).getMyeqMacId());
            //eqSensorType.setText(registeredAgents.get(pos).getMyeqType());

            return convertView;
        }
    }
}
