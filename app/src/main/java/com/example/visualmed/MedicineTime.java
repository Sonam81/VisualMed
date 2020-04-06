package com.example.visualmed;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicine_time")
public class MedicineTime {

    @PrimaryKey
    private int medicineAlarmId;
    private String medicineTime;
    private String nameOfMedicine;

    public int getMedicineAlarmId() {
        return medicineAlarmId;
    }

    public void setMedicineAlarmId(int medicineAlarmId) {
        this.medicineAlarmId = medicineAlarmId;
    }


    public String getMedicineTime() {
        return medicineTime;
    }

    public void setMedicineTime(String medicineTime) {
        this.medicineTime = medicineTime;
    }

    public String getNameOfMedicine() {
        return nameOfMedicine;
    }

    public void setNameOfMedicine(String medicineName) {
        this.nameOfMedicine = medicineName;
    }
}
