package br.com.gustavocpassos.contaazul.bankslip.api.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gustavocpassos.contaazul.bankslip.api.dto.BankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.dto.NewBankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.dto.PayBankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.entity.Bankslip;
import br.com.gustavocpassos.contaazul.bankslip.api.entity.Bankslip.BankslipStatus;
import br.com.gustavocpassos.contaazul.bankslip.api.exception.BankslipNotFoundException;
import br.com.gustavocpassos.contaazul.bankslip.api.exception.BankslipNotPendingException;
import br.com.gustavocpassos.contaazul.bankslip.api.repository.BankslipRepository;
import br.com.gustavocpassos.contaazul.bankslip.api.utils.BankslipConverter;

@Service
public class BankslipService {

	@Autowired
	private BankslipRepository repository;

	@Autowired
	private BankslipConverter converter;

	@Autowired
	private FineService fineService;

	public BankslipDTO findById(UUID id) {
		Bankslip entity = findEntity(id);
		if (BankslipStatus.PENDING.equals(entity.getStatus())) {
			entity.setFine(fineService.calculateFine(entity.getDueDate(), new Date(), entity.getTotalInCents()));
		}
		return converter.toBankslipDTO(entity);
	}

	public List<BankslipDTO> findAll() {
		return converter.toListBankslipDTO(repository.findAll());
	}

	public BankslipDTO create(NewBankslipDTO newBankslip) {
		Bankslip entity = converter.buildNewBankslip(newBankslip);
		entity.setId(UUID.randomUUID());
        entity.setStatus(BankslipStatus.PENDING);
		return converter.toBankslipDTO(repository.save(entity));
	}

	public void pay(UUID id, PayBankslipDTO payBankslip) {
		Bankslip entity = findEntity(id);
		validatePendingStatus(entity);
		entity.setFine(fineService.calculateFine(entity.getDueDate(), payBankslip.getPaymentDate(), entity.getTotalInCents()));
		entity.setStatus(BankslipStatus.PAID);
		entity.setPaymentDate(payBankslip.getPaymentDate());
		repository.save(entity);
	}

	public void cancel(UUID id) {
		Bankslip entity = findEntity(id);
		validatePendingStatus(entity);
		entity.setStatus(BankslipStatus.CANCELED);
		repository.save(entity);
	}
	
	private void validatePendingStatus(Bankslip entity) {
		if (!BankslipStatus.PENDING.equals(entity.getStatus())) {
			throw new BankslipNotPendingException("The informed bankslip is not pending payment");
		}
	}

	private Bankslip findEntity(UUID id) {
		return repository.findById(id).orElseThrow(() -> new BankslipNotFoundException("Bankslip not found with the specified id."));
	}

}
