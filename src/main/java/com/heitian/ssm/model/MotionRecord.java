package com.heitian.ssm.model;

/**
 * Created by buj on 2017/7/3.
 */
public class MotionRecord {
    private int UserId;
    private int time;
    private int state;

    @Override
    public String toString() {
        return "MotionRecord{" +
                "UserId=" + UserId +
                ", time=" + time +
                ", state=" + state +
                '}';
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}