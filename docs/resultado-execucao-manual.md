# Resultado da execução dos testes manuais — ClyvoPet

Execução dos 24 casos do [plano de testes manuais](plano-de-testes-manuais.md) em 22/10/2026, com os dados controlados do plano.
**Ambiente:** ClyvoPet em localhost (Spring Boot 3.4.1, banco H2 em modo Oracle com as migrations Flyway V1–V3), Google Chrome.
**Perfis:** `paula.lima@clyvopet.com` (VETERINARIO) e um usuário ADMIN criado só para o teste no banco de teste (o ADMIN de produção tem hash de senha desconhecido).

## Resumo

| Resultado | Qtde | Casos |
|---|---|---|
| Passed | 20 | CT01–CT17, CT19, CT20, CT22 |
| Failed | 3 | CT18, CT21, CT24 |
| Blocked | 1 | CT23 |

## Defeitos encontrados

| Caso | Defeito | Evidência |
|---|---|---|
| CT18 | **Editar consulta abre com Data/Hora vazia.** O campo é obrigatório, então o formulário não salva até a data ser digitada de novo. | Ao abrir `/web/consultas/{id}/editar` o valor de `dataHora` vem vazio; "Salvar" permanece em `/editar`. Redigitando a data, salva normalmente. |
| CT21 | **Excluir veterinário que tem consulta falha** com "Não foi possível excluir o veterinário.", contrariando a regra de preservar as consultas. | A migration `V3__allow_null_veterinario_consulta.sql` está vazia, então `CONSULTAS.VETERINARIO_ID` continua `NOT NULL`. |
| CT24 | **Enviar lembrete para tutor sem e-mail retorna HTTP 500** (Whitelabel Error Page) em vez de uma mensagem amigável. | `GET /web/lembretes/enviar/{id}` lança `RuntimeException: O tutor do animal Bolt não possui e-mail cadastrado.` sem tratamento no controller. |

## Blocked

- **CT23 (enviar lembrete por e-mail):** depende de SMTP configurado (`EMAIL_USERNAME`/`EMAIL_PASSWORD`), que não existe no ambiente local.

## Observações

- CT13: no sistema o perfil aparece como **VETERINÁRIO** (com acento); o plano foi ajustado para isso.
- Os resultados estão registrados no Azure Test Plans (planos *ClyvoPet - Sprint 3* e *ClyvoPet - Sprint 4*), com os gráficos **Outcome** e **Run By** na aba Chart de cada plano.
