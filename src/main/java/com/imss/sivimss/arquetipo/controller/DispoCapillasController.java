package com.imss.sivimss.arquetipo.controller;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.arquetipo.service.DispoCapillasService;
import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.Response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class DispoCapillasController {
	
	@Autowired
	DispoCapillasService dispoCapillasService;
	
	@PostMapping("/registro-mensual")
	public Response<?> buscarRegistros(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
	
		return dispoCapillasService.buscarRegistrosPorMes(request,authentication); 
	}
	
	@PostMapping("/disponibles")
	public Response<?> buscarCapillas(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
	
		return dispoCapillasService.buscarCapillasDisponibles(request,authentication);
      
	}

}
