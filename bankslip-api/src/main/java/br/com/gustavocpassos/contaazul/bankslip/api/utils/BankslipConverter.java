package br.com.gustavocpassos.contaazul.bankslip.api.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.gustavocpassos.contaazul.bankslip.api.dto.BankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.dto.NewBankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.entity.Bankslip;

@Component
public class BankslipConverter {

	public BankslipDTO toBankslipDTO(Bankslip entity) {
		BankslipDTO dto = new BankslipDTO();
		dto.setId(entity.getId().toString());
		dto.setStatus(entity.getStatus().toString());
		dto.setDueDate(entity.getDueDate());
		dto.setTotalInCents(entity.getTotalInCents());
		dto.setCustomer(entity.getCustomer());
		dto.setPaymentDate(entity.getPaymentDate());
		dto.setFine(entity.getFine());
		return dto;
	}

	public List<BankslipDTO> toListBankslipDTO(final List<Bankslip> bankslips) {
		return bankslips.stream().map(this::toBankslipDTO).collect(Collectors.toList());
	}
	
    public Bankslip buildNewBankslip(NewBankslipDTO dto) {
        Bankslip bankslip = new Bankslip();
        bankslip.setDueDate(dto.getDueDate());
        bankslip.setTotalInCents(dto.getTotalInCents());
        bankslip.setCustomer(dto.getCustomer());
        return bankslip;
    }

}
