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
public class BuscarDispoCapillasRequest {
	
	private String velatorio;
	private String capilla;
	private Integer idCapilla;
	private String fecha;
	
}
