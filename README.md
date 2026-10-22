# 🐾 ClyvoPet

Sistema web para gerenciamento de clínica veterinária desenvolvido em **Java com Spring Boot**, com foco no controle de animais, veterinários, consultas e lembretes.

O projeto possui autenticação e autorização por perfil de usuário, persistência em banco de dados Oracle, versionamento do banco com Flyway, validação de dados, envio de notificações por e-mail e deploy em nuvem utilizando Microsoft Azure.

---

## Aplicação publicada

O ClyvoPet está publicado no **Microsoft Azure App Service** e pode ser acessado diretamente pelo navegador:

### 🔗 [Acessar ClyvoPet](https://clyvo-pet-rm561497.azurewebsites.net)

> 🔐 As funcionalidades internas do sistema são protegidas por autenticação e exigem login.

---

## Sobre o projeto

O **ClyvoPet** foi desenvolvido para auxiliar no gerenciamento das principais atividades de uma clínica veterinária.

A aplicação centraliza informações relacionadas aos animais atendidos, profissionais veterinários, consultas realizadas e lembretes associados aos atendimentos.

Além das operações de cadastro, consulta, edição e exclusão, o sistema implementa regras de negócio, controle de acesso baseado em perfis, validação dos dados recebidos e envio de notificações por e-mail.

A aplicação possui interface web responsiva desenvolvida com **Thymeleaf e Bootstrap**, integrada ao backend Spring Boot e ao banco de dados Oracle.

---

## Principais funcionalidades

### Gerenciamento de animais

O sistema permite realizar o gerenciamento dos animais cadastrados na clínica, incluindo:

- Cadastro de novos animais;
- Visualização dos animais cadastrados;
- Atualização das informações;
- Exclusão de registros;
- Armazenamento dos dados do tutor;
- Visualização de detalhes como espécie, raça, idade, peso, sexo e observações.

---

### Gerenciamento de veterinários

Administradores possuem acesso ao gerenciamento dos profissionais veterinários.

Entre as operações disponíveis estão:

- Cadastro de veterinários;
- Consulta dos profissionais cadastrados;
- Atualização de informações;
- Exclusão de veterinários;
- Controle de CRMV;
- Especialidade;
- Telefone e e-mail;
- Controle de perfil de acesso.

A administração dos veterinários é uma funcionalidade exclusiva para usuários com perfil **ADMIN**.

---

### Gerenciamento de consultas

O ClyvoPet permite organizar os atendimentos realizados pela clínica.

As consultas relacionam:

- Animal;
- Veterinário responsável;
- Data e horário;
- Motivo da consulta;
- Observações;
- Status do atendimento.

O sistema também possui regras para preservar a consistência das consultas caso um veterinário seja excluído.

Quando necessário, as consultas associadas são canceladas e permanecem registradas no sistema, preservando o histórico do atendimento.

---

### Lembretes

A aplicação possui gerenciamento de lembretes relacionados aos atendimentos veterinários.

Os lembretes auxiliam no acompanhamento das consultas e permitem que informações importantes sejam enviadas aos responsáveis.

---

### Envio de e-mails

O ClyvoPet possui integração com serviço de e-mail utilizando **Spring Mail**.

A funcionalidade permite o envio de notificações relacionadas aos lembretes diretamente pela aplicação.

O serviço utiliza comunicação SMTP autenticada e suas credenciais são armazenadas de forma segura por meio de variáveis de ambiente no ambiente de produção.

---

## Autenticação e autorização

A segurança da aplicação é implementada utilizando **Spring Security**.

O sistema possui autenticação de usuários e controle de acesso baseado em funções (*roles*).

### 👑 ADMIN

O administrador possui acesso às funcionalidades de:

- Gerenciamento de veterinários;
- Gerenciamento de animais;
- Gerenciamento de consultas;
- Gerenciamento de lembretes;
- Funcionalidades administrativas.

### 👩‍⚕️ VETERINARIO

O usuário veterinário possui acesso às funcionalidades necessárias para operação da clínica, como:

- Animais;
- Consultas;
- Lembretes.

O gerenciamento de outros veterinários permanece restrito ao perfil **ADMIN**.

