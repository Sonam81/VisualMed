package com.example.visualmed;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class MedicineWithTime {

    @Embedded private Medicine medicine;
    @Relation(
            parentColumn = "medicineName",
            entityColumn = "nameOfMedicine"
    )
    private List<MedicineTime> medicineTimes;

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public List<MedicineTime> getMedicineTimes() {
        return medicineTimes;
    }

    public void setMedicineTimes(List<MedicineTime> medicineTimes) {
        this.medicineTimes = medicineTimes;
    }

}
