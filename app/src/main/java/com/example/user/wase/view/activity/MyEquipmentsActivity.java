package com.example.user.wase.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.user.wase.R;
import com.example.user.wase.model.MyHereAgent;
import com.example.user.wase.model.MyRecord;
import com.example.user.wase.model.MyRoutine;

/**
 * Created by ymbaek on 2016-04-18.
 */
public class MyEquipmentsActivity extends AppCompatActivity {

    public static final String TAG = "MyEquipmentsActivity";
    public static final String TAG_DB = "MyEquipmentsDBTag";


    //Toolbar
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myequipments);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("내 운동 기구");
        toolbar.setNavigationIcon(R.mipmap.ic_menu_arrow_back);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("내 운동 기구");
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        //Database tests
        dbTestCountEntries();

        dbTestInsertAgent();
        dbTestInsertRoutine();
        dbTestInsertRecord();
    }

    /**
     * [Database test] Count currently stored entries
     */
    private void dbTestCountEntries() {
        int agentCount = MainActivity.hereDB.getMyHereAgentCount();
        int routineCount = MainActivity.hereDB.getMyRoutineCount();
        int recordCount = MainActivity.hereDB.getMyRecordCount();

        Log.d(TAG_DB, "[Database] agentCount = " + agentCount);
        Log.d(TAG_DB, "[Database] routineCount = " + routineCount);
        Log.d(TAG_DB, "[Database] recordCount = " + recordCount);
    }

    /**
     * [Database test] Insert sample entries to the table
     */
    private void dbTestInsertAgent() {
        MyHereAgent agent1 = new MyHereAgent();
        MyHereAgent agent2 = new MyHereAgent();
        agent1.setMyeqMacId("ABC");
        agent2.setMyeqMacId("DEF");

        dbTestCountEntries();

        MainActivity.hereDB.insertHereAgent(agent1);
        MainActivity.hereDB.insertHereAgent(agent2);

        dbTestCountEntries();
    }

    /**
     * [Database test] Insert sample routines to the table
     */
    private void dbTestInsertRoutine() {
        MyRoutine routine1 = new MyRoutine();
        MyRoutine routine2 = new MyRoutine();
        routine1.setRoutineId("ABC");
        routine2.setRoutineId("DEF");

        dbTestCountEntries();

        MainActivity.hereDB.insertRoutine(routine1);
        MainActivity.hereDB.insertRoutine(routine2);

        dbTestCountEntries();

    }

    /**
     * [Database test] Insert sample records to the table
     */
    private void dbTestInsertRecord() {
        MyRecord record1 = new MyRecord();
        MyRecord record2 = new MyRecord();
        record1.setRecordId("ABC");
        record2.setRecordId("DEF");

        dbTestCountEntries();

        MainActivity.hereDB.insertRecord(record1);
        MainActivity.hereDB.insertRecord(record2);

        dbTestCountEntries();
    }

//    private void dbTestMyEquipments() {
//        MyHereAgent agent1 = new MyHereAgent();
//
//        MainActivity.hereDB.insertHereAgent()
//    }

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
