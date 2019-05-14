package com.github.tifezh.kchartlib.chart.entity;

/**
 * 蜡烛图实体接口
 * Created by tifezh on 2016/6/9.
 */

public interface ICandle extends IBOLL {


    void setClosePrice(float closePrice);

    /**
     * 时间
     *
     * @return
     */
    long getDatetime();
}
