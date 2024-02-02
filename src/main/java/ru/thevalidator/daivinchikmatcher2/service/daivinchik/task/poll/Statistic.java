package ru.thevalidator.daivinchikmatcher2.service.daivinchik.task.poll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Statistic {

    private static final ConcurrentLinkedQueue<Statistic> globalStats = new ConcurrentLinkedQueue<>();

    private String name;
    private int likes;
    private int dislikes;

    public int getLikes() {
        return likes;
    }

    public void increaseLikes() {
        likes++;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void increaseDislikes() {
        dislikes++;
    }

    public static void addStatisticToGlobal(Statistic s) {
        globalStats.offer(s);
    }

    public static List<Statistic> getGlobalStatistic() {
        List<Statistic> stats = new ArrayList<>(globalStats);
        return Collections.unmodifiableList(stats);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
