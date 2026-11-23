# Sistema de Condomínio

Aplicação Java de console com **CRUD completo** de duas entidades relacionadas (**Apartamento** 1:N **Morador**),
integração com **MySQL via JDBC** e consumo da API pública **ViaCEP** para preencher o endereço do morador.

## Tecnologias
- Java 17
- Maven
- MySQL (JDBC - `mysql-connector-j`)
- `dotenv-java` (leitura do `.env`)
- `gson` (leitura do JSON do ViaCEP)
- API externa: [ViaCEP](https://viacep.com.br)

## Pré-requisitos
1. **JDK 17** ou superior (`java -version`)
2. **Maven 3.8+** (`mvn -version`)
3. **MySQL** rodando em `localhost:3306` com o usuário `root` **sem senha**
   (padrão do XAMPP/MariaDB). 
4. Conexão com a internet (para o ViaCEP)

> O banco `condominio` e as tabelas são **criados automaticamente** na primeira execução.
> O arquivo `schema.sql` é apenas um backup do script.

## Configuração
Copie o arquivo de exemplo (já vem com valores reais):

```bash
# Linux / macOS
cp .env.example .env
# Windows (cmd)
copy .env.example .env
```

Conteúdo do `.env`:

```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=condominio
DB_USER=root
DB_PASSWORD=
```

Se o `.env` não existir, o programa usa esses mesmos valores como padrão.

## Como executar

```bash
mvn clean compile exec:java
```

Ou abra o projeto em uma IDE (IntelliJ/Eclipse/VS Code) e execute a classe `br.edu.poo.m5678.Main`.

## Estrutura

```
src/main/java/br/edu/poo/m5678
├── Main.java                  # ponto de entrada
├── config/ConexaoDB.java      # conexão e criação das tabelas
├── model/                     # Apartamento, Morador
├── dao/                       # ApartamentoDAO, MoradorDAO (JDBC + PreparedStatement)
├── service/ViaCepService.java # consumo da API ViaCEP
├── util/Validador.java        # CPF, e-mail, CEP, telefone
└── view/Menu.java             # menu interativo (Scanner)
```

## Tabelas

**apartamento**: `id`, `numero`, `bloco`, `andar` (único por bloco + número)

**morador**: `id`, `nome`, `cpf` (único), `telefone`, `email`, `cep`, `logradouro`, `bairro`, `cidade`, `uf`,
`apartamento_id` (FK → apartamento.id)

## Como usar
O menu principal tem duas opções: **Apartamentos** e **Moradores**. Cada uma permite cadastrar, listar,
buscar por ID, atualizar e deletar.

- Ao cadastrar um morador, informe o CEP: o sistema consulta o **ViaCEP** e preenche logradouro, bairro, cidade e UF.
- Não é possível cadastrar morador em apartamento inexistente.
- Não é possível deletar um apartamento que ainda tenha moradores.
- Na atualização, deixe o campo em branco para manter o valor atual.

## Validações
Campos obrigatórios, CPF com dígitos verificadores, e-mail, CEP (8 dígitos), telefone (DDD + número),
números inteiros, CPF/apartamento duplicados e tratamento de erros de banco e de internet.