As rotas protegidas são controladas pelo Spring Security tanto na camada web quanto nos endpoints da aplicação.

---

## Validação de dados

O projeto utiliza **Jakarta Bean Validation** para garantir a integridade dos dados recebidos.

Entre as validações implementadas estão:

- Campos obrigatórios;
- Validação de e-mail;
- Limites de tamanho;
- Valores numéricos positivos ou iguais a zero;
- Validação de dados dos animais;
- Validação de dados dos veterinários.

As mensagens de validação também são apresentadas diretamente nos formulários da interface utilizando integração entre **Thymeleaf, Spring Validation e Bootstrap**.

---

## Banco de dados

O ClyvoPet utiliza **Oracle Database** para persistência das informações.

Entre as principais tabelas utilizadas estão:

| Tabela | Responsabilidade |
|---|---|
| `VETERINARIOS` | Armazena os profissionais e usuários do sistema |
| `ANIMAIS` | Armazena os animais e informações dos tutores |
| `CONSULTAS` | Armazena os atendimentos veterinários |
| `LEMBRETES` | Armazena os lembretes relacionados aos atendimentos |

O acesso aos dados é realizado utilizando **Spring JDBC / JdbcTemplate**, com separação entre as camadas de Repository, Service e Controller.

---

## Versionamento do banco com Flyway

O banco de dados é versionado utilizando **Flyway**.

As migrations são executadas automaticamente durante a inicialização da aplicação, permitindo que alterações na estrutura do banco sejam controladas e reproduzidas de maneira organizada.

Exemplos de responsabilidades das migrations utilizadas no projeto:

```
db/migration/
├── V1__create_table_initial.sql
├── V2__...
└── V3__allow_null_veterinario_consulta.sql
```

As migrations são responsáveis por tarefas como:

- Criação inicial das tabelas;
- Configuração da estrutura do banco;
- Inclusão de dados necessários para inicialização;
- Evolução das regras e relacionamentos entre as entidades.

---

## Arquitetura

O projeto utiliza uma arquitetura em camadas para separar as responsabilidades da aplicação.

```
Controller
    ↓
Service
    ↓
Repository
    ↓
JdbcTemplate
    ↓
Oracle Database
```

---

## Testes automatizados

Testes de sistema (ponta a ponta) com **Selenium WebDriver + JUnit 5**, que sobem o ClyvoPet em porta aleatória
com banco **H2 em memória (modo Oracle)** e controlam o Chrome. Não precisam do banco Oracle da FIAP.

```
mvn test                    # roda tudo (Chrome sem janela)
mvn test -Dheadless=false   # mostra o navegador (útil para gravar o vídeo)
```

Pré-requisitos: Java 21, Maven e Google Chrome instalados. Detalhes, casos de teste e roteiro do vídeo em
[docs/testes-automatizados.md](docs/testes-automatizados.md). O plano de testes manuais está em
[docs/plano-de-testes-manuais.md](docs/plano-de-testes-manuais.md).

---

## 🛠️ Tecnologias utilizadas

### Backend

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

- Java 21;
- Spring Boot 3.4.1;
- Spring MVC;
- Spring Security;
- Spring JDBC;
- JdbcTemplate;
- Jakarta Validation;
- Spring Mail;
- Maven.

### Frontend

![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)

- HTML5;
- CSS3;
- Thymeleaf;
- Bootstrap;
- JavaScript.

### Banco de dados

![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)

- Oracle Database;
- Oracle JDBC Driver;
- Flyway;
- HikariCP.

### Cloud e DevOps


- Microsoft Azure App Service;
- GitHub;
- GitHub Actions;
- CI/CD;
- Application Insights;
- Variáveis de ambiente para gerenciamento de configurações sensíveis.

---

# ☁️ DevOps e Deploy no Microsoft Azure

O deploy do **ClyvoPet** foi realizado utilizando **Microsoft Azure App Service**, com automação de build e publicação através do **GitHub Actions**.

A infraestrutura foi criada utilizando a **Azure CLI**, permitindo automatizar a criação do Resource Group, App Service Plan, Web App, Application Insights e configurações da aplicação.

