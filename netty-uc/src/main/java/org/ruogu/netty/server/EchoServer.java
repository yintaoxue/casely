package org.ruogu.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.ruogu.netty.handler.EchoServerHandler;

public class EchoServer {
	 private final int port;

	    public EchoServer(int port) {
	        this.port = port;
	    }

	    public void run() throws Exception {
	        // Configure the server.
	        EventLoopGroup bossGroup = new NioEventLoopGroup();
	        EventLoopGroup workerGroup = new NioEventLoopGroup();
	        try {
	            ServerBootstrap b = new ServerBootstrap();
	            b.group(bossGroup, workerGroup)
	             .channel(NioServerSocketChannel.class)
	             .option(ChannelOption.SO_BACKLOG, 100)
	             .handler(new LoggingHandler(LogLevel.INFO))
	             .childHandler(new ChannelInitializer<SocketChannel>() {
	                 @Override
	                 public void initChannel(SocketChannel ch) throws Exception {
	                     ch.pipeline().addLast(
	                             //new LoggingHandler(LogLevel.INFO),
	                             new EchoServerHandler());
	                 }
	             });

	            // Start the server.
	            ChannelFuture f = b.bind(port).sync();

	            // Wait until the server socket is closed.
	            f.channel().closeFuture().sync();
	        } finally {
	            // Shut down all event loops to terminate all threads.
	            bossGroup.shutdownGracefully();
	            workerGroup.shutdownGracefully();
	        }
	    }

	    public static void main(String[] args) throws Exception {
	        int port;
	        if (args.length > 0) {
	            port = Integer.parseInt(args[0]);
	        } else {
	            port = 8080;
	        }
	        new EchoServer(port).run();
	    }

}
