package ru.thevalidator.daivinchikmatcher2.vk.dto;

import com.vk.api.sdk.objects.base.Error;

public class ErrorResponse {

    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "{\"error\":" +
                error +
                '}';
    }

}
