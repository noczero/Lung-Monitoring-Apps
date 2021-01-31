package com.example.healthmonitoring.monitoring;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class MonitoringPresenter {
    private MonitoringView view;

    // format
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    //
    String good_result = "Paru-paru anda sehat!";
    String bad_result = "Paru-paru anda tidak sehat";

    // firebase configuration
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private String node = "device1"; // node name in firebase
    private String childColor = "color"; // child name
    private String childSpiro = "spirometer";
    private String temperature;
    private String colorR, colorG, colorB;
    private String analogSpiro, voltageSpiro;
    private double currentTemperature = 0;
    private double prevTemperature = 0;
    private double vital_capacity = 0;
    private double minVoltage = 999;
    private double maxVoltage = -999;
    private double vcUkur = 0;


    // constructor
    public MonitoringPresenter(MonitoringView view){
        this.view = view;
    }

    // get temperature from firebase
    void getTemperature(){

        //get firebase reference based on node
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(node);

        // listener
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // get value of temperature
                    temperature = String.valueOf(dataSnapshot.child("temperature").getValue());

                    // set temparture to view
                    view.setTemperature(temperature);

                    // set status temperature
                    view.setTemperatureStatus(discreteTemperature(Double.parseDouble(temperature)));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("firebase-error", "onCancelled: " + databaseError);
            }
        });
    }

    // get color from firebase
    void getColor(){

        //get firebase reference based on node
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(node);

        // listener
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // get value of temperature
                    colorR = String.valueOf(dataSnapshot.child(childColor).child("R").getValue());
                    colorG = String.valueOf(dataSnapshot.child(childColor).child("G").getValue());
                    colorB = String.valueOf(dataSnapshot.child(childColor).child("B").getValue());

                    // set okisgen color to view
                    view.setColor(colorR, colorG, colorB);

                    view.setBValue("B : " + colorB);
                    view.setGValue("G : " + colorG);
                    view.setRValue("R : " + colorR);

                    // set oksigen s
                    view.setNailColor(colorR, colorG, colorB);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("firebase-error", "onCancelled: " + databaseError);
            }
        });
    }

    // get spirometer from firebase
    void getSpiroMeter(){

        //get firebase reference based on node
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(node);

        // listener
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // get value of temperature
                    analogSpiro = String.valueOf(dataSnapshot.child(childSpiro).child("analog").getValue());
                    voltageSpiro = String.valueOf(dataSnapshot.child(childSpiro).child("voltage").getValue());

                    view.setAnalogSpiro(df2.format(Double.parseDouble(analogSpiro)));
                    view.setVoltageSpiro(df2.format(Double.parseDouble(voltageSpiro)));

                    // track max, min value
                    Double maxVoltage = getMaximumVoltage(Double.parseDouble(voltageSpiro));
                    Double minVoltage = getMinimumVoltage(Double.parseDouble(voltageSpiro));
                    view.setVMax(df2.format(maxVoltage));
                    view.setVMin(df2.format(minVoltage));

                    // calculate vital capacity
                    vcUkur=  maxVoltage - minVoltage ;

                    view.setVolumeUkur(df2.format(vcUkur));

                    // decision
                    if (checkVitalCapacity(vcUkur,vital_capacity))
                        view.setDecisionSpiro(good_result);
                    else
                        view.setDecisionSpiro(bad_result);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("firebase-error", "onCancelled: " + databaseError);
            }
        });
    }



    // calculate volume estimasion based on physical parameter
    void calculate_estimated_vital_capacity(int age, int height, String gender){

        if(gender.toLowerCase().equals("pria")){
            //vital_capacity = (0.051 - 0.112 * age) * height;
            vital_capacity = (0.052 * height) - (0.022*age) - 3.00;
        } else if (gender.toLowerCase().equals("wanita")){
            //vital_capacity = (21.78 -0.101 * age) * height;
            vital_capacity = (0.041 * height) - (0.018*age) - 2.69;
        }

        view.setStandartVolume(df2.format(vital_capacity));
    }

    // get result decision
    private String makeDecision(double temperature, double colorR, double colorG, double colorB){
        // example
        if( colorB >= 80){
            return bad_result;
        } else {
            return good_result;
        }


    }

    private String discreteTemperature(double temperature){
        String status = null;

        if ((temperature >= 20) && (temperature <= 36.5)) {
            status = "Low";
        } else if ((temperature >= 36.5) && (temperature <= 37.2)) {
            status = "Normal";
        } else if  (temperature > 37.4) {
            status = "High";
        }

        return status;
    }

    private String discreteOksigen(double color){
        String status = null;
        if(color >= 0 && color <= 33.3){
            status = "Low";
        } else if (color > 33.3 && color < 66.6) {
            status = "Normal";
        } else {
            status = "High";
        }

        return status;
    }


    private double getMinimumVoltage(double voltage){
        if (voltage < minVoltage){
            minVoltage = voltage;
            return voltage;
        } else
            return minVoltage;
    }

    private double getMaximumVoltage(double voltage){
        if(voltage > maxVoltage){
            maxVoltage = voltage;
            return voltage;
        } else
            return maxVoltage;
    }

    private boolean checkVitalCapacity(double VCUkur , double estimasiVC){
        if (VCUkur > (estimasiVC * 0.8))
            return true;
        else
            return false;
    }
}
