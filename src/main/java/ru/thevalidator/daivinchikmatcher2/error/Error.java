package ru.thevalidator.daivinchikmatcher2.error;

public enum Error {
    CAPTCHA(14),
    TOO_MANY(6),
    UPLOAD_ERROR(22),
    BANNED_USER(37),
    CANT_SEND_BLACKLISTED(900),
    CANT_SEND_NO_PERMISSION(901),
    CANT_SEND_PRIVACY_RESTRICTION(902),
    CANT_SEND_TOO_BIG(910),
    CANT_SEND_TOO_LONG(914),
    NO_ACCESS_TO_CHAT(917),
    CHAT_DOESNT_EXISTS(927),
    CONTACT_NOT_FOUND(936),
    RECAPTCHA_NEEDED(3300);

    private final int errorCode;

    Error(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
