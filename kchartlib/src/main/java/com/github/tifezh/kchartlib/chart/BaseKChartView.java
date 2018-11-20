package com.github.tifezh.kchartlib.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.base.IAdapter;
import com.github.tifezh.kchartlib.chart.base.IChartDraw;
import com.github.tifezh.kchartlib.chart.base.IDateTimeFormatter;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.draw.MainDraw;
import com.github.tifezh.kchartlib.chart.draw.VolumeDraw;
import com.github.tifezh.kchartlib.chart.entity.DrawBean;
import com.github.tifezh.kchartlib.chart.entity.ICandle;
import com.github.tifezh.kchartlib.chart.entity.IKLine;
import com.github.tifezh.kchartlib.chart.entity.SimpleCandle;
import com.github.tifezh.kchartlib.chart.formatter.TimeFormatter;
import com.github.tifezh.kchartlib.chart.formatter.ValueFormatter;
import com.github.tifezh.kchartlib.chart.observer.KChartDataObserver;
import com.github.tifezh.kchartlib.utils.NumberUtil;
import com.github.tifezh.kchartlib.utils.ViewUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * k线图
 * Created by tian on 2016/5/3.
 */
public abstract class BaseKChartView extends ScrollAndScaleView {
    private int mChildDrawPosition = 0;

    private float mTranslateX = 0;

    private int mWidth = 0;

    private int mTopPadding;

    private int mBottomPadding;

    private float mMainScaleY = 1;
    private float mMainMinuteScaleY = 1;

    private float mChildScaleY = 1;

    private float mDataLen = 0;

    private float mMainMaxValue = Float.MAX_VALUE;

    private float mMainMinValue = 0;
    private float mMainMaxFinalValue;

    private float mMainMinFinalValue;

    private float mChildMaxValue = Float.MAX_VALUE;

    private float mChildMinValue = 0;

    private int mStartIndex = 0;

    private int mStopIndex = 0;

    private float mPointWidth = 6;

    private int mGridRows = 4;

    private int mGridColumns = 4;

    private Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRedPaintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mShaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextMaxMinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mSelectedIndex;

    private IChartDraw mMainDraw;

    private IAdapter mAdapter;

    private KChartDataObserver mDataSetObserver = new KChartDataObserver() {
        @Override
        public void onChanged() {
            mItemCount = getAdapter().getCount();
            mLastItemCount = mItemCount;
            notifyChanged();
        }

        @Override
        public void onItemInsertedToLast() {
            mLastItemCount = mItemCount;
            mItemCount = getAdapter().getCount();

            notifyItemInsert();
        }

        @Override
        public void onLastItemChanged(float closePrice) {
            notifyItemChange(closePrice);
        }

        @Override
        public void onItemRangeInsertedToLast() {
            mLastItemCount = mItemCount;
            mItemCount = getAdapter().getCount();
            notifyItemInsert();
        }
    };
    //当前点的个数
    private int mItemCount;
    private int mLastItemCount;
    private IChartDraw mChildDraw;
    private List<IChartDraw> mChildDraws = new ArrayList<>();

    private IValueFormatter mValueFormatter;
    private IDateTimeFormatter mDateTimeFormatter;

    protected KChartTabView mKChartTabView;


    private long mAnimationDuration = 500;

    private float mOverScrollRange = 0;

    private OnSelectedChangedListener mOnSelectedChangedListener = null;

    private Rect mMainRect;

    private Rect mTabRect;

    private Rect mChildRect;

    private float mLineWidth;
    /**
     * 左边 表 文本的 padding
     */
    private float mPadingValue = 0;
    /**
     * 横向坐标标题显示位置，相对于最左边线
     */
    private float mLeftTitleMargin = 0;
    private float mDefaultGridLineWidth;
    /**
     * 是否以线方式绘制，否则 绘 蜡烛图 样式
     */
    private boolean mDrawMinuteStyle = false;
    /**
     * 是否绘制分割线
     */
    private boolean mDrawGirdLine = true;
    private boolean mIsOutward;
    /**
     * 分时图 k 线 填充路径
     */
    protected Path cubicPath = new Path();
    /**
     * 分时图 k 线 填充绘笔
     */
    protected Paint mDrawFillPaint = new Paint();

    /**
     * 是否绘制tabView
     */
    private boolean mDrawTabView = true;
    private float animX;
    private float moveText;
    private float animY;
    private Paint mWhiteBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mWhiteDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private NumberFormat instance;
    private float mLastMainMaxValue;
    private float mChangeMainMaxValue;
    private float mLastMainMinValue;
    private float mLastChildMaxValue;
    private float mLastChildMinValue;
    private int mLastStartIndex;
    private int mLastStopIndex;
    private Path shaderPath;
    private DrawBean drawBean;
    //首次无动画
    private boolean isAnim = false;
    private float mChangeMainMinValue;
    private float mChangeNextMainMaxValue;
    private float mChangeNextMainMinValue;
    private ValueAnimator valueAnimator;
    private float perMax;
    private float perMin;
    private boolean isFirst = true;
    private ICandle lastItem;
    private ValueAnimator changePriceAnimator;
    private boolean isDrawDown = true;

    public BaseKChartView(Context context) {
        super(context);
        init();
    }

    public BaseKChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setDrawDown(boolean drawDown) {
        isDrawDown = drawDown;
        invalidate();
    }

