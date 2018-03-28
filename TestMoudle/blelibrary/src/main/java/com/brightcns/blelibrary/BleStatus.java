package com.brightcns.blelibrary;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.util.Log;

import com.brightcns.blelibrary.utils.ConstantUtils;


/**
 * @author zhangfeng
 * @data： 27/3/18
 * @description：蓝牙状态判断
 */

public class BleStatus {
    private  static final String TAG="BleStatus.class";
    private  static final int NO_BLE=1;
    private  static final int NO_SUPPORT_BLE=2;
    private  static final int BLE_DISENABLED=3;
    private  static final int BLE_CONNECT=4;
    private  static final int BLE_DISCONNECT=5;
    private  static final int BLE_CONNECTOTHER=6;

    private static  BluetoothAdapter mAdapter;
    private static BleStatusCallBack mBleStatusCallBack;

    public BleStatus() {

    }

    public BleStatus(BleStatusCallBack bleStatusCallBack) {
        this.mBleStatusCallBack=bleStatusCallBack;
    }

    /**
     * 获取蓝牙适配器
     */
    private static BluetoothAdapter getBleAdapter(){
        mAdapter=BluetoothAdapter.getDefaultAdapter();
        return mAdapter;
    }

    /**
     * 判断是否有蓝牙设备
     * @return
     */
    private static void initBle(){
        if (getBleAdapter().equals(null)){
            Log.e(TAG, ConstantUtils.NO__BLE);
            mBleStatusCallBack.onFail(NO_BLE,ConstantUtils.NO__BLE);
            return ;
        }else {
            isOpen();
        }
    }

    /**
     * 蓝牙设备是否开启
     * 注：此处为提示用户跳转到设置页面，手动打开蓝牙
     *    并非程序后台强制开启蓝牙设备
     */
    private static void isOpen(){

        if (!mAdapter.isEnabled()){//蓝牙未打开
            mBleStatusCallBack.onFail(BLE_DISENABLED,ConstantUtils.BLE_DISENABLED);
            Log.e(TAG, ConstantUtils.BLE_DISENABLED);
            return;
        }else {//蓝牙打开
            bleConnectStatus();
            isSupportBle();
        }
    }


    /**
     * 蓝牙连接状态判断（目前仅作为干扰隐私参考）
     */
    private static void bleConnectStatus(){
        // 判断蓝牙的连接状态
        if (mAdapter.isDiscovering()) {
            Log.e(TAG,"蓝牙处于连接状态或未开启状态");
            mBleStatusCallBack.connectStatus(BLE_CONNECT,"蓝牙处于连接状态或未开启状态");
        } else {
            Log.e(TAG, "蓝牙处于未连接状态");
            mBleStatusCallBack.connectStatus(BLE_DISCONNECT,"蓝牙处于未连接状态");
        }
        if (mAdapter.isOffloadedFilteringSupported()) {
            mBleStatusCallBack.connectStatus(BLE_CONNECTOTHER,"isOffloadedFilteringSupported=true");
            Log.e(TAG, "isOffloadedFilteringSupported=true");
        } else {
            mBleStatusCallBack.connectStatus(BLE_CONNECTOTHER,"isOffloadedFilteringSupported=false");
            Log.e(TAG, "isOffloadedFilteringSupported=false");
        }
        if (mAdapter.isOffloadedScanBatchingSupported()) {
            mBleStatusCallBack.connectStatus(BLE_CONNECTOTHER,"isOffloadedScanBatchingSupported=true");
            Log.e(TAG, "isOffloadedScanBatchingSupported=true");
        } else {
            mBleStatusCallBack.connectStatus(BLE_CONNECTOTHER,"isOffloadedScanBatchingSupported=false");
            Log.e(TAG, "isOffloadedScanBatchingSupported=false");
        }
    }

    private static void isSupportBle( ){
        BluetoothLeAdvertiser bluetoothLeAdvertiser=mAdapter.getBluetoothLeAdvertiser();
        if (bluetoothLeAdvertiser == null) {//不支持ble Peripheral
            Log.e(TAG, ConstantUtils.NO_SUPPORT_BLE);
            mBleStatusCallBack.onFail(NO_SUPPORT_BLE,ConstantUtils.NO_SUPPORT_BLE);
        }else {
            mBleStatusCallBack.onSuccess();
        }
    }

    public static Builder with() {
        return new Builder();
    }


    public static class Builder{
        
        private BleStatus bleStatus; 
        
        private BleStatusCallBack bleStatusCallBack;
        
        public Builder setListener(BleStatusCallBack bleStatusCallBack){
            this.bleStatusCallBack=bleStatusCallBack;
            return this;
        }
        
        public void build(){
            bleStatus=new BleStatus(bleStatusCallBack);
            initBle();
        }
    }

    public interface BleStatusCallBack {
        void onSuccess();
        void onFail(int code,String msg);
        void connectStatus(int code,String msg);
    }

}
