package com.mrj.policy;

import com.mrj.person.Person;
import com.mrj.person.ShareHolding;
import com.mrj.policy.util.StoConfidenceValuePair;
import com.mrj.sto.OriginalDataUtil;
import com.mrj.sto.Sto;

import java.util.*;

/**
 *通用公式描述 选股策略
 * 两个公式指标
 * 1. n日线上穿m日线
 * 2. 阶段放量， 阶段缩量 
 *
 * User: Clyde
 * Date: Nov 8, 2010
 * Time: 2:56:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeneralFormularyPolicy extends ChoosePolicy{
    private int chargeDescription_size = 3;// 选出的股票数量，默认为最多1000个。
    Condition avgCondition = null;

    public void setChargeDescription_size(int chargeDescription_size) {
        this.chargeDescription_size = chargeDescription_size;
    }

    Condition volCondition = null;

    Condition sellCondition = null;
    
    private String formulary;
    /**
     * 构造函数
     * @param formulary   公式说明
     * 公式格式 ： n日线上穿m日线;阶段?量    . 例如："选股数量上限为3个;5日线上穿60日线;阶段放量;盈利20%或者亏损20%卖出"的公式为
     * "chargeDescription_size:3;avg:5,60,up;vol:5,10,5,放量;sell:up,20,down,20"
     */
    public GeneralFormularyPolicy(String formulary){
        this.formulary = formulary;
        //step1: split by ;
        String[] condition_array = formulary.split(";");
        for(String condition: condition_array){
            //step2: split by :
            String key = condition.split(":")[0];
            String value = condition.split(":")[1];

            if(key.equalsIgnoreCase("chargeDescription_size")){
                this.chargeDescription_size = Integer.valueOf(value);
            }else{
                //step3: split by ,
                String[] param_array = value.split(",");
                if(key.equalsIgnoreCase("avg")){
                    this.avgCondition = new Condition("avg",param_array);
                }else if(key.equalsIgnoreCase("vol")){
                    this.volCondition = new Condition("vol",param_array);
                }else if(key.equalsIgnoreCase("sell")){
                    this.sellCondition = new Condition("sell",param_array);
                }
            }
        }

    }

    /**
     * 默认情况下，使用如下的策略  "5日线上穿60日线且阶段放量"
     */

    public GeneralFormularyPolicy(){
        avgCondition = new Condition("avg",new String[]{"5","60","up"});   // 5日线 上穿 60日线
        volCondition = new Condition("vol",new String[]{"5","10","5","放量"});   // 最近2日的均量大于最近10日均量的130%    ---  阶段放量
    }


    /**
     *
     *
     * avg:5,60,up,longTerm,60
     *
     * 如果有longTerm， 则是在判断中加入长期概念。 即，例如   5日线长期在60日线以下，然后上穿60日线   --  长期的意思，初步定为60个交易日
     *
     * //todo 排除掉最近60天长期停牌的股票
     * //todo 排除掉st股票
     * //todo 排除掉近期下跌非常多的股票，因为考虑一个因素：下跌有多深，横盘有多长
     * 均线条件判断
     * @param sto
     * @param nextChargeDay
     * @return
     */
    private boolean avgConditionJudgement(Sto sto, Calendar nextChargeDay){
        String firstAvg = avgCondition.paramValues[0];
        String secondAvg = avgCondition.paramValues[1];
        String upOrDown = avgCondition.paramValues[2];

		float lastDayPrice_firstAvgDay = getLastDayPrice(sto, nextChargeDay, "getPrice_" + firstAvg + "day");
		float lastDayPrice_secondAvgDay = getLastDayPrice(sto, nextChargeDay, "getPrice_" + secondAvg + "day");

		Calendar c1 = Calendar.getInstance();
		c1.setTime(nextChargeDay.getTime());
		c1.add(Calendar.DAY_OF_YEAR, -1);
		float lastDayPrice_firstAvgDay_1 = getLastDayPrice(sto, c1, "getPrice_" + firstAvg + "day");
		float lastDayPrice_secondAvgDay_1 = getLastDayPrice(sto, c1, "getPrice_" + secondAvg + "day");

        if(upOrDown.equalsIgnoreCase("up")){ //上穿
            if(!(lastDayPrice_firstAvgDay_1<lastDayPrice_secondAvgDay_1&&lastDayPrice_firstAvgDay>lastDayPrice_secondAvgDay) )return false;
        }else{ //下穿
            if(!(lastDayPrice_firstAvgDay_1>lastDayPrice_secondAvgDay_1&&lastDayPrice_firstAvgDay<lastDayPrice_secondAvgDay) )return false;
        }

        //长期条件判断
        if (avgCondition.paramValues.length > 3) {
            int longTerm = Integer.valueOf(avgCondition.paramValues[4]);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(nextChargeDay.getTime());
            while(longTerm>0){ //假设 longTerm == 60， 则判断过去的60个交易日内，是否运行在某均线的下法或者上方
                longTerm--;
                c2.add(Calendar.DAY_OF_YEAR, -1);
                float lastDayPrice_firstAvgDay_temp = getLastDayPrice(sto, c2, "getPrice_" + firstAvg + "day");
                float lastDayPrice_secondAvgDay_temp = getLastDayPrice(sto, c2, "getPrice_" + secondAvg + "day");

                if (upOrDown.equalsIgnoreCase("up")) { //上穿
                    if (!(lastDayPrice_firstAvgDay_temp < lastDayPrice_secondAvgDay_temp))
                        return false;
                } else { //下穿
                    if (!(lastDayPrice_firstAvgDay_temp > lastDayPrice_secondAvgDay_1))
                        return false;
                }
            }
        }


        return true;
    }

    /**
     * 成交量条件判断
     *
     * volCondition = new Condition("vol",new String[]{"5","10","10","放量"});   // 最近5日的均量大于最近10日均量的110%    ---  阶段放量
     * volCondition = new Condition("vol",new String[]{"5","10","10","缩量"});   // 最近5日的均量小于最近10日均量的90%    ---  阶段缩量
     *
     * @param sto
     * @param nextChargeDay
     * @return
     */
    private boolean volConditionJudgement(Sto sto, Calendar nextChargeDay){
        int recentPeriod = Integer.valueOf(volCondition.paramValues[0]);  //最近几天作为阶段
        int wholePeriod = Integer.valueOf(volCondition.paramValues[1]); //整个考察周期是多少天
        float volDifference = Float.valueOf(volCondition.paramValues[2]);//量的差值
        String upOrDown = volCondition.paramValues[3];  //放量还是缩量

		float lastDayPrice_recentPeriodVol = getLastDayPrice(sto, nextChargeDay, "getVolume_" + recentPeriod + "day");
		float lastDayPrice_wholePeriodVol = getLastDayPrice(sto, nextChargeDay, "getVolume_" + wholePeriod + "day");

        if(upOrDown.equalsIgnoreCase("放量")){ //放量
            if((lastDayPrice_recentPeriodVol-lastDayPrice_wholePeriodVol)/lastDayPrice_wholePeriodVol *100.0 >= volDifference )return true;
        }else{ //缩量
            if((lastDayPrice_wholePeriodVol-lastDayPrice_recentPeriodVol)/lastDayPrice_wholePeriodVol *100.0 <= volDifference )return true;
        }

        return false;
    }


    /**
     * 执行在本类所设置的选股条件，判断一只股票是否可以被选出来
     * @param sto
     * @param nextChargeDay
     * @return 如果能够选出，则返回true
     */
    private boolean canBuy(Sto sto, Calendar nextChargeDay) {
         if(sto.getCode().indexOf("002389")>=0){
            System.out.println(sto.getCode());
        }
        if (!avgConditionJudgement(sto, nextChargeDay)) {
            return false;
        }
        if (!volConditionJudgement(sto, nextChargeDay)) {
            return false;
        }
        return true;

    }

    /**
     * 给出对一些股票的信息值List
     *
     * @param nextChargeDay
     * @return
     */
    @Override
    public List<StoConfidenceValuePair> getBuyList(Calendar nextChargeDay) {
        List<StoConfidenceValuePair> re = new ArrayList<StoConfidenceValuePair>();

		List<Sto> allStoList = OriginalDataUtil.getAllStoList();

		for (Sto sto : allStoList) {
			try {
				if (canBuy(sto, nextChargeDay))
					//re.add(new StoConfidenceValuePair(sto, (float) (0.8f + 0.2 * Math.random())));
                re.add(new StoConfidenceValuePair(sto, (float) (1f))); //这里的信心值暂时没有用到 

			} catch (Exception e) {
				logger.error("", e);
			}
		}
		StoConfidenceValuePair[] reArray = new StoConfidenceValuePair[re.size()];
		re.toArray(reArray);
		Arrays.sort(reArray);
		List<StoConfidenceValuePair> re_sort = new ArrayList<StoConfidenceValuePair>();
		re_sort = Arrays.asList(reArray);
		List<StoConfidenceValuePair> re_final = new ArrayList<StoConfidenceValuePair>();
		for (int i = re_sort.size() - 1 - chargeDescription_size; i < re_sort.size(); i++) {
			if (i < 0) {
				i = -1;
				continue;
			}
			re_final.add(re_sort.get(i));
		}
		return re_final;
    }

    /**
     * 给出已有仓位中股票的信心值List
     *
     * @param nextChargeDay 下一交易日
     * @return
     */
    @Override
    public List<StoConfidenceValuePair> getOwnStoList(Calendar nextChargeDay) {
        List<StoConfidenceValuePair> re = new ArrayList<StoConfidenceValuePair>();
        for (ShareHolding sh : getOwnerPerson().getCs().getHoldingList()) {
            if (cansell(sh.getSto(), nextChargeDay)) {
                re.add(new StoConfidenceValuePair(sh.getSto(), 0));
            }
        }
        return re;
    }

    /**
     * todo --  判断在一段时间内，如果盈利没有超过某一值或者亏损没有低于某一值，则卖出股票。
     * todo --  如果小于30线大于60日线且盈利，则卖出
     * 
     * 判断在nextChargeDay卖出股票sto，是否满足sellCondition.  如果满足，返回true
     * e.g.  sell:up,20,down,20
     * @param sto
     * @param nextChargeDay
     */
    private boolean sellConditionJudgement(Sto sto, Calendar nextChargeDay){
        float profitPercent =Float.valueOf( sellCondition.paramValues[1]);
        float lostPercent = -Float.valueOf(sellCondition.paramValues[3]);
        float lastDayPrice = getLastDayFinalPrice(sto, nextChargeDay);
        float costPrice=this.getOwnerPerson().getCs().getHoldingMap().get(sto.getCode()).getCostPrice();
        if((lastDayPrice-costPrice)/costPrice<lostPercent/100.0 || (lastDayPrice-costPrice)/costPrice>profitPercent/100.0)  {
            return true;    
        }

        return false;
    }

    /**
     * 卖出策略
     * @param sto
     * @param nextChargeDay
     * @return
     */
    private boolean cansell(Sto sto, Calendar nextChargeDay) {
        if (!sellConditionJudgement(sto, nextChargeDay)) {
            return false;
        }
        return true;
    }

    @Override
    public Object getInstanceWithPropertiesList(ArrayList<Properties> list) {
        GeneralFormularyPolicy re = new GeneralFormularyPolicy();
		// TODO Auto-generated method stub
		return re;
    }

    public static ChoosePolicy getInstance(String fullargs) {
        return new GeneralFormularyPolicy(fullargs);

    }

    @Override
    public String getFullargs() {
        return this.formulary;
    }

    public static void main(String[] args){
       GeneralFormularyPolicy gfp =  new GeneralFormularyPolicy("chargeDescription_size:5;avg:5,60,up;vol:5,10,5,放量;sell:up,20,down,20");
        gfp.getFullargs();
    }
}

/**
 * 选股条件的抽象。
 */
class Condition {
    String name;//选股条件的名称
    String[] paramValues;

    public Condition(String name, String[] paramValues){
        this.name = name;
        this.paramValues =paramValues;
    }
}