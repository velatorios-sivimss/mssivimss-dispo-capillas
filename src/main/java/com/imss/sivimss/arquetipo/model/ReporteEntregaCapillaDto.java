package com.imss.sivimss.arquetipo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteEntregaCapillaDto {
	
	private String velatorio;
	private Integer idVelatorio;
	private String folio;
	private Integer noCapilla;
	private String nomContratante;
	private String nomRepresentanteVelatorio;
	private String rutaNombreReporte;
	private String tipoReporte;

}
