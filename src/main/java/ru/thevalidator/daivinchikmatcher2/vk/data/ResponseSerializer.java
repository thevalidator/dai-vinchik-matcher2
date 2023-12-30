package ru.thevalidator.daivinchikmatcher2.vk.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.thevalidator.daivinchikmatcher2.vk.dto.LongPollServerResponse;

public class ResponseSerializer {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static LongPollServerResponse readLongPollResponse(String response) throws JsonProcessingException {
        return mapper.readValue(response, LongPollServerResponse.class);
    }
}
