package com.konsung.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.konsung.R;
import com.konsung.constant.Configuration;
import com.konsung.constant.KParamType;
import com.konsung.constant.NetConstant;
import com.konsung.constant.StoreConstant;
import com.konsung.net.EchoServer;
import com.konsung.util.Logger;
import com.konsung.util.MeasureUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;


/**
 * aidl server端
 * server端为app中AIDLServer类,client端为各个子参数的Activity
 * 在增加设本类内容时需要注意本类的变量计,具体参考注释内容
 * 本类包含AIDL数据传递,以及直接进行数据存储
 *
 * @author ouyangfan
 * @version 0.0.1
 */
public class AIDLServer extends Service {
    // aidl Binder
    /*private AIDLServerBinder serverBinder;*/
    Message message = null;

    // 将趋势数据存储进List,用于数据缓存
    // 保存进List集合的原因是连续数据需要过滤
    // 如果是点测数据值则不需要加入集合进行过滤,而是直接使用即可
    private HashMap<Integer, Integer> status;
    private HashMap<Integer, Integer> trends;
    private HashMap<Integer, Integer> ecgConfig;
    private HashMap<Integer, Integer> spo2Config;
    private HashMap<Integer, Integer> nibpConfig;
    private HashMap<Integer, Integer> tempConfig;
    private HashMap<Integer, Integer> irtempConfig;

    private byte[] spo2Wave = null;
    private byte[] irtempWave = null;
    private byte[] ecgIIWave = null;
    private byte[] ecgIWave = null;
    private byte[] ecgIIIWave = null;
    private byte[] ecgAVRWave = null;
    private byte[] ecgAVLWave = null;
    private byte[] ecgAVFWave = null;
    private byte[] ecgV1Wave = null;
    private byte[] ecgV2Wave = null;
    private byte[] ecgV3Wave = null;
    private byte[] ecgV4Wave = null;
    private byte[] ecgV5Wave = null;
    private byte[] ecgV6Wave = null;
    private byte[] fhrEag = null;
    private byte[] fhr2Eag = null;
    private byte[] fhrtoco = null;

    private byte[] leadDiaResult = null;

    private boolean spo2Used = false;
    private boolean irtempUsed = false;
    private boolean ecgIIUsed = false;
    private boolean ecgIUsed = false;
    private boolean ecgIIIUsed = false;
    private boolean ecgAVRUsed = false;
    private boolean ecgAVLUsed = false;
    private boolean ecgAVFUsed = false;
    private boolean ecgV1Used = false;
    private boolean ecgV2Used = false;
    private boolean ecgV3Used = false;
    private boolean ecgV4Used = false;
    private boolean ecgV5Used = false;
    private boolean ecgV6Used = false;
    private boolean fhrEagUsed = false;
    private boolean fhr2EagUsed = false;
    private boolean fhrtocosed = false;

    private SendMSGToFragment sendMsg;
    private SetMsgFromDeviceManager sendDeviceManager;
    private MsgBinder msgBinder;
    private boolean isAppDeviceHeart = false; //AppDevice心跳

    /**
     * * 设置显示的内容
     *
     * @param obj 显示的信息
     */
    public void setSendMSGToFragment(SendMSGToFragment obj) {
        sendMsg = obj;
    }

    public void setSendDeviceManager(SetMsgFromDeviceManager obj) {
        sendDeviceManager = obj;
    }

    /**
     * appDevice发送数据的方接口
     */
    public interface SendMSGToFragment {
        /**
         * 发生数据给appDevice
         *
         * @param name    数据参数
         * @param version 数据
         */
        void sendParaStatus(String name, String version);

        /**
         * 接受心电数据的方法
         *
         * @param param 参数
         * @param bytes 数据
         */
        void sendWave(int param, byte[] bytes);

        /**
         * 接受数据的方法
         *
         * @param param 参数
         * @param value 数据
         */
        void sendTrend(int param, int value);

        void sendTrend(int param, String value);


