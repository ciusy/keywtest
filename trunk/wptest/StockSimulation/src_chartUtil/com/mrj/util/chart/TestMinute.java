package com.mrj.util.chart;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.*;
import java.awt.Color;
import java.awt.Font;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JPanel;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;

public class TestMinute  {
    
    List ls = new ArrayList();//定义一个用来保存数据的集合类List
    double max_vol,min_vol,max_pr,min_pr; 
    Date firstt,lastt;
    Map map = null;//用来表示一条记录

public void clearList() {//清空保存数据的集合类List
        ls.clear();
 }
    
///该方法主要是为WEB端的JS调用,定义一个新的Map,插入一行空的记录
public void insertRecord()  {
       map = new HashMap();
}

///该方法主要是为WEB端的JS调用,为当前记录设置值
public void setValue(java.lang.String string, java.lang.String value)  {
       map.put(string, value);
}

///该方法主要是为WEB端的JS调用,把当前记录添加到记录集List里
public void postRecord()  {
       ls.add(map);
}

public double[] calavergerprice(int datenum,double[] val) {
     int ini_num = val.length;
     int size = ini_num-datenum+1;
     double[] res = new double[size];
     
     for(int i = 0;i<size;i++) {
         double tempval = 0.0; 
         for(int j = i;j<(i+datenum);j++) {
              tempval = tempval+val[j];
         }
          res[i] = tempval/datenum;
     }
     return res;
}    

//将数据库中数据读入ls集合中
	public void readData(java.lang.String stock_id,int record_num) {
    try {
      Connection con = null; 
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("select * from stockminute where stock_id = '"+stock_id+"' order by tran_time desc limit "+record_num);
      double[] temp_avg = new double[record_num];
      double[] vol_temp_avg = new double[record_num];
      int index = 0;
      int vol_index = 0;
      while(rs.next()) {
          temp_avg[index++] = rs.getDouble(7);
          vol_temp_avg[vol_index++] = rs.getDouble(8);
      }
      //5,10,30日均价
      double[] avg5 = this.calavergerprice(5, temp_avg);
      double[] avg10 = this.calavergerprice(10, temp_avg);
      double[] avg30 = this.calavergerprice(30, temp_avg);
      
      //5,10,30日均成交量
      double[] vol_avg5 = this.calavergerprice(5,vol_temp_avg);
      double[] vol_avg10 = this.calavergerprice(10,vol_temp_avg);
      double[] vol_avg30 = this.calavergerprice(30,vol_temp_avg);
      
      //设定最高和最低的总体价格和成交量的初始值（假定为第一条记录的内容）
      rs.first();
      this.max_pr = rs.getDouble(5);
      this.min_pr = rs.getDouble(6);
      this.max_vol = rs.getDouble(8);
      this.min_vol = this.max_vol;
      SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      this.lastt = sdf1.parse(rs.getDate(3).toString()+" "+rs.getTime(3).toString());
      Calendar cal = Calendar.getInstance();
      cal.setTime(lastt);
      cal.add(Calendar.MINUTE, 1);
      lastt=cal.getTime();
      //开始填充数据
      int stop = avg30.length;
      int num = 0;
      rs.beforeFirst();
      //从此结果集的第一行开始读取
      while(rs.next()) {
      if(num==stop) break;
      else {
      this.insertRecord();
      this.setValue("stock_name",rs.getString(2));
      this.setValue("issue_datetime", rs.getDate(3).toString()+" "+rs.getTime(3).toString());
      //System.out.println((rs.getDate(3).toString()+" "+rs.getTime(3)).toString());
      this.setValue("open_value", Double.toString(rs.getDouble(4)));
      this.setValue("high_value", Double.toString(rs.getDouble(5)));
      //判断是否大于现在的最高价
      if(rs.getDouble(5)>this.max_pr) this.max_pr = rs.getDouble(5);
      
      this.setValue("low_value",  Double.toString(rs.getDouble(6)));
      //判断是否小于现在的最低价
      if(rs.getDouble(6)<this.min_pr) this.min_pr = rs.getDouble(6);
      
      this.setValue("close_value",Double.toString(rs.getDouble(7)));
      this.setValue("volume_value", Double.toString(rs.getDouble(8)));
      
      //更新最高，最低成交量
      if(rs.getDouble(8)>this.max_vol)  this.max_vol = rs.getDouble(8);
      else if(rs.getDouble(8)<this.min_vol) this.min_vol = rs.getDouble(8);
      
      this.setValue("avg5", Double.toString(avg5[num]));
      this.setValue("avg10", Double.toString(avg10[num]));
      this.setValue("avg30", Double.toString(avg30[num]));
      this.setValue("vol_avg5", Double.toString(vol_avg5[num]));
      this.setValue("vol_avg10", Double.toString(vol_avg10[num]));
      this.setValue("vol_avg30", Double.toString(vol_avg30[num]));
      this.postRecord();
      num++;
      if(num==stop) {
          this.firstt = sdf1.parse(rs.getDate(3).toString()+" "+rs.getTime(3).toString());  
      }
      }
      }
      //DatabaseConnection.releaseConnection(con, stmt, rs);
    }
    catch(Exception e) {
        e.printStackTrace();
    }
}

public Map createDatasetMap(java.lang.String stock_id,int num)  {
                  try {
                  this.readData(stock_id,num);

                  //    从每一行记录里取出特定值，用来开成各种类型的均线和阴阳线图

                  Map m = new HashMap();

                  TimeSeries avg_lin5 = new TimeSeries("5分钟均线",
                                org.jfree.data.time.Day.class);
                  TimeSeries avg_lin10 = new TimeSeries("10分钟均线",
                                org.jfree.data.time.Day.class);
                  TimeSeries avg_lin30 = new TimeSeries("30分钟均线",
                                org.jfree.data.time.Day.class);
                  TimeSeries vol_avg_lin5 = new TimeSeries("5分钟成交量均线",
                                org.jfree.data.time.Day.class);
                  TimeSeries vol_avg_lin10 = new TimeSeries("10分钟成交量均线",
                                org.jfree.data.time.Day.class);
                  TimeSeries vol_avg_lin30 = new TimeSeries("30分钟成交量均线",
                                org.jfree.data.time.Day.class);

                  int count = ls.size();
                
                  Date adate[] = new Date[count];
                  double high[] = new double[count];
                  double low[] = new double[count];
                  double close[] = new double[count];
                  double open[] = new double[count];
                  double volume[] = new double[count];
                  
                  Date adate1[] = new Date[count];
                  double high1[] = new double[count];
                  double low1[] = new double[count];
                  double close1[] = new double[count];
                  double open1[] = new double[count];
                  double volume1[] = new double[count];
     
                  String stock_name = null;
                  Calendar cal = Calendar.getInstance();
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                  for (int j = 0; j < ls.size(); j++)  {

                         Map vMap = (Map) ls.get(j);
                         stock_name = (String) vMap.get("stock_name");
                         Date issue_datetime = sdf.parse((java.lang.String)vMap.get("issue_datetime"));
                         double open_value = Double.parseDouble(vMap.get("open_value")
                                       .toString());
                         double high_value = Double.parseDouble(vMap.get("high_value")
                                       .toString());
                         double low_value = Double.parseDouble(vMap.get("low_value")
                                       .toString());
                         double close_value = Double.parseDouble(vMap.get("close_value")
                                       .toString());
                         double avg5 = Double.parseDouble(vMap.get("avg5").toString());
                         double avg10 = Double.parseDouble(vMap.get("avg10").toString());
                         double avg30 = Double.parseDouble(vMap.get("avg30").toString());
                      
                         double volume_value = Double.parseDouble(vMap.get("volume_value")
                                       .toString());
                         double vol_avg5 = Double.parseDouble(vMap.get("vol_avg5")
                                       .toString());
                         double vol_avg10 = Double.parseDouble(vMap.get("vol_avg10")
                                       .toString());
                         double vol_avg30 = Double.parseDouble(vMap.get("vol_avg30")
                                 .toString());

                         cal.setTime(issue_datetime);

                         if (avg5 > 0.0D)
                                avg_lin5.addOrUpdate(new Day(cal.get(5), cal.get(2) + 1, cal.get(1)),
                                              avg5);
                         if (avg10 > 0.0D)
                                avg_lin10.addOrUpdate(new Day(cal.get(5), cal.get(2) + 1, cal.get(1)),
                                              avg10);
                         if (avg30 > 0.0D)
                                avg_lin30.addOrUpdate(new Day(cal.get(5), cal.get(2) + 1, cal.get(1)),
                                              avg30);
                         if (vol_avg5 > 0.0D)
                                vol_avg_lin5.addOrUpdate(
                                              new Day(cal.get(5), cal.get(2) + 1, cal.get(1)),
                                              vol_avg5);
                         if (vol_avg10 > 0.0D)
                                vol_avg_lin10.addOrUpdate(new Day(cal.get(5), cal.get(2) + 1, cal
                                              .get(1)), vol_avg10);
                         if (vol_avg30 > 0.0D)
                                vol_avg_lin30.addOrUpdate(new Day(cal.get(5), cal.get(2) + 1, cal
                                              .get(1)), vol_avg30);

                         adate[j] = issue_datetime;
                         high[j] = high_value;
                         low[j] = low_value;
                         close[j] = close_value;
                         open[j] = open_value;
                         volume[j] = 0.0D;
                         adate1[j] = issue_datetime;
                         high1[j] = 0.0D;
                         low1[j] = 0.0D;
                         close1[j] = 0.0D;
                         open1[j] = 0.0D;

//    这里是我们用蜡烛图来构造与阴阳线对应的成交量图，我们主要是通过判断开盘价与收盘价相比较的值来决定到底是在表示成交量的蜡烛图的开盘价设置值还是收盘价设置值，设置之前我们把它们全部都设置为0
                         if (open_value < close_value)
                                close1[j] = volume_value;
                         else
                                open1[j] = volume_value;

                         volume1[j] = 0.0D;
                  }

                  DefaultHighLowDataset k_line = new DefaultHighLowDataset("",
                                adate, high, low, close, open, volume);
                  DefaultHighLowDataset vol = new DefaultHighLowDataset(
                                "", adate1, high1, low1, close1, open1,
                                volume1);

//    把各种类型的图表对象放到Map里，以为其它方法提供使用
                  m.put("k_line", k_line);
                  m.put("vol", vol);
                  m.put("stock_name", stock_name);
                  m.put("avg_line5", new TimeSeriesCollection(avg_lin5));
                  m.put("avg_line10", new TimeSeriesCollection(avg_lin10));
                  m.put("avg_line30", new TimeSeriesCollection(avg_lin30));
                  m.put("vol_avg_line5", new TimeSeriesCollection(vol_avg_lin5));
                  m.put("vo_avg_line10", new TimeSeriesCollection(vol_avg_lin10));
                  m.put("vol_avg_line30", new TimeSeriesCollection(vol_avg_lin30));

                  return m;
                  }
                  catch(Exception e) {
                      e.printStackTrace();
                      return null;
                  }
           }

//--------------------------------------------HERE WORKING----------------------------------------------------------
public JFreeChart createCombinedChart(java.lang.String stock_id,int num)  {

    Map m = this.createDatasetMap(stock_id,num);//从数据对象里取出各种类型的对象，主要是用来表示均线的时间线(IntervalXYDataset)对象和用来表示阴阳线和成交量的蜡烛图对象(OHLCDataset)
    IntervalXYDataset avg_line5 = (IntervalXYDataset) m.get("avg_line5");
    IntervalXYDataset avg_line10 = (IntervalXYDataset) m.get("avg_line10");
    IntervalXYDataset avg_line30 = (IntervalXYDataset) m.get("avg_line30");
    IntervalXYDataset vol_avg_line5 = (IntervalXYDataset) m.get("vol_avg_line5");
    IntervalXYDataset vol_avg_line10 = (IntervalXYDataset) m.get("vol_avg_line10");
    IntervalXYDataset vol_avg_line30 = (IntervalXYDataset) m.get("vol_avg_line30");

    OHLCDataset k_line = (OHLCDataset) m.get("k_line");
    OHLCDataset vol = (OHLCDataset) m.get("vol");

    java.lang.String stock_name = (java.lang.String) m.get("stock_name");

//设置若干个时间线的Render，目的是用来让几条均线显示不同的颜色，和为时间线加上鼠标提示
    XYLineAndShapeRenderer xyLineRender = new XYLineAndShapeRenderer(true,
                  false);
    xyLineRender.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
                  "{0}: ({1}, {2})", new SimpleDateFormat("HH:mm"),
                  new DecimalFormat("0.00")));
    xyLineRender.setSeriesPaint(0, Color.red);

    XYLineAndShapeRenderer xyLineRender1 = new XYLineAndShapeRenderer(true,
                  false);
    xyLineRender1.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
                 "{0}: ({1}, {2})", new SimpleDateFormat("HH:mm"),
                  new DecimalFormat("0.00")));
    xyLineRender1.setSeriesPaint(0, Color.BLACK);

    XYLineAndShapeRenderer xyLineRender2 = new XYLineAndShapeRenderer(true,
                  false);
    xyLineRender2.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
                "{0}: ({1}, {2})", new SimpleDateFormat("HH:mm"),
                  new DecimalFormat("0.00")));
    xyLineRender2.setSeriesPaint(0, Color.blue);


