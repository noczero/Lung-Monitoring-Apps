package com.example.healthmonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set temperature
        ArcGauge tempGauge = (ArcGauge) findViewById(R.id.temperature_gauge);
        tempGauge.setMinValue(15);
        tempGauge.setMaxValue(50);

        Range range1 = new Range();
        range1.setColor(Color.parseColor("#86D4DC"));
        range1.setFrom(15);
        range1.setTo(36);

        Range range2 = new Range();
        range2.setColor(Color.parseColor("#d67676"));
        range2.setFrom(37);
        range2.setTo(50);

        tempGauge.addRange(range1);
        tempGauge.addRange(range2);

        tempGauge.setValue(33);

        // set kadar oksigen
        HalfGauge oksigenGauge = (HalfGauge) findViewById(R.id.oksigen_gauge);
        oksigenGauge.setMinValue(0);
        oksigenGauge.setMaxValue(100);

        Range range1_oksigen = new Range();
        range1_oksigen.setColor(Color.parseColor("#86D4DC"));
        range1_oksigen.setFrom(0);
        range1_oksigen.setTo(50);

        Range range2_oksigen = new Range();
        range2_oksigen.setColor(Color.parseColor("#d67676"));
        range2_oksigen.setFrom(51);
        range2_oksigen.setTo(100);

        oksigenGauge.addRange(range1_oksigen);
        oksigenGauge.addRange(range2_oksigen);

        oksigenGauge.setValue(25);



    }
}