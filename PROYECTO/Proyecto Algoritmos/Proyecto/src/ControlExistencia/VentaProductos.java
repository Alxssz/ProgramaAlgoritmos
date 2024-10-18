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

public class VentaProductos {
    // Rutas de archivos
    private static File archivoAltaProductos = new File("datos\\AltaProductos.txt");
    private static File archivoRegistroVentas = new File("datos\\RegistroVentas.txt");
    private static File archivoMovimientos = new File("datos\\MovimientosStock.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String id;
        int cantidadIngresada = 0; // Inicializar la variable

        // Encabezado
        System.out.println("     VENTA PRODUCTO    ");
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

        // tener la cantidad disponible en stock
        int cantidadStock = obtenerCantidadStock(id);

        //la cantidad a ingresar
        do {
            System.out.println("- Ingrese la cantidad de producto a vender:");
            cantidadIngresada = scan.nextInt();
            // si la cantidad es negativa o mayor al stock
            if (cantidadIngresada < 0) {
                System.out.println("La cantidad no puede ser negativa.");
            } else if (cantidadIngresada > cantidadStock) {
                System.out.println("No hay suficiente stock para realizar la venta. Stock disponible: " + cantidadStock);
            }
        } while (cantidadIngresada < 0 || cantidadIngresada > cantidadStock);

        
        //guarda en archivos de texto
        guardarProducto(id, cantidadIngresada);
        registrarVenta(id, cantidadIngresada);
        guardarMovimiento(id,cantidadIngresada);
        System.out.println("La venta se ha realizado correctamente");
        System.out.println("");
        //nos regresa al menu
        MenuExistencias.main(new String[0]);
    }

    // Obtener la cantidad de stock disponible
    public static int obtenerCantidadStock(String id) {
        int cantidad = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivoAltaProductos))) {
            String linea;
            //leer lineas
            while ((linea = br.readLine()) != null) {
                //separar en cadenas
                String[] datos = linea.split("\\|");
                //si la posicion 0 tiene el ID
                if (datos[0].compareTo(id) == 0) {
                    cantidad = Integer.parseInt(datos[6]); // convertir a numero la posición del stock
                    break;
                }
            }
        } catch (IOException ex) {
        }
        return cantidad; // retornar cantidad
    }

    // Guardar el producto en el archivo
    public static void guardarProducto(String id, int cantidadIngresada) {
        try {
            FileReader fr = new FileReader(archivoAltaProductos);
            BufferedReader br = new BufferedReader(fr);

            // Crear copia del archivo
            File archivoCopia = new File("copia_Productos.txt");
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);
            String linea;

            // Copiar contenido existente
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                // Si la posición 0 tiene el ID
                if (datos[0].compareTo(id) == 0) {
                    // Actualizar la línea 
                    int cantidadActual = Integer.parseInt(datos[6]); //convertir en numero el stock
                    // Restar la cantidad vendida
                    cantidadActual -= cantidadIngresada; 
                    linea = datos[0] + "|" + datos[1] + "|" + datos[2] + "|" + datos[3]
                            + "|" + datos[4] + "|" + datos[5]+ "|"+ cantidadActual+ "|" + datos[7]; // Actualiza la línea
                }
                bw.write(linea + "\n"); //salto de linea entre lineas
            }
            bw.close();
            br.close();

            // Reemplazar el archivo original con la copia
            Files.move(archivoCopia.toPath(), archivoAltaProductos.toPath(), REPLACE_EXISTING);
        } catch (IOException ex) {
        }
    }

    // Registrar la venta en un archivo
    public static void registrarVenta(String id, int cantidadIngresada) {
        //crea nuevo archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoRegistroVentas, true))) {
            // declara la variable fecha a formto fecha
            String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            // declara la variable hora en formato hora
            String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
            //escribe en el archivo los datos 
            bw.write("Fecha: " + fecha + ", Hora: " + hora + ", Producto ID: " + id + ", Cantidad: " + cantidadIngresada + "\n");
        } catch (IOException ex) {
        }
    }


    // Guardar movimiento de salida en el archivo
    public static void guardarMovimiento(String id, int cantidadIngresada) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoMovimientos, true))) {
            // Obtener la fecha y hora actual
            String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String operacion = "Salida"; // operacion
           // String Usuario declarar
           //se escribe lineas
            String movimiento = id + "|" + fecha + "|" + hora + "|" + operacion + " |" + cantidadIngresada + "|usuario"; 

            bw.write(movimiento + "\n");
        } catch (IOException ex) {
            System.out.println("Error al guardar el movimiento de entrada.");
        }
    }
}