//定义K线图下半截显示的Plot
    NumberAxis y1Axis = new NumberAxis("");// 设定y轴，就是数字轴
    y1Axis.setAutoRange(false);// 不使用自动设定范围
    
    int intMin_vol = (int)(min_vol/5);
    int intMax_vol = (int)((max_vol*1.1)/5);
    
    int tempMin_vol = intMin_vol*5 - 5;
    int tempMax_vol = intMax_vol*5 + 5;
    
    y1Axis.setRange(min_vol*0.9, max_vol*1.1);// 设定y轴值的范围，比最低值要低一些，比最大值要大一些，这样图形看起来会美观些
    y1Axis.setTickUnit(new NumberTickUnit(
            (max_vol*1.1 - min_vol*0.9) / 5));// 设置刻度显示的密度
    
    y1Axis.setRange(tempMin_vol, tempMax_vol);// 设定y轴值的范围，比最低值要低一些，比最大值要大一些，这样图形看起来会美观些
    y1Axis.setTickUnit(new NumberTickUnit(
            (tempMax_vol - tempMin_vol) / 5));// 设置刻度显示的密度
    Font f = new Font("SansSerif",Font.PLAIN,5);
    y1Axis.setLabelFont(f);
    
    XYPlot volPlot = new XYPlot(vol_avg_line5, null, y1Axis, xyLineRender1);
    
