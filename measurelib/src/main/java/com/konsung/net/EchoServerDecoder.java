package com.konsung.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.konsung.constant.Configuration;
import com.konsung.constant.NetConstant;
import com.konsung.util.Logger;
import com.konsung.util.MeasureUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;


/**
 * @author ouyangfan
 * @version 0.0.1
 * netty 服务器解码器
 * 此类主要是进行解码操作
 * 读取AppDevice发送上来的数据
 */
public class EchoServerDecoder extends ByteToMessageDecoder {

    private Handler handler;
    private Message message;
    private ByteBuf byteBuff;

    /**
     * 无参构造器
     */
    public EchoServerDecoder() {
    }

    /**
     * 带数据处理的构造器
     *
     * @param handler 数据处理
     */
    public EchoServerDecoder(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf byteBuf, List<Object> objects) {

        while (byteBuf.isReadable()) {

            byteBuf.markReaderIndex();
            short head = byteBuf.readUnsignedByte();
            if (head != 0x00FF) {                // 寻找包头
                System.out.printf("Head is error, %X\n", head);
                continue;
            }

            int len = byteBuf.order(ByteOrder.LITTLE_ENDIAN)
                    .readUnsignedShort();     // 长度
            if (byteBuf.readableBytes() + 3 < len) {
                byteBuf.resetReaderIndex();
                return;
            }

            byte cmdId = byteBuf.readByte();            // 命令字
            byte[] mingling = new byte[1];
            mingling[0] = cmdId;
            int serialNO = byteBuf.order(ByteOrder.LITTLE_ENDIAN)
                    .readUnsignedShort();     // 序列号
            int checkSum = byteBuf.order(ByteOrder.LITTLE_ENDIAN)
                    .readUnsignedShort();     // 校验和
            if (checkSum(byteBuf, len) != checkSum) {
                System.out.printf("Checksum is error, %X\n", checkSum);
                byteBuf.resetReaderIndex();
                byteBuf.readChar();
                continue;
            }


            ByteBuf data = byteBuf.order(ByteOrder.LITTLE_ENDIAN).readBytes(len - 8);

            if ((int) cmdId == 128) {
                handlerBlogicCheckInfo(data, len - 8);
                break;
            }
            byteBuff = byteBuf;
            switch (cmdId) {
                // 趋势数据
                case NetConstant.NET_TREND:
                    handleTrendPkg(data);
                    break;
                // 波形数据
                case NetConstant.NET_WAVE:
                    handleWavePkg(data);
                    break;
                //量点测量数据
                case NetConstant.NET_POINT:
                    handlerPointData(data);
                    break;
                //量点状态
                case NetConstant.NET_POINT_STATUS:
                    handlerPointStatus(data);
                    break;
                case NetConstant.PARA_STATUS:
                    int param = data.order(ByteOrder.LITTLE_ENDIAN).readUnsignedShort();
                    short isActive = data.readUnsignedByte();
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(new byte[10]);

                    //获取参数模块名称和版本号
                    Bundle paraBundle = new Bundle();
                    byte[] temp = new byte[32];
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(temp);
                    for (int i = 0; i < temp.length; i++) {
                        if ((temp[i] & 0xFF) == 0) {
                            byte[] paraName = Arrays.copyOf(temp, i);
                            paraBundle.putByteArray("paraBoardName", paraName);
                            break;
                        }
                    }
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(temp);
                    for (int i = 0; i < temp.length; i++) {
                        if ((temp[i] & 0xFF) == 0) {
                            byte[] paraVersion = Arrays.copyOf(temp, i);
                            paraBundle.putByteArray("paraBoardVersion",
                                    paraVersion);
                            break;
                        }
                    }

                    message = Message.obtain();
                    message.what = NetConstant.PARA_STATUS;
                    message.arg1 = param;
                    message.arg2 = isActive;
                    message.setData(paraBundle);
                    handler.sendMessage(message);
                    break;
                case NetConstant.NET_ECG_CONFIG:
                case NetConstant.NET_SPO2_CONFIG:
                case NetConstant.NET_NIBP_CONFIG:

                    message = Message.obtain();
                    message.what = cmdId;
                    message.arg1 = data.order(ByteOrder.LITTLE_ENDIAN)
                            .readShort();
                    message.arg2 = data.order(ByteOrder.LITTLE_ENDIAN)
                            .readInt();
                    handler.sendMessage(message);
                    break;
                case NetConstant.NET_PATIENT_CONFIG:
                    message = Message.obtain();
                    message.what = cmdId;
                    Bundle bundle = new Bundle();
                    byte[] bytes = new byte[64];

                    byte[] born = new byte[7];
                    byte[] entry = new byte[7];
                    byte[] idcard = new byte[18];
                    byte[] picture = new byte[1024];
                    byte[] address = new byte[70];
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(new byte[64]);
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(new byte[64]);
                    byte type = data.order(ByteOrder.LITTLE_ENDIAN).readByte();
                    byte sex = data.order(ByteOrder.LITTLE_ENDIAN).readByte();
                    bundle.putByte("type", type);
                    bundle.putByte("sex", sex);
                    byte blood = data.order(ByteOrder.LITTLE_ENDIAN).readByte();
                    int weight = data.order(ByteOrder.LITTLE_ENDIAN).readInt();
                    int height = data.order(ByteOrder.LITTLE_ENDIAN).readInt();
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(new byte[2]);
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(born);
                    bundle.putByteArray("born", born);
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(entry);
                    byte isbo = data.order(ByteOrder.LITTLE_ENDIAN).readByte();
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(idcard)
                            .readBytes(new byte[46]);
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(bytes);
                    for (int i = 0; i < bytes.length; i++) {
                        if ((bytes[i] & 0xFF) == 0) {
                            byte[] name = Arrays.copyOf(bytes, i);
                            bundle.putByteArray("name", name);
                            break;
                        }
                    }
                    /*String id=new String(bytes,"UTF-8");*/
                    bundle.putByteArray("idcard", idcard);
                    // 读取病人名
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(bytes);
                    // 读取医生姓名
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(bytes);
                    // 读取科室
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(bytes);
                    // 读取照片信息
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(picture);
                    // 读取住址
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(address);
                    bundle.putByteArray("address", address);
                    bundle.putByteArray("picture", picture);
                    message.setData(bundle);
                    handler.sendMessage(message);

                    break;
                case NetConstant.NET_TEMP_CONFIG:
                    message = Message.obtain();
                    message.what = cmdId;
                    message.arg1 = data.order(ByteOrder.LITTLE_ENDIAN)
                            .readShort();
                    message.arg2 = data.order(ByteOrder.LITTLE_ENDIAN)
                            .readInt();
                    handler.sendMessage(message);
                    break;
                case NetConstant.NET_12LEAD_DIAG_RESULT:
                    handle12LeadDiaResult(data, len - 8);
                    break;

                //打印保活包
                case NetConstant.PRINT_STAY_ALIVE:
                    if (ByteBufUtil.hexDump(data).length() < 18)
                        return;
                    message = Message.obtain();
                    message.what = cmdId;
                    String time = getTime(data.readBytes(7));
//                    Logger.d("zrl", "心跳：" + time);
                    Bundle bundleHeartBeat = new Bundle();
                    bundleHeartBeat.putString("heartBeat", time);
                    message.setData(bundleHeartBeat);
                    handler.sendMessage(message);
                    break;
                //打印APPDEVICE心跳包
                case NetConstant.APP_DEVICE_HEART:
                    message = Message.obtain();
                    message.what = cmdId;
                    handler.sendMessage(message);
                    break;
                //打印配置包
                case NetConstant.PRINT_CONFIG:
//                    Logger.d("zrl", "配置包：" + ByteBufUtil.hexDump(data));
                    message = Message.obtain();
                    message.what = cmdId;
                    //配置类型
                    message.arg1 = data.order(ByteOrder.LITTLE_ENDIAN)
                            .readShort();
                    //配置值
                    message.arg2 = data.order(ByteOrder.LITTLE_ENDIAN)
                            .readInt();
                    handler.sendMessage(message);
                    break;

                default:
                    try {
                        message = Message.obtain();
                        message.what = cmdId;
                        message.arg1 = data.order(ByteOrder.LITTLE_ENDIAN)
                                .readShort();
                        message.arg2 = data.order(ByteOrder.LITTLE_ENDIAN)
                                .readInt();
                        handler.sendMessage(message);
                    } catch (IndexOutOfBoundsException e) {

                    }
                    break;
            }
        }
    }

