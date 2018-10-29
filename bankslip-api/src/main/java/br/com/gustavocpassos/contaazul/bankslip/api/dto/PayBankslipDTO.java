package br.com.gustavocpassos.contaazul.bankslip.api.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PayBankslipDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("payment_date")
	@NotNull(message = "payment_date is required")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date paymentDate;

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

}
