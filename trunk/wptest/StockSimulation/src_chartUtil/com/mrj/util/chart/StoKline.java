package com.mrj.util.chart;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JPanel;

import org.apache.commons.collections.ListUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.date.DateUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;

import com.mrj.sto.DHQ;
import com.mrj.sto.HQ;
import com.mrj.sto.OriginalDataUtil;
import com.mrj.sto.Sto;

import demo.CandlestickChartDemo1;

public class StoKline extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2328143145648625229L;

	/**
	 * A demonstration application showing a candlestick chart.
	 * 
	 * @param title
	 *            the frame title.
	 */
	public StoKline(String title) {
		super(title);
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return The dataset.
	 */
	private static JFreeChart createChart(OHLCDataset dataset, String stoCode) {
		if (dataset == null)
			return null;
		JFreeChart chart = ChartFactory.createCandlestickChart(stoCode == null ? "" : OriginalDataUtil.getAllStoMap().get(stoCode).getName(), "Time", "Value", dataset, true);
		configFont(chart);

		return chart;
	}

	/**
	 * 配置字体
	 * 
	 * @param chart
	 *            JFreeChart 对象
	 */
	private static void configFont(JFreeChart chart) {

		XYPlot plot = (XYPlot) chart.getPlot();

		/**
		 * 颜色
		 */

		CandlestickRenderer renderer = (CandlestickRenderer) plot.getRenderer();
		renderer.setUpPaint(Color.red);
		renderer.setDownPaint(Color.GREEN);

		/**
		 * 字体
		 */

		NumberAxis axis = (NumberAxis) plot.getRangeAxis();
		axis.setAutoRangeIncludesZero(false);
		axis.setUpperMargin(0.0);
		axis.setLowerMargin(0.0);

		// 配置字体
		Font xfont = new Font("宋体", Font.PLAIN, 12);// X轴
		Font yfont = new Font("宋体", Font.PLAIN, 12);// Y轴
		Font kfont = new Font("宋体", Font.PLAIN, 12);// 底部
		Font titleFont = new Font("隶书", Font.BOLD, 25); // 图片标题

		chart.getTitle().setFont(titleFont);
		// 底部
		chart.getLegend().setItemFont(kfont);

		// X 轴
		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(xfont);// 轴标题
		domainAxis.setTickLabelFont(xfont);// 轴数值
		domainAxis.setTickLabelPaint(Color.BLUE); // 字体颜色

		// Y 轴
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(yfont);
		rangeAxis.setLabelPaint(Color.BLUE); // 字体颜色
		rangeAxis.setTickLabelFont(yfont);

	}

	public static JFreeChart getKlineChart(String stoCode, Date fromdate, Date todate) {
		//JFreeChart re = createChart(createDataset(stoCode, fromdate, todate), stoCode);

		// 数据准备----start
		double max_vol = 0, min_vol = 0, max_pr = 0, min_pr = 0;
		Sto sto = OriginalDataUtil.getAllStoMap().get(stoCode);
		if (sto == null)
			return null;
		HQ hq = sto.getHq();
		List dateList = new ArrayList();
		List highList = new ArrayList();
		List lowList = new ArrayList();
		List openList = new ArrayList();
		List closeList = new ArrayList();
		List volumeList = new ArrayList();
		TimeSeries avg_lin5 = new TimeSeries("5 days final price avg line", org.jfree.data.time.Day.class);
		TimeSeries avg_lin10 = new TimeSeries("10 days final price avg line", org.jfree.data.time.Day.class);
		TimeSeries vol_avg_lin5 = new TimeSeries("5 days charge volume avg line", org.jfree.data.time.Day.class);
		TimeSeries vol_avg_lin10 = new TimeSeries("10 days charge volume avg line", org.jfree.data.time.Day.class);
		Calendar begin = Calendar.getInstance();
		begin.setTime(fromdate);
		Calendar end = Calendar.getInstance();
		end.setTime(todate);

		while (begin.compareTo(end) <= 0) {
			DHQ dhq = hq.getDailyHQ(begin.getTime());
			if (dhq != null) {
				dateList.add(begin.getTime());
				highList.add(dhq.getHighPrice());
				lowList.add(dhq.getLowestPrice());
				openList.add(dhq.getBeginPrice());
				closeList.add(dhq.getFinalPrice());
				volumeList.add(dhq.getChargeVolume());
				avg_lin5.addOrUpdate(new Day(begin.get(Calendar.DAY_OF_MONTH), begin.get(Calendar.MONTH) + 1, begin.get(Calendar.YEAR)), dhq.getPrice_5day());
				avg_lin10.addOrUpdate(new Day(begin.get(Calendar.DAY_OF_MONTH), begin.get(Calendar.MONTH) + 1, begin.get(Calendar.YEAR)), dhq.getPrice_10day());
				vol_avg_lin5.addOrUpdate(new Day(begin.get(Calendar.DAY_OF_MONTH), begin.get(Calendar.MONTH) + 1, begin.get(Calendar.YEAR)), dhq.getVolume_5day());
				vol_avg_lin10.addOrUpdate(new Day(begin.get(Calendar.DAY_OF_MONTH), begin.get(Calendar.MONTH) + 1, begin.get(Calendar.YEAR)), dhq.getVolume_10day());

			}
			begin.add(Calendar.DAY_OF_YEAR, 1);
		}

		Date[] date = new Date[dateList.size()];
		double[] high = new double[dateList.size()];
		double[] low = new double[dateList.size()];
		double[] open = new double[dateList.size()];
		double[] close = new double[dateList.size()];
		double[] volume = new double[dateList.size()];

		Date[] date1 = new Date[dateList.size()];
		double[] high1 = new double[dateList.size()];
		double[] low1 = new double[dateList.size()];
		double[] open1 = new double[dateList.size()];
		double[] close1 = new double[dateList.size()];
		double[] volume1 = new double[dateList.size()];

		for (int i = 0; i < dateList.size(); i++) {
			date[i] = (Date) dateList.get(i);
			high[i] = (Float) highList.get(i);
			low[i] = (Float) lowList.get(i);
			open[i] = (Float) openList.get(i);
			close[i] = (Float) closeList.get(i);
			volume[i] = (Long)volumeList.get(i);

			if (i == 0) {
				max_vol = volume[i];
				min_vol = volume[i];
				max_pr = high[i];
				min_pr = low[i];
			} else {
				if (volume[i] > max_vol)
					max_vol = volume[i];
				if (volume[i] < min_vol)
					min_vol = volume[i];
				if (high[i] > max_pr)
					max_pr = high[i];
				if (low[i] <min_pr )
					min_pr = low[i];

			}

			date1[i] = (Date) dateList.get(i);
			high1[i] = 0.0D;
			low1[i] = 0.0D;
			close1[i] = 0.0D;
			open1[i] = 0.0D;

			// 这里是我们用蜡烛图来构造与阴阳线对应的成交量图，我们主要是通过判断开盘价与收盘价相比较的值来决定到底是在表示成交量的蜡烛图的开盘价设置值还是收盘价设置值，设置之前我们把它们全部都设置为0
			if (open[i] < close[i])
				close1[i] = (Long) volumeList.get(i);
			else
				open1[i] = (Long) volumeList.get(i);

			volume1[i] = 0.0D;
			volume[i] = 0;
		}

		// 数据准备----end

		IntervalXYDataset avg_line5 = new TimeSeriesCollection(avg_lin5);
		IntervalXYDataset avg_line10 = new TimeSeriesCollection(avg_lin10);
		IntervalXYDataset vol_avg_line5 = new TimeSeriesCollection(vol_avg_lin5);
		IntervalXYDataset vol_avg_line10 = new TimeSeriesCollection(vol_avg_lin10);

		OHLCDataset k_line = new DefaultHighLowDataset("k", date, high, low, open, close, volume);
		OHLCDataset vol = new DefaultHighLowDataset("vol", date1, high1, low1, open1, close1, volume1);

		XYLineAndShapeRenderer xyLineRender = new XYLineAndShapeRenderer(true, false);
		xyLineRender.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("MM月dd日"), new DecimalFormat("0.00")));
		xyLineRender.setSeriesPaint(0, Color.red);

		XYLineAndShapeRenderer xyLineRender1 = new XYLineAndShapeRenderer(true, false);
		xyLineRender1.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("MM月dd日"), new DecimalFormat("0.00")));
		xyLineRender1.setSeriesPaint(0, Color.BLACK);

		XYLineAndShapeRenderer xyLineRender2 = new XYLineAndShapeRenderer(true, false);
		xyLineRender2.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("MM月dd日"), new DecimalFormat("0.00")));
		xyLineRender2.setSeriesPaint(0, Color.blue);

		// 下半截显示的Plot
		NumberAxis y1Axis = new NumberAxis("");// 设定y轴，就是数字轴
		y1Axis.setAutoRange(false);// 不使用自动设定范围

		int intMin_vol = (int) (min_vol / 5);
		int intMax_vol = (int) ((max_vol * 1.1) / 5);

		int tempMin_vol = intMin_vol * 5 - 5;
		int tempMax_vol = intMax_vol * 5 + 5;

		y1Axis.setRange(min_vol * 0.9, max_vol * 1.1);// 设定y轴值的范围，比最低值要低一些，比最大值要大一些，这样图形看起来会美观些
		y1Axis.setTickUnit(new NumberTickUnit((max_vol * 1.1 - min_vol * 0.9) / 5));// 设置刻度显示的密度

		y1Axis.setRange(tempMin_vol, tempMax_vol);// 设定y轴值的范围，比最低值要低一些，比最大值要大一些，这样图形看起来会美观些
		y1Axis.setTickUnit(new NumberTickUnit((tempMax_vol - tempMin_vol) / 5));// 设置刻度显示的密度
		Font f = new Font("SansSerif", Font.PLAIN, 5);
		y1Axis.setLabelFont(f);

		XYPlot volPlot = new XYPlot(vol_avg_line5, null, y1Axis, xyLineRender1);

		// 定义一个CandlestickRenderer给蜡烛图对象使用，目的是对蜡烛图对象的显示进行调整，这里主要是调整它显示的宽度并加上鼠标提示

		CandlestickRenderer candlesRender = new CandlestickRenderer();
		candlesRender.setCandleWidth(4D);
		candlesRender.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{1},{2}", new SimpleDateFormat("MM月dd日"), new DecimalFormat("0.00")));
		candlesRender.setUpPaint(Color.red);
		candlesRender.setDownPaint(Color.green);
		
		// 把其它的几个Dataset加到上半截要显示的Plot里，并同时设置它们所采用的Render，以形成一个叠加显示的效果
		volPlot.setDataset(1, vol_avg_line5);
		volPlot.setRenderer(1, xyLineRender1);
		/*volPlot.setDataset(2, vol_avg_line10);
		volPlot.setRenderer(2, xyLineRender);*/
		volPlot.setDataset(3, vol);
		volPlot.setRenderer(3, candlesRender);
		
		
