package org.trafficplatform.videoserver.bean;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateVideoCaptureFormat {
	
	@Setter(AccessLevel.NONE)
	private String dateVideoCaptureFormat;
	
	private String year;
	private String month;
	private String day;
	
	private String hour;
	private String minute;
	private String second;
	
	
	public DateVideoCaptureFormat(String dateTime) {
		
		this.dateVideoCaptureFormat = dateTime;

		// Example: 2021 02 06 15 36 04
		Pattern p = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})");
		
		Matcher m = p.matcher(dateVideoCaptureFormat);
		if (m.matches() == true) {
			this.year = m.group(1);
			this.month = m.group(2);
			this.day = m.group(3);
			
			this.hour = m.group(4);
			this.minute = m.group(5);
			this.second = m.group(6);
		} else {
			throw new IllegalArgumentException("The value " + dateVideoCaptureFormat + " had not the right video capture format");
		}
	}
	
	public DateVideoCaptureFormat(LocalDateTime dateTime) {
		
		
		this.year = String.format("%04d", dateTime.getYear());
		this.month = String.format("%02d", dateTime.getMonthValue());
		this.day = String.format("%02d", dateTime.getDayOfMonth());
		
		this.hour = String.format("%02d", dateTime.getHour());
		this.minute = String.format("%02d", dateTime.getMinute());
		this.second = String.format("%02d", dateTime.getSecond());
		
		this.dateVideoCaptureFormat = this.year + this.month + this.day + this.hour + this.minute + this.second;
		
	}


	@Override
	public String toString() {
		return this.hour + ":" + this.minute + ":" + this.second + " " + this.day + "/" + this.month + "/" + this.year;
	}
	
}
