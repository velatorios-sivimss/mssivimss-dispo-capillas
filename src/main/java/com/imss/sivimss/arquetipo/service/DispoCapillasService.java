package com.imss.sivimss.arquetipo.service;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.Response;

public interface DispoCapillasService {

	Response<Object> buscarRegistrosPorMes(DatosRequest request, Authentication authentication) throws IOException, ParseException;

	Response<Object> buscarCapillasDisponibles(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> buscarOds(DatosRequest request, Authentication authentication)  throws IOException;

	Response<Object> buscarCapillasOcupadas(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> registrarEntrada(DatosRequest request, Authentication authentication) throws IOException, ParseException;

	Response<Object> registrarSalida(DatosRequest request, Authentication authentication)throws IOException, ParseException;

	Response<Object> detallePorDia(DatosRequest request, Authentication authentication) throws IOException, ParseException;

	Response<Object> buscarVelatorios(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> descargarDocumento(DatosRequest request, Authentication authentication) throws IOException, ParseException;

	Response<Object> descargarEntregaCapilla(DatosRequest request, Authentication authentication) throws IOException;

}