//		 定义k line图上半截显示的Plot
	    NumberAxis y2Axis = new NumberAxis("");
	    y2Axis.setAutoRange(false);// 不不使用自动设定范围
	    
	    int intMin_pr = (int)(min_pr/5);
	    int intMax_pr = (int)(max_pr/5);
	    
	    int tempMin_pr = intMin_pr*5 - 5;
	    int tempMax_pr = intMax_pr*5 + 5;

	    y2Axis.setRange(tempMin_pr, tempMax_pr);// 设定y轴值的范围，比最低值要低一些，比最大值要大一些，这样图形看起来会美观些
	    y2Axis.setTickUnit(new NumberTickUnit(
	            (tempMax_pr - tempMin_pr) / 5));// 设置刻度显示的密度
	    
	    XYPlot candlePlot = new XYPlot(k_line, null, y2Axis , candlesRender);
	    candlePlot.setDataset(1, avg_line5);
	    candlePlot.setRenderer(1, xyLineRender1);
	    candlePlot.setDataset(2, avg_line10);
	    candlePlot.setRenderer(2, xyLineRender2);
	    
	    
	    DateAxis dateaxis = new DateAxis("K line");
//	  设置日期格式及显示日期的间隔
	    dateaxis.setAutoRange(false);//设置不采用自动设置时间范围
	    try {
	        dateaxis.setRange(fromdate,todate);//设置时间范围，注意时间的最大值要比已有的时间最大值要多一天
	    }catch(Exception e) {
	     e.printStackTrace();
	    }
	    
	    SegmentedTimeline stl=new SegmentedTimeline(SegmentedTimeline.DAY_SEGMENT_SIZE, 5, 2);
	    stl.setStartTime(SegmentedTimeline.firstMondayAfter1900());	
	    dateaxis.setTimeline(stl); //设置时间线显示的规则，用这个方法就摒除掉了周六和周日这些没有交易的日期(很多人都不知道有此方法)，使图形看上去连续
	     
	    dateaxis.setAutoTickUnitSelection(false);//设置不采用自动选择刻度值
	    dateaxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);//设置标记的位置
	    
	    dateaxis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());//设置标准的时间刻度单位
	    dateaxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY,10));//设置时间刻度的间隔，一般以周为单位
	    dateaxis.setDateFormatOverride(new SimpleDateFormat("MM月dd日"));//设置显示时间的格式
	  
	    dateaxis.setLowerMargin(0.0D);
	    dateaxis.setUpperMargin(0.02D);
	    
	    
