package ru.thevalidator.daivinchikmatcher2.repository;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface UserTokenRepository {

    public ConcurrentLinkedQueue<String> getTokens();

}
