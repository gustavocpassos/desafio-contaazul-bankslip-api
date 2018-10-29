package br.com.gustavocpassos.contaazul.bankslip.api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.gustavocpassos.contaazul.bankslip.api.service.FineService;

@RunWith(MockitoJUnitRunner.class)
public class FineServiceTest {

	@InjectMocks
	private FineService fineService;

	@Test
	public void shouldCalculateWithoutFineIfDueDateBeforeToday() {
		BigDecimal totalInCents = new BigDecimal(Math.random());
		LocalDate yesterday = LocalDate.now().minus(Period.ofDays(1));

		BigDecimal calculatedFine = fineService.calculateFine(new Date(), toDate(yesterday), totalInCents);

		Assert.assertEquals(0, calculatedFine.signum());
	}

	@Test
	public void shouldCalculateWithoutFineIfDueDateIsToday() {
		BigDecimal totalInCents = new BigDecimal(Math.random());
		LocalDate today = LocalDate.now();

		BigDecimal calculatedFine = fineService.calculateFine(new Date(), toDate(today), totalInCents);
		Assert.assertEquals(0, calculatedFine.signum());
	}

	@Test
	public void shouldCalculateWithZeroPointFivePercentFineIfDueDateIsOneDayLate() {
		BigDecimal totalInCents = new BigDecimal(Math.random());
		int plusDays = 1;
		LocalDate tomorrow = LocalDate.now().plus(Period.ofDays(plusDays));

		BigDecimal calculatedFine = fineService.calculateFine(new Date(), toDate(tomorrow), totalInCents);

		BigDecimal expected = totalInCents.multiply(new BigDecimal(0.005).multiply(new BigDecimal(plusDays)));
		Assert.assertEquals(expected, calculatedFine);
	}

	@Test
	public void shouldCalculateWithZeroPointFivePercentFineIfDueDateLessThanTenDaysLate() {
		BigDecimal totalInCents = new BigDecimal(Math.random());
		int plusDays = 9;
		LocalDate localDate = LocalDate.now().plus(Period.ofDays(plusDays));

		BigDecimal calculatedFine = fineService.calculateFine(new Date(), toDate(localDate), totalInCents);

		BigDecimal expected = totalInCents.multiply(new BigDecimal(0.005).multiply(new BigDecimal(plusDays)));
		Assert.assertEquals(expected, calculatedFine);
	}

	@Test
	public void shouldCalculateWithZeroPointFivePercentFineIfDueDateIsTenDaysLate() {
		BigDecimal totalInCents = new BigDecimal(Math.random());
		int plusDays = 10;
		LocalDate localDate = LocalDate.now().plus(Period.ofDays(plusDays));

		BigDecimal calculatedFine = fineService.calculateFine(new Date(), toDate(localDate), totalInCents);

		BigDecimal expected = totalInCents.multiply(new BigDecimal(0.005).multiply(new BigDecimal(plusDays)));
		Assert.assertEquals(expected, calculatedFine);
	}

	@Test
	public void shouldCalculateWithOnePercentFineIfDueDateIsElevenDaysLate() {
		BigDecimal totalInCents = new BigDecimal(Math.random());
		int plusDays = 11;
		LocalDate localDate = LocalDate.now().plus(Period.ofDays(plusDays));

		BigDecimal calculatedFine = fineService.calculateFine(new Date(), toDate(localDate), totalInCents);

		BigDecimal expected = totalInCents.multiply(new BigDecimal(0.01).multiply(new BigDecimal(plusDays)));
		Assert.assertEquals(expected, calculatedFine);
	}

	@Test
	public void shouldCalculateWithOnePercentFineIfDueDateMoreThanTenDaysLate() {
		BigDecimal totalInCents = new BigDecimal(Math.random());
		int plusDays = 18;
		LocalDate localDate = LocalDate.now().plus(Period.ofDays(plusDays));

		BigDecimal calculatedFine = fineService.calculateFine(new Date(), toDate(localDate), totalInCents);

		BigDecimal expected = totalInCents.multiply(new BigDecimal(0.01).multiply(new BigDecimal(plusDays)));
		Assert.assertEquals(expected, calculatedFine);
	}
	
	@Test
	public void shouldBeZeroPercentOfFineWithLessThanZeroDaysDelay() {
		BigDecimal finePercent = fineService.getFinePercent(-1);
		Assert.assertEquals(new BigDecimal(0), finePercent);
	}
	
	@Test
	public void shouldBeZeroPercentOfFineWithZeroDaysDelay() {
		BigDecimal finePercent = fineService.getFinePercent(0);
		Assert.assertEquals(new BigDecimal(0), finePercent);
	}
	
	@Test
	public void shouldBeZeroPointFivePercentOfFineWithOneDayDelay() {
		BigDecimal finePercent = fineService.getFinePercent(1);
		Assert.assertEquals(new BigDecimal(0.005), finePercent);
	}
	
	@Test
	public void shouldBeZeroPointFivePercentOfFineWithNineDaysDelay() {
		BigDecimal finePercent = fineService.getFinePercent(9);
		Assert.assertEquals(new BigDecimal(0.005), finePercent);
	}
	
	@Test
	public void shouldBeZeroPointFivePercentOfFineWithTenDaysDelay() {
		BigDecimal finePercent = fineService.getFinePercent(10);
		Assert.assertEquals(new BigDecimal(0.005), finePercent);
	}
	
	@Test
	public void shouldBeOnePercentOfFineWithElevenDaysDelay() {
		BigDecimal finePercent = fineService.getFinePercent(11);
		Assert.assertEquals(new BigDecimal(0.01), finePercent);
	}
	
	@Test
	public void shouldBeOnePercentOfFineWithMoreThanTenDaysDelay() {
		BigDecimal finePercent = fineService.getFinePercent(35);
		Assert.assertEquals(new BigDecimal(0.01), finePercent);
	}
	
	

	private Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
