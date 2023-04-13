package com.imss.sivimss.arquetipo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteDto {
	
	//private String ooad;
	//private Integer idOoad;
	//private Integer mes;
	//private Integer anio;
	//private String nombreMes;
	private String condition;
	private String rutaNombreReporte;
	private String tipoReporte;

}
