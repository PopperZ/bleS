package com.brightcns.blelibrary.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import net.vidageek.mirror.dsl.Mirror;

/**
 * @author zhangfeng
 * @data： 27/3/18
 * @description：进制转换工具类
 */

public class DataUtils {
    //通道参数处理
    public static byte[] GetAdvData(Context context) {
        //通信通道的参数
        String bleMac = getBleMac(context);
        String strTemp = "";
        byte[] ble = new byte[6];
        if (bleMac.equals(null)) {
            bleMac = "112233445566";//此处需注意：如果没获取到真实的蓝牙地址，就放空数据
        }
        for (int i = 0; i < 6; i++) {
            strTemp = bleMac.substring(i * 2, i * 2 + 2);
            ble[i] = Integer.valueOf(strTemp, 16).byteValue();
        }
        return ble;
    }

    //获取蓝牙地址
    public static String getBleMac(Context context) {
        Object bluetoothManagerService = new Mirror().on(BluetoothAdapter.getDefaultAdapter()).get().field("mService");
        if (bluetoothManagerService == null) {
            return null;
        } else {
            Object address = new Mirror().on(bluetoothManagerService).invoke().method("getAddress").withoutArgs();
            if (address != null && address instanceof String) {
                String bleMac = address.toString();
                String bleMacNoSplit = bleMac.replaceAll(":", "");
                return bleMacNoSplit;
            } else {
                return null;
            }
        }
    }
}
