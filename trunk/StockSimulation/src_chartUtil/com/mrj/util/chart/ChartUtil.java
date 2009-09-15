package com.mrj.util.chart;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.mrj.dm.dao.AssetDayDataDao;
import com.mrj.dm.domain.AssetDayData;

import demo.CandlestickChartDemo1;

public class ChartUtil {

	@SuppressWarnings( { "deprecation", "unchecked" })
	public static JFreeChart getAssetChart(String person_uuid) {
		TimeSeries daytime = new TimeSeries("assetChart for user " + person_uuid, org.jfree.data.time.Day.class);
		List<AssetDayData> dataList = new AssetDayDataDao().getAssetListByUserUuid(person_uuid);
		AssetDayData temp = null;
		double tempMax_vol=0;
		for (int i = 0; i < dataList.size(); i++) {
			temp = dataList.get(i);
			daytime.addOrUpdate(new Day(temp.getDateTime()), temp.getAssetValue());
			if(temp.getAssetValue()>tempMax_vol)tempMax_vol=temp.getAssetValue();
		}
		IntervalXYDataset dataset = new TimeSeriesCollection(daytime);
		NumberAxis y1Axis = new NumberAxis("");// 设定y轴，就是数字轴
		y1Axis.setAutoRange(false);// 不使用自动设定范围
		
		y1Axis.setRange(0, tempMax_vol);// 设定y轴值的范围，比最低值要低一些，比最大值要大一些，这样图形看起来会美观些
		y1Axis.setTickUnit(new NumberTickUnit((tempMax_vol - 0) / 5));// 设置刻度显示的密度
		Font f = new Font("SansSerif", Font.PLAIN, 5);
		y1Axis.setLabelFont(f);
		
		XYLineAndShapeRenderer xyLineRender1 = new XYLineAndShapeRenderer(true, false);
		xyLineRender1.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("yy年MM月dd日"), new DecimalFormat("0.00")));
		xyLineRender1.setSeriesPaint(0, Color.RED);

		DateAxis dateaxis = new DateAxis("");
		// 设置日期格式及显示日期的间隔
		dateaxis.setAutoRange(false);// 设置不采用自动设置时间范围
		
    dateaxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);//设置标记的位置
	    
	    dateaxis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());//设置标准的时间刻度单位
	    dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY,dataList.size()/5>0?dataList.size()/5:1));//设置时间刻度的间隔，一般以周为单位
	    dateaxis.setDateFormatOverride(new SimpleDateFormat("yy年MM月dd日"));//设置显示时间的格式
	  
	    dateaxis.setLowerMargin(0.0D);
	    dateaxis.setUpperMargin(0.02D);

		SegmentedTimeline stl = new SegmentedTimeline(SegmentedTimeline.DAY_SEGMENT_SIZE, 5, 2);
		stl.setStartTime(SegmentedTimeline.firstMondayAfter1900());
		dateaxis.setTimeline(stl); // 设置时间线显示的规则，用这个方法就摒除掉了周六和周日这些没有交易的日期(很多人都不知道有此方法)，使图形看上去连续

		XYPlot volPlot = new XYPlot(dataset, dateaxis, y1Axis, xyLineRender1);
		/*volPlot.setDataset(1, dataset);
		volPlot.setRenderer(1, xyLineRender1);*/
		JFreeChart jfreechart = new JFreeChart("asset chart", JFreeChart.DEFAULT_TITLE_FONT, volPlot, false);

		return jfreechart;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String person_uuid="00001";
		ApplicationFrame demo = new ApplicationFrame("");
		JPanel chartPanel = new ChartPanel(getAssetChart(person_uuid));
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
		demo.setContentPane(chartPanel);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	}

}