    private void handlerBlogicCheckInfo(ByteBuf buf, int len) {
        //打印测试调试
        byte[] by = new byte[byteBuff.capacity()];
        for (int i = 0; i < byteBuff.capacity(); i++) {
            byte b = byteBuff.getByte(i);
            by[i] = b;
            Log.v("BlogicCheckInfo_byte", "" + (int) b);
        }
        //数组转换为16进制
        MeasureUtils.printHexString(by);
        //处理传过来的生化仪测试数据
        buf.readBytes(7);
        Bundle temp = new Bundle();
        temp.putByteArray(Configuration.BLOGIC_INFO_KEY, buf.readBytes(len - 7).array());
        message = Message.obtain();
        message.what = Configuration.NET_BLOGIC_CHECK_INFO;
        message.setData(temp);
        handler.sendMessage(message);
    }

    /**
     * 校验和
     *
     * @param buf 验证的数据
     * @param len 长度
     * @return 和
     */
    private int checkSum(ByteBuf buf, int len) {
        int sum = 0;
        for (int i = 0; i < len; i++) {
            // 校验位以及头不参与计算
            if (i == 0 || i == 6 || i == 7) {
                continue;
            }
            sum += buf.getUnsignedByte(i);
        }
        return sum & 0xFFFF;
    }

