
package GestiondeProductos.Especificaciones;

import GestiondeProductos.CategoriaProducto.MenuCategoria;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EliminarEspecificacion {

    // Ruta del archivos
    private static File archivoEspecificaciones = new File("datos\\Especificaciones.txt");
    private static File archivoProductos = new File("datos\\Productos.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Encabezado
        System.out.println("");
        System.out.println("   ELIMINAR ESPECIFICACION  ");
        System.out.println("____________________________");
        System.out.println("");

        // Muestra las especificaciones existentes
        especificacionesExistentes();

        while (true) {
            // Solicitar el nombre de la especificacion a eliminar
            System.out.println("-Ingrese el nombre de la especificacion a eliminar:");
            String nombreEspecificacion = scan.nextLine().trim();

            //que la entrada no vacia
            if (nombreEspecificacion.isEmpty()) {
                System.out.println("El nombre de la especificacion no puede estar vacío.");
                continue; // Volver a solicitar
            }

            // que la especificacion existe
            if (!especificacionExiste(nombreEspecificacion)) {
                System.out.println("La especificacion (" + nombreEspecificacion + ") no existe.");
                continue; // Volver a solicitar
            }

            // Confirmar la eliminacion
            System.out.println("Esta seguro de que desea eliminar la especificacion (" + nombreEspecificacion + ")?");
            System.out.println("SI / NO:");
            String confirmacion = scan.nextLine().trim().toLowerCase();

            if (confirmacion.equals("no")) {
                System.out.println("Eliminacion cancelada.");
                continue;
            }

            // Eliminar la especificacion del archivo de texto
            eliminarEspecificacion(nombreEspecificacion);

            // Eliminar las asociaciones en el archivo de productos
            eliminarAsociaciones(nombreEspecificacion);
            // Regresar al menú

            System.out.println("Especificacion (" + nombreEspecificacion + ") eliminada con exito");
            return;
        }
    }

    // Función para eliminar la especificación del archivo 
    public static void eliminarEspecificacion(String nombreEspecificacion) {
               try {
                    
                    //abrir para leer 
                    FileReader fr = new FileReader(archivoEspecificaciones);
                    BufferedReader br = new BufferedReader(fr);

                    //Copia de archivo
                    //crea archivo
                    File archivoCopia  = new File("copia_especificaciones.txt");
                    //abrir para escritura
                    FileWriter fw = new FileWriter(archivoCopia);
                    BufferedWriter bw = new BufferedWriter(fw);

                    String linea = "";

                    //leer achivo original 
                    while ((linea = br.readLine()) != null) {
                        String[] datos = linea.split("\\|");
                        
                        if (datos[0].compareTo(nombreEspecificacion) != 0) {
                            bw.write(linea + "\n");
                        }
                    }
                    // cerrar archivos 
                    bw.close();
                    br.close();

                    //Remplaza el nombr del archivo, Archivo copia que la ruta tiene remplace el archivo original
                    
                    Files.move(archivoCopia.toPath(),archivoEspecificaciones.toPath(), REPLACE_EXISTING);
                } catch (FileNotFoundException ex) {
                } catch (IOException ex) {
                }
  
}

    public static void eliminarAsociaciones(String nombreActual) {
        try {

            //abrir para leer 
            FileReader fr = new FileReader(archivoProductos);
            BufferedReader br = new BufferedReader(fr);

            //Copia de archivo
            //crea archivo
            File archivoCopia = new File("copia_Productos.txt");
            //abrir para escritura
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);

            String linea = "";

            //leer achivo original 
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");

                if (datos[0].compareTo(nombreActual) != 0) {
                    bw.write(linea + "\n");
                }
            }
            // cerrar archivos 
            bw.close();
            br.close();

            //Remplaza el nombr del archivo, Archivo copia que la ruta tiene remplace el archivo original
            Files.move(archivoCopia.toPath(), archivoProductos.toPath(), REPLACE_EXISTING);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }

    }

    // metodo para verificar si una especificacion exista
    public static boolean especificacionExiste(String nombreEspecificacion) {
        // Leer el archivo línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(archivoEspecificaciones))) {
            String linea;
            // Recorre todas las líneas del archivo
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos[0].compareTo(nombreEspecificacion) == 0) {
                    return true;
                }
            }
        } catch (IOException excepcion) {
        }
        return false; // Si no se encuentra la característica, devuelve false
    }

    
    //metodo para ver especificaciones 
    public static void especificacionesExistentes() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoEspecificaciones))) {
            String linea;
            Scanner scan = new Scanner(System.in);
            boolean hayEspecificaciones = false; // Falso si no hay especificaciones

            System.out.println("ESPECIFICACIONES EXISTENTES");
            System.out.println("");

            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    System.out.println("- " + linea); // Imprime la línea si no está vacía
                    hayEspecificaciones = true; // Marca que hay especificaciones
                }
            }

            // Si no se encontraron especificaciones
            if (!hayEspecificaciones) {
                System.out.println("No hay especificaciones existentes para eliminar.");
                System.out.println("----------------------------------");
                System.out.println("Presiona Enter para regresar");
                scan.nextLine();
                // regresar al menu
                MenuCategoria.main(new String[0]);
            }

            System.out.println("-----------------------------------"); // Línea separadora final
        } catch (IOException e) {
        }
    }
}
