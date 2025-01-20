package br.ce.wcaquino.taskbackend.utils;

import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class DateUtilsTest {
	
	@Test
    public void shouldReturnTrueForFutureDates() {
        LocalDate date = LocalDate.of(2067, 5, 10);
        Assert.assertTrue(DateUtils.isEqualOrFutureDate(date)); 
    }
	
	@Test
    public void shouldReturnFalseForPastDates() {
        LocalDate pastDate = LocalDate.of(2005, 8, 20);
        Assert.assertFalse(DateUtils.isEqualOrFutureDate(pastDate));
    }

    @Test
    public void shouldReturnTrueForCurrentDate() {
        LocalDate todayDate = LocalDate.now();
        Assert.assertTrue(DateUtils.isEqualOrFutureDate(todayDate));
    }


}
