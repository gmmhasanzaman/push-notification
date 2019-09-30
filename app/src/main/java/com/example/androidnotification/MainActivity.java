package com.example.androidnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID =
            "com.example.androidnotification.CHANNEL_ID";
    public static final String CHANNEL_NAME =
            "com.example.androidnotification.CHANNEL_NAME";
    public static final String CHANNEL_DESC =
            "com.example.androidnotification.CHANNEL_DESC";

    private Button clickBtn;
    private TextView tokenTV;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

       /* if android version is greater than or equal Oreo
        then we need to create a notification channel*/
        createChannel();

        /*click notification to go an activity*/
        initAlertDetails();

        clickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //displayNotification();

                createToken();
            }
        });
    }
    private void initViews() {
        clickBtn = findViewById(R.id.clickBtn);
        tokenTV = findViewById(R.id.tokenTV);

    }

    private void createToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){
                            if (task.getResult() != null){
                                String token = task.getResult().getToken();
                                tokenTV.setText("Token: "+ token);
                            }

                        }else {
                            tokenTV.setText(task.getException().getMessage());
                        }
                    }
                });
    }

    private void initAlertDetails() {

        Intent intent = new Intent(this, AlertDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }




    private void displayNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_noti_black_24dp)
                        .setContentTitle("Notification Title")
                        .setContentText("My Notification")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(this);

        notificationManagerCompat.notify(1, builder.build());
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANNEL_DESC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }
}