    /**
     * 读取趋势数据
     *
     * @param buf 网络字节
     *            每一对参数类型与参数值的字节数为6个
     */
    private void handleTrendPkg(ByteBuf buf) {
        int paramCount = buf.order(ByteOrder.LITTLE_ENDIAN).readInt();
        // 参数数量
        netTime(buf.readBytes(7));
        buf.order(ByteOrder.LITTLE_ENDIAN).readShort();      // 保留字节
        while (buf.isReadable()) {
            int param = buf.order(ByteOrder.LITTLE_ENDIAN).readUnsignedShort(); // 参数类型
            int value = buf.order(ByteOrder.LITTLE_ENDIAN).readInt();
            // 参数数值

            // message 数据载体
            message = Message.obtain();
            message.what = NetConstant.NET_TREND;
            message.arg1 = param;
            message.arg2 = value;
            handler.sendMessage(message);
        }
    }

    /**
     * 回调数据
     *
     * @param buf 数据
     */
    private void handleWavePkg(ByteBuf buf) {
        int param = buf.order(ByteOrder.LITTLE_ENDIAN).readUnsignedShort();
        // 参数类型
        buf.readBytes(9);
        int waveSize = buf.order(ByteOrder.LITTLE_ENDIAN).readUnsignedShort();
        Bundle temp = new Bundle();
        temp.putByteArray(String.valueOf(param), buf.readBytes(waveSize)
                .array());
        message = Message.obtain();
        message.what = NetConstant.NET_WAVE;
        message.setData(temp);

        handler.sendMessage(message);
    }