//定义一个CandlestickRenderer给蜡烛图对象使用，目的是对蜡烛图对象的显示进行调整，这里主要是调整它显示的宽度并加上鼠标提示

    CandlestickRenderer candlesRender = new CandlestickRenderer();

    candlesRender.setCandleWidth(4D);

    candlesRender.setBaseToolTipGenerator(new StandardXYToolTipGenerator(

             "{1},{2}", new SimpleDateFormat("HH:mm"),

                   new DecimalFormat("0.00")));
    



//把其它的几个Dataset加到上半截要显示的Plot里，并同时设置它们所采用的Render，以形成一个叠加显示的效果

    volPlot.setDataset(1, vol_avg_line10);

    volPlot.setRenderer(1, xyLineRender2);
    
    volPlot.setDataset(2, vol_avg_line30);

    volPlot.setRenderer(2, xyLineRender);

    volPlot.setDataset(3, vol);

    volPlot.setRenderer(3, candlesRender);

 // 定义K线图上半截显示的Plot
    NumberAxis y2Axis = new NumberAxis("");
    y2Axis.setAutoRange(false);// 不不使用自动设定范围
    
    int intMin_pr = (int)(min_pr/5);
    int intMax_pr = (int)(max_pr/5);
    
    int tempMin_pr = intMin_pr*5 - 5;
    int tempMax_pr = intMax_pr*5 + 5;
    
