package energy;

public class ConsumoAnotado {
	// ---- VERSIÓN 9: Clase creada para anotar los consumos ----
	
	private String idConsumo, direccion;
	private int idZona;
	private double kWh;
	private boolean solar;
	
	public ConsumoAnotado(String _idConsumo, int _idZona, double _kWh, String _direccion, boolean _solar) {
		idConsumo = _idConsumo;
		idZona = _idZona;
		kWh = _kWh;
		direccion = _direccion;
		solar = _solar;
	}
	
	public String getIdConsumo() {
		return idConsumo;
	}
	public int getIdZona() {
		return idZona;
	}
	public double getKWH() {
		return kWh;
	}
	public String getDireccion() {
		return direccion;
	}
	public boolean getSolar() {
		return solar;
	}
}
