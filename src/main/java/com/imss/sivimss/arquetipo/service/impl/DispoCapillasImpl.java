package com.imss.sivimss.arquetipo.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.arquetipo.beans.DispoCapillas;
import com.imss.sivimss.arquetipo.exception.BadRequestException;
import com.imss.sivimss.arquetipo.model.ReporteDto;
import com.imss.sivimss.arquetipo.model.ReporteEntregaCapillaDto;
import com.imss.sivimss.arquetipo.model.UsuarioDto;
import com.imss.sivimss.arquetipo.model.request.BuscarDispoCapillasRequest;
import com.imss.sivimss.arquetipo.model.request.BuscarRegistroMensualRequest;
import com.imss.sivimss.arquetipo.model.request.DispoCapillasRequest;
import com.imss.sivimss.arquetipo.service.DispoCapillasService;
import com.imss.sivimss.arquetipo.util.AppConstantes;
import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.LogUtil;
import com.imss.sivimss.arquetipo.util.MensajeResponseUtil;
import com.imss.sivimss.arquetipo.util.ProviderServiceRestTemplate;
import com.imss.sivimss.arquetipo.util.Response;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DispoCapillasImpl implements DispoCapillasService{
	

	@Value("${endpoints.dominio-consulta}")
	private String urlConsulta;
	
	@Value("${endpoints.dominio-reportes}")
	private String urlReportes;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	Gson gson = new Gson();
	
	DispoCapillas dispoCapillas = new DispoCapillas();
	
	@Autowired
	private LogUtil logUtil;
	
	private static final String IMPRIMIR = "IMPRIMIR";
	private static final String ALTA = "alta";
	private static final String MODIFICACION = "modificacion";
	private static final String CONSULTA = "consulta";
	private static final String EXITO = "EXITO";
	private static final String ERROR_DESCARGA= "64";
	
	 private static final String PATH_CONSULTA="/consulta";
	 private static final String PATH_INSERTAR_MULTIPLE="/insertarMultiple";
	
	private static final String SIN_INFORMACION="87";//No contamos con capillas disponibles por el momento. Intenta mas tarde. 
	private static final String NO_EXISTE_ODS="85";//El numero de folio no existe. Verifica tu información.
	private static final String AGREGADO_CORRECTAMENTE="84";//Has registrado la entrada del servicio correctamente
	private static final String SALIDA_CORRECTA="83";//Has registrado la salida del servicio correctamente
	@Override
	public Response<Object> buscarRegistrosPorMes(DatosRequest request, Authentication authentication)
			throws IOException, ParseException {
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		BuscarRegistroMensualRequest buscarMensual = gson.fromJson(datosJson, BuscarRegistroMensualRequest .class);
		String fechaCompleta = buscarMensual.getMes() +"-" +buscarMensual.getAnio();
		Date dateF = new SimpleDateFormat("MM-yyyy").parse(fechaCompleta);
        DateFormat anioMes = new SimpleDateFormat("yyyy-MM", new Locale("es", "MX"));
        String fecha=anioMes.format(dateF);
        Response<Object> response = providerRestTemplate.consumirServicio(dispoCapillas.registrosPorMes(request, buscarMensual.getIdVelatorio(), fecha).getDatos(), urlConsulta+PATH_CONSULTA,
				authentication);
        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"DETALLE OK", CONSULTA);
        return MensajeResponseUtil.mensajeConsultaResponse(response, EXITO);
	}

	@Override
	public Response<Object> buscarCapillasDisponibles(DatosRequest request, Authentication authentication)
			throws IOException {
		Response<Object> response = providerRestTemplate.consumirServicio(dispoCapillas.capillasDisponibles(request).getDatos(), urlConsulta+PATH_CONSULTA,
				authentication);
		 logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"CONSULTA CAPILLAS DISPONIBLES OK", CONSULTA);
				return MensajeResponseUtil.mensajeConsultaResponse(response, SIN_INFORMACION);
	}

	@Override
	public Response<Object> buscarOds(DatosRequest request, Authentication authentication) throws IOException {
		Response<Object> response = providerRestTemplate.consumirServicio(dispoCapillas.buscarOrdenServicio(request).getDatos(), urlConsulta+PATH_CONSULTA,
					authentication);
		 logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"CONSULTA ODS OK", CONSULTA);
		return MensajeResponseUtil.mensajeConsultaResponse(response, NO_EXISTE_ODS);
	}

	@Override
	public Response<Object> buscarCapillasOcupadas(DatosRequest request, Authentication authentication) throws IOException {
		Response<Object> response =providerRestTemplate.consumirServicio(dispoCapillas.capillasOcupadas(request).getDatos(), urlConsulta+PATH_CONSULTA,
				authentication);
		 logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"CONSULTA CAPILLAS OCUPADAS OK", CONSULTA);
		return MensajeResponseUtil.mensajeConsultaResponse(response, "Sin información");
	}

	@Override
	public Response<Object> registrarEntrada(DatosRequest request, Authentication authentication) throws IOException, ParseException {
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		DispoCapillasRequest dispoCapillasR = gson.fromJson(datosJson, DispoCapillasRequest.class);	
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		dispoCapillas = new DispoCapillas(dispoCapillasR);
		dispoCapillas.setIdUsuarioAlta(usuarioDto.getIdUsuario());
		dispoCapillas.setFechaEntrada(formatFechas(dispoCapillasR.getFechaEntrada()));
		dispoCapillas.setHoraEntrada(formatHoras(dispoCapillasR.getHoraEntrada()));
		if(dispoCapillasR.getIdCapilla()==null) {
		throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta ");	
		}
			return MensajeResponseUtil.mensajeResponse(providerRestTemplate.consumirServicio( dispoCapillas.insertarEntrada().getDatos(), urlConsulta+PATH_INSERTAR_MULTIPLE,
					authentication), AGREGADO_CORRECTAMENTE);
	}
	
	@Override
	public Response<Object> registrarSalida(DatosRequest request, Authentication authentication)
			throws IOException, ParseException {
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		DispoCapillasRequest dispoCapillasR = gson.fromJson(datosJson, DispoCapillasRequest.class);	
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		dispoCapillas = new DispoCapillas(dispoCapillasR);
		dispoCapillas.setIdUsuarioModifica(usuarioDto.getIdUsuario());
		dispoCapillas.setFechaSalida(formatFechas(dispoCapillasR.getFechaSalida()));
		dispoCapillas.setHoraSalida(formatHoras(dispoCapillasR.getHoraSalida()));
		if(dispoCapillasR.getIdCapilla()==null || dispoCapillasR.getIdDisponibilidad()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		return MensajeResponseUtil.mensajeResponse(providerRestTemplate.consumirServicio( dispoCapillas.insertarSalida().getDatos(),  urlConsulta+PATH_INSERTAR_MULTIPLE,
						authentication), SALIDA_CORRECTA);
	}
	
	@Override
	public Response<Object> detallePorDia(DatosRequest request, Authentication authentication)
			throws IOException, ParseException {
		String datosJson = String.valueOf(request.getDatos().get("datos"));
		BuscarDispoCapillasRequest buscar = gson.fromJson(datosJson, BuscarDispoCapillasRequest .class);
        dispoCapillas.setFechaEntrada(formatFechas(buscar.getFecha()));
        Response<Object> response= providerRestTemplate.consumirServicio(dispoCapillas.detalleRegistro(request, buscar.getIdCapilla()).getDatos(), urlConsulta+PATH_CONSULTA,
				authentication);
        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"DETALLE OK", CONSULTA);
        return MensajeResponseUtil.mensajeConsultaResponse(response,"No hay registros en el dia");
	}	

	private String formatHoras(String hora) throws ParseException {
		Date dateF = new SimpleDateFormat("HH:mm").parse(hora);
        DateFormat horaFormateada = new SimpleDateFormat("HH:mm:ss", new Locale("es", "MX"));
        return horaFormateada.format(dateF);
	}

	private String formatFechas(String fecha) throws ParseException {
		Date dateF = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);
        DateFormat fechaFormateada = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "MX"));
        return fechaFormateada.format(dateF);
	}

	@Override
	public Response<Object> buscarVelatorios(DatosRequest request, Authentication authentication) throws IOException {
		return providerRestTemplate.consumirServicio(dispoCapillas.catalogoVelatorio(request).getDatos(), urlConsulta+PATH_CONSULTA,
				authentication);
	}

	@Override
	public Response<Object> descargarDocumento(DatosRequest request, Authentication authentication) throws IOException, ParseException {
	
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ReporteDto reporteDto= gson.fromJson(datosJson, ReporteDto.class);
		if(reporteDto.getAnio()==null || reporteDto.getMes()==null || reporteDto.getIdVelatorio()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Falta infomación");
		}
		Map<String, Object> envioDatos = new DispoCapillas().generarReporte(reporteDto);
		Response <Object> response = providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes ,
				authentication);
		 logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"GENERAR DOCUMENTO OK", IMPRIMIR);
		return MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_DESCARGA);
	}

	@Override
	public Response<Object> descargarEntregaCapilla(DatosRequest request, Authentication authentication) throws IOException {
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ReporteEntregaCapillaDto reporte= gson.fromJson(datosJson, ReporteEntregaCapillaDto.class);
		if(reporte.getFolioOds()==null || reporte.getIdCapilla()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Falta infomación");
		}
		Map<String, Object> envioDatos = new DispoCapillas().reporteEntregaCapillas(reporte);
		Response <Object> response = providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes ,
				authentication);
		 logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"GENERAR PLANTILLA OK", IMPRIMIR);
		return MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_DESCARGA);
	}
}