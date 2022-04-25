package com.example.projectbit;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;


import com.example.projectbit.Wasmachine.Wasmachine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Objects;

public class addWasmachine extends AppCompatActivity {
    TextView edtWasmachine, edtWasmachineType, edtMaxCapacity;
    Button btnSave;
    Wasmachine wasmachine = null;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    DatabaseReference reference;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wasmachine);

        edtWasmachine = findViewById(R.id.edtWasmachine);
        edtWasmachineType = findViewById(R.id.edtWasmachineType);
        edtMaxCapacity = findViewById(R.id.edtMaxCapacity);
        btnSave = findViewById(R.id.btnSave);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        firestore = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();

        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Wasmachine){
            wasmachine = (Wasmachine) object;
        }

        if (wasmachine != null){
            edtWasmachine.setText(wasmachine.getName());
            edtWasmachineType.setText(wasmachine.getType());
            edtMaxCapacity.setText("" + wasmachine.getMaxCapacity() + " KG");
        }

        btnSave.setOnClickListener(v -> addWasMachineToList());
    }

    private void addWasMachineToList() {

        final HashMap<String, Object> wasMachineAdd = new HashMap<>();

        wasMachineAdd.put("wasmachine", wasmachine.getIDWasmachine());

        reference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).updateChildren(wasMachineAdd);

        finish();
        startActivity(new Intent(this, UserOverview.class));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(addWasmachine.this, WasmachineOverviewActivity.class));
    }
}