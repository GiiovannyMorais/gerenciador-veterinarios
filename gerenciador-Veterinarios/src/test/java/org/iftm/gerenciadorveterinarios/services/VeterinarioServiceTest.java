package org.iftm.gerenciadorveterinarios.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.AssertTrue;

import org.iftm.gerenciadorveterinarios.entities.Veterinario;
import org.iftm.gerenciadorveterinarios.repositories.VeterinarioRepository;
import org.iftm.gerenciadorveterinarios.servicies.VeterinarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VeterinarioServiceTest {
    
    // cria a simulação/mock de todas as classes que serão injetadas na classe a ser testada
    @Mock
    private VeterinarioRepository repositorio;

    // classe a ser testada que recebera todas as injeçoes de classe mock
    @InjectMocks
    private VeterinarioService service;


    @Test
    public void testarBuscarVeterinarioPorIDExistenteRetornarVeterinarioExistente(){
        //Arrage
        Integer idExistente = 2;
        String nomeExistente = "Pedro";
        int tamanhoEsperado = 3;
        String nomeEsperado = "Ped";
        
        Veterinario veterinarioEsperado = new Veterinario(2,nomeExistente,"","", BigDecimal.valueOf(0));

        when(repositorio.findById(Mockito.anyInt())).thenReturn(Optional.of(veterinarioEsperado));

        //act
        Optional<Veterinario> resposta = service.buscaVeterinariosPeloId(Mockito.anyInt());
        Veterinario veterinarioRetornado = resposta.get();


        assertTrue(resposta.isPresent());
        assertEquals(tamanhoEsperado, veterinarioRetornado.getNome().length());
        assertEquals(nomeEsperado, veterinarioRetornado.getNome());

        verify(repositorio).findById(Mockito.anyInt());
    }

    @Test
    public void testarApagarRealmenteApagarRegistro(){

         Integer idExistente = 2;
        String nomeExistente = "Pedro"; 
        Veterinario veterinarioExcluido = new Veterinario(idExistente, nomeExistente,  "", nomeExistente, BigDecimal.valueOf(0));

        assertDoesNotThrow(
            ()->{
              service.apagar(veterinarioExcluido);  
            }
        );

        verify(repositorio).delete(veterinarioExcluido);
    }

    @Test 
    public void testarBuscaVeterinariosComParteNomeDeveRetornarListaComDoisVeterinarios(){
             //arrege
            String busca = "Silva";

            List<Veterinario> listaVeterinarios = new ArrayList<>();
           

            Veterinario vet1 = new Veterinario(1,"Pedro Silva", "", "",BigDecimal.valueOf(0));
            Veterinario vet2 = new Veterinario(2, "Maria Silva","","",BigDecimal.valueOf(0));

            listaVeterinarios.add(vet1);
            listaVeterinarios.add(vet2);

            //mock
            when(repositorio.findByNomeContains(busca)).thenReturn(listaVeterinarios);


            List<Veterinario> resultado = service.buscaVeterinariosComParteNome(busca);
            //assert
            assertEquals(2,  resultado.size());

    }

    @Test
    public void deveLancarExcecaoAoApagarQuandoIdNaoExistir(){
     //arrege
    Integer idNaoExistente = 100;

    Veterinario veterinarioInexistente = new Veterinario(idNaoExistente, "","","", BigDecimal.valueOf(0));

    when(repositorio.findById(idNaoExistente)).thenReturn(Optional.empty());
    Assertions.assertThrows(RuntimeException.class, () -> {
        service.apagar(veterinarioInexistente);
    });

    verify(repositorio, Mockito.never()).delete(Mockito.any());
}

 

}
