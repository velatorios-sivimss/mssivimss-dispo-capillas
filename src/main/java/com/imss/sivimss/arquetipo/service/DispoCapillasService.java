package com.imss.sivimss.arquetipo.service;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.Response;

public interface DispoCapillasService {

	Response<?> buscarRegistrosPorMes(DatosRequest request, Authentication authentication) throws IOException, ParseException;

}
