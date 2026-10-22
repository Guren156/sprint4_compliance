# Testes automatizados — ClyvoPet (Sprint 4)

**Ferramenta:** Selenium WebDriver 4.30.0 + JUnit 5 (Java 21, Maven).
**Onde:** `src/test/java/br/com/fiap/ClyvoPet/ui/` (`BaseSeleniumTest` e `ClyvoPetUiTest`).
**Ambiente:** o próprio teste sobe a aplicação (`@SpringBootTest` em porta aleatória, perfil `test`) com
H2 em memória no modo Oracle, aplicando as mesmas migrations Flyway do projeto. Cada execução começa com o
banco limpo, e cada teste cria seus próprios dados (e-mail/CRMV/nome únicos), então os testes não dependem uns dos outros.

## Como executar

```
mvn test                    # Chrome sem janela (headless)
mvn test -Dheadless=false   # Chrome visível (para o vídeo)
mvn test -Dtest=ClyvoPetUiTest#cadastroDeAnimal   # um teste só
```

O Selenium Manager baixa o chromedriver compatível com o Chrome instalado na primeira execução (precisa de internet).

## Casos de teste automatizados

| ID | Método | O que valida | Resultado esperado |
|----|--------|--------------|--------------------|
| CT01 | `cadastroELoginComSucesso` | Cadastro de veterinário em `/registrar` e login em `/login` | Redireciona para `/login?sucesso` com "Cadastro realizado com sucesso!"; após login chega em `/home` com "Bem-vindo ao ClyvoPet" |
| CT02 | `loginComSenhaInvalida` | Login com senha errada | Fica em `/login?error` com "E-mail ou senha inválidos" |
| CT03 | `acessoSemLoginRedirecionaParaLogin` | Acesso a `/web/animais` sem sessão | Redireciona para `/login` |
| CT04 | `cadastroDeAnimal` | Cadastro de animal (Rex, Cachorro, Labrador, tutor Maria Silva) | Animal e tutor aparecem na lista `/web/animais` |
| CT05 | `cadastroDeAnimalSemNome` | Validação do servidor (Bean Validation) com a validação HTML5 desligada | Mensagem "O nome do animal é obrigatório." no formulário |
| CT06 | `edicaoDeAnimal` | Edição do nome de um animal | Novo nome aparece na lista |
| CT07 | `logout` | Clique em "Sair" | Vai para `/login?logout` com "Você foi desconectado" |
| CT08 | `veterinarioNaoAcessaAreaAdmin` | Perfil VETERINARIO acessando `/web/veterinarios` (só ADMIN) | Acesso negado (HTTP 403) |

Resultado da última execução: `Tests run: 9, Failures: 0, Errors: 0` (8 de interface + `contextLoads`).

## Roteiro do vídeo (configuração e execução, sem erros)

1. Mostrar o repositório no GitHub, branch `develop`, e fazer `git clone` + `cd` na pasta.
2. Mostrar as versões: `java -version`, `mvn -version` e o Chrome instalado.
3. Mostrar o `pom.xml` (dependências `selenium-java` e `h2` com escopo `test`) e o `application-test.properties`.
4. Abrir `ClyvoPetUiTest.java` e comentar rapidamente cada caso (tabela acima).
5. Rodar `mvn test -Dheadless=false` e deixar o Chrome aparecendo enquanto os testes executam.
6. Mostrar o final do console: `Tests run: 9, Failures: 0, Errors: 0` e `BUILD SUCCESS`.
7. (Opcional) mostrar o relatório em `target/surefire-reports`.
