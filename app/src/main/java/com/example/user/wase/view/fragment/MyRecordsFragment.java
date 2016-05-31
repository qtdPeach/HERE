package com.example.user.wase.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.wase.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ymbae on 2016-04-18.
 */
public class MyRecordsFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();


        List<Double> workoutData =  new ArrayList<Double>();

        //here! add list values
        workoutData.add(300.0);workoutData.add(500.0);workoutData.add(100.0);workoutData.add(300.0);workoutData.add(800.0);workoutData.add(400.0);workoutData.add(700.0);

        View currentView = inflater.inflate(R.layout.fragment_myrecords, container, false);
        GraphView gv = (GraphView) currentView.findViewById(R.id.week_bar_graph);
        //GraphView gv = (GraphView) getView().findViewById(R.id.week_bar_graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(1,workoutData.get(0)),
                new DataPoint(2,workoutData.get(1)),
                new DataPoint(3,workoutData.get(2)),
                new DataPoint(4,workoutData.get(3)),
                new DataPoint(5,workoutData.get(4)),
                new DataPoint(6,workoutData.get(5)),
                new DataPoint(7,workoutData.get(6))});

        gv.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX) + "kcal";
                }
            }
        });

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(gv);
        staticLabelsFormatter.setHorizontalLabels(new String[] {getDay(calendar.get(Calendar.DAY_OF_WEEK)-6), getDay(calendar.get(Calendar.DAY_OF_WEEK)-5), getDay(calendar.get(Calendar.DAY_OF_WEEK)-4), getDay(calendar.get(Calendar.DAY_OF_WEEK)-3), getDay(calendar.get(Calendar.DAY_OF_WEEK)-2), getDay(calendar.get(Calendar.DAY_OF_WEEK)-1), getDay(calendar.get(Calendar.DAY_OF_WEEK))});
        gv.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLUE);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(135,206,235);
            }
        });
        gv.addSeries(series);

        return currentView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    String getDay (int date){
        int modulo = date%7;
        if (modulo < 0) modulo+=7;
        switch(modulo){
            case 1:
                return "Sun";
            case 2:
                return "Mon";
            case 3:
                return "Tue";
            case 4:
                return "Wed";
            case 5:
                return "Thu";
            case 6:
                return "Fri";
            case 0:
                return "Sat";
        }
        return "unknown";
    }
}