    private void init() {
        drawBean = new DrawBean();
        setWillNotDraw(false);
        instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(4);
        instance.setMinimumFractionDigits(4);
        mDrawFillPaint.setStyle(Paint.Style.STROKE);
        mDrawFillPaint.setColor(Color.parseColor("#665D91E7"));
        mWhitePaint.setColor(Color.WHITE);
        mRedPaintRed.setColor(Color.RED);
        mTextMaxMinPaint.setColor(Color.WHITE);
        mTextMaxMinPaint.setTextSize(sp2px(10));
        mRedPaintRed.setStyle(Paint.Style.FILL_AND_STROKE);
        mShaderPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        Shader mShader = new LinearGradient(0, 0, 0, 1000, new int[]{Color.TRANSPARENT, Color.TRANSPARENT}, null, Shader.TileMode.REPEAT);
        mShaderPaint.setShader(mShader);

        mWhiteBgPaint.setColor(Color.argb(120, 255, 255, 255));
        mWhiteDotPaint.setColor(Color.rgb(255, 255, 255));
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mTopPadding = (int) getResources().getDimension(R.dimen.chart_top_padding);
        mBottomPadding = (int) getResources().getDimension(R.dimen.chart_bottom_padding);

        mKChartTabView = new KChartTabView(getContext());
        addView(mKChartTabView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mKChartTabView.setOnTabSelectListener(new KChartTabView.TabSelectListener() {
            @Override
            public void onTabSelected(int type) {
                setChildDraw(type);
            }
        });

    }

    public void setVolumeMaGone(boolean isGone) {
        if (mChildDraw instanceof VolumeDraw) {
            ((VolumeDraw) mChildDraw).setMaGone(isGone);
        }
    }

    /**
     * 设置K线下方渐变色
     *
     * @param colorTop
     * @param colorEnd
     */
    public void setShader(int colorTop, int colorEnd,int endY) {
        Shader mShader = new LinearGradient(0, 0, 0, endY, new int[]{colorTop, colorEnd}, null, Shader.TileMode.REPEAT);
        mShaderPaint.setShader(mShader);
    }

    public float getLeftTitleMargin() {
        return mLeftTitleMargin;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        String format = formatValue(0f);

        if (!mDrawTabView) {
            mKChartTabView.setVisibility(INVISIBLE);
        }

        if (mIsOutward) {
            mLeftTitleMargin = mTextPaint.measureText(format, 0, format.length()) + 2 * mPadingValue;
        } else {
            mLeftTitleMargin = 0;
        }
        initRect(w, h);
        mKChartTabView.setTranslationY(mMainRect.bottom);
        mKChartTabView.setTranslationX(mLeftTitleMargin);
        setTranslateXFromScrollX(mScrollX);
    }

    private void initRect(int w, int h) {
        int mMainChildSpace = mKChartTabView.getMeasuredHeight() == 0 ? ViewUtil.Dp2Px(getContext(), 5) : mKChartTabView.getMeasuredHeight();
        int displayHeight = h - mTopPadding - mBottomPadding - mMainChildSpace;
        int mMainHeight = 0;
        int mChildHeight = 0;
        if (isDrawDown) {
            mMainHeight = (int) (displayHeight * 0.75f);
            mChildHeight = (int) (displayHeight * 0.25f);
        } else {
            mMainHeight = (int) (displayHeight * 1f);
        }
        mMainRect = new Rect(0, mTopPadding, mWidth, mTopPadding + mMainHeight);
        mTabRect = new Rect(0, mMainRect.bottom, mWidth, mMainRect.bottom + mMainChildSpace);
        mChildRect = new Rect(0, mTabRect.bottom, mWidth, mTabRect.bottom + mChildHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackgroundPaint.getColor());
        if (mWidth == 0 || mMainRect.height() == 0 || mItemCount == 0) {
            return;
        }
        calculateValue();
        canvas.save();
        canvas.scale(1, 1);
        drawGird(canvas);
        drawK(canvas);
        drawText(canvas);
        drawValue(canvas, isLongPress ? mSelectedIndex : mStopIndex);
        canvas.restore();
    }

    public float getMainY(float value) {
        float newValue = (mMainMaxValue - value) * mMainScaleY + mMainRect.top;
        return newValue > (mMainRect.height() + mMainRect.top) ? mMainRect.height() + mMainRect.top : newValue;
    }

    public float getChildY(float value) {
        return (mChildMaxValue - value) * mChildScaleY + mChildRect.top;
    }

    /**
     * 解决text居中的问题
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }

    /**
     * 画表格
     *
     * @param canvas
     */
    private void drawGird(Canvas canvas) {
        //-----------------------上方k线图------------------------
        //横向的grid
        float rowSpace = mMainRect.height() / mGridRows;
        for (int i = 0; i <= mGridRows; i++) {
            if (mDrawGirdLine) {
                canvas.drawLine(mLeftTitleMargin, rowSpace * i + mMainRect.top, mWidth, rowSpace * i + mMainRect.top, mGridPaint);
            } else {//只画 首，尾 分割线
                if (i == mGridRows) {
                    canvas.drawLine(mLeftTitleMargin, rowSpace * i + mMainRect.top, mWidth, rowSpace * i + mMainRect.top, mGridPaint);
                }
            }
        }
        //-----------------------下方子图------------------------
        canvas.drawLine(mLeftTitleMargin, mChildRect.top, mWidth, mChildRect.top, mGridPaint);
        canvas.drawLine(mLeftTitleMargin, mChildRect.bottom, mWidth, mChildRect.bottom, mGridPaint);

        //纵向的grid
        float columnSpace = (mWidth - mLeftTitleMargin) / mGridColumns;
        for (int i = 0; i <= mGridColumns; i++) {
            if (mDrawGirdLine) {
                canvas.drawLine(columnSpace * i + mLeftTitleMargin, mChildRect.top, columnSpace * i + mLeftTitleMargin, mChildRect.bottom, mGridPaint);
                canvas.drawLine(columnSpace * i + mLeftTitleMargin, mMainRect.top, columnSpace * i + mLeftTitleMargin, mMainRect.bottom, mGridPaint);
            } else {//只画 首，尾 分割线
                if (i == 0) {
                    canvas.drawLine(columnSpace * i + mLeftTitleMargin, mChildRect.top, columnSpace * i + mLeftTitleMargin, mChildRect.bottom, mGridPaint);
                    canvas.drawLine(columnSpace * i + mLeftTitleMargin, mMainRect.top, columnSpace * i + mLeftTitleMargin, mMainRect.bottom, mGridPaint);
                }
            }
        }
    }

    /**
     * 画k线图
     *
     * @param canvas
     */
    private void drawK(Canvas canvas) {
        //保存之前的平移，缩放
        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0);

        canvas.scale(mScaleX, 1);
        shaderPath = new Path();
        if (!isDrawMinuteStyle()) {
            if (mStopIndex < mAdapter.getCount() - 1) {
                mStopIndex += 1;
            }
        }
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            Object currentPoint = getItem(i);
            float currentPointX = getX(i);

            Object lastPoint = i == 0 ? currentPoint : getItem(i - 1);
            float lastX = i == 0 ? currentPointX : getX(i - 1);
            if (i == mStartIndex) {
                shaderPath.moveTo(lastX, mMainRect.height() + mMainRect.top);
                shaderPath.lineTo(lastX, getMainY(((IKLine) lastPoint).getClosePrice()));
            } else {
                shaderPath.lineTo(lastX, getMainY(((IKLine) lastPoint).getClosePrice()));

            }
            if (mMainDraw != null) {
                mMainDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
            if (mChildDraw != null && isDrawDown) {
                mChildDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
                if (i == mStopIndex) {
                    int rightIndex = mStopIndex >= mAdapter.getCount() - 1 ? mAdapter.getCount() - 1 : mStopIndex + 1;
                    mChildDraw.drawTranslated(currentPoint, getItem(rightIndex), currentPointX, getX(rightIndex), canvas, this, i);
                }
            }

        }

        if (mMainDraw != null) {
            if (isDrawMinuteStyle()) {
                Object lastPoint = getItem(mStopIndex);
                float lastX = getX(mStopIndex);
                SimpleCandle simpleCandle = new SimpleCandle();
                simpleCandle.setClosePrice(drawBean.mMostRightClosePrice);
                mMainDraw.drawTranslated(lastPoint, simpleCandle, lastX, drawBean.mMostRightX, canvas, this, mAdapter.getCount() - 1 > mStopIndex ? mAdapter.getCount() - 1 : mStopIndex);
                shaderPath.lineTo(getX(mStopIndex), getMainY(((IKLine) getItem(mStopIndex)).getClosePrice()));
                shaderPath.lineTo(drawBean.mMostRightX, drawBean.mMostRightY);
                shaderPath.lineTo(drawBean.mMostRightX, mMainRect.height() + mMainRect.top);
                shaderPath.lineTo(getX(mStartIndex), mMainRect.height() + mMainRect.top);
                shaderPath.close();
                canvas.drawPath(shaderPath, mShaderPaint);
            }
        }
        canvas.restore();

    }

