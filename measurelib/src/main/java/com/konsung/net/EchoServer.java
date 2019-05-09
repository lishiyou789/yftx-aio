package com.konsung.net;

import android.content.Context;
import android.os.Handler;

import com.konsung.constant.NetConstant;
import com.konsung.util.Logger;
import com.konsung.util.MeasureUtils;

import java.nio.ByteOrder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author ouyangfan
 * @version 0.0.1
 * @date 2015-01-12 18:36
 * 使用netty 框架的服务器类
 */
public class EchoServer {
    // 端口号.final一旦赋值不能更改
    private Handler handler;
    private Context mContext;

    /*
     * 构造器，带端口号和handler数据处理
     * @param port
     * @param handler
     */
    public EchoServer(Context mContext, Handler handler) {
        this.mContext = mContext;
        this.handler = handler;
    }

    /*
     * 启动方法
     * @throws Exception
     */
    public void start() throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap1 = new ServerBootstrap();
            bootstrap1.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            Logger.i("zrl", "AppDevice Listen: " + channel.localAddress().getPort());
                            ChannelPipeline _pipeline = channel.pipeline();
                            // 进行数据分包处理,这里的参数是根据具体协议来指定的
                            _pipeline.addLast("decoder", new
                                    LengthFieldBasedFrameDecoder(
                                    ByteOrder.LITTLE_ENDIAN,
                                    Integer.MAX_VALUE, 1, 2, -3, 0, false));
                            _pipeline.addLast(new EchoServerDecoder(handler));
                            if (channel.localAddress().getPort() == NetConstant.PORT) {
                                _pipeline.addLast("encoder1", new EchoServerEncoder(mContext));
                                //Logger.i("zrl", "Receiver From AppDevice: " + channel.localAddress().getPort());
                            }
                            if (channel.localAddress().getPort() == NetConstant.DM_PORT) {
                                _pipeline.addLast("encoder2", new EchoDeviceManagerServerEncoder());
                                //Logger.i("zrl", "Receiver From DeviceManager: " + channel.localAddress().getPort());
                            }
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            //绑定端口开始接收
            ChannelFuture f = bootstrap1.bind(NetConstant.PORT).sync();
            Logger.i("zrl", "start listen: " + f.channel().localAddress() );

            ChannelFuture f1 = bootstrap1.bind(NetConstant.DM_PORT).sync();
            Logger.i("zrl", "start listen: " + f1.channel().localAddress());

            f.channel().closeFuture().sync();
            f1.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
