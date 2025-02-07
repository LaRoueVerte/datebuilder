package com.laroueverte.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.util.Locale;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class DateConstantTest {
	static IntStream getIntsFor2Hours() {
		return IntStream.range(1, 120);
	}

	@Nested
	class getWeekOfYear {
		@ParameterizedTest
		@CsvSource({ "2001-01-01, 1", "2012-12-12, 50", "2020-12-28, 53", "2021-01-01, 53", "2021-12-31, 52", "2022-01-01, 52", "1817-10-17, 42" })
		void should_return_correct_results_in_french(String dateIso, int expectedWeekNumber) throws ParseException {
			// Arrange
			DateConstant dateConstant = DateBuilder.iso(dateIso);
			Locale locale = Locale.FRENCH;
			// Act
			int weekOfYear = dateConstant.getWeekOfYear(locale);
			// Assert
			assertThat(weekOfYear).isEqualTo(expectedWeekNumber);
		}

		@ParameterizedTest
		@CsvSource({ "2001-01-01, 1", "2012-12-12, 50", "2020-12-28, 1", "2021-01-01, 1", "2021-12-31, 1", "2022-01-01, 1", "1817-10-17, 42" })
		void getWeekOfYear_should_return_correct_results_in_english(String dateIso, int expectedWeekNumber) throws ParseException {
			// Arrange
			DateConstant dateConstant = DateBuilder.iso(dateIso);
			Locale locale = Locale.ENGLISH;
			// Act
			int weekOfYear = dateConstant.getWeekOfYear(locale);
			// Assert
			assertThat(weekOfYear).isEqualTo(expectedWeekNumber);
		}
	}

	@Nested
	class toISO8601Local {
		private DateConstant dateWithMilliseconds;
		private DateConstant dateWithoutMilliseconds;

		@BeforeEach
		void init() {
			dateWithMilliseconds = DateBuilder.dateTime(2022, 11, 28, 10, 11, 15, 568).constant();
			dateWithoutMilliseconds = DateBuilder.dateTime(2022, 11, 28, 10, 11, 15, 0).constant();

		}

		@Test
		void with_force_false_should_return_date_with_ms_if_ms_is_not_0() {
			// Act
			String result = dateWithMilliseconds.toISO8601Local(false);

			// Assert
			assertThat(result).isEqualTo("2022-11-28T10:11:15.568");
		}

		@Test
		void with_force_false_should_return_date_without_ms_if_ms_is_0() {
			// Act
			String result = dateWithoutMilliseconds.toISO8601Local(false);

			// Assert
			assertThat(result).isEqualTo("2022-11-28T10:11:15");
		}

		@Test
		void with_default_should_return_date_with_ms_if_ms_is_not_0() {
			// Act
			String result = dateWithMilliseconds.toISO8601Local();

			// Assert
			assertThat(result).isEqualTo("2022-11-28T10:11:15.568");
		}

		@Test
		void with_default_should_return_date_without_ms_if_ms_is_0() {
			// Act
			String result = dateWithoutMilliseconds.toISO8601Local();

			// Assert
			assertThat(result).isEqualTo("2022-11-28T10:11:15");
		}

		@Test
		void with_force_true_should_return_date_with_ms_if_ms_is_not_0() {
			// Act
			String result = dateWithMilliseconds.toISO8601Local(true);

			// Assert
			assertThat(result).isEqualTo("2022-11-28T10:11:15.568");
		}

		@Test
		void with_force_true_should_return_date_with_ms_if_ms_is_0() {
			// Act
			String result = dateWithoutMilliseconds.toISO8601Local(true);

			// Assert
			assertThat(result).isEqualTo("2022-11-28T10:11:15.000");
		}

		@Test
		void should_return_date_with_ms_if_ms_and_second_is_0() {
			// Act
			String result = dateWithoutMilliseconds.builder().trimToMinute().toISO8601Local(true);

			// Assert
			assertThat(result).isEqualTo("2022-11-28T10:11:00.000");
		}

		@Test
		void should_return_date_with_ms_if_ms_and_second_and_minute_is_0() {
			// Act
			String result = dateWithoutMilliseconds.builder().trimToHour().toISO8601Local(true);

			// Assert
			assertThat(result).isEqualTo("2022-11-28T10:00:00.000");
		}
	}

	@ParameterizedTest
	@CsvSource({ "2022-11-24T10:10, 2023-01-12T09:30, 2022-11-24T10:10", "1969-12-31T00:00, 1969-12-31T00:00, 1969-12-31T00:00",
			"1969-12-31T00:01, 1969-12-31T00:00, 1969-12-31T00:00", "1969-12-31T00:00, 1969-12-31T00:01, 1969-12-31T00:00" })
	void min_should_return_correct_result(String d1Iso, String d2Iso, String expectedIso) {
		// Arrange
		DateConstant d1 = DateBuilder.iso8601(d1Iso);
		DateConstant d2 = DateBuilder.iso8601(d2Iso);
		// Act
		DateConstant result = DateConstant.min(d1, d2);
		// Assert
		DateConstant expected = DateBuilder.iso8601(expectedIso);
		assertThat(result).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource({ "2022-11-24T10:10, 2023-01-12T09:30, 2023-01-12T09:30", "1969-12-31T00:00, 1969-12-31T00:00, 1969-12-31T00:00",
			"1969-12-31T00:01, 1969-12-31T00:00, 1969-12-31T00:01", "1969-12-31T00:00, 1969-12-31T00:01, 1969-12-31T00:01" })
	void max_should_return_correct_result(String d1Iso, String d2Iso, String expectedIso) {
		// Arrange
		DateConstant d1 = DateBuilder.iso8601(d1Iso);
		DateConstant d2 = DateBuilder.iso8601(d2Iso);
		// Act
		DateConstant result = DateConstant.max(d1, d2);
		// Assert
		DateConstant expected = DateBuilder.iso8601(expectedIso);
		assertThat(result).isEqualTo(expected);
	}

	@Test
	public void getMillisecond() {
		// Arrange
		DateConstant dateWithMilliseconds = DateBuilder.dateTime(2022, 11, 28, 10, 11, 15, 568).constant();

		// Act
		int result = dateWithMilliseconds.getMillisecond();

		// Assert
		assertThat(result).isEqualTo(568);
	}

	@ParameterizedTest
	@CsvSource({ "2005-06-23T09:00, 0", "2006-06-23T09:00, 1", "2023-06-23T11:23, 18", "2023-06-23T06:23, 18", "2023-06-22T06:23, 17", "2023-06-24T11:23, 18" })
	void getAge(String nowIso, int expectedAge) {
		// Arrange
		DateConstant eventDate = DateBuilder.iso8601("2005-06-23T09:00");
		DateConstant now = DateBuilder.iso8601(nowIso);
		// Act
		int age = eventDate.getAge(now);
		// Assert
		assertThat(age).isEqualTo(expectedAge);
	}

	@Nested
	class getTimeZoneOffset {
		@Test
		void should_return_1h_in_winter_time_with_date_moved() {
			// Arrange
			DateConstant date = DateBuilder.dateTime(2024, 7, 1, 10, 0).addMonth(-6);

			// Act
			int result = date.getTimeZoneOffset();

			// Assert
			assertThat(result).isEqualTo(60);
		}

		@Test
		void should_return_1h_in_winter_time_with_date_set() {
			// Arrange
			DateConstant date = DateBuilder.dateTime(2024, 1, 1, 10, 0);

			// Act
			int result = date.getTimeZoneOffset();

			// Assert
			assertThat(result).isEqualTo(60);
		}

		@Test
		void should_return_1h_in_winter_time() {
			// Arrange
			DateConstant date = DateBuilder.dateTime(2024, 1, 1, 10, 0);

			// Act
			int result = date.getTimeZoneOffset();

			// Assert
			assertThat(result).isEqualTo(60);
		}

		@Test
		void should_return_2h_in_summer_time_with_date_moved() {
			// Arrange
			DateConstant date = DateBuilder.dateTime(2024, 1, 1, 10, 0).addMonth(6);

			// Act
			int result = date.getTimeZoneOffset();

			// Assert
			assertThat(result).isEqualTo(2 * 60);
		}

		@Test
		void should_return_2h_in_summer_time_with_date_set() {
			// Arrange
			DateConstant date = DateBuilder.dateTime(2024, 7, 1, 10, 0);

			// Act
			int result = date.getTimeZoneOffset();

			// Assert
			assertThat(result).isEqualTo(2 * 60);
		}

		@Test
		void should_return_2h_in_the_first_2h_to_3h_span_in_winter_DST() {
			// Arrange
			DateConstant date = DateBuilder.dateTime(2024, 10, 27, 1, 30).addHours(1);

			// Act
			int result = date.getTimeZoneOffset();

			// Assert : local hour is 2
			assertThat(date.getHour()).isEqualTo(2);
			// Assert : time zone is still summer
			assertThat(result).isEqualTo(2 * 60);
		}

		@Test
		void should_return_1h_in_the_second_2h_to_3h_span_in_winter_DST() {
			// Arrange
			DateConstant date = DateBuilder.dateTime(2024, 10, 27, 1, 30).addHours(2);

			// Act
			int result = date.getTimeZoneOffset();

			// Assert
			// Assert : local hour is 2
			assertThat(date.getHour()).isEqualTo(2);
			// Assert : time zone is winter now
			assertThat(result).isEqualTo(1 * 60);
		}

		@Test
		void should_return_1h_in_the_second_2h_to_3h_span_in_winter_DST_when_set() {
			// Arrange : this local date is non unique, behavior is not very defined... This code will produce 2h30 GMT+1 (after DST)
			DateConstant date = DateBuilder.dateTime(2024, 10, 27, 2, 30);

			// Act
			int result = date.getTimeZoneOffset();

			// Assert : local hour is 2
			assertThat(date.getHour()).isEqualTo(2);
			// Assert : time zone is winter
			assertThat(result).isEqualTo(1 * 60);
		}
	}

	@Nested
	class isALocalNonUniqueTime {

		@Nested
		class summer_dst {

			@ParameterizedTest
			@MethodSource("com.laroueverte.utils.DateConstantTest#getIntsFor2Hours")
			void should_return_false_during_summer_dst(int minSpan) {
				// Arrange
				DateConstant date = DateBuilder.dateTime(2024, 3, 31, 0, 59).addMinutes(minSpan);

				// Act
				boolean result = date.isALocalNonUniqueTime();

				// Assert
				assertThat(result).isFalse();
			}
		}

		@Nested
		class winter_dst {
			@ParameterizedTest
			@ValueSource(ints = { 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59 })
			void should_return_false_before_1hXX(int min) {
				// Arrange
				DateConstant date = DateBuilder.dateTime(2024, 10, 27, 1, 0).addMinutes(min);

				// Act
				boolean result = date.isALocalNonUniqueTime();

				// Assert
				assertThat(result).isFalse();
			}

			@ParameterizedTest
			@ValueSource(ints = { 1, 2, 3, 55, 56, 57, 58, 59, 60, 61, 115, 116, 117, 118, 119 })
			void should_return_true_before_from_2h00_to_2h59(int min) {
				// Arrange
				DateConstant date = DateBuilder.dateTime(2024, 10, 27, 1, 59).addMinutes(min);

				// Act
				boolean result = date.isALocalNonUniqueTime();

				// Assert
				assertThat(result).isTrue();
			}

			@ParameterizedTest
			@ValueSource(ints = { 0, 1, 2, 3, 4, 5 })
			void should_return_false_after_3hXX(int min) {
				// Arrange
				DateConstant date = DateBuilder.dateTime(2024, 10, 27, 1, 0).addHours(3).addMinutes(min);

				// Act
				boolean result = date.isALocalNonUniqueTime();

				// Assert
				assertThat(result).isFalse();
			}

			@Test
			void should_return_true_at_1h59() {
				// Arrange
				DateConstant date = DateBuilder.dateTime(2024, 10, 27, 1, 59);

				// Act
				boolean result = date.isALocalNonUniqueTime();

				// Assert
				assertThat(result).isFalse();
			}

			@Test
			void should_return_false_at_1h59() {
				// Arrange
				DateConstant date = DateBuilder.dateTime(2024, 10, 27, 1, 59);

				// Act
				boolean result = date.isALocalNonUniqueTime();

				// Assert
				assertThat(result).isFalse();
			}
		}
	}
}
