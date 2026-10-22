package br.com.fiap.ClyvoPet.controller.web;

import br.com.fiap.ClyvoPet.service.LembreteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/lembretes")
public class LembreteWebController {

    private final LembreteService lembreteService;

    public LembreteWebController(
            LembreteService lembreteService) {

        this.lembreteService = lembreteService;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "lembretes",
                lembreteService.listarTodos()
        );

        model.addAttribute(
                "pendentes",
                lembreteService.buscarPendentes()
        );

        return "lembretes/lista";
    }

    @GetMapping("/pendentes")
    public String listarPendentes(Model model) {

        var pendentes =
                lembreteService.buscarPendentes();

        model.addAttribute(
                "lembretes",
                pendentes
        );

        model.addAttribute(
                "pendentes",
                pendentes
        );

        return "lembretes/lista";
    }

    @GetMapping("/enviar/{consultaId}")
    public String enviarLembrete(
            @PathVariable Long consultaId) {

        lembreteService.criarEEnviarLembrete(
                consultaId
        );

        return "redirect:/web/consultas/" + consultaId;
    }

    @GetMapping("/enviar-todos")
    public String enviarTodosPendentes() {

        lembreteService.enviarTodosLembretesPendentes();

        return "redirect:/web/lembretes";
    }
}