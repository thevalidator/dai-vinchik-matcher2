package ru.thevalidator.daivinchikmatcher2.config.settings;

import ru.thevalidator.daivinchikmatcher2.config.settings.options.BaseSettings;
import ru.thevalidator.daivinchikmatcher2.config.settings.options.UserSettings;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Settings implements BaseSettings, UserSettings {
    INSTANCE;
    private static final String SETTINGS_FOLDER_PATH = "data" + File.separator + "config" + File.separator;
    private static final String BASE_SETTINGS_FILE_PATH = SETTINGS_FOLDER_PATH + "base_settings";
    private static final String USER_SETTINGS_FILE_PATH = SETTINGS_FOLDER_PATH + "user_settings";

    private final Map<Parameter, Object> map = new ConcurrentHashMap<>();
    Settings() {
        initSettings();
    }

    private void initSettings() {
        setBaseSettings();
    }

    private void setBaseSettings() {
        map.put(Parameter.LONG_POLL_WAIT_TIME, 25);
        map.put(Parameter.LONG_POLL_VERSION, 3);
        map.put(Parameter.DAI_VINCHIK_PEER, -91050183L);
    }

    @Override
    public void setLongPollWaitTime(Integer delayInSeconds) {
        map.put(Parameter.LONG_POLL_WAIT_TIME, delayInSeconds);
    }

    @Override
    public Integer getLongPollWaitTime() {
        return (Integer) map.get(Parameter.LONG_POLL_WAIT_TIME);
    }

    @Override
    public Integer getLongPollVersion() {
        return (Integer) map.get(Parameter.LONG_POLL_VERSION);
    }

    @Override
    public Long getDaiVinchickPeerId() {
        return (Long) map.get(Parameter.DAI_VINCHIK_PEER);
    }

    public enum Parameter {
        LONG_POLL_WAIT_TIME,
        LONG_POLL_VERSION,
        DAI_VINCHIK_PEER,
        AGE_FILTER,
        CITY_FILTER,
        TEXT_FILTER,
        SOUND_ALARM,
        LIKE_ON_LIKE,
        EXPERIMENTAL_HANDLER,
        HOURS_TO_SLEEP,
        DEBUG_MODE,
        BASE_DELAY,
        RANDOM_DELAY,
        REPLY_CHECK_PERIOD

    }
}
