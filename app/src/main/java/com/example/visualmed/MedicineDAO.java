package com.example.visualmed;

import java.util.List;

import androidx.annotation.WorkerThread;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class MedicineDAO {

    @Transaction
    @Query("Select * from medicines")
    public abstract List<MedicineWithTime> getAll();


    @Insert
    public abstract void insertMedicine(Medicine medicine);

    @Insert
    public abstract void insertMedicineTime(List<MedicineTime> medicineTimes);

    @Delete
    public abstract void deleteMedicine(Medicine medicine);

    @Delete
    public abstract void deleteAll(Medicine medicine);


    @Query("DELETE FROM medicines")
    public abstract void deleteAll();

    @Transaction
    public void insertMedicineWithTime(Medicine medicine) {
        List<MedicineTime> medicineTimes = medicine.getMedicineTimes();
        for (int i = 0; i < medicineTimes.size(); i++) {
            medicineTimes.get(i).setNameOfMedicine(medicine.getMedicineName());
        }
        insertMedicine(medicine);
        insertMedicineTime(medicineTimes);
    }
//
//    @Transaction
//    public void deleteMedicineWithTime(Medicine medicine){
//        List<MedicineTime> medicineTimes = medicine.getMedicineTimes();
//        for (int i = 0; i < medicineTimes.size(); i++) {
//            deleteMedicineTime(medicine.getMedicineTimes().get(i));
//        }
//        deleteMedicine(medicine);
//    }

    @Transaction
    @Query("Select * from medicines where medicineName =:medicineName ")
    public abstract List<MedicineWithTime> findMedicine(String medicineName);

}