//    y2Axis.setRange(min_pr*0.9, max_pr*1.1);// 设定y轴值的范围，比最低值要低一些，比最大值要大一些，这样图形看起来会美观些
//    y2Axis.setTickUnit(new NumberTickUnit(
//            (max_pr*1.1 - min_pr*0.9) / 5));// 设置刻度显示的密度
    
    y2Axis.setRange(tempMin_pr, tempMax_pr);// 设定y轴值的范围，比最低值要低一些，比最大值要大一些，这样图形看起来会美观些
    y2Axis.setTickUnit(new NumberTickUnit(
            (tempMax_pr - tempMin_pr) / 5));// 设置刻度显示的密度
    
    XYPlot candlePlot = new XYPlot(k_line, null, y2Axis , candlesRender);
    candlePlot.setDataset(1, avg_line5);
    candlePlot.setRenderer(1, xyLineRender1);
    candlePlot.setDataset(2, avg_line10);
    candlePlot.setRenderer(2, xyLineRender2);
    candlePlot.setDataset(3, avg_line30);
    candlePlot.setRenderer(3, xyLineRender);
   

    DateAxis dateaxis = new DateAxis("K线图");

 //   dateaxis.setTickUnit(new DateTickUnit(4, 60, new SimpleDateFormat(

 //                 "HH:mm")));//设置日期格式及显示日期的间隔
    dateaxis.setAutoRange(false);//设置不采用自动设置时间范围
    try {
        dateaxis.setRange(firstt,lastt);//设置时间范围，注意时间的最大值要比已有的时间最大值要多一天
    }catch(Exception e) {
     e.printStackTrace();
    }
    dateaxis.setTimeline(new SegmentedTimeline(30*60*1000,4,3));//设置时间线显示的规则，用这个方法就摒除掉了周六和周日这些没有交易的日期(很多人都不知道有此方法)，使图形看上去连续
    dateaxis.setAutoTickUnitSelection(false);//设置不采用自动选择刻度值
    dateaxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);//设置标记的位置
    dateaxis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());//设置标准的时间刻度单位
    dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.MINUTE,30));//设置时间刻度的间隔，一般以周为单位
    dateaxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));//设置显示时间的格式
  
    dateaxis.setLowerMargin(0.0D);
    dateaxis.setUpperMargin(0.02D);

