
package com.zzy.luggage;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class BLEGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String LUGGAGE_RD = "08590f7d-db05-467e-8757-72f6faeb13d4";
    public static String LUGGAGE_WRITE = "08590f7f-db05-467e-8757-72f6faeb13d4";
    public static String LUGGAGE_NTF = "08590f7e-db05-467e-8757-72f6faeb13d4";

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put("e20a39f4-73f5-4bc4-a12f-17d1ad07a961", "Luggage Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        // Sample Characteristics.
        attributes.put(LUGGAGE_RD, "RD");
        attributes.put(LUGGAGE_WRITE, "WRITE");
        attributes.put(LUGGAGE_NTF, "NTF");

        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