//	  定义一个复合类型的Plot，目的是为了把Chart的上半截和下半截结合起来，形成一张完整的K线图
	    CombinedDomainXYPlot combineXY = new CombinedDomainXYPlot(dateaxis);

	    //把上下两个Plot都加到复合Plot里，并设置它们在图中所占的比重
	    combineXY.add(candlePlot, 3);
	    combineXY.add(volPlot, 1);
	    combineXY.setGap(8D);
	    combineXY.setDomainGridlinesVisible(true);

	    String stock_name=stoCode == null ? "" : OriginalDataUtil.getAllStoMap().get(stoCode).getName();
	    JFreeChart jfreechart = new JFreeChart(stock_name,JFreeChart.DEFAULT_TITLE_FONT, combineXY, false);
	  //  ChartPanel cPanel = new ChartPanel(jfreechart);
	  
	    
	    jfreechart.setBackgroundPaint(Color.white);

//	为Chart图添加一个图例，这里我们可以定义需要显示的一些信息，及图例放置的位置，我们选择的顶部
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

	public static JFreeChart getChart(String stoCode, Date fromdate, Date todate) {
		JFreeChart re = createChart(createDataset(stoCode, fromdate, todate), stoCode);
		return re;
	}

	@SuppressWarnings("unchecked")
	private static OHLCDataset createDataset(String stoCode, Date fromdate, Date todate) {
		Sto sto = OriginalDataUtil.getAllStoMap().get(stoCode);
		if (sto == null)
			return null;
		HQ hq = sto.getHq();

		List dateList = new ArrayList();
		List highList = new ArrayList();
		List lowList = new ArrayList();
		List openList = new ArrayList();
		List closeList = new ArrayList();
		List volumeList = new ArrayList();

		Calendar begin = Calendar.getInstance();
		begin.setTime(fromdate);
		Calendar end = Calendar.getInstance();
		end.setTime(todate);

		while (begin.compareTo(end) <= 0) {
			DHQ dhq = hq.getDailyHQ(begin.getTime());
			if (dhq != null) {
				dateList.add(begin.getTime());
				highList.add(dhq.getHighPrice());
				lowList.add(dhq.getLowestPrice());
				openList.add(dhq.getBeginPrice());
				closeList.add(dhq.getFinalPrice());
				volumeList.add(dhq.getChargeVolume());
			}
			begin.add(Calendar.DAY_OF_YEAR, 1);
		}

		Date[] date = new Date[dateList.size()];
		double[] high = new double[dateList.size()];
		double[] low = new double[dateList.size()];
		double[] open = new double[dateList.size()];
		double[] close = new double[dateList.size()];
		double[] volume = new double[dateList.size()];

		for (int i = 0; i < dateList.size(); i++) {
			date[i] = (Date) dateList.get(i);
			high[i] = (Float) highList.get(i);
			low[i] = (Float) lowList.get(i);
			open[i] = (Float) openList.get(i);
			close[i] = (Float) closeList.get(i);
			volume[i] = (Long) volumeList.get(i);

		}

		return new DefaultHighLowDataset("Series 1", date, high, low, open, close, volume);
	}

	/**
	 * Starting point for the demonstration application.
	 * 
	 * @param args
	 *            ignored.
	 */
	public static void main(String[] args) {
		CandlestickChartDemo1 demo = new CandlestickChartDemo1("JFreeChart : Candlestick Demo 1");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}

}
