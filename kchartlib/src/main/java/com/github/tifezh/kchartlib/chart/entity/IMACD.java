package com.github.tifezh.kchartlib.chart.entity;

/**
 * MACD指标(指数平滑移动平均线)接口
 * @see <a href="https://baike.baidu.com/item/MACD指标"/>相关说明</a>
 * Created by tifezh on 2016/6/10.
 */

public interface IMACD extends ILine{


    /**
     * DEA值
     */
    float getDea();
    void setDea(float dea);

    /**
     * DIF值
     */
    float getDif();
    void setDif(float dif);


    /**
     * MACD值
     */
    float getMacd();
    void setMacd(float macd);


}
