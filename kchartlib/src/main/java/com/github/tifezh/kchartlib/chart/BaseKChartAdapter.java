package com.github.tifezh.kchartlib.chart;

import com.github.tifezh.kchartlib.chart.base.IAdapter;
import com.github.tifezh.kchartlib.chart.entity.ICandle;
import com.github.tifezh.kchartlib.chart.observer.KChartDataObservable;
import com.github.tifezh.kchartlib.chart.observer.KChartDataObserver;

/**
 * k线图的数据适配器
 * Created by tifezh on 2016/6/9.
 */

public abstract class BaseKChartAdapter implements IAdapter {

    private final KChartDataObservable mDataSetObservable = new KChartDataObservable();

    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }


    @Override
    public void registerDataSetObserver(KChartDataObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(KChartDataObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    @Override
    public void notifyItemInsertedToLast() {
        mDataSetObservable.notifyItemInsertedToLast();
    }

    @Override
    public void notifyLastItemChanged(float closePrice) {
        mDataSetObservable.notifyLastItemChanged(closePrice);
    }

    @Override
    public void notifyItemRangeInsertedToLast() {
        mDataSetObservable.notifyItemRangeInserted();

    }
}
