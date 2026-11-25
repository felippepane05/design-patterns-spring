# Design Patterns com Spring

Projeto de estudo baseado no laboratorio **Explorando Padroes de Projetos na Pratica com Java**, da DIO. Reproduz a API de clientes e enderecos apresentada em aula e acrescenta tratamento para clientes inexistentes e correcao da atualizacao pelo ID da URL.

## Creditos

Projeto original: [digitalinnovationone/lab-padroes-projeto-spring](https://github.com/digitalinnovationone/lab-padroes-projeto-spring).

A estrutura, as funcionalidades e os exemplos de padroes sao da base da aula. Os comentarios de autoria originais foram preservados.

## Funcionalidades

- Cadastrar, listar, consultar, atualizar e excluir clientes.
- Consultar endereco pelo CEP usando ViaCEP.
- Reutilizar enderecos armazenados no banco H2.

## Melhoria realizada

Na busca por ID, atualizacao e exclusao, um cliente inexistente retorna **HTTP 404** com a mensagem **Cliente não encontrado**.

A atualizacao agora usa o ID da URL, mesmo quando o corpo nao informa um ID ou informa outro. Isso corrige uma falha da base que criava outro cadastro em vez de atualizar o cliente existente.

Arquivos alterados em relacao ao projeto original:

- `ClienteServiceImpl.java`: usa `orElseThrow()` com `ResponseStatusException`, verifica existencia antes de atualizar/excluir e fixa o ID da URL na atualizacao.
- `src/main/resources/application.properties`: inclui a mensagem de erro na resposta HTTP.
- `ClienteServiceImplTest.java`: testes de regressao para o 404 e a atualizacao sem duplicacao.

## Padroes explorados na aula

- **Singleton:** beans gerenciados pelo Spring com escopo singleton por padrao.
- **Strategy:** separacao entre o contrato ClienteService e sua implementacao. A base possui uma implementacao.
- **Facade:** operacoes da API escondem os detalhes do banco de dados e da integracao ViaCEP.
- **Repository:** interfaces do Spring Data abstraem o acesso aos dados; nao e um padrao GoF.

## Tecnologias

Java (alvo de compilacao 11), Spring Boot 2.5.4, Spring Data JPA, Spring Web, OpenFeign, H2, OpenAPI/Swagger e Maven. As versoes do laboratorio foram preservadas para acompanhar a aula. Projeto destinado a estudo local.

## Executar

Com JDK e Maven configurados, abra o terminal na pasta do pom.xml:

```shell
mvn test
mvn spring-boot:run
```

No Windows, tambem e possivel usar o wrapper incluido:

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

A primeira execucao baixa dependencias e precisa de internet. O cadastro consulta o ViaCEP quando o endereco ainda nao esta armazenado. O banco H2 e temporario: os dados sao perdidos ao encerrar a aplicacao.

Swagger: [http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config).

## Endpoints

| Metodo | Rota | Operacao |
| --- | --- | --- |
| GET | /clientes | Listar |
| GET | /clientes/{id} | Consultar |
| POST | /clientes | Cadastrar |
| PUT | /clientes/{id} | Atualizar |
| DELETE | /clientes/{id} | Excluir |

Exemplo de corpo para cadastro:

```json
{
  "nome": "Felipe",
  "endereco": {
    "cep": "01001-000"
  }
}
```

Para conferir a melhoria, consulte `GET /clientes/999999` sem cadastrar esse ID: a resposta sera HTTP 404 com `message` igual a `Cliente não encontrado`.

## Verificacao

Em 24/11/2026, o fluxo HTTP foi testado em uma instancia temporaria com H2 vazio e ViaCEP real: cadastro com CEP 01001-000 (Praca da Se, Sao Paulo), consulta, listagem, atualizacao para CEP 20040-020 (Rio de Janeiro), atualizacao sem ID no corpo e com ID divergente, reutilizacao de endereco e exclusao. A atualizacao manteve o ID e nao duplicou clientes. Busca, atualizacao e exclusao de IDs inexistentes retornaram 404. OpenAPI e Swagger responderam corretamente. Os dados eram ficticios e a instancia foi encerrada depois dos testes.

Build e testes automatizados executados com JDK 21.0.12.1. Os testes locais nao garantem disponibilidade permanente do ViaCEP nem cobrem todas as entradas invalidas possiveis; as demais regras do exemplo original foram preservadas.