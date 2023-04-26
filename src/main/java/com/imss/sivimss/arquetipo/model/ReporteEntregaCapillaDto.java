package com.imss.sivimss.arquetipo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteEntregaCapillaDto {
	
	private String folioOds;
	private Integer idCapilla;
	private String rutaNombreReporte;
	private String tipoReporte;

}
