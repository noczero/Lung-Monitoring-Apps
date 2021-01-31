package com.example.healthmonitoring.monitoring;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.MultiGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.example.healthmonitoring.AHP.AHPActivity;
import com.example.healthmonitoring.fuzzy.FuzzyActivity;
import com.example.healthmonitoring.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonitoringActivity extends AppCompatActivity implements MonitoringView {

    // find element
    @BindView(R.id.temperature_gauge)
    ArcGauge tempGauge;

    @BindView(R.id.oksigen_gauge)
    MultiGauge spiroMeter;

    @BindView(R.id.temperature_status)
    TextView temperatureStatus;

    @BindView(R.id.nail_color)
    ImageView nailColor;

    @BindView(R.id.red_value)
    TextView redValue;

    @BindView(R.id.blue_value)
    TextView blueValue;

    @BindView(R.id.green_value)
    TextView greenValue;

    @BindView(R.id.volume_standart)
    TextView volumeStandart;

    @BindView(R.id.analog_spiro)
    TextView analogSpiro;

    @BindView(R.id.voltage_spiro)
    TextView voltageSpiro;

    @BindView(R.id.v_max)
    TextView vMax;

    @BindView(R.id.v_min)
    TextView vMin;

    @BindView(R.id.volume_ukur)
    TextView volumeUkur;

    @BindView(R.id.spiro_decision)
    TextView spiroDecision;

    String ageStr, vitalCapacityStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set main layout name
        setContentView(R.layout.activity_main);

        // butterknife
        ButterKnife.bind(this);

        // load gauges
        loadSpiroMeter();
        loadTemperatureGauge();

        // invoke MVP monitoring
        MonitoringPresenter presenter = new MonitoringPresenter(this);


        // get intent
        Intent intent = getIntent();
        ageStr = intent.getStringExtra("AGE");
        String height = intent.getStringExtra("HEIGHT");
        String gender = intent.getStringExtra("GENDER");

        // set standart estimated VC
        presenter.calculate_estimated_vital_capacity(Integer.parseInt(ageStr),Integer.parseInt(height), gender);

        // invoke the methods
        presenter.getTemperature();
        presenter.getColor();
        presenter.getSpiroMeter();


    }

    @Override
    public void setTemperature(String temperature) {
        tempGauge.setValue(Double.parseDouble(temperature)); // set value
    }

    @Override
    public void setColor(String color_R, String color_G, String color_B) {

        // set value
        spiroMeter.setValue(Double.parseDouble(color_R));
        spiroMeter.setSecondValue(Double.parseDouble(color_G));
        spiroMeter.setThirdValue(Double.parseDouble(color_B));

    }


    @Override
    public void setTemperatureStatus(String status) {
        temperatureStatus.setText(status);
    }

    @Override
    public void setNailColor(String color_R, String color_G, String color_B) {
        nailColor.setColorFilter(Color.argb(255, Integer.parseInt(color_R), Integer.parseInt(color_G), Integer.parseInt(color_B)));
    }


    @Override
    public void setRValue(String rValue) {
        redValue.setText(rValue);
    }

    @Override
    public void setGValue(String gValue) {
        greenValue.setText(gValue);
    }

    @Override
    public void setBValue(String bValue) {
        blueValue.setText(bValue);
    }

    @Override
    public void setStandartVolume(String value) {
      volumeStandart.setText("Estimasi : " + value + " L");
    }

    @Override
    public void setVolumeUkur(String value) {
        vitalCapacityStr = value;
        volumeUkur.setText("Pengukuran : " + value + " L");
    }

    @Override
    public void setAnalogSpiro(String value) {
        analogSpiro.setText("Analog : " + value);
    }

    @Override
    public void setVoltageSpiro(String value) {
        voltageSpiro.setText("Voltage : " + value + " V");
    }

    @Override
    public void setVMax(String value) {
        vMax.setText("VMax : " + value + " V");
    }

    @Override
    public void setVMin(String value) {
        vMin.setText("VMin : " + value + " V");
    }

    @Override
    public void setDecisionSpiro(String value) {
        spiroDecision.setText(value);
    }

    private void loadSpiroMeter(){
        // set kadar oksigen
        spiroMeter.setMinValue(0);
        spiroMeter.setMaxValue(255);

        // red
        Range range1_oksigen = new Range();
        range1_oksigen.setColor(Color.parseColor("#ff0000"));
        range1_oksigen.setFrom(0);
        range1_oksigen.setTo(255);

        // green
        Range range2_oksigen = new Range();
        range2_oksigen.setColor(Color.parseColor("#00ff00"));
        range2_oksigen.setFrom(0);
        range2_oksigen.setTo(255);

        spiroMeter.setDisplayValuePoint(true);
        spiroMeter.setUseRangeBGColor(true);

        // blue
        Range range3_oksigen = new Range();
        range3_oksigen.setColor(Color.parseColor("#0000ff"));
        range3_oksigen.setFrom(0);
        range3_oksigen.setTo(255);

        spiroMeter.addRange(range1_oksigen);
        spiroMeter.addSecondRange(range2_oksigen);
        spiroMeter.addThirdRange(range3_oksigen);

    }

    private void loadTemperatureGauge(){
        // set temperature
        tempGauge.setMinValue(20);
        tempGauge.setMaxValue(50);

        Range range1 = new Range();
        range1.setColor(Color.parseColor("#0000ff"));
        range1.setFrom(20);
        range1.setTo(36.5);

        Range range2 = new Range();
        range2.setColor(Color.parseColor("#00ff00"));
        range2.setFrom(36.5);
        range2.setTo(37.2);

        Range range3 = new Range();
        range3.setColor(Color.parseColor("#ff0000"));
        range3.setFrom(37.4);
        range3.setTo(50);

        tempGauge.addRange(range1);
        tempGauge.addRange(range2);
        tempGauge.addRange(range3);
    }

    // run fuzzy activity
    public void startFuzzyActivity(View view){
        double temperature = tempGauge.getValue();
        double red_color = spiroMeter.getValue();
        double green_color = spiroMeter.getSecondValue();
        double blue_color = spiroMeter.getThirdValue();

        //call fuzzy activity intent
        Intent intent = new Intent(this, FuzzyActivity.class);

        // passing variable
        intent.putExtra("TEMPERATURE", temperature);
        intent.putExtra("R", red_color);
        intent.putExtra("G", green_color);
        intent.putExtra("B", blue_color);

        // debug
        Log.d("FUZZY-VARIABLE", "Temperature : " + temperature);
        Log.d("FUZZY-VARIABLE", "R : " + red_color);
        Log.d("FUZZY-VARIABLE", "B : " + blue_color);
        Log.d("FUZZY-VARIABLE", "G : " + green_color);

        // invoke
        startActivity(intent);
    }

    // run ahp activity
    public void startAHPActivity(View view){
        double temperature = tempGauge.getValue();
        double red_color = spiroMeter.getValue();
        double green_color = spiroMeter.getSecondValue();
        double blue_color = spiroMeter.getThirdValue();
        double vitalCapacity = Double.parseDouble(vitalCapacityStr);
        double age = Double.parseDouble(ageStr);

        Intent intent = new Intent(this, AHPActivity.class);

        intent.putExtra("TEMPERATURE", temperature);
        intent.putExtra("R", red_color);
        intent.putExtra("G", green_color);
        intent.putExtra("B", blue_color);
        intent.putExtra("VC", vitalCapacity);
        intent.putExtra("AGE", age);

        startActivity(intent);
    }


}
