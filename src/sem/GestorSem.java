package sem;

import java.time.LocalTime;

import sem_estacionamiento.EstacionamientoCompraApp;
import sem_estacionamiento.EstacionamientoCompraPuntual;

public class GestorSem {

	private LocalTime inicioDeJornada;
	private LocalTime finDeJornada;
	private double costoPorHora;
	private ISemEstacionamiento semEstacionamiento;
	
	public GestorSem(ISemEstacionamiento semEstacionamiento) {
		this.costoPorHora = 40;
		this.inicioDeJornada = LocalTime.of(07, 00);
		this.finDeJornada = LocalTime.of(20, 00);
		this.semEstacionamiento = semEstacionamiento;
	}

	public Boolean tieneEstacionamientoVigente() {
		return false;
	}
	
	public Boolean estaDentroDeUnaZonaConLaCoordenada(int coordenada) {
		return true;
	}
	
	public Boolean estaEnElMismoPuntoGeograficoDeInicioEstcaiomiento(int coordenada) {
		return false;
	}
	
	/**
	 * @param patente: String 
	 * @param saldoDisponible: Double
	 * @return Un String que representa al msg describiendo los datos del inicio del estcionamiento
	 * */
	
	public String iniciarEstacionamiento(String patente, double saldoDisponible,int nroCelular) {
		LocalTime horaMaxima;
		if(saldoDisponible > 0) {
			horaMaxima = this.calcularTiempoMaximo(saldoDisponible, LocalTime.now());
			this.getSemEstacionamiento().registrarEstacionamiento(new EstacionamientoCompraApp(patente, nroCelular, horaMaxima));
			
			//FALTA IMPLEMENTAR EL MSG DETALLANDO CADA OPERACION DEL INICIO DEL ESTACIONAMIENTO
			return "";
		}
		else {
			return "Saldo insuficiente para realizar el Inicio del estacionamiento";
		}
	}
	
	/**
	 * @param saldoDisponible: double 
	 * @param horaActual: LocalTime
	 * @return Un LocalTime indicando la hora final del estacionamiento, caso contrario sino esta
	 * en la franja horaria la hora final de la jornada.
	 * */
	
	private LocalTime calcularTiempoMaximo(double saldoDisponible, LocalTime horaActual) {
		long minutosDisponibles = (long)this.cantidadDeMinutosDisponibles(saldoDisponible);
		LocalTime result = horaActual.plusMinutes(minutosDisponibles);
		return this.estaDentroDeFranjaHoraria(result) ? result : this.getInicioDeJornada();
	}
	
	private double cantidadDeMinutosDisponibles(double saldo) {
		// TODO Auto-generated method stub
		return saldo / this.precioPorMinuto();
	}

	private double precioPorMinuto() {
		// TODO Auto-generated method stub
		return this.getCostoPorHora() / 60;
	}
	

	/**
	 * @param patente: Un String
	 * @param cantidadDeHoras: Un Integer
	 * */

	public void generarEstacionamientoPuntual(String patente, int cantidadDeHoras) {
		LocalTime horaFinal = LocalTime.now().plusHours(cantidadDeHoras);
		if(this.estaDentroDeFranjaHoraria(horaFinal)) {
			semEstacionamiento.registrarEstacionamiento(new EstacionamientoCompraPuntual(patente,cantidadDeHoras, horaFinal));	
		}
		else {
			horaFinal =  this.getFinDeJornada();
			semEstacionamiento.registrarEstacionamiento(new EstacionamientoCompraPuntual(patente,cantidadDeHoras, horaFinal));
		}
	}
	
	private boolean estaDentroDeFranjaHoraria(LocalTime horaMaxima) {
		// TODO Auto-generated method stub
		return this.getInicioDeJornada().isBefore(horaMaxima) && this.getFinDeJornada().isAfter(horaMaxima);
	}

	public Boolean seEncuentraEnZonaElUsuario(int coordenada) {
		return true;
	}
	
	public String finalizarEstacionamiento() {
		return "";
	}
	
	public LocalTime getInicioDeJornada() {
		return inicioDeJornada;
	}

	public LocalTime getFinDeJornada() {
		return finDeJornada;
	}

	public double getCostoPorHora() {
		return costoPorHora;
	}

	public ISemEstacionamiento getSemEstacionamiento() {
		return semEstacionamiento;
	}
}
