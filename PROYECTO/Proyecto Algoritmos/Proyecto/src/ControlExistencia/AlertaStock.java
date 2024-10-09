package ControlExistencia;

import Menus.MenuExistencias;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlertaStock {

    // Ruta de archivo
    private static File archivoAltaProductos = new File("datos\\AltaProductos.txt");

    public static void main(String[] args) {
        // ejecutor programado para que se ejecute consecutivamente 
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Programar la verificación del stock cada 15 segundos 
        scheduler.scheduleAtFixedRate(() -> verificarStockProductos(), 0, 15, TimeUnit.SECONDS);
    }

    // Verificar el stock de productos
    public static void verificarStockProductos() {
        boolean alertaDetectada = false;// si hay alertas

        try (BufferedReader br = new BufferedReader(new FileReader(archivoAltaProductos))) {
            String linea;
            Scanner scan = new Scanner(System.in);
            System.out.println("     VERIFICACION DE STOCK    ");
            System.out.println("______________________________");

            while ((linea = br.readLine()) != null) {
                // separar los datos
                String[] datos = linea.split("\\|");
                String id = datos[0];
                String nombre = datos[1]; 
                int stock = Integer.parseInt(datos[6]); // Stock actual
                int puntoReorden = 10; // Punto de reorden 10  

                // si el stock es menor o igual al punto de reorden
                if (stock <= puntoReorden) {
                    mostrarAlerta(nombre, stock, puntoReorden);
                    alertaDetectada = true; // Se detectó una alerta
                }
            }

            // Si no alerta
            if (!alertaDetectada) {
                System.out.println("No hay alertas de stock bajo.");
                System.out.println("Presione Enter para salir.");
                scan.nextLine(); 
                MenuExistencias.main(new String [0]);
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo de productos.");
        }
    }

    // Método para mostrar la alerta de stock bajo
    public static void mostrarAlerta(String nombreProducto, int stockActual, int puntoReorden) {
        System.out.println("ALERTA DE STOCK BAJO");
        System.out.println("Producto: " + nombreProducto);
        System.out.println("Stock actual: " + stockActual);
        System.out.println("Punto de reorden: " + puntoReorden);
        System.out.println("------------------------------");
    }
}
