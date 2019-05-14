package com.gamerole.kchart;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.tifezh.kchartlib.chart.BaseKChartAdapter;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.DataHelper;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.TimeFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class CandleActivity extends AppCompatActivity {

    private KChartAdapter mAdapter;
    private List<Stock> stocksAdd;
    private boolean isShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kline);
        final KChartView mMinuteKchartview = findViewById(R.id.minute_kchartview);
        mMinuteKchartview.setDrawTabView(true);
        mMinuteKchartview.setShader(Color.WHITE, Color.GREEN, Color.YELLOW, 1500);
        mMinuteKchartview.setGridRows(4);
        mMinuteKchartview.setRedUpAndGreenDown(false);
        mMinuteKchartview.setGridColumns(4);
        mMinuteKchartview.setVolumeMaGone(false);
        mMinuteKchartview.setMainDrawBollShow();
        mMinuteKchartview.setDrawMinuteStyle(false);
        mMinuteKchartview.setBreathColor(Color.GREEN);
        mMinuteKchartview.setDateTimeFormatter(new TimeFormatter());
        mMinuteKchartview.setSelectorBackgroundColor(Color.GREEN);
        mMinuteKchartview.setTextColor(Color.RED);
        mMinuteKchartview.setCurrentDownText("123123123");
        mMinuteKchartview.setCurrentDownText2("123123123");
        mMinuteKchartview.setUpDownPadding(30);
        mMinuteKchartview.setLabel("杀生科技");
        mMinuteKchartview.setDrawVolume(false);
        mMinuteKchartview.setDrawDown(false);
        mMinuteKchartview.setLightDrawable(R.drawable.ada);
        mMinuteKchartview.setLabelGravity(BaseKChartView.Gravity.RIGHT);
        Paint paint = mMinuteKchartview.getmCurrentLinePaint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
        paint.setColor(Color.RED);

//        mMinuteKchartview.setTabBackGroundColor(ContextCompat.getColor(this,R.color.chart_red));
        mAdapter = new KChartAdapter();
        mMinuteKchartview.setAdapter(mAdapter);
        InputStream input = null;
        try {
            input = getAssets().open("data.json");
            String json = convertStreamToString(input);
            HttpResult<List<Stock>> httpResult = new Gson().fromJson(json, new TypeToken<HttpResult<List<Stock>>>() {
            }.getType());
            Collections.reverse(httpResult.getData());

            DataUtil.calculate(httpResult.getData());
            List<Stock> stocks = httpResult.getData().subList(0, 100);
            stocksAdd = httpResult.getData().subList(100, httpResult.getData().size());
            mAdapter.updateData(stocks);
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        findViewById(R.id.btBoll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMinuteKchartview.setMainDrawBollShow();
            }
        });
        findViewById(R.id.btMa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMinuteKchartview.setMainDrawMaShow();

            }
        });
        findViewById(R.id.btKdj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMinuteKchartview.changeKDJ();

            }
        });
        findViewById(R.id.btMacd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMinuteKchartview.changeMACD();

            }
        });
        findViewById(R.id.btRsi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMinuteKchartview.changeRSI();

            }
        });
        findViewById(R.id.btWr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMinuteKchartview.changeWR();

            }
        });
        findViewById(R.id.btToggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = !isShow;
                mMinuteKchartview.setDrawDown(isShow);

            }
        });

    }

    private void startTimer() {
        Flowable.intervalRange(0, stocksAdd.size(), 0, 2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mAdapter.changeLastItemClosePrice(stocksAdd.get(aLong.intValue()).getClosePrice());
                    }
                });
    }

    /**
     * input 流转换为字符串
     *
     * @param is
     * @return
     */
    private static String convertStreamToString(InputStream is) {
        String s = null;
        try {
            Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
            if (scanner.hasNext()) {
                s = scanner.next();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }


    /**
     * 数据适配器
     * Created by tifezh on 2016/6/18.
     */

    public class KChartAdapter extends BaseKChartAdapter {

        private List<Stock> datas = new ArrayList<>();

        public KChartAdapter() {

        }

        public List<Stock> getDatas() {
            return datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public Date getDate(int position) {
            return new Date(datas.get(position).getDate());
        }

        /**
         * 向头部添加数据
         */
        public void addNewData(List<Stock> data) {
            if (data != null && !data.isEmpty()) {
                datas.addAll(data);
                notifyItemRangeInsertedToLast();
            }
        }

        /**
         * 向头部添加数据
         */
        public void addNewData(Stock data) {
            if (data != null) {
                datas.add(data);
                notifyItemRangeInsertedToLast();
            }
        }


        /**
         * 向尾部添加数据
         */
        public void updateData(List<Stock> data) {
            if (data != null && !data.isEmpty()) {
                datas.clear();
                datas.addAll(0, data);
                notifyDataSetChanged();
            }
        }

        /**
         * 改变某个点的值
         */
        public void changeLastItemClosePrice(float closePrice) {
            notifyLastItemChanged(closePrice);
        }

    }
}
