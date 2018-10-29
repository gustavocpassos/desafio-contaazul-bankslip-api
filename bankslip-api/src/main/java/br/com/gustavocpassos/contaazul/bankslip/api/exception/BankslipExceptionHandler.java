package br.com.gustavocpassos.contaazul.bankslip.api.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

@ControllerAdvice
public class BankslipExceptionHandler {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Error httpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return new Error(HttpStatus.BAD_REQUEST.value(), "No request provided in the request body");
	}

	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Error methodArgumentNotValidException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
		return processFieldErrors(fieldErrors);
	}

	private Error processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
		Error error = new Error(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Invalid request provided.");
		for (org.springframework.validation.FieldError fieldError : fieldErrors) {
			error.addFieldError(fieldError.getDefaultMessage());
		}
		return error;
	}

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	static class Error {
		private final int status;
		private final String message;
		private List<String> fieldErrors = new ArrayList<>();

		Error(int status, String message) {
			this.status = status;
			this.message = message;
		}

		public int getStatus() {
			return status;
		}

		public String getMessage() {
			return message;
		}

		public void addFieldError(String message) {
			fieldErrors.add(message);
		}

		public List<String> getFieldErrors() {
			return fieldErrors;
		}
	}
}