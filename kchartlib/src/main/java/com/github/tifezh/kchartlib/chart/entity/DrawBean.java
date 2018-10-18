package com.github.tifezh.kchartlib.chart.entity;

public class DrawBean {
   public int maxPos=0;//最高价所在的位置
   public int minPos=0;//最低价所在的位置
   public float centerX=0;//当前屏中心位置X的坐标
   public int mLastStartCalcuIndex;//上次计算的开始位置
   public int mLastEndCalcuIndex;//上次计算的结束位置
   public float mLastCalcuMainMaxValue;//上次计算的最大值
   public float mLastCalcuMainMinValue;//上次计算的最小值
   public float mMostRightY;//最右边点的Y坐标
   public float mMostRightX;//最右边点的X坐标
   public float mMostRightClosePrice;//最右边点的推算价格
}