        /**
         * 接受配置的方法
         *
         * @param param 参数
         * @param value 数据
         */
        void sendConfig(int config, int param, int value);

        /**
         * 接受身份证的方法
         *
         * @param name     姓名
         * @param idcard   身份证
         * @param sex      性别
         * @param type     类别
         * @param birthday 生日
         * @param picture  照片信息
         */
        void sendPersonalDetail(String name, String idcard, int sex,
                                int type, String birthday, String picture, String a);

        /**
         * 发送12导联结果包方法
         *
         * @param bytes 数据
         */
        void send12LeadDiaResult(byte[] bytes);

        /**
         * 接收保活包
         */
        void sendHeartBeat(Bundle pBundle);
    }

    /**
     * DeviceManager 发送数据
     */
    public interface SetMsgFromDeviceManager {

        void sendConfig1(int config, int param, int value);

        void sendTrend1(int param, String value);

        void sendHeartBeat(Bundle pBundle);
    }

    /**
     * 构造器
     */
    public AIDLServer() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("lsy", "AIDL CREATE SUCCESS!");
        // 初始化
        status = new HashMap<>();
        trends = new HashMap<>();
        ecgConfig = new HashMap<>();
        spo2Config = new HashMap<>();
        nibpConfig = new HashMap<>();
        tempConfig = new HashMap<>();
        msgBinder = new MsgBinder();