    /**
     * 回调心电数据
     *
     * @param buf 数据
     * @param len 数据类型
     */
    private void handle12LeadDiaResult(ByteBuf buf, int len) {
        buf.readBytes(7);
        Bundle temp = new Bundle();
        temp.putByteArray("12leaddiaresult", buf.readBytes(len - 7).array());
        message = Message.obtain();
        message.what = NetConstant.NET_12LEAD_DIAG_RESULT;
        message.setData(temp);
        handler.sendMessage(message);
    }

    /**
     * 读取网络时间
     *
     * @param buf 网络字节
     */
    private void netTime(ByteBuf buf) {
        // 时间戳为7个字节
        if (buf.capacity() != 7) {
            return;
        }
        short year = buf.order(ByteOrder.LITTLE_ENDIAN).readShort();     // 年
        byte month = buf.readByte();        // 月
        byte day = buf.readByte();          // 日
        byte hour = buf.readByte();         // 时
        byte minute = buf.readByte();       // 分
        byte second = buf.readByte();       // 秒
    }

    /**
     * 读取网络时间
     *
     * @param buf 网络字节
     */
    private String getTime(ByteBuf buf) {
        // 时间戳为7个字节
        if (buf.capacity() != 7) {
            return "";
        }
        short year = buf.order(ByteOrder.LITTLE_ENDIAN).readShort();     // 年
        byte month = buf.readByte();        // 月
        byte day = buf.readByte();          // 日
        byte hour = buf.readByte();         // 时
        byte minute = buf.readByte();       // 分
        byte second = buf.readByte();       // 秒

        return (int) year + "-" + (int) month + 1 + "-" + (int) day + " " + (int) hour + ":" + (int) minute + ":" + (int) second;
    }


    /**
     * 解析发送点测结果数据包
     *
     * @param data 数据
     */
    private void handlerPointData(ByteBuf data) {
        byte deviceType = data.readByte(); //设备类型
        data.order(ByteOrder.LITTLE_ENDIAN).readInt(); //参数数量
        data.order(ByteOrder.LITTLE_ENDIAN).readShort(); // 保留字节
        switch (deviceType) {
            case 0x01://生化分析仪
            case 0x02://荧光免疫分析仪
                while (data.isReadable()) {
                    int param = data.order(ByteOrder.LITTLE_ENDIAN).readUnsignedShort(); // 参数类型
                    byte[] value = data.readBytes(16).array(); //参数数值
                    byte[] result = getNewBytes(value, Byte.parseByte("0"));
                    data.readBytes(16); //参数单位
                    data.readBytes(16); //参数范围
                    data.readBytes(16); //保留字节
                    Bundle temp = new Bundle();
                    temp.putString(Configuration.BLOGIC_INFO_KEY, new String(result, StandardCharsets.UTF_8));
                    Logger.d("zrl", "量点: " + temp.toString());
                    message = Message.obtain();
                    message.what = NetConstant.NET_POINT;
                    message.arg1 = param;
                    message.setData(temp);
                    handler.sendMessage(message);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 解析发送点测状态包
     *
     * @param data
     */
    private void handlerPointStatus(ByteBuf data) {
        Short configType = data.readShort();//配置类型
        int value = data.readInt();//配置值
        switch (configType) {
            case 0x00://设备状态改变时
            case 0x01://有点测设备连接或断开时，点测设备的连接方式
                message = Message.obtain();
                message.what = NetConstant.NET_POINT_STATUS;
                message.arg1 = configType;
                message.arg2 = value;
                handler.sendMessage(message);
                Logger.d("zrl", "Receiver Point " + "case:" + configType + " value: " + value);
                break;
        }
    }

    /**
     * 解析获取数值字节数组
     *
     * @param bytes 原始字节数组
     * @param value 目标字节角标
     * @return 数值字节数组
     */
    private byte[] getNewBytes(byte[] bytes, byte value) {
        int index = -1;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == value) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            return Arrays.copyOfRange(bytes, 0, index);
        } else {
            return bytes;
        }
    }

}
