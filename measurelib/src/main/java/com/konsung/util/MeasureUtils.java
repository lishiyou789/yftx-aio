package com.konsung.util;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.konsung.R;
import com.konsung.bean.MeasureBean;
import com.konsung.bean.StateBean;
import com.konsung.constant.Configuration;
import com.konsung.constant.DiagCodeToText;
import com.konsung.constant.EcgDefine;
import com.konsung.constant.KParamType;
import com.konsung.constant.NetConstant;
import com.konsung.constant.ProtocolDefine;
import com.konsung.constant.RespDefine;
import com.konsung.constant.Spo2Define;
import com.konsung.constant.TempDefine;
import com.konsung.listen.AppDeviceListen;
import com.konsung.listen.DeviceManagerStateListen;
import com.konsung.listen.IdCardListen;
import com.konsung.listen.Measure;
import com.konsung.listen.MeasureCompleteListen;
import com.konsung.listen.MeasureListener;
import com.konsung.net.EchoServerEncoder;
import com.konsung.service.AIDLServer;

import static com.konsung.constant.NetConstant.NET_POINT_STATUS;
import static com.konsung.constant.NetConstant.PRINT_CONFIG;

/**
 * 工具类
 */

public class MeasureUtils {
    private static MeasureBean bean; //记录测量项的bean
    private static Handler mHandler = new Handler();
    private static AIDLServer aidlServer; //appDevice服务
    private static AppDeviceListen appDeviceListen; //监听数据的方法
    private static boolean isEChecking = false; // 是否已经开始测量心电
    private static boolean isEcgConnect = false; // ecg是否有连接
    private static int progress = 30; // 进度条默认值
    private static MeasureCompleteListen l; //AD监听类
    private static MeasureListener listener; //AD监听类
    private static IdCardListen cardListen; //监听类
    private static DeviceManagerStateListen deviceManagerListen;//DM监听

    private static Context context;
    private static boolean isSChecking = false; //血氧的测量
    private static int spo2Value;
    private static int measureCount = 0;
    private static int sProgress = 15; //血氧
    private static int spo2Pr;
    private static boolean attachSpo2 = false; //血氧是否有连接
    public static int measureState = 0;              // 血压测量状态
    private static int heartState = 0; //台心的连接状态
    static boolean finger = false;
    static boolean probe = false;

    private static final String APP_DEVICE = "org.qtproject.qt5.android.bindings";
    private static final String DEVICE_MANAGER = "com.konsung.devicemanager";

    public static int deviceConfig;

    /**
     * 设置测量完成的监听类
     *
     * @param le     监听的类
     * @param contex 上下文
     */
    public static void setMeasureListen(MeasureCompleteListen le, Context contex) {
        l = le;
        context = contex;
    }

    public static void setMeasureListener(MeasureListener listener1) {
        listener = listener1;
    }


    public static void setIdCardListen(IdCardListen cardListen1) {
        cardListen = cardListen1;
    }

    public static void setPrinterStateListen(DeviceManagerStateListen psl) {
        deviceManagerListen = psl;
    }

    /**
     * 让线程运行到主线程
     *
     * @param task 线程
     */
    public static void post(Runnable task) {
        mHandler.post(task);
    }

    /**
     * 设置测量bean
     *
     * @param b 测量bean
     */
    public static void setMeasureBean(MeasureBean b) {
        bean = b;
    }

    public static int getDeviceConfig() {
        return deviceConfig;
    }

