package com.imss.sivimss.arquetipo.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@JsonIgnoreType(value = true)
public class DispoCapillasRequest {
	
	private Integer idCapilla;
	private Integer idDisponibilidad;
	private String fechaEntrada;
	private String horaEntrada;
	private Integer idOrdenServicio;
	private String fechaSalida;
	private String horaSalida;
	private Integer cveEstatus;

}