        startService();
        mCountDownTimer.start();
    }

    private void startService() {
        // 开启线程处理网络数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Thread.sleep(2000);
                    EchoServer _echoServer1 = new EchoServer(getApplicationContext(), mHandler);
                    _echoServer1.start();

                } catch (Exception e) {
                    Log.e("p", "error reason:" + e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return msgBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 绑定服务
     */
    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return 当前的服务
         */
        public AIDLServer getService() {
            return AIDLServer.this;
        }
    }

    /*
     * Handler 处理数据
     * 使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            try {
                //********************
                switch (msg.what) {
                    case NetConstant.NET_FHR_CONFIG:
                        if (sendMsg != null) {
                            sendMsg.sendConfig(msg.what, msg.arg1, msg.arg2);
                        }
                        break;
                    case NetConstant.PARA_STATUS:
                        status.put(msg.arg1, msg.arg2);
                        Bundle paraStatusBundle = msg.getData();
                        byte[] paraBoardName = paraStatusBundle.getByteArray("paraBoardName");
                        byte[] paraBoardVersion = paraStatusBundle
                                .getByteArray("paraBoardVersion");
                        String boardName = new String(paraBoardName, StandardCharsets.UTF_8);
                        String boardVersion = new String(paraBoardVersion,
                                StandardCharsets.UTF_8);
//                        if (sendMsg != null) {
//                            sendMsg.sendParaStatus
//                                    (boardName, boardVersion);
//                        }
                        // 把KSM5的版本号作为多参模块版本号
                        if (boardName.equals(AIDLServer.this.getString(R.string.ksm5))) {
                            MeasureUtils.saveToSp(getApplicationContext(), "app_config",
                                    "paraBoardName", boardName);
                            MeasureUtils.saveToSp(getApplicationContext(),
                                    "app_config", "paraBoardVersion", boardVersion);
                        }
                        break;
                    case NetConstant.NET_TREND:
                        if (sendMsg != null) {
                            sendMsg.sendTrend(msg.arg1, msg.arg2);
                        }
                        trends.put(msg.arg1, msg.arg2);
                        break;

                    //点测
                    case NetConstant.NET_POINT:
                        if (sendDeviceManager != null) {
                            Logger.e("zrl", "点测测量:" + msg.getData().getString(Configuration.BLOGIC_INFO_KEY));
                            sendDeviceManager.sendTrend1(msg.arg1, msg.getData().getString(Configuration.BLOGIC_INFO_KEY));
                        }
                        if (sendMsg != null) {
                            sendMsg.sendTrend(msg.arg1, msg.getData().getString(Configuration.BLOGIC_INFO_KEY));
                        }
                        break;
                    //点测状态
                    case NetConstant.NET_POINT_STATUS:
                        if (sendDeviceManager != null) {
                            Logger.e("zrl", "点测状态:" + msg.getData().getString(Configuration.BLOGIC_INFO_KEY));
                            sendDeviceManager.sendConfig1(msg.what, msg.arg1, msg.arg2);
                        }
                        break;

                    case NetConstant.NET_WAVE://波形数据
                        Bundle data = msg.getData();
                        if (data.containsKey(String.valueOf(KParamType.SPO2_WAVE))) {
                            spo2Wave = data.getByteArray(String.valueOf(KParamType.SPO2_WAVE));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.SPO2_WAVE, spo2Wave);
                            }
                            spo2Used = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_II))) {
                            ecgIIWave = data.getByteArray(String.valueOf(KParamType.ECG_II));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_II, ecgIIWave);
                            }
                            /*sentToWave(2, ecgIIWave);*/
                            ecgIIUsed = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_I))) {
                            ecgIWave = data.getByteArray(String.valueOf(KParamType.ECG_I));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_I, ecgIWave);
                            }
                            ecgIUsed = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_III))) {
                            ecgIIIWave = data.getByteArray(String.valueOf(KParamType.ECG_III));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_III,
                                        ecgIIIWave);
                            }
                            ecgIIIUsed = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_AVR))) {
                            ecgAVRWave = data.getByteArray(String.valueOf(KParamType.ECG_AVR));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_AVR,
                                        ecgAVRWave);
                            }
                            ecgAVRUsed = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_AVL))) {
                            ecgAVLWave = data.getByteArray(String.valueOf(KParamType.ECG_AVL));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_AVL,
                                        ecgAVLWave);
                            }
                            ecgAVLUsed = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_AVF))) {
                            ecgAVFWave = data.getByteArray(String.valueOf(KParamType.ECG_AVF));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_AVF,
                                        ecgAVFWave);
                            }
                            ecgAVFUsed = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V1))) {
                            ecgV1Wave = data.getByteArray(String.valueOf(KParamType.ECG_V1));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V1, ecgV1Wave);
                            }
                            ecgV1Used = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V2))) {
                            ecgV2Wave = data.getByteArray(String.valueOf(KParamType.ECG_V2));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V2, ecgV2Wave);
                            }
                            ecgV2Used = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V3))) {
                            ecgV3Wave = data.getByteArray(String.valueOf(KParamType.ECG_V3));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V3, ecgV3Wave);
                            }
                            ecgV3Used = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V4))) {
                            ecgV4Wave = data.getByteArray(String.valueOf(KParamType.ECG_V4));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V4, ecgV4Wave);
                            }
                            ecgV4Used = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V5))) {
                            ecgV5Wave = data.getByteArray(String.valueOf(KParamType.ECG_V5));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V5, ecgV5Wave);
                            }
                            ecgV5Used = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V6))) {
                            ecgV6Wave = data.getByteArray(String.valueOf(KParamType.ECG_V6));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V6, ecgV6Wave);
                            }
                            ecgV6Used = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .FHR_EAG))) {
                            fhrEag = data.getByteArray(String.valueOf(KParamType.FHR_EAG));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.FHR_EAG, fhrEag);
                            }
                            fhrEagUsed = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .FHR2))) {
                            fhr2Eag = data.getByteArray(String.valueOf(KParamType.FHR2));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.FHR2, fhr2Eag);
                            }
                            fhr2EagUsed = false;
                        } else if (data.containsKey(String.valueOf(KParamType
                                .FHR_TOCO))) {
                            fhrtoco = data.getByteArray(String.valueOf(KParamType.FHR_TOCO));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.FHR_TOCO, fhrtoco);
                            }
                            fhrtocosed = false;
                        }
                        break;
                    case NetConstant.NET_NIBP_CONFIG:
                        nibpConfig.put(msg.arg1, msg.arg2);
                        if (sendMsg != null) {
                            sendMsg.sendConfig(msg.what, msg.arg1, msg.arg2);
                        }
                        break;
                    case NetConstant.NET_SPO2_CONFIG:
                        spo2Config.put(msg.arg1, msg.arg2);
                        if (msg.arg1 == 0x05) {
                            StoreConstant.leffoff = msg.arg2;
                        }
                        if (sendMsg != null) {
                            sendMsg.sendConfig(msg.what, msg.arg1, msg.arg2);
                        }
                        break;
                    case NetConstant.NET_ECG_CONFIG:
                        ecgConfig.put(msg.arg1, msg.arg2);
                        if (sendMsg != null) {
                            sendMsg.sendConfig(msg.what, msg.arg1, msg.arg2);
                        }
                        break;
                    //temp
                    case NetConstant.NET_TEMP_CONFIG:
                        tempConfig.put(msg.arg1, msg.arg2);
                        StoreConstant.anInt = msg.arg2;
                        if (sendMsg != null) {
                            sendMsg.sendConfig(msg.what, msg.arg1, msg.arg2);
                        }
                        break;
                    //身份证
                    case NetConstant.NET_PATIENT_CONFIG:
                        Bundle bundle = msg.getData();
                        byte[] idcards = bundle.getByteArray("idcard");
                        byte[] name = bundle.getByteArray("name");
                        byte[] born = bundle.getByteArray("born");
                        byte type = bundle.getByte("type");
                        byte sex = bundle.getByte("sex");
                        byte[] picture = bundle.getByteArray("picture");
                        String id = new String(idcards, StandardCharsets.UTF_8);
                        String n = new String(name, StandardCharsets.UTF_8);
                        byte[] address = bundle.getByteArray("address");
                        String strAddress = new String(address, StandardCharsets.UTF_8);
                        // 根据协议解析出生日期，拼接成出生日期字符串
                        String birthday = "";
                        if (born.length >= 4) {
                            birthday = String.valueOf((born[0] & 0xFF)
                                    + ((born[1] & 0xFF) << 8)) + ""
                                    + String.valueOf(born[2] & 0xFF) + ""
                                    + String.valueOf(born[3] & 0xFF);
                        }
                        String pictureString = new String(picture, StandardCharsets.ISO_8859_1);
                        sendMsg.sendPersonalDetail(n, id, sex, type, birthday,
                                pictureString, strAddress);
                        break;
                    case NetConstant.NET_12LEAD_DIAG_RESULT:
                        Bundle result = msg.getData();
                        leadDiaResult = result.getByteArray("12leaddiaresult");
                        if (sendMsg != null) {
                            sendMsg.send12LeadDiaResult(leadDiaResult);
                        }
                        break;

                    //打印AppDevice保活包
                    case NetConstant.APP_DEVICE_HEART:
                        isAppDeviceHeart = true;
                        if (sendMsg != null) {

                            sendMsg.sendHeartBeat(msg.getData());
                        }
                        break;
                    //打印保活包
                    case NetConstant.PRINT_STAY_ALIVE:
                        if (sendDeviceManager != null) {
//                            Logger.w("zrl", "AIDL receiver HB:"+msg.getData());
                            sendDeviceManager.sendHeartBeat(msg.getData());
                        }
                        break;
                    //打印配置包
                    case NetConstant.PRINT_CONFIG:
                        if (sendDeviceManager != null) {
                            sendDeviceManager.sendConfig1(msg.what, msg.arg1, msg.arg2);
                        }
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    });

    //计时器 5秒钟执行一次 检查AppDevice启动状态
    private CountDownTimer mCountDownTimer = new CountDownTimer(5 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (isAppDeviceHeart) {
                isAppDeviceHeart = false;
            } else {
                int deviceConfig = MeasureUtils.getDeviceConfig();
                MeasureUtils.startAppDevice(getApplication(), 2, deviceConfig);
            }
            mCountDownTimer.start();
        }


    };
}
