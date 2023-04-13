package com.imss.sivimss.arquetipo.service;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.Response;

public interface DispoCapillasService {

	Response<?> buscarRegistrosPorMes(DatosRequest request, Authentication authentication) throws IOException, ParseException;

	Response<?> buscarCapillasDisponibles(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> buscarOds(DatosRequest request, Authentication authentication)  throws IOException;

	Response<?> buscarCapillasOcupadas(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> registrarEntrada(DatosRequest request, Authentication authentication) throws IOException, ParseException;

	Response<?> registrarSalida(DatosRequest request, Authentication authentication)throws IOException, ParseException;

	Response<?> detallePorDia(DatosRequest request, Authentication authentication) throws IOException, ParseException;

	Response<?> buscarVelatorios(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> descargarDocumento(DatosRequest request, Authentication authentication) throws IOException;

}
