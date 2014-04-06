package org.ruogu.netty.handler;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeanerServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private static final Logger LOG = LoggerFactory.getLogger(LeanerServerHandler.class);

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
		final String uri = request.getUri();
		HttpMethod method = request.getMethod();

		// 请求参数
		Map<String, String> params = null;

		// GET
		if (method == HttpMethod.GET) {
			Map<String, List<String>> getParams = queryDecoder.parameters();
			params = parseParams(getParams);
		}
		// POST
		else if (method == HttpMethod.POST) {
			ByteBuf content = request.content();
			if (content.isReadable()) {
				String param = content.toString(Charset.forName("UTF-8"));
				QueryStringDecoder postQueryStringDecoder = new QueryStringDecoder("/?" + param);
				Map<String, List<String>> postParams = postQueryStringDecoder.parameters();
				params = parseParams(postParams);
			}
		}
		
		final String path = queryDecoder.path();

		LOG.info("req, path:" + path + ", method:" + method + ", uri:" + uri);

		LOG.info("params:" + params.toString());
		
		writeResponse(request, ctx);
	}

	private void writeResponse(FullHttpRequest request, ChannelHandlerContext ctx) {
		// Build the response object.
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, request.getDecoderResult().isSuccess() ? OK
				: BAD_REQUEST);

		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

		StringBuilder buf = new StringBuilder();
		buf.append("Hello\n");
		buf.append("world!\n");

		ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
		response.content().writeBytes(buffer);
		buffer.release();

		// Close the connection as soon as the error message is sent.
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private Map<String, String> parseParams(Map<String, List<String>> qParams) {
		Map<String, String> params = null;
		if (!qParams.isEmpty()) {
			params = new HashMap<String, String>();
			for (Entry<String, List<String>> p : qParams.entrySet()) {
				String key = p.getKey();
				String value = p.getValue().get(0);
				params.put(key, value);
			}
		}
		return params;
	}

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer("Failure: "
				+ status.toString() + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

		// Close the connection as soon as the error message is sent.
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// Close the connection when an exception is raised.
		LOG.warn("Unexpected exception from downstream.", cause);
		ctx.close();
	}
}
