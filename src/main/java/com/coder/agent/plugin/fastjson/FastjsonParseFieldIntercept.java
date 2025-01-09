package com.coder.agent.plugin.fastjson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.deserializer.ContextObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.FieldInfo;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company:  Co., Ltd.
 * Create Time: 2022/5/23 22:09
 *
 * @author coderLee23
 */
@Slf4j
public class FastjsonParseFieldIntercept {

    @RuntimeType
    public static void intercept(@Advice.This DefaultFieldDeserializer defaultFieldDeserializer,
                                 @Advice.FieldValue("fieldValueDeserilizer") ObjectDeserializer oldFieldValueDeserilizer,
                                 @Advice.FieldValue("fieldInfo") FieldInfo fieldInfo,
                                 @Advice.FieldValue("clazz") Class<?> clazz,
                                 @Advice.Argument(0) DefaultJSONParser parser, @Advice.Argument(1) Object object,
                                 @Advice.Argument(2) Type objectType, @Advice.Argument(3) Map<String, Object> fieldValues) throws Exception {

        if (oldFieldValueDeserilizer == null) {
            defaultFieldDeserializer.getFieldValueDeserilizer(parser.getConfig());
        }

        ObjectDeserializer fieldValueDeserilizer = oldFieldValueDeserilizer;
        Type fieldType = fieldInfo.fieldType;
        if (objectType instanceof ParameterizedType) {
            ParseContext objContext = parser.getContext();
            if (objContext != null) {
                objContext.type = objectType;
            }
            if (fieldType != objectType) {
                fieldType = FieldInfo.getFieldType(clazz, objectType, fieldType);
//                if (fieldValueDeserilizer instanceof JavaObjectDeserializer) {
//                    fieldValueDeserilizer = parser.getConfig().getDeserializer(fieldType);
//                }
            }
        }

        // ContextObjectDeserializer
        Object value;
        if (fieldValueDeserilizer instanceof JavaBeanDeserializer && fieldInfo.parserFeatures != 0) {
            JavaBeanDeserializer javaBeanDeser = (JavaBeanDeserializer) fieldValueDeserilizer;
            value = javaBeanDeser.deserialze(parser, fieldType, fieldInfo.name, fieldInfo.parserFeatures);
        } else {
            if ((fieldInfo.format != null || fieldInfo.parserFeatures != 0)
                    && fieldValueDeserilizer instanceof ContextObjectDeserializer) {
                value = ((ContextObjectDeserializer) fieldValueDeserilizer) //
                        .deserialze(parser,
                                fieldType,
                                fieldInfo.name,
                                fieldInfo.format,
                                fieldInfo.parserFeatures);
            } else {
                value = fieldValueDeserilizer.deserialze(parser, fieldType, fieldInfo.name);
            }
        }

        if (value instanceof byte[]
                && ("gzip".equals(fieldInfo.format) || "gzip,base64".equals(fieldInfo.format))) {
            byte[] bytes = (byte[]) value;
            GZIPInputStream gzipIn = null;
            try {
                gzipIn = new GZIPInputStream(new ByteArrayInputStream(bytes));

                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                for (; ; ) {
                    byte[] buf = new byte[1024];
                    int len = gzipIn.read(buf);
                    if (len == -1) {
                        break;
                    }
                    if (len > 0) {
                        byteOut.write(buf, 0, len);
                    }
                }
                value = byteOut.toByteArray();

            } catch (IOException ex) {
                throw new JSONException("unzip bytes error.", ex);
            }
        }

        if (parser.getResolveStatus() == DefaultJSONParser.NeedToResolve) {
            DefaultJSONParser.ResolveTask task = parser.getLastResolveTask();
            task.fieldDeserializer = defaultFieldDeserializer;
            task.ownerContext = parser.getContext();
            parser.setResolveStatus(DefaultJSONParser.NONE);
        } else {
            if (object == null) {
                fieldValues.put(fieldInfo.name, value);
            } else {
                defaultFieldDeserializer.setValue(object, value);
            }
        }
    }

}