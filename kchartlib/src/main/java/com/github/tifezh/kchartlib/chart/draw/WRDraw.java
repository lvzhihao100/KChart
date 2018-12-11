package com.github.tifezh.kchartlib.chart.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.base.IChartDraw;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.entity.IWR;
import com.github.tifezh.kchartlib.chart.formatter.ValueFormatter;

/**
 * WR实现类
 * Created by david on 2018/12/01.
 */

public class WRDraw implements IChartDraw<IWR> {

    private Paint mWrPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WRDraw(BaseKChartView view) {

    }

    @Override
    public void drawTranslated(@Nullable IWR lastPoint, @NonNull IWR curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKChartView view, int position) {
        view.drawChildLine(canvas, mWrPaint, lastX, lastPoint.getWr(), curX, curPoint.getWr());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKChartView view, int position, float x, float y) {
        String text = "";
        IWR point = (IWR) view.getItem(position);
        text = "WR(9):" + view.formatValue(point.getWr()) + " ";
        canvas.drawText(text, x, y, mWrPaint);
    }

    @Override
    public float getMaxValue(IWR point) {
        if (Float.isNaN(point.getWr())) {
            return point.getWr();
        }
        return point.getWr();
    }

    @Override
    public float getMinValue(IWR point) {
        if (Float.isNaN(point.getWr())) {
            return point.getWr();
        }
        return point.getWr();
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }
    /**
     * 设置wr颜色
     */
    public void seWrColor(int color) {
        mWrPaint.setColor(color);
    }
    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mWrPaint.setStrokeWidth(width);
    }
    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mWrPaint.setTextSize(textSize);
    }
    /**
     * 设置文字颜色
     */
    public void setTextColor(int color) {
        mWrPaint.setColor(color);
    }
}
