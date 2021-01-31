package com.example.healthmonitoring.AHP;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthmonitoring.R;
import com.example.healthmonitoring.fuzzy.FuzzyPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AHPActivity extends AppCompatActivity implements AHPView{

    @BindView(R.id.ahp_output_value)
    TextView ahpOutput;

    @BindView(R.id.ahp_weight)
    TextView ahpResultWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahp);
        // butterknife
        ButterKnife.bind(this);

        AHPPresenter ahpPresenter = new AHPPresenter(this);

        // get variable
        Intent intent = getIntent();
        double temperature = intent.getDoubleExtra("TEMPERATURE",0.0);
        double red = intent.getDoubleExtra("R",0.0);
        double green = intent.getDoubleExtra("G", 0.0);
        double blue = intent.getDoubleExtra("B", 0.0);
        double age = intent.getDoubleExtra("AGE", 0.0);
        double vitalCapacity = intent.getDoubleExtra("VC", 0.0);

        // invoke AHP process
        ahpPresenter.runAHP(temperature, vitalCapacity, red, green, blue, age);

    }

    @Override
    public void setResultAHP(String result) {
        ahpOutput.setText(result);
    }

    @Override
    public void setDetail(String detail) {
        ahpResultWeight.setText(detail);
    }
}
