package com.example.user.wase.data;

/**
 * Created by PCPC on 2016-05-25.
 */
public class SingleData {
    public static final char INDEX_ACC = 'a';
    public static final char INDEX_GYRO = 'g';
    public static final char INDEX_MAG = 'm';
    public static final char INDEX_FORCE = 'f';

    public static final String END_BIT = "@";

    public static long appStartTime = 0;

    private char dataType;
    private float rawData;
    private long createTime; //millisecond


    public SingleData(char type, float raw){
        createTime = System.currentTimeMillis() - appStartTime;
        dataType = type;
        rawData = raw;
    }

}
