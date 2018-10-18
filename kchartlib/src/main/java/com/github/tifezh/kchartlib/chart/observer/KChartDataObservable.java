package com.github.tifezh.kchartlib.chart.observer;


import android.database.Observable;

import com.github.tifezh.kchartlib.chart.entity.ICandle;

public class KChartDataObservable extends Observable<KChartDataObserver> {
    public boolean hasObservers() {
        return !mObservers.isEmpty();
    }

    public void notifyChanged() {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onChanged();
        }
    }

    public void notifyLastItemChanged(float closePrice) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onLastItemChanged(closePrice);
        }
    }
    public void notifyItemInsertedToLast() {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onItemInsertedToLast();
        }
    }
    public void notifyItemRangeInserted() {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onItemInsertedToLast();
        }
    }

}
