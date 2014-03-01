package org.ruogu.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

@Sharable
public class EchoClientHandler extends ChannelHandlerAdapter {

	

	/**
	 *  Once the connection is established, a sequence of bytes is sent to the server. 
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive");
		ctx.write(Unpooled.copiedBuffer("Netty rocks!".getBytes(CharsetUtil.UTF_8)));
		System.out.println("write ok");
	}

	/**
	 * The  method  is  called  once  data  is received. 
	 * Note that the bytes may be fragmented
	 *  The only guarantee is that the bytes will be received in the same order as they’re sent.
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("channelRead");
		ByteBuf b = (ByteBuf)msg;
		System.out.println("Client received:" + ByteBufUtil.hexDump(b.readBytes(b.readableBytes())));
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
