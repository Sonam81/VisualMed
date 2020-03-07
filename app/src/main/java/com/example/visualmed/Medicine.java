package com.example.visualmed;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicines")
public class Medicine {


    @NonNull
    @PrimaryKey private String medicineName;

    @Ignore
    private List<MedicineTime> medicineTimes;




    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public List<MedicineTime> getMedicineTimes() {
        return medicineTimes;
    }

    public void setMedicineTimes(List<MedicineTime> medicineTimes) {
        this.medicineTimes = medicineTimes;
    }

}
