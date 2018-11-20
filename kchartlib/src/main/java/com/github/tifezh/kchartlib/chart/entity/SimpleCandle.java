package com.github.tifezh.kchartlib.chart.entity;

/**
 * 蜡烛图实体接口
 * Created by tifezh on 2016/6/9.
 */

public class SimpleCandle implements ICandle {


    float closePrice;

    @Override
    public void setClosePrice(float closePrice) {
        this.closePrice = closePrice;
    }

    @Override
    public float getOpenPrice() {
        return 0;
    }

    @Override
    public float getHighPrice() {
        return 0;
    }

    @Override
    public float getLowPrice() {
        return 0;
    }

    @Override
    public float getClosePrice() {
        return closePrice;
    }


    @Override
    public float getMA5Price() {
        return 0;
    }

    @Override
    public float getMA10Price() {
        return 0;
    }

    @Override
    public float getMA20Price() {
        return 0;
    }

    @Override
    public long getDatetime() {
        return 0;
    }

    @Override
    public float getUp() {
        return 0;
    }

    @Override
    public float getMb() {
        return 0;
    }

    @Override
    public float getDn() {
        return 0;
    }
}