    /**
     * 画文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        //--------------画上方k线图的值-------------
        if (mMainDraw != null) {
            canvas.drawText(formatValue(mMainMaxValue), mPadingValue, baseLine + mMainRect.top, mTextPaint);
            canvas.drawText(formatValue(mMainMinValue), mPadingValue, mMainRect.bottom - textHeight + baseLine, mTextPaint);

            float rowValue = (mMainMaxValue - mMainMinValue) / mGridRows;
            float rowSpace = mMainRect.height() / mGridRows;
            for (int i = 1; i < mGridRows; i++) {
                String text = formatValue(rowValue * (mGridRows - i) + mMainMinValue);
                canvas.drawText(text, mPadingValue, fixTextY(rowSpace * i + mMainRect.top), mTextPaint);
            }
        }
        //--------------画下方子图的值-------------
        if (mChildDraw != null&&isDrawDown) {
            canvas.drawText(mChildDraw.getValueFormatter().format(mChildMaxValue), mPadingValue, mChildRect.top + baseLine, mTextPaint);
            canvas.drawText(mChildDraw.getValueFormatter().format(mChildMinValue), mPadingValue, mChildRect.bottom, mTextPaint);
        }
        //--------------画时间---------------300------
        float columnSpace = (mWidth - mLeftTitleMargin) / mGridColumns;
        float y = mChildRect.bottom + baseLine;

        float startX = getX(mStartIndex) - mLeftTitleMargin - 2 * mPadingValue - mPointWidth / 2;
        float stopX = getX(mStopIndex) + mPointWidth / 2;

        for (int i = 1; i < mGridColumns; i++) {
            float translateX = xToTranslateX(columnSpace * i + mLeftTitleMargin);
            if (translateX >= startX && translateX <= stopX) {
                int index = indexOfTranslateX(translateX);
                String text = formatDateTime(mAdapter.getDate(index));
                canvas.drawText(text, columnSpace * i + mLeftTitleMargin - mTextPaint.measureText(text) / 2, y, mTextPaint);
            }
        }
        //绘制左边时间
        float translateX = xToTranslateX(0);
        if (translateX >= startX && translateX <= stopX) {
            canvas.drawText(formatDateTime(getAdapter().getDate(mStartIndex)), mLeftTitleMargin, y, mTextPaint);
        }
        //绘制右边时间
        translateX = xToTranslateX(mWidth - 6);
//        if (translateX >= startX && translateX <= stopX) {
        String rightTimetext = formatDateTime(getAdapter().getDate(mStopIndex));
        canvas.drawText(rightTimetext, mWidth - mTextPaint.measureText(rightTimetext), y, mTextPaint);
//        }

        if (!mDrawMinuteStyle) {
            //绘制最右边的点及线
            ICandle point = (ICandle) getItem(mStopIndex);

            String closePrice = String.valueOf(point.getClosePrice());
            float timeLineTextWidth = mTextPaint.measureText(closePrice);

            float endX = translateXtoX(getX(mStopIndex));
            float endY = getMainY(point.getClosePrice());
            canvas.drawLine(0, endY, endX, endY, mWhitePaint);
            //绘制最右边的点
            canvas.drawCircle(endX, endY, dp2px(2), mRedPaintRed);
            canvas.drawRect(0, endY - textHeight, timeLineTextWidth, endY, mBackgroundPaint);
            canvas.drawText(closePrice, 0, endY - textHeight + baseLine, mTextPaint);
            //绘制最高最低点指示文本
            drawIndicatorText(canvas, textHeight, baseLine);
        } else {
            float closePriceExact = getExactValue(mWidth - 6, new CalcuMode() {
                @Override
                public float getValue(IKLine value) {
                    return value.getClosePrice();
                }
            });
            float timeLineTextWidth = mTextPaint.measureText(NumberUtil.keepMax8(closePriceExact + ""));
            canvas.drawLine(0, drawBean.mMostRightY, translateXtoX(drawBean.mMostRightX), drawBean.mMostRightY, mWhitePaint);
            //绘制最右边的点
            canvas.drawCircle(translateXtoX(drawBean.mMostRightX), drawBean.mMostRightY, dp2px(2), mRedPaintRed);
            canvas.drawRect(0, drawBean.mMostRightY - textHeight, timeLineTextWidth, drawBean.mMostRightY, mBackgroundPaint);
            canvas.drawText(NumberUtil.keepMax8(closePriceExact + ""), 0, drawBean.mMostRightY - textHeight + baseLine, mTextPaint);
        }

        if (isLongPress) {
            IKLine point = (IKLine) getItem(mSelectedIndex);
            String text;
            String timeText;
            timeText = formatDateTime(mAdapter.getDate(mSelectedIndex));
            text = instance.format(moveText);
            float r = textHeight / 2;
            y = getMainY(point.getClosePrice());
            float x;
            if (translateXtoX(getX(mSelectedIndex)) < getChartWidth() / 2) {
                x = 0;
                canvas.drawRect(x, y - r, mTextPaint.measureText(text), y + r, mBackgroundPaint);
            } else {
                x = mWidth - mTextPaint.measureText(text);
                canvas.drawRect(x, y - r, mWidth, y + r, mBackgroundPaint);
            }
            canvas.drawText(text, x, fixTextY(y), mTextPaint);


            //画指示线的时间 下方时间
            float timeTextWidth = mTextPaint.measureText(timeText);
            if (translateXtoX(getX(mSelectedIndex)) + timeTextWidth / 2 > mWidth) {
                canvas.drawRect(translateXtoX(getX(mSelectedIndex)) - timeTextWidth, mMainRect.bottom - textHeight, mWidth, mMainRect.bottom + textHeight, mBackgroundPaint);
                canvas.drawText(timeText, translateXtoX(getX(mSelectedIndex)) - timeTextWidth, mMainRect.bottom - textHeight + baseLine, mTextPaint);
            } else if (translateXtoX(getX(mSelectedIndex)) - timeTextWidth / 2 < mLeftTitleMargin) {
                canvas.drawRect(mLeftTitleMargin, mMainRect.bottom - textHeight, mLeftTitleMargin + timeTextWidth, mMainRect.bottom + textHeight, mBackgroundPaint);
                canvas.drawText(timeText, mLeftTitleMargin, mMainRect.bottom - textHeight + baseLine, mTextPaint);
            } else {
                canvas.drawRect(translateXtoX(getX(mSelectedIndex)) - timeTextWidth / 2, mMainRect.bottom - textHeight, translateXtoX(getX(mSelectedIndex)) + timeTextWidth / 2, mMainRect.bottom + textHeight, mBackgroundPaint);
                canvas.drawText(timeText, translateXtoX(getX(mSelectedIndex)) - timeTextWidth / 2, mMainRect.bottom - textHeight + baseLine, mTextPaint);
            }
            canvas.drawLine(animX, mMainRect.top, animX, mMainRect.bottom, mSelectedLinePaint);
            canvas.drawLine(0, animY, mWidth, animY, mSelectedLinePaint);
            canvas.drawLine(animX, mChildRect.top, animX, mChildRect.bottom, mSelectedLinePaint);
            //画指示点
            canvas.drawCircle(animX, animY, 20, mWhiteBgPaint);
            canvas.drawCircle(animX, animY, 8, mWhiteDotPaint);
        }
    }

    /**
     * 绘制 k线 下部填充
     *
     * @param canvas
     * @param textHeight
     * @param baseLine
     */
    private void drawIndicatorText(Canvas canvas, float textHeight, float baseLine) {
        ICandle maxCandle = (ICandle) mAdapter.getItem(drawBean.maxPos);
        ICandle minCandle = (ICandle) mAdapter.getItem(drawBean.minPos);
        if (getX(drawBean.maxPos) > drawBean.centerX) {
            mTextMaxMinPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(maxCandle.getHighPrice() + "-->", translateXtoX(getX(drawBean.maxPos)), getMainY(maxCandle.getHighPrice()) + baseLine - textHeight / 2, mTextMaxMinPaint);
        } else {
            mTextMaxMinPaint.setTextAlign(Paint.Align.LEFT);

            canvas.drawText("<--" + maxCandle.getHighPrice(), translateXtoX(getX(drawBean.maxPos)), getMainY(maxCandle.getHighPrice()) + baseLine - textHeight / 2, mTextMaxMinPaint);
        }
        if (getX(drawBean.minPos) > drawBean.centerX) {
            mTextMaxMinPaint.setTextAlign(Paint.Align.RIGHT);

            canvas.drawText(minCandle.getLowPrice() + "-->", translateXtoX(getX(drawBean.minPos)), getMainY(minCandle.getLowPrice()) + baseLine - textHeight / 2, mTextMaxMinPaint);
        } else {
            mTextMaxMinPaint.setTextAlign(Paint.Align.LEFT);

            canvas.drawText("<--" + minCandle.getLowPrice(), translateXtoX(getX(drawBean.minPos)), getMainY(minCandle.getLowPrice()) + baseLine - textHeight / 2, mTextMaxMinPaint);
        }
    }


