package com.example.user.wase.utility;

import android.os.CountDownTimer;

import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by PCPC on 2016-06-08.
 */
public class SinusoidalDetector {
    private double period;
    private double amplitude;
    private double unit; // unit number of one cycle
    private double unitLength;
    private boolean isPeriodic;
    private final double maxAngle = 2*Math.PI;
    private final double radToDeg = 180/Math.PI;

    private double maxPosition, minPosition;
    private double previousCountPosition ;
    private boolean isPrevCheckMax, isPrevCheckMin;
    //is most currently check max peak?

    private final int periodCheckThreshold = 50 * 4; // 7/5s
    private final double phaseThreshold = 10;
    private final double amplitudeThreshold = 30; //1s
    private final double periodThresholdMin = 1000; //1s
    private final double periodThresholdMax = 6000; //6s
    private final double periodThresholdMinHalf = periodThresholdMin/2; //1s
    private final double periodThresholdMaxHalf = periodThresholdMax/2; //6s
    private PeakDetector peak;

    private double count; //
    private boolean modeChangeable;
    private CountDownTimer periodTimer;
    private int periodChecker;

    public SinusoidalDetector(double waistMeasurement, double hoopLength){
        period = 0;
        isPeriodic = false;
        amplitude = 0;
        unit = hoopLength / waistMeasurement;

        count = 0;
        peak = new PeakDetector(2);
        peak.setDelta(30);
        maxPosition = 0;
        minPosition = 0;
        previousCountPosition = 0;
        isPrevCheckMax = false;
        unitLength = Double.MAX_VALUE;
        modeChangeable = false;
        periodChecker = 0;

    }

    public void reset(){
        period = 0;
        isPeriodic = false;
        amplitude = 0;
        count = 0;
        peak.reset();
        maxPosition = 0;
        minPosition = 0;
        previousCountPosition = 0;
        isPrevCheckMax = false;
        isPrevCheckMin = false;
        unitLength = Double.MAX_VALUE;
        modeChangeable = false;
    }
    public void reset(double waistMeasurement, double hoopLength){
        period = 0;
        isPeriodic = false;
        amplitude = 0;
        unit = hoopLength / waistMeasurement;

        count = 0;
        peak.reset();
        maxPosition = 0;
        minPosition = 0;
        previousCountPosition = 0;
        isPrevCheckMax = false;
        isPrevCheckMin = false;
        unitLength = Double.MAX_VALUE;
        modeChangeable = false;

    }

    public boolean periodMonitor(DataPoint point){

        if(modeChangeable && point.getY() > phaseThreshold ){
            peak.setDetectingMode(1);
            modeChangeable = false;
        }else if(modeChangeable && point.getY() < -phaseThreshold){
            peak.setDetectingMode(-1);
            modeChangeable = false;
        }

        if(point.getY() < amplitudeThreshold && point.getY() > -amplitudeThreshold){
            periodChecker++;
            if(periodChecker > periodCheckThreshold){
                periodChecker = 0;
                halfPeriodUpdate(0);
            }
        }else{
            periodChecker = 0;
        }

        switch (peak.peakDetection(point)) {
            case 0:
                modeChangeable = true;
                break;
            case 1:
                if (peak.getMaxValue() > amplitudeThreshold) {
                    maxPosition = peak.getMaxPosition();
                    if (isPrevCheckMin) {
                        if (halfPeriodUpdate(maxPosition - minPosition)) {
                        } else {
                        }
                    }
                    isPrevCheckMax = true;
                    isPrevCheckMin = false;
                }
                break;
            case -1:
                if (peak.getMinValue() < -amplitudeThreshold) {
                    minPosition = peak.getMinPosition();
                    } else if (isPrevCheckMax) {
                        if (halfPeriodUpdate(minPosition - maxPosition)) {
                        } else {
                        }
                    isPrevCheckMin = true;
                    isPrevCheckMax = false;
                }
                break;
        }

        if(isPeriodic){
            double temp = point.getX() - previousCountPosition;
            if(previousCountPosition == 0){
                count = count + unit;
                previousCountPosition = point.getX();
            }else if(temp > periodThresholdMax){
                halfPeriodUpdate(0);
                return false;
            }
            else{
                count += (temp) / unitLength;
                previousCountPosition = point.getX();
            }
            return true;
        }
        return false;
    }


    public boolean halfPeriodUpdate(double peri){
        if(peri > periodThresholdMinHalf && peri < periodThresholdMaxHalf) {
            if (period == 0) {
                period = peri*2;
                unitLength = period / unit;
                isPeriodic = true;
                previousCountPosition = 0;
                return true;
            }
            else if (peri < period / 8) {
                return false;
            }else{
                period = peri*2;
                unitLength = period / unit;
                isPeriodic = true;
                previousCountPosition = 0;
                return true;
            }
        }else{
            isPeriodic = false;
            period = 0;
            unitLength = Double.MAX_VALUE;
            previousCountPosition = 0;
            count = 0;
            return false;
        }
    }



    public double getPhase(double value){
        if(isPeriodic){
            if(amplitude < value) amplitude = value;
            else if(-amplitude > value) amplitude = -value;
            return Math.asin(value/amplitude);
        }
        return 0;
    }

    public int getCount(){
        return (int)count;
    }



    public double getRadToDeg(double rad){
        return rad * radToDeg;
    }
    public double getDegToRad(double deg){
        return deg / radToDeg;
    }

}
