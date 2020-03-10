package com.example.visualmed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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
            for (int j = 0; j < medicineWithTimes.get(i).getMedicineTimes().size(); j++){

                medTimes = medTimes + medicineWithTimes.get(i).getMedicineTimes().get(j).getMedicineTime()+"\n";
            }
            medicineDetails.add(new MedicineDetail(medicineWithTimes.get(i).getMedicine().getMedicineName(),medTimes));
            Log.i("name",medTimes);

        }
        MedicineDisplayAdapter adapter = new MedicineDisplayAdapter(this, medicineDetails);

        recyclerView.setAdapter(adapter);
    }


}



















//
//        for (int z = 0; z < medicineWithTimes.size();z++){
//            Log.i("Medicine With Time",z +". "+medicineWithTimes.get(z).getMedicine().getMedicineName());
//        }
//
//        int k = medicineWithTimes.size() - 1;
//        Log.i("Last",medicineWithTimes.get(k).getMedicineTimes().get(0).getNameOfMedicine()+" "+
//                medicineWithTimes.get(k).getMedicineTimes().get(0).getMedicineTime());
