# Plano de testes manuais — ClyvoPet (Sprint 4 — Compliance, Quality Assurance & Tests)

Plano de testes de **sistema** (nível ponta a ponta, caixa-preta) das funcionalidades principais do ClyvoPet, para ser registrado no **Azure Boards / Azure Test Plans**: **1 Plano de Teste por Sprint** e **1 Suíte de Teste por PBI**. Os casos seguem a ISO 29119-3 (identificação, pré-condições, dados de teste, passos e resultados esperados).

> Atenção: os PBIs abaixo foram derivados do que o sistema faz (login, animais, veterinários, consultas, lembretes). Antes de criar no Azure, confira o nome de cada PBI e a sprint em que cada um foi entregue no **release plan** do projeto e ajuste a coluna "Sprint".

## 1. Escopo e ambiente

| Item | Descrição |
|---|---|
| Sistema sob teste | ClyvoPet (Spring Boot 3.4.1, Thymeleaf, Spring Security, Oracle) |
| URL | http://localhost:8082 (execução local, `mvn spring-boot:run`) ou o endereço publicado |
| Navegador | Google Chrome (versão atual) |
| Banco | Oracle com as migrations Flyway V1–V3; usuário ADMIN semeado: `admin@clyvopet.com` |
| Perfis | ADMIN e VETERINARIO |
| Dados | Controlados e predefinidos (seção 2), usados como `ParamValues` no Azure |

**Fora de escopo:** testes de performance, API REST/JWT (`/api/**`) e e-mail real em ambiente sem SMTP (CT23).

## 2. Dados de teste controlados

| Entidade | Dados |
|---|---|
| Veterinário 1 | Dra. Paula Lima · paula.lima@clyvopet.com · Teste@123 · CRMV-SP-10001 |
| Veterinário 2 | Dr. Bruno Alves · bruno.alves@clyvopet.com · Teste@456 · CRMV-SP-10002 |
| Veterinário 3 (admin cria) | Dr. Carlos Mota · carlos.mota@clyvopet.com · Teste@789 · CRMV-SP-10003 · (11) 97777-1111 · Cirurgia |
| Animal 1 | Rex · Cachorro · Labrador · 4 anos · 28.5 kg · M · tutor Maria Silva · (11) 99999-1234 · maria.silva@email.com |
| Animal 2 | Mimi · Gato · Siamês · 2 anos · 4.2 kg · F · tutor João Souza · (11) 98888-0000 · joao.souza@email.com |
| Consulta | Rex com Dra. Paula Lima · 10/11/2026 09:00 · AGENDADA · "Vacinação anual" |

Ordem de execução recomendada: CT01 → CT02 … na ordem numérica (os casos seguintes reaproveitam os dados criados pelos anteriores).

## 3. Estrutura no Azure Test Plans

| Plano de Teste | Suíte (1 por PBI) | Casos |
|---|---|---|
| **ClyvoPet – Sprint 3** | PBI-01 – Autenticação e controle de acesso | CT01, CT02, CT03, CT04, CT05, CT06 |
|  | PBI-02 – Gerenciar animais | CT07, CT08, CT09, CT10, CT11, CT12 |
|  | PBI-03 – Gerenciar veterinários (perfil ADMIN) | CT13, CT14, CT15, CT16 |
| **ClyvoPet – Sprint 4** | PBI-04 – Agendar e gerenciar consultas | CT17, CT18, CT19, CT20, CT21 |
|  | PBI-05 – Lembretes de consulta por e-mail | CT22, CT23, CT24 |

Total: **24 casos de teste** em 5 suítes e 2 planos.

## 4. Casos de teste

### Plano: ClyvoPet – Sprint 3

#### Suíte PBI-01 – Autenticação e controle de acesso

**CT01 – Cadastrar veterinário pela tela de registro** · Prioridade 1

