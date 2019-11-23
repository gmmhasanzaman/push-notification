package com.example.androidnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        title = getIntent().getStringExtra("value");
        /*if (title != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new NotificationFragment())
                    .commit();
        }*/

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new SignInFragment();
        ft.replace(R.id.fragmentContainer, fragment)
                .commit();




    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();


       /* if (currentUser == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new SignInFragment();
            ft.replace(R.id.fragmentContainer, fragment)
                    .commit();

        }*/

        if (currentUser != null && title != null){
            Log.d("title",title);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new NotificationFragment())
                    .addToBackStack(null)
                    .commit();
            return;
        }
        if (currentUser != null) {
            Toast.makeText(this, "user: "+currentUser.getEmail(), Toast.LENGTH_LONG).show();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new HomeFragment();
            ft.replace(R.id.fragmentContainer, fragment);
            ft.commit();

        }






    }


}
