package com.example.visualmed;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicine_time")
public class MedicineTime {

    @PrimaryKey(autoGenerate = true) public int id;
    private String medicineTime;
    private String nameOfMedicine;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
