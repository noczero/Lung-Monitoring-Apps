package com.example.healthmonitoring.AHP;

import java.text.DecimalFormat;

public class AHPPresenter {

    private AHPView ahpView;

    // weight init
    private double[] weightCriteria = {0.55, 0.26, 0.02, 0.06};
    private double[] weightVC = {0.75, 0.25};
    private double[] weightAge = {0.63, 0.26, 0.11};
    private double[] weightColorType = {0.49, 0.31, 0.20};
    private double[] weightColorR = {0.75, 0.25};
    private double[] weightColorG = {0.75, 0.25};
    private double[] weightColorB = {0.75, 0.25};
    private double[] weightTemperature = {0.63, 0.26, 0.11};

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public AHPPresenter(AHPView view) {
        this.ahpView = view;
    }


    void runAHP(double temperature, double VC, double colorR, double colorG, double colorB, double age){

        // calculate subcriteria
        double resultTemperature = calculateTemperature(temperature);
        double resultVC = calculateVC(VC);
        double resultR = calculateColorR(colorR);
        double resultG = calculateColorG(colorG);
        double resultB = calculateColorB(colorB);
        double resultAge = calculateAge(age);

        // sum total
        double total = resultTemperature + resultVC + resultR + resultG + resultB + resultAge;

        // display
        ahpView.setResultAHP(df2.format(total));

        String detail_result = df2.format(resultTemperature) + "/Temperature + "
                + df2.format(resultAge) + "/Age + "
                + df2.format(resultVC) + "/VC + "
                + df2.format(resultR) + "/R + "
                + df2.format(resultG) + "/G + "
                + df2.format(resultB) + "/B";

        ahpView.setDetail(detail_result);
    }

    private double calculateVC(double VC){
        if (VC >= 2.12){
            return weightCriteria[0] * weightVC[1];
        } else {
            return weightCriteria[0] * weightVC[0];
        }
    }

    private double calculateAge(double age){
        if(age > 35) {
            return weightCriteria[1] * weightAge[0];
        } else if (age <= 35 && age >= 18) {
            return weightCriteria[1] * weightAge[1];
        } else {
            return weightCriteria[1] * weightAge[2];
        }
    }

    private double calculateColorR(double R){
        if(R >= 150){
            return weightCriteria[2] * weightColorType[0] * weightColorR[1];
        } else {
            return weightCriteria[2] * weightColorType[0] * weightColorR[0];
        }

    }

    private double calculateColorG(double G){
        if(G >= 150){
            return weightCriteria[2] * weightColorType[1] * weightColorG[1];
        } else {
            return weightCriteria[2] * weightColorType[1] * weightColorG[0];
        }
    }

    private double calculateColorB(double B){
        if(B >= 150){
            return weightCriteria[2] * weightColorType[2] * weightColorB[1];
        } else {
            return weightCriteria[2] * weightColorType[2] * weightColorB[0];
        }
    }

    private double calculateTemperature(double temp){
        if(temp > 37.2){
            return weightCriteria[3] * weightTemperature[0];
        } else if (temp >= 36 && temp <= 37.2){
            return weightCriteria[3] * weightTemperature[2];
        } else {
            return weightCriteria[3] * weightTemperature[1];
        }
    }


}
