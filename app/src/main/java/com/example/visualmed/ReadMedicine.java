package com.example.visualmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ReadMedicine extends AppCompatActivity {

    //stores medicines name and times
    List<MedicineDetail> medicineDetails;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_medicine);

        //get recycler view by id
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //get name of medicine in string and time in List<MedicineTime>
        List<MedicineWithTime> medicineWithTimes;
        medicineWithTimes = ManageMedicineActivity.myAppDatabase.medicineDAO().getAll();

        //needs to convert it to display in recyclerview
        medicineDetails = new ArrayList<>();
        for (int i = 0; i < medicineWithTimes.size();i++) {
            String medTimes = "";
            String medAlarmIds = "";
            for (int j = 0; j < medicineWithTimes.get(i).getMedicineTimes().size(); j++){
                medTimes = medTimes + medicineWithTimes.get(i).getMedicineTimes().get(j).getMedicineTime()+"  ";
                medAlarmIds = medAlarmIds + medicineWithTimes.get(i).getMedicineTimes().get(j).getMedicineAlarmId();
            }
            String medName = medicineWithTimes.get(i).getMedicine().getMedicineName();
            medicineDetails.add(new MedicineDetail(medTimes,medName.substring(0,1).toUpperCase()+medName.substring(1),0000));
        }
        //create adapter and display all the details in the card layout
        MedicineDisplayAdapter adapter = new MedicineDisplayAdapter(this, medicineDetails);
        recyclerView.setAdapter(adapter);
    }
}