O fluxo de publicação utilizado no projeto é:

```
Código fonte
    │
    ▼
GitHub - branch main
    │
    ▼
GitHub Actions
    │
    ├── Maven Build
    │
    ├── mvn clean install
    │
    └── Geração do arquivo .jar
    │
    ▼
Azure App Service
    │
    ├── Java 21
    ├── Linux
    ├── Variáveis de ambiente
    └── Application Insights
    │
    ▼
ClyvoPet em produção
```

---

## Passo a passo para publicação no Azure

### 1. Pré-requisitos

Antes de executar os comandos de criação da infraestrutura, é necessário possuir:

- Uma conta no Microsoft Azure;
- Azure CLI instalada;
- Uma conta no GitHub;
- Repositório do projeto publicado no GitHub;
- Java 21;
- Maven;
- Projeto Spring Boot funcionando localmente.

Para autenticar a Azure CLI:

```
az login
```

Após a autenticação, os comandos seguintes podem ser executados pelo terminal.

---

## 2. Definir as variáveis da infraestrutura

Para facilitar a reutilização dos comandos, inicialmente foram criadas variáveis contendo os nomes dos recursos utilizados no Azure.

```
RESOURCE_GROUP_NAME="rg-clyvo-pet"
WEBAPP_NAME="clyvo-pet-rm561497"
APP_SERVICE_PLAN="clyvo-pet"
LOCATION="northcentralus"
RUNTIME="JAVA:21-java21"
GITHUB_REPO_NAME="isismodd/ChallengeJava3"
BRANCH="main"
APP_INSIGHTS_NAME="ai-clyvo-pet"
```

### Variáveis utilizadas

| Variável | Descrição |
|---|---|
| `RESOURCE_GROUP_NAME` | Nome do Resource Group |
| `WEBAPP_NAME` | Nome da aplicação no Azure App Service |
| `APP_SERVICE_PLAN` | Nome do plano do App Service |
| `LOCATION` | Região do Azure utilizada |
| `RUNTIME` | Runtime utilizado pela aplicação |
| `GITHUB_REPO_NAME` | Repositório GitHub do projeto |
| `BRANCH` | Branch utilizada para deploy |
| `APP_INSIGHTS_NAME` | Nome do Application Insights |

---

## 3. Criar o Resource Group

O primeiro recurso criado foi o **Resource Group**, responsável por agrupar os recursos relacionados ao projeto.

```
az group create \
  --name "$RESOURCE_GROUP_NAME" \
  --location "$LOCATION"
```

Neste projeto foi utilizado:

```
Resource Group: rg-clyvo-pet
Região: northcentralus
```

O Resource Group facilita o gerenciamento dos recursos da aplicação dentro do Azure.

---

## 4. Criar o Application Insights

O **Application Insights** foi utilizado para monitorar a aplicação publicada no Azure.

```
az monitor app-insights component create \
  --app "$APP_INSIGHTS_NAME" \
  --location "$LOCATION" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --application-type web
```

O recurso criado para o projeto foi:

```
ai-clyvo-pet
```

O Application Insights permite acompanhar informações relacionadas ao funcionamento da aplicação, como telemetria, requisições e possíveis falhas em produção.

---

## 5. Criar o App Service Plan

O próximo passo foi criar o plano responsável por fornecer a infraestrutura utilizada pelo Azure App Service.

```
az appservice plan create \
  --name "$APP_SERVICE_PLAN" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --location "$LOCATION" \
  --sku F1 \
  --is-linux
```

Neste projeto foi utilizado o plano:

```
clyvo-pet
```

com:

```
Sistema operacional: Linux
SKU: F1
```

O plano **F1** corresponde à camada gratuita utilizada durante o desenvolvimento e publicação acadêmica do projeto.

---

## 6. Criar o Azure Web App

Após a criação do App Service Plan, foi criado o serviço responsável por hospedar a aplicação Spring Boot.

```
az webapp create \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --plan "$APP_SERVICE_PLAN" \
  --runtime "$RUNTIME"
```

A aplicação foi configurada utilizando:

