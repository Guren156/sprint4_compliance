package br.com.fiap.ClyvoPet.controller.web;

import br.com.fiap.ClyvoPet.model.Consulta;
import br.com.fiap.ClyvoPet.service.AnimalService;
import br.com.fiap.ClyvoPet.service.ConsultaService;
import br.com.fiap.ClyvoPet.service.VeterinarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/consultas")
public class ConsultaWebController {

    private final ConsultaService consultaService;
    private final AnimalService animalService;
    private final VeterinarioService veterinarioService;

    public ConsultaWebController(
            ConsultaService consultaService,
            AnimalService animalService,
            VeterinarioService veterinarioService) {

        this.consultaService = consultaService;
        this.animalService = animalService;
        this.veterinarioService = veterinarioService;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "consultas",
                consultaService.listarTodas()
        );

        return "consultas/lista";
    }

    @GetMapping("/nova")
    public String showFormCadastro(Model model) {

        model.addAttribute(
                "consulta",
                new Consulta()
        );

        model.addAttribute(
                "animais",
                animalService.listarTodos()
        );

        model.addAttribute(
                "veterinarios",
                veterinarioService.listarTodos()
        );

        return "consultas/formulario";
    }

    @PostMapping
    public String salvar(@ModelAttribute Consulta consulta) {

        consultaService.salvar(consulta);

        return "redirect:/web/consultas";
    }

    @GetMapping("/{id}")
    public String detalhes(
            @PathVariable Long id,
            Model model) {

        Consulta consulta = consultaService.buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Consulta não encontrada com ID: " + id
                        )
                );

        model.addAttribute(
                "consulta",
                consulta
        );

        return "consultas/detalhes";
    }

    @GetMapping("/{id}/editar")
    public String showFormEditar(
            @PathVariable Long id,
            Model model) {

        Consulta consulta = consultaService.buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Consulta não encontrada com ID: " + id
                        )
                );

        model.addAttribute(
                "consulta",
                consulta
        );

        model.addAttribute(
                "animais",
                animalService.listarTodos()
        );

        model.addAttribute(
                "veterinarios",
                veterinarioService.listarTodos()
        );

        return "consultas/formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Consulta consulta) {

        consulta.setId(id);

        consultaService.atualizar(consulta);

        return "redirect:/web/consultas";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id) {

        consultaService.cancelar(id);

        return "redirect:/web/consultas";
    }

    @PostMapping("/{id}/finalizar")
    public String finalizar(@PathVariable Long id) {

        consultaService.finalizar(id);

        return "redirect:/web/consultas";
    }
}