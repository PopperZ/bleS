package com.brightcns.blelibrary;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import com.brightcns.blelibrary.utils.ConstantUtils;

import java.util.UUID;

/**
 * @author zhangfeng
 * @data： 28/3/18
 * @description：通信通道回调
 */

public class BleMockServerCallBack extends BluetoothGattServerCallback {
    private static final String TAG="BleMockServerCallBack";
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTED = 2;
    private static final int GATT_IS_NULL = 11;
    private static final int GATT_SUCCESS = 12;
    private static final int GATT_ADD_FAIL = 13;
    private static final int GATT_SERVER_NULL = 14;


    private Context mContext;
    private BluetoothGattServer mGattServer;//蓝牙通道服务

    private BleMockServerInterface mInterface;

    public BleMockServerCallBack(BleMockServerInterface bleMockServerInterface) {
//        this.mContext = mContext;
        this.mInterface=bleMockServerInterface;
    }

    public void setUpServers(BluetoothGattServer gattServer)throws InterruptedException{
        if (gattServer==null){
            mInterface.onFail(GATT_IS_NULL, ConstantUtils.GATT_IS_NULL);
            return;
        }
        mGattServer=gattServer;
        // 设置GattService以及BluetoothGattCharacteristic
        {
            //immediate alert
            BluetoothGattService service = new BluetoothGattService(UUID.fromString(ConstantUtils.SERVICESUUID),
                    BluetoothGattService.SERVICE_TYPE_PRIMARY);
                if (service==null){
                    mInterface.onFail(GATT_SERVER_NULL, ConstantUtils.GATT_SERVER_NULL);
                    return;
                }
            //alert read char.
            BluetoothGattCharacteristic readAlc = new BluetoothGattCharacteristic(
                    UUID.fromString(ConstantUtils.READUUID),
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY | BluetoothGattCharacteristic.PROPERTY_READ,
                    BluetoothGattCharacteristic.PERMISSION_READ);
            //alert write char.
            BluetoothGattCharacteristic writeAlc = new BluetoothGattCharacteristic(
                    UUID.fromString(ConstantUtils.WRITEUUID),
                    BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
                    BluetoothGattCharacteristic.PERMISSION_WRITE);
            readAlc.setValue("try add");
            //添加进服务
            service.addCharacteristic(readAlc);
            service.addCharacteristic(writeAlc);

            //服务添加进通道服务
            mGattServer.addService(service);
        }
    }

    public interface BleMockServerInterface{
        void onSuccess();
        void onFail(int code ,String msg);
        void onConnected(String address);
        void onDisConnected();
    }


    /**
     * 连接状态
     * @param device 连接设备
     * @param status 状态
     * @param newState 连接状态
     */
    @Override
    public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
        super.onConnectionStateChange(device, status, newState);
        switch (newState){
            case STATE_CONNECTED://连接成功
                mInterface.onConnected(device.getAddress());
                break;
            case STATE_DISCONNECTED://断开连接
                mInterface.onDisConnected();
                break;
            default:
                break;
        }
    }

    /**
     * 服务添加
     * @param status 添加状态
     * @param service  服务
     */
    @Override
    public void onServiceAdded(int status, BluetoothGattService service) {
        super.onServiceAdded(status, service);
        if (status== BluetoothGatt.GATT_SUCCESS){
            mInterface.onSuccess();
        }else {
            mInterface.onFail(GATT_ADD_FAIL, ConstantUtils.GATT_ADD_FAIL);
        }
    }
    //主控端读
    @Override
    public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
    }
    //主控端写
    @Override
    public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
        super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
        mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
        // TODO: 28/3/18 数据处理
        Log.e(TAG,value.length+"");
    }
}
