package com.smartcampus.api.store;

import com.smartcampus.api.model.Room;
import com.smartcampus.api.model.Sensor;
import com.smartcampus.api.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryStore {
    public static final Map<String, Room> ROOMS = new HashMap<>();
    public static final Map<String, Sensor> SENSORS = new HashMap<>();
    public static final Map<String, List<SensorReading>> SENSOR_READINGS = new HashMap<>();

    private InMemoryStore() {
    }

    public static List<SensorReading> getReadingsBySensor(String sensorId) {
        List<SensorReading> readings = SENSOR_READINGS.get(sensorId);
        if (readings == null) {
            readings = new ArrayList<>();
            SENSOR_READINGS.put(sensorId, readings);
        }
        return readings;
    }
}