    /**
     * 开始测量的方法
     */
    public static boolean startMeasure(Measure measure) {
        switch (measure) {
            case SPO2:
                Log.e("p", "我开始测量  attachSpo2 = " + attachSpo2);
                if (attachSpo2 && !isSChecking) {
                    isSChecking = true;
                    sProgress = 15;
                    spo2Value = 0;
                    measureCount = 0;
                } else {
                    sProgress = 15;
                    isSChecking = false;
                    return false;
                }
                break;
            case NIBP:
                if (measureState == 0) {
                    /* handler.post(updateThread);*/            // 更新数据
                        /*_aidlInterface.sendNibpConfig(0x05, 0);         //
                        发送启动测量指令*/
//                    EchoServerEncoder.setEcgConfig((short)0x05,3);
                    // 暂时通过发送两条命令的方式，解决连续多次快速点击启动测量
                    // 按钮时，偶发下发命令不成功的问题
                    EchoServerEncoder.setNibpConfig((short) 0x05, 0);
                    EchoServerEncoder.setNibpConfig((short) 0x05, 0);
                    measureState = 1;
                } else {
                    EchoServerEncoder.setNibpConfig((short) 0x06, 0);
                    EchoServerEncoder.setNibpConfig((short) 0x06, 0);
                    measureState = 0;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获取指定key的float
     *
     * @param mContext mContext
     * @param name     name
     * @param key      key
     * @param defValue defValue
     * @return 返回值
     */
    public static synchronized float getSpFloat(Context mContext, String
            name, String key, float defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }

    public static void setAppDeviceListen(AppDeviceListen l) {
        appDeviceListen = l;
    }

    /**
     * 启动appDevice和DeviceManager
     *
     * @param context    上下文
     * @param deviceType 1: DM,  2: AD
     */
    public static void startAppDevice(Application context, int deviceType, int deviceConfig1) {
        //启动DeviceManager
        if (deviceType == 1) {
            Intent DeviceManagerIntent = context.getPackageManager().getLaunchIntentForPackage(
                    DEVICE_MANAGER);
            if (DeviceManagerIntent != null && checkPackInfo(context, DEVICE_MANAGER)) {
                context.startActivity(DeviceManagerIntent);
                startDeviceManagerService(context);
            }
        }

        //利用包名，启动AppDevice
        if (deviceType == 2) {
            deviceConfig = deviceConfig1;
            Intent AppDeviceIntent = context.getPackageManager().getLaunchIntentForPackage(
                    APP_DEVICE);
            if (AppDeviceIntent != null && checkPackInfo(context, APP_DEVICE)) {
                context.startActivity(AppDeviceIntent);
                startService(context);
            }
        }
    }

    /**
     * 检查包名是否存在
     */
    public static boolean checkPackInfo(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("lsy", "PackageName NotFoundException");
        }
        return packageInfo != null;
    }

    /**
     * @param context
     */
    public static Intent startService(Context context) {
        Intent intent = new Intent(context, AIDLServer.class);
        context.startService(intent);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        // 设置心电导联类型，旧版本的硬件，拔交流电时会导致参数板断电重启，为规避此问题，在进入心电
        // 界面时再发送一次导联设置命令
        int value = MeasureUtils.getSpInt(context.getApplicationContext(), "sys_config", "ecg_lead_system",
                EcgDefine.ECG_12_LEAD);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_LEAD_SYSTEM, value);
        // 为了避免硬件上体温对心电的干扰，此处关闭体温测量
        EchoServerEncoder.setTempConfig(ProtocolDefine.NET_TEMP_START_STOP, TempDefine.TEMP_STOP);
        EchoServerEncoder.setTempConfig(ProtocolDefine.NET_TEMP_START_STOP, TempDefine.TEMP_STOP);
        return intent;
    }

    private static Intent startDeviceManagerService(Context context) {
        Intent intent = new Intent(context, AIDLServer.class);
        context.startService(intent);
        context.bindService(intent, serviceManagerConnection, Context.BIND_AUTO_CREATE);
        return intent;
    }

    //AppDeviceManager 连接器
    public static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidlServer = ((AIDLServer.MsgBinder) service).getService();
            aidlServer.setSendMSGToFragment(new AIDLServer.SendMSGToFragment() {
                @Override
                public void sendParaStatus(String name, String version) {
                    if (null != appDeviceListen) {
                        appDeviceListen.sendParaStatus(name, version);
                    }
                }

                @Override
                public void sendWave(int param, byte[] bytes) {
                    if (null != appDeviceListen) {
                        appDeviceListen.sendWave(param, bytes);
                    }
                    if (null == bean) {
                        return;
                    }
                    switch (param) {
                        case com.konsung.constant.KParamType.ECG_I:
                            if (isEChecking) {
                                bean.setEcgWave(1, UnitConvertUtil.bytesToHexString(bytes));
                            }
                            break;
                        case com.konsung.constant.KParamType.ECG_II:
                            if (isEChecking) {
                                bean.setEcgWave(2, UnitConvertUtil.bytesToHexString(bytes));
                            }

                            break;
                        case com.konsung.constant.KParamType.ECG_III:
                            if (isEChecking) {
                                bean.setEcgWave(3, UnitConvertUtil.bytesToHexString(bytes));
                            }
                            break;
                        case com.konsung.constant.KParamType.ECG_AVR:
                            if (isEChecking) {
                                bean.setEcgWave(4, UnitConvertUtil.bytesToHexString(bytes));
                                if (isEChecking) {
                                    progress--;
                                    if (progress == 0) {
                                        isEChecking = false;
                                        if (null != l) {
                                            l.onFail(Measure.ECG, context.getString(R.string
                                                    .ecg_check_timeout));
                                        }
                                    }
                                }
                            }
                            break;
                        case com.konsung.constant.KParamType.ECG_AVL:
                            if (isEChecking) {
                                bean.setEcgWave(5, UnitConvertUtil.bytesToHexString(bytes));
                            }
                            break;
                        case com.konsung.constant.KParamType.ECG_AVF:
                            if (isEChecking) {
                                bean.setEcgWave(6, UnitConvertUtil.bytesToHexString(bytes));
                            }
                            break;

                        case com.konsung.constant.KParamType.ECG_V1:
                            if (isEChecking) {
                                bean.setEcgWave(7, UnitConvertUtil.bytesToHexString(bytes));
                            }
                            break;
                        case com.konsung.constant.KParamType.ECG_V2:
                            if (isEChecking) {
                                bean.setEcgWave(8, UnitConvertUtil.bytesToHexString(bytes));
                            }
                            break;
                        case com.konsung.constant.KParamType.ECG_V3:
                            if (isEChecking) {
                                bean.setEcgWave(9, UnitConvertUtil.bytesToHexString(bytes));
                            }
                            break;
                        case com.konsung.constant.KParamType.ECG_V4:
                            if (isEChecking) {
                                bean.setEcgWave(10, UnitConvertUtil.bytesToHexString(bytes));
                            }
                            break;
                        case com.konsung.constant.KParamType.ECG_V5:
                            if (isEChecking) {
                                bean.setEcgWave(11, UnitConvertUtil.bytesToHexString(bytes));
                            }
                            break;
                        case com.konsung.constant.KParamType.ECG_V6:
                            if (isEChecking) {
                                bean.setEcgWave(12, UnitConvertUtil.bytesToHexString(bytes));
                            }
                            break;
                        case KParamType.SPO2_WAVE:
                            if (isSChecking) {
                                sProgress--;
                                if (sProgress == 0) {
                                    isSChecking = false;
                                    sProgress = 15;
                                    if (null != l) {
                                        l.onFail(Measure.SPO2, context.
                                                getString(R.string.ecg_check_timeout));
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void sendTrend(int param, int value) {
                    if (value != -1000) {
                        Logger.i("lsy", "param=" + param + ",value=" + value);
                    }
                    if (null != appDeviceListen) {
                        appDeviceListen.sendTrend(param, value);
                    }
                    int v1 = value / Configuration.TREND_FACTOR;
                    String str = valueToString(v1);
                    switch (param) {
                        case KParamType.SPO2_TREND:
                            if (isSChecking) {
                                if ((Math.abs(spo2Value - value /
                                        Configuration.TREND_FACTOR) < 4)
                                        && value != Configuration.INVALID_DATA) {
                                    if ((measureCount++) == 6) {
                                        measureCount = 0;
                                        sProgress = 15;
                                        bean.setSpo2(spo2Value);
                                        bean.setPr(spo2Pr);
                                        if (null != l) {
                                            l.onComplete(Measure.SPO2, bean);
                                        }
                                        isSChecking = false;
                                        return;
                                    }
                                } else {
                                    spo2Value = value / Configuration.TREND_FACTOR;
                                    measureCount = 0;
                                }
                            }
                            break;
                        case KParamType.SPO2_PR:
                            spo2Pr = value / Configuration.TREND_FACTOR;
                            break;
                        case KParamType.NIBP_SYS:
                            if (value > 0) {
                                bean.setSbp(value / Configuration.TREND_FACTOR);
                            }
                            break;
                        case KParamType.NIBP_DIA:
                            if (measureState == 2) {
                                if (value > 0) {
                                    measureState = 0;
                                    bean.setDbp(value / Configuration.TREND_FACTOR);
                                }
                            }
                            break;
                        case KParamType.NIBP_MAP:
                            if (value > 0) {
                                bean.setMbp(value / Configuration.TREND_FACTOR);
                            }
                            break;
                        case KParamType.NIBP_PR:
                            if (value > 0) {
                                bean.setNibPr(value / Configuration.TREND_FACTOR);
                                if (null != l) {
                                    l.onComplete(Measure.NIBP, bean);
                                }
                            }
                            break;
                        case KParamType.IRTEMP_TREND:
                            if (value != Configuration.INVALID_DATA) {
                                float temp = NumUtil.trans2FloatValue((float) value /
                                        Configuration.TREND_FACTOR, 1);
                                bean.setTemp(temp);
                                if (null != l) {
                                    l.onComplete(Measure.TEMP, bean);
                                }
                                if (listener != null) {
                                    listener.onComplete(Measure.TEMP, bean);
                                }
                            }
                            break;
                        //血糖902
                        case KParamType.BLOODGLU_AFTER_MEAL:
                            if (value != Configuration.INVALID_DATA) {
                                bean.setGlu((float) value / Configuration.TREND_FACTOR);
                                if (null != l) {
                                    l.onComplete(Measure.GLU, bean);
                                }
                                if (listener != null) {
                                    listener.onComplete(Measure.GLU, bean);
                                }
                            }
                            break;
                        //血糖901
                        case KParamType.BLOODGLU_BEFORE_MEAL:
                            if (value != Configuration.INVALID_DATA) {
                                bean.setGlu((float) value / Configuration.TREND_FACTOR);
                                if (null != l) {
                                    l.onComplete(Measure.GLU, bean);
                                }
                                if (listener != null) {
                                    listener.onComplete(Measure.GLU, bean);
                                }
                            }
                            break;
                        //尿酸 1001
                        case KParamType.URICACID_TREND:
                            if (value != Configuration.INVALID_DATA) {
                                //umol/L做单位
                                bean.setUricacid(value * 10);
                                if (null != l) {
                                    l.onComplete(Measure.UA, bean);
                                }
                                if (listener != null) {
                                    listener.onComplete(Measure.UA, bean);
                                }
                            }
                            break;
                        //总胆固醇 1101
                        case KParamType.CHOLESTEROL_TREND:
                            if (value != Configuration.INVALID_DATA) {
                                float chol = (float) value / Configuration.TREND_FACTOR;
                                bean.setXzzdgc(chol);
                                if (null != l) {
                                    l.onComplete(Measure.CHOL, bean);
                                }
                                if (listener != null) {
                                    listener.onComplete(Measure.CHOL, bean);
                                }
                            }
                            break;
                        //血红蛋白 1402
                        case KParamType.BLOOD_HGB:
                            if (value != Configuration.INVALID_DATA) {
                                int hgb = value / Configuration.TREND_FACTOR;
                                //g/L转mmol/L
//                                String result = String.format("%.1f", hgb / 10 * 0.621);
                                bean.setAssxhb(hgb);
                                sendData(Measure.HB);
                            }
                            break;
                        //血红蛋白压积值 1403
                        case KParamType.BLOOD_HCT:
                            if (value != Configuration.INVALID_DATA) {
                                int hgb = value / Configuration.TREND_FACTOR;
                                bean.setAssxhct(hgb);
                                sendData(Measure.HCT);
                            }
                            break;
                        case KParamType.URINERT_LEU://1201
                            bean.setUrineLeu(str);
                            break;
                        case KParamType.URINERT_NIT://1202
                            bean.setUrineNit(str);
                            break;
                        case KParamType.URINERT_UBG://1203
                            bean.setUrineUbg(str);
                            break;
                        case KParamType.URINERT_PRO://1204
                            bean.setUrinePro(str);
                            break;
                        case KParamType.URINERT_PH://1205
                            float ph = value / 100.0f;
                            bean.setUrinePh(ph);
                            break;
                        case KParamType.URINERT_BLD://1206
                            bean.setUrineBld(str);
                            break;
                        case KParamType.URINERT_SG://1207
                            double sg = (double) value / 1000.0f;
                            bean.setUrineSg(sg);
                            break;
                        case KParamType.URINERT_BIL://1208
                            bean.setUrineBil(str);
                            break;
                        case KParamType.URINERT_KET://1209
                            bean.setUrineKet(str);
                            break;
                        case KParamType.URINERT_GLU://1210
                            bean.setUrineGlu(str);
                            sendData(Measure.URINE);
                            break;
                        case KParamType.URINERT_VC://1211
                            bean.setUrineVc(str);
                            break;
                        case KParamType.URINERT_ALB://1212
                            bean.setUrineMa(str);
                            break;
                        case KParamType.URINERT_ASC://1213
                            bean.setUrineAsc(str);
                            break;
                        case KParamType.URINERT_CRE://1214
                            bean.setUrineCre(str);
                            break;
                        case KParamType.URINERT_CA://1215
                            bean.setUrineCa(str);
                            break;
                        //血脂四项
                        case KParamType.LIPIDS_TC:
//                            param = 1501, value = -10
//                            param = 1502, value = 54
//                            param = 1503, value = 49
//                            param = 1504, value = -10
                            if (value == OverCheckUtil.FLAG_BELOW_MIN) {
                                bean.setBlood_tc(OverCheckUtil.getOverMinString(param, value));
                            } else if (value == OverCheckUtil.FLAG_OVER_MAX) {
                                bean.setBlood_tc(OverCheckUtil.getOverMaxString(param, value));
                            } else {
                                double tc = value / 100.0;
                                bean.setBlood_tc(String.format("%.2f", tc));
                            }
                            break;
                        case KParamType.LIPIDS_TG://54
                            if (value == OverCheckUtil.FLAG_BELOW_MIN) {
                                bean.setBlood_tg(OverCheckUtil.getOverMinString(param, value));
                            } else if (value == OverCheckUtil.FLAG_OVER_MAX) {
                                bean.setBlood_tg(OverCheckUtil.getOverMaxString(param, value));
                            } else {
                                double tg = value / 100.0;
                                bean.setBlood_tg(String.format("%.2f", tg));
                            }
                            break;
                        case KParamType.LIPIDS_HDL://49
                            if (value == OverCheckUtil.FLAG_BELOW_MIN) {
                                bean.setBlood_hdl(OverCheckUtil.getOverMinString(param, value));
                            } else if (value == OverCheckUtil.FLAG_OVER_MAX) {
                                bean.setBlood_hdl(OverCheckUtil.getOverMaxString(param, value));
                            } else {
                                double hdl = value / 100.0;
                                bean.setBlood_hdl(String.format("%.2f", hdl));
                            }
                            break;
                        case KParamType.LIPIDS_LDL:
                            if (value == OverCheckUtil.FLAG_BELOW_MIN) {
                                bean.setBlood_ldl(OverCheckUtil.getOverMinString(param, value));
                            } else if (value == OverCheckUtil.FLAG_OVER_MAX) {
                                bean.setBlood_ldl(OverCheckUtil.getOverMaxString(param, value));
                            } else {
                                double ldl = value / 100.0;
                                bean.setBlood_ldl(String.format("%.2f", ldl));
                            }
                            sendData(Measure.BLOOD);
                            break;
                        //白细胞
                        case KParamType.BLOOD_WBC:
                            //AppDevice定的单位是10的6次方，再乘以100。一体机界面显示的是10的9次方。
                            float wbc = value / (Configuration.TREND_FACTOR * 1000.0f);
                            bean.setWbc(wbc);
                            sendData(Measure.WBC);
                            break;
                        case KParamType.BLOOD_LYM:
                            float lym = value / (Configuration.TREND_FACTOR * 1000.0f);
                            bean.setWbc_lym(lym);
                            sendData(Measure.WBC);
                            break;
                        case KParamType.BLOOD_MON:
                            float mon = value / (Configuration.TREND_FACTOR * 1000.0f);
                            bean.setWbc_mon(mon);
                            sendData(Measure.WBC);
                            break;
                        case KParamType.BLOOD_NEU:
                            float neu = value / (Configuration.TREND_FACTOR * 1000.0f);
                            bean.setWbc_neu(neu);
                            sendData(Measure.WBC);
                            break;
                        case KParamType.BLOOD_EOS:
                            float eos = value / (Configuration.TREND_FACTOR * 1000.0f);
                            bean.setWbc_eos(eos);
                            sendData(Measure.WBC);
                            break;
                        case KParamType.BLOOD_BAS:
                            float bas = value / (Configuration.TREND_FACTOR * 1000.0f);
                            bean.setWbc_bas(bas);
                            sendData(Measure.WBC);
                            break;
                        //糖化血红蛋白
                        case KParamType.HBA1C_NGSP:
                            if (value == OverCheckUtil.FLAG_BELOW_MIN) {
                                bean.setNgsp(OverCheckUtil.getOverMinString(param, value));
                            } else if (value == OverCheckUtil.FLAG_OVER_MAX) {
                                bean.setNgsp(OverCheckUtil.getOverMaxString(param, value));
                            } else {
                                double ngsp = value / 100.0;
                                bean.setNgsp(String.format("%.1f", ngsp));
                            }
                            break;
                        case KParamType.HBA1C_IFCC:
                            if (value == OverCheckUtil.FLAG_BELOW_MIN) {
                                bean.setIfcc(OverCheckUtil.getOverMinString(param, value));
                            } else if (value == OverCheckUtil.FLAG_OVER_MAX) {
                                bean.setIfcc(OverCheckUtil.getOverMaxString(param, value));
                            } else {
                                double ifcc = value / 100.0;
                                bean.setIfcc(String.format("%.1f", ifcc));
                            }
                            break;
                        case KParamType.HBA1C_EAG:
                            if (value == OverCheckUtil.FLAG_BELOW_MIN) {
                                bean.setEag(OverCheckUtil.getOverMinString(param, value));
                            } else if (value == OverCheckUtil.FLAG_OVER_MAX) {
                                bean.setEag(OverCheckUtil.getOverMaxString(param, value));
                            } else {
                                double eag = value / 100.0;
                                bean.setEag(String.format("%.1f", eag));
                            }
                            sendData(Measure.GHB);
                            break;
                        default:
                            break;
                    }
                }

                //干式免疫分析仪
                @Override
                public void sendTrend(int param, String value) {
                    Logger.i("lsy", "param=" + param + ",value=" + value);

                    if (param == KParamType.FIA_PCT) {
                        bean.setFia_pct(value);
                        if (l != null)
                            l.onComplete(Measure.PCT, bean);
                        if (listener != null) {
                            listener.onComplete(Measure.PCT, bean);
                        }
                    }

                    if (param == KParamType.FIA_CTNI || param == KParamType.FIA_CK_MB
                            || param == KParamType.FIA_MYO) {
                        if (param == KParamType.FIA_CTNI)
                            bean.setFia_ctnl(value);
                        if (param == KParamType.FIA_CK_MB)
                            bean.setFia_ckmb(value);
                        if (param == KParamType.FIA_MYO)
                            bean.setFia_myo(value);

                        sendData(Measure.MYO);

                    }
                }

                @Override
                public void sendConfig(int con, int param, int value) {
                    if (null != appDeviceListen) {
                        appDeviceListen.sendConfig(param, value);
                    }
                    if (con == NetConstant.NET_ECG_CONFIG || con == NetConstant.NET_NIBP_CONFIG) {
                        switch (param) {
                            case 0x10:
                                if (value == 0) {
                                    attachSpo2 = true;
                                    isEcgConnect = true;
                                } else {
                                    attachSpo2 = false;
                                    isEcgConnect = false;
                                }
                                break;
                            case 0x07:
                                if (measureState == 1) {
                                    if (value == 1) {
                                        measureState = 2;
                                    }
                                }
                                break;
                            case 0x02:
                                if (value > 0) {
                                    measureState = 0;
                                    showMeasureResult(value);
                                }
                                break;
                            case 0x04:
                                if (measureState == 2) {
                                    if (null != l) {
                                        l.NibpCuff(value == Configuration.INVALID_DATA ? 0 : value);
                                    }
                                }
                        }
                    }
                    if (con == NetConstant.NET_FHR_CONFIG) {
                        switch (param) {
                            case 0x06:
                                heartState = value;
                                Log.e("p", "我收到信号 = " + value);
                                break;
                            case 0x07:
                                break;
                            default:
                                break;
                        }
                    }

                    if (con == NetConstant.NET_ECG_CONFIG || con == NetConstant.NET_SPO2_CONFIG) {
                        measureState(con, param, value);
                    }
                }

                @Override
                public void sendHeartBeat(Bundle pBundle) {
                }

                //向网络发送身份证信息
                @Override
                public void sendPersonalDetail(String name, String idcard, int sex, int type,
                                               String birthday, String picture, String a) {
                    if (null != cardListen) {
                        cardListen.onListen(name, idcard, sex, type, birthday, picture, a);
                    }
                }

                @Override
                public void send12LeadDiaResult(byte[] bytes) {
                    if (null != appDeviceListen) {
                        appDeviceListen.send12LeadDiaResult(bytes);
                    }
                    String diaResult = " "; // 12导诊断结果
                    // 只有在测量过程中才对12导诊断结果进行处理，用户停止测量、
                    // 重新刷新界面等情况下不对12导诊断结果进行处理。
                    if (!isEChecking) {
                        return;
                    }
                    //根据AppDevice中的协议，诊断结果最多有26个数据（不包括时间戳）
                    if (bytes.length != 52) {
                        Log.v("HealthOne", "12 Lead Dia Result len is not right!");
                    }
                    int[] result = new int[26];

                    for (int i = 0; i < 26; i++) {
                        result[i] = (short) (((bytes[i * 2 + 1] & 0x00FF)
                                << 8) | (0x00FF & bytes[i * 2]));
                    }
                    int hrValue = result[0]; // HR值
                    int prInterval = result[1]; // PR间期
                    int qrsDuration = result[2]; // QRS间期, 单位ms
                    int qt = result[3]; // QT间期
                    int qtc = result[4]; // QTC间期
                    int pAxis = result[5]; // P 波轴
                    int qrsAxis = result[6]; // QRS波心电轴
                    int tAxis = result[7]; // T波心电轴
                    int rv5 = result[8]; // RV5, 单位0.01ms
                    int sv1 = result[9]; // SV1, 单位0.01ms
                    if (prInterval < 0) {
                        prInterval = (short) -prInterval;
                    }

                    diaResult = String.valueOf(hrValue) + "," + String
                            .valueOf(prInterval) + ","
                            + String.valueOf(qrsDuration) + "," + String
                            .valueOf(qt) + ","
                            + String.valueOf(qtc) + "," + String.valueOf(pAxis) + ","
                            + String.valueOf(qrsAxis) + "," + String.valueOf(tAxis) + ","
                            + String.format("%.2f", (float) rv5 / 100) + ","
                            + String.format("%.2f", (float) sv1 / 100) + ","
                            + String.format("%.2f", ((float) rv5 / 100 +
                            (float) sv1 / 100)) + ",";

                    //根据AppDevice协议，诊断码有16个，但不是所有都有效
                    DiagCodeToText diagCodeToText = new DiagCodeToText();
                    for (int i = 0; i < 16; i++) {
                        for (int j = 0; j < DiagCodeToText.ecg12LeadDiagText.length; j++) {
                            String[] str = DiagCodeToText.ecg12LeadDiagText[j].split(":");
                            if (result[10 + i] == Integer.parseInt(str[0])) {
                                diaResult += str[1];
                                if ((str[1] != null) && (!"".equals(str[1]))) {
                                    diaResult += ";";
                                }
                            }
                        }
                    }
                    isEChecking = false;
                    bean.setHr(hrValue);
                    bean.setAnal(diaResult);
                    if (null != l) {
                        l.onComplete(Measure.ECG, bean);
                    }
                }


            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (null != appDeviceListen) {
                appDeviceListen.onServiceDisconnected(name);
            }
        }
    };

    //DeviceManager 连接器
    public static ServiceConnection serviceManagerConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidlServer = ((AIDLServer.MsgBinder) service).getService();
            aidlServer.setSendDeviceManager(new AIDLServer.SetMsgFromDeviceManager() {

                @Override
                public void sendTrend1(int param, String value) {
                    Logger.i("zrl", "Param: " + param + " value: " + value);
                    //hs-CRP
                    if (param == KParamType.FIA_HS_CRP) {
                        bean.setFia_hscrp(value);
                        sendData(Measure.Hs_CRP);
                    }

                    //CRP
                    if (param == KParamType.FIA_CRP) {
                        bean.setFia_crp(value);
                        sendData(Measure.CRP);
                    }

                    //SAA
                    if (param == KParamType.FIA_SAA) {
                        bean.setFia_saa(value);
                        sendData(Measure.SAA);
                    }
                }

                @Override
                public void sendConfig1(int config, int param, int value) {
                    //打印机打印配置包
                    if (config == PRINT_CONFIG) {
                        printState(config, param, value);
                    }
                    //点测
                    if (config == NET_POINT_STATUS) {
                        if (null != deviceManagerListen) {
                            deviceManagerListen.onPointState(param, value);
                        }
                    }
                }

                @Override
                public void sendHeartBeat(Bundle pBundle) {
                    //向网络发送DeviceManager心跳
                    if (null != deviceManagerListen) {
                        deviceManagerListen.onPrinterHeartBeat(pBundle);
//                        Logger.i("zrl", "MeasureUtils Receiver HB from AIDL: "+pBundle.toString());
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    static StateBean state = new StateBean();

    /**
     * 分析状态的方法
     *
     * @param con   设备类型
     * @param param 设备附件
     * @param value 状态值
     */
    private static void measureState(int con, int param, int value) {
        if (state == null)
            state = new StateBean();
        switch (con) {
            case 0x21:
                if (param == 0x10) {
                    if ((value & 0x2) == 0x2) {
                        state.setLa(false);
                    } else {
                        state.setLa(true);
                    }
                    if ((value & 0x4) == 0x4) {
                        state.setRa(false);
                    } else {
                        state.setRa(true);
                    }
                    if ((value & 0x1) == 0x1) {
                        state.setLl(false);
                    } else {
                        state.setLl(true);
                    }
                    if ((value & 0x8) == 0x8) {
                        state.setV1(false);
                    } else {
                        state.setV1(true);
                    }
                    if ((value & 0x10) == 0x10) {
                        state.setV2(false);
                    } else {
                        state.setV2(true);
                    }
                    if ((value & 0x20) == 0x20) {
                        state.setV3(false);
                    } else {
                        state.setV3(true);
                    }
                    if ((value & 0x40) == 0x40) {
                        state.setV4(false);
                    } else {
                        state.setV4(true);
                    }
                    if ((value & 0x80) == 0x80) {
                        state.setV5(false);
                    } else {
                        state.setV5(true);
                    }
                    if ((value & 0x100) == 0x100) {
                        state.setV6(false);
                    } else {
                        state.setV6(true);
                    }
                }
                break;
            case 0x24:
                if (param == 0x05) {
                    if (value == 0) {
                        finger = true;
                        probe = true;
                    } else if (value == 1) {
                        finger = false;
                        probe = false;
                    } else if (value == 2) {
                        finger = false;
                        probe = true;
                    }
                }
                break;

            default:
                break;
        }
        state.setFinger(finger);
        state.setProbe(probe);
        if (null != l) {
            l.onState(state);
        }
    }

    /**
     * 打印机状态监听
     *
     * @param con
     * @param param
     * @param value
     */
    private static void printState(int con, int param, int value) {
        if (state == null)
            state = new StateBean();
//        Logger.e("zrl", "MeasureUtilsState: " + con + " param:" + param + " value:" + value);
        switch (con) {
            //打印配置包
            case PRINT_CONFIG:
                switch (param) {
                    //设备状态改变时，打印设备状态
                    case 0x00:
                        state.setPrintDeviceState(value);
                        break;
                    //打印机连接或断开时，连接方式
                    case 0x01:
                        state.setPrintConnState(value);
                        break;
                    //打印命令执行结果
                    case 0x02:
                        state.setPrintResult(value);
                        break;

                    default:
                        break;
                }
            default:
                break;
        }
        if (null != deviceManagerListen) {
            deviceManagerListen.onPrinterState(state);
            state = null;
        }
    }

    /**
     * 发生测量成功的监听数据
     */
    private static void sendData(final Measure measure) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != l) {
                    l.onComplete(measure, bean);
                }
                if (listener != null) {
                    listener.onComplete(measure, bean);
                }
            }
        }, 200);
    }


    /**
     * 手动解绑服务
     */
    public static void unbindService() {
        context.unbindService(serviceConnection);
        context.unbindService(serviceManagerConnection);
    }

    /**
     * 初始化配置信息
     *
     * @param context 上下文
     */
    public static void initSysConfig(Context context, int measureConfig) {
        //注意：需要优先进行设备配置初始化
//        value = MeasureUtils.getSpInt(context, "sys_config",
//                "device_config", Configuration.DEVICE_CONFIG);
        int value = measureConfig;
//        Logger.e("lsy", "value=" + value);
        EchoServerEncoder.setDeviceConfig(ProtocolDefine.NET_DEVICE_CONFIG, value);
        //病人信息初始化
//        int type = MeasureUtils.getSpInt(context, "sys_config", "type", PatientDefine.ADULT);
//        int sex = MeasureUtils.getSpInt(context, "sys_config", "sex", PatientDefine.UNKNOWN);
//        int blood = MeasureUtils.getSpInt(context, "sys_config", "blood", PatientDefine.A);
//        float weight = MeasureUtils.getSpFloat(context, "sys_config", "weight", (float) 0.0);
//        float height = MeasureUtils.getSpFloat(context, "sys_config", "height", (float) 0.0);
//        EchoServerEncoder.setPatientConfig((short) type, (short) sex, (short) blood, weight, height, (short) 0);
        //心电参数配置初始化
        //波形速度
//        SpUtils.getSpInt(getApplicationContext(), "sys_config",
//        "ecg_wave_speed", value);
//         增益
//        SpUtils.getSpInt(getApplicationContext(), "sys_config",
//        "ecg_gain", value);
        // 导联系统
        value = MeasureUtils.getSpInt(context, "sys_config", "ecg_lead_system", EcgDefine.ECG_12_LEAD);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_LEAD_SYSTEM, value);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_LEAD_SYSTEM, value);
        // 计算导联
        value = MeasureUtils.getSpInt(context, "sys_config", "ecg_calc_lead", EcgDefine.ECG_LEAD_II);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_CALC_LEAD, value);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_CALC_LEAD, value);
        // 工频干扰抑制开关
        value = MeasureUtils.getSpInt(context, "sys_config", "ecg_hum_filter_mode", EcgDefine.ECG_HUM_ON);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_HUMFILTER_MODE, value);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_HUMFILTER_MODE, value);
        // 滤波模式
        value = MeasureUtils.getSpInt(context, "sys_config", "ecg_filter_mode", EcgDefine.ECG_FILTER_DIAGNOSIS);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_FILTER_MODE, value);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_FILTER_MODE, value);
        // PACE开关
        value = MeasureUtils.getSpInt(context, "sys_config", "ecg_pace_mode", EcgDefine.ECG_PACE_UNKNOW);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_PACE_SWITCH, value);
        EchoServerEncoder.setEcgConfig(ProtocolDefine.NET_ECG_PACE_SWITCH, value);
        // ST分析开关
