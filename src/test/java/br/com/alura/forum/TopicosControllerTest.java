package br.com.alura.forum.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.dto.AtualizacaoTopicoForm;
import br.com.alura.forum.dto.DetalhesTopicoDTO;
import br.com.alura.forum.dto.TopicoDTO;
import br.com.alura.forum.dto.TopicoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@ExtendWith(MockitoExtension.class)
public class TopicosControllerTest {
	
	@InjectMocks
	private TopicosController controller;
	
	@Mock
	private TopicoRepository topicoRepository;
	@Mock
	private CursoRepository cursoRepository;
	
	@Test
	void testeRetornarTodasEntidades() {
		Mockito.when(topicoRepository.findAll()).thenReturn(List.of(new Topico(), new Topico(), new Topico()));
		
		List<TopicoDTO> resultList = controller.lista(null);
		
		assertNotNull(resultList);
		assertEquals(resultList.size(), 3);
		Mockito.verify(topicoRepository).findAll();
		Mockito.verify(topicoRepository, never()).findByCursoNome(Mockito.any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"Java", "PHP", "Python"})
	void testeRetornarEntidadesPorNome(String name) {
		Mockito.when(topicoRepository.findByCursoNome(name)).thenReturn(List.of(new Topico()));
		
		List<TopicoDTO> resultList = controller.lista(name);
		
		assertNotNull(resultList);
		assertEquals(resultList.size(), 1);
		Mockito.verify(topicoRepository).findByCursoNome(name);
		Mockito.verify(topicoRepository, never()).findAll();
	}
	
	@Test
	void testeCadastrar() {
		TopicoForm form = new TopicoForm(); 
		form.setTitulo("Duvida");
		form.setMensagem("Tenho dúvidas me ajuda");
		form.setNomeCurso("Java");
		
		ResponseEntity<TopicoDTO> result = controller.cadastrar(form, UriComponentsBuilder.newInstance());
		
		assertNotNull(result);
		Mockito.verify(topicoRepository).save(Mockito.any());
	}	
	
	@Test
	void testeDetalhar_TopicoEncontrado() {
		String titulo = "Ajuda";
		String mensagem = "Mensagem do tópico";
		Mockito.when(topicoRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(createTopico(titulo, mensagem)));
		
		ResponseEntity<DetalhesTopicoDTO> result = controller.detalhar(1L);
		
		assertNotNull(result);
		assertEquals(result.getStatusCodeValue(), 200);
		assertEquals(result.getBody().getTitulo(), titulo);
		assertEquals(result.getBody().getMensagem(), mensagem);
	}
	
	@Test
	void testeDetalhar_TopicoNaoEncontrado() {
		Mockito.when(topicoRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		ResponseEntity<DetalhesTopicoDTO> result = controller.detalhar(1L);
		
		assertNotNull(result);
		assertEquals(result.getStatusCodeValue(), 404);
		assertNull(result.getBody());
	}
	
	@Test
	void testeAtualizar_TopicoAtualizado() {
		String novoTitulo = "Novo título";
		String novaMensagem = "Nova mensagem do tópico";		
		Mockito.when(topicoRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(createTopico("Ajuda", "Mensagem")));
		Mockito.when(topicoRepository.getOne(Mockito.anyLong())).thenReturn(createTopico("Ajuda", ""));
		
		AtualizacaoTopicoForm atualizacaoTopicoForm = new AtualizacaoTopicoForm();
		atualizacaoTopicoForm.setTitulo(novoTitulo);
		atualizacaoTopicoForm.setMensagem(novaMensagem);
		
		ResponseEntity<TopicoDTO> result = controller.atualizar(1L, atualizacaoTopicoForm);
		
		assertNotNull(result);
		assertEquals(result.getStatusCodeValue(), 200);
		assertEquals(result.getBody().getTitulo(), novoTitulo);
		assertEquals(result.getBody().getMensagem(), novaMensagem);;
	}
	
	@Test
	void testeAtualizar_TopicoNaoEncontrado() {
		Mockito.when(topicoRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		ResponseEntity<TopicoDTO> result = controller.atualizar(1L, new AtualizacaoTopicoForm());
		
		assertNotNull(result);
		assertEquals(result.getStatusCodeValue(), 404);
		assertNull(result.getBody());
	}
	
	@Test
	void testeDeletar_DeleteExecutado() {
		Mockito.when(topicoRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(createTopico("Ajuda", "Mensagem")));
		
		ResponseEntity<?> result = controller.deletar(1L);
		
		Mockito.verify(topicoRepository).deleteById(1L);
		assertNotNull(result);
		assertEquals(result.getStatusCodeValue(), 200);
	}	
	
	@Test
	void testeDeletar_TopicoNaoEncontrado() {
		Mockito.when(topicoRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		ResponseEntity<?> result = controller.deletar(1L);
		
		Mockito.verify(topicoRepository, never()).deleteById(1L);
		assertNotNull(result);
		assertEquals(result.getStatusCodeValue(), 404);
	}	

	private Topico createTopico(String titulo, String mensagem) {
		Topico topico = new Topico(titulo, mensagem, new Curso());
		topico.setAutor(new Usuario());
		return topico;
	}	
	

}
