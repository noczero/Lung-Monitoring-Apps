package com.example.healthmonitoring.monitoring;

public interface MonitoringView {

    void setTemperature(String temperature);
    void setColor(String color_R, String color_G, String color_B);
    void setTemperatureStatus(String status);
    void setNailColor(String color_R, String color_G, String color_B);
    void setRValue(String rValue);
    void setGValue(String gValue);
    void setBValue(String bValue);
    void setStandartVolume(String value);
    void setVolumeUkur(String value);
    void setAnalogSpiro(String value);
    void setVoltageSpiro(String value);
    void setVMax(String value);
    void setVMin(String value);
    void setDecisionSpiro(String value);

}
