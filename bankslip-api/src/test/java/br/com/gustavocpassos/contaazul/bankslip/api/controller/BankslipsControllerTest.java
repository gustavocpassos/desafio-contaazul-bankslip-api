package br.com.gustavocpassos.contaazul.bankslip.api.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import br.com.gustavocpassos.contaazul.bankslip.api.dto.BankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.dto.NewBankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.dto.PayBankslipDTO;
import br.com.gustavocpassos.contaazul.bankslip.api.entity.Bankslip.BankslipStatus;
import br.com.gustavocpassos.contaazul.bankslip.api.exception.BankslipNotFoundException;
import br.com.gustavocpassos.contaazul.bankslip.api.service.BankslipService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) 
public class BankslipsControllerTest {
	
    @Autowired
    private TestRestTemplate restTemplate;
    
    @MockBean
    private BankslipService bankslipService;
    
    @Test
    public void shouldBeOkStatusWhenGetAllBankslipsWithouAny() throws Exception {
    	ResponseEntity<String> responseEntity = this.restTemplate.getForEntity("/rest/bankslips", String.class);
    	Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    	Assert.assertEquals("[]", responseEntity.getBody());
    }
    
    @Test
    public void shouldBeOkStatusWhenGetAllBankslipsWithOne() throws Exception {
    	
    	BankslipDTO bankslip = new BankslipDTO();
    	bankslip.setId(UUID.randomUUID().toString());
    	bankslip.setStatus(BankslipStatus.PENDING.toString());
    	bankslip.setCustomer("Test Customer");
    	bankslip.setTotalInCents(new BigDecimal(237891));
    	bankslip.setDueDate(new Date());
    	
    	Mockito.when(bankslipService.findAll()).thenReturn(Collections.singletonList(bankslip));
    	
    	ParameterizedTypeReference<List<BankslipDTO>> responseType = new ParameterizedTypeReference<List<BankslipDTO>>() {};
    	ResponseEntity<List<BankslipDTO>> responseEntity = restTemplate.exchange("/rest/bankslips", HttpMethod.GET, null, responseType);
    	
    	Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    	Assert.assertFalse(responseEntity.getBody().isEmpty());
    	Assert.assertEquals(1, responseEntity.getBody().size());
    }
    
