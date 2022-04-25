package com.example.projectbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserOverview extends AppCompatActivity {
    Button btnAddWasmachine, btnProfile, btnLogout, btnStatus;
    TextView txtProfileName;
    CircleImageView profilePicture;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_overview);

        btnAddWasmachine = findViewById(R.id.btnAddWasmachine);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnStatus = findViewById(R.id.btnStatus);
        txtProfileName = findViewById(R.id.mainProfileName);
        profilePicture = findViewById(R.id.profileImageMain);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        btnAddWasmachine.setOnClickListener(view -> {
            startActivity(new Intent(UserOverview.this, WasmachineOverviewActivity.class));
            finish();
        });

        btnProfile.setOnClickListener(view -> {
            startActivity(new Intent(UserOverview.this, ProfileActivity.class));
            finish();
        });

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            signOutUser();
        });

        btnStatus.setOnClickListener(v -> {
            startActivity(new Intent(UserOverview.this, StatusOverviewActivity.class));
            finish();
        });


        getUserInfo();
    }

    private void getUserInfo() {
        reference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0){
                    if (snapshot.hasChild("image")){
                        String image = Objects.requireNonNull(snapshot.child("image").getValue()).toString();
                        Picasso.get().load(image).into(profilePicture);
                    }
                    if (snapshot.hasChild("name")){
                        String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                        txtProfileName.setText(name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void signOutUser() {
        Intent mainActivity = new Intent(UserOverview.this, LoginActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);
        finish();
    }

}