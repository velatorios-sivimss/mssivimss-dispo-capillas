package com.imss.sivimss.arquetipo.beans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.arquetipo.model.ReporteDto;
import com.imss.sivimss.arquetipo.model.ReporteEntregaCapillaDto;
import com.imss.sivimss.arquetipo.model.request.BuscarDispoCapillasRequest;
import com.imss.sivimss.arquetipo.model.request.DispoCapillasRequest;
import com.imss.sivimss.arquetipo.util.AppConstantes;
import com.imss.sivimss.arquetipo.util.DatosRequest;
import com.imss.sivimss.arquetipo.util.QueryHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class DispoCapillas {
	
	private Integer idCapilla;
	private Integer idDisponibilidad;
	 private String fechaEntrada;
	 private String horaEntrada;
	 private Integer idOrdenServicio;
	 private String fechaSalida;
	 private String horaSalida;
	 private Integer cveEstatus;
	 private Integer idUsuarioAlta;
	 private Integer idUsuarioModifica;
	 private Integer idUsuarioBaja;
	 
	 		public DispoCapillas(DispoCapillasRequest dispoCapillas) {
	 		this.idCapilla = dispoCapillas.getIdCapilla();
		    this.idDisponibilidad = dispoCapillas.getIdDisponibilidad();
			this.fechaEntrada = dispoCapillas.getFechaEntrada();
			this.horaEntrada = dispoCapillas.getHoraEntrada();
			this.idOrdenServicio = dispoCapillas.getIdOrdenServicio();
			this.fechaSalida = dispoCapillas.getFechaSalida();
			this.horaSalida = dispoCapillas.getHoraSalida();
			this.cveEstatus = dispoCapillas.getCveEstatus();
	 }

			public DatosRequest registrosPorMes(DatosRequest request, Integer id, String fecha) {
				String query = "SELECT "
						+ "SC.ID_CAPILLA AS idCapilla,"
						+ " SC.CVE_COLOR AS color, "
						+ "DATE_FORMAT(SD.FEC_ENTRADA, \"%d-%m-%Y\") AS fechaEntrada,"
						+ " TIME_FORMAT(SD.TIM_HORA_ENTRADA, \"%H:%i\") AS hrEntrada, "
						+ "DATE_FORMAT(SD.FEC_SALIDA, \"%d-%m-%Y\") AS fechaSalida, "
						+ "TIME_FORMAT(SD.TIM_HORA_SALIDA, \"%H:%i\") AS hrSalida, "
						+ "SC.NOM_CAPILLA AS nomCapilla, SV.NOM_VELATORIO AS nomVelatorio"
						+ " FROM SVC_CAPILLA SC "
						+ " JOIN SVT_DISPONIBILIDAD_CAPILLAS SD ON SD.ID_CAPILLA = SC.ID_CAPILLA "
						+ " JOIN SVC_VELATORIO SV ON SV.ID_VELATORIO = SC.ID_VELATORIO "
						+ " WHERE SC.CVE_ESTATUS=1 AND SD.FEC_ENTRADA LIKE '%"+ fecha +"%' "
						+ " AND SC.ID_VELATORIO = '"+id+"'";
				log.info(query);
				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				return request;
			}

			public DatosRequest capillasDisponibles(DatosRequest request) {
				String palabra = request.getDatos().get(""+AppConstantes.PALABRA+"").toString();
				String query = "SELECT SC.ID_CAPILLA AS idCapilla, SC.NOM_CAPILLA AS nomCapilla,"
						+ " SV.NOM_VELATORIO AS nomVelatorio, "
						+ " SC.IND_DISPONIBILIDAD AS disponibilidad "
						+ "FROM SVC_CAPILLA SC "
						+ "JOIN SVC_VELATORIO SV ON SV.ID_VELATORIO = SC.ID_VELATORIO "
						+ " WHERE SC.CVE_ESTATUS=1 AND SC.IND_DISPONIBILIDAD=1 AND SV.ID_VELATORIO="+ Integer.parseInt(palabra) +""
								+ " GROUP BY SC.ID_CAPILLA";
					
				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				log.info(query);
				return request;
			}

			public DatosRequest buscarOrdenServicio(DatosRequest request) {
				String palabra = request.getDatos().get(""+AppConstantes.PALABRA+"").toString();
				String query = "SELECT CONCAT (PS.NOM_PERSONA, ' ', PS.NOM_PRIMER_APELLIDO, ' ', PS.NOM_SEGUNDO_APELLIDO) AS finado, " 
								+ "OS.ID_ORDEN_SERVICIO AS idOds, "
								+ "(SELECT CONCAT (SPN.NOM_PERSONA, ' ', SPN.NOM_PRIMER_APELLIDO, ' ', SPN.NOM_SEGUNDO_APELLIDO) "
								+ "FROM SVC_CONTRATANTE SC  "
								+ "JOIN SVC_ORDEN_SERVICIO ON OS.ID_CONTRATANTE = SC.ID_CONTRATANTE  "
								+ "JOIN SVC_PERSONA SPN ON SC.ID_PERSONA = SPN.ID_PERSONA LIMIT 1) AS nombreContratante, "
								+ "CONCAT(SUS.NOM_USUARIO, ' ', SUS.NOM_APELLIDO_PATERNO, ' ', SUS.NOM_APELLIDO_MATERNO )AS representanteVelatorio "
								+ "FROM SVC_ORDEN_SERVICIO OS  "
								+ "JOIN SVC_FINADO SF ON OS.ID_ORDEN_SERVICIO = SF.ID_ORDEN_SERVICIO  "
								+ "JOIN SVT_USUARIOS SUS ON SF.ID_VELATORIO = SUS.ID_VELATORIO "
								+ "JOIN SVC_PERSONA PS ON PS.ID_PERSONA = SF.ID_PERSONA "
								+ "WHERE OS.CVE_ESTATUS = 2 OR OS.CVE_ESTATUS = 3 AND OS.CVE_FOLIO = '"+ palabra +"' ";
					log.info(query);
				request.getDatos().remove(""+AppConstantes.PALABRA+"");
				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				return request;
			}

			public DatosRequest capillasOcupadas(DatosRequest request) {
				String palabra = request.getDatos().get(""+AppConstantes.PALABRA+"").toString();
				String query = "SELECT SC.ID_CAPILLA AS idCapilla, SC.NOM_CAPILLA AS nomCapilla, "
						+ "SC.IND_DISPONIBILIDAD AS disponibilidad, SD.ID_DISPONIBILIDAD AS idDisponibilidad, "
						+ "DATE_FORMAT(SD.FEC_ENTRADA, \"%d-%m-%Y\") AS fechaEntrada, "
						+ "TIME_FORMAT(SD.TIM_HORA_ENTRADA, \"%H:%i\") AS horaEntrada,"
						+ " SV.NOM_VELATORIO AS nomVelatorio "
						+ "FROM SVC_CAPILLA SC "
						+ "JOIN SVC_VELATORIO SV ON SV.ID_VELATORIO = SC.ID_VELATORIO "
						+ "JOIN SVT_DISPONIBILIDAD_CAPILLAS SD ON SD.ID_CAPILLA = SC.ID_CAPILLA "
						+ "WHERE  SC.CVE_ESTATUS=1 AND SC.IND_DISPONIBILIDAD=0 AND SD.CVE_ESTATUS=0 "
						+ "AND SC.ID_VELATORIO="+Integer.parseInt(palabra)+" "
						+ "GROUP BY SD.ID_CAPILLA ORDER BY fechaEntrada DESC";
					log.info(query);
				request.getDatos().remove(""+AppConstantes.PALABRA+"");
				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				return request;
			}

			public DatosRequest insertarEntrada() {
				DatosRequest request = new DatosRequest();
				Map<String, Object> parametro = new HashMap<>();
				final QueryHelper q = new QueryHelper("INSERT INTO SVT_DISPONIBILIDAD_CAPILLAS ");
				q.agregarParametroValues(" ID_CAPILLA", "'" + this.idCapilla + "'");
				q.agregarParametroValues("FEC_ENTRADA", "'" + this.fechaEntrada + "'");
				q.agregarParametroValues("TIM_HORA_ENTRADA", "'" + this.horaEntrada + "'");
				q.agregarParametroValues("ID_ORDEN_SERVICIO", "" + this.idOrdenServicio + "");
				q.agregarParametroValues(""+AppConstantes.CVE_ESTATUS+"", "0");
				q.agregarParametroValues("ID_USUARIO_ALTA", "" + idUsuarioAlta +"");
				q.agregarParametroValues("FEC_ALTA", ""+AppConstantes.CURRENT_TIMESTAMP+"");
				String query = q.obtenerQueryInsertar() + " $$ " + cambiarEstatusCapilla(this.idCapilla, idUsuarioAlta) +" $$ " + cambiarEstatusOds(this.idOrdenServicio, idUsuarioAlta);
				log.info(query);
				parametro.put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				 parametro.put("separador","$$");
				request.setDatos(parametro);
				return request;
			}

			private String cambiarEstatusOds(Integer idOds, Integer idUsuarioModifica) {
				DatosRequest request = new DatosRequest();
		        Map<String, Object> parametro = new HashMap<>();
		        final QueryHelper q = new QueryHelper("UPDATE SVC_ORDEN_SERVICIO");
		        q.agregarParametroValues(""+AppConstantes.CVE_ESTATUS+"", "3");
		        q.agregarParametroValues("ID_USUARIO_MODIFICA ", "" + idUsuarioModifica +"");
				q.agregarParametroValues("FEC_ACTUALIZACION ", ""+AppConstantes.CURRENT_TIMESTAMP+"");
		        q.addWhere("ID_ORDEN_SERVICIO =" +idOds);
		        String query = q.obtenerQueryActualizar();
		        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		        parametro.put(AppConstantes.QUERY, encoded);
		        request.setDatos(parametro);
		        return query;
			}

			private String cambiarEstatusCapilla(Integer idCapilla, Integer idUsuarioModifica) {
				 DatosRequest request = new DatosRequest();
			        Map<String, Object> parametro = new HashMap<>();
			        final QueryHelper q = new QueryHelper("UPDATE SVC_CAPILLA");
			        q.agregarParametroValues("IND_DISPONIBILIDAD", "0");
			        q.agregarParametroValues(" ID_USUARIO_MODIFICA", "" + idUsuarioModifica +"");
					q.agregarParametroValues(" FEC_ACTUALIZACION", ""+AppConstantes.CURRENT_TIMESTAMP+"");
			        q.addWhere("ID_CAPILLA =" +idCapilla);
			        String query = q.obtenerQueryActualizar();
			        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			        parametro.put(AppConstantes.QUERY, encoded);
			        request.setDatos(parametro);
			        return query;
			}

			public DatosRequest insertarSalida() {
				DatosRequest request = new DatosRequest();
				Map<String, Object> parametro = new HashMap<>();
				final QueryHelper q = new QueryHelper("UPDATE SVT_DISPONIBILIDAD_CAPILLAS ");
				q.agregarParametroValues("FEC_SALIDA", "'" + this.fechaSalida + "'");
				q.agregarParametroValues("TIM_HORA_SALIDA", "'" + this.horaSalida + "'");
				q.agregarParametroValues("ID_USUARIO_MODIFICA", "" + idUsuarioModifica +"");
				q.agregarParametroValues(""+AppConstantes.CVE_ESTATUS+"", "1");
				q.agregarParametroValues("FEC_ACTUALIZACION", ""+AppConstantes.CURRENT_TIMESTAMP+"");
				  q.addWhere("ID_DISPONIBILIDAD =" +this.idDisponibilidad);
				String query = q.obtenerQueryActualizar()  + " $$ " + cambiarCapillaDisponible(this.idCapilla, idUsuarioModifica);
				log.info("estoy en " +query);
				parametro.put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				 parametro.put("separador","$$");
				request.setDatos(parametro);

				return request;
			}

			private String cambiarCapillaDisponible(Integer idCapilla, Integer idUsuarioModifica) {
				DatosRequest request = new DatosRequest();
				Map<String, Object> parametro = new HashMap<>();
				final QueryHelper q = new QueryHelper("UPDATE SVC_CAPILLA ");
				q.agregarParametroValues("IND_DISPONIBILIDAD", "1");
				q.agregarParametroValues("ID_USUARIO_MODIFICA", "" + idUsuarioModifica +"");
				q.agregarParametroValues("FEC_ACTUALIZACION", ""+AppConstantes.CURRENT_TIMESTAMP+"");
				  q.addWhere("ID_CAPILLA =" +idCapilla);
				String query = q.obtenerQueryActualizar();
				log.info("estoy en " +query);
				parametro.put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				request.setDatos(parametro);

				return query;
			}

			public DatosRequest detalleRegistro(DatosRequest request, Integer id) {
				String query = "SELECT "
						+ " CONCAT (PS.NOM_PERSONA, ' ', PS.NOM_PRIMER_APELLIDO, ' ', PS.NOM_SEGUNDO_APELLIDO) AS nombreContratante, "
						+ " OS.ID_ORDEN_SERVICIO AS idOds, OS.CVE_FOLIO AS folioOds, "
						+ " (SELECT CONCAT (SPN.NOM_PERSONA, ' ', SPN.NOM_PRIMER_APELLIDO, ' ', SPN.NOM_SEGUNDO_APELLIDO) "
						+ " FROM SVC_FINADO SF "
						+ " JOIN SVC_ORDEN_SERVICIO ON OS.ID_ORDEN_SERVICIO = SF.ID_ORDEN_SERVICIO "
						+ "JOIN SVC_PERSONA SPN ON SF.ID_PERSONA = SPN.ID_PERSONA LIMIT 1) AS finado, "
						+ " CAP.NOM_CAPILLA AS nomCapilla, "
						+ "TIME_FORMAT(SDC.TIM_HORA_ENTRADA, \"%H:%i\") AS registroEntrada, "
						+ "TIME_FORMAT(SDC.TIM_HORA_SALIDA, \"%H:%i\") AS registroSalida "
						+ "FROM SVC_ORDEN_SERVICIO OS "
						+ "JOIN SVC_CONTRATANTE SC ON OS.ID_CONTRATANTE = SC.ID_CONTRATANTE  "
						+ " JOIN SVC_PERSONA PS ON PS.ID_PERSONA = SC.ID_PERSONA  "
						+ " JOIN SVT_DISPONIBILIDAD_CAPILLAS SDC ON OS.ID_ORDEN_SERVICIO = SDC.ID_ORDEN_SERVICIO "
						+ " JOIN SVC_CAPILLA CAP ON CAP.ID_CAPILLA = SDC.ID_CAPILLA "
						+ " WHERE SDC.FEC_ENTRADA = '"+ fechaEntrada +"' "
						+ " AND CAP.ID_CAPILLA = "+id+" ";
			
				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				log.info("estoy en:" +query);
				return request;
			}

			public DatosRequest  catalogoVelatorio(DatosRequest request) {
				String query = "SELECT ID_VELATORIO AS id, NOM_VELATORIO AS velatorio "
						+ " FROM SVC_VELATORIO";
				log.info(query);
				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				return request;
			}

			public Map<String, Object> generarReporte(ReporteDto reporteDto) throws ParseException {
			
				String fechaCompleta= reporteDto.getMes() + "-"+ reporteDto.getAnio();
				Date dateF = new SimpleDateFormat("MMMM-yyyy").parse(fechaCompleta);
		        DateFormat anioMes = new SimpleDateFormat("yyyy-MM", new Locale("es", "MX"));
		        String fecha=anioMes.format(dateF);
		        log.info("estoy en:" +fecha);
				Map<String, Object> envioDatos = new HashMap<>();
				envioDatos.put("condition", " AND SDC.FEC_ENTRADA LIKE '%"+fecha+"%' AND SV.ID_VELATORIO = "+reporteDto.getIdVelatorio()+"");
				envioDatos.put("rutaNombreReporte", reporteDto.getRutaNombreReporte());
				envioDatos.put("tipoReporte", reporteDto.getTipoReporte());
				if(reporteDto.getTipoReporte().equals("xls")) {
					envioDatos.put("IS_IGNORE_PAGINATION", true);
				}
				return envioDatos;
			}

			public Map<String, Object> reporteEntregaCapillas(ReporteEntregaCapillaDto reporte) {
				Map<String, Object> envioDatos = new HashMap<>();
				envioDatos.put("rutaNombreReporte", reporte.getRutaNombreReporte());
				envioDatos.put("tipoReporte", reporte.getTipoReporte());
				envioDatos.put("velatorio", reporte.getVelatorio() +" " +reporte.getIdVelatorio());
				//envioDatos.put("folio", reporte.getFolio());
				envioDatos.put("noCapilla", reporte.getNoCapilla());
				envioDatos.put("nomContratante", reporte.getNomContratante());
				envioDatos.put("nomRepresentanteVelatorio", reporte.getNomRepresentanteVelatorio());
				return envioDatos;
			}

			
}
