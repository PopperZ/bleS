package com.brightcns.blelibrary;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import com.brightcns.blelibrary.utils.ConstantUtils;
import com.brightcns.blelibrary.utils.DataUtils;

import static android.content.Context.BLUETOOTH_SERVICE;

/**
 * @author zhangfeng
 * @data： 28/3/18
 * @description：发送蓝牙广播
 */

public class SendBleBroadcast {

    private static final String TAG = "SendBleBroadcast.class";
    private static final int BLE_MAC_NULL = 15;

    private static SendBleBroadcastCallBack mCallBack;

    private static BluetoothLeAdvertiser mbleAdvertiser;//蓝牙广播对象
    private static BluetoothGattServer mBleGattServer;
    private static BluetoothGattCharacteristic mBleGattChar;//蓝牙通信通道特征值对象
    private static BluetoothManager mBleManager;//蓝牙管理器对象
    private static BleMockServerCallBack mMockServerCallBack;

    private static byte[] mData;
    private static Context mContext;


    public SendBleBroadcast() {
    }

    public SendBleBroadcast(SendBleBroadcastCallBack mCallBack, Context context) {
        this.mCallBack = mCallBack;
        this.mContext = context;
    }

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {
        private SendBleBroadcast sendBleBroadcast;
        private SendBleBroadcastCallBack callBack;
        private Context context;

        public Builder setListener(SendBleBroadcastCallBack callBack, Context context) {
            this.callBack = callBack;
            this.context = context;
            return this;
        }

        public void builder() {
            sendBleBroadcast = new SendBleBroadcast(callBack, context);
            // TODO: 28/3/18 发送广播操作
            sendBleAbv();
        }
    }


    public interface SendBleBroadcastCallBack {
        void onSuccess();

        void onFail(int code, String msg);

        void onConnected(String address);

        void onDisConnected();

        void stopAdvertis();
    }

    private static void sendBleAbv() {
        mbleAdvertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
        //初始化蓝牙管理器的对象
        mBleManager = (BluetoothManager) mContext.getSystemService(BLUETOOTH_SERVICE);

        mMockServerCallBack = new BleMockServerCallBack(new BleMockServerCallBack.BleMockServerInterface() {
            @Override
            public void onSuccess() {
                //蓝牙通道创建成功
                Log.e(TAG, ConstantUtils.GATT_ADD_SUCCESS);
                //开启广播
                startAdvertis();
            }

            @Override
            public void onFail(int code, String msg) {
                //蓝牙通道添加失败
                Log.e(TAG, code + msg);
                mCallBack.onFail(code, msg);
            }

            @Override
            public void onConnected(String address) {
                //连接上设备
                Log.e(TAG, address);
                mCallBack.onConnected(address);
            }

            @Override
            public void onDisConnected() {
                //设备断开连接
                Log.e(TAG, "断开连接");
                mCallBack.onDisConnected();
            }
        });
        //创建服务通道
        try {
            //蓝牙通信通道服务对象
            mBleGattServer = mBleManager.openGattServer(mContext, mMockServerCallBack);
            mMockServerCallBack.setUpServers(mBleGattServer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //开启广播
    private static void startAdvertis() {
        //广播参数设定
        AdvertiseSettings.Builder localBuilder = new AdvertiseSettings.Builder();
        localBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        localBuilder.setConnectable(true);
        localBuilder.setTimeout(0);
        localBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        AdvertiseSettings svtAdvParm = localBuilder.setConnectable(true).build();
        //广播数据设定
        AdvertiseData.Builder svtAdvDataBuild = new AdvertiseData.Builder();
        svtAdvDataBuild.addServiceUuid(ParcelUuid.fromString(ConstantUtils.SERVICESUUID));
        svtAdvDataBuild.setIncludeDeviceName(false);
        svtAdvDataBuild.addManufacturerData(59, DataUtils.GetAdvData(mContext));
        AdvertiseData svtAdvData = svtAdvDataBuild.build();
        mbleAdvertiser.startAdvertising(svtAdvParm, svtAdvData, blsAdvSvrCallBack);//开始广播
    }

    //关闭广播
    private static void stopAdvertis(){
        if (mBleGattServer == null) {
            Log.e(TAG, "广播已关闭1");
            return;
        }
        mBleGattServer.clearServices();
        mBleGattServer.close();
        mBleGattServer = null;//add by david 是否需要加？
        if (mbleAdvertiser == null) {
            Log.e(TAG, "广播已关闭2");
            return;
        }
        mbleAdvertiser.stopAdvertising(blsAdvSvrCallBack);
        mbleAdvertiser = null;
        mCallBack.stopAdvertis();
    }


    private static AdvertiseCallback blsAdvSvrCallBack = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.e(TAG, "广播发送成功");
            mCallBack.onSuccess();
            // TODO: 28/3/18  申请二维码

        }

        /**
         * 蓝牙广播开启失败（错误码为 4，2需要提示用户重启蓝牙或手机）
         * @param errorCode 错误码
         */
        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.e(TAG, "广播发送失败:+错误码：" + errorCode);
            mCallBack.onFail(errorCode, ConstantUtils.ADV_FAIL);
//            switch (errorCode){
//                case ADVERTISE_FAILED_INTERNAL_ERROR:
//                case ADVERTISE_FAILED_TOO_MANY_ADVERTISERS:
//                    break;
//                default:
//                    break;
//            }
        }
    };


}
