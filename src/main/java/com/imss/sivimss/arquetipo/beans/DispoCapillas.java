package com.imss.sivimss.arquetipo.beans;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.arquetipo.model.request.BuscarDispoCapillasRequest;
import com.imss.sivimss.arquetipo.model.request.DispoCapillasRequest;
import com.imss.sivimss.arquetipo.util.AppConstantes;
import com.imss.sivimss.arquetipo.util.DatosRequest;

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
						+ " FROM svc_capilla SC "
						+ " JOIN svt_disponibilidad_capillas SD ON SD.ID_CAPILLA = SC.ID_CAPILLA "
						+ " JOIN svc_velatorio SV ON SV.ID_VELATORIO = SC.ID_VELATORIO "
						+ " WHERE SC.CVE_ESTATUS=1 AND SD.FEC_ENTRADA LIKE '%"+ fechaEntrada +"%' "
						+ " AND SV.NOM_VELATORIO = '"+buscar.getVelatorio()+"'";
				log.info(query);
				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes()));
				return request;
			}
}
