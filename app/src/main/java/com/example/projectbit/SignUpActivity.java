package com.example.projectbit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectbit.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    Button btnRegister;
    EditText edtEmail, edtPassword, edtConfirmPassword;
    TextView txtSignIn;
    FirebaseAuth auth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassWord);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtSignIn = findViewById(R.id.txtSign_in);

        if (auth.getCurrentUser() != null){
            startActivity(new Intent(SignUpActivity.this, UserOverview.class));
            finish();
        }

        txtSignIn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));

        btnRegister.setOnClickListener(v -> createUser());

    }
    private void createUser(){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String confirm = edtConfirmPassword.getText().toString();
        String IDArduino = "ARDUINO_Janneke";

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirm)){
            Toast.makeText(this, "Passwords doesn't match!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6){
            Toast.makeText(this, "Password length must be greater then 6 letters!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, task -> {
                    if (task.isSuccessful()){
                        User user = new User(email, IDArduino);
                        String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                        database.getReference().child("Users").child(id).setValue(user);

                        Toast.makeText(SignUpActivity.this, "Sign up successfull", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, UserOverview.class));
                    }else{
                        Toast.makeText(SignUpActivity.this, "Error: "+ task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}