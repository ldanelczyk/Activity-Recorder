package pl.dels.toolsprovider;

import java.time.LocalTime;

public class ToolProvider {

	public static double round(double value, int places) {

		if (places < 0)

			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);

		value = value * factor;

		long tmp = Math.round(value);

		return (double) tmp / factor;
	}

	public static String convertTimeToFileName(LocalTime localTime) {

		String localTimeToConvert = String.valueOf(localTime);

		String localTimeConverted = localTimeToConvert.replace(':', '_');

		return localTimeConverted;
	}
}
