package br.com.alura.forum.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.alura.forum.modelo.StatusTopico;
import br.com.alura.forum.modelo.Topico;

public class DetalhesTopicoDTO extends TopicoDTO {

	private String nomeAutor;
	private StatusTopico status;
	private List<RespostaDTO> listRespostas;

	public DetalhesTopicoDTO(Topico topico) {
		super(topico);
		this.nomeAutor = topico.getAutor().getNome();
		this.status = topico.getStatus();
		this.listRespostas = getListRespostas(topico);
	}

	public String getNomeAutor() {
		return nomeAutor;
	}

	public StatusTopico getStatus() {
		return status;
	}

	public List<RespostaDTO> getListRespostas() {
		return listRespostas;
	}

	private List<RespostaDTO> getListRespostas(Topico topico) {
		return topico.getRespostas().stream().map(RespostaDTO::new).collect(Collectors.toList());
	}

}