```
Runtime: Java 21
Sistema operacional: Linux
```

O nome definido para o Web App foi:

```
clyvo-pet-rm561497
```

Após a publicação, a aplicação fica disponível em:

```
https://clyvo-pet-rm561497.azurewebsites.net
```

---

## 7. Habilitar as credenciais de publicação

Para permitir a integração responsável pelo processo de publicação, foi habilitada a política de credenciais SCM do App Service.

```
az resource update \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --namespace Microsoft.Web \
  --resource-type basicPublishingCredentialsPolicies \
  --name scm \
  --parent sites/"$WEBAPP_NAME" \
  --set properties.allow=true
```

Essa configuração permite que os mecanismos de publicação autorizados consigam realizar o deploy da aplicação no Web App.

---

## 8. Obter a Connection String do Application Insights

Após a criação do Application Insights, sua connection string foi recuperada utilizando a Azure CLI.

```
CONNECTION_STRING=$(az monitor app-insights component show \
  --app "$APP_INSIGHTS_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --query connectionString \
  --output tsv)
```

O valor obtido é armazenado temporariamente na variável:

```
$CONNECTION_STRING
```

Essa variável é utilizada posteriormente para configurar o monitoramento no App Service.

---

## 9. Configurar as variáveis de ambiente no Azure

Informações sensíveis não são armazenadas diretamente no código-fonte.

Dados como:

- usuário do banco;
- senha do banco;
- usuário do serviço de e-mail;
- senha de e-mail;
- credenciais de segurança;

são configurados através das **Application Settings** do Azure App Service.

```
az webapp config appsettings set \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --settings \
    APPLICATIONINSIGHTS_CONNECTION_STRING="$CONNECTION_STRING" \
    ApplicationInsightsAgent_EXTENSION_VERSION="~3" \
    XDT_MicrosoftApplicationInsights_Mode="Recommended" \
    XDT_MicrosoftApplicationInsights_PreemptSdk="1" \
    DB_HOST="oracle.fiap.com.br" \
    DB_PORT="1521" \
    DB_SERVICE="orcl" \
    DB_USERNAME="****" \
    DB_PASSWORD="********" \
    EMAIL_USERNAME="*****" \
    EMAIL_PASSWORD="********" \
    THYMELEAF_CACHE="true" \
    SECURITY_USERNAME="****" \
    SECURITY_PASSWORD="****"
```

> ⚠️ As credenciais reais não devem ser adicionadas ao README ou enviadas para o GitHub, por isso está marcarado aqui.

Em produção, esses valores ficam armazenados nas configurações do **Azure App Service**.

---

## 10. Configuração do banco de dados

O ClyvoPet utiliza um banco **Oracle Database**.

As informações necessárias para conexão são fornecidas por variáveis de ambiente:

```
DB_HOST
DB_PORT
DB_SERVICE
DB_USERNAME
DB_PASSWORD
```

Exemplo da configuração:

```
DB_HOST="oracle.fiap.com.br"
DB_PORT="1521"
DB_SERVICE="orcl"
DB_USERNAME="****"
DB_PASSWORD="********"
```

Dessa maneira, dados sensíveis não precisam ficar diretamente no arquivo `application.properties`.

---

## 11. Configuração do serviço de e-mail

O envio dos lembretes utiliza **Spring Mail**.

As credenciais de autenticação também são configuradas através de variáveis de ambiente:

```
EMAIL_USERNAME="*****"
EMAIL_PASSWORD="********"
```

Assim, usuário e senha utilizados pelo serviço SMTP não precisam ser armazenados diretamente no código.

---

## 12. Configuração do Spring Security

As credenciais necessárias para configuração de segurança da aplicação também foram armazenadas através das Application Settings.

```
SECURITY_USERNAME="****"
SECURITY_PASSWORD="****"
```

Essa estratégia evita a exposição de informações sensíveis dentro do repositório GitHub.

---

## 13. Reiniciar o Web App

Depois de configurar as variáveis de ambiente, o Web App foi reiniciado para que as novas configurações fossem carregadas.

```
az webapp restart \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME"
```