- **Pré-condições:** Aplicação no ar; e-mails e CRMVs dos dados de teste ainda não cadastrados.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| nome | email | senha | crmv |
|---|---|---|---|
| Dra. Paula Lima | paula.lima@clyvopet.com | Teste@123 | CRMV-SP-10001 |
| Dr. Bruno Alves | bruno.alves@clyvopet.com | Teste@456 | CRMV-SP-10002 |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir http://localhost:8082/registrar | Tela "Criar Conta" exibida com os campos Nome, E-mail, Senha, CRMV, Telefone e Especialidade |
| 2 | Preencher Nome completo com @nome@ | Campo preenchido |
| 3 | Preencher E-mail com @email@ | Campo preenchido |
| 4 | Preencher Senha com @senha@ | Campo preenchido (mascarado) |
| 5 | Preencher CRMV com @crmv@ | Campo preenchido |
| 6 | Clicar em "Cadastrar" | Redireciona para /login?sucesso e exibe "Cadastro realizado com sucesso!" |

**CT02 – Login com credenciais válidas** · Prioridade 1

- **Pré-condições:** CT01 executado (veterinário Dra. Paula Lima cadastrado); usuário deslogado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| email | senha |
|---|---|
| paula.lima@clyvopet.com | Teste@123 |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir http://localhost:8082/login | Tela "Bem-vindo de volta" exibida |
| 2 | Preencher E-mail com @email@ e Senha com @senha@ | Campos preenchidos |
| 3 | Clicar em "Entrar" | Redireciona para /home com o título "Bem-vindo ao ClyvoPet" |

**CT03 – Login com credenciais inválidas exibe erro** · Prioridade 1

- **Pré-condições:** CT01 executado; usuário deslogado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| email | senha |
|---|---|
| paula.lima@clyvopet.com | SenhaErrada1 |
| inexistente@clyvopet.com | Teste@123 |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir http://localhost:8082/login | Tela de login exibida |
| 2 | Preencher E-mail com @email@ e Senha com @senha@ | Campos preenchidos |
| 3 | Clicar em "Entrar" | Permanece em /login?error e exibe "E-mail ou senha inválidos"; nenhuma sessão é criada |

**CT04 – Página protegida sem login redireciona para o login** · Prioridade 2

- **Pré-condições:** Usuário deslogado (janela anônima ou após Sair).
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| rota |
|---|
| /web/animais |
| /web/consultas |
| /web/lembretes |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir http://localhost:8082@rota@ diretamente na barra de endereço | Redireciona para /login; o conteúdo da rota não é exibido |

**CT05 – Logout encerra a sessão** · Prioridade 2

- **Pré-condições:** Veterinário logado e na /home.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Na home, clicar no botão de menu (hambúrguer) | Menu de navegação é aberto |
| 2 | Clicar em "Sair" | Redireciona para /login?logout com "Você foi desconectado" |
| 3 | Abrir http://localhost:8082/web/animais | Redireciona para /login (sessão encerrada) |

**CT06 – Veterinário (não-admin) não acessa gestão de veterinários** · Prioridade 2

- **Pré-condições:** Logado como Dra. Paula Lima (perfil VETERINARIO).
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir o menu e verificar os itens | Item "Veterinários" não é exibido |
| 2 | Abrir http://localhost:8082/web/veterinarios | Acesso negado (HTTP 403); a lista de veterinários não é exibida |

#### Suíte PBI-02 – Gerenciar animais

**CT07 – Cadastrar animal com dados do tutor** · Prioridade 1

- **Pré-condições:** Logado como veterinário.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| nome | especie | raca | idade | peso | sexo | tutor | telefone | email_tutor |
|---|---|---|---|---|---|---|---|---|
| Rex | Cachorro | Labrador | 4 | 28.5 | M | Maria Silva | (11) 99999-1234 | maria.silva@email.com |
| Mimi | Gato | Siamês | 2 | 4.2 | F | João Souza | (11) 98888-0000 | joao.souza@email.com |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir http://localhost:8082/web/animais/novo | Formulário "Novo Animal" exibido |
| 2 | Preencher Nome=@nome@, Espécie=@especie@, Raça=@raca@, Idade=@idade@, Peso=@peso@ e Sexo=@sexo@ | Campos preenchidos |
| 3 | Preencher Nome do Tutor=@tutor@, Telefone=@telefone@ e E-mail do Tutor=@email_tutor@ | Campos preenchidos |
| 4 | Clicar em "Salvar" | Redireciona para /web/animais; a lista contém uma linha com ID, @nome@, @especie@, @raca@ e @tutor@ |

