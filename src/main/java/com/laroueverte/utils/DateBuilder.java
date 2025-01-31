
package com.laroueverte.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * This Class implement a DateBuilder by inheriting from DateConstant. It provides builder and state modification methods
 *
 * @author la roue verte
 *
 */
public class DateBuilder extends DateConstant {
	// Private Java constructors
	private DateBuilder() {
		super();
	}

	private DateBuilder(Date date) {
		setDate(date);
	}

	private DateBuilder(Calendar calendar) {
		super(calendar);
	}

	// Constructors

	public static DateBuilder now() {
		return new DateBuilder();
	}

	public static DateBuilder date(Date date) {
		return new DateBuilder(date);
	}

	/**
	 * Build a new DateBuilder combining one Date object for the date part and another one Date object for the time part
	 *
	 * @param date
	 * @param time
	 * @return
	 */
	public static DateBuilder dateTime(Date date, Date time) {
		DateBuilder timeDateBuilder = DateBuilder.date(time);
		return new DateBuilder(date).setHour(timeDateBuilder.getHour()).setMinute(timeDateBuilder.getMinute()).setSecond(timeDateBuilder.getSecond());
	}

	/**
	 * Build a new DateBuilder that is only a Time object (Date is on EPOCH)
	 *
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	public static DateBuilder time(int hours, int minutes, int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1970);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, seconds);
		calendar.set(Calendar.MILLISECOND, 0);
		return date(calendar.getTime());
	}

	/**
	 * Build a new DateBuilder from a number of milliseconds since EPOCH
	 *
	 * @param milliseconds
	 * @return
	 */
	public static DateBuilder milliseconds(long milliseconds) {
		return date(new Date(milliseconds));
	}

	/**
	 * Build a new DateBuilder by parsing a string in ISO8601 format
	 *
	 * @param dateString
	 * @return
	 * @throws DateTimeParseException
	 */
	public static DateBuilder iso8601(String dateString) throws DateTimeParseException {
		if (dateString.matches(".*(([+-]\\d\\d:\\d\\d)|Z)$")) {
			return iso8601WithTimeZone(dateString);
		} else {
			return iso8601InLocalDateTime(dateString);
		}
	}

	/**
	 * Parse a ISO8601 string including timezone (like 2014-02-07T16:25+01:00 or 2014-02-07T16:25Z)
	 *
	 * @param iso8601
	 * @return
	 */
	public static DateBuilder iso8601WithTimeZone(String iso8601) {
		return offsetDateTime(OffsetDateTime.parse(iso8601));
	}

	/**
	 * Convert a OffsetDateTime to a java Date (uses years, month, day, hours, minutes)
	 *
	 * @param offsetDateTime
	 * @return
	 */
	public static DateBuilder offsetDateTime(OffsetDateTime offsetDateTime) {
		return milliseconds(offsetDateTime.toInstant().toEpochMilli());
	}

	/**
	 * Convert a OffsetDateTime to a java Date (uses years, month, day, hours, minutes)
	 *
	 * @param localDateTime
	 * @return
	 */
	public static DateBuilder localDateTime(LocalDateTime localDateTime) {
		return milliseconds(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
	}

	/**
	 * Parse a ISO8601 string in local format (not including timezone) like 2014-02-07T16:25:00
	 *
	 * @param iso8601
	 * @return
	 */
	public static DateBuilder iso8601InLocalDateTime(String iso8601) {
		return localDateTime(LocalDateTime.parse(iso8601));
	}

	public static DateBuilder string(String dateString, String format) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return date(simpleDateFormat.parse(dateString));
	}

	public static DateBuilder stringTime(String timeString, String format) throws ParseException {
		return string(timeString, format).trimToTime();
	}

