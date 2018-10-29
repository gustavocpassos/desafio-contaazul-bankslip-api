package br.com.gustavocpassos.contaazul.bankslip.api.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gustavocpassos.contaazul.bankslip.api.dto.BankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.dto.NewBankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.dto.PayBankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.service.BankslipService;

@RestController
@RequestMapping("/rest/bankslips")
public class BankslipsController {
	
	@Autowired
	private BankslipService service;

	@GetMapping("/{id}")
	public ResponseEntity<BankslipDTO> findById(@PathVariable String id) {
		return ResponseEntity.ok( service.findById( UUID.fromString(id) ) );
	}

	@GetMapping
	public ResponseEntity<List<BankslipDTO>> findAll() {
		return ResponseEntity.ok( service.findAll() );
	}

	@PostMapping
	public ResponseEntity<BankslipDTO> create(@Valid @RequestBody NewBankslipDTO newBankslip) {
		return ResponseEntity.status(HttpStatus.CREATED).body( service.create(newBankslip) );
	}
	
	@PostMapping("{id}/payments")
	public ResponseEntity<Void> pay(@PathVariable String id, @Valid @RequestBody PayBankslipDTO payBankslip) {
		service.pay(UUID.fromString(id), payBankslip);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> cancel(@PathVariable String id) {
		service.cancel(UUID.fromString(id));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
