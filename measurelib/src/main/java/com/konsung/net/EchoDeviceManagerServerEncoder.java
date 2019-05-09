package com.konsung.net;

import com.konsung.constant.NetConstant;
import com.konsung.util.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * DeviceManager专用
 *
 * @author Runnlin
 * @date 2019/1/23/0023.
 */

public class EchoDeviceManagerServerEncoder extends ChannelInboundHandlerAdapter {
    // mBuf包含帧头和帧体的每包数据
    private static ArrayList<ByteBuf> cmds = new ArrayList<>();
    // 帧体
    private static ByteBuf mBodyBuf;
    // 序列号，从0开始递增
    private static int mSerialNo = 0;
    private static final int HEAD_SIZE = 8;

    private static void sendData(byte cmdId, int len) {
        ByteBuf buff = Unpooled.buffer();
        // 帧头
        buff.writeByte(0xFF);
        buff.order(ByteOrder.LITTLE_ENDIAN).writeShort(HEAD_SIZE + len);
        buff.writeByte(cmdId);
        buff.order(ByteOrder.LITTLE_ENDIAN).writeShort(mSerialNo++);

        // 将读写指针放在校验位,将校验位补为0
        mBodyBuf.resetReaderIndex();
        buff.markWriterIndex();
        buff.order(ByteOrder.LITTLE_ENDIAN).writeShort(0);
        // 在校验位后加上帧体内容
        int sum = checkSum(buff.writeBytes(mBodyBuf));
        // 将写指针重置到校验位重新赋值
        buff.resetWriterIndex();
        buff.order(ByteOrder.LITTLE_ENDIAN).writeShort(sum);
        // 重新赋帧体内容
        mBodyBuf.resetReaderIndex();
        buff.writeBytes(mBodyBuf);

        cmds.add(buff);
//        mBuf.resetReaderIndex();
    }

    /**
     * 向网络发送打印配置包
     *
     * @param cmdId 命令字ID  0x03: 颜色         0x04: 纸张大小  0x05: 取消打印
     * @param value 值         03: 0黑白|1彩色     04: 0A4|1A5   05: 无
     */
    public static void setPrintConfig(int cmdId, int value) {
        Logger.d("zrl", "发送配置包: " + cmdId + "\nvalue: " + value);
        mBodyBuf = Unpooled.buffer();
        mBodyBuf.markReaderIndex();
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeShort(cmdId);
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeInt(value);
        sendData(NetConstant.PRINT_CONFIG, mBodyBuf.readableBytes());
    }

    /**
     * 向网络发送打印数据包
     *
     * @param path 文件路径
     */
    public static void setPrintData(String path) {
        if (path == null || path.length() == 0)
            return;

        byte[] desFilePath = new byte[255];

        byte[] pathString = path.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(pathString, 0, desFilePath, 0, pathString.length);
//            Logger.i("zrl","send data package: "+ Arrays.toString(desFilePath)+"\nPath: "+path);

        mBodyBuf = Unpooled.buffer();
        mBodyBuf.markReaderIndex();
        mBodyBuf.writeBytes(desFilePath);
        sendData(NetConstant.PRINT_DATA, mBodyBuf.readableBytes());
    }

    /**
     * 校验和
     *
     * @param buf 命令字ID
     * @return sum 正确
     */
    private static int checkSum(ByteBuf buf) {
        int sum = 0;
        for (int i = 0; i < buf.capacity(); i++) {
            // 校验位以及头不参与计算
            if (i == 0 || i == 6 || i == 7) {
                continue;
            }
            sum += buf.getUnsignedByte(i);
        }
        return sum;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        if (cmds.isEmpty()) {
            return;
        }
        for (int i = 0; i < cmds.size(); i++) {
            System.out.println("channelReadCompleteList: " + cmds.get(i).toString());
            System.out.println("send = " + ByteBufUtil.hexDump(cmds.get(i)));
            ctx.writeAndFlush(cmds.get(i));
        }
        cmds.clear();
    }
}
