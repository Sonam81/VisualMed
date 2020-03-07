package com.example.visualmed;

import java.util.List;

public class MedicineDetail {

    private String medicineName;
    private String medicineTime;

    public MedicineDetail(String medicineName, String medicineTime) {
        this.medicineName = medicineName;
        this.medicineTime = medicineTime;
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
}
