package com.example.healthmonitoring.monitoring;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MonitoringPresenter {
    private MonitoringView view;

    //
    String good_result = "Paru-paru anda sehat!";
    String bad_result = "Paru-paru anda tidak sehat";

    // firebase configuration
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private String node = "device1"; // node name in firebase
    private String childColor = "color"; // child name
    private String temperature;
    private String colorR, colorG, colorB;
    private double currentTemperature = 0;
    private double prevTemperature = 0;

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

                    // make final decision
                    if(colorR != null && colorR != null && colorG != null){
                        String result = makeDecision(
                                Double.parseDouble(temperature),
                                Double.parseDouble(colorR),
                                Double.parseDouble(colorG),
                                Double.parseDouble(colorB)
                        );
                        view.setDecisionResult(result);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("firebase-error", "onCancelled: " + databaseError);
            }
        });
    }

    // get temperature from firebase
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

                    // set oksigen s
                    view.setOksigenStatus(discreteOksigen(Double.parseDouble(colorR)));

                    // make final decision
                    if(temperature != null){
                        String result = makeDecision(
                                Double.parseDouble(temperature),
                                Double.parseDouble(colorR),
                                Double.parseDouble(colorG),
                                Double.parseDouble(colorB)
                        );
                        view.setDecisionResult(result);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("firebase-error", "onCancelled: " + databaseError);
            }
        });
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

        if ((temperature >= 20) && (temperature <= 35.4)) {
            status = "Low";
        } else if ((temperature >= 35.4) && (temperature <= 37.4)) {
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

}
