package com.example.user.wase.utility;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by PCPC on 2016-05-24.
 */
public class DataAnalyzer extends Thread{
    private int equipmentType;
    private ArrayList<DataPoint> data;

    private boolean isDetectingMax, isDetectingMin;

    private double maxValue, minValue;
    private int maxPosition, minPosition;
    private double delta; // minimum difference for the peaks
    private final double minDelta = 20;

    private boolean isEnable;

    private final int listCapacity = 1000;


    public DataAnalyzer(int type){
        equipmentType = type;
        data = new ArrayList<DataPoint>();
        minValue = Double.MAX_VALUE;
        maxValue = Double.MIN_VALUE;

        maxPosition = -1;
        minPosition = -1;

        isDetectingMax = true;
        isDetectingMin = true;

        delta = 100;
    }

    public void reset(){
        data = new ArrayList<DataPoint>();
        minValue = Double.MAX_VALUE;
        maxValue = Double.MIN_VALUE;
        maxPosition = -1;
        minPosition = -1;
        isDetectingMax = true;
        isDetectingMin = true;
    }

    //reference: http://www.billauer.co.il/peakdet.html
    /*
    @arguments
    @return: no peak (0) min peak (-1) max peak(1)
     */
    public int peakDetection(DataPoint point){
        double added = point.getY();
        if(isDetectingMax){
            if(maxValue < added) {
                maxValue = added;
                maxPosition = data.size();
                double temp = Math.abs(maxValue /2);
                if(temp > minDelta) delta = temp;
            }
            else if( added < maxValue - delta){
                isDetectingMax = false;
                return 1;
            }

        }

        if(isDetectingMin){
            if(minValue > added){
                minValue = added;
                minPosition = data.size();
                double temp = Math.abs(minValue /2);
                if(temp > minDelta) delta = temp;
            }
            else if(added > minValue + delta){
                isDetectingMin = false;
                return -1;
            }
        }
        return 0;
    }


    /*
    @arguments
    @return: min: -1, max: 1, both = 0
     */
    public void setDetectingMode(int mode){
        switch (mode){
            case -1:
                isDetectingMin = true;
                isDetectingMax = false;
                break;
            case 1:
                isDetectingMin = false;
                isDetectingMax = true;
                break;
            case 0:
                isDetectingMin = false;
                isDetectingMax = false;
                break;
        }
    }

    public void cutFromTo(int from, int to){
        data = (ArrayList<DataPoint>)data.subList(from, to);

    }
}