//定义一个复合类型的Plot，目的是为了把Chart的上半截和下半截结合起来，形成一张完整的K线图
    CombinedDomainXYPlot combineXY = new CombinedDomainXYPlot(dateaxis);

    //把上下两个Plot都加到复合Plot里，并设置它们在图中所占的比重
    combineXY.add(candlePlot, 3);
    combineXY.add(volPlot, 1);
    combineXY.setGap(8D);
    combineXY.setDomainGridlinesVisible(true);

    JFreeChart jfreechart = new JFreeChart(stock_name,JFreeChart.DEFAULT_TITLE_FONT, combineXY, false);
  //  ChartPanel cPanel = new ChartPanel(jfreechart);
  
    
    jfreechart.setBackgroundPaint(Color.white);

//为Chart图添加一个图例，这里我们可以定义需要显示的一些信息，及图例放置的位置，我们选择的顶部
    LegendTitle legendtitle = new LegendTitle(candlePlot);
    BlockContainer blockcontainer = new BlockContainer(
                  new BorderArrangement());
    blockcontainer.setFrame(new BlockBorder(0.10000000000000001D,
                  0.10000000000000001D, 0.10000000000000001D,
                  0.10000000000000001D));
    BlockContainer blockcontainer1 = legendtitle.getItemContainer();
    blockcontainer1.setPadding(2D, 10D, 5D, 2D);
    blockcontainer.add(blockcontainer1);
    legendtitle.setWrapper(blockcontainer);
    legendtitle.setPosition(RectangleEdge.TOP);
    legendtitle.setHorizontalAlignment(HorizontalAlignment.CENTER);

    jfreechart.addSubtitle(legendtitle);

    return jfreechart;

}


public static void main(java.lang.String[] args)  {
        TestMinute t = new TestMinute();
        java.lang.String stock_id = "SZ000001";
        int num = 240;
        JFreeChart chart = t.createCombinedChart(stock_id,num);
        ChartFrame chartFrame = new ChartFrame(chart.getTitle().getText(),chart);
        chartFrame.pack();
        chartFrame.setVisible(true);
}
}

