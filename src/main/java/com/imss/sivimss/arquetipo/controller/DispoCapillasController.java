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
	
	@PostMapping("/catalogo-velatorios")
	public Response<?> buscarVelatorios(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return dispoCapillasService.buscarVelatorios(request,authentication); 
	}
	
	@PostMapping("/registro-mensual")
	public Response<?> buscarRegistros(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
	
		return dispoCapillasService.buscarRegistrosPorMes(request,authentication); 
	}
	
	@PostMapping("/disponibles")
	public Response<?> buscarCapillas(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
	
		return dispoCapillasService.buscarCapillasDisponibles(request,authentication);
	}
	
	@PostMapping("/buscar-ods")
	public Response<?> buscarOrdenServicio(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
	
		return dispoCapillasService.buscarOds(request,authentication);
	}
	
	@PostMapping("/ocupadas")
	public Response<?> buscarCapillasOcupadas(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
	
		return dispoCapillasService.buscarCapillasOcupadas(request,authentication); 
	}
	
	@PostMapping("/registrar-entrada")
	public Response<?> registrarEntradaCapilla(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
	
		return dispoCapillasService.registrarEntrada(request,authentication); 
	}
	
	@PostMapping("/registrar-salida")
	public Response<?> registrarSalidaCapilla(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
	
		return dispoCapillasService.registrarSalida(request,authentication);
	}

	@PostMapping("/detalle-por-dia")
	public Response<?> detalleRegistroCapilla(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
	
		return dispoCapillasService.detallePorDia(request,authentication);
      
	}
}
