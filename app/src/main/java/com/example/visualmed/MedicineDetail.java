package com.example.visualmed;

import java.util.List;

public class MedicineDetail {

    private String medicineName;
    private String medicineTime;
    private int medicineAlarmId;

    public MedicineDetail(String medicineName, String medicineTime, int medicineAlarmId) {
        this.medicineName = medicineName;
        this.medicineTime = medicineTime;
        this.medicineAlarmId = medicineAlarmId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineTime() {
        return medicineTime;
    }

    public void setMedicineTime(String medicineTime) {
        this.medicineTime = medicineTime;
    }

    public int getMedicineAlarmId() {
        return medicineAlarmId;
    }

    public void setMedicineAlarmId(int medicineAlarmId) {
        this.medicineAlarmId = medicineAlarmId;
    }
}
