package com.example.visualmed;

import android.content.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MedicineDaoTest{
    private MedicineDAO medicineDAO;
    private MyAppDatabase db;
    Medicine medicine;
    MedicineTime medicineTime;

    @Before
    public void createDB(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MyAppDatabase.class).build();
        medicineDAO = db.medicineDAO();
    }



    @Test
    public void writeReadMedicineTest() throws Exception {
        medicine = new Medicine();
        medicine.setMedicineName("Paracetamol");

        medicineTime = new MedicineTime();
        medicineTime.setMedicineTime("2:00p.m.");
        medicineTime.setMedicineAlarmId(23333);

        List<MedicineTime> medicineTimes = new ArrayList<>();
        medicineTimes.add(medicineTime);
        medicine.setMedicineTimes(medicineTimes);
        medicineDAO.insertMedicineWithTime(medicine);

        List<MedicineWithTime> medicineList = medicineDAO.findMedicine("Paracetamol");
        assertEquals(medicineList.get(0).getMedicine().getMedicineName(), medicine.getMedicineName());
    }

    @Test
    public void readMedicineTimeTest() throws Exception{
        medicine = new Medicine();
        medicine.setMedicineName("Paracetamol");

        medicineTime = new MedicineTime();
        medicineTime.setMedicineTime("2:00p.m.");
        medicineTime.setMedicineAlarmId(23333);

        List<MedicineTime> medicineTimes = new ArrayList<>();
        medicineTimes.add(medicineTime);
        medicine.setMedicineTimes(medicineTimes);
        medicineDAO.insertMedicineWithTime(medicine);

        List<MedicineWithTime> medicineList = medicineDAO.getAll();
        assertEquals(medicineList.get(0).getMedicineTimes().get(0).getMedicineTime(), medicineTime.getMedicineTime());
    }

    @Test
    public void readMedicineAlarmId() throws Exception{
        medicine = new Medicine();
        medicine.setMedicineName("Paracetamol");

        medicineTime = new MedicineTime();
        medicineTime.setMedicineTime("2:00p.m.");
        medicineTime.setMedicineAlarmId(23333);

        List<MedicineTime> medicineTimes = new ArrayList<>();
        medicineTimes.add(medicineTime);
        medicine.setMedicineTimes(medicineTimes);
        medicineDAO.insertMedicineWithTime(medicine);

        List<MedicineWithTime> medicineList = medicineDAO.getAll();
        assertEquals(medicineList.get(0).getMedicineTimes().get(0).getMedicineAlarmId(), medicineTime.getMedicineAlarmId());
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
