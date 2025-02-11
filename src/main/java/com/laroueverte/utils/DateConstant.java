package com.laroueverte.utils;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A constant date that can be only used to be read, tested or transformed. It can't be modified. To obtain a modifiable copy, one should use {@link #builder()}
 *
 * @author la roue verte
 */
public class DateConstant {
	public static final String ISO_FORMAT = "yyyy-MM-dd";
	public static final String ISO_FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FRENCH_FORMAT = "dd/MM/yyyy";
	public static final String DATE_TIME_FOR_FILE_FORMAT = "yyyy-MM-dd-HH-mm";
	public static final String MONTH_NAME_FORMAT = "MMMMM";
	/**
	 * French date format, for example : Lundi 10 juillet 2011 10h40
	 */
	public static final String DATETIME_FORMAT_FRENCH_LONG = "EEEEEEEEEEEEEEE dd MMMMMMMMMMMMMMMMMMMMMMM yyyy HH'h'mm";

	protected Calendar calendar;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DateConstant) {
			return calendar.equals(((DateConstant) obj).calendar);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return calendar.hashCode();
	}

	protected DateConstant() {
		this(new Date());
	}

	/**
	 * @return a new builder based on same date as this one
	 */
	public DateBuilder builder() {
		return DateBuilder.date(getDate());
	}

	protected DateConstant(Date date) {
		setDate(date);
	}

	protected DateConstant(Calendar calendar) {
		this.calendar = calendar;
	}

	protected DateConstant setDate(Date date) {
		this.calendar = Calendar.getInstance();
		this.calendar.setTime(date);
		return this;
	}

	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}

	public int getHour() {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		return calendar.get(Calendar.MINUTE);
	}

	public int getSecond() {
		return calendar.get(Calendar.SECOND);
	}

	public int getMillisecond() {
		return calendar.get(Calendar.MILLISECOND);
	}

	public long getMillisecondsToReach(Date futureDate) {
		return futureDate.getTime() - this.calendar.getTimeInMillis();
	}

	/**
	 *
	 * @return the time in milliseconds (since epoch)
	 */
	public long getTimeInMillis() {
		return this.calendar.getTimeInMillis();
	}

	/**
	 *
	 * @param d1 is the first date to compare
	 * @param d2 is the second date to compare
	 * @return the minimum of the two d1 and d2
	 */
	public static DateConstant min(DateConstant d1, DateConstant d2) {
		if (d1.isSameOrBefore(d2.getDate())) {
			return d1;
		} else {
			return d2;
		}
	}

	/**
	 *
	 * @param d1 is the first date to compare
	 * @param d2 is the second date to compare
	 * @return the maximum of the two d1 and d2
	 */
	public static DateConstant max(DateConstant d1, DateConstant d2) {
		if (d1.isSameOrAfter(d2.getDate())) {
			return d1;
		} else {
			return d2;
		}
	}

	/**
	 *
	 * @param futureDate is a future date that will be used to get the seconds difference
	 * @return the number of seconds between the current date (of this builder) and the supplied date
	 */
	public int getSecondsToReach(Date futureDate) {
		return (int) (getMillisecondsToReach(futureDate) / 1000);
	}

	/**
	 * @param futureDate is a future date that will be used to get the minutes difference
	 * @return the number of days between those two dates (not including one of them). For instance between 2010-01-25 and 2010-01-26, there is 1 day.
	 */
	public int getMinutesToReach(Date futureDate) {
		return getSecondsToReach(futureDate) / 60;
	}

	/**
	 *
	 * @param futureDate is a future date that will be used to get the hours difference
	 * @return the number of seconds between the current date (of this builder) and the supplied date
	 */
	public int getHoursToReach(Date futureDate) {
		return getMinutesToReach(futureDate) / 60;
	}

	/**
	 * @param futureDate is a future date that will be used to get the days difference
	 * @return the number of days between those two dates (not including one of them). For instance between 2010-01-25 and 2010-01-26, there is 1 day.
	 */
	public int getDaysToReach(Date futureDate) {
		return getHoursToReach(futureDate) / 24;
	}

	/**
	 *
	 * @return if the date is in the future (using the current time as reference)
	 */
	public boolean isFuture() {
		return calendar.after(Calendar.getInstance());
	}

	/**
	 * @return if the date object hold by this builder
	 */
	public Date getDate() {
		return calendar.getTime();
	}

	/**
	 * Warning : date must be trimed before
	 *
	 * @return a Time object
	 */
	public Time getTime() {
		return new Time(calendar.getTimeInMillis());
	}

	/**
	 * Returns the month value for the current builder's date. 1 = january, ... 12 = december
	 *
	 * @return a value between 1 and 12
	 */
	public int getHumanMonth() {
		int month = calendar.get(Calendar.MONTH);
		return month + 1;
	}

	/**
	 * Return the current builder's date day of month
	 *
	 * @return a value between 1 and 31
	 */
	public int getDayOfMonth() {
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		return dayOfMonth;
	}

	/**
	 * Format the date using a java DateFormat
	 *
	 * @param format is a DataFormat, common format strings can be found on DateUtils class
	 * @param locale is the locate to use for formatting
	 * @return the formatted date
	 */
	public String toString(String format, Locale locale) {
		DateFormat dateFormat = new SimpleDateFormat(format, locale);
		return dateFormat.format(getDate());
	}

	/**
	 * Format the date using a java DateFormat
	 *
	 * @param format is a DataFormat, common format strings can be found on DateUtils class
	 * @return the formatted date
	 */
	public String toString(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(getDate());
	}

	/**
	 * Return the current builder's date day of week
	 *
	 * @return 1 for Sunday, 2 for Monday, ... 7 for Saturday
	 */
	public int getDayOfWeek() {
		int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
		return dayOfweek;
	}

	/**
	 * @return the current builder's date week of year, depending on locale
	 * @param locale locale to use to compute week number. It can vary with locale. Cf. https://fr.wikipedia.org/wiki/Semaine_53
	 */
	public int getWeekOfYear(Locale locale) {
		Calendar localCalendar = Calendar.getInstance(locale);
		localCalendar.setTime(getDate());
		return localCalendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 *
	 * @return the time zone offset in minutes (-12*60 / +14*60) of the current date
	 */
	public int getTimeZoneOffset() {
		return (calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / 1000 / 60;
	}

	/**
	 *
	 * @return true if the local time pointed out is not unique, because it could be set twice in a daily light save time. Return true for all the date between
	 *         2am and 2h59am the day of winter DST
	 */
	public boolean isALocalNonUniqueTime() {
		return builder().addHours(1).getTimeZoneOffset() < getTimeZoneOffset() || builder().addHours(-1).getTimeZoneOffset() > getTimeZoneOffset();
	}

	/**
	 * Outputs a local date time (no Z or +-01:00)
	 *
	 * @return the formatted date
	 */
	public String toISO8601Local() {
		return toISO8601Local(false);
	}

	/**
	 * Outputs a local date time (no Z or +-01:00)
	 *
	 * @param forceMilliseconds if true,0 will be appended up to milliseconds if it's zero
	 * @return the formatted date
	 */
	public String toISO8601Local(boolean forceMilliseconds) {
		String result = toISO8601LocalDateTime();
		if (forceMilliseconds) {
			if (getMillisecond() == 0) {
				if (getSecond() == 0) {
					result += ":00";
				}
				result += ".000";
			}
		}
		return result;
	}

	/**
	 * Outputs a date time with time zone(with Z or +-01:00)
	 *
	 * @return the formatted date
	 */
	public String toISO8601WithTimeZone() {
		return toISO8601OffsetDateTime();
	}

	/**
	 * Produces a String in ISO8601 format as an offset datetime, with time zone like 2014-02-07T16:25+01:00
	 *
	 * @return the formatted date
	 */
	public String toISO8601OffsetDateTime() {
		return toOffsetDateTime().toString();
	}

	/**
	 * Produces a Zulu Time ISO8601 String representation of the given date.<br>
	 * Eg: 2023-03-15T15:03:12 UTC+1 whould be rendered as 2023-03-15T14:03:12Z
	 *
	 * @param truncatedTo is the temporal unit to which the date should be truncated
	 * @return a zulu time string
	 */
	public String toISO8601ZuluTime(TemporalUnit truncatedTo) {
		OffsetDateTime offsetDateTime = toOffsetDateTime();
		if (truncatedTo != null) {
			offsetDateTime = offsetDateTime.truncatedTo(truncatedTo);
		}
		// An instant in java is in Java Time-Scale, which is an approximation of UTC a.k.a. Zulu Time (check Instant's java doc, very interesting)
		return offsetDateTime.toInstant().toString();
	}

	/**
	 * Formats the given date using the given timestamp format in french locale
	 *
	 * @param timeStampFormat is format to user
	 * @return the formatted date
	 */
	public String toTimestampUsingFormatInFrench(String timeStampFormat) {
		DateFormat dateFormat = new SimpleDateFormat(timeStampFormat, Locale.FRENCH);
		return dateFormat.format(getDate());
	}

	/**
	 * Produces a String in ISO8601 format as a local datetime, no time zone like 2014-02-07T16:25
	 *
	 * @return the formatted date
	 */
	public String toISO8601LocalDateTime() {
		return toLocalDateTime().toString();
	}

	/**
	 * Converts date to OffsetDateTime using the current time zone
	 *
	 * @return the offset date time object
	 */
	public OffsetDateTime toOffsetDateTime() {
		return OffsetDateTime.ofInstant(Instant.ofEpochMilli(getDate().getTime()), ZoneId.systemDefault());
	}

	/**
	 * Converts date to LocalDateTime using the current time zone
	 *
	 * @return local date time object
	 */
	public LocalDateTime toLocalDateTime() {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(getDate().getTime()), ZoneId.systemDefault());
	}

	/**
	 * Returns this date constant current date and time in a Zulu Time formatted string.
	 *
	 * @return the formatted date
	 */
	public String toISO8601ZuluTimeNoMillis() {
		return toISO8601ZuluTime(ChronoUnit.SECONDS);
	}

	public String toHourMinuteString() {
		return toString("HH:mm");
	}

	/**
	 * @return formated as 2015-11-02 16:45:12
	 */
	public String toISOTimestamp() {
		return toISOFormat();
	}

	/**
	 *
	 * @return date formated as YYYY-MM-DD
	 */
	public String toISOFormat() {
		return toString(ISO_FORMAT);
	}

	/**
	 *
	 * @return date formater as dd/MM/yyyy
	 */
	public String toFrenchFormat() {
		return toString(FRENCH_FORMAT);
	}

	/**
	 *
	 * @return date formatted as "EEEEEEEEEEEEEEE dd MMMMMMMMMMMMMMMMMMMMMMM yyyy HH'h'mm"
	 */
	public String toFrenchLongFormat() {
		return toString(DATETIME_FORMAT_FRENCH_LONG);
	}

	/**
	 * Formats the date to yyyy-MM-dd-HH-mm
	 *
	 * @return the date formatted
	 */
	public String toDateTimeForFile() {
		return toString(DATE_TIME_FOR_FILE_FORMAT);
	}

	/**
	 * @param day the DateConstant to compare with
	 * @return true if the day is the same as the argument
	 */
	public boolean isSameDay(DateConstant day) {
		return day.getYear() == getYear() && day.getHumanMonth() == getHumanMonth() && day.getDayOfMonth() == getDayOfMonth();
	}

	/**
	 *
	 * @return true if the date is weekday, false if the date is Saturday or Sunday
	 */
	public boolean isWeekDay() {
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
	}

	/**
	 *
	 * @return true if current date is before 12:00:00
	 */
	public boolean isMorning() {
		return getHour() < 12;
	}

	/**
	 * Computes the age in years of an event occurred on this object date, relatively to current date, given in parameter. Granularity is day.
	 *
	 * @param now current date
	 * @return age in years
	 */
	public int getAge(DateConstant now) {
		int age = now.getYear() - getYear();
		// Si on n'a pas passé la date d'anniversaire
		if (now.getHumanMonth() < getHumanMonth() || (now.getHumanMonth() == getHumanMonth() && now.getDayOfMonth() < getDayOfMonth())) {
			age--;
		}
		return age;
	}

	/**
	 * @param date to compare with
	 * @return true if the current datebuilder's date is equals(date) or after(date)
	 */
	public boolean isSameOrAfter(Date date) {
		return this.getDate().equals(date) || this.getDate().after(date);
	}

	/**
	 * @param date to compare with
	 * @return true if the current datebuilder's date is equals(date) or before(date)
	 */
	public boolean isSameOrBefore(Date date) {
		return this.getDate().equals(date) || this.getDate().before(date);
	}

	/**
	 *
	 * @param start inclusive
	 * @param end inclusive
	 * @return true if the current datebuilder's date is between start and end
	 */
	public boolean isBetween(Date start, Date end) {
		return (isSameOrAfter(start) && isSameOrBefore(end));
	}

	/**
	 * Returns the age of this builder's date. Age is computed in years.
	 *
	 * @return a string representing the age in years
	 */
	public String toAge() {
		LocalDate startDateInclusive = getLocalDate();
		LocalDate endDateExclusive = LocalDate.now();
		return String.valueOf(Period.between(startDateInclusive, endDateExclusive).getYears());
	}

	private LocalDate getLocalDate() {
		return LocalDate.of(getYear(), getHumanMonth(), getDayOfMonth());
	}

	@Override
	public String toString() {
		// Convenient method for debugging
		return toISOTimestamp();
	}
}
