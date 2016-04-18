package com.example.user.wase.model;

/**
 * Created by user on 2016-04-18.
 */
public class Equipment {

    String equipmentID;
    String equipmentName;
    String equipmentSensorID;
    String equipmentRegisterDate;

    //Equipment type
    //0: NO NAME, 1: Dumbbells, 2: Pushup bars, 3: Jumprope, 4: Hoola-hoop, 5: Others
    int equipmentType;


    public Equipment() {
        this.equipmentID = "NO ID";
        this.equipmentSensorID = "NO SENSOR ID";
        this.equipmentRegisterDate = "NO DATE";
        this.equipmentName = "NO NAME";
        this.equipmentType = 0;
    }

    public Equipment(String equipmentName, int equipmentType) {
        this.equipmentID = "NO ID";
        this.equipmentSensorID = "NO SENSOR ID";
        this.equipmentRegisterDate = "NO DATE";
        this.equipmentName = equipmentName;
        this.equipmentType = equipmentType;

    }

    public Equipment(String equipmentID, String equipmentName, int equipmentType) {
        this.equipmentSensorID = "NO SENSOR ID";
        this.equipmentRegisterDate = "NO DATE";
        this.equipmentID = equipmentID;
        this.equipmentName = equipmentName;
        this.equipmentType = equipmentType;
    }

    public Equipment(String equipmentID, String equipmentName, String equipmentRegisterDate, int equipmentType) {
        this.equipmentSensorID = "NO SENSOR ID";
        this.equipmentID = equipmentID;
        this.equipmentName = equipmentName;
        this.equipmentRegisterDate = equipmentRegisterDate;
        this.equipmentType = equipmentType;
    }

    public Equipment(String equipmentID, String equipmentName, String equipmentSensorID, String equipmentRegisterDate, int equipmentType) {
        this.equipmentID = equipmentID;
        this.equipmentName = equipmentName;
        this.equipmentSensorID = equipmentSensorID;
        this.equipmentRegisterDate = equipmentRegisterDate;
        this.equipmentType = equipmentType;
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(String equipmentID) {
        this.equipmentID = equipmentID;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentSensorID() {
        return equipmentSensorID;
    }

    public void setEquipmentSensorID(String equipmentSensorID) {
        this.equipmentSensorID = equipmentSensorID;
    }

    public String getEquipmentRegisterDate() {
        return equipmentRegisterDate;
    }

    public void setEquipmentRegisterDate(String equipmentRegisterDate) {
        this.equipmentRegisterDate = equipmentRegisterDate;
    }

    public int getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(int equipmentType) {
        this.equipmentType = equipmentType;
    }
}
