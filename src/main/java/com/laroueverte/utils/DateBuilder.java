
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

	/**
	 *
	 * @return a DateBuilder build with the current system date and time
	 */
	public static DateBuilder now() {
		return new DateBuilder();
	}

	/**
	 *
	 * @param date is the source date
	 * @return a new DateBuilder build with the given date
	 */
	public static DateBuilder date(Date date) {
		return new DateBuilder(date);
	}

	/**
	 * Build a new DateBuilder combining one Date object for the date part and another one Date object for the time part
	 *
	 * @param date is the date part of the Date and Time
	 * @param time is the time part of the Date and Time
	 * @return a new DateBuilder build with the given parameters
	 */
	public static DateBuilder dateTime(Date date, Date time) {
		DateBuilder timeDateBuilder = DateBuilder.date(time);
		return new DateBuilder(date).setHour(timeDateBuilder.getHour()).setMinute(timeDateBuilder.getMinute()).setSecond(timeDateBuilder.getSecond());
	}

	/**
	 * Build a new DateBuilder that is only a Time object (Date is on EPOCH)
	 *
	 * @param hours is the number of hours
	 * @param minutes is the number if minutes
	 * @param seconds is the number of seconds
	 * @return a new DateBuilder build with the given parameters
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
	 * @param milliseconds is the number of milliseconds since EPOCH
	 * @return a new DateBuilder build with the given parameters
	 */
	public static DateBuilder milliseconds(long milliseconds) {
		return date(new Date(milliseconds));
	}

	/**
	 * Build a new DateBuilder by parsing a string in ISO8601 format
	 *
	 * @param dateString is the source string
	 * @return a new DateBuilder build with the given parameters
	 * @throws DateTimeParseException if the string is not in ISO8601 format
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
	 * @param iso8601 is the source string
	 * @return a new DateBuilder build with the given parameters
	 */
	public static DateBuilder iso8601WithTimeZone(String iso8601) {
		return offsetDateTime(OffsetDateTime.parse(iso8601));
	}

	/**
	 * Convert a OffsetDateTime to a java Date (uses years, month, day, hours, minutes)
	 *
	 * @param offsetDateTime is the OffsetDateTime to convert
	 * @return a new DateBuilder build with the given parameters
	 */
	public static DateBuilder offsetDateTime(OffsetDateTime offsetDateTime) {
		return milliseconds(offsetDateTime.toInstant().toEpochMilli());
	}

	/**
	 * Convert a OffsetDateTime to a java Date (uses years, month, day, hours, minutes)
	 *
	 * @param localDateTime is the LocalDateTime to convert
	 * @return a new DateBuilder build with the given parameters
	 */
	public static DateBuilder localDateTime(LocalDateTime localDateTime) {
		return milliseconds(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
	}

	/**
	 * Parse a ISO8601 string in local format (not including timezone) like 2014-02-07T16:25:00
	 *
	 * @param iso8601 is the source string
	 * @return a new DateBuilder build with the given parameters
	 */
	public static DateBuilder iso8601InLocalDateTime(String iso8601) {
		return localDateTime(LocalDateTime.parse(iso8601));
	}

	/**
	 *
	 * @param dateString if the source string
	 * @param format is date format
	 * @return a new DateBuilder build with the given parameters
	 * @throws ParseException if the string is not in correct format
	 */
	public static DateBuilder string(String dateString, String format) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return date(simpleDateFormat.parse(dateString));
	}

	/**
	 *
	 * @param timeString if the source string
	 * @param format is date format
	 * @return a new DateBuilder build with the given parameters
	 * @throws ParseException if the string is not in correct format
	 */
	public static DateBuilder stringTime(String timeString, String format) throws ParseException {
		return string(timeString, format).trimToTime();
	}

	/**
	 *
	 * @param year with 4 digits
	 * @param month from 1 to 12 ...
	 * @param dayOfMonth from 1 to 31
	 * @return a new DateBuilder build with the given parameters
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
	 * @param year with 4 digits
	 * @param month from 1 to 12 ...
	 * @param dayOfMonth from 1 to 31
	 * @param hour from 0 to 23
	 * @param minute from 0 to 59
	 * @return a new DateBuilder build with the given parameters
	 */
	public static DateBuilder dateTime(int year, int month, int dayOfMonth, int hour, int minute) {
		DateBuilder res = date(year, month, dayOfMonth);
		res.setHour(hour);
		res.setMinute(minute);
		return res;
	}

	/**
	 *
	 * @param year with 4 digits
	 * @param month from 1 to 12 ...
	 * @param dayOfMonth from 1 to 31
	 * @param hour from 0 to 23
	 * @param minute from 0 to 59
	 * @param second from 0 to 59
	 * @return a new DateBuilder build with the given parameters
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
	 * @param year with 4 digits
	 * @param month from 1 to 12 ...
	 * @param dayOfMonth from 1 to 31
	 * @param hour from 0 to 23
	 * @param minute from 0 to 59
	 * @param second from 0 to 59
	 * @param millisecond from 0 to 999
	 * @return a new DateBuilder build with the given parameters
	 */
	public static DateBuilder dateTime(int year, int month, int dayOfMonth, int hour, int minute, int second, int millisecond) {
		DateBuilder res = dateTime(year, month, dayOfMonth, hour, minute, second);
		res.setMillisecond(millisecond);
		return res;
	}

	/**
	 *
	 * @param dateString is a date formatted in "iso" format yyyy-MM-dd
	 * @return the formatted date
	 * @throws ParseException is input is not a valid date
	 */
	public static DateBuilder iso(String dateString) throws ParseException {
		return string(dateString, ISO_FORMAT);
	}

	/**
	 *
	 * @param timestamp is source iso date string formatted in "iso" format yyyy-MM-dd HH:mm:ss
	 * @return the formatted date
	 * @throws ParseException is input is not a valid date
	 */
	public static DateBuilder isoTimestamp(String timestamp) throws ParseException {
		return string(timestamp, ISO_FORMAT_TIME);
	}

	/**
	 * Builds a new constant instance based on this builder
	 *
	 * @return a new datebuilder constant instance
	 */
	public DateConstant constant() {
		return new DateConstant(this.getDate());
	}

	// Methods modifying the state of the builder

	/**
	 * Only keep, hour, minute and seconds
	 *
	 * @return the same instance, modified
	 */
	public DateBuilder trimToTime() {
		calendar.set(Calendar.YEAR, 1970);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MILLISECOND, 0);
		return this;
	}

	/**
	 * Sets date to 1st january of the same year
	 *
	 * @return the same instance, modified
	 */
	public DateBuilder trimToYear() {
		trimToMonth();
		calendar.set(Calendar.MONTH, 0);
		return this;
	}

	/**
	 * Sets the first day of the month, midgnight
	 *
	 * @return the same instance, modified
	 */
	public DateBuilder trimToMonth() {
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		trimToDay();
		return this;
	}

	/**
	 * Keep only date components (clears from hour to ms)
	 *
	 * @return the same instance, modified
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
	 * @return the same instance, modified
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
	 * @return the same instance, modified
	 */
	public DateBuilder trimToMinute() {
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return this;
	}

	/**
	 * Remove milliseconds, keep all other fields
	 *
	 * @return the same instance, modified
	 */
	public DateBuilder trimToSecond() {
		calendar.set(Calendar.MILLISECOND, 0);
		return this;
	}

	/**
	 * Change the hour of the date builder
	 *
	 * @param hour the hour to set
	 * @return the same instance, modified
	 */
	public DateBuilder setHour(int hour) {
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		return this;
	}

	/**
	 * Change the minute of the date builder
	 *
	 * @param minute the minute to set
	 * @return the same instance, modified
	 */
	public DateBuilder setMinute(int minute) {
		calendar.set(Calendar.MINUTE, minute);
		return this;
	}

	/**
	 * Change the second of the date builder
	 *
	 * @param second the second to set
	 * @return the same instance, modified
	 */
	public DateBuilder setSecond(int second) {
		calendar.set(Calendar.SECOND, second);
		return this;
	}

	/**
	 * Change the millisecond of the date builder
	 *
	 * @param millisecond the millisecond to set
	 * @return the same instance, modified
	 */
	public DateBuilder setMillisecond(int millisecond) {
		calendar.set(Calendar.MILLISECOND, millisecond);
		return this;
	}

	/**
	 * Change the day of month of the date builder
	 *
	 * @param day the day of month to set
	 * @return the same instance, modified
	 */
	public DateBuilder setDay(int day) {
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return this;
	}

	/**
	 * Change the month of the date builder
	 *
	 * @param month From 00 to 11
	 * @return the same instance, modified
	 */
	public DateBuilder setMonth(int month) {
		calendar.set(Calendar.MONTH, month);
		return this;
	}

	/**
	 * Change the year of the date builder
	 *
	 * @param year the year to set
	 * @return the same instance, modified
	 */
	public DateBuilder setYear(int year) {
		calendar.set(Calendar.YEAR, year);
		return this;
	}

	/**
	 *
	 * @param count number of year to add. Can be negative
	 * @return the same instance, modified
	 */
	public DateBuilder addYear(int count) {
		calendar.add(Calendar.YEAR, count);
		return this;
	}

	/**
	 *
	 * @param count number of month to add. Can be negative
	 * @return the same instance, modified
	 */
	public DateBuilder addMonth(int count) {
		calendar.add(Calendar.MONTH, count);
		return this;
	}

	/**
	 *
	 * @param count number of days to add. Can be negative to go back in time
	 * @return the same instance, modified
	 */
	public DateBuilder addDays(int count) {
		calendar.add(Calendar.DAY_OF_YEAR, count);
		return this;
	}

	/**
	 *
	 * @param count number of hours to add. Can be negative to go back in time
	 * @return the same instance, modified
	 */
	public DateBuilder addHours(int count) {
		calendar.add(Calendar.HOUR_OF_DAY, count);
		return this;
	}

	/**
	 *
	 * @param count number of minutes to add. Can be negative to go back in time
	 * @return the same instance, modified
	 */
	public DateBuilder addMinutes(int count) {
		calendar.add(Calendar.MINUTE, count);
		return this;
	}

	/**
	 *
	 * @param count number of seconds to add. Can be negative to go back in time
	 * @return the same instance, modified
	 */
	public DateBuilder addSeconds(int count) {
		calendar.add(Calendar.SECOND, count);
		return this;
	}

	/**
	 * Adds a duration, currenly only from seconds (milliseconds not supported)
	 *
	 * @param duration is a Duration to add
	 * @return the same instance, modified
	 */
	public DateBuilder addDuration(Duration duration) {
		return addSeconds((int) duration.getSeconds());
	}

	/**
	 * move to next date that is day of week (1 = sunday, 2= monday ...., 7=saturday). Stay on it if we're already that day
	 *
	 * @param dayOfWeek is an int taken from the Calendar class (1 = sunday, 2= monday ...., 7=saturday)
	 * @return the same instance, modified
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
	 * @return the same instance, modified
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
	 * @return the same instance, modified
	 */
	public DateBuilder moveToNextLocalUniqueTime() {
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
