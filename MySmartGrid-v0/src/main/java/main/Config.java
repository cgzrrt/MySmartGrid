package main;

public final class Config {
    private Config() {}

    // --- Configuracion general ---
    public static final int NUM_OPERADORES_POR_ZONA = 3; // hilos que se crean
    public static final int NUM_ZONAS = 5;
    public static final int NUM_MAX_CONSUMOS = 3; // ---- VERSION 6 ----
    public static final int TIPO_LANZADO = 2; // 0: normal 1: observables 2: executor
    
    // --- Ventanas ---
    public static final int TAMAÑO_VENTANA = 250;

    // Baterias (kWh)
    public static final double CAPACIDAD_BATERIA = 200.0;
    public static final double NIVEL_INICIAL_BATERIA = 120.0;

    // Ficheros
    public static final String FICHERO_CONSUMOS = "consumos5.bin";

    // Generacion de consumos
    public static final int NUM_CONSUMOS_A_GENERAR = 50;

    // Semilla para generacion reproducible
    public static final long SEED = 12345L;
}
