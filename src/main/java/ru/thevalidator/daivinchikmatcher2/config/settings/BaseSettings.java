package ru.thevalidator.daivinchikmatcher2.config.settings;

public interface BaseSettings {
    public void setLongPollWaitTime(Integer delayInSeconds);

    public Integer getLongPollWaitTime();
    public Integer getLongPollVersion();

    public Long getDaiVinchickPeerId();
}
