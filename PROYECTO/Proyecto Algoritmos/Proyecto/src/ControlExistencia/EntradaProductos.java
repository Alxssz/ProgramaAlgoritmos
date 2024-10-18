package ControlExistencia;

import static GestiondeProductos.Productos.BajaProductos.productosExistentes;
import static GestiondeProductos.Productos.ModificacionAltaProducto.productoExiste;
import Menus.MenuExistencias;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class EntradaProductos {
    // Rutas de archivos
    private static File archivoAltaProductos = new File("datos\\AltaProductos.txt");
    private static File archivoMovimientos = new File("datos\\MovimientosStock.txt"); 

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String id;
        int cantidadIngresada = 0;

        // Encabezado
        System.out.println("     ENTRADA PRODUCTO    ");
        System.out.println("_________________________");

        productosExistentes();

        // Pedir el ID del producto
        do {
            System.out.println("- Ingrese el ID del producto:");
            id = scan.nextLine().trim();
            if (id.isEmpty()) {
                System.out.println("El ID del producto no puede estar vacío.");
            }
        } while (id.isEmpty());

        // Verificar si el producto existe
        if (!productoExiste(id)) {
            System.out.println("El ID (" + id + ") no está registrado.");
            return; // Termina el programa si el producto no existe
        }

        // Elegir la cantidad a ingresar
        do {
            System.out.println("- Ingrese la cantidad de producto a ingresar:");
            while (!scan.hasNextInt()) {
                System.out.println("Por favor, ingrese un numero entero positivo.");
                scan.next(); // limpia entrada incorrecta
            }
            cantidadIngresada = scan.nextInt();
            // Verificar si la cantidad es negativa
            if (cantidadIngresada < 0) {
                System.out.println("La cantidad no puede ser negativa.");
            }
        } while (cantidadIngresada < 0);

        // metodo de guardado
        guardarProducto(id, cantidadIngresada);
        guardarMovimiento(id, cantidadIngresada);

        System.out.println("La entrada de producto se ha realizado correctamente");
        System.out.println("");
        //nos regresa al menu
        MenuExistencias.main(new String[0]);
    }

    // metodo de guardar el producto
    public static void guardarProducto(String id, int cantidadIngresada) {
        try {
            FileReader fr = new FileReader(archivoAltaProductos);
            BufferedReader br = new BufferedReader(fr);

            // Crear copia del archivo
            File archivoCopia = new File("copia_AltaProductos.txt");
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);
            String linea;

            // Copiar contenido existente
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                // Si la posición 0 tiene el ID
                if (datos[0].compareTo(id) == 0) {
                    // escribir la nueva cantidad ingresada
                    int stockActual = Integer.parseInt(datos[6]) + cantidadIngresada; // Aumentar el stock
                    linea = datos[0] + "|" + datos[1] + "|" + datos[2] + "|" + datos[3]
                            + "|" + datos[4] + "|" + datos[5]+ "|" +stockActual+ "|"+ datos[7];
                }
                bw.write(linea + "\n");
            }
            bw.close();
            br.close();

            // Reemplazar el archivo original con la copia
            Files.move(archivoCopia.toPath(),archivoAltaProductos.toPath(), REPLACE_EXISTING);
        } catch (IOException ex) {
        }
    }

    // metodo de guardar movimiento de entrada en el archivo
    public static void guardarMovimiento(String id, int cantidadIngresada) {
        //crea archivo para leer
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoMovimientos, true))) {
            // decclarar la fecha y hora actual y operacion
            String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String operacion = "Entrada"; // operacion
           //se escriben las lineas
            String movimiento = id + "|" + fecha + "|" + hora + "|" + operacion + "|" + cantidadIngresada + "|usuario";

            bw.write(movimiento + "\n");
        } catch (IOException ex) {
            System.out.println("Error al guardar el movimiento de entrada.");
        }
    }
}