//        value = SpUtils.getSpInt(getApplicationContext(),
//                "sys_config", "ecg_st_analysis", EcgDefine.ECG_ST_OFF);

        //血氧参数配置初始化/////////////////
        // 调制音
//        SpUtils.getSpInt(getApplicationContext(), "sys_config",
//                "spo2_pitch_tone", value);
        // 灵敏度
        value = MeasureUtils.getSpInt(context, "sys_config", "spo2_sensitivity", Spo2Define.SPO2_SENSITIVITY_MIDDLE);
        EchoServerEncoder.setSpo2Config(ProtocolDefine.NET_SPO2_SENSITIVE, value);
        EchoServerEncoder.setSpo2Config(ProtocolDefine.NET_SPO2_SENSITIVE, value);
        // 波形速度
        //SpUtils.getSpInt(getApplicationContext(), "sys_config",
        // "spo2_wave_speed", value);

        //血压参数配置初始化

        // 测量模式
//        value = SpUtils.getSpInt(getApplicationContext(),
//                "sys_config", "nibp_measure_mode", NibpDefine.MANUAL_MODE);
        //测量间隔
//        SpUtils.getSpInt(getApplicationContext(), "sys_config", "nibp_interval", value);

        //呼吸参数配置初始化
        // 呼吸导联
        value = MeasureUtils.getSpInt(context, "sys_config", "resp_lead_type", RespDefine.RESP_LEAD_II);
        EchoServerEncoder.setRespConfig(ProtocolDefine.NET_RESP_LEAD_TYPE, value);
        EchoServerEncoder.setRespConfig(ProtocolDefine.NET_RESP_LEAD_TYPE, value);
        // 窒息报警延迟时间
        value = MeasureUtils.getSpInt(context, "sys_config", "resp_apnea_time", RespDefine.RESP_APNEA_DELAY_20S);
        EchoServerEncoder.setRespConfig(ProtocolDefine.NET_RESP_APNEA_TIME, value);
        EchoServerEncoder.setRespConfig(ProtocolDefine.NET_RESP_APNEA_TIME, value);
        // 波形速度
        //SpUtils.getSpInt(getApplicationContext(), "sys_config",
        // "resp_wave_speed", value);
        // 波形增益
        //SpUtils.getSpInt(getApplicationContext(), "sys_config",
        // "resp_wave_gain", value);

        //参数配置初始化
        // 体温类型
        value = MeasureUtils.getSpInt(context, "sys_config", "temp_type", TempDefine.TEMP_INFRARED);
        EchoServerEncoder.setTempConfig(ProtocolDefine.NET_TEMP_TYPE, value);
        EchoServerEncoder.setTempConfig(ProtocolDefine.NET_TEMP_TYPE, value);
    }


    /**
     * 保存字符串
     *
     * @param mContext mContext
     * @param name     name
     * @param key      key
     * @param value    value
     */
    public static synchronized void saveToSp(Context mContext, String name,
                                             String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 保存小数
     *
     * @param mContext mContext
     * @param name     name
     * @param key      key
     * @param value    value
     */
    public static synchronized void saveToSp(Context mContext, String name,
                                             String key, float value) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 获取指定key的int
     *
     * @param mContext mContext
     * @param name     name
     * @param key      key
     * @param defValue defValue
     * @return 返回值
     */
    public static synchronized int getSpInt(Context mContext, String name,
                                            String key, int defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    /**
     * 根据身份证判断性别
     *
     * @param idCard 身份证号码
     */
    public static String judgeSexByIdCard(String idCard) {
        String sexFlag = "";
        if (TextUtils.isEmpty(idCard)) {
            sexFlag = "男";
            return sexFlag;
        }
        if (idCard.length() != 18) {
            return "";
        }
        float flag = Float.parseFloat(idCard.substring(16, 17));
        if ((flag % 2) == 1) {
            //奇数为男性 偶数为女性
            sexFlag = "男";
        } else {
            sexFlag = "女";
        }
        return sexFlag;
    }

    /**
     * 显示测量结果
     *
     * @param code 结果值
     */
    private static void showMeasureResult(int code) {
        String result = new String();
        switch (code) {
            case 0:
                result = context.getString(R.string.nibbp_result_0);
                break;
            case 1:
                result = context.getString(R.string.nibbp_result_1);
                break;
            case 2:
                result = context.getString(R.string.nibbp_result_2);
                break;
            case 3:
                result = context.getString(R.string.nibbp_result_3);
                break;
            case 4:
                result = context.getString(R.string.nibbp_result_4);
                break;
            case 5:
                result = context.getString(R.string.nibbp_result_5);
                break;
            case 6:
                result = context.getString(R.string.nibbp_result_6);
                break;
            case 7:
            case 8:
                result = context.getString(R.string.nibbp_result_7);
                break;
            case 9:
                result = context.getString(R.string.nibbp_result_8);
                break;
            case 10:
                result = context.getString(R.string.nibbp_result_9);
                break;
            case 11:
                result = context.getString(R.string.nibbp_result_10);
                break;
            case 12:
                result = context.getString(R.string.nibbp_result_11);
                break;
            case 13:
                result = context.getString(R.string.nibbp_result_12);
                break;
            default:
                break;
        }
        if (code != 0 && null != l) {
            l.onFail(Measure.NIBP, result);
        }
    }

    /**
     * 尿常规值转换
     *
     * @param value 模块传递过来的值
     * @return 显示测量值
     */
    public static String valueToString(int value) {
        switch (value) {
            case -1:
                return "-";
            case 0:
                return "+-";
            case 1:
                return "+1";
            case 2:
                return "+2";
            case 3:
                return "+3";
            case 4:
                return "+4";
            case 5:
                return "+";
            case 6:
                return "";
            default:
                return "";
        }
    }

    /**
     * 数组转换为16进制
     */
    public static void printHexString(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase() + " ");
            Log.v("all_check_info", hex.toUpperCase() + " ");
        }
    }
}