	/**
	 *
	 * @param year
	 * @param month from 1 to 12 ...
	 * @param dayOfMonth
	 * @return
	 */
	public static DateBuilder date(int year, int month, int dayOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, dayOfMonth);
		DateBuilder res = new DateBuilder(calendar);
		res.trimToDay();
		return res;
	}

	/**
	 *
	 * @param year
	 * @param month from 1 to 12 ...
	 * @param dayOfMonth
	 * @param hour from 0 to 23
	 * @param minute from 0 to 59
	 * @return
	 */
	public static DateBuilder dateTime(int year, int month, int dayOfMonth, int hour, int minute) {
		DateBuilder res = date(year, month, dayOfMonth);
		res.setHour(hour);
		res.setMinute(minute);
		return res;
	}

	/**
	 *
	 * @param year
	 * @param month from 1 to 12 ...
	 * @param dayOfMonth
	 * @param hour from 0 to 23
	 * @param minute from 0 to 59
	 * @param second from 0 to 59
	 * @return
	 */
	public static DateBuilder dateTime(int year, int month, int dayOfMonth, int hour, int minute, int second) {
		DateBuilder res = date(year, month, dayOfMonth);
		res.setHour(hour);
		res.setMinute(minute);
		res.setSecond(second);
		return res;
	}

	/**
	 *
	 * @param year
	 * @param month from 1 to 12 ...
	 * @param dayOfMonth
	 * @param hour from 0 to 23
	 * @param minute from 0 to 59
	 * @param minute from 0 to 59
	 * @param millisecond from 0 to 999
	 * @return
	 */
	public static DateBuilder dateTime(int year, int month, int dayOfMonth, int hour, int minute, int second, int millisecond) {
		DateBuilder res = dateTime(year, month, dayOfMonth, hour, minute, second);
		res.setMillisecond(millisecond);
		return res;
	}

	/**
	 *
	 * @param dateString is a date formatted in "iso" format yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static DateBuilder iso(String dateString) throws ParseException {
		return string(dateString, ISO_FORMAT);
	}

	public static DateBuilder isoTimestamp(String timestamp) throws ParseException {
		return string(timestamp, ISO_FORMAT_TIME);
	}

	/**
	 * Builds a new constant instance based on this builder
	 *
	 * @return
	 */
	public DateConstant constant() {
		return new DateConstant(this.getDate());
	}

	// Methods modifying the state of the builder

	/**
	 * Only keep, hour, minute and seconds
	 *
	 * @return
	 */
	public DateBuilder trimToTime() {
		calendar.set(Calendar.YEAR, 1970);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MILLISECOND, 0);
		return this;
	}

	/**
	 * Sets date to 1st january
	 *
	 * @return
	 */
	public DateBuilder trimToYear() {
		trimToMonth();
		calendar.set(Calendar.MONTH, 0);
		return this;
	}

	/**
	 * Sets the first day of the month, midgnight
	 *
	 * @return
	 */
	public DateBuilder trimToMonth() {
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		trimToDay();
		return this;
	}

	/**
	 * Keep only date components (clears from hour to ms)
	 *
	 * @return
	 */
	public DateBuilder trimToDay() {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return this;
	}

	/**
	 * keep all until hour
	 *
	 * @return
	 */
	public DateBuilder trimToHour() {
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return this;
	}

	/**
	 * keep all until minute
	 *
	 * @return
	 */
	public DateBuilder trimToMinute() {
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return this;
	}

	/**
	 * Remove milliseconds
	 *
	 * @return
	 */
	public DateBuilder trimToSecond() {
		calendar.set(Calendar.MILLISECOND, 0);
		return this;
	}

	public DateBuilder setHour(int hour) {
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		return this;
	}

	public DateBuilder setMinute(int minute) {
		calendar.set(Calendar.MINUTE, minute);
		return this;
	}

	public DateBuilder setSecond(int second) {
		calendar.set(Calendar.SECOND, second);
		return this;
	}

	public DateBuilder setMillisecond(int millisecond) {
		calendar.set(Calendar.MILLISECOND, millisecond);
		return this;
	}

	public DateBuilder setDay(int day) {
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return this;
	}

	/**
	 *
	 * @param month From 00 to 11
	 * @return
	 */
	public DateBuilder setMonth(int month) {
		calendar.set(Calendar.MONTH, month);
		return this;
	}

	/**
	 *
	 * @param year
	 * @return
	 */
	public DateBuilder setYear(int year) {
		calendar.set(Calendar.YEAR, year);
		return this;
	}

	public DateBuilder addYear(int count) {
		calendar.add(Calendar.YEAR, count);
		return this;
	}

	public DateBuilder addMonth(int count) {
		calendar.add(Calendar.MONTH, count);
		return this;
	}

	public DateBuilder addDays(int count) {
		calendar.add(Calendar.DAY_OF_YEAR, count);
		return this;
	}

	public DateBuilder addHours(int count) {
		calendar.add(Calendar.HOUR_OF_DAY, count);
		return this;
	}

	public DateBuilder addMinutes(int count) {
		calendar.add(Calendar.MINUTE, count);
		return this;
	}

	public DateBuilder addSeconds(int count) {
		calendar.add(Calendar.SECOND, count);
		return this;
	}

	/**
	 * Adds a duration, currenly only from seconds (milliseconds not supported)
	 *
	 * @param duration
	 * @return
	 */
	public DateBuilder addDuration(Duration duration) {
		return addSeconds((int) duration.getSeconds());
	}

	/**
	 * move to next date that is day of week (1 = sunday, 2= monday ...., 7=saturday). Stay on it if we're already that day
	 *
	 * @param dayOfWeek is an int take from the Calendar class
	 * @return
	 */
	public DateBuilder moveToNextDayOfWeek(int dayOfWeek) {
		while (calendar.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
			addDays(1);
		}
		return this;
	}

	/**
	 * move to the previous date that is day of week (1 = sunday, 2= monday ...., 7=saturday). Stay on it if we're already that day
	 *
	 * @param dayOfWeek is an int take from the Calendar class
	 * @return
	 */
	public DateBuilder moveToPreviousDayOfWeek(int dayOfWeek) {
		while (calendar.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
			addDays(-1);
		}
		return this;
	}

	/**
	 * move the to the next local unique time (see {@link DateConstant#isALocalNonUniqueTime()} for "local unique time" definition)
	 *
	 * @return
	 */
	public DateConstant moveToNextLocalUniqueTime() {
		// Pseudo dichotomical algorithm to look for next unique time
		if (isALocalNonUniqueTime()) {
			addMinutes(60);
			if (isALocalNonUniqueTime()) {
				addMinutes(40);
			} else {
				addMinutes(-40);
			}
			if (isALocalNonUniqueTime()) {
				addMinutes(10);
			} else {
				addMinutes(-10);
			}
			if (isALocalNonUniqueTime()) {
				addMinutes(5);
			} else {
				addMinutes(-5);
			}
			if (isALocalNonUniqueTime()) {
				while (isALocalNonUniqueTime()) {
					addMinutes(1);
				}
			} else {
				while (!isALocalNonUniqueTime()) {
					addMinutes(-1);
				}
				addMinutes(1);
			}
		}
		return this;
	}
}