    /**
     * 画值
     *
     * @param canvas
     * @param position 显示某个点的值
     */
    private void drawValue(Canvas canvas, int position) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        if (position >= 0 && position < mItemCount) {
            if (mMainDraw != null) {
                float y = mMainRect.top + baseLine - textHeight;
                float x = mLeftTitleMargin;
                mMainDraw.drawText(canvas, this, position, x, y);
            }
            if (mChildDraw != null) { //底部 ma5、ma10、ma20 文本位置
                float y = mChildRect.top + baseLine;
                float x = mLeftTitleMargin == 0 ? mTextPaint.measureText(mChildDraw.getValueFormatter().format(mChildMaxValue) + "    ") : mLeftTitleMargin;
                mChildDraw.drawText(canvas, this, position, x, y);
            }
        }
    }

    public int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 格式化值
     */
    public String formatValue(float value) {
        if (getValueFormatter() == null) {
            setValueFormatter(new ValueFormatter());
        }
        return getValueFormatter().format(value);
    }

    /**
     * 重新计算并刷新线条
     */
    public void notifyChanged() {
        if (mItemCount != 0) {
            mDataLen = (mItemCount - 1) * mPointWidth;
            checkAndFixScrollX();
            setTranslateXFromScrollX(mScrollX);
        } else {
            setScrollX(0);
        }
        invalidate();
    }

    //    public void notifyItemInsert() {
