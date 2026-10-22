package br.com.fiap.ClyvoPet.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

/**
 * Sobe o ClyvoPet em porta aleatoria (banco H2 em memoria, perfil "test")
 * e abre o Chrome via Selenium WebDriver.
 * Para ver o navegador (ex.: gravar o video): mvn test -Dheadless=false
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
abstract class BaseSeleniumTest {

    @LocalServerPort
    private int port;

    protected WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        ChromeOptions options = new ChromeOptions();
        if (!"false".equals(System.getProperty("headless"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1366,900", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    void fecharNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected WebDriverWait esperar() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected String url(String caminho) {
        return "http://localhost:" + port + caminho;
    }

    /** Cadastra um veterinario pela tela /registrar (dados unicos por execucao). */
    protected void registrar(String nome, String email, String senha, String crmv) {
        driver.get(url("/registrar"));
        driver.findElement(By.id("nome")).sendKeys(nome);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("senha")).sendKeys(senha);
        driver.findElement(By.id("crmv")).sendKeys(crmv);
        driver.findElement(By.cssSelector("button.btn-register")).click();
        esperar().until(d -> !d.getCurrentUrl().endsWith("/registrar"));
    }

    protected void login(String email, String senha) {
        driver.get(url("/login"));
        driver.findElement(By.id("username")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(senha);
        driver.findElement(By.cssSelector("button.btn-login")).click();
        esperar().until(d -> !d.getCurrentUrl().endsWith("/login"));
    }

    /** Cria um veterinario novo e ja entra no sistema com ele. */
    protected String registrarELogar() {
        String sufixo = String.valueOf(System.nanoTime());
        String email = "vet" + sufixo + "@clyvopet.com";
        registrar("Vet Teste " + sufixo, email, "senha123", "CRMV-SP-" + sufixo.substring(sufixo.length() - 8));
        login(email, "senha123");
        return email;
    }
}
