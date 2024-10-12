package InformesEstasiticas;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Estadisticas {

    // Archivos
    private static File archivoInventarioActual = new File("datos\\AltaProductos.txt");
    private static File archivoCompras = new File("datos\\Pedidos.txt");
    private static File archivoVentas = new File("datos\\RegistroVentas.txt");
    private static File archivoMovimientosStock = new File("datos\\MovimientosStock.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int opcion;

        System.out.println("             ESTADISTICAS            ");
        System.out.println("_____________________________________");
        System.out.println("1. Estadisticas del inventario actual");
        System.out.println("2. Estadisticas de ventas");
        System.out.println("3. Estadisticas de compras");
        System.out.println("4. Movimientos de stock");
        System.out.println("5. Salir");
        System.out.println("-------------------------------------");

        System.out.print("Ingrese una opcion: ");
        opcion = scan.nextInt();

        while (opcion < 1 || opcion > 5) {
            System.out.println("Opcion no valida, por favor ingrese una opcion valida:");
            opcion = scan.nextInt();
            scan.nextLine(); // Consumir el salto de línea pendiente
        }

        switch (opcion) {
            case 1:
                mostrarEstadisticasInventario();
                break;
            case 2:
                estadisticasVenta();
                break;
            case 3:
                mostrarEstadisticasCompras();
                break;
            case 4:
                movimientosStock();
                break;
            case 5:
                System.out.println("Saliendo...");
                break;
        }
    }

    public static void mostrarEstadisticasInventario() {
        System.out.println("Porcentajes de Productos en el Inventario:");

        int totalCantidad = 0; // Suma total de todas las cantidades
        Map<String, Integer> inventario = new HashMap<>(); // Mapa para almacenar el nombre del producto y su cantidad

        // Leer el archivo de inventario y acumular las cantidades
        try (BufferedReader br = new BufferedReader(new FileReader(archivoInventarioActual))) {
            String linea;

            // Leer cada línea del archivo de inventario
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|"); // Separar por "|"

                if (datos.length == 7) { // Verificar que hay suficientes datos
                    String nombreProducto = datos[1]; // Nombre del producto
                    int cantidad = Integer.parseInt(datos[5]); // Cantidad en inventario

                    // Si el producto ya existe en el mapa, sumar la cantidad; de lo contrario, agregarlo
                    inventario.put(nombreProducto, inventario.getOrDefault(nombreProducto, 0) + cantidad);

                    // Acumular la cantidad total
                    totalCantidad += cantidad;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de inventario: " + e.getMessage());
            return;
        }

        // Calcular y mostrar el porcentaje para cada producto
        for (Map.Entry<String, Integer> entry : inventario.entrySet()) {
            String nombreProducto = entry.getKey();
            int cantidad = entry.getValue();
            double porcentaje = (double) cantidad / totalCantidad * 100; // Calcular el porcentaje
            System.out.printf("Producto: %s | Cantidad: %d | Porcentaje: %.2f%%\n", nombreProducto, cantidad, porcentaje);
        }
    }

    public static void estadisticasVenta() {
        System.out.println("Estadísticas de Ventas:");

        int totalVentas = 0;  // Contador total de ventas
        Map<Integer, Integer> ventasPorProducto = new HashMap<>(); // Almacena las ventas por ID de producto

        try (BufferedReader br = new BufferedReader(new FileReader(archivoVentas))) {
            String linea;

            // Leer cada línea del archivo de ventas
            while ((linea = br.readLine()) != null) {
                // Suponiendo que el formato es: Fecha|Hora|Producto ID|Cantidad
                String[] datos = linea.split(", ");  // Separar por ", "

                if (datos.length == 4) {
                    int idProducto = Integer.parseInt(datos[2].split(": ")[1]);  // ID del producto
                    int cantidad = Integer.parseInt(datos[3].split(": ")[1]);     // Cantidad vendida

                    // acumula total de ventas
                    totalVentas += cantidad;

                    // acummula venta por producto
                    ventasPorProducto.put(idProducto, ventasPorProducto.getOrDefault(idProducto, 0) + cantidad);
                }
            }

            // Mostrar resultados
            if (totalVentas > 0) {
                System.out.printf("Total de ventas: %d\n", totalVentas);
                System.out.println("Porcentaje de ventas por producto:");

                for (Map.Entry<Integer, Integer> entry : ventasPorProducto.entrySet()) {
                    int idProducto = entry.getKey();
                    int cantidadVendida = entry.getValue();
                    double porcentaje = (double) cantidadVendida / totalVentas * 100;

                    System.out.printf("Producto ID: %d, Cantidad vendida: %d, Porcentaje de ventas: %.2f%%\n", idProducto, cantidadVendida, porcentaje);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de ventas: " + e.getMessage());
        }
    }

    public static void mostrarEstadisticasCompras() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCompras))) {
            String linea;
            int contadorCompletado = 0;
            int contadorCurso = 0;
            int contadorPendiente = 0;
            int totalPedidos = 0;  // Contador total de pedidos

            // Leer cada línea del archivo
            while ((linea = br.readLine()) != null) {
                // Verificar si la línea empieza con "Estado"
                if (linea.startsWith("Estado:")) {
                    totalPedidos++;  // Contar cada pedido

                    // Separar el estado del resto del texto
                    String estado = linea.split(":")[1].trim().toLowerCase();  // Convertir a minúsculas para comparar fácilmente

                    // Incrementar los contadores según el estado
                    if (estado.equals("completado")) {
                        contadorCompletado++;
                    } else if (estado.equals("en curso")) {
                        contadorCurso++;
                    } else if (estado.equals("pendiente")) {
                        contadorPendiente++;
                    }
                }
            }

            // Si hay pedidos, calcular los porcentajes
            if (totalPedidos > 0) {
                double porcentajeCompletado = (double) contadorCompletado / totalPedidos * 100;
                double porcentajeCurso = (double) contadorCurso / totalPedidos * 100;
                double porcentajePendiente = (double) contadorPendiente / totalPedidos * 100;

                // Mostrar los resultados
                System.out.printf("Total de Pedidos: %d\n", totalPedidos);
                System.out.printf("Completado: %.2f%%\n", porcentajeCompletado);
                System.out.printf("En curso: %.2f%%\n", porcentajeCurso);
                System.out.printf("Pendiente: %.2f%%\n", porcentajePendiente);
            }       
        } catch (IOException ex) {
            System.out.println("Error al leer el archivo de movimientos de stock: " + ex.getMessage());
        }
    }

    // Método para calcular y mostrar el porcentaje de entradas y salidas
    public static void movimientosStock() {
        System.out.println("Porcentaje de Entradas y Salidas:");

        // Variables para acumular cantidades de entradas y salidas
        int totalEntradas = 0;
        int totalSalidas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(archivoMovimientosStock))) {
            String linea;

            // Leer cada línea del archivo
            while ((linea = br.readLine()) != null) {
                // Separar los campos por el delimitador "|"
                String[] datos = linea.split("\\|");

                if (datos.length == 6) {
                    // Usar trim() para eliminar posibles espacios en blanco
                    String tipoMovimiento = datos[3].trim();
                    int cantidad = Integer.parseInt(datos[4]);

                    // Acumular entradas y salidas
                    if (tipoMovimiento.equals("Entrada")) {
                        totalEntradas += cantidad;
                    } else if (tipoMovimiento.equals("Salida")) {
                        totalSalidas += cantidad;
                    }
                }
            }

            // Calcular el total de movimientos
            int totalMovimientos = totalEntradas + totalSalidas;

            if (totalMovimientos > 0) {
                // Calcular porcentajes
                double porcentajeEntradas = (double) totalEntradas / totalMovimientos * 100;
                double porcentajeSalidas = (double) totalSalidas / totalMovimientos * 100;

                // Mostrar resultados
                System.out.printf("Total de movimientos: %d\n", totalMovimientos);
                System.out.printf("Entradas de productos (%.2f%%) \n", porcentajeEntradas);
                System.out.printf("Salidas de productos(%.2f%%)\n", porcentajeSalidas);

            } else {
                System.out.println("No hay movimientos registrados.");
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo de movimientos de stock: " + e.getMessage());
        }
    }
}
