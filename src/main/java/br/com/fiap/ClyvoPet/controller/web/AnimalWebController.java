package br.com.fiap.ClyvoPet.controller.web;

import br.com.fiap.ClyvoPet.model.Animal;
import br.com.fiap.ClyvoPet.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/animais")
public class AnimalWebController {

    private final AnimalService animalService;

    public AnimalWebController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("animais", animalService.listarTodos());
        return "animais/lista";
    }

    @GetMapping("/novo")
    public String showFormCadastro(Model model) {
        model.addAttribute("animal", new Animal());
        return "animais/formulario";
    }

    @PostMapping
    public String salvar(
            @Valid @ModelAttribute("animal") Animal animal,
            BindingResult result) {

        if (result.hasErrors()) {
            return "animais/formulario";
        }

        animalService.salvar(animal);

        return "redirect:/web/animais";
    }

    @GetMapping("/{id}")
    public String detalhes(
            @PathVariable Long id,
            Model model) {

        Animal animal = animalService.buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Animal não encontrado com ID: " + id
                        )
                );

        model.addAttribute("animal", animal);

        return "animais/detalhes";
    }

    @GetMapping("/{id}/editar")
    public String showFormEditar(
            @PathVariable Long id,
            Model model) {

        Animal animal = animalService.buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Animal não encontrado com ID: " + id
                        )
                );

        model.addAttribute("animal", animal);

        return "animais/formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("animal") Animal animal,
            BindingResult result) {

        animal.setId(id);

        if (result.hasErrors()) {
            return "animais/formulario";
        }

        animalService.atualizar(animal);

        return "redirect:/web/animais";
    }

    @PostMapping("/{id}/delete")
    public String deletar(@PathVariable Long id) {

        animalService.deletar(id);

        return "redirect:/web/animais";
    }
}