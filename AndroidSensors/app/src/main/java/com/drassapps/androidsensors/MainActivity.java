package com.drassapps.androidsensors;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView sensorRecyclerView;        // RecylcerView que contiene la información

    private ArrayList<String> sensorsNames = new ArrayList<>();
    private ArrayList<String> sensorsVendor = new ArrayList<>();
    private ArrayList<String> sensorsVersion = new ArrayList<>();

    // Campos asociados al BottomSheet
    private BottomSheetBehavior bottomSheetBehavior;
    private TextView sensorNameBS, sensorVendorBS, sensorVersionBS,
            bottomSheetHideButton, bottomSheetNextAct;

    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view,int position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorRecyclerView = (RecyclerView) findViewById(R.id.sensorRecycleView);

        // Dumy line para que cuando se abra la aplicación el EditTet no salga focuseado
        LinearLayout dummyLine = (LinearLayout) findViewById(R.id.dummy_line);
        dummyLine.requestFocus();

        // Tol
        final Toolbar toolbar = (Toolbar) findViewById(R.id.mainActivity_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // Si pulsamos buscar, nos buscará el contacto por el nombre
                    case R.id.SearchSensor:
                        searchSensor();
                        return true;
                }
                return true;
            }
        });
        toolbar.inflateMenu(R.menu.menu_main);

        getSensorList();
        configuraBottonSheet();

        sensorRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                sensorRecyclerView, new ClickListener() {

            // Cuando pulsamos de forma simple en un elemento nos muestra su informacion
            @Override
            public void onClick(View view, int position) {
                showSensorInfo(position);
            }

            @Override
            public void onLongClick(View view, final int position) {}

        }));
    }

    public void getSensorList() {

        // Get the SensorManager
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // List of Sensors Available
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < sensorList.size(); i++){
            sensorsNames.add(sensorList.get(i).getName());
            sensorsVendor.add(sensorList.get(i).getVendor());
            sensorsVersion.add(String.valueOf(sensorList.get(i).getVersion()));
        }

        SensorAdapter sensorAdapter = new SensorAdapter(this, sensorsNames);
        sensorRecyclerView.setAdapter(sensorAdapter);
        sensorRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    }

    private void searchSensor() {

    }

    // Asigna los elementos del Linear a una variables para poder gestionarlas
    private void configuraBottonSheet(){

        LinearLayout btt_linear = (LinearLayout) findViewById(R.id.btt_sh);
        bottomSheetBehavior = BottomSheetBehavior.from(btt_linear);

        bottomSheetHideButton = (TextView) findViewById(R.id.btsheet_hideButton);
        bottomSheetNextAct = (TextView) findViewById(R.id.btsheet_nextAct);
        sensorNameBS = (TextView) findViewById(R.id.btsheet_sensorName);
        sensorVersionBS = (TextView) findViewById(R.id.btsheet_sensorVersion);
        sensorVendorBS = (TextView) findViewById(R.id.btsheet_sensorVendor);
    }

    // Abre el Lay del BotomSheet con los datos del usuario
    private void showSensorInfo(final int position) {

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

        bottomSheetNextAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,DeatilSensorView.class);
                Bundle bundle = new Bundle();
                bundle.putInt("currentSensor", position);
                i.putExtras(bundle);
                Log.i("AVERE",""+position);
                startActivity(i);
            }
        });
    }
}
