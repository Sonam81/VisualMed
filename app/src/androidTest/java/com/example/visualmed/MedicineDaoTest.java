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
import static junit.framework.TestCase.assertEquals;

public class MedicineDaoTest {
    private MedicineDAO medicineDAO;
    private MyAppDatabase db;

    @Before
    public void createDB(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, MyAppDatabase.class).build();
        medicineDAO = db.medicineDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setMedicineName("Paracetamol");

        MedicineTime medicineTime = new MedicineTime();
        medicineTime.setMedicineTime("2:00p.m.");

        List<MedicineTime> medicineTimes = new ArrayList<>();
        medicineTimes.add(medicineTime);
        medicine.setMedicineTimes(medicineTimes);

        medicineDAO.insertMedicineWithTime(medicine);
        List<MedicineWithTime> medicineList = medicineDAO.findMedicine("Paracetamol");

        assertEquals(medicineList.get(0).getMedicine().getMedicineName(), medicine.getMedicineName());

    }
}
