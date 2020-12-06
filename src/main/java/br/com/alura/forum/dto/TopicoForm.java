package br.com.alura.forum.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;

public class TopicoForm {

	@NotNull @NotBlank @Size(min = 5)
	private String titulo;
	@NotNull @NotBlank
	private String mensagem; @Size(min = 10)
	@NotNull @NotBlank
	private String nomeCurso;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getNomeCurso() {
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public Topico converter(CursoRepository cursoRespository) {
		Curso curso = cursoRespository.findByNome(nomeCurso);
		return new Topico(titulo, mensagem, curso);
	}
}
