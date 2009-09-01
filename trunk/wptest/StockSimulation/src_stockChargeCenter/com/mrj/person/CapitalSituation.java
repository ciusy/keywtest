package com.mrj.person;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mrj.charge.ChargeCenter;
import com.mrj.dm.dao.CapitalFlowDao;
import com.mrj.dm.domain.CapitalFlow;
import com.mrj.sto.Sto;

public class CapitalSituation {

    static Logger logger = Logger.getLogger(CapitalSituation.class);
    private CapitalFlowDao cfDao = new CapitalFlowDao();
    public Person ownerPerson = null;
    BigDecimal leftMoney;
    List<ShareHolding> holdingList = new ArrayList<ShareHolding>();

    public CapitalSituation(List<ShareHolding> holdingList, BigDecimal leftMoney) {
        this.holdingList = holdingList;
        this.leftMoney = leftMoney;
    }

    public BigDecimal getLeftMoney() {
        return leftMoney;
    }

    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }

    public List<ShareHolding> getHoldingList() {
        return holdingList;
    }

    public void setHoldingList(List<ShareHolding> holdingList) {
        this.holdingList = holdingList;
    }

    public Person getOwnerPerson() {
        return ownerPerson;
    }

    public void setOwnerPerson(Person p) {
        this.ownerPerson = p;
    }

    public BigDecimal getTotalAssets(Date date) {
        float reFloat = 0f;
        reFloat += this.leftMoney.floatValue();
        for (int i = 0; i < holdingList.size(); i++) {
            ShareHolding temp = holdingList.get(i);
            reFloat += temp.getAssets(date).floatValue();
        }
        BigDecimal re = new BigDecimal(reFloat);
        return re;
    }

    public int increaseSto(Sto sto, float plan_price, int operateAmount,Date date) {
        if (this.leftMoney.compareTo(new BigDecimal(operateAmount * plan_price)) > 0) {
            this.leftMoney = this.leftMoney.subtract(new BigDecimal(operateAmount * plan_price));
            boolean isAlreadyOwn = false;
            int i = 0;
            for (; i < holdingList.size(); i++) {
                ShareHolding shtemp = holdingList.get(i);
                if (shtemp.sto.getCode().equals(sto.getCode())) {
                    isAlreadyOwn = true;
                    break;
                }
            }
            if (isAlreadyOwn) {
                ShareHolding shtemp = holdingList.get(i);
                int alreadyMount = shtemp.hodingAmount;
                float preCostPrice = shtemp.getCostPrice();
                int nowMount = alreadyMount + operateAmount;
                float costPrice = (preCostPrice * alreadyMount + operateAmount * plan_price) / (nowMount * 1.0f);
                shtemp.setCostPrice(costPrice);
                shtemp.hodingAmount = nowMount;
            } else {
                ShareHolding shtemp = new ShareHolding();
                shtemp.setCostPrice(plan_price);
                shtemp.hodingAmount = operateAmount;
                shtemp.sto = sto;
                holdingList.add(shtemp);

            }
            logger.info("已经买入股票" + sto.getName() + operateAmount + "股，成交价格为" + plan_price + "元");
            cfDao.add(new CapitalFlow(getOwnerPerson().getUserUuid(), sto.getName(), CapitalFlow.sto_buy, date, plan_price, operateAmount, new BigDecimal(plan_price*operateAmount), leftMoney, new BigDecimal(0), sto.getCode()));
            return ChargeCenter.charge_succuss;
        } else {
            logger.error("余额不足以买入要求的股票");
            return ChargeCenter.charge_fail;
        }

    }

    /**
     * 卖出股票。
     * @param sto
     * @param plan_price
     * @param operateAmount参数应该保证卖出数量小于已有数量
     */
    public int reduceSto(Sto sto, float plan_price, int operateAmount,Date date) {
        ShareHolding shtemp_SellAll = null;
        boolean flag_isHodingThisSto = false;
        for (ShareHolding shtemp : holdingList) {
            if (shtemp.sto.getCode().equals(sto.getCode())) {
                if (shtemp.availableAmountForSell < operateAmount) {
                    logger.error("要求卖出的股票数量大于当前可卖股票的数量");
                    return ChargeCenter.charge_fail;
                } else {
                    flag_isHodingThisSto = true;
                    shtemp_SellAll = shtemp;
                    break;
                }
            }
        }
        if (flag_isHodingThisSto) {
            int alreadyMount = shtemp_SellAll.hodingAmount;
            int nowMount = alreadyMount - operateAmount;
            if (nowMount == 0) {
                holdingList.remove(shtemp_SellAll);
            } else {
                shtemp_SellAll.setCostPrice((shtemp_SellAll.getCostPrice() * alreadyMount - plan_price * operateAmount) / (nowMount * 1.0f));
                shtemp_SellAll.availableAmountForSell -= operateAmount;
                shtemp_SellAll.hodingAmount = nowMount;
            }
            this.leftMoney = this.leftMoney.add(new BigDecimal(operateAmount * plan_price));
            logger.info("已经卖出股票" + sto.getName() + operateAmount + "股，成交价格为" + plan_price + "元");
             cfDao.add(new CapitalFlow(getOwnerPerson().getUserUuid(), sto.getName(), CapitalFlow.sto_sell, date, plan_price, operateAmount, new BigDecimal(plan_price*operateAmount), leftMoney, new BigDecimal(0), sto.getCode()));
            return ChargeCenter.charge_succuss;
        } else {
            logger.error("要求卖出的股票不在当前已有股票中");
            return ChargeCenter.charge_fail;
        }





    }

    public void availableAmountForSellProcess() {
        if (holdingList != null) {
            for (ShareHolding shtemp : holdingList) {
                shtemp.availableAmountForSell = shtemp.hodingAmount;
            }
        }
    }
}
