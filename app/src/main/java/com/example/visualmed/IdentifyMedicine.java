package com.example.visualmed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Token;
import org.ahocorasick.trie.Trie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class IdentifyMedicine extends AppCompatActivity {

    String information = "";
    ArrayList<String> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_medicine);
        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2;
        ref2 = ref1.child("medicine");

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                // Result will be holded Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Map map = (Map) dsp.getValue();

                    userList.add(String.valueOf(map.get("medicine_name"))); //add result into array list
                }
                men(information);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = getIntent();
        information = intent.getExtras().getString("detail");
        if(!information.equals("")){
            String finalInformation = information.replaceAll("\\s", "").toLowerCase();
            System.out.println("THis is the final information ............ "+finalInformation);
        }
        else{
            System.out.println("Empty");
        }
    }

    public void men(String details){
        Trie trie = Trie.builder().ignoreOverlaps().onlyWholeWords().ignoreCase()
                .addKeywords(userList)
                .build();

        Emit firstMatch = trie.firstMatch(details);
        System.out.println("The first match is ................." + firstMatch.getKeyword());
    }

}
