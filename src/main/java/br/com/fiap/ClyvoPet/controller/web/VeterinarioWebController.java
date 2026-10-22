package br.com.fiap.ClyvoPet.controller.web;

import br.com.fiap.ClyvoPet.model.Veterinario;
import br.com.fiap.ClyvoPet.service.VeterinarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/veterinarios")
public class VeterinarioWebController {

    private final VeterinarioService veterinarioService;

    public VeterinarioWebController(
            VeterinarioService veterinarioService) {

        this.veterinarioService = veterinarioService;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "veterinarios",
                veterinarioService.listarTodos()
        );

        return "veterinarios/lista";
    }

    @GetMapping("/novo")
    public String showFormCadastro(Model model) {

        model.addAttribute(
                "veterinario",
                new Veterinario()
        );

        return "veterinarios/formulario";
    }

    @PostMapping
    public String salvar(
            @Valid @ModelAttribute("veterinario") Veterinario veterinario,
            BindingResult result) {

        if (result.hasErrors()) {
            return "veterinarios/formulario";
        }

        veterinarioService.salvar(veterinario);

        return "redirect:/web/veterinarios";
    }

    @GetMapping("/{id}/editar")
    public String showFormEditar(
            @PathVariable Long id,
            Model model) {

        Veterinario veterinario =
                veterinarioService.buscarPorId(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Veterinário não encontrado com ID: " + id
                                )
                        );

        model.addAttribute(
                "veterinario",
                veterinario
        );

        return "veterinarios/formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("veterinario") Veterinario veterinario,
            BindingResult result) {

        veterinario.setId(id);

        if (result.hasErrors()) {
            return "veterinarios/formulario";
        }

        veterinarioService.atualizar(veterinario);

        return "redirect:/web/veterinarios";
    }

    @PostMapping("/{id}/delete")
    public String deletar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            int consultasCanceladas =
                    veterinarioService.deletar(id);

            if (consultasCanceladas > 0) {

                redirectAttributes.addFlashAttribute(
                        "mensagem",
                        "Veterinário excluído com sucesso. "
                                + consultasCanceladas
                                + " consulta(s) vinculada(s) foram canceladas."
                );

            } else {

                redirectAttributes.addFlashAttribute(
                        "mensagem",
                        "Veterinário excluído com sucesso."
                );
            }

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não foi possível excluir o veterinário."
            );
        }

        return "redirect:/web/veterinarios";
    }
}