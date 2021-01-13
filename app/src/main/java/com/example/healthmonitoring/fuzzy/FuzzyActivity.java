package com.example.healthmonitoring.fuzzy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.healthmonitoring.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FuzzyActivity extends AppCompatActivity implements FuzzyView {

    @BindView(R.id.fuzzy_output_value)
    TextView fuzzyOutputValue;

    @BindView(R.id.fuzzy_output_detail)
    TextView fuzzyOutputDetail;

    @BindView(R.id.model_input_fuzzy)
    TextView modelInputFuzzy;

    @BindView(R.id.model_output_fuzzy)
    TextView modelOutputFuzzy;

    @BindView(R.id.model_rules_fuzzy)
    TextView modelRulesFuzzy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuzzy);
        // butterknife
        ButterKnife.bind(this);

        FuzzyPresenter fuzzyPresenter = new FuzzyPresenter(this);

        // get variable
        Intent intent = getIntent();
        Double temperature = intent.getDoubleExtra("TEMPERATURE",0.0);
        Double red = intent.getDoubleExtra("R",0.0);
        Double green = intent.getDoubleExtra("G", 0.0);
        Double blue = intent.getDoubleExtra("B", 0.0);

        // load file
        try {
            InputStream inputStream = getAssets().open("lung-fuzzy.fis");
            fuzzyPresenter.runFuzzy(temperature, red, green, blue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setOutputValue(String value) {
        fuzzyOutputValue.setText(value);
    }

    @Override
    public void setOutputDetail(String value) {
        fuzzyOutputDetail.setText(value);
    }

    @Override
    public void setInputFuzzy(String value) {
        modelInputFuzzy.setText(value);
    }

    @Override
    public void setOutputFuzzy(String value) {
        modelOutputFuzzy.setText(value);
    }

    @Override
    public void setRulesFuzzy(String value){
        modelRulesFuzzy.setText(value);
    }


}