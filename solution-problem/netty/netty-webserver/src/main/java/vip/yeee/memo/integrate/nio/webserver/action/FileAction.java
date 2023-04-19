package vip.yeee.memo.integrate.nio.webserver.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import vip.yeee.memo.integrate.nio.webserver.ServerSetting;
import vip.yeee.memo.integrate.nio.webserver.handler.Request;
import vip.yeee.memo.integrate.nio.webserver.handler.Response;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * 默认的主页Action，当访问主页且没有定义主页Action时，调用此Action
 * 
 * @author Looly
 *
 */
public class FileAction implements Action {
	private static final Log log = LogFactory.get();

	private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
	private static final SimpleDateFormat HTTP_DATE_FORMATER = new SimpleDateFormat(DatePattern.HTTP_DATETIME_PATTERN, Locale.US);

	@Override
	public void doAction(Request request, Response response) {
		if (!Request.METHOD_GET.equalsIgnoreCase(request.getMethod())) {
			response.sendError(HttpResponseStatus.METHOD_NOT_ALLOWED, "Please use GET method to request file!");
			return;
		}
		
		if(!ServerSetting.isRootAvailable()){
			response.sendError(HttpResponseStatus.NOT_FOUND, "404 Root dir not avaliable!");
			return;
		}

		final File file = getFileByPath(request.getPath());
		log.debug("Client [{}] get file [{}]", request.getIp(), file.getPath());
		
		// 隐藏文件，跳过
		if (file.isHidden() || !file.exists()) {
			response.sendError(HttpResponseStatus.NOT_FOUND, "404 File not found!");
			return;
		}

		// 非文件，跳过
		if (!file.isFile()) {
			response.sendError(HttpResponseStatus.FORBIDDEN, "403 Forbidden!");
			return;
		}

		// Cache Validation
		String ifModifiedSince = request.getHeader(HttpHeaderNames.IF_MODIFIED_SINCE.toString());
		if (StrUtil.isNotBlank(ifModifiedSince)) {
			Date ifModifiedSinceDate = null;
			try {
				ifModifiedSinceDate = DateUtil.parse(ifModifiedSince, HTTP_DATE_FORMATER);
			} catch (Exception e) {
				log.warn("If-Modified-Since header parse error: {}", e.getMessage());
			}
			if(ifModifiedSinceDate != null) {
				// 只对比到秒一级别
				long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
				long fileLastModifiedSeconds = file.lastModified() / 1000;
				if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds) {
					log.debug("File {} not modified.", file.getPath());
					response.sendNotModified();
					return;
				}
			}
		}
		
		response.setContent(file);
	}
	
	/**
	 * 通过URL中的path获得文件的绝对路径
	 * 
	 * @param httpPath Http请求的Path
	 * @return 文件绝对路径
	 */
	public static File getFileByPath(String httpPath) {
		// Decode the path.
		try {
			httpPath = URLDecoder.decode(httpPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Error(e);
		}

		if (httpPath.isEmpty() || httpPath.charAt(0) != '/') {
			return null;
		}

		// 路径安全检查
		if (httpPath.contains("/.") || httpPath.contains("./") || httpPath.charAt(0) == '.' || httpPath.charAt(httpPath.length() - 1) == '.' || ReUtil.isMatch(INSECURE_URI, httpPath)) {
			return null;
		}

		// 转换为绝对路径
		return FileUtil.file(ServerSetting.getRoot(), httpPath);
	}
}