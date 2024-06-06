# E-commerce Spring Boot 3 + AWS

## Descrição

Este projeto é uma API de um e-commerce, na qual possui diversas funcionalidades como: autenticar usuário, autorizar rotas, cadastrar e remover produtos, fazer o upload de arquivos na nuvem utilizando o S3 da AWS, etc.

## Tecnologias

- Java 17
- Maven
- Docker
- Spring Boot
- Spring Data JPA
- Spring Security
- AWS SDK
- JWT
- MySQL
- SpringDoc OpenAPI
- Logging

## Pré-requisitos

- JRE 17
- Maven

## Sumário
- [Descrição](#descrição)
- [Tecnologias Utilizadas](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
  - [Iniciar o projeto via Maven](#iniciar-o-projeto-via-maven)
  - [Iniciar o projeto via Docker Compose](#iniciar-o-projeto-via-docker-compose)
- [Requisições](#requisições)
  - [Requisição de Login](#requisição-de-login)
  - [Requisição de Cadastro de um Novo Usuário](#requisição-de-cadastro-de-um-novo-usuário)
  - [Requisição de Dados do Usuário Autenticado](#requisição-de-dados-do-usuário-autenticado)
  - [Requisição de Criação de um Endereço do Usuário](#requisição-de-criação-de-um-endereço-do-usuário)
  - [Requisição de Criação de um Produto](#requisição-de-criação-de-um-produto)
  - [Requisição de Criação de uma Order](#requisição-de-criação-de-uma-order)
  - [Requisição do Histórico de Orders de um Usuário](#requisição-do-histórico-de-orders-de-um-usuário)
- [Autor e Contato](#autor)

### Iniciar o projeto via Maven:

1. Clone o repositório:

```shell
git clone github.com/nicholasboari/ecommerce-boari-api
```

2. Acesse a pasta da api:

```shell
cd ecommerce-boari-api/api/
```

3. Execute o projeto:

```shell
mvn spring-boot:run
```

#### Por padrão o projeto é executado em modo "test", no qual é usado um banco em memória (H2 database).

### Iniciar o projeto via *Docker Compose*:

1. Acesse a pasta raiz do projeto (ecommerce-boari-api):

```shell
cd ecommerce-boari-api/
```

2. Execute o docker compose:

```shell
docker compose up
```

# Requisições 

## Requisição de Login


#### Alguns endpoints são necessários o usuário estar autenticado.
#### Um usuário é inserido no banco assim que a aplicação é iniciada.

#### Dados do usuário:
- id: 1
- email: nicholasboari@gmail.com
- firstName: Nicholas
- lastName: Boari
- username: nicholasboari
- password: 123
- role: ADMIN
----

- Acesse a URL: *localhost:8090/api/v1/auth/login*
- Realize a requisição através do método GET no formato JSON

```json
{
    "email": "nicholasboari@gmail.com",
    "password": "123"
}
```

- Exemplo de retorno:

```
{
    "token": eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaXNBY3RpdmUiOnRydWUsInN1YiI6Im5pY2hvbGFzYm9hcmkiLCJpYXQiOjE3MDA2ODEwNTEsImV4cCI6MTcwMTU0NTA1MX0.8edCSh1DGRr3DN7cE5ciZBtV0IG8spjqHgPKMOUdc48
}
```
## Requisição de cadastro de um novo usuário

- Acesse a URL: *localhost:8090/api/v1/users*
- A requisição dos dados é via *POST* no formato *JSON*


```json
{
    "email": "joaozinho@gmail.com",
    "password": "123",
    "username": "joao",
    "phone": "12992220086",
    "firstName": "Joao",
    "lastName": "Silva"
}

```


## Requisição de dados do usuário autenticado

- Acesse a URL: *localhost:8090/api/v1/users*
- Para obter os dados do usuário autenticado é necessário passar o token obtido no login e inseri-lo no Bearer Token da requsição
- A requisição dos dados é via *GET*
- Exemplo de retorno

![Alt text](image.png)

## Requisição de criação de um endereço do usuário

- Acesse a URL: *localhost:8090/api/v1/addresses*
- Para criar endereço do usuário autenticado é necessário passar o token obtido no login e inseri-lo no Bearer Token da requsição
- A criação do endereço é via *POST* e no formato *JSON*

```json
{
    "cep": "12040-000",
    "country": "Brasil",
    "state": "SP",
    "street": "Rua joaozinho",
    "neighborhood": "Alphaville"
}
```

## Requisição de criação de um produto

- Acesse a URL: *localhost:8090/api/v1/products*
- Para criar um produto o usuário autenticado é necessário uma role (permissão) de "ADMIN" ou "MANAGER"
- A criação do endereço é via *POST* e no formato *JSON*

```json
{
    "name": "Celular Xiaomi",
    "price": 1299.00,
    "brand": {
        "id": 1
    },
    "category": {
        "id": 1
    }
}
```
- Exemplo de retorno

![Alt text](image-1.png)

## Requisição de criação de uma order

- Acesse a URL: *localhost:8090/api/v1/orders*
- Para criar uma order o usuário autenticado é ncessário ter um endereço associado e do token obtido no login
- A criação do endereço é via *POST* e no formato *JSON*

```json
{
    "products": [
        {
            "id": 1
        }
    ]
}
```
- Exemplo de retorno

![Alt text](image-2.png)

## Requisição do histórico de orders de um usuário

- Acesse a URL: *localhost:8090/api/v1/orders*
- Para obter o histórico de orders do usuário autenticado é necessário ter um endereço associado e do token obtido no login
- A criação do endereço é via *GET*

- Exemplo de retorno

![Alt text](image-3.png)

# Autor

**Nome do Autor:** Nicholas Boari

**Contato:**
- E-mail: nicholasboari@gmail.com
- LinkedIn: https://www.linkedin.com/in/nicholasboari/
- GitHub: https://github.com/nicholasboari
