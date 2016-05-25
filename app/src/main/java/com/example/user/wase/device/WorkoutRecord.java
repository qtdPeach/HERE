package com.example.user.wase.device;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.user.wase.R;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarahsong on 2016-05-25.
 */
public class WorkoutRecord extends AppCompatActivity {

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_workout_record);
        ImageView userImg = (ImageView)findViewById(R.id.usrimg);
        userImg.setImageResource(R.mipmap.ic_setting_user_information);

        List<Double> workoutData = new ArrayList<Double>();
        //here to input data from DB
        workoutData.add(300.0);workoutData.add(500.0);workoutData.add(100.0);workoutData.add(300.0);workoutData.add(800.0);workoutData.add(400.0);workoutData.add(700.0);

        GraphView gv = (GraphView) findViewById(R.id.workoutbargraph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {new DataPoint(1,workoutData.get(0)),
                new DataPoint(2,workoutData.get(1)),
                new DataPoint(3,workoutData.get(2)),
                new DataPoint(4,workoutData.get(3)),
                new DataPoint(5,workoutData.get(4)),
                new DataPoint(6,workoutData.get(5)),
                new DataPoint(7,workoutData.get(6))});
        gv.addSeries(series);
    }

    @Override
    public void onStart () {
        super.onStart();
    }

    @Override
    public void onResume () {
        super.onResume();
    }
}
