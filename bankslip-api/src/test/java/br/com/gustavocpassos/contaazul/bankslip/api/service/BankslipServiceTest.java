package br.com.gustavocpassos.contaazul.bankslip.api.service;

import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.gustavocpassos.contaazul.bankslip.api.dto.BankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.dto.NewBankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.dto.PayBankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.entity.Bankslip;
import br.com.gustavocpassos.contaazul.bankslip.api.entity.Bankslip.BankslipStatus;
import br.com.gustavocpassos.contaazul.bankslip.api.exception.BankslipNotFoundException;
import br.com.gustavocpassos.contaazul.bankslip.api.exception.BankslipNotPendingException;
import br.com.gustavocpassos.contaazul.bankslip.api.repository.BankslipRepository;
import br.com.gustavocpassos.contaazul.bankslip.api.utils.BankslipConverter;

@RunWith(MockitoJUnitRunner.class)
public class BankslipServiceTest {
	
	@InjectMocks
	private BankslipService bankslipService;
	
	@Mock
	private BankslipRepository bankslipRepository;
	
	@Mock
	private FineService fineService;
	
	@Spy
	private BankslipConverter converter;
	
	@Test
	public void shouldCalculateFineWhenFindByIdWithPendingStatus() {
		
		UUID randomUUID = UUID.randomUUID();
		
		Bankslip bankslip = new Bankslip();
    	bankslip.setId(randomUUID);
    	bankslip.setStatus(BankslipStatus.PENDING);
    	bankslip.setCustomer("Test Customer");
    	bankslip.setTotalInCents(new BigDecimal(237891));
    	bankslip.setDueDate(new Date());
    	
    	Mockito.when(bankslipRepository.findById(randomUUID)).thenReturn(Optional.of(bankslip));
    	Mockito.when(fineService.calculateFine(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new BigDecimal(123));
    	
    	BankslipDTO findById = bankslipService.findById(randomUUID);
    	
    	Assert.assertNotNull(findById.getFine());
    	Mockito.verify( fineService, times(1) ).calculateFine(Mockito.any(), Mockito.any(), Mockito.any());
	}
	
	@Test
	public void shouldNotCalculateFineWhenFindByIdWithPaidStatus() {
		
		UUID randomUUID = UUID.randomUUID();
		
		BigDecimal mockedFine = new BigDecimal(444);
		
		Bankslip bankslip = new Bankslip();
    	bankslip.setId(randomUUID);
    	bankslip.setStatus(BankslipStatus.PAID);
    	bankslip.setCustomer("Test Customer");
    	bankslip.setTotalInCents(new BigDecimal(237891));
    	bankslip.setDueDate(new Date());
    	bankslip.setPaymentDate(new Date());
    	bankslip.setFine(mockedFine);
    	
    	Mockito.when(bankslipRepository.findById(randomUUID)).thenReturn(Optional.of(bankslip));
    	
    	BankslipDTO findById = bankslipService.findById(randomUUID);

    	Assert.assertNotNull(findById);
    	Assert.assertNotNull(findById.getFine());
    	Assert.assertEquals(mockedFine, findById.getFine());
    	Mockito.verify( fineService, times(0) ).calculateFine(Mockito.any(), Mockito.any(), Mockito.any());
	}
	
	@Test
	public void shouldNotCalculateFineWhenFindByIdWithCanceledStatus() {
		
		UUID randomUUID = UUID.randomUUID();
		
		Bankslip bankslip = new Bankslip();
    	bankslip.setId(randomUUID);
    	bankslip.setStatus(BankslipStatus.CANCELED);
    	bankslip.setCustomer("Test Customer");
    	bankslip.setTotalInCents(new BigDecimal(237891));
    	bankslip.setDueDate(new Date());
    	
    	Mockito.when(bankslipRepository.findById(randomUUID)).thenReturn(Optional.of(bankslip));
    	
    	BankslipDTO findById = bankslipService.findById(randomUUID);
    	
    	Assert.assertNotNull(findById);
    	Assert.assertNull(findById.getFine());
    	Mockito.verify( fineService, times(0) ).calculateFine(bankslip.getDueDate(), bankslip.getPaymentDate(), bankslip.getTotalInCents());
	}
	
	@Test(expected = BankslipNotFoundException.class)
	public void shouldThrowExceptionIfNotExistsBankslipWithId() {
		UUID randomUUID = UUID.randomUUID();
    	Mockito.when(bankslipRepository.findById(randomUUID)).thenReturn(Optional.ofNullable(null));
    	bankslipService.findById(randomUUID);
	}
	
	@Test 
	public void shouldFindAllReturnAllOfSaved(){
		Bankslip bankslip = new Bankslip();
    	bankslip.setId(UUID.randomUUID());
    	bankslip.setStatus(BankslipStatus.PENDING);
    	bankslip.setCustomer("Test Customer");
    	bankslip.setTotalInCents(new BigDecimal(237891));
    	bankslip.setDueDate(new Date());
    	
    	Bankslip bankslip2 = new Bankslip();
    	bankslip2.setId(UUID.randomUUID());
    	bankslip2.setStatus(BankslipStatus.PAID); 
    	bankslip2.setCustomer("Test Customer 2");
    	bankslip2.setTotalInCents(new BigDecimal(11232));
    	bankslip2.setDueDate(new Date());
    	
    	Mockito.when(bankslipRepository.findAll()).thenReturn(Arrays.asList(bankslip, bankslip2));
    	
    	List<BankslipDTO> findAll = bankslipService.findAll();
    	
    	Assert.assertNotNull(findAll);
    	Assert.assertEquals(2, findAll.size());
	}
	

	@Test
	public void shouldSaveNewBankslipWithPendingStatus() {
		
		Bankslip bankslip = new Bankslip();
    	bankslip.setId(UUID.randomUUID());
    	bankslip.setStatus(BankslipStatus.PENDING);
    	bankslip.setCustomer("Customer");
    	bankslip.setTotalInCents(new BigDecimal(1231232));
    	bankslip.setDueDate(new Date());
		
		Mockito.when(bankslipRepository.save(Mockito.any())).thenReturn(bankslip);
		
		NewBankslipDTO newBankslip = new NewBankslipDTO();
		newBankslip.setCustomer("Customer");
		newBankslip.setDueDate(new Date());
		newBankslip.setTotalInCents(new BigDecimal(1231232));
		
		BankslipDTO create = bankslipService.create(newBankslip);
		
		Assert.assertNotNull(create);

		ArgumentCaptor<Bankslip> banksliptCaptor = ArgumentCaptor.forClass(Bankslip.class);
		Mockito.verify(bankslipRepository, times(1)).save(banksliptCaptor.capture());
		
		Bankslip entitySaved = banksliptCaptor.getValue();
		Assert.assertNotNull(entitySaved);
		Assert.assertEquals(BankslipStatus.PENDING, entitySaved.getStatus());
	}
	
	@Test(expected = BankslipNotFoundException.class)
	public void shouldThrowExceptionIfPayANotExistsBankslip() {
		UUID randomUUID = UUID.randomUUID();
		
    	Mockito.when(bankslipRepository.findById(randomUUID)).thenReturn(Optional.ofNullable(null));
    	
    	PayBankslipDTO payBankslipDTO = new PayBankslipDTO();
    	payBankslipDTO.setPaymentDate(new Date());
    	
		bankslipService.pay(randomUUID, payBankslipDTO);
	}
	
	@Test(expected = BankslipNotPendingException.class)
	public void shouldThrowExceptionIfPayAAlreadyPayedBankslip() {
		UUID randomUUID = UUID.randomUUID();
		
		Bankslip bankslip = new Bankslip();
    	bankslip.setId(randomUUID);
    	bankslip.setStatus(BankslipStatus.PAID);
    	bankslip.setCustomer("Test Customer");
    	bankslip.setTotalInCents(new BigDecimal(237891));
    	bankslip.setDueDate(new Date());
    	bankslip.setPaymentDate(new Date());
		
    	Mockito.when(bankslipRepository.findById(randomUUID)).thenReturn(Optional.of(bankslip));
    	
    	PayBankslipDTO payBankslipDTO = new PayBankslipDTO();
    	payBankslipDTO.setPaymentDate(new Date());
    	
		bankslipService.pay(randomUUID, payBankslipDTO);
	}
	
	@Test(expected = BankslipNotPendingException.class)
	public void shouldThrowExceptionIfPayACanceledBankslip() {
		UUID randomUUID = UUID.randomUUID();
		
		Bankslip bankslip = new Bankslip();
    	bankslip.setId(randomUUID);
    	bankslip.setStatus(BankslipStatus.CANCELED);
    	bankslip.setCustomer("Test Customer");
    	bankslip.setTotalInCents(new BigDecimal(237891));
    	bankslip.setDueDate(new Date());
		
    	Mockito.when(bankslipRepository.findById(randomUUID)).thenReturn(Optional.of(bankslip));
    	
    	PayBankslipDTO payBankslipDTO = new PayBankslipDTO();
    	payBankslipDTO.setPaymentDate(new Date());
    	
		bankslipService.pay(randomUUID, payBankslipDTO);
	}
	
	@Test
	public void shouldSetStatusAndPaymentDateAndFineWhenPay() {
		UUID randomUUID = UUID.randomUUID();
		
		Bankslip bankslip = new Bankslip();
    	bankslip.setId(randomUUID);
    	bankslip.setStatus(BankslipStatus.PENDING);
    	bankslip.setCustomer("Test Customer");
    	bankslip.setTotalInCents(new BigDecimal(237891));
    	bankslip.setDueDate(new Date());
    	
    	Mockito.when(bankslipRepository.findById(randomUUID)).thenReturn(Optional.of(bankslip));
    	
    	BigDecimal mockedFine = new BigDecimal(123);
    	Mockito.when(fineService.calculateFine(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(mockedFine);
    	
    	PayBankslipDTO payBankslipDTO = new PayBankslipDTO();
    	payBankslipDTO.setPaymentDate(new Date());
    	
		bankslipService.pay(randomUUID, payBankslipDTO);
		
		Mockito.verify( fineService, times(1) ).calculateFine(bankslip.getDueDate(), payBankslipDTO.getPaymentDate(), bankslip.getTotalInCents());
		
		ArgumentCaptor<Bankslip> banksliptCaptor = ArgumentCaptor.forClass(Bankslip.class);
		Mockito.verify(bankslipRepository, times(1)).save(banksliptCaptor.capture());
		
		Bankslip savedEntity = banksliptCaptor.getValue();
		
		Assert.assertEquals(BankslipStatus.PAID, savedEntity.getStatus());
		Assert.assertEquals(payBankslipDTO.getPaymentDate(), savedEntity.getPaymentDate());
		Assert.assertEquals(mockedFine, savedEntity.getFine());
	}
	
	
	@Test(expected = BankslipNotFoundException.class)
	public void shouldThrowExceptionIfCancelANotExistsBankslip() {
		UUID randomUUID = UUID.randomUUID();
    	Mockito.when(bankslipRepository.findById(randomUUID)).thenReturn(Optional.ofNullable(null));
		bankslipService.cancel(randomUUID);
	}
	
	@Test
	public void shouldSetStatusWhenCancel() {
		UUID randomUUID = UUID.randomUUID();
		
		Bankslip bankslip = new Bankslip();
    	bankslip.setId(randomUUID);
    	bankslip.setStatus(BankslipStatus.PENDING);
    	bankslip.setCustomer("Test Customer");
    	bankslip.setTotalInCents(new BigDecimal(237891));
    	bankslip.setDueDate(new Date());
    	
    	Mockito.when(bankslipRepository.findById(randomUUID)).thenReturn(Optional.of(bankslip));
    	
		bankslipService.cancel(randomUUID);
		
		ArgumentCaptor<Bankslip> banksliptCaptor = ArgumentCaptor.forClass(Bankslip.class);
		Mockito.verify(bankslipRepository, times(1)).save(banksliptCaptor.capture());
		
		Bankslip savedEntity = banksliptCaptor.getValue();
		
		Assert.assertEquals(BankslipStatus.CANCELED, savedEntity.getStatus());
	}

	
}
