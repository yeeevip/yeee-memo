package vip.yeee.memo.integrate.thirdsdk.pay.utils;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class StringKit {

	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "") + Thread.currentThread().getId();
	}

	public static String getUUID(int endAt){
		return getUUID().substring(0, endAt);
	}

	/** 拼接url参数 **/
	public static String appendUrlQuery(String url, Map<String, Object> map){

		if(StringUtils.isEmpty(url) || map == null || map.isEmpty()){
			return url;
		}

		StringBuilder sb = new StringBuilder(url);
		if(!url.contains("?")){
			sb.append("?");
		}

		//是否包含query条件
		boolean isHasCondition = url.contains("=");

		for (String k : map.keySet()) {
			if(k != null && map.get(k) != null){
				if(isHasCondition){
					sb.append("&"); //包含了查询条件， 那么应当拼接&符号
				}else{
					isHasCondition = true; //变更为： 已存在query条件
				}
				sb.append(k).append("=").append(URLUtil.encodeQuery(map.get(k).toString()));
			}
		}
		return sb.toString();
	}


	/** 拼接url参数: 旧版采用Hutool方式（当回调地址是 http://abc.com/#/abc 时存在位置问题） **/
	@Deprecated
	public static String appendUrlQueryWithHutool(String url, Map<String, Object> map){

		if(StringUtils.isEmpty(url) || map == null || map.isEmpty()){
			return url;
		}
		UrlBuilder result = UrlBuilder.of(url, StandardCharsets.UTF_8);
		map.forEach((k, v) -> {
			if(k != null && v != null){
				result.addQuery(k, v.toString());
			}
		});

		return result.build();
	}

	/** 是否 http 或 https连接 **/
	public static boolean isAvailableUrl(String url){

		if(StringUtils.isEmpty(url)){
			return false;
		}

		return url.startsWith("http://") ||url.startsWith("https://");
	}

	/**
	 * 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替
	 *
	 * @param content 传入的字符串
	 * @param frontNum 保留前面字符的位数
	 * @param endNum 保留后面字符的位数
	 * @return 带星号的字符串
	 */
	public static String str2Star2(String content, int frontNum, int endNum) {
		if (frontNum >= content.length() || frontNum < 0) {
			return content;
		}
		if (endNum >= content.length() || endNum < 0) {
			return content;
		}
		if (frontNum + endNum >= content.length()) {
			return content;
		}
		String starStr = "";
		for (int i = 0; i < (content.length() - frontNum - endNum); i++) {
			starStr = starStr + "*";
		}
		return content.substring(0, frontNum) + starStr
				+ content.substring(content.length() - endNum, content.length());
	}

	/**
	 * 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替
	 *
	 * @param content 传入的字符串
	 * @param frontNum 保留前面字符的位数
	 * @param endNum 保留后面字符的位数
	 * @param starNum 指定star的数量
	 * @return 带星号的字符串
	 */
	public static String str2Star(String content, int frontNum, int endNum, int starNum) {
		if (frontNum >= content.length() || frontNum < 0) {
			return content;
		}
		if (endNum >= content.length() || endNum < 0) {
			return content;
		}
		if (frontNum + endNum >= content.length()) {
			return content;
		}
		String starStr = "";
		for (int i = 0; i < starNum; i++) {
			starStr = starStr + "*";
		}
		return content.substring(0, frontNum) + starStr
				+ content.substring(content.length() - endNum, content.length());
	}


	/**
	 * 合并两个json字符串
	 * key相同，则后者覆盖前者的值
	 * key不同，则合并至前者
	 * @param originStr
	 * @param mergeStr
	 * @return 合并后的json字符串
	 */
	public static String marge(String originStr, String mergeStr) {

		if (StringUtils.isAnyBlank(originStr, mergeStr)) {
			return null;
		}

		JSONObject originJSON = JSONObject.parseObject(originStr);
		JSONObject mergeJSON = JSONObject.parseObject(mergeStr);

		if (originJSON == null || mergeJSON == null) {
			return null;
		}

		originJSON.putAll(mergeJSON);
		return originJSON.toJSONString();
	}

}
