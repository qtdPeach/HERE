package com.example.user.wase.view.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.wase.R;
import com.example.user.wase.device.EquipmentRecordTerminal;
import com.example.user.wase.deviceLE.DataViewTerminal;
import com.example.user.wase.model.Equipment;
import com.example.user.wase.model.EquipmentRecord;
import com.example.user.wase.model.MyHereAgent;
import com.example.user.wase.model.MyInformation;
import com.example.user.wase.model.MyRecord;
import com.example.user.wase.model.MyRoutine;
import com.example.user.wase.model.RecordDateCalorie;
import com.example.user.wase.model.RecordForGraph;
import com.example.user.wase.utility.CalorieCalculator;
import com.example.user.wase.view.activity.MainActivity;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ymbae on 2016-04-18.
 */
public class MyRecordsFragment extends Fragment{

    private static final String TAG_RECORD_DATA = "MyRecordsFragment";


    private GraphView gv;

    private HERE_DeviceListAdapter equipListAdapter;
    private ListView lvEquipList;
    private ArrayList<Equipment> equipmentList;

    private ImageView iv_character_img;
    private TextView tv_nick_name;
    private TextView tv_msg;

    private ArrayList<MyRecord> arrMyRecords;
    private ArrayList<RecordForGraph> arrGraphElements;
    //private ArrayList<Double> dailyCalories;
    private ArrayList<RecordDateCalorie> arrDateCalorie;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View currentView = inflater.inflate(R.layout.fragment_myrecords, container, false);


        /**
         * Youngmin
         */
        //Initialize widgets
        initWidgets(currentView);

        //Initialize arraylist from MyRecords table
        //Divide a single record into multiple records (by equip)
        initArrayListFromDB();

        //Initialize DailyCalories arraylist using daily record
        initDailyRecord();


        /**
         * Jiyoung
         */

        //Draw calorie graph using dailyCalorie arraylist
        drawDailyCalorieGraph(currentView);


        equipListAdapter = new HERE_DeviceListAdapter();
        lvEquipList = (ListView) currentView.findViewById(R.id.equipment_record_list);
        lvEquipList.setAdapter(equipListAdapter);
        //equipment added
        equipListAdapter.addDevice(new Equipment("EQ01", "DUMBBELL", "Sensor-Q03-87A", "2016-04-18", 2));
        equipListAdapter.addDevice(new Equipment("EQ02", "HOOLA-HOOP", "Accelerometer-X-3", "2016-04-18", 1));
        equipListAdapter.addDevice(new Equipment("EQ03", "PLANK", "FORCE-ss-2033", "2016-04-15", 1));
        equipListAdapter.addDevice(new Equipment("EQ04", "JUMP-ROPE", "ZEROZERO", "2016-04-16", 0));

        equipListAdapter.notifyDataSetChanged();

