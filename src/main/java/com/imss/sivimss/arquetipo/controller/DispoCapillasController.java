package com.imss.sivimss.arquetipo.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.sivimss.arquetipo.service.DispoCapillasService;
import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.LogUtil;
import com.imss.sivimss.arquetipo.util.ProviderServiceRestTemplate;
import com.imss.sivimss.arquetipo.util.Response;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import java.util.logging.Level;

@AllArgsConstructor
@RestController
@RequestMapping("/")
public class DispoCapillasController {
	
	private static final String ALTA = "alta";
	private static final String IMPRIMIR = "imprimir";
	private static final String MODIFICACION = "modificacion";
	private static final String CONSULTA = "consulta";
	
	@Autowired
	private LogUtil logUtil;
	
	@Autowired
	DispoCapillasService dispoCapillasService;
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	@PostMapping("catalogo-velatorios")
	public CompletableFuture<Object> buscarVelatorios(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Catalogo Velatorios", CONSULTA);
		Response<Object> response = dispoCapillasService.buscarVelatorios(request,authentication); 
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	@PostMapping("registro-mensual")
	public CompletableFuture<Object> buscarRegistros(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Registro mensual de e/s por velatorio", CONSULTA);
		Response<Object> response =  dispoCapillasService.buscarRegistrosPorMes(request,authentication); 
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	@PostMapping("disponibles")
	public CompletableFuture<Object> buscarCapillas(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Buscar capillas disponibles", CONSULTA);
		Response<Object> response =  dispoCapillasService.buscarCapillasDisponibles(request,authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	@PostMapping("buscar-ods")
	public CompletableFuture<Object> buscarOrdenServicio(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Buscar orden de servicio", CONSULTA);
		Response<Object> response = dispoCapillasService.buscarOds(request,authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	@PostMapping("ocupadas")
	public CompletableFuture<Object> buscarCapillasOcupadas(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Buscar capillas ocupadas", CONSULTA);
		Response<Object> response = dispoCapillasService.buscarCapillasOcupadas(request,authentication); 
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	@PostMapping("registrar-entrada")
	public CompletableFuture<Object> registrarEntradaCapilla(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Registrar entrada a capilla", ALTA);
		Response<Object> response = dispoCapillasService.registrarEntrada(request,authentication); 
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	@PostMapping("registrar-salida")
	public CompletableFuture<Object> registrarSalidaCapilla(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Registrar salida a capilla", MODIFICACION);
		Response<Object> response = dispoCapillasService.registrarSalida(request,authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	@PostMapping("detalle-por-dia")
	public CompletableFuture<Object> detalleRegistroCapilla(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Detalle de registros en e/s de capilla", CONSULTA);
		Response<Object> response = dispoCapillasService.detallePorDia(request,authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	@PostMapping("descargar-reporte")
	public CompletableFuture<Object> descargarReporte(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException {
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Imprimir historico mensual de e/s por velatorio", IMPRIMIR);
		Response<Object> response = dispoCapillasService.descargarDocumento(request,authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	@CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
	@TimeLimiter(name = "msflujo")
	@PostMapping("descargar-entrega-capilla")
	public CompletableFuture<Object> descargarReporteEntregaCapilla(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"Imprimir plantilla entrega de capilla", IMPRIMIR);
		Response<Object> response = dispoCapillasService.descargarEntregaCapilla(request,authentication);
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}
	
	/**
	 * fallbacks generico
	 * 
	 * @return respuestas
	 */
	private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			CallNotPermittedException e) {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			RuntimeException e) {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
			NumberFormatException e) {
		Response<?> response = providerRestTemplate.respuestaProvider(e.getMessage());
		return CompletableFuture
				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
	}

	
}
