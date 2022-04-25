package com.example.projectbit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectbit.Models.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Objects;

public class StatusOverviewActivity extends AppCompatActivity {

    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    String[] washmachineId = new String[1];
    int[] capacity = new int[1];
    private static final String TAG = StatusOverviewActivity.class.getSimpleName();
    long[] weights = new long[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_status_overview);

        TextView humidityView = (TextView)findViewById(R.id.humidityView);
        TextView totalWeightView = (TextView)findViewById(R.id.totalWeightView);
        TextView whiteWeightView = (TextView)findViewById(R.id.whiteWeightView);
        TextView colorWeightView = (TextView)findViewById(R.id.colorWeightView);
        TextView blackWeightView = (TextView)findViewById(R.id.blackWeightView);
        TextView maxCapacity = (TextView)findViewById(R.id.maxCapacity);

        ValueEventListener postListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                User userObj = snapshot.child("Users").child(userId).getValue(User.class);

                washmachineId[0] = Objects.requireNonNull(userObj).getWasmachine();
                String arduinoName = userObj.getIdArduino();

                Log.d("firestore", "wasmachine id = " + washmachineId[0]);
                DocumentReference docRef = firestore.collection("AllWasmachine").document(washmachineId[0]);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            capacity[0] = Objects.requireNonNull(document.getLong("maxCapacity")).intValue();
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData() + " " + capacity[0]);
                            maxCapacity.setText("Capaciteit " + document.getString("name") + " wasmachine: " + String.valueOf(capacity[0]) + " kg");
                            float blackPercentage;
                            float whitePercentage;
                            float colorPercentage;
                            long capacityInGrams = capacity[0] * 1000L;


                            colorPercentage = (float) weights[2]/ capacityInGrams  * 100;
                            whitePercentage = (float) weights[1] / capacityInGrams * 100;
                            blackPercentage = (float) weights[0] / capacityInGrams * 100;

                            Log.d(TAG, "Colorpercentage = " + colorPercentage);

                            PieChart pieChartWit, pieChartZwart, pieChartKleur;

                            pieChartWit = findViewById(R.id.pieChartWit);
                            pieChartKleur = findViewById(R.id.pieChartKleur);
                            pieChartZwart = findViewById(R.id.pieChartZwart);

                            pieChartWit.clearChart();
                            pieChartKleur.clearChart();
                            pieChartZwart.clearChart();

                            pieChartWit.setClickable(true);
                            pieChartKleur.setClickable(true);
                            pieChartZwart.setClickable(true);


                            pieChartWit.addPieSlice(
                                    new PieModel(
                                            "Used",
                                            whitePercentage,
                                            Color.parseColor("#00BCD4")));
                            pieChartWit.addPieSlice(
                                    new PieModel(
                                            "Capacity left",
                                            100 - whitePercentage,
                                            Color.parseColor("#D3D3D3")
                                    )
                            );

                            pieChartKleur.addPieSlice(new PieModel(
                                    "Used",
                                    colorPercentage,
                                    Color.parseColor("#8B0000")
                            ));
                            pieChartKleur.addPieSlice(new PieModel(
                                    "Capacity left",
                                    100 - colorPercentage,
                                    Color.parseColor("#D3D3D3")
                            ));

                            pieChartZwart.addPieSlice(new PieModel(
                                    "Used",
                                    blackPercentage,
                                    Color.parseColor("#000000")
                            ));

                            pieChartZwart.addPieSlice(new PieModel(
                                    "Capacity left",
                                    100 - blackPercentage,
                                    Color.parseColor("#D3D3D3")
                            ));

                            pieChartWit.setOnClickListener(v -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(StatusOverviewActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Reset white laundry?");
                                builder.setMessage("This will reset weight of white laundry");
                                builder.setPositiveButton("Confirm",
                                        (dialog, which) -> {
                                            db.child(arduinoName).child("colorDetection").child("TOTAL").setValue(weights[3] - weights[1]);
                                            db.child(arduinoName).child("colorDetection").child("WHITE").setValue(0);
                                        });
                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            });

                            pieChartZwart.setOnClickListener(v -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(StatusOverviewActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Reset black laundry?");
                                builder.setMessage("This will reset weight of black laundry");
                                builder.setPositiveButton("Confirm",
                                        (dialog, which) -> {
                                            db.child(arduinoName).child("colorDetection").child("TOTAL").setValue(weights[3] - weights[0]);
                                            db.child(arduinoName).child("colorDetection").child("BLACK").setValue(0);
                                            pieChartZwart.startAnimation();
                                        });
                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            });

                            pieChartKleur.setOnClickListener(v -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(StatusOverviewActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Reset colored laundry?");
                                builder.setMessage("This will reset weight of colored laundry");
                                builder.setPositiveButton("Confirm",
                                        (dialog, which) -> {
                                            db.child(arduinoName).child("colorDetection").child("TOTAL").setValue(weights[3] - weights[2]);
                                            db.child(arduinoName).child("colorDetection").child("COLOR").setValue(0);
                                        });
                                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            });

                            pieChartKleur.startAnimation();
                            pieChartZwart.startAnimation();
                            pieChartWit.startAnimation();
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });

                long blackWeight = snapshot.child(arduinoName).child("colorDetection").child("BLACK").getValue(Long.class);
                weights[0] = blackWeight;
                blackWeightView.setText(String.valueOf(blackWeight) + " grams");

                long whiteWeight = snapshot.child(arduinoName).child("colorDetection").child("WHITE").getValue(Long.class);
                whiteWeightView.setText(String.valueOf(whiteWeight) + " grams");
                weights[1] = whiteWeight;

                long colorWeight = snapshot.child(arduinoName).child("colorDetection").child("COLOR").getValue(Long.class);
                colorWeightView.setText(String.valueOf(colorWeight) + " grams");
                weights[2] = colorWeight;

                long totalWeight = snapshot.child(arduinoName).child("colorDetection").child("TOTAL").getValue(Long.class);
                totalWeightView.setText(String.valueOf(totalWeight) + " grams");
                weights[3] = totalWeight;

                String humidity = snapshot.child(arduinoName).child("humidity").getValue(String.class);
                humidityView.setText("Humidity: " + humidity);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Failed to read values", error.toException());
            }
        };

        db.addValueEventListener(postListener);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(StatusOverviewActivity.this, UserOverview.class));
    }
}
