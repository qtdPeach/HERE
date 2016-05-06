package com.example.user.wase.device;

import java.util.Date;

/**
 * Created by PCPC on 2016-05-05.
 * Classifying the device
 */
public class Device{
    public static int DUMBEL = 1;
    public static int PUSH_UP = 2;
    public static int HOOP = 3;
    public static int JUMP_ROPE = 4;

    private int deviceCategory;
    private int deviceID;
    private Date lastWork;

    public Device(){

    }
}
