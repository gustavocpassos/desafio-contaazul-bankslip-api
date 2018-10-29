package br.com.gustavocpassos.contaazul.bankslip.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewBankslipDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("due_date")
	@NotNull(message = "Due_date is required")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dueDate;

	@JsonProperty("total_in_cents")
	@NotNull(message = "total_in_cents is required")
	private BigDecimal totalInCents;

	@NotEmpty(message = "customer is required")
	private String customer;

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getTotalInCents() {
		return totalInCents;
	}

	public void setTotalInCents(BigDecimal totalInCents) {
		this.totalInCents = totalInCents;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

}
