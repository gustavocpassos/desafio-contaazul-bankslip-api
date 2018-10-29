package br.com.gustavocpassos.contaazul.bankslip.api.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "bankslip")
public class Bankslip {

	@Id
	@Column(nullable = false, unique = true)
	private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "due_date", nullable = false)
	private Date dueDate;

	@Column(name = "total_in_cents", nullable = false)
	private BigDecimal totalInCents;

	@Column(nullable = false)
	private String customer;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BankslipStatus status;
	
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "payment_date")
	private Date paymentDate;
    
    @Column
	private BigDecimal fine;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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

	public BankslipStatus getStatus() {
		return status;
	}

	public void setStatus(BankslipStatus status) {
		this.status = status;
	}
	
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	public BigDecimal getFine() {
		return fine;
	}

	public void setFine(BigDecimal fine) {
		this.fine = fine;
	}

	public enum BankslipStatus {
	    PENDING,
	    PAID,
	    CANCELED
	}
	
}
