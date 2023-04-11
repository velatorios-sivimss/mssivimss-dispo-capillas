package com.imss.sivimss.arquetipo.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.arquetipo.beans.DispoCapillas;
import com.imss.sivimss.arquetipo.exception.BadRequestException;
import com.imss.sivimss.arquetipo.model.request.BuscarDispoCapillasRequest;
import com.imss.sivimss.arquetipo.service.DispoCapillasService;
import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.MensajeResponseUtil;
import com.imss.sivimss.arquetipo.util.ProviderServiceRestTemplate;
import com.imss.sivimss.arquetipo.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DispoCapillasImpl implements DispoCapillasService{
	

	@Value("${endpoints.dominio-consulta}")
	private String urlDominioConsulta;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	Gson gson = new Gson();
	
	DispoCapillas dispoCapillas = new DispoCapillas();
	
	//MensajeResponseUtil mensaje;
	
	private static final String SIN_INFORMACION="87";//No contamos con capillas disponibles por el momento. Intenta mas tarde. 

	@Override
	public Response<?> buscarRegistrosPorMes(DatosRequest request, Authentication authentication)
			throws IOException, ParseException {
		String datosJson = String.valueOf(request.getDatos().get("datos"));
		BuscarDispoCapillasRequest buscar = gson.fromJson(datosJson, BuscarDispoCapillasRequest .class);
		Date dateF = new SimpleDateFormat("dd-MM-yyyy").parse(buscar.getFecha());
        DateFormat anioMes = new SimpleDateFormat("yyyy-MM", new Locale("es", "MX"));
        String fecha=anioMes.format(dateF);
        dispoCapillas.setFechaEntrada(fecha);
		return providerRestTemplate.consumirServicio(dispoCapillas.registrosPorMes(request, buscar).getDatos(), urlDominioConsulta + "/paginado",
				authentication);
	}

	@Override
	public Response<?> buscarCapillasDisponibles(DatosRequest request, Authentication authentication)
			throws IOException {
		String datosJson = String.valueOf(request.getDatos().get("datos"));
		BuscarDispoCapillasRequest buscar = gson.fromJson(datosJson, BuscarDispoCapillasRequest .class);
		
		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(dispoCapillas.capillasDisponibles(request, buscar).getDatos(), urlDominioConsulta + "/consulta",
				authentication), SIN_INFORMACION);
		
	}

}
