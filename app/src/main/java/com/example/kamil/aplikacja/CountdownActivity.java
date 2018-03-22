package com.example.kamil.aplikacja;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CountdownActivity extends AppCompatActivity {

    EditText title,body,value;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = findViewById(R.id.notifyTitle);
        body = findViewById(R.id.notifyBody);
        value = findViewById(R.id.notifyValue);
        startButton = findViewById(R.id.startBtn);
    }

    public void startCountDownClick(View view) {
        long startTime = Long.parseLong(value.getText().toString());
        startTime *= 1000;
        new CountDownTimer(startTime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                startButton.setEnabled(false);
                value.setEnabled(false);
                value.setText(""+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                createNotification();
                startButton.setEnabled(true);
                value.setEnabled(true);
            }
        }.start();
    }

    private void createNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            CharSequence name = getString(R.string.name);
            String description = getString(R.string.channel_description);
            NotificationChannel mChannel = new NotificationChannel("channelNotification", "channel", NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,"channelNotification")
                .setContentTitle(title.getText())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(body.getText())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(0, notification.build());
    }

}