**CT08 – Campos obrigatórios do animal bloqueiam o envio** · Prioridade 2

- **Pré-condições:** Logado como veterinário.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| campo_vazio |
|---|
| Nome |
| Espécie |
| Nome do Tutor |
| Telefone |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir http://localhost:8082/web/animais/novo | Formulário exibido |
| 2 | Preencher todos os campos obrigatórios com os dados de Rex, deixando vazio apenas @campo_vazio@ | Campo @campo_vazio@ vazio |
| 3 | Clicar em "Salvar" | Formulário não é enviado; o navegador destaca o campo @campo_vazio@; permanece em /web/animais/novo; nenhum animal é criado |

**CT09 – Valores inválidos no cadastro de animal são rejeitados** · Prioridade 3

- **Pré-condições:** Logado como veterinário.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| campo | valor |
|---|---|
| Idade | -1 |
| Peso (kg) | -5 |
| E-mail do Tutor | maria@ |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir http://localhost:8082/web/animais/novo e preencher os campos obrigatórios com os dados de Rex | Formulário preenchido |
| 2 | Preencher @campo@ com @valor@ | Valor digitado |
| 3 | Clicar em "Salvar" | Formulário não é enviado; o navegador destaca @campo@ como inválido; nenhum animal é criado |

**CT10 – Visualizar detalhes de um animal** · Prioridade 2

- **Pré-condições:** CT07 executado (Rex cadastrado); logado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| nome |
|---|
| Rex |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir http://localhost:8082/web/animais | Lista de animais exibida |
| 2 | Clicar no ícone de olho (Visualizar) da linha @nome@ | Abre /web/animais/{id} com espécie, raça, idade, peso, sexo e dados do tutor de Rex |

**CT11 – Editar dados de um animal** · Prioridade 2

- **Pré-condições:** CT07 executado; logado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| nome | peso_novo |
|---|---|
| Rex | 30.0 |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Na lista de animais, clicar no ícone de lápis (Editar) da linha @nome@ | Formulário "Editar Animal" exibido com os dados atuais |
| 2 | Alterar Peso (kg) para @peso_novo@ e clicar em "Salvar" | Redireciona para /web/animais |
| 3 | Abrir os detalhes de @nome@ | Peso exibido é @peso_novo@ kg |

**CT12 – Desativar animal remove-o da lista** · Prioridade 2

- **Pré-condições:** CT07 executado (Mimi cadastrada); logado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| nome |
|---|
| Mimi |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Na lista, clicar no ícone de lixeira da linha @nome@ | Diálogo "Tem certeza que deseja desativar esse registro?" |
| 2 | Clicar em "Cancelar" no diálogo | Animal continua na lista |
| 3 | Clicar novamente na lixeira e confirmar com "OK" | Redireciona para /web/animais e @nome@ não aparece mais na lista |

#### Suíte PBI-03 – Gerenciar veterinários (perfil ADMIN)

**CT13 – Admin lista veterinários e perfis** · Prioridade 2

- **Pré-condições:** Conta ADMIN disponível (admin@clyvopet.com); CT01 executado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Fazer login com a conta ADMIN | Home exibida |
| 2 | Abrir o menu e clicar em "Veterinários" | Lista com colunas ID, Nome, E-mail, CRMV, Telefone, Especialidade e Perfil |
| 3 | Verificar a coluna Perfil | Administrador aparece como ADMIN e os demais como VETERINÁRIO |

**CT14 – Admin cadastra veterinário** · Prioridade 2

- **Pré-condições:** Logado como ADMIN.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| nome | email | senha | crmv | telefone | especialidade |
|---|---|---|---|---|---|
| Dr. Carlos Mota | carlos.mota@clyvopet.com | Teste@789 | CRMV-SP-10003 | (11) 97777-1111 | Cirurgia |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir http://localhost:8082/web/veterinarios/novo | Formulário de veterinário exibido (com campo Senha) |
| 2 | Preencher Nome=@nome@, E-mail=@email@, Senha=@senha@, CRMV=@crmv@, Telefone=@telefone@ e Especialidade=@especialidade@ | Campos preenchidos |
| 3 | Clicar em "Salvar" | Redireciona para /web/veterinarios e @nome@ aparece na lista |

