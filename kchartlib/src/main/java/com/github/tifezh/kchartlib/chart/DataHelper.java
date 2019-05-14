package com.github.tifezh.kchartlib.chart;


import com.github.tifezh.kchartlib.chart.entity.IBOLL;
import com.github.tifezh.kchartlib.chart.entity.ICandle;
import com.github.tifezh.kchartlib.chart.entity.IKDJ;
import com.github.tifezh.kchartlib.chart.entity.IMACD;
import com.github.tifezh.kchartlib.chart.entity.IRSI;
import com.github.tifezh.kchartlib.chart.entity.IVolume;
import com.github.tifezh.kchartlib.chart.entity.IWR;

import java.util.List;

/**
 * 数据辅助类 计算macd rsi等
 * Created by tifezh on 2016/11/26.
 */

public class DataHelper {

    /**
     * 计算RSI
     *
     * @param datas
     */
    public static void calculateRSI(List<? extends IRSI> datas) {
        float rsi1 = 0;
        float rsi2 = 0;
        float rsi3 = 0;
        float rsi1ABSEma = 0;
        float rsi2ABSEma = 0;
        float rsi3ABSEma = 0;
        float rsi1MaxEma = 0;
        float rsi2MaxEma = 0;
        float rsi3MaxEma = 0;
        for (int i = 0; i < datas.size(); i++) {
            IRSI point = datas.get(i);
            final float closePrice = point.getClosePrice();
            if (i == 0) {
                rsi1 = 0;
                rsi2 = 0;
                rsi3 = 0;
                rsi1ABSEma = 0;
                rsi2ABSEma = 0;
                rsi3ABSEma = 0;
                rsi1MaxEma = 0;
                rsi2MaxEma = 0;
                rsi3MaxEma = 0;
            } else {
                float Rmax = Math.max(0, closePrice - datas.get(i - 1).getClosePrice());
                float RAbs = Math.abs(closePrice - datas.get(i - 1).getClosePrice());
                rsi1MaxEma = (Rmax + (6f - 1) * rsi1MaxEma) / 6f;
                rsi1ABSEma = (RAbs + (6f - 1) * rsi1ABSEma) / 6f;

                rsi2MaxEma = (Rmax + (12f - 1) * rsi2MaxEma) / 12f;
                rsi2ABSEma = (RAbs + (12f - 1) * rsi2ABSEma) / 12f;

                rsi3MaxEma = (Rmax + (24f - 1) * rsi3MaxEma) / 24f;
                rsi3ABSEma = (RAbs + (24f - 1) * rsi3ABSEma) / 24f;

                rsi1 = (rsi1MaxEma / rsi1ABSEma) * 100;
                rsi2 = (rsi2MaxEma / rsi2ABSEma) * 100;
                rsi3 = (rsi3MaxEma / rsi3ABSEma) * 100;
            }
            point.setRsi1(rsi1);
            point.setRsi2(rsi2);
            point.setRsi3(rsi3);
        }
    }

    /**
     * 计算kdj
     *
     * @param datas
     */
    public static void calculateKDJ(List<? extends IKDJ> datas) {
        float k = 0;
        float d = 0;

        for (int i = 0; i < datas.size(); i++) {
            IKDJ point = datas.get(i);
            final float closePrice = point.getClosePrice();
            int startIndex = i - 8;
            if (startIndex < 0) {
                startIndex = 0;
            }
            float max9 = Float.MIN_VALUE;
            float min9 = Float.MAX_VALUE;
            for (int index = startIndex; index <= i; index++) {
                max9 = Math.max(max9, datas.get(index).getHighPrice());
                min9 = Math.min(min9, datas.get(index).getLowPrice());

            }
            float rsv = 100f * (closePrice - min9) / (max9 - min9);
            if (Float.isNaN(rsv)) {
                point.setK(0);
                point.setD(0);
                point.setJ(0);
                continue;
            }
            if (i == 0) {
                k = rsv;
                d = rsv;
            } else {
                k = (rsv + 2f * k) / 3f;
                d = (k + 2f * d) / 3f;
            }
            point.setK(k);
            point.setD(d);
            point.setJ(3f * k - 2 * d);
        }

    }

