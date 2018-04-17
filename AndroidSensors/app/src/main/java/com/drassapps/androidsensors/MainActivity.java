package com.drassapps.androidsensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // MARK - PROPERTIES
    private RecyclerView sensorRecyclerView;

    private List<Sensor> sensorList;
    private ArrayList<String> sensorsNames = new ArrayList<>();
    private ArrayList<String> sensorsVendor = new ArrayList<>();
    private ArrayList<String> sensorsVersion = new ArrayList<>();

    private BottomSheetBehavior bottomSheetBehavior;
    private TextView sensorNameBS, sensorVendorBS, sensorVersionBS;
    private ImageView bottomSheetHideButton, bottomSheetNextAct;

    // MARK - INTERFACE
    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view,int position);
    }

    // MARK - MAIN
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorRecyclerView = (RecyclerView) findViewById(R.id.sensorRecycleView);

        getSensorList();
        configuraBottonSheet();

        sensorRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                sensorRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                showSensorInfo(position);
            }

            @Override
            public void onLongClick(View view, final int position) {}

        }));
    }

    // Retrieve all sensor of device and create list
    public void getSensorList() {

        // Get the SensorManager
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // List of Sensors Available
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < sensorList.size(); i++){
            sensorsNames.add(sensorList.get(i).getName());
            sensorsVendor.add(sensorList.get(i).getVendor());
            sensorsVersion.add(String.valueOf(sensorList.get(i).getVersion()));
        }

        SensorAdapter sensorAdapter = new SensorAdapter(this, sensorsNames);
        sensorRecyclerView.setAdapter(sensorAdapter);
        sensorRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    // Initialize ui bottomsheet
    private void configuraBottonSheet(){

        LinearLayout btt_linear = (LinearLayout) findViewById(R.id.btt_sh);
        bottomSheetBehavior = BottomSheetBehavior.from(btt_linear);

        bottomSheetHideButton = (ImageView) findViewById(R.id.btsheet_hideButton);
        bottomSheetNextAct = (ImageView) findViewById(R.id.btsheet_nextAct);
        sensorNameBS = (TextView) findViewById(R.id.btsheet_sensorName);
        sensorVersionBS = (TextView) findViewById(R.id.btsheet_sensorVersion);
        sensorVendorBS = (TextView) findViewById(R.id.btsheet_sensorVendor);
    }


    // Show bottomSheet with info of sensor.
    private void showSensorInfo(final int position) {

        // Interact only with four sensors
        if (sensorList.get(position).getType() == Sensor.TYPE_PROXIMITY ||
                sensorList.get(position).getType() == Sensor.TYPE_ACCELEROMETER ||
                sensorList.get(position).getType() == Sensor.TYPE_LIGHT ||
                sensorList.get(position).getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            bottomSheetNextAct.setVisibility(View.VISIBLE);
            bottomSheetNextAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this,DeatilSensorView.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentSensor", position);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });

        } else {  bottomSheetNextAct.setVisibility(View.INVISIBLE);  }

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        sensorNameBS.setText(sensorsNames.get(position));
        sensorVendorBS.setText(sensorsVendor.get(position));
        sensorVersionBS.setText(sensorsVersion.get(position));

        bottomSheetHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }
}
