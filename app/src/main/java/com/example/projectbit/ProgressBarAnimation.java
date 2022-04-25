package com.example.projectbit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProgressBarAnimation extends Animation {
    private Context context;
    private final ProgressBar progressBar;
    private final TextView textView;
    private final float from;
    private final float to;

    public ProgressBarAnimation(Context context, ProgressBar progressBar, TextView textView, float from, float to){
        this.context = context;
        this.progressBar = progressBar;
        this.textView = textView;
        this.from = from;
        this.to = to;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t){
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
        textView.setText((int) value + " %");

        if (value == to){
            FirebaseAuth oAuth = FirebaseAuth.getInstance();
            oAuth.addAuthStateListener(authStateListener);
        }
    }


    FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> context.startActivity(new Intent(context, SignUpActivity.class));
}