    /**
     * 计算macd
     *
     * @param datas
     */
    public static void calculateMACD(List<? extends IMACD> datas) {
        float ema12 = 0;
        float ema26 = 0;
        float dif = 0;
        float dea = 0;
        float macd = 0;

        for (int i = 0; i < datas.size(); i++) {
            IMACD point = datas.get(i);
            final float closePrice = point.getClosePrice();
            if (i == 0) {
                ema12 = closePrice;
                ema26 = closePrice;
            } else {
//                EMA（12） = 前一日EMA（12） X 11/13 + 今日收盘价 X 2/13
//                EMA（26） = 前一日EMA（26） X 25/27 + 今日收盘价 X 2/27
                ema12 = ema12 * 11f / 13f + closePrice * 2f / 13f;
                ema26 = ema26 * 25f / 27f + closePrice * 2f / 27f;
            }
//            DIF = EMA（12） - EMA（26） 。
//            今日DEA = （前一日DEA X 8/10 + 今日DIF X 2/10）
//            用（DIF-DEA）*2即为MACD柱状图。
            dif = ema12 - ema26;
            dea = dea * 8f / 10f + dif * 2f / 10f;
            macd = (dif - dea) * 2f;
            point.setDif(dif);
            point.setDea(dea);
            point.setMacd(macd);
        }

    }

    /**
     * 计算 BOLL 需要在计算ma之后进行
     *
     * @param datas
     */
    public static void calculateBOLL(List<? extends IBOLL> datas) {
        for (int i = 0; i < datas.size(); i++) {
            IBOLL point = datas.get(i);
            final float closePrice = point.getClosePrice();
            if (i == 0) {
                point.setMb(closePrice);
                point.setUp(Float.NaN);
                point.setDn(Float.NaN);
            } else {
                int n = 20;
                if (i < 20) {
                    n = i + 1;
                }
                float md = 0;
                for (int j = i - n + 1; j <= i; j++) {
                    float c = datas.get(j).getClosePrice();
                    float m = point.getMA20Price();
                    float value = c - m;
                    md += value * value;
                }
                md = md / (n - 1);
                md = (float) Math.sqrt(md);
                point.setMb(point.getMA20Price());
                point.setUp(point.getMb() + 2f * md);
                point.setDn(point.getMb() - 2f * md);
            }
        }

    }

    /**
     * 计算ma
     *
     * @param datas
     */
    public static void calculateMA(List<? extends ICandle> datas) {
        float ma5 = 0;
        float ma10 = 0;
        float ma20 = 0;

        for (int i = 0; i < datas.size(); i++) {
            ICandle point = datas.get(i);
            final float closePrice = point.getClosePrice();

            ma5 += closePrice;
            ma10 += closePrice;
            ma20 += closePrice;
            if (i >= 5) {
                ma5 -= datas.get(i - 5).getClosePrice();
                point.setMA5Price(ma5 / 5f);
            } else {
                point.setMA5Price(ma5 / (i + 1f));
            }
            if (i >= 10) {
                ma10 -= datas.get(i - 10).getClosePrice();
                point.setMA10Price(ma10 / 10f);
            } else {
                point.setMA10Price(ma10 / (i + 1f));
            }
            if (i >= 20) {
                ma20 -= datas.get(i - 20).getClosePrice();
                point.setMA20Price(ma20 / 20f);
            } else {
                point.setMA20Price(ma20 / (i + 1f));
            }
        }
    }


    public static void calculateVolumeMA(List<? extends IVolume> entries) {
        float volumeMa5 = 0;
        float volumeMa10 = 0;

        for (int i = 0; i < entries.size(); i++) {
            IVolume entry = entries.get(i);

            volumeMa5 += entry.getVolume();
            volumeMa10 += entry.getVolume();

            if (i >= 5) {

                volumeMa5 -= entries.get(i - 5).getVolume();
                entry.setMA5Volume((volumeMa5 / 5f));
            } else {

                entry.setMA5Volume((volumeMa5 / (i + 1f)));
            }

            if (i >= 10) {
                volumeMa10 -= entries.get(i - 10).getVolume();
                entry.setMA10Volume((volumeMa10 / 5f));
            } else {
                entry.setMA10Volume((volumeMa10 / (i + 1f)));
            }
        }
    }

    /**
     * 计算WR
     *
     * @param datas
     */
    public static void calculateWR(List<? extends IWR> datas) {
        for (int i = 0; i < datas.size(); i++) {
            IWR point = datas.get(i);
            int startIndex = i - 8;
            if (startIndex < 0) {
                startIndex = 0;
            }
            //9日最高价
            float max9 = Float.MIN_VALUE;
            //9日最低价
            float min9 = Float.MAX_VALUE;

            for (int index = startIndex; index <= i; index++) {
                max9 = Math.max(max9, datas.get(index).getHighPrice());
                min9 = Math.min(min9, datas.get(index).getLowPrice());
            }
            //当日收盘价
            final float closePrice = point.getClosePrice();
            float wr = 100f * (max9 - closePrice) / (max9 - min9);
            point.setWr(wr);
        }
    }
}