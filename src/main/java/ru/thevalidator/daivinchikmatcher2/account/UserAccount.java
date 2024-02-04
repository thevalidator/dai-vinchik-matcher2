package ru.thevalidator.daivinchikmatcher2.account;

public class UserAccount {
    private String name;
    private String token;

    public UserAccount(String name, String token) {
        this.name = name;
        this.token = token;
        //TODO: move to repository or model
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
