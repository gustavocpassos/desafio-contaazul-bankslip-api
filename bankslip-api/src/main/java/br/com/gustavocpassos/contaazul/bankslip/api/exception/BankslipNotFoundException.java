package br.com.gustavocpassos.contaazul.bankslip.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BankslipNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1540250379035859042L;

	public BankslipNotFoundException(String message) {
		super(message);
	}

}
