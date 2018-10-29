package br.com.gustavocpassos.contaazul.bankslip.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gustavocpassos.contaazul.bankslip.api.entity.Bankslip;

public interface BankslipRepository extends JpaRepository<Bankslip, UUID>{

}