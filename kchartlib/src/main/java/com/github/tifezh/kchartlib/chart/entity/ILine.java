package com.github.tifezh.kchartlib.chart.entity;

public interface ILine {
    /**
     * 开盘价
     */
    float getOpenPrice();

    /**
     * 最高价
     */
    float getHighPrice();

    /**
     * 最低价
     */
    float getLowPrice();

    /**
     * 收盘价
     */
    float getClosePrice();

    /**
     * 成交量
     */
    float getVolume();
}
