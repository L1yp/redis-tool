package com.l1yp.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Lyp
 * @Date   2020-06-23
 * @Email  l1yp@qq.com
 */
public abstract class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 序列化对象
     * @param target 目标对象
     * @return 序列化结果文本
     */
    public static String serialize(Object target){
        try {
            return mapper.writeValueAsString(target);
        } catch (Exception e) {
            logger.error("mapper.writeValueAsString error: ", e);
            return null;
        }
    }

    /**
     * 反序列化对象
     * @param val   源字符串
     * @param clazz 目标类型
     * @param <T> 类型
     * @return 目标对象实例
     */
    public static <T> T deserialize(String val, Class<T> clazz){
        try {
            return mapper.readValue(val, clazz);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 反序列化对象
     * @param val   源字符串
     * @param type 目标类型
     * @param <T> 类型
     * @return 目标对象实例
     */
    public static <T> T deserialize(String val, TypeReference<T> type){
        try {
            return mapper.readValue(val, type);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

}
