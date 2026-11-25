# Design Patterns com Spring

Projeto do desafio de Padrões de Projeto da DIO. É uma API para cadastrar clientes e buscar seus endereços pelo CEP, usando o ViaCEP.

A base é o [projeto apresentado na aula](https://github.com/digitalinnovationone/lab-padroes-projeto-spring), com exemplos de Singleton, Strategy e Facade.

## O que foi alterado

- Retorno 404 com a mensagem "Cliente não encontrado" ao consultar, atualizar ou excluir um ID que não existe.
- Correção da atualização para usar o ID da URL e não criar outro cliente.
- Testes para esses comportamentos.

## Tecnologias

Java, Spring Boot, Spring Data JPA, OpenFeign, H2 e Swagger.

## Como executar

Com Java e Maven instalados, na pasta do projeto:

```bash
mvn spring-boot:run
```

Ou pelo Maven Wrapper, no Bash:

```bash
./mvnw spring-boot:run
```

No PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Com a aplicação rodando, abra o [Swagger](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config) para testar as operações de clientes. Esse endereço é local e só funciona enquanto a aplicação estiver ligada no seu computador.

Exemplo de cadastro em `POST /clientes`:

```json
{
  "nome": "Felipe",
  "endereco": {
    "cep": "01001-000"
  }
}
```

O cadastro precisa de internet para consultar o ViaCEP. Os dados ficam no H2 em memória e são apagados ao encerrar a aplicação.

Para rodar os testes:

```bash
./mvnw test
```