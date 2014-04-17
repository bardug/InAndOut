package com.berdugo.timeclock.backend;

/**
 * User: ami
 * Date: 19/05/13
 * Time: 13:32
 */
public class InOutPair {


    Integer id;
    Long inTimeMillis;
    Long outTimeMillis;


    public InOutPair(Long inTimeMillis, Long outTimeMillis, Integer id) {
        this.inTimeMillis = inTimeMillis;
        this.outTimeMillis = outTimeMillis;
        this.id = id;
    }

    public Long getInTimeMillis() {
        return inTimeMillis;
    }

    public void setInTimeMillis(Long inTimeMillis) {
        this.inTimeMillis = inTimeMillis;
    }

    public Long getOutTimeMillis() {
        return outTimeMillis;
    }

    public void setOutTimeMillis(Long outTimeMillis) {
        this.outTimeMillis = outTimeMillis;
    }

    public Integer getId() {
        return id;
    }
}
