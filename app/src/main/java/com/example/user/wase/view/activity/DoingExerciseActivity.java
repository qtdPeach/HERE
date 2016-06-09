package com.example.user.wase.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.wase.R;
import com.example.user.wase.model.RecordAgent;
import com.example.user.wase.model.MyRoutine;
import com.example.user.wase.utility.TaskScheduler;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by user on 2016-06-07.
 */
public class DoingExerciseActivity extends AppCompatActivity {

    private static final int GOAL_SET = 51;
    private static final int GOAL_COUNT = 52;
    private static final int GOAL_TIME = 53;

    private static final int RAND_MIN = 0;
    private static final int RAND_MAX = 3;

    public static Activity thisActivity;
    MyRoutine myRoutine;

    LinearLayout layout_whole;

    TextView tv_timer;

    LinearLayout layout_title;
    ImageView iv_current_eq;
    TextView tv_eq_order;
    TextView tv_eq_name;
    TextView tv_eq_goal;
    TextView tv_eq_count;

    ArrayList<RecordAgent> agentRecords;
    RecordAgent currentAgent;
    int numAgents;
    int currentOrder;

    int countCurrentEq;

    int currentRecordCount;
    int currentRecordTime;

    TaskScheduler timer;
    boolean isTimerRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doingexercise);

        StartExerciseActivity.thisActivity.finish();
        thisActivity = this;

        //Obtain a selected user routine
        myRoutine = MainActivity.mySelectedRoutine;

        //Arraylist to save the user's records
        agentRecords = new ArrayList<>();
        numAgents = 0;
        currentOrder = 0;

        initWidgets();
        initAgentRecords();
        initAgentValues();


        timer = new TaskScheduler();
        timer.scheduleAtFixedRate(increaseTimer, 1000);

    }

    private void initWidgets() {
        isTimerRunning = true;

        layout_whole = (LinearLayout) findViewById(R.id.doingexercise_layout_whole);
        layout_title = (LinearLayout) findViewById(R.id.doingexercise_layout_title);

        tv_timer = (TextView) findViewById(R.id.doingexercise_tv_timer);

        iv_current_eq = (ImageView) findViewById(R.id.doingexercise_iv_current_eq);
        tv_eq_order = (TextView) findViewById(R.id.doingexercise_tv_eq_order);
        tv_eq_name = (TextView) findViewById(R.id.doingexercise_tv_eq_name);
        tv_eq_goal = (TextView) findViewById(R.id.doingexercise_tv_eq_goal);
        tv_eq_count = (TextView) findViewById(R.id.doingexercise_tv_eq_count);

        initMeasureValues();

    }

    private void initMeasureValues() {
        currentRecordTime = 0;
        currentRecordCount = 0;

        countCurrentEq = 0;

        tv_timer.setText(secondToTimerString(currentRecordTime));
        tv_eq_count.setText(String.format("%d", currentRecordCount));
    }

    private void initAgentValues() {
        if (currentOrder == 0) {
            tv_eq_order.setText(String.format("%dst",currentOrder + 1) + " Exercise");
        }else if (currentOrder == 1) {
            tv_eq_order.setText(String.format("%dnd",currentOrder + 1) + " Exercise");
        }else if (currentOrder == 2) {
            tv_eq_order.setText(String.format("%drd", currentOrder + 1) + " Exercise");
        } else {
            tv_eq_order.setText(String.format("%dth", currentOrder + 1) + " Exercise");
        }

        tv_eq_name.setText(agentRecords.get(currentOrder).getAgentName());
        tv_eq_goal.setText(agentRecords.get(currentOrder).makeGoalString());

    }


    private void initAgentRecords() {

        agentRecords.clear();
        numAgents = 0;

        boolean isMoreAgent = true;

        if (myRoutine == null) {
            return;
        }

        /* EQ1 */
        if ((myRoutine.getRoutineEq1Id().equals("-1") ||
                        myRoutine.getRoutineEq1Id().equals("") ||
                        myRoutine.getRoutineEq1Id() == null)) {
            isMoreAgent = false;
            return;
        } else {
            numAgents ++;
            RecordAgent tmpAgentRecord1 = new RecordAgent();
            String agent1MacId = myRoutine.getRoutineEq1Id();
            tmpAgentRecord1.setAgentMacId(agent1MacId);
            tmpAgentRecord1.setAgentName(MainActivity.hereDB.getMyHereAgent(agent1MacId).getMyeqName());
            tmpAgentRecord1.setAgentType(MainActivity.hereDB.getMyHereAgent(agent1MacId).getMyeqType());

            tmpAgentRecord1.setGoalSet(getIntFromGoal(GOAL_SET, myRoutine.getRoutineEq1Goal()));
            tmpAgentRecord1.setGoalCount(getIntFromGoal(GOAL_COUNT, myRoutine.getRoutineEq1Goal()));
            tmpAgentRecord1.setGoalTime(getIntFromGoal(GOAL_TIME, myRoutine.getRoutineEq1Goal()));

            tmpAgentRecord1.setRecordCount(0);
            tmpAgentRecord1.setRecordTime(0);

            agentRecords.add(tmpAgentRecord1);
            Log.d("InitializeAgentRecords", "A record is added: "
                    + tmpAgentRecord1.getAgentName() + " ["
                    + tmpAgentRecord1.getGoalSet() + "|"
                    + tmpAgentRecord1.getGoalCount() + "|"
                    + tmpAgentRecord1.getGoalTime() + "]");
            Log.d("InitializeAgentRecords", "Agent num: " + numAgents);

        }

        if(!isMoreAgent)    return;

        /* EQ2 */
        if ((myRoutine.getRoutineEq2Id().equals("-1") ||
                        myRoutine.getRoutineEq2Id().equals("") ||
                        myRoutine.getRoutineEq2Id() == null)) {
            isMoreAgent = false;
            return;
        } else {
            numAgents ++;
            RecordAgent tmpAgentRecord2 = new RecordAgent();
            String agent2MacId = myRoutine.getRoutineEq2Id();
            tmpAgentRecord2.setAgentMacId(agent2MacId);
            tmpAgentRecord2.setAgentName(MainActivity.hereDB.getMyHereAgent(agent2MacId).getMyeqName());
            tmpAgentRecord2.setAgentType(MainActivity.hereDB.getMyHereAgent(agent2MacId).getMyeqType());

            tmpAgentRecord2.setGoalSet(getIntFromGoal(GOAL_SET, myRoutine.getRoutineEq2Goal()));
            tmpAgentRecord2.setGoalCount(getIntFromGoal(GOAL_COUNT, myRoutine.getRoutineEq2Goal()));
            tmpAgentRecord2.setGoalTime(getIntFromGoal(GOAL_TIME, myRoutine.getRoutineEq2Goal()));

            tmpAgentRecord2.setRecordCount(0);
            tmpAgentRecord2.setRecordTime(0);

            agentRecords.add(tmpAgentRecord2);
            Log.d("InitializeAgentRecords", "A record is added: "
                    + tmpAgentRecord2.getAgentName() + " ["
                    + tmpAgentRecord2.getGoalSet() + "|"
                    + tmpAgentRecord2.getGoalCount() + "|"
                    + tmpAgentRecord2.getGoalTime() + "]");
            Log.d("InitializeAgentRecords", "Agent num: " + numAgents);

        }

        if(!isMoreAgent)    return;

        /* EQ3 */
        if ((myRoutine.getRoutineEq3Id().equals("-1") ||
                        myRoutine.getRoutineEq3Id().equals("") ||
                        myRoutine.getRoutineEq3Id() == null)) {
            isMoreAgent = false;
            return;
        } else {
            numAgents ++;
            RecordAgent tmpAgentRecord3 = new RecordAgent();
            String agent3MacId = myRoutine.getRoutineEq3Id();
            tmpAgentRecord3.setAgentMacId(agent3MacId);
            tmpAgentRecord3.setAgentName(MainActivity.hereDB.getMyHereAgent(agent3MacId).getMyeqName());
            tmpAgentRecord3.setAgentType(MainActivity.hereDB.getMyHereAgent(agent3MacId).getMyeqType());

            tmpAgentRecord3.setGoalSet(getIntFromGoal(GOAL_SET, myRoutine.getRoutineEq3Goal()));
            tmpAgentRecord3.setGoalCount(getIntFromGoal(GOAL_COUNT, myRoutine.getRoutineEq3Goal()));
            tmpAgentRecord3.setGoalTime(getIntFromGoal(GOAL_TIME, myRoutine.getRoutineEq3Goal()));

            tmpAgentRecord3.setRecordCount(0);
            tmpAgentRecord3.setRecordTime(0);

            agentRecords.add(tmpAgentRecord3);
            Log.d("InitializeAgentRecords", "A record is added: "
                    + tmpAgentRecord3.getAgentName() + " ["
                    + tmpAgentRecord3.getGoalSet() + "|"
                    + tmpAgentRecord3.getGoalCount() + "|"
                    + tmpAgentRecord3.getGoalTime() + "]");
            Log.d("InitializeAgentRecords", "Agent num: " + numAgents);

        }

        if(!isMoreAgent)    return;

        /* EQ4 */
        if ((myRoutine.getRoutineEq4Id().equals("-1") ||
                        myRoutine.getRoutineEq4Id().equals("") ||
                        myRoutine.getRoutineEq4Id() == null)) {
            isMoreAgent = false;
            return;
        } else {
            numAgents ++;
            RecordAgent tmpAgentRecord4 = new RecordAgent();
            String agent4MacId = myRoutine.getRoutineEq4Id();
            tmpAgentRecord4.setAgentMacId(agent4MacId);
            tmpAgentRecord4.setAgentName(MainActivity.hereDB.getMyHereAgent(agent4MacId).getMyeqName());
            tmpAgentRecord4.setAgentType(MainActivity.hereDB.getMyHereAgent(agent4MacId).getMyeqType());

            tmpAgentRecord4.setGoalSet(getIntFromGoal(GOAL_SET, myRoutine.getRoutineEq4Goal()));
            tmpAgentRecord4.setGoalCount(getIntFromGoal(GOAL_COUNT, myRoutine.getRoutineEq4Goal()));
            tmpAgentRecord4.setGoalTime(getIntFromGoal(GOAL_TIME, myRoutine.getRoutineEq4Goal()));

            tmpAgentRecord4.setRecordCount(0);
            tmpAgentRecord4.setRecordTime(0);

            agentRecords.add(tmpAgentRecord4);
            Log.d("InitializeAgentRecords", "A record is added: "
                    + tmpAgentRecord4.getAgentName() + " ["
                    + tmpAgentRecord4.getGoalSet() + "|"
                    + tmpAgentRecord4.getGoalCount() + "|"
                    + tmpAgentRecord4.getGoalTime() + "]");
            Log.d("InitializeAgentRecords", "Agent num: " + numAgents);

        }

        if(!isMoreAgent)    return;

        /* EQ5 */
        if ((myRoutine.getRoutineEq5Id().equals("-1") ||
                        myRoutine.getRoutineEq5Id().equals("") ||
                        myRoutine.getRoutineEq5Id() == null)) {
            isMoreAgent = false;
            return;
        } else {
            numAgents ++;
            RecordAgent tmpAgentRecord5 = new RecordAgent();
            String agent5MacId = myRoutine.getRoutineEq5Id();
            tmpAgentRecord5.setAgentMacId(agent5MacId);
            tmpAgentRecord5.setAgentName(MainActivity.hereDB.getMyHereAgent(agent5MacId).getMyeqName());
            tmpAgentRecord5.setAgentType(MainActivity.hereDB.getMyHereAgent(agent5MacId).getMyeqType());

            tmpAgentRecord5.setGoalSet(getIntFromGoal(GOAL_SET, myRoutine.getRoutineEq5Goal()));
            tmpAgentRecord5.setGoalCount(getIntFromGoal(GOAL_COUNT, myRoutine.getRoutineEq5Goal()));
            tmpAgentRecord5.setGoalTime(getIntFromGoal(GOAL_TIME, myRoutine.getRoutineEq5Goal()));

            tmpAgentRecord5.setRecordCount(0);
            tmpAgentRecord5.setRecordTime(0);

            agentRecords.add(tmpAgentRecord5);
            Log.d("InitializeAgentRecords", "A record is added: "
                    + tmpAgentRecord5.getAgentName() + " ["
                    + tmpAgentRecord5.getGoalSet() + "|"
                    + tmpAgentRecord5.getGoalCount() + "|"
                    + tmpAgentRecord5.getGoalTime() + "]");
            Log.d("InitializeAgentRecords", "Agent num: " + numAgents);

        }

        Log.d("InitializeAgentRecords", "Agent num: " + numAgents);

    }


    private int getIntFromGoal(int target, String rawGoal) {

        //Type 1. 3|15|-1
        //Type 2. 2|-1|60

        int goalType = 0;   //1: count times, 2: count secs, 3: count times & secs

        String set = "";    //sets
        String count = "";  //times
        String time = "";   //secs

        StringTokenizer tokens = new StringTokenizer(rawGoal, "|");
        set = tokens.nextToken();
        count = tokens.nextToken();
        time = tokens.nextToken();

        //Single or multiple
        int intSet = Integer.parseInt(set);
        int intCount = Integer.parseInt(count);
        int intTime = Integer.parseInt(time);

        switch (target) {
            case 51:
                return intSet;
            case 52:
                return intCount;
            case 53:
                return intTime;
            default:
                return -1;
        }
    }

    private String secondToTimerString(int seconds) {

        int mins = seconds / 60;
        int secs = seconds % 60;

        String timerString = String.format("%02d:%02d", mins, secs);

        return timerString;
    }



    private Runnable increaseTimer = new Runnable() {
        @Override
        public void run() {

            //Obtain a current RecordAgent for recording
            if (numAgents > 0) {
                currentAgent = agentRecords.get(currentOrder);
            }



            Random rand = new Random();
            int randNum;

            //TODO: tv_eq_count와 currentRecordCount는 성근이형 코드로 대체
            if (isTimerRunning) {
                currentRecordTime++;
                randNum = rand.nextInt((RAND_MAX - RAND_MIN) + 1) + RAND_MIN;
                currentRecordCount += randNum;
            }
            tv_timer.setText(secondToTimerString(currentRecordTime));
            tv_eq_count.setText(String.format("%d", currentRecordCount));
        }
    };


    private void initializeRecord() {
        countCurrentEq = 0;
        currentRecordTime = 0;

        //Restart exercising -> Run timer
        isTimerRunning = true;
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.doingexercise_btn_back:
                AlertDialog quitExercisingAlert = askQuitExercising();
                quitExercisingAlert.show();
                break;
            case R.id.doingexercise_btn_skipgonext:
                if (isTimerRunning) {
                    isTimerRunning = false;
                }
                //timer.stop(increaseTimer);

                Log.d("DoingExerciseOrder", "currentOrder: " + currentOrder + ", agentRecords.size(): " + agentRecords.size());

                //If there is any remaining equipment (or now doing the last one)
                if (currentOrder + 1 < numAgents) {
                    AlertDialog goToNextExerciseAlert = askGoToNextExercise();
                    goToNextExerciseAlert.show();
                }
                //If there is no remaining equipment to do
                else {
                    AlertDialog finishExerciseAlert = askFinishExercise();
                    finishExerciseAlert.show();
                }



                //Toast.makeText(getApplicationContext(), "Skip this equipment\nor Go to next step", Toast.LENGTH_SHORT).show();
                break;
        }
    }



    private AlertDialog askQuitExercising() {
        AlertDialog myDeleteDialogBox = new AlertDialog.Builder(this)
                .setTitle("Quit Exercising")
                .setMessage("Are you sure you want to stop exercising and go to the main page?")
                .setPositiveButton("Give up", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Exercising is stopped.", Toast.LENGTH_SHORT).show();
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        myDeleteDialogBox.setCanceledOnTouchOutside(false);

        return myDeleteDialogBox;
    }

    private AlertDialog askGoToNextExercise() {
        AlertDialog myGoNextDialogBox = new AlertDialog.Builder(this)
                .setTitle("Go to next step")
                .setMessage("Are you sure you want to finish current exercise?")
                .setPositiveButton("Go next!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "Exercising is stopped.", Toast.LENGTH_SHORT).show();

                        //TODO: Store previous exercise record to agentRecords
                        agentRecords.get(currentOrder).setRecordCount(currentRecordCount);
                        agentRecords.get(currentOrder).setRecordTime(currentRecordTime);

                        //if currentOrder = 2, numAgent = 4
                        currentOrder++;

                        //TODO: Initialize DoingExerciseActivity
                        //TODO: Restart timer & measuring sensor
                        isTimerRunning = true;
                        initMeasureValues();
                        initAgentValues();

                        Log.d("DoingExerciseOrder", "currentOrder: " + currentOrder + ", agentRecords.size(): " + agentRecords.size());


                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!isTimerRunning) {
                            isTimerRunning = true;
                        }
                        dialog.dismiss();
                    }
                })
                .create();

        myGoNextDialogBox.setCanceledOnTouchOutside(false);

        return myGoNextDialogBox;
    }

    private AlertDialog askFinishExercise() {
        AlertDialog myFinishDialogBox = new AlertDialog.Builder(this)
                .setTitle("Finish exercising")
                .setMessage("This is the last step of your routine.\nDo you want to finish exercising?")
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Exercising is stopped.", Toast.LENGTH_SHORT).show();

                        //TODO: Store previous exercise record to agentRecords
                        agentRecords.get(currentOrder).setRecordCount(currentRecordCount);
                        agentRecords.get(currentOrder).setRecordTime(currentRecordTime);

                        Log.d("agentRecords", "Exercise is finished.");
                        for (int i = 0; i < numAgents; i++) {
                            Log.d("agentRecords", "agentRecords[" + i + "]: " + agentRecords.get(i).getRecordCount() + " / " + agentRecords.get(i).getRecordTime());
                        }


                        //Go to FinishExerciseActivity
                        Intent intent_finishexercise = new Intent(getApplicationContext(), FinishExerciseActivity.class);
                        intent_finishexercise.putExtra("agentRecords", agentRecords);
                        intent_finishexercise.putExtra("routineName", myRoutine.getRoutineName());
                        startActivity(intent_finishexercise);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Do more!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!isTimerRunning) {
                            isTimerRunning = true;
                        }
                        dialog.dismiss();
                    }
                })
                .create();

        myFinishDialogBox.setCanceledOnTouchOutside(false);

        return myFinishDialogBox;
    }

    @Override
    public void onBackPressed() {
        AlertDialog quitExercisingAlert = askQuitExercising();
        quitExercisingAlert.show();
        //super.onBackPressed();
    }
}
