package com.ossean.extractormonitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Repository;

import com.ossean.extractormonitor.dao.ExtractorSourceData;
import com.ossean.extractormonitor.destDao.DestSource;
import com.ossean.util.TxtRead;

@Repository("Monitor")
public class ExtractorMonitor extends TimerTask {


	@SuppressWarnings("restriction")
	@Resource
	private ExtractorSourceData extractorSourceData;

	@SuppressWarnings("restriction")
	@Resource
	private DestSource destSource;

	private List<String> txt = new ArrayList<String>();
	private List<String> tables = new ArrayList<String>();
	private List<String> category = new ArrayList<String>();
	private List<String> timeCol = new ArrayList<String>();

	private String end_time = "23:59:59";
	private String begin_time = "00:00:00";
	private String begin_day;
	private String begin_week;
	private String begin_month;
	private String crawler_tail = "_html_detail";
	private static String dest_table = "ossean_monitors";

	@Override
	public void run() {
		// 对抽取环节的监控
		String time_format = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(time_format);
		String end = sdf.format(new Date());
		end = end + " " + end_time;
		begin_day = getTime(-1) + " " + begin_time;
		begin_week = getTime(-7) + " " + begin_time;
		begin_month = getTime(-30) + " " + begin_time;

		// 加载extract的配置文件
		getExtractorConfig("extractTables");
		//添加每行数据的website属性
		checkDestItem(tables);
		//开始统计所需的各个属性的值与每个站点现有的数据总量
		for (int i = 0; i < tables.size(); i++) {
			DesTable dest = new DesTable(tables.get(i), category.get(i));
			dest.setDay_extractor(extractorSourceData.selectByTime(
					tables.get(i), timeCol.get(i), begin_day, end));
			dest.setWeek_extractor(extractorSourceData.selectByTime(
					tables.get(i), timeCol.get(i), begin_week, end));
			dest.setMonth_extractor(extractorSourceData.selectByTime(
					tables.get(i), timeCol.get(i), begin_month, end));
			dest.setTotal_num(extractorSourceData.countNum(tables.get(i)));
			destSource.updateExtractorItem(dest_table, dest);
			destSource.updateTotalNum(dest_table, dest);
		}
	}
    
	public void getExtractorConfig(String txt_name) {
		txt.clear();
		tables.clear();
		category.clear();
		timeCol.clear();
		txt = TxtRead.read("./config/" + txt_name + ".txt");
		for (String str : txt) {
			String[] tmp = str.split(" ");
			tables.add(tmp[0]);
			category.add(tmp[1]);
			timeCol.add(tmp[2]);
		}
	}

	public void getCrawlerConfig(String txt_name) {
		txt.clear();
		tables.clear();
		category.clear();
		timeCol.clear();
		txt = TxtRead.read("./config/" + txt_name + ".txt");
		for (String str : txt) {
			String[] tmp = str.split(" ");
			tables.add(tmp[0] + crawler_tail);
			category.add(tmp[1]);
			timeCol.add(tmp[2]);
		}
	}

	public void checkDestItem(List<String> tables) {
		for (String website : tables) {
			if (destSource.checkItem(dest_table, website) == 0) {
				destSource.addItem(dest_table, website);
			}
		}
	}

	public static String getTime(int days) {
		String time_format = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(time_format);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, days);
		return sdf.format(c.getTime());
	}

	public void begin() {
		Timer timer = new Timer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		Date firstTime = null;
		try {
			firstTime = sdf.parse(now.substring(0, 10) + " 23:59:59");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		timer.schedule(this, 0, 1000);
	}

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:/applicationContext*.xml");
		ExtractorMonitor m = (ExtractorMonitor) applicationContext.getBean("Monitor");
		// Main m = new Main();
		m.begin();
	}
}
