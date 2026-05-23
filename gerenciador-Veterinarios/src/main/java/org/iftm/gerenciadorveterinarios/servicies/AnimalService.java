
package org.iftm.gerenciadorveterinarios.servicies;

import java.util.Optional;

import org.iftm.gerenciadorveterinarios.entities.Animal;
import org.iftm.gerenciadorveterinarios.repositories.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository repositorio;

    public Animal cadastrar(Animal animalParaCadastrar) {

        String especie = animalParaCadastrar.getEspecie();

        if (!"Cachorro".equalsIgnoreCase(especie) && !"Gato".equalsIgnoreCase(especie)) {
            throw new IllegalArgumentException("Espécie não atendida pela clínica veterinária.");
        }
        animalParaCadastrar.setInternado(true);
        return repositorio.save(animalParaCadastrar);
    }

    public void darAlta(Integer idExistente) {
        
        Optional<Animal> animalBuscado = repositorio.findById(idExistente);
        
        if (!animalBuscado.isPresent()) {
            throw new RuntimeException("Animal não encontrado para dar alta.");
        }
        
        Animal animal = animalBuscado.get();
        animal.setInternado(false);
        
        repositorio.save(animal);
    }

}