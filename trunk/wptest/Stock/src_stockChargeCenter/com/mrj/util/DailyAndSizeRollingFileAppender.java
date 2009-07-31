package com.mrj.util;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

public class DailyAndSizeRollingFileAppender extends FileAppender {
	private long maxFileSize = 2 * 1024 * 1024;

	private int currentIndex = 0;

	private long nextCheckTime = System.currentTimeMillis() - 1;

	private Calendar cal = Calendar.getInstance();

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Date now = null;

	private String dateString = dateFormat.format(new Date());

	private String prefix;

	private String suffix;

	public void setMaxFileSize(String value) {
		maxFileSize = OptionConverter.toFileSize(value, maxFileSize + 1);
	}

	@Override
	protected void setQWForFiles(Writer writer) {
		this.qw = new CountingQuietWriter(writer, errorHandler);
	}

	@Override
	public synchronized void setFile(String fileName, boolean append,
		boolean bufferedIO, int bufferSize) throws IOException {
		super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
		File f = new File(fileName);
		if (append) {
			((CountingQuietWriter) qw).setCount(f.length());
		}

		if(now == null) {
			if(System.currentTimeMillis() > f.lastModified())
				now = new Date(f.lastModified());
			else
				now = new Date();
		}
		nextCheckTime = calculateNextCheckTime(now);
		dateString = dateFormat.format(now);
		
		int idx = fileName.lastIndexOf('.');
		if(idx != -1) {
			prefix = fileName.substring(0, idx);
			suffix = fileName.substring(idx + 1);
		}
		else {
			prefix = fileName;
			suffix = "";
		}
	}

	/**
	 * ����Ҫsynchronized�������е�doAppend��֤���̰߳�ȫ
	 */
	@Override
	protected void subAppend(LoggingEvent e) {
		long n = System.currentTimeMillis();
		if (n >= nextCheckTime) {
			now.setTime(n);
			nextCheckTime = calculateNextCheckTime(now);
			try {
				dateRollOver();
			} catch (IOException ioe) {
				//ioe.printStackTrace();
			}
		}

		super.subAppend(e);

		if ((fileName != null) && ((CountingQuietWriter) qw).getCount() >= maxFileSize)
			indexRollOver();
	}

	private void dateRollOver() throws IOException {
		if(new File(fileName).length() > 0)
			indexRollOver();
		dateString = dateFormat.format(now);
		currentIndex = 0;
	}

	private void indexRollOver() {
		currentIndex++;
		
		File target;
		File file;

		while(true) {
			file = new File(buildRollFileName(currentIndex));
			if(file.exists())
				currentIndex++;
			else
				break;
		}

		target = new File(buildRollFileName(currentIndex));
		closeFile(); // keep windows happy.
		file = new File(fileName);
		file.renameTo(target);
		try {
			// This will also close the file. This is OK since multiple
			// close operations are safe.
			this.setFile(fileName, false, bufferedIO, bufferSize);
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	private String buildRollFileName(int index) {
		StringBuffer buf = new StringBuffer(prefix);
		buf.append('_');
		buf.append(dateString);
		buf.append('.');
		buf.append(index);
		buf.append('.');
		buf.append(suffix);
		return buf.toString();
	}
	
	private long calculateNextCheckTime(Date now) {
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, 1);
		return cal.getTimeInMillis();
	}
}