package com.example.test_session.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtil {
    static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
    public static final TypeReference<Map<String, Object>> TYPE_MAP_STRING_OBJ = new TypeReference<Map<String, Object>>() {
    };
    public static final TypeReference<List<Object>> TYPE_LIST_OBJ = new TypeReference<List<Object>>() {
    };
    public static final TypeReference<Set<String>> TYPE_SET_STRING = new TypeReference<Set<String>>() {
    };
    public static final TypeReference<List<Map<String, Object>>> TYPE_LIST_MAP_STRING_OBJ = new TypeReference<List<Map<String, Object>>>() {
    };

    public JsonUtil() {
    }

    public static String removeUTF8BOM(String json) {
        String UTF8_BOM = "\ufeff";
        if (json.startsWith("\ufeff")) {
            json = json.substring(1);
        }

        return json;
    }

    public static Map<String, Object> toMap(String json) {
        if (json == null) {
            return Maps.newHashMap();
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                return (Map)mapper.readValue(json, TYPE_MAP_STRING_OBJ);
            } catch (NullPointerException | IOException var3) {
                log.error("Exception json : {}", json);
                log.error("Exception", var3);
                return null;
            }
        }
    }

    public static List<Object> toList(String json) {
        if (json == null) {
            return Lists.newArrayList();
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                return (List)mapper.readValue(json, TYPE_LIST_OBJ);
            } catch (NullPointerException | IOException var3) {
                log.error("Exception json : {}", json);
                log.error("Exception", var3);
                return null;
            }
        }
    }

    public static <T> T toClass(String json, Class<T> toValueType) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return mapper.readValue(json, toValueType);
        } catch (NullPointerException | IOException var4) {
            log.error("Exception json : {}", json);
            log.error("Exception", var4);
            return null;
        }
    }

    public static Map<String, Object> toMap(byte[] json) {
        if (json == null) {
            return Maps.newHashMap();
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                return (Map)mapper.readValue(json, TYPE_MAP_STRING_OBJ);
            } catch (NullPointerException | IOException var3) {
                log.error("Exception json : {}", new String(json));
                log.error("Exception", var3);
                return null;
            }
        }
    }

    public static Map<String, String> toStringMap(byte[] json) {
        if (json == null) {
            return Maps.newHashMap();
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                return (Map)mapper.readValue(json, TYPE_MAP_STRING_OBJ);
            } catch (NullPointerException | IOException var3) {
                log.error("Exception json : {}", new String(json));
                log.error("Exception", var3);
                return null;
            }
        }
    }

    public static List<Object> toList(byte[] json) {
        if (json == null) {
            return Lists.newArrayList();
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            try {
                return (List)mapper.readValue(json, TYPE_LIST_OBJ);
            } catch (NullPointerException | IOException var3) {
                log.error("Exception json : {}", new String(json));
                log.error("Exception", var3);
                return null;
            }
        }
    }

    public static <T> T toClass(byte[] json, Class<T> toValueType) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return mapper.readValue(json, toValueType);
        } catch (NullPointerException | IOException var4) {
            log.error("Exception json : {}", new String(json));
            log.error("Exception", var4);
            return null;
        }
    }

    public static String toJson(Object object) {
        if (object == null) {
            log.error("object is null");
            return null;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            try {
                return mapper.writeValueAsString(object);
            } catch (NullPointerException | IOException var3) {
                log.error("Exception", var3);
                return null;
            }
        }
    }
}
