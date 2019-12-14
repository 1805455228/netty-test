package top.tomxwd.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author xieweidu
 * @createDate 2019-12-14 17:48
 */
public class NettySever {

    public static void main(String[] args) throws InterruptedException {
        /*
            说明：
            1. 创建两个线程组BossGroup以及WorkGroup
            2. BossGroup只是处理连接请求，真正与客户端的业务处理，会交给WorkerGroup完成
            3. 两个都是无限循环
         */
        // 创建BossGroup 以及 WorkGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 使用链式编程的方式进行设置
        bootstrap.group(bossGroup,workerGroup)          // 设置两个线程组
                .channel(NioServerSocketChannel.class)  // 使用NioServerSocketChannel作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128)//设置线程队列等待连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)// 设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道初始化对象（匿名对象）
                    // 向workerGroup关联的pipeline设置处理器
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(null);//添加处理器到pipeline尾部
                    }
                });//给workerGroup的EventLoop对应的管道设置处理器
        System.out.println("==============服务器 is ready==============");

        // 绑定一个端口并同步，生成了一个ChannelFuture对象
        // 启动了服务器并绑定端口
        ChannelFuture cf = bootstrap.bind(6688).sync();

        // 对关闭通道进行监听
        cf.channel().closeFuture().sync();

    }

}
