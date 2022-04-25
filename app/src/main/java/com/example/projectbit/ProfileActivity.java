package com.example.projectbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectbit.Wasmachine.Wasmachine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;


import java.util.HashMap;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    EditText txtEmail, txtNaam, txtAdres, txtPhone;
    Button btnUpdate;
    CircleImageView profileImage;
    TextView txtWasmachine;

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseFirestore db;

    Uri imageUri;
    String myUri = "";
    StorageTask task;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile pic");
        db = FirebaseFirestore.getInstance();

        txtNaam = findViewById(R.id.edtNaam);
        txtEmail = findViewById(R.id.edtEmailLogin);
        txtAdres = findViewById(R.id.edtAdres);
        txtPhone = findViewById(R.id.edtPhoneNumber);
        profileImage = findViewById(R.id.profileImage);
        btnUpdate = findViewById(R.id.btnUpdateProfile);
        txtWasmachine = findViewById(R.id.txtMachine);
        
        btnUpdate.setOnClickListener(v -> {
            validateAndSave();
            startActivity(new Intent(ProfileActivity.this, UserOverview.class));

        });

        profileImage.setOnClickListener(v -> startCropActivity());
        getUserInfo();
    }

    private void startCropActivity() {
        CropImage.activity()
                .start(this);
    }

    private void validateAndSave() {
        if (TextUtils.isEmpty(txtNaam.getText().toString())){
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(txtPhone.getText().toString())){
            Toast.makeText(this, "Phonenumber cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("name", txtNaam.getText().toString());
            userMap.put("phone", txtPhone.getText().toString());
            userMap.put("adres", txtAdres.getText().toString());

            reference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).updateChildren(userMap);
            updateProfile();
        }
    }

    private void getUserInfo() {
        reference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0){
                    if (snapshot.hasChild("image")){
                        String image = Objects.requireNonNull(snapshot.child("image").getValue()).toString();
                        Picasso.get().load(image).into(profileImage);
                    }
                    if (snapshot.hasChild("name")){
                        String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                        txtNaam.setText(name);
                    }
                    if (snapshot.hasChild("email")){
                        String email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                        txtEmail.setText(email);
                    }
                    if (snapshot.hasChild("phone")){
                        String phone = Objects.requireNonNull(snapshot.child("phone").getValue()).toString();
                        txtPhone.setText(phone);
                    }
                    if (snapshot.hasChild("adres")){
                        String adres = Objects.requireNonNull(snapshot.child("adres").getValue()).toString();
                        txtAdres.setText(adres);
                    }
                    if (snapshot.hasChild("wasmachine")){
                        String wasmachineID = snapshot.child("wasmachine").getValue().toString();
                        DocumentReference reference = db.collection("AllWasmachine").document(wasmachineID);
                        reference.get().addOnSuccessListener(documentSnapshot -> {
                            Wasmachine wasmachine = documentSnapshot.toObject(Wasmachine.class);
                            if (wasmachine != null) {
                                String wasmachineNaam = wasmachine.getName() + " " + wasmachine.getType();
                                txtWasmachine.setText(wasmachineNaam);

                            } else {
                                txtWasmachine.setText("No washing machine selected");
                            }
                        });


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                imageUri = result.getUri();

                profileImage.setImageURI(imageUri);
            }  else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }

        }else{
            Toast.makeText(this, "Error, try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {

        if (imageUri != null){
            final StorageReference fileRef = storageReference
                    .child(Objects.requireNonNull(auth.getCurrentUser()).getUid() + ".jpg");

            task = fileRef.putFile(imageUri);
            task.continueWithTask(task -> {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return fileRef.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()){
                    Uri dwonloadUrl = task.getResult();
                    myUri = dwonloadUrl.toString();

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("image", myUri);

                    reference.child(auth.getCurrentUser().getUid()).updateChildren(userMap);


                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, UserOverview.class));
    }
}