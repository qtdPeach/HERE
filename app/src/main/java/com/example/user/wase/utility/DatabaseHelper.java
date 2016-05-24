package com.example.user.wase.utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2016-05-24.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "hereAppDatabase.db";
    private static final int DATABASE_VERSION = 1;

    /* Database tables in hereAppDatabase.db */
    private static final String TABLE_MYINFO = "myinfo";
    private static final String TABLE_MYHEREAGENTS = "myhereagents";
    private static final String TABLE_MYROUTINES = "myroutines";
    private static final String TABLE_MYRECORDS = "myrecords";

    /* myinfo table */
    private static final String ATTR_MYINFO_NICK = "myinfo_nick";
    private static final String ATTR_MYINFO_NAME = "myinfo_name";
    private static final String ATTR_MYINFO_AGE = "myinfo_age";
    private static final String ATTR_MYINFO_SEX = "myinfo_sex";
    private static final String ATTR_MYINFO_HEIGHT = "myinfo_height";
    private static final String ATTR_MYINFO_WEIGHT = "myinfo_weight";
    private static final String ATTR_MYINFO_REGISTERED = "myinfo_registered";
    private static final String ATTR_MYINFO_DEVICEID = "myinfo_device_id";

    private static final String CREATE_TABLE_MYINFO =
            "CREATE TABLE " + TABLE_MYINFO + "(" +
                    ATTR_MYINFO_NICK + " VARCHAR(30) PRIMARY KEY, " +
                    ATTR_MYINFO_NAME + " VARCHAR(30), " +
                    ATTR_MYINFO_AGE + " INTEGER, " +
                    ATTR_MYINFO_SEX + " INTEGER, " +
                    ATTR_MYINFO_HEIGHT + " REAL DEFAULT 160.0, " +
                    ATTR_MYINFO_WEIGHT + " REAL DEFAULT 50.0, " +
                    ATTR_MYINFO_REGISTERED + " INTEGER DEFAULT -1, " +
                    ATTR_MYINFO_DEVICEID + " VARCHAR(100)" +
                    ");";

    /* myhereagents table */
    private static final String ATTR_MYEQ_MACID = "myeq_mac_id";
    private static final String ATTR_MYEQ_NAME = "myeq_name";
    private static final String ATTR_MYEQ_TYPE = "myeq_type";
    private static final String ATTR_MYEQ_BEACONMAJORID = "myeq_beacon_major_id";
    private static final String ATTR_MYEQ_BEACONMINORID = "myeq_beacon_minor_id";

    private static final String CREATE_TABLE_MYHEREAGENTS =
            "CREATE TABLE " + TABLE_MYHEREAGENTS + "(" +
                    ATTR_MYEQ_MACID + " VARCHAR(30) PRIMARY KEY, " +
                    ATTR_MYEQ_NAME + " VARCHAR(30), " +
                    ATTR_MYEQ_TYPE + " INTEGER, " +
                    ATTR_MYEQ_BEACONMAJORID + " VARCHAR(50), " +
                    ATTR_MYEQ_BEACONMINORID + " VARCHAR(50)" +
                    ");";

    /* myroutines table */
    private static final String ATTR_MYROUTINE_ID = "myroutine_id";
    private static final String ATTR_MYROUTINE_NAME = "myroutine_name";
    private static final String ATTR_MYROUTINE_EQ1_ID = "myroutine_eq1_id";
    private static final String ATTR_MYROUTINE_EQ1_GOAL = "myroutine_eq1_goal";
    private static final String ATTR_MYROUTINE_EQ2_ID = "myroutine_eq2_id";
    private static final String ATTR_MYROUTINE_EQ2_GOAL = "myroutine_eq2_goal";
    private static final String ATTR_MYROUTINE_EQ3_ID = "myroutine_eq3_id";
    private static final String ATTR_MYROUTINE_EQ3_GOAL = "myroutine_eq3_goal";
    private static final String ATTR_MYROUTINE_EQ4_ID = "myroutine_eq4_id";
    private static final String ATTR_MYROUTINE_EQ4_GOAL = "myroutine_eq4_goal";
    private static final String ATTR_MYROUTINE_EQ5_ID = "myroutine_eq5_id";
    private static final String ATTR_MYROUTINE_EQ5_GOAL = "myroutine_eq5_goal";

    private static final String CREATE_TABLE_MYROUTINES =
            "CREATE TABLE " + TABLE_MYROUTINES + "(" +
                    ATTR_MYROUTINE_ID + " VARCHAR(30), " +
                    ATTR_MYROUTINE_NAME + " VARCHAR(50), " +
                    ATTR_MYROUTINE_EQ1_ID + " VARCHAR(30), " +
                    ATTR_MYROUTINE_EQ1_GOAL + " VARCHAR(20), " +
                    ATTR_MYROUTINE_EQ2_ID + " VARCHAR(30), " +
                    ATTR_MYROUTINE_EQ2_GOAL + " VARCHAR(20), " +
                    ATTR_MYROUTINE_EQ3_ID + " VARCHAR(30), " +
                    ATTR_MYROUTINE_EQ3_GOAL + " VARCHAR(20), " +
                    ATTR_MYROUTINE_EQ4_ID + " VARCHAR(30), " +
                    ATTR_MYROUTINE_EQ4_GOAL + " VARCHAR(20), " +
                    ATTR_MYROUTINE_EQ5_ID + " VARCHAR(30), " +
                    ATTR_MYROUTINE_EQ5_GOAL + " VARCHAR(20), " +
                    "FOREIGN_KEY(" + ATTR_MYROUTINE_EQ1_ID +
                        ") REFERENCES " + TABLE_MYHEREAGENTS + "(" + ATTR_MYEQ_MACID + ")," +
                    "FOREIGN_KEY(" + ATTR_MYROUTINE_EQ2_ID +
                        ") REFERENCES " + TABLE_MYHEREAGENTS + "(" + ATTR_MYEQ_MACID + ")," +
                    "FOREIGN_KEY(" + ATTR_MYROUTINE_EQ3_ID +
                        ") REFERENCES " + TABLE_MYHEREAGENTS + "(" + ATTR_MYEQ_MACID + ")," +
                    "FOREIGN_KEY(" + ATTR_MYROUTINE_EQ4_ID +
                        ") REFERENCES " + TABLE_MYHEREAGENTS + "(" + ATTR_MYEQ_MACID + ")," +
                    "FOREIGN_KEY(" + ATTR_MYROUTINE_EQ5_ID +
                        ") REFERENCES " + TABLE_MYHEREAGENTS + "(" + ATTR_MYEQ_MACID + ")" +
                    ");";

    /* myrecords table */
    private static final String ATTR_MYRECORD_ID = "myrecord_id";
    private static final String ATTR_MYRECORD_DATE = "myrecord_date";
    private static final String ATTR_MYRECORD_EQ1_ID = "myrecord_eq1_id";
    private static final String ATTR_MYRECORD_EQ1_DONE = "myrecord_eq1_done";
    private static final String ATTR_MYRECORD_EQ2_ID = "myrecord_eq2_id";
    private static final String ATTR_MYRECORD_EQ2_DONE = "myrecord_eq2_done";
    private static final String ATTR_MYRECORD_EQ3_ID = "myrecord_eq3_id";
    private static final String ATTR_MYRECORD_EQ3_DONE = "myrecord_eq3_done";
    private static final String ATTR_MYRECORD_EQ4_ID = "myrecord_eq4_id";
    private static final String ATTR_MYRECORD_EQ4_DONE = "myrecord_eq4_done";
    private static final String ATTR_MYRECORD_EQ5_ID = "myrecord_eq5_id";
    private static final String ATTR_MYRECORD_EQ5_DONE = "myrecord_eq5_done";

    private static final String CREATE_TABLE_MYRECORDS =
            "CREATE TABLE " + TABLE_MYRECORDS + "(" +
                    ATTR_MYRECORD_ID + " VARCHAR(30), " +
                    ATTR_MYRECORD_DATE + " VARCHAR(30), " +
                    ATTR_MYRECORD_EQ1_ID + " VARCHAR(30), " +
                    ATTR_MYRECORD_EQ1_DONE + " VARCHAR(30), " +
                    ATTR_MYRECORD_EQ2_ID + " VARCHAR(30), " +
                    ATTR_MYRECORD_EQ2_DONE + " VARCHAR(30), " +
                    ATTR_MYRECORD_EQ3_ID + " VARCHAR(30), " +
                    ATTR_MYRECORD_EQ3_DONE + " VARCHAR(30), " +
                    ATTR_MYRECORD_EQ4_ID + " VARCHAR(30), " +
                    ATTR_MYRECORD_EQ4_DONE + " VARCHAR(30), " +
                    ATTR_MYRECORD_EQ5_ID + " VARCHAR(30), " +
                    ATTR_MYRECORD_EQ5_DONE + " VARCHAR(30), " +
                    "FOREIGN_KEY(" + ATTR_MYRECORD_EQ1_ID +
                        ") REFERENCES " + TABLE_MYHEREAGENTS + "(" + ATTR_MYEQ_MACID + ")," +
                    "FOREIGN_KEY(" + ATTR_MYRECORD_EQ2_ID +
                        ") REFERENCES " + TABLE_MYHEREAGENTS + "(" + ATTR_MYEQ_MACID + ")," +
                    "FOREIGN_KEY(" + ATTR_MYRECORD_EQ3_ID +
                        ") REFERENCES " + TABLE_MYHEREAGENTS + "(" + ATTR_MYEQ_MACID + ")," +
                    "FOREIGN_KEY(" + ATTR_MYRECORD_EQ4_ID +
                        ") REFERENCES " + TABLE_MYHEREAGENTS + "(" + ATTR_MYEQ_MACID + ")," +
                    "FOREIGN_KEY(" + ATTR_MYRECORD_EQ5_ID +
                        ") REFERENCES " + TABLE_MYHEREAGENTS + "(" + ATTR_MYEQ_MACID + ")" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
