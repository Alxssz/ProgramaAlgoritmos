package ControlExistencia;

import static GestiondeProductos.Productos.BajaProductos.productosExistentes;
import static GestiondeProductos.Productos.ModificacionAltaProducto.productoExiste;
import Menus.MenuExistencias;
import java.io.*;
import java.util.Scanner;

public class HistorialMovimientos {

    // Ruta de archivo
    private static File archivoMovimientos = new File("datos\\MovimientosStock.txt");
    private static File archivoAltaProductos = new File("datos\\AltaProductos.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String idProducto;

        // Encabezado
        System.out.println("");
        System.out.println("      MOVIMIENTO DE STOCK    ");
        System.out.println("_____________________________");
        System.out.println("");
        // Mostrar lista de productos
        productosExistentes();

        // Pedir ID del producto
        System.out.println("- Ingrese el ID del producto para ver su historial:");
        idProducto = scan.nextLine().trim();

        // ver si el producto existe
        if (!productoExiste(idProducto)) {
            System.out.println("El ID (" + idProducto + ") no esta registrado.");
            return;
        }

        // Leer y mostrar el historial de movimientos
        try (BufferedReader br = new BufferedReader(new FileReader(archivoMovimientos))) {
            String linea;
            System.out.println("           HISTORIAL DE MOVIMIENTOS DE STOCK            ");
            System.out.println("________________________________________________________");
            System.out.println("Fecha       | Hora      | Operacion | Cantidad | Usuario");

            // Solo mostrar movimientos del producto seleccionado
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos[0].equals(idProducto)) {
                    // Mostrar los datos
                    System.out.println (datos[1] + "  | " + datos[2] + "  2| "+ datos[3]+"   | " + datos[4] +"       | " + datos[5]);
                }
            }
            
            //volver al menu
        System.out.println("________________________________________________________");
        System.out.println("Enter para regresar ");
        scan.nextLine();
        MenuExistencias.main(new String[0]);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de movimientos.");
        }
        
    }  
}