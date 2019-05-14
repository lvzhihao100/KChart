package com.github.tifezh.kchartlib.chart.entity;

/**
 * 威廉指标接口
 *
 * @see <a href="https://baike.baidu.com/item/wR%E6%8C%87%E6%A0%87/9464148?fr=aladdin"/>相关说明</a>
 * Created by david on 2018/12/1.
 */

public interface IWR extends ILine {

    /**
     * 14日威廉指标
     */
    float getWr();
    void setWr(float wr);

}
