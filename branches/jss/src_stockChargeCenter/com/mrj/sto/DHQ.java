package com.mrj.sto;

import java.util.*;

public class DHQ {

    private Date date;
    private float lowestPrice;
    private float highPrice;
    private float beginPrice;
    private float finalPrice;
    private long chargeVolume;
    private float chargeMoney;
    private float price_180day;
    private float price_60day;
    private float price_30day;
    private float price_20day;
    private float price_10day;
    private float price_5day;
    private long volume_year;
    private long volume_halfyear;
    private long volume_threemonth;
    private long volume_month;
    private long volume_20day;
    private long volume_10day;
    private long volume_5day;

    public float getPrice_180day() {
        return price_180day;
    }

    public void setPrice_180day(float price_180day) {
        this.price_180day = price_180day;
    }

    public float getPrice_30day() {
        return price_30day;
    }

    public void setPrice_30day(float price_30day) {
        this.price_30day = price_30day;
    }

    public float getPrice_60day() {
        return price_60day;
    }

    public void setPrice_60day(float price_60day) {
        this.price_60day = price_60day;
    }

    public float getBeginPrice() {
        return beginPrice;
    }

    public void setBeginPrice(float beginPrice) {
        this.beginPrice = beginPrice;
    }

    public long getChargeVolume() {
        return chargeVolume;
    }

    public void setChargeVolume(long chargeVolume) {
        this.chargeVolume = chargeVolume;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public float getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(float highPrice) {
        this.highPrice = highPrice;
    }

    public float getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(float lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public float getPrice_10day() {
        return price_10day;
    }

    public void setPrice_10day(float price_10day) {
        this.price_10day = price_10day;
    }

    public float getPrice_20day() {
        return price_20day;
    }

    public void setPrice_20day(float price_20day) {
        this.price_20day = price_20day;
    }

    public float getPrice_5day() {
        return price_5day;
    }

    public void setPrice_5day(float price_5day) {
        this.price_5day = price_5day;
    }

    public long getVolume_10day() {
        return volume_10day;
    }

    public void setVolume_10day(long volume_10day) {
        this.volume_10day = volume_10day;
    }

    public long getVolume_20day() {
        return volume_20day;
    }

    public void setVolume_20day(long volume_20day) {
        this.volume_20day = volume_20day;
    }

    public long getVolume_5day() {
        return volume_5day;
    }

    public void setVolume_5day(long volume_5day) {
        this.volume_5day = volume_5day;
    }

    public long getVolume_halfyear() {
        return volume_halfyear;
    }

    public void setVolume_halfyear(long volume_halfyear) {
        this.volume_halfyear = volume_halfyear;
    }

    public long getVolume_month() {
        return volume_month;
    }

    public void setVolume_month(long volume_month) {
        this.volume_month = volume_month;
    }

    public long getVolume_threemonth() {
        return volume_threemonth;
    }

    public void setVolume_threemonth(long volume_threemonth) {
        this.volume_threemonth = volume_threemonth;
    }

    public long getVolume_year() {
        return volume_year;
    }

    public void setVolume_year(long volume_year) {
        this.volume_year = volume_year;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(float chargeMoney) {
        this.chargeMoney = chargeMoney;
    }
}
