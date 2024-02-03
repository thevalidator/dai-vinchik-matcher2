package ru.thevalidator.daivinchikmatcher2.statisctic;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Statistic {

    private static final ConcurrentLinkedQueue<Statistic> globalStats = new ConcurrentLinkedQueue<>();

    private String name;
    private int likesSent;
    private int dislikesSent;
    private int matchesCount;
    private Instant startTime;
    private Instant finishTime;

    public int getLikesSent() {
        return likesSent;
    }

    public void increaseLikes() {
        likesSent++;
    }

    public int getDislikesSent() {
        return dislikesSent;
    }

    public void increaseDislikes() {
        dislikesSent++;
    }

    public int getMatchesCount() {
        return matchesCount;
    }

    public void increaseMatchesCount() {
        matchesCount++;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(Instant finishTime) {
        this.finishTime = finishTime;
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

    @Override
    public String toString() {
        return String.format(("[%s] [%s - %s | %s minute(s)] (L_%03d / D_%03d / M_%03d)"),
                this.getName(),
                LocalDateTime.ofInstant(startTime, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm")),
                LocalDateTime.ofInstant(finishTime, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm")),
                startTime.until(finishTime, ChronoUnit.MINUTES),
                this.getLikesSent(),
                this.getDislikesSent(),
                this.getMatchesCount());
    }

}