        lvEquipList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                final Equipment device = equipmentList.get(position);
//                if (device == null) return;
//                final Intent intent = new Intent(getActivity(), EquipmentRecordTerminal.class);
//
//                //deliver data --> should be modified
//                intent.putExtra(EquipmentRecordTerminal.EXTRAS_DEVICE_NAME, device.getEquipmentName());
//                intent.putExtra(EquipmentRecordTerminal.EXTRAS_DEVICE_RECORD, device.getEquipmentID());
//
//                startActivity(intent);
            }
        });





        return currentView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onResume() {
        initArrayListFromDB();
        initDailyRecord();
        drawDailyCalorieGraph(getView());

        super.onResume();
    }

    private int getMonth(String currentDate) {
        String monthString = currentDate.substring(0,2);
        //Log.d("currentDateTime", "monthString: " + monthString);

        return Integer.parseInt(monthString);
    }

    private int getDay(String currentDate) {
        String dayString = currentDate.substring(3,5);
        //Log.d("currentDateTime", "dayString: " + dayString);

        return Integer.parseInt(dayString);
    }

    private void initDataPointsZero() {

    }

    private void drawDailyCalorieGraph(View fragmentView) {

        //Calendar for date
        Calendar calendar = Calendar.getInstance();

        Log.d("arrDateCalorie", "arrDateCalorie-gap(0): " + arrDateCalorie.get(0).getDaysGap());
        Log.d("arrDateCalorie", "arrDateCalorie-gap(1): " + arrDateCalorie.get(1).getDaysGap());
        Log.d("arrDateCalorie", "arrDateCalorie-gap(2): " + arrDateCalorie.get(2).getDaysGap());

        //If we redraw, we first remove all series
        gv.removeAllSeries();

        //Make data points for a recent 1 week
        DataPoint[] dataPoints = new DataPoint[7];

        //Initialize datapoints
        for (int i = 0; i < 7; i++) {
            DataPoint tmpDataPoint = new DataPoint(i + 1, 0.0);
            dataPoints[i] = tmpDataPoint;
        }

        //There is a record more than one week ago
        if (arrDateCalorie.get(0).getDaysGap() > 6) {

            for (int i = 0; i < arrDateCalorie.size(); i++) {
                //Only for recent 1 week
                if (arrDateCalorie.get(i).getDaysGap() < 7) {
                    DataPoint tmpDataPoint = new DataPoint(i + 1, arrDateCalorie.get(i).getRecordCalorie());
                    dataPoints[6-arrDateCalorie.get(i).getDaysGap()] = tmpDataPoint;
                }
            }

        }
        //There are only records in this recent week
        else {
            for (int i = 0; i < arrDateCalorie.size(); i++) {
                //Only for recent 1 week
                if (arrDateCalorie.get(i).getDaysGap() < 7) {
                    DataPoint tmpDataPoint = new DataPoint(i + 1, arrDateCalorie.get(i).getRecordCalorie());
                    dataPoints[6-arrDateCalorie.get(i).getDaysGap()] = tmpDataPoint;
                }
            }
        }

        for (int i = 0; i < 7; i++) {
            Log.d("DataPoints", "[" + i + "] " + dataPoints[i].getX() + "/" + dataPoints[i].getY());
        }


        BarGraphSeries<DataPoint> calorieSeries = new BarGraphSeries<DataPoint>(dataPoints);

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

        //Initialize StaticLabelsFormatter
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(gv);
        staticLabelsFormatter.setHorizontalLabels(new String[]{
                getDay(calendar.get(Calendar.DAY_OF_WEEK) - 6),
                getDay(calendar.get(Calendar.DAY_OF_WEEK) - 5),
                getDay(calendar.get(Calendar.DAY_OF_WEEK) - 4),
                getDay(calendar.get(Calendar.DAY_OF_WEEK) - 3),
                getDay(calendar.get(Calendar.DAY_OF_WEEK) - 2),
                getDay(calendar.get(Calendar.DAY_OF_WEEK) - 1),
                getDay(calendar.get(Calendar.DAY_OF_WEEK))});
        gv.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        calorieSeries.setSpacing(50);
        calorieSeries.setDrawValuesOnTop(true);
        calorieSeries.setValuesOnTopColor(Color.BLUE);
        calorieSeries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                if (data.getY() < 50.0) {
                    return Color.rgb(227, 21, 0);
                }else if (data.getY() >= 50.0 && data.getY() < 100.0) {
                    return Color.rgb(239, 107, 0);
                }else if (data.getY() >= 100.0 && data.getY() < 150.0) {
                    return Color.rgb(239, 197, 0);
                }else if (data.getY() >= 150.0 && data.getY() < 200.0) {
                    return Color.rgb(159, 199, 0);
                } else {
                    return Color.rgb(28, 199, 0);
                }
//                return Color.rgb(135, 206, 235);
            }
        });
        gv.addSeries(calorieSeries);


    }


    private void initArrayListFromDB() {
        //Initialize my records
        arrMyRecords = new ArrayList<>();
        arrMyRecords.addAll(MainActivity.hereDB.getAllMyRecords());
        Log.d(TAG_RECORD_DATA, "arrMyRecords.size(): " + arrMyRecords.size());

        //Initialize arrGraphElements using arrMyRecords; Group records by each equipment
        arrGraphElements = new ArrayList<>();
        analyzeMyRecords();

        logArrGraphElements();
    }


    private void analyzeMyRecords() {

        int arrGraphElementCount = 0;

        for (int i = 0; i < arrMyRecords.size(); i++) {
            RecordForGraph tmpRecord = new RecordForGraph();

            String recordId = arrMyRecords.get(i).getRecordId();
            String recordName = arrMyRecords.get(i).getRecordName();
            String recordDateTime = arrMyRecords.get(i).getRecordDateTime();

            tmpRecord.setRecordId(recordId);
            tmpRecord.setRecordName(recordName);
            tmpRecord.setRecordDateTime(recordDateTime);

            /* EQ1 */
            //If there is no equipment record
            if (arrMyRecords.get(i).getRecordEq1Id().equals("-1")
                    || arrMyRecords.get(i).getRecordEq1Id().equals("")) {
                continue;
            }
            //If there is at least one equipment record
            else {
                tmpRecord = new RecordForGraph(recordId, recordName, recordDateTime);
                tmpRecord.setRecordEqId(arrMyRecords.get(i).getRecordEq1Id());
                tmpRecord.setRecordEqDone(arrMyRecords.get(i).getRecordEq1Done());

                //Add a new element to arrGraphElements
                arrGraphElements.add(tmpRecord);
//                logArrGraphElements();

                arrGraphElementCount ++;

                /* EQ2 */
                if (arrMyRecords.get(i).getRecordEq2Id().equals("-1")
                        || arrMyRecords.get(i).getRecordEq2Id().equals("")) {
                    continue;
                } else {
                    tmpRecord = new RecordForGraph(recordId, recordName, recordDateTime);
                    tmpRecord.setRecordEqId(arrMyRecords.get(i).getRecordEq2Id());
                    tmpRecord.setRecordEqDone(arrMyRecords.get(i).getRecordEq2Done());

                    //Add a new element to arrGraphElements
                    arrGraphElements.add(tmpRecord);
//                    logArrGraphElements();

                    arrGraphElementCount ++;

                    /* EQ3 */
                    if (arrMyRecords.get(i).getRecordEq3Id().equals("-1")
                            || arrMyRecords.get(i).getRecordEq3Id().equals("")) {
                        continue;
                    } else {
                        tmpRecord = new RecordForGraph(recordId, recordName, recordDateTime);
                        tmpRecord.setRecordEqId(arrMyRecords.get(i).getRecordEq3Id());
                        tmpRecord.setRecordEqDone(arrMyRecords.get(i).getRecordEq3Done());

                        //Add a new element to arrGraphElements
                        arrGraphElements.add(tmpRecord);
//                        logArrGraphElements();

                        arrGraphElementCount ++;

                        /* EQ4 */
                        if (arrMyRecords.get(i).getRecordEq4Id().equals("-1")
                                || arrMyRecords.get(i).getRecordEq4Id().equals("")) {
                            continue;
                        } else {
                            tmpRecord = new RecordForGraph(recordId, recordName, recordDateTime);
                            tmpRecord.setRecordEqId(arrMyRecords.get(i).getRecordEq4Id());
                            tmpRecord.setRecordEqDone(arrMyRecords.get(i).getRecordEq4Done());

                            //Add a new element to arrGraphElements
                            arrGraphElements.add(tmpRecord);
//                            logArrGraphElements();

                            arrGraphElementCount ++;

                            /* EQ5 */
                            if (arrMyRecords.get(i).getRecordEq5Id().equals("-1")
                                    || arrMyRecords.get(i).getRecordEq5Id().equals("")) {
                                continue;
                            } else {
                                tmpRecord = new RecordForGraph(recordId, recordName, recordDateTime);
                                tmpRecord.setRecordEqId(arrMyRecords.get(i).getRecordEq5Id());
                                tmpRecord.setRecordEqDone(arrMyRecords.get(i).getRecordEq5Done());

                                //Add a new element to arrGraphElements
                                arrGraphElements.add(tmpRecord);
//                                logArrGraphElements();

                                arrGraphElementCount ++;
                            }
                        }
                    }
                }
            }

        }

    }


    private void initDailyRecord() {

        arrDateCalorie = new ArrayList<>();
        //dailyCalories = new ArrayList<>();

        CalorieCalculator calorieCalculator = new CalorieCalculator();

        double dailyCalorie = 0.0;

        RecordDateCalorie dateCalorie = new RecordDateCalorie();

        String currentDate;
        int day = 0;

        if (arrGraphElements.size() == 0) {
            return;
        } else {
            currentDate = parseDateWithYear(arrGraphElements.get(0).getRecordDateTime());
            dateCalorie.setRecordDate(currentDate);

            Log.d("DateDebugging", "currentDate: " + currentDate);

            for (int i = 0; i < arrGraphElements.size(); i++) {

                //Get the date of record
                String tmpElementDate = parseDateWithYear(arrGraphElements.get(i).getRecordDateTime());
                Log.d("DateDebugging", "tmpElementDate: " + tmpElementDate);

                //Get agent by Id
                MyHereAgent tmpAgent = MainActivity.hereDB.getMyHereAgent(arrGraphElements.get(i).getRecordEqId());
                //Get my information (height, weight)
                MyInformation tmpMyInfo = MainActivity.hereDB.getMyInformation();

                //TODO: Done, Time
                int recordNum = 0;
                int recordTime = 0;
                switch (tmpAgent.getMyeqType()) {
                    case MyHereAgent.TYPE_DUMBEL:
                        recordNum = arrGraphElements.get(i).getRecordEqDone();
                        recordTime = (int)(recordNum * 1.67);
                        break;
                    case MyHereAgent.TYPE_PUSH_UP:
                        recordNum = arrGraphElements.get(i).getRecordEqDone();
                        recordTime = (int)(recordNum * 3.0);
                        break;
                    case MyHereAgent.TYPE_HOOLA_HOOP:
                        recordTime = arrGraphElements.get(i).getRecordEqDone();
                        recordNum = (int)(recordTime * 1.35);
                        break;
                }

                double tmpColorie = calorieCalculator.getCalorie(tmpAgent, tmpMyInfo, recordNum, recordTime);

                //Record in same date
                if (!currentDate.equals(tmpElementDate)) {
                    //Add this day's accumulated colorie
                    //dailyCalories.add(dailyCalorie);
                    dateCalorie.setRecordCalorie(dailyCalorie);
                    arrDateCalorie.add(dateCalorie);

                    //Reinitialize dateCalorie (new instance)
                    dateCalorie = new RecordDateCalorie();

                    //Update currentDate
                    currentDate = tmpElementDate;
                    dateCalorie.setRecordDate(currentDate);

                    //Initialize daily calorie
                    dailyCalorie = 0;
                    dailyCalorie += tmpColorie;

                    day++;
                } else {
                    //Add the calorie of this record
                    dailyCalorie += tmpColorie;
                }


            }

            //For last day
            //dailyCalories.add(dailyCalorie);
            dateCalorie.setRecordCalorie(dailyCalorie);
            arrDateCalorie.add(dateCalorie);

        }

        Log.d("arrDateCalorie", "arrDateCalorie.size(): " + arrDateCalorie.size());

        logArrDateCalorie();

//        Log.d("DailyCalorie", "dailyColories.size(): " + dailyCalories.size());
//
//        for (int i = 0; i < dailyCalories.size(); i++) {
//            Log.d("DailyCalorie", "dailyColories[" + i + "]: " + dailyCalories.get(i) + "kcal");
//        }

    }

    private String parseDateWithYear(String datetimeData) {
        //dateTimeData: 2016-06-10 00:21:32

        int spaceLoc = datetimeData.indexOf(" ");
        Log.d("TestDateParse", "spaceLoc: " + spaceLoc);
        return datetimeData.substring(0, spaceLoc);
    }

    private String parseOnlyDate(String dateWithYear) {
        //dateWithYear: 2016-06-10

        int hyphenLoc = dateWithYear.indexOf("-");
        Log.d("TestDateParse", "hyphenLoc: " + hyphenLoc + " (" + dateWithYear.charAt(hyphenLoc) + ")");
        return dateWithYear.substring(hyphenLoc + 1, hyphenLoc + 6);
    }

    private void logArrDateCalorie() {
        Log.d("arrDateCalorie", "arrDateCalorie.size(): " + arrDateCalorie.size());
        Log.d("arrDateCalorie", "-------------------------------------------------");

        for (int i = 0; i < arrDateCalorie.size(); i++) {
            Log.d("arrDateCalorie", "arrDateCalorie[" + i + "]: " + arrDateCalorie.get(i).getRecordDate());
            Log.d("arrDateCalorie", "arrDateCalorie[" + i + "]: " + arrDateCalorie.get(i).getRecordCalorie());
            Log.d("arrDateCalorie", " ");
        }

        Log.d("arrDateCalorie", "-------------------------------------------------");
    }

    private void logArrGraphElements() {

        Log.d(TAG_RECORD_DATA, "arrGraphElements.size(): " + arrGraphElements.size());
        Log.d(TAG_RECORD_DATA, "-------------------------------------------------");

        for (int i = 0; i < arrGraphElements.size(); i++) {
            Log.d(TAG_RECORD_DATA, "arrGraphElement[" + i + "]: " + arrGraphElements.get(i).getRecordId());
            Log.d(TAG_RECORD_DATA, "arrGraphElement[" + i + "]: " + arrGraphElements.get(i).getRecordName());
            Log.d(TAG_RECORD_DATA, "arrGraphElement[" + i + "]: " + arrGraphElements.get(i).getRecordDateTime());
            Log.d(TAG_RECORD_DATA, "arrGraphElement[" + i + "]: " + arrGraphElements.get(i).getRecordEqId());
            Log.d(TAG_RECORD_DATA, "arrGraphElement[" + i + "]: " + arrGraphElements.get(i).getRecordEqDone());
            Log.d(TAG_RECORD_DATA, " ");
        }

        Log.d(TAG_RECORD_DATA, "-------------------------------------------------");
    }

    private void initWidgets(View fragmentView) {
        //Initialize GraphView
        gv = (GraphView) fragmentView.findViewById(R.id.week_bar_graph);

        iv_character_img = (ImageView) fragmentView.findViewById(R.id.record_character_img);
        tv_nick_name = (TextView) fragmentView.findViewById(R.id.record_nick_name);
        tv_msg = (TextView) fragmentView.findViewById(R.id.record_msg);

        if (iv_character_img == null | tv_nick_name == null | tv_msg == null) {
            Log.d("RecordInitWidgets", "Widget initialization is failed.");
        } else {
            MyInformation tmpMyInformation = MainActivity.hereDB.getMyInformation();

            if (tmpMyInformation == null) {
                iv_character_img.setImageResource(R.drawable.here_logo_character_notitle);
                tv_nick_name.setText("User (not registered)");
                tv_msg.setText("Insert user information");
            } else {
                Log.d("RecordInitWidgets", "initMyInfo() is called");
                Log.d("RecordInitWidgets", "user_sex: " + tmpMyInformation.getUserSex());
                Log.d("RecordInitWidgets", "user_name: " + tmpMyInformation.getUserName());
                Log.d("RecordInitWidgets", "user_nick: " + tmpMyInformation.getUserNick());

                if (tmpMyInformation.getUserSex() == 2) {
                    iv_character_img.setImageResource(R.drawable.here_character_simple_girl);
                } else {
                    iv_character_img.setImageResource(R.drawable.here_character_simple_boy);
                }

                tv_nick_name.setText(tmpMyInformation.getUserNick() + " (" + tmpMyInformation.getUserName() + ")");

                //TODO: Record를 분석하여 Message를 출력해줄 수 있도록 수정
                tv_msg.setText("Lazy! Do your best!");
            }
        }
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

    private class HERE_DeviceListAdapter extends BaseAdapter {
        private LayoutInflater mInflator;

        public HERE_DeviceListAdapter() {
            super();
            equipmentList = new ArrayList<Equipment>();
            mInflator = getActivity().getLayoutInflater();
        }

        public void addDevice(Equipment device) {
            equipmentList.add(device);
        }

        @Override
        public int getCount() {
            return equipmentList.size();
        }

        @Override
        public Object getItem(int position) {
            return equipmentList.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            // General ListView optimization code.
            if (view == null) {
                int res = 0;
                res = R.layout.fragment_myrecord_item;
                view = mInflator.inflate(res, viewGroup, false);

            }

            ImageView eqTypeImage = (ImageView)view.findViewById(R.id.equipment_img);
            TextView eqName = (TextView)view.findViewById(R.id.equipment_name);
            GraphView gvLinear = (GraphView) view.findViewById(R.id.weekly_linear_graph);
            Calendar calendar = Calendar.getInstance();

            List<Double> workoutData = new ArrayList<Double>();

            //TODO: ArrayList(가변길이): Equipment 개수, Array(고정길이): 7개의 DataPoint

            //here to input data from DB
            workoutData.add(300.0);workoutData.add(500.0);workoutData.add(100.0);workoutData.add(300.0);workoutData.add(800.0);workoutData.add(400.0);workoutData.add(700.0);

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {new DataPoint(1,workoutData.get(0)),
                    new DataPoint(2,workoutData.get(1)),
                    new DataPoint(3,workoutData.get(2)),
                    new DataPoint(4,workoutData.get(3)),
                    new DataPoint(5,workoutData.get(4)),
                    new DataPoint(6,workoutData.get(5)),
                    new DataPoint(7,workoutData.get(6))});

            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(gvLinear);
            staticLabelsFormatter.setHorizontalLabels(new String[] {getDay(calendar.get(Calendar.DAY_OF_WEEK)-6), getDay(calendar.get(Calendar.DAY_OF_WEEK)-5), getDay(calendar.get(Calendar.DAY_OF_WEEK)-4), getDay(calendar.get(Calendar.DAY_OF_WEEK)-3), getDay(calendar.get(Calendar.DAY_OF_WEEK)-2), getDay(calendar.get(Calendar.DAY_OF_WEEK)-1), getDay(calendar.get(Calendar.DAY_OF_WEEK))});
            gvLinear.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

            series.setDrawDataPoints(true);
            series.setColor(Color.rgb(135, 206, 235));
            series.setDataPointsRadius(5);
            series.setThickness(4);
            gvLinear.addSeries(series);

            switch (equipmentList.get(i).getEquipmentType()) {
                case 0:
                    eqTypeImage.setImageResource(R.mipmap.ic_setting_update_alarm);
                    break;
                case 1:
                    eqTypeImage.setImageResource(R.mipmap.ic_setting_best_interest);
                    break;
                case 2:
                    eqTypeImage.setImageResource(R.mipmap.ic_setting_user_information);
                    break;
                case 3:
                    break;
                default:
                    break;
            }

            eqName.setText(equipmentList.get(i).getEquipmentName());
            return view;
        }
    }
}
