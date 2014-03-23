package org.ruogu.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Level;
import java.util.logging.Logger;

@Sharable
public class EchoClientHandler extends ChannelHandlerAdapter {

	 private static final Logger logger = Logger.getLogger(
	            EchoClientHandler.class.getName());

	    private final ByteBuf firstMessage;

	    /**
	     * Creates a client-side handler.
	     */
	    public EchoClientHandler(int firstMessageSize) {
	        if (firstMessageSize <= 0) {
	            throw new IllegalArgumentException("firstMessageSize: " + firstMessageSize);
	        }
	        firstMessage = Unpooled.buffer(firstMessageSize);
	        for (int i = 0; i < firstMessage.capacity(); i ++) {
	            firstMessage.writeByte((byte) i);
	        }
	    }

	    @Override
	    public void channelActive(ChannelHandlerContext ctx) {
	        ctx.writeAndFlush(firstMessage);
	    }

	    @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	    	System.out.println("client read." + msg);
//	        ctx.write(msg);
	    }

	    @Override
	    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	       ctx.flush();
	    }

	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	        // Close the connection when an exception is raised.
	        logger.log(Level.WARNING, "Unexpected exception from downstream.", cause);
	        ctx.close();
	    }
}
