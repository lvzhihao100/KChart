package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.base.IChartDraw;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.entity.IBOLL;
import com.github.tifezh.kchartlib.chart.entity.ICandle;
import com.github.tifezh.kchartlib.chart.entity.IKLine;
import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;
import com.github.tifezh.kchartlib.chart.formatter.TimeFormatter;
import com.github.tifezh.kchartlib.chart.formatter.ValueFormatter;
import com.github.tifezh.kchartlib.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 主图的实现类
 * Created by tifezh on 2016/6/14.
 */

public class MainDraw implements IChartDraw<ICandle> {

    private float mCandleWidth = 0;
    private float mCandleLineWidth = 0;
    private int startPadding = 10;
    private int spacePadding = 10;
    private Paint mBluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma20Paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mUpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorBackgroundStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context mContext;

    private boolean mCandleSolid = true;
    private BaseKChartView view;
    private final Context context;
    private int type = 1;//0 没有线,1 MA,2 Boll

    public MainDraw(BaseKChartView view) {
        this.view = view;
        context = view.getContext();
        mContext = context;
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mBluePaint.setColor(ContextCompat.getColor(context, R.color.blue_5d));
        mBluePaint.setStyle(Paint.Style.STROKE);
        mBluePaint.setStrokeWidth(view.dp2px(1));
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.chart_green));
        mLinePaint.setColor(ContextCompat.getColor(context, R.color.blue_5d));
        mWhitePaint.setColor(ContextCompat.getColor(context, R.color.chart_white));
        mSelectorBackgroundStrokePaint.setColor(ContextCompat.getColor(context, R.color.chart_white));
        mSelectorBackgroundStrokePaint.setStyle(Paint.Style.STROKE);
        mSelectorBackgroundStrokePaint.setStrokeWidth(view.dp2px(1));
    }

    public void setShowMa() {
        type = 1;
    }

    public void setShowBoll() {
        type = 2;
    }

    public void setShowNone() {
        type = 0;
    }

    public void setUpColor(@ColorRes int color) {
        mRedPaint.setColor(ContextCompat.getColor(context, color));
    }

    public void setDownColor(@ColorRes int color) {
        mGreenPaint.setColor(ContextCompat.getColor(context, color));
    }

    public void setMinuteLineColor(int color) {
        mLinePaint.setColor(color);
    }

    public void setMinuteLineWidth(int width) {
        mLinePaint.setStrokeWidth(width);
    }

    @Override
    public void drawTranslated(@Nullable ICandle lastPoint, @NonNull ICandle curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKChartView view, int position) {
        if (view.isDrawMinuteStyle()) {//绘制线
            view.drawMainLine(canvas, mLinePaint, lastX, lastPoint.getClosePrice(), curX, curPoint.getClosePrice());
        } else { //绘制蜡烛图
            drawCandle(view, canvas, curX, curPoint.getHighPrice(), curPoint.getLowPrice(), curPoint.getOpenPrice(), curPoint.getClosePrice());
            if (type == 1) {
                //画ma5
                if (lastPoint.getMA5Price() != 0) {
                    view.drawMainLine(canvas, ma5Paint, lastX, lastPoint.getMA5Price(), curX, curPoint.getMA5Price());
                }
                //画ma10
                if (lastPoint.getMA10Price() != 0) {
                    view.drawMainLine(canvas, ma10Paint, lastX, lastPoint.getMA10Price(), curX, curPoint.getMA10Price());
                }
                //画ma20
                if (lastPoint.getMA20Price() != 0) {
                    view.drawMainLine(canvas, ma20Paint, lastX, lastPoint.getMA20Price(), curX, curPoint.getMA20Price());
                }
            } else if (type == 2) {
                //画mb
                if (lastPoint.getMb() != 0) {
                    view.drawMainLine(canvas, mMbPaint, lastX, lastPoint.getMb(), curX, curPoint.getMb());
                }
                //画ub
                if (lastPoint.getUp() != 0) {
                    view.drawMainLine(canvas, mUpPaint, lastX, lastPoint.getUp(), curX, curPoint.getUp());
                }
                //画db
                if (lastPoint.getDn() != 0) {
                    view.drawMainLine(canvas, mDnPaint, lastX, lastPoint.getDn(), curX, curPoint.getDn());
                }
            }
        }

    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKChartView view, int position, float x, float y) {
        if (type == 1) {
            ICandle point = (IKLine) view.getItem(position);
            String text = "MA5:" + view.formatValue(point.getMA5Price()) + " ";
            x += startPadding;
            canvas.drawText(text, x, y, ma5Paint);
            x += ma5Paint.measureText(text) + spacePadding;
            text = "MA10:" + view.formatValue(point.getMA10Price()) + " ";
            canvas.drawText(text, x, y, ma10Paint);
            x += ma10Paint.measureText(text) + spacePadding;
            text = "MA20:" + view.formatValue(point.getMA20Price()) + " ";
            canvas.drawText(text, x, y, ma20Paint);
        } else if (type == 2) {
            IBOLL point = (IBOLL) view.getItem(position);
            String text = "BOLL:" + view.formatValue(point.getMb()) + " ";
            x += startPadding;
            canvas.drawText(text, x, y, mMbPaint);
            x += mMbPaint.measureText(text) + spacePadding;
            text = "UB:" + view.formatValue(point.getUp()) + " ";
            canvas.drawText(text, x, y, mUpPaint);
            x += mUpPaint.measureText(text) + spacePadding;
            text = "LB:" + view.formatValue(point.getDn()) + " ";
            canvas.drawText(text, x, y, mDnPaint);
        }
        if (view.isLongPress() && !view.isDrawMinuteStyle()) {
            drawSelector(view, canvas);
        }
    }

    @Override
    public float getMaxValue(ICandle point) {
        if (view.isDrawMinuteStyle()) {
            return point.getClosePrice();
        } else {
            return point.getHighPrice();
        }
    }

    @Override
    public float getMinValue(ICandle point) {
        if (view.isDrawMinuteStyle()) {
            return point.getClosePrice();
        } else {
            return point.getLowPrice();
        }
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return view.getValueFormatter();
    }

    /**
     * 画Candle
     *
     * @param canvas
     * @param x      x轴坐标
     * @param high   最高价
     * @param low    最低价
     * @param open   开盘价
     * @param close  收盘价
     */
    private void drawCandle(BaseKChartView view, Canvas canvas, float x, float high, float low, float open, float close) {
        high = view.getMainY(high);
        low = view.getMainY(low);
        open = view.getMainY(open);
        close = view.getMainY(close);
        float r = mCandleWidth / 2;
        float lineR = mCandleLineWidth / 2;
        if (open > close) {
            //实心
            if (mCandleSolid) {
                canvas.drawRect(x - r, close, x + r, open, mRedPaint);
                canvas.drawRect(x - lineR, high, x + lineR, low, mRedPaint);
            } else {
                mRedPaint.setStrokeWidth(mCandleLineWidth);
                canvas.drawLine(x, high, x, close, mRedPaint);
                canvas.drawLine(x, open, x, low, mRedPaint);
                canvas.drawLine(x - r + lineR, open, x - r + lineR, close, mRedPaint);
                canvas.drawLine(x + r - lineR, open, x + r - lineR, close, mRedPaint);
                mRedPaint.setStrokeWidth(mCandleLineWidth * view.getScaleX());
                canvas.drawLine(x - r, open, x + r, open, mRedPaint);
                canvas.drawLine(x - r, close, x + r, close, mRedPaint);
            }

        } else if (open < close) {
            canvas.drawRect(x - r, open, x + r, close, mGreenPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, mGreenPaint);
        } else {
            canvas.drawRect(x - r, open, x + r, close + 1, mRedPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, mRedPaint);
        }
//        canvas.drawText(iCandle.getHighPrice() + "", x, high, mWhitePaint);
    }

    /**
     * draw选择器
     *
     * @param view
     * @param canvas
     */
    private void drawSelector(BaseKChartView view, Canvas canvas) {
        Paint.FontMetrics metrics = mSelectorTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        int index = view.getSelectedIndex();
        float padding = ViewUtil.Dp2Px(mContext, 5);
        float margin = ViewUtil.Dp2Px(mContext, 5);
        float leftTableWidth = view.getLeftTitleMargin();
        float width = 0;
        float left;
        float top = margin + view.getTopPadding();

        ICandle point = (ICandle) view.getItem(index);
        List<String> strings = new ArrayList<>();
        strings.add(mContext.getString(R.string.kchart_date));
        strings.add(mContext.getString(R.string.kchart_time));
        strings.add(mContext.getString(R.string.kchart_high));
        strings.add(mContext.getString(R.string.kchart_low));
        strings.add(mContext.getString(R.string.kchart_open));
        strings.add(mContext.getString(R.string.kchart_close));
        List<String> stringValue = new ArrayList<>();
        stringValue.add(new DateFormatter().format(view.getAdapter().getDate(index)));
        stringValue.add(new TimeFormatter().format(view.getAdapter().getDate(index)));
        stringValue.add("" + getValueFormatter().format(point.getHighPrice()));
        stringValue.add("" + getValueFormatter().format(point.getLowPrice()));
        stringValue.add("" + getValueFormatter().format(point.getOpenPrice()));
        stringValue.add("" + getValueFormatter().format(point.getClosePrice()));
        float height = padding * 8 + textHeight * strings.size();


        for (int i = 0; i < strings.size(); i++) {
            width = Math.max(width, mSelectorTextPaint.measureText(strings.get(i) + stringValue.get(i) + "    "));
        }
        width += padding * 2;

        float x = view.translateXtoX(view.getX(index));
        if (x > view.getChartWidth() / 2) {
            left = leftTableWidth;
        } else {
            left = view.getChartWidth() - width - margin;
        }

        RectF r = new RectF(left, top, left + width, top + height);
        canvas.drawRoundRect(r, padding, padding, mSelectorBackgroundPaint);
        canvas.drawRoundRect(r, padding, padding, mSelectorBackgroundStrokePaint);
        float y = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2;

        for (String s : strings) {
            mSelectorTextPaint.setTextAlign(Paint.Align.LEFT);

            canvas.drawText(s, left + padding, y, mSelectorTextPaint);
            y += textHeight + padding;
        }
        y = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2;

        for (String s : stringValue) {
            mSelectorTextPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(s, left + width - padding, y, mSelectorTextPaint);
            y += textHeight + padding;
        }

    }

    /**
     * 设置蜡烛宽度
     *
     * @param candleWidth
     */
    public void setCandleWidth(float candleWidth) {
        mCandleWidth = candleWidth;
    }

    /**
     * 设置蜡烛线宽度
     *
     * @param candleLineWidth
     */
    public void setCandleLineWidth(float candleLineWidth) {
        mCandleLineWidth = candleLineWidth;
    }

    /**
     * 设置ma5颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        this.ma5Paint.setColor(color);
    }

    /**
     * 设置ma10颜色
     *
     * @param color
     */
    public void setMa10Color(int color) {
        this.ma10Paint.setColor(color);
    }

    /**
     * 设置ma20颜色
     *
     * @param color
     */
    public void setMa20Color(int color) {
        this.ma20Paint.setColor(color);
    }

    /**
     * 设置选择器文字颜色
     *
     * @param color
     */
    public void setSelectorTextColor(int color) {
        mSelectorTextPaint.setColor(color);
    }


    /**
     * 设置选择器文字大小
     *
     * @param textSize
     */
    public void setSelectorTextSize(float textSize) {
        mSelectorTextPaint.setTextSize(textSize);
    }

    /**
     * 设置选择器背景
     *
     * @param color
     */
    public void setSelectorBackgroundColor(int color) {
        mSelectorBackgroundPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        ma20Paint.setStrokeWidth(width);
        ma10Paint.setStrokeWidth(width);
        ma5Paint.setStrokeWidth(width);
        mUpPaint.setStrokeWidth(width);
        mDnPaint.setStrokeWidth(width);
        mMbPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        ma20Paint.setTextSize(textSize);
        ma10Paint.setTextSize(textSize);
        ma5Paint.setTextSize(textSize);
        mUpPaint.setTextSize(textSize);
        mDnPaint.setTextSize(textSize);
        mMbPaint.setTextSize(textSize);
    }

    /**
     * 蜡烛是否实心
     */
    public void setCandleSolid(boolean candleSolid) {
        mCandleSolid = candleSolid;
    }

    public void setMaStartPadding(int startPadding) {
        this.startPadding = startPadding;
    }

    public void setMaSpacePadding(int spacePadding) {
        this.spacePadding = spacePadding;
    }

    /**
     * 设置up颜色
     */
    public void setBollUpColor(int color) {
        mUpPaint.setColor(color);
    }

    /**
     * 设置mb颜色
     *
     * @param color
     */
    public void setBollMbColor(int color) {
        mMbPaint.setColor(color);
    }

    /**
     * 设置dn颜色
     */
    public void setBollDnColor(int color) {
        mDnPaint.setColor(color);
    }
}
