package com.example.user.wase.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.wase.R;
import com.example.user.wase.controller.PagerAdapter;
import com.example.user.wase.device.BluetoothComm;
import com.example.user.wase.device.WorkoutRecord;
import com.example.user.wase.device.WorkoutRecords;
import com.example.user.wase.model.MyHereAgent;
import com.example.user.wase.model.MyInformation;
import com.example.user.wase.model.MyRecord;
import com.example.user.wase.model.MyRoutine;
import com.example.user.wase.utility.DatabaseHelper;
import com.example.user.wase.view.fragment.SupportHelpFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivityTag";
    private static final String TAG_DB = "MainActivityDBTag";

    public static DatabaseHelper hereDB;

    //Global variables
    public static ArrayList<MyHereAgent> myConnectedAgents; //My connected agents
    public static MyHereAgent mySelectedAgent;              //My currently selected agent (exercising)
    public static MyRoutine mySelectedRoutine;              //My currently selected routine
    public static MyRecord myCurrentRecord;                   //My today's record (start-end)

    private TabLayout tabLayout;
    private ViewPager viewPager;


    // Modification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Initialize global variables */
        myConnectedAgents = null;       //My connected agents
        mySelectedAgent = null;         //My currently selected agent (exercising)
        mySelectedRoutine = null;       //My currently selected routine
        myCurrentRecord = null;         //My today's record (start-end)

        /* Initialize Database */
        hereDB = new DatabaseHelper(getApplicationContext());

        if (hereDB != null) {
            Log.d(TAG_DB, "[Database] DatabaseHelper is created.");
        }

        tabLayout = (TabLayout) findViewById(R.id.main_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("연결 기기"));
        tabLayout.addTab(tabLayout.newTab().setText("운동 설정"));
        tabLayout.addTab(tabLayout.newTab().setText("운동 기록"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        final PagerAdapter pagerAdapter = new PagerAdapter(
                getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        System.out.println("Modification test");

        System.out.print("GOOD");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Please select your routine first", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Toast.makeText(getApplicationContext(), "START EXERCISE", Toast.LENGTH_SHORT).show();
//
//                //MyRoutine is serializable
//                MyRoutine selectedTmpRoutine = new MyRoutine();
//                selectedTmpRoutine.setRoutineId("ROUTINE01");
//                selectedTmpRoutine.setRoutineName("아령푸쉬업훌라후프");
//                selectedTmpRoutine.setRoutineEq1Id("11:22:33:44:55:66");
//                selectedTmpRoutine.setRoutineEq1Goal("3|15|-1");
//                selectedTmpRoutine.setRoutineEq2Id("22:33:44:55:66:77");
//                selectedTmpRoutine.setRoutineEq2Goal("2|20|-1");
//                selectedTmpRoutine.setRoutineEq3Id("33:44:55:66:77:88");
//                selectedTmpRoutine.setRoutineEq3Goal("5|-1|60");

                //Send a selected routine by serializing MyRoutine object
                Intent intent_startexercise = new Intent(getApplicationContext(), StartExerciseActivity.class);
                intent_startexercise.putExtra("selectedRoutine", mySelectedRoutine);
                startActivity(intent_startexercise);

//                if (mySelectedRoutine == null) {
//                    Snackbar.make(view, "Please select your routine first", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//
//                    if (viewPager != null) {
//                        viewPager.setCurrentItem(1);
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "Selected routine:\n" + mySelectedRoutine.getRoutineName(), Toast.LENGTH_SHORT).show();
//
//                    //Send a selected routine by serializing MyRoutine object
//                    Intent intent_startexercise = new Intent(getApplicationContext(), StartExerciseActivity.class);
//                    intent_startexercise.putExtra("selectedRoutine", mySelectedRoutine);
//                    startActivity(intent_startexercise);
//                }


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (item.getItemId()) {
            //Test page
            case R.id.nav_dbtest:
                //TODO: Bluetooth test activity
                //Move to test page
                Intent dbtestIntent = new Intent(this, DatabaseTestActivity.class);
                startActivity(dbtestIntent);
                break;
            //Test page
            case R.id.nav_testpage:
                //TODO: Bluetooth test activity
                //Move to test page
                Intent testIntent = new Intent(this, WorkoutRecords.class);
                startActivity(testIntent);
                break;
            //My exercise equipments
            case R.id.nav_myequipments:
                Toast.makeText(getApplicationContext(), "MY EXERCISE EQUIPMENTS", Toast.LENGTH_SHORT).show();
                //TODO: Activity transition (EquipmentsActivity)

                Intent intent_myequipments = new Intent(getApplicationContext(), MyEquipmentsActivity.class);
                startActivity(intent_myequipments);
                break;
            //My exercise routines
            case R.id.nav_myroutine:
                Toast.makeText(getApplicationContext(), "MY EXERCISE ROUTINES", Toast.LENGTH_SHORT).show();
                //TODO: Activity transition (RoutinesActivity)

                Intent intent_myroutines = new Intent(getApplicationContext(), MyRoutinesActivity.class);
                startActivity(intent_myroutines);
                break;
            //My exercise record
            case R.id.nav_myrecord:
                Toast.makeText(getApplicationContext(), "MY EXERCISE RECORDS", Toast.LENGTH_SHORT).show();
                //TODO: Activity transition (RecordActivity)

                Intent intent_myrecords = new Intent(getApplicationContext(), MyRecordsActivity.class);
                startActivity(intent_myrecords);
                break;
//            //My alarm
//            case R.id.nav_alarm_myalarm:
//                Toast.makeText(getApplicationContext(), "MY ALARM LIST", Toast.LENGTH_SHORT).show();
//                //TODO: Activity transition (AlarmActivity)
//                break;
            //My information setting
            case R.id.nav_mng_myinfo:
                Toast.makeText(getApplicationContext(), "MY INFORMATION SETTING", Toast.LENGTH_SHORT).show();
                //TODO: Activity transition (MyInfoActivity)

                Intent intent_settingmyinfo = new Intent(getApplicationContext(), SettingMyInfoActivity.class);
                startActivity(intent_settingmyinfo);
                break;
            //App setting
            case R.id.nav_mng_appsetting:
                Toast.makeText(getApplicationContext(), "APPLICATION SETTING", Toast.LENGTH_SHORT).show();
                //TODO: Activity transition (AppSettingActivity)

                Intent intent_settingappinfo = new Intent(getApplicationContext(), SettingAppInfoActivity.class);
                startActivity(intent_settingappinfo);
                break;
            //Help
            case R.id.nav_mng_showhelp:
                Toast.makeText(getApplicationContext(), "SHOW HELP DIALOG", Toast.LENGTH_SHORT).show();
                //TODO: Dialog (HelpDialog)

                SupportHelpFragment fragment = new SupportHelpFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                transaction.add(R.id.container, fragment, "instruction");
                transaction.addToBackStack("instruction");

                transaction.commit();

//                Intent intent_supporthelp = new Intent(getApplicationContext(), SupportHelpActivity.class);
//                startActivity(intent_supporthelp);
                break;
            //Application information
            case R.id.nav_mng_showappinfo:
                Toast.makeText(getApplicationContext(), "SHOW APP INFO DIALOG", Toast.LENGTH_SHORT).show();
                //TODO: Dialog (AppInfoDialog)

                Intent intent_supportappinfo = new Intent(getApplicationContext(), SupportAppInfoActivity.class);
                startActivity(intent_supportappinfo);
                break;
            case R.id.nav_db_add_myinformation:
                //Android device id
                String android_deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                MyInformation tmpMyInformation = new MyInformation();

                tmpMyInformation.setUserId("ThisIsAnId");
                tmpMyInformation.setUserName("UserName");
                tmpMyInformation.setUserNick("Nickname");
                tmpMyInformation.setUserAge(25);
                tmpMyInformation.setUserSex(1);
                tmpMyInformation.setUserHeight(180);
                tmpMyInformation.setUserWeight(75);
                tmpMyInformation.setUserRegistered(1);
                tmpMyInformation.setUserDeviceId(android_deviceId);

                hereDB.insertMyInformation(tmpMyInformation);
                Toast.makeText(getApplicationContext(), "MyInformation is added into DB.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_db_add_myagents:
                MyHereAgent tmpMyHereAgent1 = new MyHereAgent();
                tmpMyHereAgent1.setMyeqMacId("11:22:33:44:55:66");
                tmpMyHereAgent1.setMyeqName("아령");
                tmpMyHereAgent1.setMyeqType(1);
                tmpMyHereAgent1.setMyeqBeaconMajorId("Major1");
                tmpMyHereAgent1.setMyeqBeaconMinorId("Minor1");

                MyHereAgent tmpMyHereAgent2 = new MyHereAgent();
                tmpMyHereAgent2.setMyeqMacId("22:33:44:55:66:77");
                tmpMyHereAgent2.setMyeqName("푸쉬업바");
                tmpMyHereAgent2.setMyeqType(2);
                tmpMyHereAgent2.setMyeqBeaconMajorId("Major2");
                tmpMyHereAgent2.setMyeqBeaconMinorId("Minor2");

                MyHereAgent tmpMyHereAgent3 = new MyHereAgent();
                tmpMyHereAgent3.setMyeqMacId("33:44:55:66:77:88");
                tmpMyHereAgent3.setMyeqName("훌라후프");
                tmpMyHereAgent3.setMyeqType(4);
                tmpMyHereAgent3.setMyeqBeaconMajorId("Major3");
                tmpMyHereAgent3.setMyeqBeaconMinorId("Minor3");

                hereDB.insertHereAgent(tmpMyHereAgent1);
                hereDB.insertHereAgent(tmpMyHereAgent2);
                hereDB.insertHereAgent(tmpMyHereAgent3);
                Toast.makeText(getApplicationContext(), "Three agents are added into DB.", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_db_add_myroutines:
                MyRoutine tmpMyRoutine1 = new MyRoutine();
                tmpMyRoutine1.setRoutineId("ROUTINE01");
                tmpMyRoutine1.setRoutineName("아령푸쉬업훌라후프");
                tmpMyRoutine1.setRoutineEq1Id("11:22:33:44:55:66");
                tmpMyRoutine1.setRoutineEq1Goal("3|15|-1");
                tmpMyRoutine1.setRoutineEq2Id("22:33:44:55:66:77");
                tmpMyRoutine1.setRoutineEq2Goal("2|20|-1");
                tmpMyRoutine1.setRoutineEq3Id("33:44:55:66:77:88");
                tmpMyRoutine1.setRoutineEq3Goal("5|-1|60");

                MyRoutine tmpMyRoutine2 = new MyRoutine();
                tmpMyRoutine2.setRoutineId("ROUTINE02");
                tmpMyRoutine2.setRoutineName("푸쉬업훌라후프");
                tmpMyRoutine2.setRoutineEq1Id("22:33:44:55:66:77");
                tmpMyRoutine2.setRoutineEq1Goal("2|20|-1");
                tmpMyRoutine2.setRoutineEq2Id("33:44:55:66:77:88");
                tmpMyRoutine2.setRoutineEq2Goal("1|50|-1");

                hereDB.insertRoutine(tmpMyRoutine1);
                hereDB.insertRoutine(tmpMyRoutine2);
                Toast.makeText(getApplicationContext(), "Two routines are added into DB.", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_db_add_myrecords:
                break;
        }



//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
