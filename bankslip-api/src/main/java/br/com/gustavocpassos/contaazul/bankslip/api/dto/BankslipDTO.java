package br.com.gustavocpassos.contaazul.bankslip.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.gustavocpassos.contaazul.bankslip.api.utils.MoneySerializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankslipDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	@JsonProperty("due_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dueDate;

	@JsonProperty("payment_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date paymentDate;

	@JsonSerialize(using = MoneySerializer.class)
	@JsonProperty("total_in_cents")
	private BigDecimal totalInCents;

	private String customer;

	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal fine;

	private String status;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(final Date dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getTotalInCents() {
		return totalInCents;
	}

	public void setTotalInCents(final BigDecimal totalInCents) {
		this.totalInCents = totalInCents;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(final String customer) {
		this.customer = customer;
	}

	public BigDecimal getFine() {
		return fine;
	}

	public void setFine(final BigDecimal fine) {
		this.fine = fine;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

}