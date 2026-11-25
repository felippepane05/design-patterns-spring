package one.digitalinnovation.gof;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import one.digitalinnovation.gof.model.*;
import one.digitalinnovation.gof.service.ViaCepService;
import one.digitalinnovation.gof.service.impl.ClienteServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {
    @Mock private ClienteRepository clienteRepository;
    @Mock private EnderecoRepository enderecoRepository;
    @Mock private ViaCepService viaCepService;
    @InjectMocks private ClienteServiceImpl service;

    @Test
    void buscarClienteInexistenteRetorna404() {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.buscarPorId(99L));
        assertEquals(404, error.getStatus().value());
        assertEquals("Cliente não encontrado", error.getReason());
    }

    @Test
    void atualizarUsaIdDaUrlMesmoSemIdNoCorpo() {
        verificarAtualizacao(null);
    }

    @Test
    void atualizarIgnoraIdDiferenteNoCorpo() {
        verificarAtualizacao(999L);
    }

    private void verificarAtualizacao(Long idCorpo) {
        Cliente cliente = new Cliente();
        cliente.setId(idCorpo);
        cliente.setNome("Teste");
        Endereco endereco = new Endereco();
        endereco.setCep("01001-000");
        cliente.setEndereco(endereco);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(new Cliente()));
        when(enderecoRepository.findById("01001-000")).thenReturn(Optional.of(endereco));
        service.atualizar(1L, cliente);
        assertEquals(Long.valueOf(1L), cliente.getId());
        verify(clienteRepository).save(cliente);
        verifyNoInteractions(viaCepService);
    }

    @Test
    void atualizarInexistenteNaoCriaCliente() {
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.atualizar(99L, new Cliente())).getStatus().value());
        verify(clienteRepository, never()).save(any());
        verifyNoInteractions(enderecoRepository, viaCepService);
    }

    @Test
    void excluirInexistenteRetorna404() {
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.deletar(99L)).getStatus().value());
        verify(clienteRepository, never()).deleteById(anyLong());
    }
}