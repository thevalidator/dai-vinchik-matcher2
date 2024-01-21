package ru.thevalidator.daivinchikmatcher2.util.data;

import com.google.gson.Gson;
import ru.thevalidator.daivinchikmatcher2.vk.dto.LongPollServerResponse;

public class SerializerUtil {
    private static final Gson mapper = new Gson();

    public static LongPollServerResponse readLongPollResponse(String response) {
        return mapper.fromJson(response, LongPollServerResponse.class);
    }

    public static String writeJson(Object o) {
        return mapper.toJson(o);
    }

    public static <T> T readJson(String json, Class<T> className) {
        return mapper.fromJson(json, className);
    }

}
