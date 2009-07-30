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
            priceArray[i] = dhqMap.get(key[i]).getFinalPrice();
            chargeMoneyArray[i] = dhqMap.get(key[i]).getChargeMoney();
            chargeVolumnArray[i] = dhqMap.get(key[i]).getChargeVolume();
        }

        float[] priceArray_5day = calculateIndicator(priceArray, 5);
        long[] chargeVolumn_5day = calculateIndicator(chargeVolumnArray, 5);

        for (int i = 0; i < dhqMap.size(); i++) {
            dhqMap.get(key[i]).setPrice_5day(priceArray_5day[i]);
            dhqMap.get(key[i]).setVolume_5day(chargeVolumn_5day[i]);
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