O restart garante que a aplicação Spring Boot seja executada utilizando os valores atualizados das variáveis de ambiente.

---

## 14. Conectar o Web App ao Application Insights

Depois da criação dos dois recursos, foi criada a integração entre o Web App e o Application Insights.

```
az monitor app-insights component connect-webapp \
  --app "$APP_INSIGHTS_NAME" \
  --web-app "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME"
```

A partir desse momento, o Application Insights pode receber informações de monitoramento relacionadas à aplicação.

---

# 🔄 CI/CD com GitHub Actions

Além da criação manual da infraestrutura através da Azure CLI, foi configurado um processo de **CI/CD** utilizando GitHub Actions.

CI/CD significa:

```
CI - Continuous Integration
CD - Continuous Deployment / Delivery
```

No ClyvoPet, esse processo permite que alterações realizadas na branch principal sejam compiladas e posteriormente publicadas no Azure.

---

## 15. Integrar o Azure App Service ao GitHub Actions

A integração foi criada utilizando:

```
az webapp deployment github-actions add \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --repo "$GITHUB_REPO_NAME" \
  --branch "$BRANCH" \
  --login-with-github
```

Neste projeto foi configurado:

```
Repositório: isismodd/ChallengeJava3
Branch: main
Web App: clyvo-pet-rm561497
```

Esse processo cria/configura o workflow responsável pela publicação da aplicação.

---

## 16. Workflow do GitHub Actions

O workflow utilizado pelo projeto está localizado em:

```
.github/workflows/main_clyvo-pet-rm561497.yml
```

Arquivo no GitHub:

