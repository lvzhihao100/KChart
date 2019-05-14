package com.github.tifezh.kchartlib.chart.entity;

/**
 * 布林线指标接口
 * @see <a href="https://baike.baidu.com/item/%E5%B8%83%E6%9E%97%E7%BA%BF%E6%8C%87%E6%A0%87/3325894"/>相关说明</a>
 * Created by tifezh on 2016/6/10.
 */

public interface IBOLL extends ILine{

    /**
     * 上轨线
     */
    float getUp();
    void setUp(float up);

    /**
     * 中轨线
     */
    float getMb();
    void setMb(float mb);


    /**
     * 下轨线
     */
    float getDn();
    void setDn(float dn);

    /**
     * 五(月，日，时，分，5分等)均价
     */
    float getMA5Price();

    void setMA5Price(float ma5Price);

    /**
     * 十(月，日，时，分，5分等)均价
     */
    float getMA10Price();

    void setMA10Price(float ma10Price);

    /**
     * 二十(月，日，时，分，5分等)均价
     */
    float getMA20Price();

    void setMA20Price(float ma20Price);

}
