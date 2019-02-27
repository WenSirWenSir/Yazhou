package com.zbyj.Yazhou.Listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SenSorManagerListener implements SensorEventListener {
    private OnSensorChangedListener mSensorListener = null;
    @Override
    public void onSensorChanged(SensorEvent event) {

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public interface  OnSensorChangedListener{
        void onSensor();
    }

    public void setOnSensorChangeListener(OnSensorChangedListener tListener){
        this.mSensorListener = tListener;
    }
}
