package com.example.healthmonitoring.fuzzy;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import com.fuzzylite.*;
import com.fuzzylite.imex.FisImporter;
import com.fuzzylite.imex.FllImporter;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

public class FuzzyPresenter {

    // format
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private FuzzyView view;

    public FuzzyPresenter(FuzzyView view){
        this.view = view;
    }

    void runFuzzy(double temperature, double red, double green, double blue ) throws IOException {

        File sdcard = Environment.getExternalStorageDirectory();
        Log.d("SDCARD", "runFuzzy: ");
        File file = new File(sdcard,"lung-fuzzy.fis");

        Engine engine = new FisImporter().fromFile(file);

        InputVariable temperature_input = engine.getInputVariable("temperature");
        InputVariable red_input = engine.getInputVariable("Red");
        InputVariable green_input = engine.getInputVariable("Green");
        InputVariable blue_input = engine.getInputVariable("Blue");

        OutputVariable status_output = engine.getOutputVariable("Status");

        // set input
        temperature_input.setValue(temperature);
        red_input.setValue(red);
        blue_input.setValue(blue);
        green_input.setValue(green);

        // fuzzy process
        engine.process();

        // output
        status_output.getAggregation().toString();
        Log.d("FUZZY", "Output RAW Value: " + status_output.getValue());
        Log.d("FUZZY", "Fuzzy Output Detail : " + status_output.fuzzyOutputValue());
        Log.d("FUZZY", "Input : " + engine.getInputVariables());
        Log.d("FUZZY", "Output : " + engine.getOutputVariables());
        Log.d("FUZZY", "Rules : " + engine.getRuleBlocks());

        // invoke view
        view.setInputFuzzy(joinInputVariables(engine.getInputVariables()));
        view.setOutputFuzzy(engine.getOutputVariable(0).toString());
        view.setRulesFuzzy(engine.getRuleBlock(0).toString());

        view.setOutputDetail(status_output.fuzzyOutputValue());
        view.setOutputValue(df2.format(status_output.getValue()));
    }

    private String joinInputVariables(List<InputVariable> inputVariables){
       String result = "";

       for (int i = 0; i < inputVariables.size(); i++){
           result = result + inputVariables.get(i).toString() + "\n\n";
       }

       return result;
    }

}


