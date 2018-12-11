package com.gamerole.kchart;

import android.text.TextUtils;

import com.github.tifezh.kchartlib.chart.entity.IBOLL;
import com.github.tifezh.kchartlib.chart.entity.IKDJ;
import com.github.tifezh.kchartlib.chart.entity.IKLine;
import com.github.tifezh.kchartlib.chart.entity.IMACD;
import com.github.tifezh.kchartlib.chart.entity.IMinuteLine;
import com.github.tifezh.kchartlib.chart.entity.IRSI;
import com.github.tifezh.kchartlib.chart.entity.IWR;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/3.
 */

public class Stock implements IKLine, IMinuteLine, IMACD, IRSI, IKDJ, IBOLL, IWR {


    public float MA5Price;

    public float MA10Price;

    public float MA20Price;

    public float dea;

    public float dif;

    public float macd;

    public float k;

    public float d;

    public float j;

    public float rsi1;

    public float rsi2;

    public float rsi3;

    public float up;

    public float mb;

    public float dn;

    public float MA5Volume;

    public float MA10Volume;
    /**
     * id : 2271121
     * code : BTC/USDT
     * period :
     * volume : 1.73359092
     * price :
     * openPrice : 7611.414
     * closePrice : 7611.4262
     * prevClose :
     * high : 7615.667
     * low : 7610
     * date : 1533114360000
     * createTime :
     * isDeleted : 0
     * timestamp : 1533114400000
     */

    private int id;
    private String code;
    private String period;
    private double volume;
    private String price;
    private double openPrice;
    private double closePrice;
    private String prevClose;
    private double high;
    private double low;
    float wr;
    //    @SerializedName(value = "ewr")
    private long date;

    private String createTime;
    private int isDeleted;
    private long timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public float getVolume() {
        return (float) volume;
    }

    @Override
    public float getMA5Volume() {
        return MA5Volume;
    }

    @Override
    public float getMA10Volume() {
        return MA10Volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public float getAvgPrice() {
        return 0;
    }

    public float getPrice() {
        return TextUtils.isEmpty(price) ? 0 : Float.valueOf(price);
    }

    @Override
    public Date getMinuteDate() {
        return new Date(date);
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getOpenPrice() {
        return (float) openPrice;
    }

    @Override
    public float getHighPrice() {
        return (float) high;
    }

    @Override
    public float getLowPrice() {
        return (float) low;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public float getClosePrice() {
        return (float) closePrice;
    }

    @Override
    public void setClosePrice(float closePrice) {
        this.closePrice = closePrice;
    }

    @Override
    public float getMA5Price() {
        return MA5Price;
    }

    @Override
    public float getMA10Price() {
        return MA10Price;
    }

    @Override
    public float getMA20Price() {
        return MA20Price;
    }

    @Override
    public long getDatetime() {
        return date;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public String getPrevClose() {
        return prevClose;
    }

    public void setPrevClose(String prevClose) {
        this.prevClose = prevClose;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public float getLow() {
        return (float) low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public float getUp() {
        return up;
    }

    @Override
    public float getMb() {
        return mb;
    }

    @Override
    public float getDn() {
        return dn;
    }

    @Override
    public float getK() {
        return k;
    }

    @Override
    public float getD() {
        return d;
    }

    @Override
    public float getJ() {
        return j;
    }

    @Override
    public float getDea() {
        return dea;
    }

    @Override
    public float getDif() {
        return dif;
    }

    @Override
    public float getMacd() {
        return macd;
    }

    @Override
    public float getRsi1() {
        return rsi1;
    }

    @Override
    public float getRsi2() {
        return rsi2;
    }

    @Override
    public float getRsi3() {
        return rsi3;
    }

    @Override
    public float getWr() {
        return wr;
    }
}
/**
 * id : 2271121
 * code : BTC/USDT
 * period :
 * volume : 1.73359092
 * price :
 * openPrice : 7611.414
 * closePrice : 7611.4262
 * prevClose :
 * high : 7615.667
 * low : 7610
 * date : 1533114360000
 * createTime :
 * isDeleted : 0
 * timestamp : 1533114400000
 */

