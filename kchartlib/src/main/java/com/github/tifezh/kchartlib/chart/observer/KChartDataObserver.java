package com.github.tifezh.kchartlib.chart.observer;

public abstract class KChartDataObserver {
    /**
     * 刷新所有
     */
    public void onChanged() {
        // Do nothing
    }

    /**
     * 最后一个数据修改
     */
    public void onLastItemChanged(float closePrice) {
        // do nothing
    }

    /**
     * 最后插入一个数据
     */
    public void onItemInsertedToLast() {
        // do nothing
    }

    /**
     * 最后插入一堆数据
     */
    public void onItemRangeInsertedToLast() {
        // do nothing
    }

}
