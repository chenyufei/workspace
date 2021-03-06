package com.example.blemasterv3;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    private static HashMap<String, String> devicenames = new HashMap();
        
    
    static {
        // Sample Services.
        attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        // Sample Characteristics.
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
        
        attributes.put("00001804-0000-1000-8000-00805f9b34fb", "TX_POWER_SERVICE");
        attributes.put("00001803-0000-1000-8000-00805f9b34fb", "LINK_LOSS_SERVICE");
        attributes.put("00001802-0000-1000-8000-00805f9b34fb", "IMMEDIATE_ALERT_SERVICE");
        attributes.put("00001801-0000-1000-8000-00805f9b34fb", "GATT_SERVICE");
        attributes.put("00001800-0000-1000-8000-00805f9b34fb", "GAP_SERVICE");

        attributes.put("0000180f-0000-1000-8000-00805f9b34fb", "BATTERY_SERVICE");
        attributes.put("6e400001-b5a3-f393-e0a9-e50e24dcca9e", "NORDIC_UART_SERVICE");
        
        
        attributes.put("00002a00-0000-1000-8000-00805f9b34fb", "Device Name");
        attributes.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance");
        attributes.put("00002a04-0000-1000-8000-00805f9b34fb", "Peripheral Preferred Connection Parameters");
        attributes.put("00002a07-0000-1000-8000-00805f9b34fb", "Tx Power Level");
        attributes.put("00002a06-0000-1000-8000-00805f9b34fb", "Alert Level");
        
        attributes.put("00002a19-0000-1000-8000-00805f9b34fb", "Battery Level");
        attributes.put("00002902-0000-1000-8000-00805f9b34fb", "Descriptors");
        
        attributes.put("6e400003-b5a3-f393-e0a9-e50e24dcca9e", "RX characteristic");
        attributes.put("6e400002-b5a3-f393-e0a9-e50e24dcca9e", "TX characteristic ");
        
        
    }

    static { 
    	devicenames.put("Nordic_11", "device_11");
    	devicenames.put("Nordic_15", "device_15");

    	devicenames.put("Nordic_51", "device_51");
    	devicenames.put("Nordic_55", "device_55");
    	
    }
    
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
    
    public static boolean finddevice(String device){
    	
    	String name = devicenames.get(device);
    	return name == null? false : true;
    }
}
