package br.com.gustavocpassos.contaazul.bankslip.api.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class FineService {

	private static final int RANGE_DAYS = 10;
	public static final BigDecimal LESS_THAN_10_DAYS_FINE_PERCENT = new BigDecimal(0.005);
	public static final BigDecimal MORE_THAN_10_DAYS_FINE_PERCENT = new BigDecimal(0.01);

	public BigDecimal calculateFine(Date dueDate, Date paymentDate, BigDecimal totalInCents) {
		int delayedDays = calculateDelayedDays(dueDate, paymentDate);
		BigDecimal finePercent = getFinePercent(delayedDays);
		return totalInCents.multiply(new BigDecimal(delayedDays).multiply(finePercent));
	}

	private int calculateDelayedDays(Date dueDate, Date paymentDate) {
		LocalDate localDueDate = Instant.ofEpochMilli(dueDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate localPaymentDate = Instant.ofEpochMilli(paymentDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		return Period.between(localDueDate, localPaymentDate).getDays();
	}

	protected BigDecimal getFinePercent(int delayedDays) {
		if (delayedDays <= 0) {
			return new BigDecimal(0);
		}
		if (delayedDays <= RANGE_DAYS) {
			return LESS_THAN_10_DAYS_FINE_PERCENT;
		}
		return MORE_THAN_10_DAYS_FINE_PERCENT;
	}
}
