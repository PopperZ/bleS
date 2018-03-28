package com.brightcns.blelibrary.utils;

/**
 * @author zhangfeng
 * @data： 27/3/18
 * @description：常量参数
 */

public class ConstantUtils {

    //----------------------------蓝牙状态判定-----------------------------------------
    public static final String NO_SUPPORT_BLE = "noSupportBle";
    public static final String NO__BLE = "noBle";
    public static final String BLE_DISENABLED = "ble isEnabled";

    //----------------------------蓝牙广播-----------------------------------------
    public final static String WRITEUUID= "00002AF1-0000-1000-8000-00805F9B34FB";
    public final static String READUUID = "00002AF0-0000-1000-8000-00805F9B34FB";
    public final static String SERVICESUUID = "000018F0-0000-1000-8000-00805F9B34FB";
    public static final String GATT_IS_NULL = "gatt is null";
    public static final String GATT_ADD_SUCCESS = "GattServer add success";
    public static final String GATT_ADD_FAIL = "GattServer add fail";
    public static final String GATT_SERVER_NULL = "GattServer is null";
    public static final String ADV_FAIL = "蓝牙广播开启失败";
    public static final String BLE_MAC_NULL = "不能获取真实的蓝牙地址";
}
