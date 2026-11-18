# MySmartGrid

Proyecto académico que simula la gestión concurrente y distribuida de una red eléctrica inteligente. Permite tramitar consumos eléctricos por zonas, controlar baterías solares, eólicas y convencionales, calcular precios y consultar datos de monitorización.

**Asignatura:** Programación Concurrente y Distribuida  
**Grado:** Ingeniería Informática de Software — Universidad de Extremadura (UEx)

## Descripción

MySmartGrid simula una red formada por varias zonas energéticas. Cada consumo contiene una dirección, una zona y uno o varios tipos de demanda: solar, eólica o rápida. Los consumos se tramitan mediante hilos y mecanismos de sincronización y se almacenan o procesan desde ficheros binarios.

El proyecto incluye también servicios gRPC para:

- registrar consumos y obtener totales por zona y red;
- consultar consumos solares y consumos por dirección;
- calcular el precio de cada consumo a partir de sus demandas.

## Configuración

La configuración general se encuentra en `MySmartGrid-v0/src/main/java/main/Config.java`:

| Parámetro                 |    Valor actual | Descripción                                               |
| ------------------------- | --------------: | --------------------------------------------------------- |
| `NUM_ZONAS`               |               5 | Número de zonas de la red                                 |
| `NUM_OPERADORES_POR_ZONA` |               3 | Operadores de red por zona                                |
| `NUM_MAX_CONSUMOS`        |               3 | Máximo de consumos considerados en algunos controles      |
| `TIPO_LANZADO`            |               2 | Método de lanzamiento: 0 hilos, 1 observables, 2 executor |
| `TIPO_TODOS`              |               1 | Sincronización: 0 semáforo, 1 barrera cíclica             |
| `CAPACIDAD_BATERIA`       |         200 kWh | Capacidad máxima de las baterías                          |
| `NIVEL_INICIAL_BATERIA`   |         120 kWh | Nivel inicial de las baterías                             |
| `FICHERO_CONSUMOS`        | `consumos5.bin` | Fichero binario leído por la aplicación                   |
| `NUM_CONSUMOS_A_GENERAR`  |              50 | Número de consumos creados por el generador               |
| `SEED`                    |           12345 | Semilla para generación reproducible                      |

Las ventanas se pueden ajustar mediante `TAMAÑO_VENTANA`.

## Organización del código

```text
MySmartGrid-v0/
├── consumos2.bin
├── consumos5.bin
├── pom.xml
└── src/main/
    ├── java/
    │   ├── client/
    │   ├── concurrency/
    │   ├── energy/
    │   ├── grpc/
    │   ├── main/
    │   ├── operators/
    │   ├── pcd/util/
    │   ├── server/
    │   ├── service/
    │   └── storage/
    └── resources/
        ├── Monitorizacion.proto
        └── Precio.proto
```

## Personalización

Para probar distintas formas de lanzamiento y sincronización, modificar los valores de `TIPO_LANZADO` y `TIPO_TODOS` en `Config.java`:

- `TIPO_LANZADO = 0`: un hilo por consumo.
- `TIPO_LANZADO = 1`: procesamiento reactivo con observables RxJava.
- `TIPO_LANZADO = 2`: pool fijo de ejecuciones.
- `TIPO_TODOS = 0`: semáforo.
- `TIPO_TODOS = 1`: barrera cíclica.
