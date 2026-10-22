package br.com.fiap.ClyvoPet.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testes de sistema (ponta a ponta) das telas do ClyvoPet. Cada metodo = 1 caso de teste (CT). */
class ClyvoPetUiTest extends BaseSeleniumTest {

    @Test
    @DisplayName("CT01 - Cadastro de veterinario seguido de login com sucesso")
    void cadastroELoginComSucesso() {
        String sufixo = String.valueOf(System.nanoTime());
        String email = "ct01" + sufixo + "@clyvopet.com";

        registrar("Dra. Teste", email, "senha123", "CRMV-SP-" + sufixo.substring(sufixo.length() - 8));
        assertTrue(driver.getCurrentUrl().endsWith("/login?sucesso"));
        assertTrue(driver.findElement(By.cssSelector(".alert-success-custom")).getText()
                .contains("Cadastro realizado com sucesso!"));

        login(email, "senha123");
        assertTrue(driver.getCurrentUrl().endsWith("/home"));
        // o titulo da home tem animacao de entrada: espera ficar visivel
        WebElement titulo = esperar().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h1.home-title")));
        assertEquals("Bem-vindo ao ClyvoPet", titulo.getText());
    }

    @Test
    @DisplayName("CT02 - Login com senha invalida exibe mensagem de erro")
    void loginComSenhaInvalida() {
        login("admin@clyvopet.com", "senha-errada");

        assertTrue(driver.getCurrentUrl().endsWith("/login?error"));
        assertTrue(driver.findElement(By.cssSelector(".alert-danger-custom")).getText()
                .contains("E-mail ou senha inválidos"));
    }

    @Test
    @DisplayName("CT03 - Acesso a pagina protegida sem login redireciona para /login")
    void acessoSemLoginRedirecionaParaLogin() {
        driver.get(url("/web/animais"));

        assertTrue(driver.getCurrentUrl().endsWith("/login"));
        assertEquals("Login - ClyvoPet", driver.getTitle());
    }

    @Test
    @DisplayName("CT04 - Cadastro de animal aparece na lista de animais")
    void cadastroDeAnimal() {
        registrarELogar();
        String nomeAnimal = "Rex" + System.nanoTime();

        driver.get(url("/web/animais/novo"));
        driver.findElement(By.id("nome")).sendKeys(nomeAnimal);
        driver.findElement(By.id("especie")).sendKeys("Cachorro");
        driver.findElement(By.id("raca")).sendKeys("Labrador");
        driver.findElement(By.id("tutorNome")).sendKeys("Maria Silva");
        driver.findElement(By.id("tutorTelefone")).sendKeys("(11) 99999-1234");
        driver.findElement(By.cssSelector("button.btn-primary[type='submit']")).click();

        assertTrue(driver.getCurrentUrl().endsWith("/web/animais"));
        String tabela = driver.findElement(By.cssSelector("table.table")).getText();
        assertTrue(tabela.contains(nomeAnimal));
        assertTrue(tabela.contains("Maria Silva"));
    }

    @Test
    @DisplayName("CT05 - Cadastro de animal sem nome e rejeitado pela validacao do servidor")
    void cadastroDeAnimalSemNome() {
        registrarELogar();
        driver.get(url("/web/animais/novo"));

        // desliga a validacao HTML5 do navegador para exercitar o Bean Validation do servidor
        ((JavascriptExecutor) driver).executeScript("document.querySelector('form.needs-validation').noValidate = true;");
        driver.findElement(By.id("especie")).sendKeys("Gato");
        driver.findElement(By.id("tutorNome")).sendKeys("Joao Souza");
        driver.findElement(By.id("tutorTelefone")).sendKeys("(11) 98888-0000");
        driver.findElement(By.cssSelector("button.btn-primary[type='submit']")).click();

        assertEquals("O nome do animal é obrigatório.",
                driver.findElement(By.cssSelector(".invalid-feedback")).getText());
        assertTrue(driver.getCurrentUrl().endsWith("/web/animais"));
    }

    @Test
    @DisplayName("CT06 - Edicao do nome de um animal e refletida na lista")
    void edicaoDeAnimal() {
        registrarELogar();
        String nomeOriginal = "Mel" + System.nanoTime();
        String nomeNovo = nomeOriginal + "-editado";

        driver.get(url("/web/animais/novo"));
        driver.findElement(By.id("nome")).sendKeys(nomeOriginal);
        driver.findElement(By.id("especie")).sendKeys("Gato");
        driver.findElement(By.id("tutorNome")).sendKeys("Ana Costa");
        driver.findElement(By.id("tutorTelefone")).sendKeys("(11) 97777-5555");
        driver.findElement(By.cssSelector("button.btn-primary[type='submit']")).click();

        driver.findElement(By.xpath("//tr[td[text()='" + nomeOriginal + "']]//a[@title='Editar']")).click();
        driver.findElement(By.id("nome")).clear();
        driver.findElement(By.id("nome")).sendKeys(nomeNovo);
        driver.findElement(By.cssSelector("button.btn-primary[type='submit']")).click();

        String tabela = driver.findElement(By.cssSelector("table.table")).getText();
        assertTrue(tabela.contains(nomeNovo));
    }

    @Test
    @DisplayName("CT07 - Logout encerra a sessao e volta para o login")
    void logout() {
        registrarELogar();

        // na home o menu fica atras do botao hamburguer
        driver.findElement(By.id("menuToggle")).click();
        esperar().until(ExpectedConditions.elementToBeClickable(By.linkText("Sair"))).click();

        assertTrue(driver.getCurrentUrl().endsWith("/login?logout"));
        assertTrue(driver.findElement(By.cssSelector(".alert-success-custom")).getText()
                .contains("Você foi desconectado"));
    }

    @Test
    @DisplayName("CT08 - Veterinario (perfil nao-admin) nao acessa a gestao de veterinarios")
    void veterinarioNaoAcessaAreaAdmin() {
        registrarELogar();

        driver.get(url("/web/veterinarios"));

        assertTrue(driver.getPageSource().contains("403")
                || driver.getPageSource().toLowerCase().contains("forbidden"));
    }
}
