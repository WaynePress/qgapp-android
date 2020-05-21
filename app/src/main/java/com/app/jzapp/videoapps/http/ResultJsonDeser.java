package com.app.jzapp.videoapps.http;

import com.app.jzapp.videoapps.base.BaseBean;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/12/15.
 */

public class ResultJsonDeser implements JsonDeserializer<BaseBean<?>> {

    @Override
    public BaseBean<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        BaseBean baseBean = new BaseBean();
        if (jsonElement.isJsonObject()) {
            JsonObject object = jsonElement.getAsJsonObject();
            String code = object.get("code").getAsString();
            baseBean.setCode(code);
            baseBean.setMsg(object.get("msg").getAsString());
            if (!code.equals("0"))
                return baseBean;
            if (code.equals("0")) {
                try {
                    Type itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
                    baseBean.setData(jsonDeserializationContext.deserialize(object.get("data"), itemType));
                } catch (Exception e) {
                    return baseBean;
                } finally {
                    return baseBean;
                }
            }
        }
        return baseBean;
    }
}
