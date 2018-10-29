# Desafio Conta Azul - bankslip-api

REST API para gerenciamento de boletos em resposta ao Desafio da Conta Azul.

## Tecnologias e frameworks utilizados
- Java 8 (jdk1.8.0_92)
- Springboot 2.0.6
- Maven 3.5
- Docker 18
- Swagger 2.8.0 

#### Ambiente de desenvolvimento
- Ubuntu 18.04
- Eclipse Photon
- Postman para testes de mesa

#### Suposições e decisões tomadas
- Houve três regras que decidi implementar a mais do que dizia o desafio
	1. Um boleto só pode ser pago se estiver com o status pendente.
	2. Um boleto só pode ser cancelado se estiver com o status pendente.
	3. Durante um pagamento, é calculada a multa e salva no banco para que na consulta ela possa ser devolvida e saber o quanto de multa foi paga com o cálculo em vigor no momento do pagamento.
- Fiz os testes sobre as camadas de Controller e de Service. 100% de cobertura.

## Subir o projeto

1. Acesse a pasta root do projeto onde está presente o pom.xml.
2. Faça a instalação do projeto utilizando o maven e já gerando a imagem docker.

```
mvn clean install docker:build
```

Com isto finalizado, há 3 maneiras de fazer o projeto subir:

##### 1 - Rodando através de imagem docker
```
docker run -p8080:8080  br.com.gustavocpassos.contaazul/bankslip-api:1.0.0
```

##### 2 - Utilizando o Maven
```
mvn spring-boot:run
```

##### 3 - Executando o JAR
```
java -jar target/bankslip-api-1.0.0.jar
```
