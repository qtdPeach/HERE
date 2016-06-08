package com.example.user.wase.model;

import com.example.user.wase.view.activity.MainActivity;

/**
 * Created by user on 2016-06-08.
 */
public class AgentRecord {
    String agentName;
    String agentMacId;
    int agentType;

    int goalSet;
    int goalCount;
    int goalTime;

    int recordCount;
    int recordTime;

    public AgentRecord() {
        agentName = "-1";
        agentMacId = "-1";

        goalSet = -1;
        goalCount = -1;
        goalTime = -1;

        agentType = 1;
        recordCount = 0;
        recordTime = 0;
    }

    public AgentRecord(String agentMacId) {
        this.agentMacId = agentMacId;

        MyHereAgent tmpAgent = MainActivity.hereDB.getMyHereAgent(agentMacId);
        this.agentName = tmpAgent.getMyeqName();
        this.agentType = tmpAgent.getMyeqType();

        goalSet = -1;
        goalCount = -1;
        goalTime = -1;

        recordCount = 0;
        recordTime = 0;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentMacId() {
        return agentMacId;
    }

    public void setAgentMacId(String agentMacId) {
        this.agentMacId = agentMacId;
    }

    public int getAgentType() {
        return agentType;
    }

    public void setAgentType(int agentType) {
        this.agentType = agentType;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(int recordTime) {
        this.recordTime = recordTime;
    }

    public int getGoalSet() {
        return goalSet;
    }

    public void setGoalSet(int goalSet) {
        this.goalSet = goalSet;
    }

    public int getGoalCount() {
        return goalCount;
    }

    public void setGoalCount(int goalCount) {
        this.goalCount = goalCount;
    }

    public int getGoalTime() {
        return goalTime;
    }

    public void setGoalTime(int goalTime) {
        this.goalTime = goalTime;
    }

    public void setGoal(int goalSet, int goalCount, int goalTime) {
        this.goalSet = goalSet;
        this.goalCount = goalCount;
        this.goalTime = goalTime;
    }
}
