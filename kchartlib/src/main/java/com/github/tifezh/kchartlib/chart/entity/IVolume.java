package com.github.tifezh.kchartlib.chart.entity;

/**
 * 成交量接口
 * Created by hjm on 2017/11/14 17:46.
 */

public interface IVolume extends ILine{



    /**
     * 五(月，日，时，分，5分等)均量
     */
    float getMA5Volume();
    void setMA5Volume(float ma5Volume);

    /**
     * 十(月，日，时，分，5分等)均量
     */
    float getMA10Volume();
    void setMA10Volume(float ma10Volume);

}
