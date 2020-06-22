package com.snomyc.common.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpHelper {

    private final static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    public static String getBodyString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            char[] bodyCharBuffer = new char[1024];
            int len;
            while ((len = reader.read(bodyCharBuffer)) != -1) {
                sb.append(new String(bodyCharBuffer, 0, len));
            }
        } catch (IOException e) {
            logger.error("获取post请求body异常", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("输入流关闭异常", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("reader关闭异常", e);
                }
            }
        }

        if (sb.length() > 0) {
            //进行压缩处理，去除不必要的换行和空格
            try {
                return JSON.parseObject(sb.toString()).toString();
            } catch (Exception e) {
                logger.error("body解析异常，body={}", sb.toString());
            }
        }

        return sb.toString();
    }

}
