package ru.thevalidator.daivinchikmatcher2.util.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.thevalidator.daivinchikmatcher2.vk.dto.LongPollServerResponse;

public class SerializerUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static LongPollServerResponse readLongPollResponse(String response) throws JsonProcessingException {
        return mapper.readValue(response, LongPollServerResponse.class);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

}
