package com.drassapps.androidsensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class DeatilSensorView extends Activity implements SensorEventListener {

    // MARK - PROPERTIES
    private SensorManager sensorManager;
    private Sensor sensor;
    private RelativeLayout main;
    private TextView infoSensor, nameSensor, infoX, infoY, infoZ;
    private ImageView infoArrow;

    private float[] lastAccelerometerValue = new float[3];
    private float[] lastMagnetometerVale = new float[3];
    private boolean isAccelerometer = false;
    private boolean isMagnetometer = false;

    private float[] rotationValues = new float[9];
    private float[] orientation = new float[3];
    private float currentDegrees = 0f;

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
        infoArrow = (ImageView) findViewById(R.id.infoArrow);
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

        // Show values of three axis
        else if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            nameSensor.setText(getResources().getString(R.string.acl));
            infoX.setText("Axis X " + String.valueOf(firtValue));
            infoY.setText("Axis Y " + String.valueOf(secondValue));
            infoZ.setText("Axis Z " + String.valueOf(thirdValue));
        }

        // Show device orientation with an arrow
        else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // Setup new sensor for retrieve device orientation
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

            nameSensor.setText(getResources().getString(R.string.magnetic));
            infoArrow.setBackground(getResources().getDrawable(R.drawable.magnetic_arrow));

            // If sensor is set up retrieve accelerometer values
            if (event.sensor == accelerometer) {
                System.arraycopy(event.values, 0, lastAccelerometerValue, 0, event.values.length);
                isAccelerometer = true;
            }

            // If sensor is set up retrieve magnetic field values
            else if (event.sensor == sensor) {
                System.arraycopy(event.values, 0, lastMagnetometerVale, 0, event.values.length);
                isMagnetometer = true;
            }

            if (isAccelerometer && isMagnetometer) {

                SensorManager.getRotationMatrix(rotationValues, null, lastAccelerometerValue, lastMagnetometerVale);
                SensorManager.getOrientation(rotationValues, orientation);

                float azimuthInRadians = orientation[0];
                float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;

                // Rotate arrow
                RotateAnimation rotateAnimation = new RotateAnimation(
                        currentDegrees,
                        -azimuthInDegress,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                rotateAnimation.setDuration(250);
                rotateAnimation.setFillAfter(true);

                infoArrow.startAnimation(rotateAnimation);

                // Update degrees for animation
                currentDegrees = -azimuthInDegress;

                // Show orientation info
                infoX.setText("Axis X " + String.valueOf(orientation[0]));
                infoY.setText("Axis Y " + String.valueOf(orientation[1]));
                infoZ.setText("Axis Z " + String.valueOf(orientation[2]));
            }
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