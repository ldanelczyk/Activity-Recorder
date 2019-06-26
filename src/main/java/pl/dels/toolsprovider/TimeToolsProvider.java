package pl.dels.toolsprovider;

import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeToolsProvider {

	private final static double DIVIDER_OF_SECONDS = 1000000000;

	private final static double DIVIDER_OF_HOUR = 3600;

	private final static double ROUND_CONSTANCE = 1000;

	public static double downtimeCounter(Timestamp startDateTime, Timestamp stopDateTime) {

		LocalDateTime localStartDateTime = startDateTime.toLocalDateTime();

		LocalDateTime localStopDateTime = stopDateTime.toLocalDateTime();

		long differenceBetweenDateInNanoseconds = ChronoUnit.NANOS.between(localStartDateTime, localStopDateTime);

		double downtime = differenceBetweenDateInNanoseconds / DIVIDER_OF_SECONDS / DIVIDER_OF_HOUR;

		double downtimeInHour = Math.floor(downtime * ROUND_CONSTANCE) / ROUND_CONSTANCE;

		return downtimeInHour;
	}

	public static String convertTimeToFileName(LocalTime localTime) {

		String localTimeToConvert = String.valueOf(localTime);

		String localTimeConverted = localTimeToConvert.replace(':', '_');

		return localTimeConverted;
	}
}
