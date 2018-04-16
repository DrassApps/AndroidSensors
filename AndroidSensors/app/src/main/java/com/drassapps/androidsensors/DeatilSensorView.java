package com.drassapps.androidsensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class DeatilSensorView extends Activity implements SensorEventListener {

    // MARK - PROPERTIES
    private SensorManager sensorManager;
    private Sensor sensor;
    private RelativeLayout main;
    private TextView infoSensor, nameSensor, infoX, infoY, infoZ;

    // MARK - LIFCE CYCLE
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // MARK - MAIN
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_sensor);

        // Intialice sensor
        setUpSensor();

        // MARK - UI
        main = (RelativeLayout) findViewById(R.id.detailSensor_main);
        infoSensor = (TextView) findViewById(R.id.infoSensor);
        nameSensor = (TextView) findViewById(R.id.nameSensor);
        infoX = (TextView) findViewById(R.id.infoSensorX);
        infoY = (TextView) findViewById(R.id.infoSensorY);
        infoZ = (TextView) findViewById(R.id.infoSensorZ);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) { }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Rename used vars
        float firtValue = event.values[0];
        float secondValue = event.values[1];
        float thirdValue = event.values[2];

        // Change background color for proximity Sensor
        if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
            infoSensor.setText(String.valueOf(firtValue));
            nameSensor.setText(getResources().getString(R.string.proximity));
            if (firtValue < 1.0) {
                main.setBackgroundColor(getResources().getColor(R.color.blue));
            }else if (firtValue > 1 && firtValue < 5) {
                main.setBackgroundColor(getResources().getColor(R.color.orange));
            } else {
                main.setBackgroundColor(getResources().getColor(R.color.red));
            }
        }

        // Change background color for light Sensor
        else if (sensor.getType() == Sensor.TYPE_LIGHT) {
            infoSensor.setText(String.valueOf(firtValue));
            nameSensor.setText(getResources().getString(R.string.light));
            if (firtValue < 10000) {
                main.setBackgroundColor(getResources().getColor(R.color.blue));
            }else if (firtValue > 10000 && firtValue < 20000) {
                main.setBackgroundColor(getResources().getColor(R.color.orange));
            } else {
                main.setBackgroundColor(getResources().getColor(R.color.red));
            }
        }

        //
        else if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            nameSensor.setText(getResources().getString(R.string.acl));

        }
    }

    // MARK - METHODS
    public void setUpSensor() {
        // Sensor clicked on mainActivity
        int currentSensor = getIntent().getExtras().getInt("currentSensor");

        // Retrieve all sensor list
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // Sensorlist is the same, so we can get the same sensor
        sensor = sensorList.get(currentSensor);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
