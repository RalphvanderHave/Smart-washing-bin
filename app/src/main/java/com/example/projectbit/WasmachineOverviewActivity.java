package com.example.projectbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageButton;
import android.widget.Toast;

import com.example.projectbit.Wasmachine.Wasmachine;
import com.example.projectbit.Wasmachine.WasmachineAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class WasmachineOverviewActivity extends AppCompatActivity {


    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    WasmachineAdapter adapter;
    List<Wasmachine> list;
    ImageButton saveToUser;
    FirebaseAuth auth;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wasmachine_overview);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        saveToUser = findViewById(R.id.btnSaveToUser);

        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new WasmachineAdapter(this, list);
        recyclerView.setAdapter(adapter);

        firestore.collection("AllWasmachine")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Wasmachine wasmachine = documentSnapshot.toObject(Wasmachine.class);
                            list.add(wasmachine);
                            adapter.notifyDataSetChanged();
                        }
                    } else{
                        Toast.makeText(getApplicationContext(), "Error: "+ task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WasmachineOverviewActivity.this, UserOverview.class));
    }
}