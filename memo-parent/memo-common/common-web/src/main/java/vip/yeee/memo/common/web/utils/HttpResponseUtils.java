package vip.yeee.memo.common.web.utils;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 16:30
 */
public class HttpResponseUtils {

    /**
     * 返回数据封装
     */
    public static void write(HttpServletResponse response, Object data) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.append(JSONObject.toJSONString(data, SerializerFeature.WriteNullStringAsEmpty));
        IoUtil.close(out);
    }

}