[main_clyvo-pet-rm561497.yml](https://github.com/isismodd/ChallengeJava3/blob/main/.github/workflows/main_clyvo-pet-rm561497.yml)

O workflow realiza o build da aplicação utilizando Maven antes da publicação.

A etapa de build contém:

```
- name: Build with Maven
  run: mvn clean install -DskipTests
```

O comando:

```
mvn clean install -DskipTests
```

executa uma nova compilação do projeto, gera o pacote da aplicação e ignora a execução dos testes durante essa etapa do pipeline.

---

## 17. Configurar variáveis de ambiente no workflow

Na etapa de build do Maven foram adicionadas as variáveis necessárias para execução da aplicação.

O trecho:

```
- name: Build with Maven
  run: mvn clean install -DskipTests
```

passou a ficar:

```
- name: Build with Maven
  run: mvn clean install -DskipTests
  env:
    SPRING_DATASOURCE_URL: ${{ secrets.SPRING_DATASOURCE_URL }}
    SPRING_DATASOURCE_USERNAME: ${{ secrets.SPRING_DATASOURCE_USERNAME }}
    SPRING_DATASOURCE_PASSWORD: ${{ secrets.SPRING_DATASOURCE_PASSWORD }}
    SPRING_MAIL_HOST: ${{ secrets.SPRING_MAIL_HOST }}
    SPRING_MAIL_PASSWORD: ${{ secrets.SPRING_MAIL_PASSWORD }}
    SPRING_MAIL_USERNAME: ${{ secrets.SPRING_MAIL_USERNAME }}
    SPRING_SECURITY_USER_NAME: ${{ secrets.SPRING_SECURITY_USER_NAME }}
    SPRING_SECURITY_USER_PASSWORD: ${{ secrets.SPRING_SECURITY_USER_PASSWORD }}
```

As informações sensíveis não ficam diretamente dentro do arquivo YAML.

O workflow utiliza **GitHub Secrets**.

---

## 18. Configurar os GitHub Secrets

No repositório GitHub, acessar:

```
Settings
   ↓
Secrets and variables
   ↓
Actions
   ↓
New repository secret
```

Foram configurados os seguintes secrets:

```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SPRING_MAIL_HOST
SPRING_MAIL_PASSWORD
SPRING_MAIL_USERNAME
SPRING_SECURITY_USER_NAME
SPRING_SECURITY_USER_PASSWORD
```

Por exemplo:

```
SPRING_DATASOURCE_URL
→ URL JDBC utilizada para conexão com o Oracle
```

```
SPRING_DATASOURCE_USERNAME
→ usuário do Oracle
```

```
SPRING_DATASOURCE_PASSWORD
→ senha do Oracle
```

```
SPRING_MAIL_HOST
→ servidor SMTP
```

```
SPRING_MAIL_USERNAME
→ usuário utilizado para envio de e-mail
```

```
SPRING_MAIL_PASSWORD
→ credencial utilizada pelo serviço SMTP
```

As credenciais nunca devem ser colocadas diretamente no arquivo `.yml`.

---

## 19. Funcionamento do pipeline

Depois da configuração do GitHub Actions, o fluxo de deploy passa a funcionar da seguinte maneira:

```
Desenvolvedor
     │
     ▼
Alteração no projeto
     │
     ▼
git add .
     │
     ▼
git commit
     │
     ▼
git push origin main
     │
     ▼
GitHub
     │
     ▼
GitHub Actions
     │
     ├── prepara ambiente
     ├── configura Java
     ├── executa Maven
     ├── gera aplicação .jar
     └── realiza deploy
     │
     ▼
Azure App Service
     │
     ▼
Nova versão em produção
```

Dessa maneira, alterações enviadas para a branch configurada podem ser processadas automaticamente pelo pipeline.

---

## 20. Arquitetura DevOps final

A arquitetura de deploy utilizada pelo ClyvoPet pode ser representada da seguinte maneira:

```
┌──────────────────────────┐
│      Desenvolvedor       │
└─────────────┬────────────┘
              │
              │ git push
              ▼
┌──────────────────────────┐
│          GitHub          │
│ isismodd/ChallengeJava3  │
└─────────────┬────────────┘
              │
              ▼
┌──────────────────────────┐
│      GitHub Actions      │
│                          │
│  Maven Clean Install     │
│  Java 21                 │
│  GitHub Secrets          │
└─────────────┬────────────┘
              │
              │ Deploy
              ▼
┌──────────────────────────┐
│    Azure App Service     │
│                         │
│ clyvo-pet-rm561497       │
│ Linux + Java 21          │
└──────┬───────────┬───────┘
       │           │
       │           │
       ▼           ▼
┌─────────────┐  ┌───────────────────┐
│   Oracle    │  │Application Insights│
│  Database   │  │                   │
└─────────────┘  └───────────────────┘
       │
       ▼
┌──────────────────────────┐
│       Spring Mail        │
│   Envio de lembretes     │
└──────────────────────────┘
```

---

## 21. Recursos utilizados no Azure

| Recurso | Nome / Configuração |
|---|---|
| Resource Group | `rg-clyvo-pet` |
| Região | `northcentralus` |
| App Service Plan | `clyvo-pet` |
| App Service | `clyvo-pet-rm561497` |
| Sistema operacional | Linux |
| Runtime | Java 21 |
| SKU | F1 |
| Application Insights | `ai-clyvo-pet` |
| Repositório | `isismodd/ChallengeJava3` |
| Branch de deploy | `main` |
| Build | Maven |
| CI/CD | GitHub Actions |

---

## ✅ Resultado do deploy

Após a conclusão das etapas, o sistema fica disponível através do Azure App Service.

### Aplicação em produção

[https://clyvo-pet-rm561497.azurewebsites.net](https://clyvo-pet-rm561497.azurewebsites.net)

O ambiente de produção utiliza:

- Java 21;
- Spring Boot;
- Azure App Service;
- Linux;
- Oracle Database;
- GitHub Actions;
- Application Insights;
- Variáveis de ambiente;
- GitHub Secrets;
- CI/CD.

Com essa estrutura, o projeto aplica conceitos de **DevOps, Cloud Computing, integração contínua, entrega contínua, segurança de configurações e monitoramento de aplicações em nuvem**.

---

## Desenvolvimento

Projeto desenvolvido como parte das atividades acadêmicas da **FIAP**, aplicando conceitos de desenvolvimento Java, Spring Boot, banco de dados, segurança e computação em nuvem.

---

<p align="center">
  🐾 <strong>ClyvoPet</strong><br>
  Cuidado, organização e tecnologia para a gestão veterinária.
</p>
