package br.com.alura.forum.dto;

import java.time.LocalDateTime;

import br.com.alura.forum.modelo.Topico;

public class TopicoDTO {
	
	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCricação;
	
	public TopicoDTO(Topico topico) {
		this.id = topico.getId();		
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCricação = topico.getDataCriacao();
	}
	
	public Long getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public LocalDateTime getDataCricação() {
		return dataCricação;
	}

}
