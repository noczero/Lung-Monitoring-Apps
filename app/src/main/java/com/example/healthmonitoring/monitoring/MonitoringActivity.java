package com.example.healthmonitoring.monitoring;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.MultiGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.example.healthmonitoring.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonitoringActivity extends AppCompatActivity implements MonitoringView {

    // find element
    @BindView(R.id.temperature_gauge)
    ArcGauge tempGauge;

    @BindView(R.id.oksigen_gauge)
    MultiGauge oksigenGauge;

    @BindView(R.id.temperature_status)
    TextView temperatureStatus;

    @BindView(R.id.oksigen_status)
    TextView oksigenStatus;

    @BindView(R.id.decision_result)
    TextView decisionResult;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set main layout name
        setContentView(R.layout.activity_main);

        // butterknife
        ButterKnife.bind(this);

        // load gauges
        loadOksigenGauge();
        loadTemperatureGauge();

        // invoke MVP monitoring
        MonitoringPresenter presenter = new MonitoringPresenter(this);

        // invoke the methods
        presenter.getTemperature();
        presenter.getColor();

    }

    @Override
    public void setTemperature(String temperature) {
        tempGauge.setValue(Double.parseDouble(temperature)); // set value
    }

    @Override
    public void setColor(String color_R, String color_G, String color_B) {

        // set value
        oksigenGauge.setValue(Double.parseDouble(color_R));
        oksigenGauge.setSecondValue(Double.parseDouble(color_G));
        oksigenGauge.setThirdValue(Double.parseDouble(color_B));

    }

    @Override
    public void setDecisionResult(String result) {
        decisionResult.setText(result);
    }

    @Override
    public void setTemperatureStatus(String status) {
        temperatureStatus.setText(status);
    }

    @Override
    public void setOksigenStatus(String status) {
        oksigenStatus.setText(status);
    }

    private void loadOksigenGauge(){
        // set kadar oksigen
        oksigenGauge.setMinValue(0);
        oksigenGauge.setMaxValue(100);

        // red
        Range range1_oksigen = new Range();
        range1_oksigen.setColor(Color.parseColor("#ff0000"));
        range1_oksigen.setFrom(0);
        range1_oksigen.setTo(100);

        // green
        Range range2_oksigen = new Range();
        range2_oksigen.setColor(Color.parseColor("#00ff00"));
        range2_oksigen.setFrom(0);
        range2_oksigen.setTo(100);

        oksigenGauge.setDisplayValuePoint(true);
        oksigenGauge.setUseRangeBGColor(true);

        // blue
        Range range3_oksigen = new Range();
        range3_oksigen.setColor(Color.parseColor("#0000ff"));
        range3_oksigen.setFrom(0);
        range3_oksigen.setTo(100);

        oksigenGauge.addRange(range1_oksigen);
        oksigenGauge.addSecondRange(range2_oksigen);
        oksigenGauge.addThirdRange(range3_oksigen);

    }

    private void loadTemperatureGauge(){
        // set temperature
        tempGauge.setMinValue(20);
        tempGauge.setMaxValue(50);

        Range range1 = new Range();
        range1.setColor(Color.parseColor("#0000ff"));
        range1.setFrom(20);
        range1.setTo(35.3);

        Range range2 = new Range();
        range2.setColor(Color.parseColor("#00ff00"));
        range2.setFrom(35.4);
        range2.setTo(37.3);

        Range range3 = new Range();
        range3.setColor(Color.parseColor("#ff0000"));
        range3.setFrom(37.4);
        range3.setTo(50);

        tempGauge.addRange(range1);
        tempGauge.addRange(range2);
        tempGauge.addRange(range3);
    }
}
