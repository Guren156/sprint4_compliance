package br.com.fiap.ClyvoPet.controller.api;

import br.com.fiap.ClyvoPet.model.Animal;
import br.com.fiap.ClyvoPet.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private final AnimalService service;

    public AnimalController(AnimalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Animal> criar(@RequestBody Animal animal) {
        service.salvar(animal);
        return ResponseEntity.status(HttpStatus.CREATED).body(animal);
    }

    @GetMapping
    public ResponseEntity<List<Animal>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/especie/{especie}")
    public ResponseEntity<List<Animal>> buscarPorEspecie(
            @PathVariable String especie) {

        return ResponseEntity.ok(service.buscarPorEspecie(especie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> atualizar(
            @PathVariable Long id,
            @RequestBody Animal animal) {

        animal.setId(id);
        service.atualizar(animal);

        return ResponseEntity.ok(animal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}