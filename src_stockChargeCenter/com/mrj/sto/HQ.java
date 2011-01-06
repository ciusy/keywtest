package com.mrj.sto;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HQ {

    private Date earliestDate;
    private Date latestDate;
    private HashMap<Date, DHQ> dhqMap = new HashMap<Date, DHQ>();

    public HashMap<Date, DHQ> getDhqMap() {
        return dhqMap;
    }

    public Date getEarliestDate() {
        return earliestDate;
    }

    public void setEarliestDate(Date earliestDate) {
        this.earliestDate = earliestDate;
    }

    public Date getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(Date latestDate) {
        this.latestDate = latestDate;
    }

    public DHQ getDailyHQ(Date date) {
        return dhqMap.get(date);

    }

    public void putDHQ(Date date, DHQ dhq) {
        dhqMap.put(date, dhq);
    }

    /**
     * ����ָ��
     */
    public void calculateAllIndicator() {
        float[] priceArray = new float[dhqMap.size()];
        float[] chargeMoneyArray = new float[dhqMap.size()];
        long[] chargeVolumnArray = new long[dhqMap.size()];

        Object[] key = dhqMap.keySet().toArray();
        Arrays.sort(key);
        for (int i = 0; i < key.length; i++) {
            priceArray[i] = dhqMap.get(key[i]).getFinalPrice();//收盘价
            chargeMoneyArray[i] = dhqMap.get(key[i]).getChargeMoney();//成交额
            chargeVolumnArray[i] = dhqMap.get(key[i]).getChargeVolume();//成交量
        }

         float[] priceArray_5day = calculateIndicator(priceArray, 5);//计算5日均线
        float[] priceArray_10day = calculateIndicator(priceArray, 10);//计算10日均线
        float[] priceArray_20day = calculateIndicator(priceArray, 20);//计算20日均线
        float[] priceArray_30day = calculateIndicator(priceArray, 30);//计算30日均线
        float[] priceArray_60day = calculateIndicator(priceArray, 60);//计算60日均线
        float[] priceArray_180day = calculateIndicator(priceArray, 180);//计算180日均线

        long[] chargeVolumn_5day = calculateIndicator(chargeVolumnArray, 5);  //计算5日量均线
        long[] chargeVolumn_10day = calculateIndicator(chargeVolumnArray, 10);  //计算10日量均线
        long[] chargeVolumn_20day = calculateIndicator(chargeVolumnArray, 20);  //计算20日量均线

        for (int i = 0; i < dhqMap.size(); i++) {
            DHQ hdq = dhqMap.get((Date)key[i]);
            hdq.setPrice_5day(priceArray_5day[i]);
            hdq.setPrice_10day(priceArray_10day[i]);
            hdq.setPrice_20day(priceArray_20day[i]);
            hdq.setPrice_30day(priceArray_30day[i]);
            hdq.setPrice_60day(priceArray_60day[i]);
            hdq.setPrice_180day(priceArray_180day[i]);
            hdq.setVolume_5day(chargeVolumn_5day[i]);
            hdq.setVolume_10day(chargeVolumn_10day[i]);
            hdq.setVolume_20day(chargeVolumn_20day[i]);
        }

    }

    private float[] calculateIndicator(float[] priceArray, int days) {
        float[] re = new float[priceArray.length];
        for (int i = 0; i < re.length; i++) {
            float sum = 0;
            int j = 0;
            for (; j < days && i - j >= 0; j++) {
                sum += priceArray[i - j];
            }
            re[i] = sum / (j * 1.0f);
        }

        return re;
    }

    private long[] calculateIndicator(long[] priceArray, int days) {
        long[] re = new long[priceArray.length];
        for (int i = 0; i < re.length; i++) {
            long sum = 0;
            int j = 0;
            for (; j < days && i - j >= 0; j++) {
                sum += priceArray[i - j];
            }
            re[i] = (long) (sum / (j * 1.0d));
        }

        return re;
    }

    public float getPrice(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        while (true) {
            if (getDailyHQ(c.getTime()) != null) {
                return getDailyHQ(c.getTime()).getFinalPrice();
            } else {
                if (c.getTime().before(this.earliestDate)) {
                    return 0;
                } else {
                    c.add(Calendar.DAY_OF_YEAR, -1);
                }
            }
        }

    }
}
