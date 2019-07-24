package pl.dels.toolsprovider;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MathToolsProviderTest {

	@Test
	void roundShouldReturnRoundedNumber() {

		//given
		//when
		double roundedNumber = MathToolsProvider.round(1000.25555558, 3);

		//then
		assertEquals(1000.256, roundedNumber);
	}

	@Test
	void roundShouldReturnException() {

		//given
		//when
		//then
		assertThrows(IllegalArgumentException.class, () -> MathToolsProvider.round(1000.25555558, -3));
	}
}
