package com.imss.sivimss.arquetipo.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.springframework.http.HttpStatus;

import com.imss.sivimss.arquetipo.exception.BadRequestException;
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

			public DatosRequest registrosPorMes(DatosRequest request, BuscarDispoCapillasRequest buscar) {
				String query = "SELECT "
						+ "SC.ID_CAPILLA AS idCapilla,"
						+ "DATE_FORMAT(SD.FEC_ENTRADA, \"%d-%m-%Y\") AS fechaEntrada,"
						+ " TIME_FORMAT(SD.TIM_HORA_ENTRADA, \"%H:%i\") AS hrEntrada, "
						+ "DATE_FORMAT(SD.FEC_SALIDA, \"%d-%m-%Y\") AS fechaSalida, "
						+ "TIME_FORMAT(SD.TIM_HORA_SALIDA, \"%H:%i\") AS hrSalida, "
						+ "SC.NOM_CAPILLA, SV.NOM_VELATORIO"
						+ " FROM SVC_CAPILLA SC "
						+ " JOIN SVT_DISPONIBILIDAD_CAPILLAS SD ON SD.ID_CAPILLA = SC.ID_CAPILLA "
						+ " JOIN SVC_VELATORIO SV ON SV.ID_VELATORIO = SC.ID_VELATORIO "
						+ " WHERE SC.CVE_ESTATUS=1 AND SD.FEC_ENTRADA LIKE '%"+ fechaEntrada +"%' "
						+ " AND SV.NOM_VELATORIO = '"+buscar.getVelatorio()+"'";
				log.info(query);
				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				return request;
			}

			public DatosRequest capillasDisponibles(DatosRequest request, BuscarDispoCapillasRequest buscar) {
				String query = "SELECT SC.ID_CAPILLA AS id, SC.NOM_CAPILLA AS nomCapilla,"
						+ " SC.IND_DISPONIBILIDAD AS disponibilidad "
						+ "FROM SVC_CAPILLA SC "
						+ "JOIN SVC_VELATORIO SV ON SV.ID_VELATORIO = SC.ID_VELATORIO "
						+ " WHERE SC.CVE_ESTATUS=1 AND SC.IND_DISPONIBILIDAD=1 AND SV.NOM_VELATORIO='"+ buscar.getVelatorio() +"'"
								+ " GROUP BY SC.ID_CAPILLA";
					log.info(query);
				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				return request;
			}

			public DatosRequest buscarOrdenServicio(DatosRequest request) {
				String palabra = request.getDatos().get("palabra").toString();
				String query = "SELECT CONCAT (PS.NOM_PERSONA, ' ', PS.NOM_PRIMER_APELLIDO, ' ', PS.NOM_SEGUNDO_APELLIDO) AS nombreContratante, "
						+ "OS.ID_ORDEN_SERVICIO AS id, "
						+ "(SELECT CONCAT (SPN.NOM_PERSONA, ' ', SPN.NOM_PRIMER_APELLIDO, ' ', SPN.NOM_SEGUNDO_APELLIDO)  "
						+ " FROM SVC_FINADO SF "
						+ "JOIN SVC_PERSONA SPN ON SF.ID_PERSONA = SPN.ID_PERSONA) AS finado "
						+ "FROM SVC_ORDEN_SERVICIO OS "
						+ " JOIN SVC_CONTRATANTE SC ON OS.ID_CONTRATANTE = SC.ID_CONTRATANTE "
						+ "JOIN SVC_PERSONA PS ON PS.ID_PERSONA = SC.ID_PERSONA "
						+ " WHERE OS.CVE_ESTATUS = 2 OR OS.CVE_ESTATUS = 3 AND OS.CVE_FOLIO ="+ Integer.parseInt(palabra) +"";
					log.info(query);
				request.getDatos().remove("palabra");
				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				return request;
			}

			public DatosRequest capillasOcupadas(DatosRequest request) {
				String palabra = request.getDatos().get("palabra").toString();
				String query = "SELECT SC.ID_CAPILLA AS idCapilla, SC.NOM_CAPILLA AS nomCapilla, "
						+ "DATE_FORMAT(SD.FEC_ENTRADA, \"%d-%m-%Y\") AS fechaEntrada, "
						+ " TIME_FORMAT(SD.TIM_HORA_ENTRADA, \"%H:%i\") AS hrEntrada,"
						+ " SC.IND_DISPONIBILIDAD AS disponibilidad "
						+ "FROM SVC_CAPILLA SC "
						+ "JOIN SVC_VELATORIO SV ON SV.ID_VELATORIO = SC.ID_VELATORIO "
						+ " JOIN SVT_DISPONIBILIDAD_CAPILLAS SD ON SD.ID_CAPILLA = SC.ID_CAPILLA "
						+ " WHERE SC.CVE_ESTATUS=1 AND SC.IND_DISPONIBILIDAD=0 AND SV.NOM_VELATORIO='"+ palabra +"'"
								+ " GROUP BY SC.ID_CAPILLA";
					log.info(query);
				request.getDatos().remove("palabra");
				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				return request;
			}

			public DatosRequest insertar() {
				DatosRequest request = new DatosRequest();
				Map<String, Object> parametro = new HashMap<>();
				final QueryHelper q = new QueryHelper("INSERT INTO SVT_DISPONIBILIDAD_CAPILLAS ");
				q.agregarParametroValues(" ID_CAPILLA", "'" + this.idCapilla + "'");
				q.agregarParametroValues("FEC_ENTRADA", "'" + this.fechaEntrada + "'");
				q.agregarParametroValues("TIM_HORA_ENTRADA", "'" + this.horaEntrada + "'");
				q.agregarParametroValues("ID_ORDEN_SERVICIO", "" + this.idOrdenServicio + "");
				q.agregarParametroValues("CVE_ESTATUS", "1");
				q.agregarParametroValues("ID_USUARIO_ALTA", "" + idUsuarioAlta +"");
				q.agregarParametroValues("FEC_ALTA", " CURRENT_TIMESTAMP() ");
				String query = q.obtenerQueryInsertar() + " $$ " + cambiarEstatusCapilla(this.idCapilla) +" $$ " + cambiarEstatusOds(this.idOrdenServicio);
				log.info("estoy en " +query);
				parametro.put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				 parametro.put("separador","$$");
				request.setDatos(parametro);
				return request;
			}

			private String cambiarEstatusOds(Integer idOds) {
				DatosRequest request = new DatosRequest();
		        Map<String, Object> parametro = new HashMap<>();
		        final QueryHelper q = new QueryHelper("UPDATE SVC_ORDEN_SERVICIO");
		        q.agregarParametroValues("CVE_ESTATUS", "3");
		        q.addWhere("ID_ORDEN_SERVICIO =" +idOds);
		        String query = q.obtenerQueryActualizar();
		        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		        parametro.put(AppConstantes.QUERY, encoded);
		        request.setDatos(parametro);
		        return query;
			}

			private String cambiarEstatusCapilla(Integer idCapilla) {
				 DatosRequest request = new DatosRequest();
			        Map<String, Object> parametro = new HashMap<>();
			        final QueryHelper q = new QueryHelper("UPDATE SVC_CAPILLA");
			        q.agregarParametroValues("IND_DISPONIBILIDAD", "0");
			        q.addWhere("ID_CAPILLA =" +idCapilla);
			        String query = q.obtenerQueryActualizar();
			        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			        parametro.put(AppConstantes.QUERY, encoded);
			        request.setDatos(parametro);
			        return query;
			}

			
}
