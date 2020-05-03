package com.example.visualmed;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DuplicateMedicineTest {
    MedicineDAO medicineDAO;
    MyAppDatabase db;
    private Medicine medicine,medicine_duplicate;
    private MedicineTime medicineTime,medicineTime_duplicate;

    @Before
    public void createDB(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MyAppDatabase.class).build();
        medicineDAO = db.medicineDAO();

    }

    public void insertData(){
        medicine = new Medicine();
        medicine.setMedicineName("Paracetamol");
        medicineTime = new MedicineTime();
        medicineTime.setMedicineTime("2:00p.m.");
        medicineTime.setMedicineAlarmId(23333);
        List<MedicineTime> medicineTimes = new ArrayList<>();
        medicineTimes.add(medicineTime);
        medicine.setMedicineTimes(medicineTimes);
        medicineDAO.insertMedicineWithTime(medicine);
    }

    @Test
    public void test_duplicate_medicine(){
        medicine_duplicate = new Medicine();
        medicine_duplicate.setMedicineName("Paracetamol");

        medicineTime_duplicate = new MedicineTime();
        medicineTime_duplicate.setMedicineTime("2:00p.m.");
        medicineTime_duplicate.setMedicineAlarmId(23333);

        List<MedicineTime> medicineTimes_duplicate = new ArrayList<>();
        medicineTimes_duplicate.add(medicineTime_duplicate);
        medicine_duplicate.setMedicineTimes(medicineTimes_duplicate);

        List<MedicineWithTime> medicineWithTimes = medicineDAO.findMedicine(medicine_duplicate.getMedicineName());
        assertNotNull(medicineWithTimes);
    }

    @Test
    public void test_non_duplicate_medicine(){
        Medicine medicine2 = new Medicine();
        medicine2.setMedicineName("Metformin");

        MedicineTime medicineTime2 = new MedicineTime();
        medicineTime2.setMedicineTime("2:00p.m.");
        medicineTime2.setMedicineAlarmId(11111);

        List<MedicineTime> medicineTimes2 = new ArrayList<>();
        medicineTimes2.add(medicineTime2);
        medicine2.setMedicineTimes(medicineTimes2);

        List<MedicineWithTime> medicineWithTimes = medicineDAO.findMedicine(medicine2.getMedicineName());
        assertNotNull(medicineWithTimes);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
