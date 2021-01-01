package com.example.healthmonitoring.monitoring;

public interface MonitoringView {

    void setTemperature(String temperature);
    void setColor(String color_R, String color_G, String color_B);
    void setDecisionResult(String result);
    void setTemperatureStatus(String status);
    void setOksigenStatus(String status);
}
