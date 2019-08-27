package com.dipanshu.notifyme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity  {
    String channelid = "channel";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SensorManager sensorManager =
                (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        final Sensor proximitySensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        // Create listener
        SensorEventListener proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here
                if(sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                    // Detected something nearby
                    Log.e("Val=", String.valueOf(sensorEvent.values[0]));

                    NotificationManager notificationManager = (NotificationManager)
                            getSystemService(Context.
                                    NOTIFICATION_SERVICE);
                    assert notificationManager != null;
                    notificationManager.cancelAll();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        // Register it, specifying the polling interval in
// microseconds
        sensorManager.registerListener(proximitySensorListener,
                proximitySensor, 1000*100);


        Button btn = findViewById(R.id.btn);

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        if android version is above 8.0
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelid, "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        final EditText title=findViewById(R.id.input_edittitle);
        final EditText message=findViewById(R.id.input_editmessage);


        Button del=findViewById(R.id.btndel);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(Context.
                                NOTIFICATION_SERVICE);
                assert notificationManager != null;
                notificationManager.cancelAll();            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tit="Title";
                tit = title.getText().toString();
                String mess="Message";
                mess = message.getText().toString();

                Intent intent = new Intent(getBaseContext(), MainActivity.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification notification = new NotificationCompat.Builder(getBaseContext(), channelid)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(tit)
                        .setOngoing(true)
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine(mess))
                        .setContentIntent(pendingIntent)

                        .setAutoCancel(true)
                        .build();
                assert notificationManager != null;
                int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
                notificationManager.notify(m, notification);
//                unique id is given so that logcat can be used to get the id where error is present
//                request code is added so that dubugging can be done easily.
            }
        });
    }

}