//        if (mAdapter.getCount() != 0) {
//            mDataLen = (mItemCount - 1) * mPointWidth;
//            if (mDrawMinuteStyle && isFullScreen()) {
//                float endScrollX = mScrollX + 6;
//                float startScrollX = mScrollX + (mItemCount - mLastItemCount) * mPointWidth;
//                if (startScrollX != endScrollX) {
//                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(startScrollX, endScrollX)
//                            .setDuration(1000);
//                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            mScrollX = ((Float) animation.getAnimatedValue()).intValue();
////                            checkAndFixScrollX();
//                            setTranslateXFromScrollX(mScrollX);
//                            invalidate();
//                        }
//                    });
//                    valueAnimator.start();
//                }
//            } else {
//                checkAndFixScrollX();
//                setTranslateXFromScrollX(mScrollX);
//                invalidate();
//            }
//        } else {
//            setScrollX(0);
//            invalidate();
//        }
//    }
    public void notifyItemInsert() {
        if (mAdapter.getCount() != 0) {
            mDataLen = (mItemCount - 1) * mPointWidth;
            if (mDrawMinuteStyle && isFullScreen() && mStopIndex + 5 >= mLastItemCount) {
                float endLen = mDataLen;
                float startLen = (mLastItemCount - 1) * mPointWidth;
                if (endLen != startLen) {
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(startLen, endLen)
                            .setDuration(700);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mDataLen = (Float) animation.getAnimatedValue();
//                            checkAndFixScrollX();
                            setTranslateXFromScrollX(mScrollX);
                            invalidate();
                        }
                    });
                    valueAnimator.start();
                }
            } else if (!mDrawMinuteStyle && isFullScreen() && mStopIndex + 5 >= mLastItemCount) {
                float endLen = mDataLen;
                float startLen = (mLastItemCount - 1) * mPointWidth;
                if (endLen != startLen) {
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(startLen, endLen)
                            .setDuration(700);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mDataLen = (Float) animation.getAnimatedValue();
//                            checkAndFixScrollX();
                            setTranslateXFromScrollX(mScrollX);
                            invalidate();
                        }
                    });
                    valueAnimator.start();
                }
                lastItem = (ICandle) mAdapter.getItem(mAdapter.getCount() - 1);
                float startPrice = lastItem.getOpenPrice();
                float endPrice = lastItem.getClosePrice();
                if (startPrice != endPrice) {
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(startPrice, endPrice)
                            .setDuration(900);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            lastItem.setClosePrice((Float) animation.getAnimatedValue());
                            invalidate();
                        }
                    });
                    valueAnimator.start();
                }

            } else {
                checkAndFixScrollX();
//                setTranslateXFromScrollX((int) (mScrollX+mPointWidth));
                invalidate();
            }
        } else {
            setScrollX(0);
            invalidate();
        }
    }

    public void notifyItemChange(float closePrice) {
        final ICandle lastItem = (ICandle) mAdapter.getItem(mAdapter.getCount() - 1);
        float startPrice = lastItem.getClosePrice();
        float endPrice = closePrice;
        if (startPrice != endPrice) {
//            lastItem.setClosePrice(closePrice);
//            invalidate();
            if (changePriceAnimator != null && changePriceAnimator.isRunning()) {
                changePriceAnimator.cancel();
                changePriceAnimator = null;
            }
            changePriceAnimator = getChangePriceAnimator(startPrice, endPrice);
        }
    }

    private ValueAnimator getChangePriceAnimator(float startPrice, float endPrice) {
        ValueAnimator newPriceValueAnimator = ValueAnimator.ofFloat(startPrice, endPrice)
                .setDuration(1000);
        newPriceValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ((ICandle) mAdapter.getItem(mAdapter.getCount() - 1)).setClosePrice((Float) animation.getAnimatedValue());
                invalidate();
            }
        });
        newPriceValueAnimator.start();
        return newPriceValueAnimator;

    }

    private void calculateSelectedX(float x) {
        mSelectedIndex = indexOfTranslateX(xToTranslateX(x));
        if (mSelectedIndex < mStartIndex) {
            mSelectedIndex = mStartIndex;
        }
        if (mSelectedIndex > mStopIndex) {
            mSelectedIndex = mStopIndex;
        }
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        int lastIndex = mSelectedIndex;

        calculateSelectedX(e.getX());
        if (lastIndex != mSelectedIndex) {
            onSelectedChanged(this, getItem(mSelectedIndex), mSelectedIndex);
        }
        calculateAnimXY(e);
        invalidate();
    }

    /**
     * 计算移动的点位坐标
     *
     * @param e
     */
    private void calculateAnimXY(MotionEvent e) {
        animX = xToTranslateX(e.getX());
        int startIndex = (int) (animX / mPointWidth);
        int endIndex = (int) (animX / mPointWidth) + 1;
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (startIndex >= getAdapter().getCount()) {
            startIndex = getAdapter().getCount() - 1;
        }
        if (endIndex >= getAdapter().getCount()) {
            endIndex = getAdapter().getCount() - 1;
        }
        if (endIndex < 0) {
            endIndex = 0;
        }
        IKLine startKLine = (IKLine) getItem(startIndex);
        IKLine endKLine = (IKLine) getItem(endIndex);
        float percent = (animX - startIndex * mPointWidth) / mPointWidth;
        float startY = getMainY(startKLine.getClosePrice());
        float endY = getMainY(endKLine.getClosePrice());
        moveText = startKLine.getClosePrice() + (endKLine.getClosePrice() - startKLine.getClosePrice()) * percent;
        animY = startY + (endY - startY) * percent;
        animX = e.getX();
        animX = Math.min(e.getX(), translateXtoX(getX(mAdapter.getCount() - 1)));
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        setTranslateXFromScrollX(mScrollX);
    }

    @Override
    protected void onScaleChanged(float scale, float oldScale) {
        checkAndFixScrollX();
        setTranslateXFromScrollX(mScrollX);
        super.onScaleChanged(scale, oldScale);
    }

    /**
     * 计算当前的显示区域
     */
    private void calculateValue() {
        if (!isLongPress()) {
            mSelectedIndex = -1;
        }
        mStartIndex = indexOfTranslateX(xToTranslateX(mLeftTitleMargin + (2 * mPadingValue))); //TODO 这里修改蜡烛图起始位置(加上pading,解决绘制越界问题)
        mStartIndex = mStartIndex <= 0 ? 0 : (mStartIndex - 1);
        mStopIndex = (int) (xToTranslateX(mWidth - 6) / mPointWidth);//TODO 这里修改蜡烛图结束位置
        if (mStopIndex > mAdapter.getCount() - 1) {
            mStopIndex = mAdapter.getCount() - 1;
        }

        calculateMaxAndMinUp();//计算上面绘制的最大值与最小值
        animateMaxAndMinUp();
        mMainMaxValue = mMainMaxFinalValue;
        mMainMinValue = mMainMinFinalValue;
//        if (mMainMaxValue != mMainMinValue) {
//            float padding = (mMainMaxValue - mMainMinValue) * 0.1f;
////            if (padding < 0.00012f) {
////                padding = 0.00012f;
////            }
//            mMainMaxValue += padding;
//            mMainMinValue -= padding;
//        } else {
//            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
//            mMainMaxValue += Math.abs(mMainMaxValue * 0.1f);
//            mMainMinValue -= Math.abs(mMainMinValue * 0.1f);
//            if (mMainMaxValue == 0) {
//                mMainMaxValue = 1;
//            }
//        }
        float[] floats = calculateMaxAndMin(mMainMaxValue, mMainMinValue);
        mMainMinValue = floats[0];
        mMainMaxValue = floats[1];
        calculateMaxAndMinDown();//计算下面绘制的最大值与最小值

        mMainScaleY = mMainRect.height() * 1f / (mMainMaxValue - mMainMinValue);
        mChildScaleY = mChildRect.height() * 1f / (mChildMaxValue - mChildMinValue);
        drawBean.centerX = xToTranslateX(mWidth / 2);
        drawBean.mMostRightY = getExactValue(mWidth - 6, new CalcuMode() {
            @Override
            public float getValue(IKLine value) {
                return getMainY(value.getClosePrice());
            }
        });
        drawBean.mMostRightClosePrice = getExactValue(mWidth - 6, new CalcuMode() {
            @Override
            public float getValue(IKLine value) {
                return value.getClosePrice();
            }
        });
        if (isFullScreen()) {
            drawBean.mMostRightX = xToTranslateX(mWidth - 6);
        } else {
            drawBean.mMostRightX = getX(mStopIndex);
        }
    }

    private void animateMaxAndMinUp() {
        if (!isAnim) {//无动画
            if (!isFirst) {
                if ((mChangeMainMaxValue != mLastMainMaxValue || mChangeMainMinValue != mLastMainMinValue)) {
                    animScaleValue();
                }
                mLastMainMaxValue = mMainMaxFinalValue;
                mLastMainMinValue = mMainMinFinalValue;

            } else {
                isFirst = false;
                mMainMaxFinalValue = mChangeMainMaxValue;
                mMainMinFinalValue = mChangeMainMinValue;
                mLastMainMaxValue = mMainMaxFinalValue;
                mLastMainMinValue = mMainMinFinalValue;
            }

        } else {//动画进行中
            if (mChangeMainMaxValue != mChangeNextMainMaxValue || mChangeMainMinValue != mChangeNextMainMinValue) {
                mLastMainMaxValue = mMainMaxFinalValue;
                mLastMainMinValue = mMainMinFinalValue;
                animScaleValue();
            }
            if (mChangeNextMainMaxValue == mMainMaxFinalValue && mChangeNextMainMinValue == mMainMinFinalValue) {//动画结束
                mLastMainMaxValue = mMainMaxFinalValue;
                mLastMainMinValue = mMainMinFinalValue;
                isAnim = false;
            }
        }
    }

    private void animScaleValue() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
        valueAnimator = ValueAnimator.ofInt(0, 100)
                .setDuration(300);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        isAnim = true;
        perMax = (mChangeMainMaxValue - mMainMaxFinalValue) / 100f;
        perMin = (mChangeMainMinValue - mMainMinFinalValue) / 100f;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMainMaxFinalValue = mLastMainMaxValue + (float) ((int) animation.getAnimatedValue() * perMax);
                mMainMinFinalValue = mLastMainMinValue + (float) ((int) animation.getAnimatedValue() * perMin);
                invalidate();
            }
        });
        valueAnimator.start();
        mChangeNextMainMaxValue = mChangeMainMaxValue;
        mChangeNextMainMinValue = mChangeMainMinValue;
    }

    /**
     * 计算下部绘制区域，最大值与最小值
     */
    private void calculateMaxAndMinDown() {
        if (mLastStartIndex != mStartIndex || mLastStopIndex != mStopIndex || (mStartIndex == mStopIndex && mStopIndex == 0)) {//当起始与结束位置不一样时，重新构建绘制时用的数据
            mChildMaxValue = 0;
            mChildMinValue = Float.MAX_VALUE;
            for (int i = mStartIndex; i <= ((mStopIndex >= mAdapter.getCount() - 1) ? mStopIndex : mStopIndex + 1); i++) {
                IKLine point = (IKLine) getItem(i);
                if (mChildDraw != null) {
                    mChildMaxValue = Math.max(mChildMaxValue, mChildDraw.getMaxValue(point));
                    mChildMinValue = Math.min(mChildMinValue, mChildDraw.getMinValue(point));
                }
            }
            float[] floats = calculateMaxAndMin(mChildMaxValue, mChildMinValue);
            mChildMinValue = floats[0];
            mChildMaxValue = floats[1];
        } else {
            mChildMaxValue = mLastChildMaxValue;
            mChildMinValue = mLastChildMinValue;
        }
        mLastChildMaxValue = mChildMaxValue;
        mLastChildMinValue = mChildMinValue;
    }

    /**
     * 当最大值和最小值都相等的时候 处于中间位置,小于等于0时,最大为1,最小为0,其它最大值处于
     *
     * @param mMaxValue 不能小于0
     * @param mMinValue 不能小于0
     * @return
     */
    private float[] calculateMaxAndMin(float mMaxValue, float mMinValue) {
        float flag;
        flag = Math.max(mMaxValue, mMinValue);
        mMinValue = Math.min(mMaxValue, mMinValue);
        mMaxValue = flag;
        float[] floats = new float[2];
        if (mMaxValue == mMinValue) {
            if (mMaxValue <= 0) {
                floats[0] = 0;
                floats[1] = 1;
            } else {
                floats[0] = mMaxValue * 0.9f;
                floats[1] = mMaxValue * 1.1f;
            }
        } else {
            floats[0] = mMinValue - Math.abs(mMaxValue - mMinValue) * 0.1f;
            floats[1] = mMaxValue + Math.abs(mMaxValue - mMinValue) * 0.1f;
//            if (floats[0] < 0) {
//                floats[0] = 0;
//            }
        }
        return floats;
    }

    public DrawBean getDrawBean() {
        return drawBean;
    }

    /**
     * 计算上部绘制区域，最大值与最小值
     */
    private void calculateMaxAndMinUp() {
        mChangeMainMaxValue = Float.MIN_VALUE;
        mChangeMainMinValue = Float.MAX_VALUE;
        float startX = xToTranslateX(mLeftTitleMargin + (2 * mPadingValue));
        int mStartCalcuIndex = (int) (startX / mPointWidth);
        float endX = xToTranslateX(mWidth);
        int mEndCalcuIndex = (int) (endX / mPointWidth);
        float mMainMaxHighValue = Float.MIN_VALUE;
        float mMainMinLowValue = Float.MAX_VALUE;
        if (mStartCalcuIndex < 0) {
            mStartCalcuIndex = 0;
        }
        if (mEndCalcuIndex > mAdapter.getCount() - 1) {
            mEndCalcuIndex = mAdapter.getCount() - 1;
        }
        if (mStartCalcuIndex != drawBean.mLastStartCalcuIndex || mEndCalcuIndex != drawBean.mLastEndCalcuIndex) {
            for (int i = mStartCalcuIndex; i <= mEndCalcuIndex; i++) {
                IKLine point = (IKLine) getItem(i);
                if (mMainDraw != null) {
                    mChangeMainMaxValue = Math.max(mChangeMainMaxValue, mMainDraw.getMaxValue(point));
                    mMainMaxHighValue = Math.max(mMainMaxHighValue, point.getHighPrice());
                    if (mMainMaxHighValue == point.getHighPrice()) {
                        drawBean.maxPos = i;
                    }
                    mChangeMainMinValue = Math.min(mChangeMainMinValue, mMainDraw.getMinValue(point));
                    mMainMinLowValue = Math.min(mMainMinLowValue, point.getLowPrice());
                    if (mMainMinLowValue == point.getLowPrice()) {
                        drawBean.minPos = i;
                    }
                }
            }
        } else {
            drawBean.mLastStartCalcuIndex = mStartCalcuIndex;
            drawBean.mLastEndCalcuIndex = mEndCalcuIndex;
            drawBean.mLastCalcuMainMaxValue = mChangeMainMaxValue;
            drawBean.mLastCalcuMainMinValue = mChangeMainMinValue;
        }
        mChangeMainMaxValue = Math.max(mChangeMainMaxValue, getExactValue(mLeftTitleMargin + (2 * mPadingValue), new CalcuMode() {
            @Override
            public float getValue(IKLine value) {
                return mMainDraw.getMaxValue(value);
            }
        }));
        mChangeMainMaxValue = Math.max(mChangeMainMaxValue, getExactValue(mWidth, new CalcuMode() {

            @Override
            public float getValue(IKLine value) {
                return mMainDraw.getMaxValue(value);
            }
        }));
        mChangeMainMinValue = Math.min(mChangeMainMinValue, getExactValue(mLeftTitleMargin + (2 * mPadingValue), new CalcuMode() {

            @Override
            public float getValue(IKLine value) {
                return mMainDraw.getMinValue(value);
            }
        }));
        mChangeMainMinValue = Math.min(mChangeMainMinValue, getExactValue(mWidth, new CalcuMode() {

            @Override
            public float getValue(IKLine value) {
                return mMainDraw.getMinValue(value);
            }
        }));

    }

    /**
     * 根据当前坐标值，计算出该计算模式的平滑过渡值
     *
     * @param positionX
     * @param calcuMode
     * @return
     */
    private float getExactValue(float positionX, CalcuMode calcuMode) {
        float startX = xToTranslateX(positionX);
        int startIndex = (int) (startX / mPointWidth);
        int endIndex = (int) (startX / mPointWidth) + 1;
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (startIndex >= getAdapter().getCount()) {
            startIndex = getAdapter().getCount() - 1;
        }
        if (endIndex >= getAdapter().getCount()) {
            endIndex = getAdapter().getCount() - 1;
        }
        if (endIndex < 0) {
            endIndex = 0;
        }
        IKLine startKLine = (IKLine) getItem(startIndex);
        IKLine endKLine = (IKLine) getItem(endIndex);
        float percent = (startX - startIndex * mPointWidth) / mPointWidth;
        float startY = calcuMode.getValue(startKLine);
        float endY = calcuMode.getValue(endKLine);
        return startY + (endY - startY) * percent;
    }

    interface CalcuMode {
        float getValue(IKLine value);
    }

    /**
     * 获取平移的最小值
     *
     * @return
     */
    private float getMinTranslateX() {
        return -mDataLen + mWidth / mScaleX - mPointWidth / 2;
    }

    /**
     * 获取平移的最大值
     *
     * @return
     */
    private float getMaxTranslateX() {
        if (!isFullScreen()) {
            return getMinTranslateX();
        }
        return mPointWidth / 2;
    }

    @Override
    public int getMinScrollX() {
        return (int) -(mOverScrollRange / mScaleX);
    }

    public int getMaxScrollX() {
        return Math.round(getMaxTranslateX() - getMinTranslateX());
    }

    public int indexOfTranslateX(float translateX) {
        return indexOfTranslateX(translateX, 0, mItemCount - 1);
    }

    /**
     * 在主区域画线
     *
     * @param startX    开始点的横坐标
     * @param stopX     开始点的值
     * @param stopX     结束点的横坐标
     * @param stopValue 结束点的值
     */
    public void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), paint);


    }

    /**
     * 在子区域画线
     *
     * @param startX     开始点的横坐标
     * @param startValue 开始点的值
     * @param stopX      结束点的横坐标
     * @param stopValue  结束点的值
     */
    public void drawChildLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        canvas.drawLine(startX, getChildY(startValue), stopX, getChildY(stopValue), paint);
    }

    /**
     * 根据索引获取实体
     *
     * @param position 索引值
     * @return
     */
    public Object getItem(int position) {
        if (mAdapter != null) {
            return mAdapter.getItem(position);
        } else {
            return null;
        }
    }

    /**
     * 根据索引索取x坐标
     *
     * @param position 索引值
     * @return
     */
    public float getX(int position) {
        return position * mPointWidth;
    }

    /**
     * 获取适配器
     *
     * @return
     */
    public IAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 设置子图的绘制方法
     *
     * @param position
     */
    private void setChildDraw(int position) {
        this.mChildDraw = mChildDraws.get(position);
        mChildDrawPosition = position;
        invalidate();
    }

    /**
     * 给子区域添加画图方法
     *
     * @param name      显示的文字标签
     * @param childDraw IChartDraw
     */
    public void addChildDraw(String name, IChartDraw childDraw) {
        mChildDraws.add(childDraw);
        mKChartTabView.addTab(name);
    }

    /**
     * scrollX 转换为 TranslateX
     *
     * @param scrollX
     */
    private void setTranslateXFromScrollX(int scrollX) {
        if (isFullScreen()) {
            mTranslateX = scrollX + getMinTranslateX();
        } else {
            mTranslateX = scrollX;
        }
    }

    /**
     * 获取ValueFormatter
     *
     * @return
     */
    public IValueFormatter getValueFormatter() {
        return mValueFormatter;
    }

    /**
     * 设置ValueFormatter
     *
     * @param valueFormatter value格式化器
     */
    public void setValueFormatter(IValueFormatter valueFormatter) {
        this.mValueFormatter = valueFormatter;
    }

    /**
     * 获取DatetimeFormatter
     *
     * @return 时间格式化器
     */
    public IDateTimeFormatter getDateTimeFormatter() {
        return mDateTimeFormatter;
    }

    /**
     * 设置dateTimeFormatter
     *
     * @param dateTimeFormatter 时间格式化器
     */
    public void setDateTimeFormatter(IDateTimeFormatter dateTimeFormatter) {
        mDateTimeFormatter = dateTimeFormatter;
    }

    /**
     * 格式化时间
     *
     * @param date
     */
    public String formatDateTime(Date date) {
        if (getDateTimeFormatter() == null) {
            setDateTimeFormatter(new TimeFormatter());
        }
        return getDateTimeFormatter().format(date);
    }

    /**
     * 获取主区域的 IChartDraw
     *
     * @return IChartDraw
     */
    public IChartDraw getMainDraw() {
        return mMainDraw;
    }

    /**
     * 设置主区域的 IChartDraw
     *
     * @param mainDraw IChartDraw
     */
    public void setMainDraw(IChartDraw mainDraw) {
        mMainDraw = mainDraw;
    }

    /**
     * 二分查找当前值的index
     *
     * @return
     */
    public int indexOfTranslateX(float translateX, int start, int end) {
        if (end == start) {
            return start;
        }
        if (end - start == 1) {
            float startValue = getX(start);
            float endValue = getX(end);
            return Math.abs(translateX - startValue) < Math.abs(translateX - endValue) ? start : end;
        }
        int mid = start + (end - start) / 2;
        float midValue = getX(mid);
        if (translateX < midValue) {
            return indexOfTranslateX(translateX, start, mid);
        } else if (translateX > midValue) {
            return indexOfTranslateX(translateX, mid, end);
        } else {
            return mid;
        }
    }

    /**
     * 设置数据适配器
     */
    public void setAdapter(IAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mItemCount = mAdapter.getCount();
        } else {
            mItemCount = 0;
        }
        notifyChanged();
    }

    /**
     * 设置表格行数
     */
    public void setGridRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        mGridRows = gridRows;
    }

    /**
     * 设置表格列数
     */
    public void setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        mGridColumns = gridColumns;
    }

    /**
     * view中的x转化为TranslateX
     *
     * @param x
     * @return
     */
    public float xToTranslateX(float x) {
        return -mTranslateX + x / mScaleX;
    }

    /**
     * translateX转化为view中的x
     *
     * @param translateX
     * @return
     */
    public float translateXtoX(float translateX) {
        return (translateX + mTranslateX) * mScaleX;
    }

    /**
     * 获取上方padding
     */
    public float getTopPadding() {
        return mTopPadding;
    }

    /**
     * 获取图的宽度
     *
     * @return
     */
    public int getChartWidth() {
        return mWidth;
    }

    /**
     * 是否长按
     */
    public boolean isLongPress() {
        return isLongPress;
    }

    /**
     * 获取选择索引
     */
    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public Rect getChildRect() {
        return mChildRect;
    }

    /**
     * 设置选择监听
     */
    public void setOnSelectedChangedListener(OnSelectedChangedListener l) {
        this.mOnSelectedChangedListener = l;
    }

    public void onSelectedChanged(BaseKChartView view, Object point, int index) {
        if (this.mOnSelectedChangedListener != null) {
            mOnSelectedChangedListener.onSelectedChanged(view, point, index);
        }
    }

    /**
     * 数据是否充满屏幕
     *
     * @return
     */
    public boolean isFullScreen() {
        return mDataLen >= mWidth / mScaleX;
    }

    /**
     * 设置超出右方后可滑动的范围
     */
    public void setOverScrollRange(float overScrollRange) {
        if (overScrollRange < 0) {
            overScrollRange = 0;
        }
        mOverScrollRange = overScrollRange;
    }

    /**
     * 设置上方padding
     *
     * @param topPadding
     */
    public void setTopPadding(int topPadding) {
        mTopPadding = topPadding;
    }

    /**
     * 设置下方padding
     *
     * @param bottomPadding
     */
    public void setBottomPadding(int bottomPadding) {
        mBottomPadding = bottomPadding;
    }

    /**
     * 设置表格线宽度
     */
    public void setGridLineWidth(float width) {
        mDefaultGridLineWidth = width;
        mGridPaint.setStrokeWidth(width);
    }

    /**
     * 设置表格线颜色
     */
    public void setGridLineColor(int color) {
        mGridPaint.setColor(color);
    }

    /**
     * 设置 k线 下部填充颜色
     */
    public void setFillColor(int color) {
        mDrawFillPaint.setColor(color);
    }

    /**
     * 设置选择线宽度
     */
    public void setSelectedLineWidth(float width) {
        mSelectedLinePaint.setStrokeWidth(width);
    }

    /**
     * 设置表格线颜色
     */
    public void setSelectedLineColor(int color) {
        mSelectedLinePaint.setColor(color);
    }

    /**
     * 设置文字颜色
     */
    public void setTextColor(int color) {
        mTextPaint.setColor(color);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mTextPaint.setTextSize(textSize);
    }

    /**
     * 设置背景颜色
     */
    public void setBackgroundColor(int color) {
        mBackgroundPaint.setColor(color);
    }


    public boolean isDrawMinuteStyle() {
        return mDrawMinuteStyle;
    }

    /**
     * 是否以蜡烛图方式绘制，否则 绘制线
     *
     * @param drawMinuteStyle
     */
    public void setDrawMinuteStyle(boolean drawMinuteStyle) {
        mDrawMinuteStyle = drawMinuteStyle;
    }

    /**
     * 设置是否绘制分割线
     *
     * @param drawGirdLine
     */
    public void setDrawGirdLine(boolean drawGirdLine) {
        mDrawGirdLine = drawGirdLine;
    }

    /**
     * 设置左边分界标题是否显示在边框外部
     *
     * @param isOutward
     */
    public void setLeftTitleOutward(boolean isOutward) {
        mIsOutward = isOutward;
    }

    /**
     * 选中点变化时的监听
     */
    public interface OnSelectedChangedListener {
        /**
         * 当选点中变化时
         *
         * @param view  当前view
         * @param point 选中的点
         * @param index 选中点的索引
         */
        void onSelectedChanged(BaseKChartView view, Object point, int index);
    }

    /**
     * 获取文字大小
     */
    public float getTextSize() {
        return mTextPaint.getTextSize();
    }

    /**
     * 获取曲线宽度
     */
    public float getLineWidth() {
        return mLineWidth;
    }

    /**
     * 设置曲线的宽度
     */
    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
    }

    /**
     * 是否绘制tabView
     *
     * @param drawTabView
     */
    public void setDrawTabView(boolean drawTabView) {
        mDrawTabView = drawTabView;
    }


    /**
     * 设置每个点的宽度
     */
    public void setPointWidth(float pointWidth) {
        mPointWidth = pointWidth;
    }

    public Paint getGridPaint() {
        return mGridPaint;
    }

    public Paint getTextPaint() {
        return mTextPaint;
    }

    public Paint getBackgroundPaint() {
        return mBackgroundPaint;
    }

    public Paint getSelectedLinePaint() {
        return mSelectedLinePaint;
    }

    public void setMainMaStartPadding(int padding) {
        if (mMainDraw != null) {
            if (mMainDraw instanceof MainDraw) {
                ((MainDraw) mMainDraw).setMaStartPadding(padding);
            }
        }
    }

    public void setMainMaSpacePadding(int padding) {
        if (mMainDraw != null) {
            if (mMainDraw instanceof MainDraw) {
                ((MainDraw) mMainDraw).setMaSpacePadding(padding);
            }
        }
    }
}
