package br.com.fiap.ClyvoPet.controller.web;

import br.com.fiap.ClyvoPet.model.Veterinario;
import br.com.fiap.ClyvoPet.service.VeterinarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthWebController {

    private final VeterinarioService veterinarioService;

    public AuthWebController(VeterinarioService veterinarioService) {
        this.veterinarioService = veterinarioService;
    }

    @GetMapping("/registrar")
    public String showRegistro(Model model) {
        model.addAttribute("veterinario", new Veterinario());
        return "registrar";
    }

    @PostMapping("/registrar")
    public String registrar(Veterinario veterinario) {

        try {

            // Cadastro público nunca pode criar ADMIN
            veterinario.setRole("VETERINARIO");

            veterinarioService.salvar(veterinario);

            return "redirect:/login?sucesso";

        } catch (Exception e) {

            return "redirect:/registrar?error";
        }
    }
}