**CT15 – Admin edita veterinário** · Prioridade 3

- **Pré-condições:** CT14 executado; logado como ADMIN.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| nome | especialidade_nova |
|---|---|
| Dr. Carlos Mota | Dermatologia |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Na lista de veterinários, clicar em Editar na linha @nome@ | Formulário exibido com dados atuais e sem campo Senha |
| 2 | Alterar Especialidade para @especialidade_nova@ e salvar | Lista exibe @especialidade_nova@ para @nome@ |

**CT16 – Admin exclui veterinário** · Prioridade 3

- **Pré-condições:** CT14 executado; logado como ADMIN; veterinário sem consultas.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| nome |
|---|
| Dr. Carlos Mota |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Clicar em Excluir na linha @nome@ | Diálogo "Tem certeza que deseja excluir este veterinário? As consultas vinculadas serão canceladas." |
| 2 | Confirmar com "OK" | Mensagem "Veterinário excluído com sucesso." e @nome@ some da lista |

### Plano: ClyvoPet – Sprint 4

#### Suíte PBI-04 – Agendar e gerenciar consultas

**CT17 – Agendar consulta** · Prioridade 1

- **Pré-condições:** Rex e Dra. Paula Lima cadastrados; logado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| animal | veterinario | data_hora | status | motivo |
|---|---|---|---|---|
| Rex | Dra. Paula Lima | 10/11/2026 09:00 | AGENDADA | Vacinação anual |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir http://localhost:8082/web/consultas/nova | Formulário "Nova Consulta" exibido |
| 2 | Selecionar Animal=@animal@ e Veterinário=@veterinario@ | Opções selecionadas |
| 3 | Informar Data/Hora=@data_hora@, Status=@status@ e Motivo=@motivo@ | Campos preenchidos |
| 4 | Clicar em "Salvar" | Redireciona para /web/consultas; a lista mostra @animal@, @veterinario@, a data/hora e o status @status@ |

**CT18 – Editar consulta (diagnóstico e prescrição)** · Prioridade 2

- **Pré-condições:** CT17 executado; logado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| diagnostico | prescricao |
|---|---|
| Animal saudável | Vermífugo dose única |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Na lista de consultas, clicar em Editar na consulta de Rex | Formulário "Editar Consulta" exibido |
| 2 | Preencher Diagnóstico=@diagnostico@ e Prescrição=@prescricao@ e salvar | Redireciona para /web/consultas |
| 3 | Abrir os detalhes da consulta | Diagnóstico e prescrição exibem os valores informados |

**CT19 – Finalizar consulta agendada** · Prioridade 1

- **Pré-condições:** Consulta de Rex com status AGENDADA; logado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Na lista, clicar em Finalizar (ícone de check) da consulta | Diálogo "Finalizar consulta?" |
| 2 | Confirmar com "OK" | Status da consulta passa a REALIZADA |
| 3 | Verificar os botões da linha | Botões Cancelar, Finalizar e Enviar Lembrete não são mais exibidos |

**CT20 – Cancelar consulta agendada** · Prioridade 1

- **Pré-condições:** Nova consulta AGENDADA criada como no CT17; logado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Na lista, clicar em Cancelar da consulta | Diálogo "Cancelar consulta?" |
| 2 | Confirmar com "OK" | Status da consulta passa a CANCELADA; consulta permanece no histórico |

**CT21 – Excluir veterinário cancela suas consultas agendadas** · Prioridade 2

- **Pré-condições:** Veterinário Dr. Bruno Alves com uma consulta AGENDADA; logado como ADMIN.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| nome |
|---|
| Dr. Bruno Alves |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Excluir o veterinário @nome@ na lista de veterinários e confirmar | Mensagem "Veterinário excluído com sucesso. N consulta(s) vinculada(s) foram canceladas." |
| 2 | Abrir a lista de consultas | A consulta aparece com status CANCELADA e veterinário "Veterinário excluído" |

