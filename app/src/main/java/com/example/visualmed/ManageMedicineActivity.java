package com.example.visualmed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;



public class ManageMedicineActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicine);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        System.out.println(message);

        switch (message){
            case "Add Medicine":
                System.out.println("Added");
                break;

            case "Delete Medicine":
                System.out.println("Delete");
                break;

            case "Update Medicine":
                System.out.println("Update");
                break;

            default:
                System.out.println("Default");
        }

    }
}