    @Test
    public void shouldBeOkStatusWhenGetAllBankslipsWithMoreThanOne() throws Exception {
    	
    	BankslipDTO bankslip = new BankslipDTO();
    	bankslip.setId(UUID.randomUUID().toString());
    	bankslip.setStatus(BankslipStatus.PENDING.toString());
    	bankslip.setCustomer("Test Customer");
    	bankslip.setTotalInCents(new BigDecimal(237891));
    	bankslip.setDueDate(new Date());
    	
    	BankslipDTO bankslip2 = new BankslipDTO();
    	bankslip2.setId(UUID.randomUUID().toString());
    	bankslip2.setStatus(BankslipStatus.PAID.toString());
    	bankslip2.setCustomer("Test Customer 2");
    	bankslip2.setTotalInCents(new BigDecimal(11232));
    	bankslip2.setDueDate(new Date());
    	
    	Mockito.when(bankslipService.findAll()).thenReturn(Arrays.asList(bankslip, bankslip2));
    	
    	ParameterizedTypeReference<List<BankslipDTO>> responseType = new ParameterizedTypeReference<List<BankslipDTO>>() {};
    	ResponseEntity<List<BankslipDTO>> responseEntity = restTemplate.exchange("/rest/bankslips", HttpMethod.GET, null, responseType);
    	
    	Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    	Assert.assertFalse(responseEntity.getBody().isEmpty());
    	Assert.assertEquals(2, responseEntity.getBody().size());
    }
    
    
    @Test
    public void shouldBeOkStatusWhenGetBankslipByIdWithOneFound() throws Exception {
    	
    	UUID randomUUID = UUID.randomUUID();
    	
    	BankslipDTO bankslip = new BankslipDTO();
    	bankslip.setId(randomUUID.toString());
    	bankslip.setStatus(BankslipStatus.PENDING.toString());
    	bankslip.setCustomer("Test Customer");
    	bankslip.setTotalInCents(new BigDecimal(237891));
    	bankslip.setDueDate(new Date());
    	
    	Mockito.when(bankslipService.findById(randomUUID)).thenReturn(bankslip);
    	
    	ResponseEntity<BankslipDTO> responseEntity = restTemplate.getForEntity("/rest/bankslips/{id}", BankslipDTO.class, randomUUID.toString());
    	
    	Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    	Assert.assertEquals(bankslip.getId().toString(), responseEntity.getBody().getId());
    }
    
    
    @Test
    public void shouldBeNotFoundStatusWhenGetByIdWithIdNotFound() throws Exception {
    	
    	UUID randomUUID = UUID.randomUUID();
    	
    	Mockito.when(bankslipService.findById(randomUUID)).thenThrow(new BankslipNotFoundException("Not Found"));
    	
    	ResponseEntity<BankslipDTO> responseEntity = restTemplate.getForEntity("/rest/bankslips/{id}", BankslipDTO.class, randomUUID.toString());
    	
    	Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    
    
    @Test
    public void shouldBeCreatedStatusWhenSaveNewCorrectBankslip() throws Exception {
    	
    	NewBankslipDTO request = new NewBankslipDTO();
    	request.setCustomer("Customer");
    	request.setDueDate(new Date());
    	request.setTotalInCents(new BigDecimal("12321"));
    	
    	UUID randomUUID = UUID.randomUUID();
    	
    	BankslipDTO bankslip = new BankslipDTO();
    	bankslip.setId(randomUUID.toString());
    	bankslip.setStatus(BankslipStatus.PENDING.toString());
    	bankslip.setCustomer("Customer");
    	bankslip.setTotalInCents(new BigDecimal(12321));
    	bankslip.setDueDate(new Date());
    	
    	Mockito.when(bankslipService.create(Mockito.any())).thenReturn(bankslip);
    	
    	ResponseEntity<BankslipDTO> responseEntity = restTemplate.postForEntity("/rest/bankslips", request, BankslipDTO.class);
    	
    	Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldBeBadRequestStatusWhenSaveIncorrectBankslipRequest() throws Exception {
    	
    	MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
    	requestHeaders.add("content-type", "application/json");
    	requestHeaders.add("Accept", "application/json");
    	
    	HttpEntity<String> requestEntity = new HttpEntity<String>("", requestHeaders);
    	
    	ResponseEntity<BankslipDTO> responseEntity = restTemplate.postForEntity("/rest/bankslips", requestEntity, BankslipDTO.class);
    	
    	Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldBeUnprocessableEntityStatusWhenSaveBankslipRequestWithoutCustomer() throws Exception {
    	
    	NewBankslipDTO request = new NewBankslipDTO();
    	request.setDueDate(new Date());
    	request.setTotalInCents(new BigDecimal("12321"));
    	
    	ResponseEntity<BankslipDTO> responseEntity = restTemplate.postForEntity("/rest/bankslips", request, BankslipDTO.class);
    	
    	Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldBeUnprocessableEntityStatusWhenSaveBankslipRequestWithoutDueDate() throws Exception {
    	
    	NewBankslipDTO request = new NewBankslipDTO();
    	request.setCustomer("Customer");
    	request.setTotalInCents(new BigDecimal("12321"));
    	
    	ResponseEntity<BankslipDTO> responseEntity = restTemplate.postForEntity("/rest/bankslips", request, BankslipDTO.class);
    	
    	Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldBeUnprocessableEntityStatusWhenSaveBankslipRequestTotalInCents() throws Exception {
    	
    	NewBankslipDTO request = new NewBankslipDTO();
    	request.setCustomer("Customer");
    	request.setDueDate(new Date());
    	
    	ResponseEntity<BankslipDTO> responseEntity = restTemplate.postForEntity("/rest/bankslips", request, BankslipDTO.class);
    	
    	Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldBeUnprocessableEntityStatusWhenSaveBankslipRequestWithBlankCustomer() throws Exception {
    	
    	NewBankslipDTO request = new NewBankslipDTO();
    	request.setCustomer("");
    	request.setDueDate(new Date());
    	request.setTotalInCents(new BigDecimal("12321"));
    	
    	ResponseEntity<BankslipDTO> responseEntity = restTemplate.postForEntity("/rest/bankslips", request, BankslipDTO.class);
    	
    	Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldBeNoContentStatusWhenPayBankslip() throws Exception {
    	
    	UUID randomUUID = UUID.randomUUID();

    	PayBankslipDTO request=  new PayBankslipDTO();
    	request.setPaymentDate(new Date());
    	
    	Mockito.doNothing().when(bankslipService).pay(randomUUID, request);
    	
    	ResponseEntity<BankslipDTO> responseEntity = restTemplate.postForEntity("/rest/bankslips/{id}/payments", request, BankslipDTO.class, randomUUID.toString());
    	
    	Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldBeUnproessableEntityStatusWhenPayBankslipWithoutPaymentDate() throws Exception {
    	
    	UUID randomUUID = UUID.randomUUID();
    	
    	PayBankslipDTO request = new PayBankslipDTO();
    	
    	ResponseEntity<BankslipDTO> responseEntity = restTemplate.postForEntity("/rest/bankslips/{id}/payments", request, BankslipDTO.class, randomUUID.toString());
    	
    	Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldBeBadRequestStatusWhenPayBankslipWithIncorrectRequest() throws Exception {
    	
    	UUID randomUUID = UUID.randomUUID();
    	
    	MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
    	requestHeaders.add("content-type", "application/json");
    	requestHeaders.add("Accept", "application/json");
    	
    	HttpEntity<String> requestEntity = new HttpEntity<String>("", requestHeaders);
    	
    	ResponseEntity<BankslipDTO> responseEntity = restTemplate.postForEntity("/rest/bankslips/{id}/payments", requestEntity, BankslipDTO.class, randomUUID.toString());
    	
    	Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    
    @Test
    public void shouldBeNoContentStatusWhenCancelBankslip() throws Exception {
    	
    	UUID randomUUID = UUID.randomUUID();
    	
    	Mockito.doNothing().when(bankslipService).cancel(randomUUID);
    	
    	ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/bankslips/{id}", HttpMethod.DELETE, null, String.class, randomUUID.toString());
    	
    	Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
    
    @Test
    public void shouldBeNotFoundStatusWhenPayBankslipThatNotExists() throws Exception {
    	
    	UUID randomUUID = UUID.randomUUID();
    	
    	Mockito.doThrow(new BankslipNotFoundException("Not found")).when(bankslipService).cancel(randomUUID);
    	
    	ResponseEntity<String> responseEntity = restTemplate.exchange("/rest/bankslips/{id}", HttpMethod.DELETE, null, String.class, randomUUID.toString());
    	
    	Assert.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
