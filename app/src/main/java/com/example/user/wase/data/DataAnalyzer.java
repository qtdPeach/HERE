package com.example.user.wase.data;

import com.example.user.wase.model.Equipment;

import java.util.ArrayList;

/**
 * Created by PCPC on 2016-05-24.
 */
public class DataAnalyzer extends Thread{
    private int equipmentType;
    private ArrayList<SingleData>[] data;

    private boolean isEnable;

    private final int listCapacity = 1000;


    public DataAnalyzer(int type){
        equipmentType = type;
        switch (equipmentType){
            case Equipment.TYPE_DUMBEL:
                /*
                ACC_X
                ACC_Y
                ACC_Z
                ORIENTATION_X
                ORIENTATION_Y
                ORIENTATION_Z
                 */
                data = new ArrayList[6];
                isEnable = true;
                break;
            case Equipment.TYPE_PUSH_UP:
                //FORCE
                data = new ArrayList[1];
                isEnable = true;
                break;
            case Equipment.TYPE_HOOLA_HOOP:
                /*
                ACC_X
                ACC_Y
                ACC_Z
                */
                data = new ArrayList[3];
                isEnable = true;
                break;
            case Equipment.TYPE_JUMP_ROPE:
                //Magnet
                data = new ArrayList[1];
                isEnable = true;
                break;
            default:
                isEnable = false;
                break;
        }
        if(isEnable) {
            for (int i = 0 ; i < data.length; i++) {
                data[i] = new ArrayList<SingleData>();
            }
            equipmentType = type;
        }
    }

    public void add(SingleData point, char rawDataType){
        if(isEnable){

            switch(rawDataType){
                case SingleData.INDEX_ACC:
                    break;
                case SingleData.INDEX_GYRO:
                    break;
                case SingleData.INDEX_MAG:
                    break;
                case SingleData.INDEX_FORCE:
                    break;
                default:
                    break;
            }
        }
    }


}