#### Suíte PBI-05 – Lembretes de consulta por e-mail

**CT22 – Listar lembretes (lista vazia)** · Prioridade 3

- **Pré-condições:** Nenhum lembrete criado; logado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Abrir http://localhost:8082/web/lembretes | Tela de lembretes exibida com colunas ID, Animal, Tutor, E-mail e Data Envio |
| 2 | Verificar o conteúdo | Mensagem "Nenhum lembrete cadastrado." |

**CT23 – Enviar lembrete de consulta ao tutor** · Prioridade 2

- **Pré-condições:** Consulta AGENDADA de Rex (tutor com e-mail); SMTP configurado (EMAIL_USERNAME/EMAIL_PASSWORD); logado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

ParamValues (Parameter values no Azure):

| email_tutor |
|---|
| maria.silva@email.com |

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Na lista de consultas, clicar em Enviar Lembrete da consulta de Rex | Redireciona para os detalhes da consulta |
| 2 | Abrir http://localhost:8082/web/lembretes | Lembrete criado para @email_tutor@ com Data Envio preenchida |
| 3 | Verificar a caixa de entrada de @email_tutor@ | Chega e-mail "Lembrete de Consulta - ClyvoPet" com data, animal, veterinário e motivo |

**CT24 – Lembrete não é criado para tutor sem e-mail** · Prioridade 3

- **Pré-condições:** Animal cadastrado sem E-mail do Tutor e com consulta AGENDADA; logado.
- **Critério de aceite:** todos os resultados esperados abaixo são observados.

| # | Passo (procedimento) | Resultado esperado |
|---|---|---|
| 1 | Na lista de consultas, clicar em Enviar Lembrete dessa consulta | O sistema informa que o tutor não possui e-mail cadastrado e nenhum lembrete é enviado |

## 5. Como criar no Azure DevOps

1. **Casos:** em *Boards → Queries* (ou *Test Plans → Test cases*) use **Import work items** com `azure-test-cases.csv` (UTF-8). As colunas `Test Step`, `Step Action` e `Step Expected` criam os passos de cada Test Case. Se a importação recusar algum campo, crie os casos pela tela *New Test Case* usando as tabelas acima.
2. **Planos:** em *Test Plans → New Test Plan* crie `ClyvoPet – Sprint 3` e `ClyvoPet – Sprint 4`, com *Iteration* igual à sprint.
3. **Suítes:** em cada plano, *New → Static suite* com o nome `PBI-0X – título`. Se os PBIs já existirem no Backlog, use *Requirement-based suite* escolhendo o PBI, para a suíte ficar ligada a ele.
4. **Casos nas suítes:** em cada suíte, *Add existing test case* (ou *New → Add test cases using grid*) e adicione os casos conforme a seção 3.
5. **ParamValues:** abra o Test Case → seção *Parameter values* → *Add parameter* e digite as colunas e linhas das tabelas (os passos já usam `@nome_do_parametro@`). O CSV não importa essa tabela.
6. **Execução:** *Test Plans → Execute* → *Run for web application* (ou *Run*) e marque cada caso/iteração como **Passed** ou **Failed**. Pela regra da disciplina: **Blocked** se a funcionalidade não foi liberada para teste até a entrega e **N/A (Not Applicable)** se o PBI deixou de existir. O CT23 depende de SMTP; sem SMTP configurado, marque **Blocked**.
7. **Relatórios:** em cada Plano, aba *Charts → New test result chart*: **Outcome** (Pie/Stacked, agrupado por *Outcome*) e **Run By** (agrupado por *Run by*). Salve os dois gráficos para cada plano e tire print.
8. **Acesso do professor:** *Project settings → Teams* (membro **administrador** do projeto) e *Organization settings → Users* (Access Level **Basic**). Informe o link de *Overview → Summary* do projeto.

## 6. Testes automatizados (Parte B)

Os testes de interface automatizados (Selenium WebDriver + JUnit 5) estão descritos em [testes-automatizados.md](testes-automatizados.md).
