package org.iftm.gerenciadorveterinarios.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.iftm.gerenciadorveterinarios.entities.Animal;
import org.iftm.gerenciadorveterinarios.repositories.AnimalRepository;
import org.iftm.gerenciadorveterinarios.servicies.AnimalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    @Mock
    private AnimalRepository repositorio;

    @InjectMocks
    private AnimalService service;

    @Test
    public void deveForcarStatusInternadoTrueAoCadastrarAnimal() {
        // arrange
        Animal animalParaCadastrar = new Animal(null, "Rex", "Cachorro", 3, false);
        
        Animal animalSalvoEsperado = new Animal(1, "Rex", "Cachorro", 3, true);

        //mock
        when(repositorio.save(Mockito.any(Animal.class))).thenReturn(animalSalvoEsperado);

        // ACT
        Animal animalResultado = service.cadastrar(animalParaCadastrar);

        // ASSERT 
        assertTrue(animalResultado.getInternado(), "O animal cadastrado deve sempre ter o status internado como true");
        assertEquals(1, animalResultado.getId());

        verify(repositorio).save(Mockito.any(Animal.class));
    }

    @Test
    public void deveBarrarDadosInvalidosNoCadastrarAnimal(){
       // arrange
        Animal animalInvalido = new Animal(null,"","Cobral", 1,false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrar(animalInvalido);
        });

        verify(repositorio, Mockito.never()).save(Mockito.any(Animal.class));
    }
    
    @Test void deveMudarStatusParaInternadoFalseAoDarAlta(){
        //arrege
        Integer idExistente = 5;

       Animal animalInternado = new Animal(idExistente,"Botas","Gato",2,true);

       when(repositorio.findById(idExistente)).thenReturn(Optional.of(animalInternado));

       //quando salvarem o animal alterado, apenas retorne ele mesmo
       when(repositorio.save(Mockito.any(Animal.class))).thenReturn(animalInternado);

       service.darAlta(idExistente);

       org.junit.jupiter.api.Assertions.assertFalse(animalInternado.getInternado(), "O status do animal deveria ser alterado para false");
       verify(repositorio).save(animalInternado);

    }
}