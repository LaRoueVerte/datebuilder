package com.laroueverte.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class DateBuilderTest extends UnitTest {

	@Nested
	class dateTime {
		@Test
		void without_ms_should_generate_date_with_0_ms() {
			// Act
			DateBuilder date = DateBuilder.dateTime(2022, 11, 27, 2, 58, 60);

			// Assert
			assertThat(date.getMillisecond()).isEqualTo(0);
		}

		@Test
		void with_ms_should_generate_date_with_correct_ms() {
			// Act
			DateBuilder date = DateBuilder.dateTime(2022, 11, 27, 2, 58, 60, 456);

			// Assert
			assertThat(date.getMillisecond()).isEqualTo(456);
		}
	}

	@Test
	void dateTimeMillisecond() {
		// Arrange
		DateBuilder date = DateBuilder.dateTime(2022, 11, 27, 2, 58, 60, 512);

		// Act
		date.setMillisecond(858);
		int result = date.getMillisecond();

		// Assert
		assertThat(result).isEqualTo(858);
	}

	@Test
	void setMillisecond() {
		// Arrange
		DateBuilder date = DateBuilder.dateTime(2022, 11, 27, 2, 58, 60, 512);

		// Act
		date.setMillisecond(858);
		int result = date.getMillisecond();

		// Assert
		assertThat(result).isEqualTo(858);
	}

	@Test
	void xToReach() throws ParseException {
		DateBuilder isoTimestamp = DateBuilder.isoTimestamp("2015-09-01 08:20:00");
		assertThat(isoTimestamp.getMillisecondsToReach(DateBuilder.isoTimestamp("2015-09-01 08:20:01").getDate())).isEqualTo(1000);
		assertThat(isoTimestamp.getSecondsToReach(DateBuilder.isoTimestamp("2015-09-01 08:20:30").getDate())).isEqualTo(30);
		assertThat(isoTimestamp.getMinutesToReach(DateBuilder.isoTimestamp("2015-09-01 08:40:00").getDate())).isEqualTo(20);
		assertThat(isoTimestamp.getHoursToReach(DateBuilder.isoTimestamp("2015-09-01 11:20:00").getDate())).isEqualTo(3);
		assertThat(isoTimestamp.getDaysToReach(DateBuilder.isoTimestamp("2015-09-05 08:20:00").getDate())).isEqualTo(4);
	}

	@Test
	void iso8601() {
		// Dates in three correct formats and the corresponding date object
		String iso8601GMT = "2017-02-13T08:08:43Z";
		String iso8601Paris = "2017-02-13T09:08:43+01:00";
		String iso8601Minus1 = "2017-02-13T07:08:43-01:00";
		String iso8601ParisBis = "2017-02-13T09:08:43+0100";
		String iso8601Local = "2017-02-13T09:08:43";

		Date okDate = DateBuilder.date(2017, 2, 13).setHour(9).setMinute(8).setSecond(43).getDate();

		String iso8601ParisSummer = "2017-07-13T09:08:43+02:00";
		Date okDateSummer = DateBuilder.date(2017, 7, 13).setHour(9).setMinute(8).setSecond(43).getDate();

		// Check calling directly DateUtils and using DateBuilder to verify all expected behaviours

		/////////////////////////////////////
		// GMT format (ending with Z)
		assertThat(DateBuilder.iso8601WithTimeZone(iso8601GMT).getTimeInMillis()).isEqualTo(okDate.getTime());
		try {
			DateBuilder.iso8601InLocalDateTime(iso8601GMT);
			fail("should be an error");
		} catch (DateTimeParseException e) {
			// OK
		}
		assertThat(DateBuilder.iso8601(iso8601GMT).getDate().getTime()).isEqualTo(okDate.getTime());

		/////////////////////////////////////
		// Time zone format (containing +01:00)
		assertThat(DateBuilder.iso8601WithTimeZone(iso8601Paris).getTimeInMillis()).isEqualTo(okDate.getTime());
		try {
			DateBuilder.iso8601InLocalDateTime(iso8601Paris);
			fail("should be an error");
		} catch (DateTimeParseException e) {
			// OK
		}
		assertThat(DateBuilder.iso8601(iso8601Paris).getTimeInMillis()).isEqualTo(okDate.getTime());

		/////////////////////////////////////
		// Time zone format (containing -01:00)
		assertThat(DateBuilder.iso8601WithTimeZone(iso8601Minus1).getTimeInMillis()).isEqualTo(okDate.getTime());
		try {
			DateBuilder.iso8601InLocalDateTime(iso8601Minus1);
			fail("should be an error");
		} catch (DateTimeParseException e) {
			// OK
		}
		assertThat(DateBuilder.iso8601(iso8601Minus1).getTimeInMillis()).isEqualTo(okDate.getTime());

		/////////////////////////////////////
		// Time zone format (containing +0100) is not supported by java ?
		try {
			DateBuilder.iso8601WithTimeZone(iso8601ParisBis);
			fail("should be an error");
		} catch (DateTimeParseException e) {
			// OK
		}
		try {
			DateBuilder.iso8601InLocalDateTime(iso8601ParisBis);
			fail("should be an error");
		} catch (DateTimeParseException e) {
			// OK
		}
		try {
			DateBuilder.iso8601(iso8601ParisBis);
			fail("should be an error");
		} catch (DateTimeParseException e) {
			// OK
		}

		/////////////////////////////////////
		// Local format
		try {
			DateBuilder.iso8601WithTimeZone(iso8601Local);
			fail("should be an error");
		} catch (DateTimeParseException e) {
			// OK
		}
		assertThat(DateBuilder.iso8601InLocalDateTime(iso8601Local).getTimeInMillis()).isEqualTo(okDate.getTime());
		assertThat(DateBuilder.iso8601(iso8601Local).getDate().getTime()).isEqualTo(okDate.getTime());

		/////////////////////////////////////
		// Time zone format summer time (containing +02:00)
		assertThat(DateBuilder.iso8601WithTimeZone(iso8601ParisSummer).getTimeInMillis()).isEqualTo(okDateSummer.getTime());
		try {
			DateBuilder.iso8601InLocalDateTime(iso8601ParisSummer);
			fail("should be an error");
		} catch (DateTimeParseException e) {
			// OK
		}
		assertThat(DateBuilder.iso8601(iso8601ParisSummer).getDate().getTime()).isEqualTo(okDateSummer.getTime());
	}

	@Test
	void testMoveToPreviousDay() {
		// Trim to week go back to previous monday
		assertThat(DateBuilder.date(2019, 10, 13).moveToPreviousDayOfWeek(2).getDayOfMonth()).isEqualTo(7);
		assertThat(DateBuilder.date(2019, 9, 30).moveToPreviousDayOfWeek(2).getDayOfMonth()).isEqualTo(30);
		assertThat(DateBuilder.date(2019, 10, 2).moveToPreviousDayOfWeek(2).getDayOfMonth()).isEqualTo(30);
		assertThat(DateBuilder.date(2019, 10, 6).moveToPreviousDayOfWeek(2).getDayOfMonth()).isEqualTo(30);
		assertThat(DateBuilder.date(2019, 10, 7).moveToPreviousDayOfWeek(2).getDayOfMonth()).isEqualTo(7);
		assertThat(DateBuilder.date(2019, 10, 8).moveToPreviousDayOfWeek(2).getDayOfMonth()).isEqualTo(7);
		assertThat(DateBuilder.date(2019, 10, 10).moveToPreviousDayOfWeek(2).getDayOfMonth()).isEqualTo(7);
	}

	@Nested
	class moveToNextLocalUniqueTime {

		@Nested
		class winter_dst {
			@ParameterizedTest
			@MethodSource("com.laroueverte.utils.DateConstantTest#getIntsFor2Hours")
			void doesnt_move_before(int minuteSpan) {
				// Arrange
				DateBuilder date = DateBuilder.dateTime(2024, 10, 26, 23, 59).addMinutes(minuteSpan);
				DateBuilder source = date.builder();

				// Act
				date.moveToNextLocalUniqueTime();

				// Assert
				assertThat(date).isEqualTo(source);
			}

			@ParameterizedTest
			@MethodSource("com.laroueverte.utils.DateConstantTest#getIntsFor2Hours")
			void should_move(int minuteSpan) {
				// Arrange
				DateBuilder date = DateBuilder.dateTime(2024, 10, 27, 1, 59).addMinutes(minuteSpan);

				// Act
				date.moveToNextLocalUniqueTime();

				// Assert
				assertThat(date).isEqualTo(DateBuilder.dateTime(2024, 10, 27, 3, 0));
			}

			@Test
			void doesnt_move_after() {
				// Arrange
				DateBuilder date = DateBuilder.dateTime(2024, 10, 27, 3, 0);
				DateBuilder source = date.builder();

				// Act
				date.moveToNextLocalUniqueTime();

				// Assert
				assertThat(date).isEqualTo(source);
			}
		}

		@Nested
		class summer_dst {
			@ParameterizedTest
			@MethodSource("com.laroueverte.utils.DateConstantTest#getIntsFor2Hours")
			void doesnt_move_around(int minuteSpan) {
				// Arrange
				DateBuilder date = DateBuilder.dateTime(2024, 3, 31, 0, 29).addMinutes(minuteSpan);
				DateBuilder source = date.builder();

				// Act
				date.moveToNextLocalUniqueTime();

				// Assert
				assertThat(date).isEqualTo(source);
			}
		}
	}
}
