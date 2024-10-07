package GestiondeProductos.Caracteristicas;

import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EliminarCaracteristica {

    // Ruta de archivo de características y productos
    private static File archivoCaracteristica = new File("datos\\Caracteristicas.txt");
    private static File archivoProductos = new File("datos\\Productos.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Encabezado
        System.out.println("");
        System.out.println("      ELIMINAR CARACTERISTICA     ");
        System.out.println("__________________________________");
        System.out.println("");

        // Muestra características existentes
        caracteristicasExistentes();

        while (true) {
            // Solicitar el nombre de la característica a eliminar
            System.out.println("-Ingrese el nombre de la caracteristica a eliminar:");
            String nombreActual = scan.nextLine().trim();

            // Validar que la entrada no esté vacía
            if (nombreActual.isEmpty()) {
                System.out.println("El nombre de la caracteristica no puede estar vacio.");
                continue; // Volver a solicitar
            }

            // Validar que la característica existe
            if (!caracteristicaExiste(nombreActual)) {
                System.out.println("La caracteristica (" + nombreActual + ") no existe.");
                continue; // Volver a solicitar
            }

            // confirmar la eliminacion
            System.out.println("Esta seguro de que desea eliminar la caracteristica (" + nombreActual + ")?");
            System.out.println("SI / NO");
            //la confirmacion elimina espacios en blanco y convirte a minuscula
            String confirmacion = scan.nextLine().trim().toLowerCase();

            //si la confirmacion es no 
            if (confirmacion.equals("No")) {
                System.out.println("Eliminacion cancelada.");
                continue;
            }

            // Eliminar la característica del archivo de texto
            eliminarCaracteristica(nombreActual);

            // Eliminar las asociaciones en el archivo de productos
            eliminarAsociaciones(nombreActual);

            // Regresar al menu con return
            return;
        }
    }

    // Función para eliminar la característica del archivo 
    public static void eliminarCaracteristica(String nombreActual) {
        try {

            //abrir para leer 
            FileReader fr = new FileReader(archivoCaracteristica);
            BufferedReader br = new BufferedReader(fr);

            //Copia de archivo
            //crea archivo
            File archivoCopia = new File("copia_Caracteristicas.txt");
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
            Files.move(archivoCopia.toPath(), archivoCaracteristica.toPath(), REPLACE_EXISTING);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }

    }

    // Función para eliminar las asociaciones
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
              //si en la posicion 0 esta el nombre de la caracteristica 
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

    
    // Función para verificar si una característica existe en el archivo de texto
    public static boolean caracteristicaExiste(String nombreActual) {
        //leer el archivo línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCaracteristica))) {
            String linea;
            // Recorre todas las líneas del archivo
            while ((linea = br.readLine()) != null) {
                //separa en cadena
                String[] datos = linea.split("\\|");
                if (datos[0].compareTo(nombreActual) == 0) {;// si la posicion 0 tiene caracteristica
                    return true; //retorna true si esta caracteristica
                }
            }
        } catch (IOException excepcion) {
        }
        return false; // Si no se encuentra la característica, devuelve false
    }

    //Metodo para mostrar caracteristicas
    public static void caracteristicasExistentes() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCaracteristica))) {
            String linea;
            Scanner scan = new Scanner(System.in);

            System.out.println("CARACTERISTICAS EXISTENTES");
            System.out.println("");

            while ((linea = br.readLine()) != null) {

                //si la linea no esta vacia 
                if (!linea.isEmpty()) {
                    System.out.println("- " + linea); // imprime lina 
                } else { // si no regreso menu
                    System.out.println("No hay caracteristicas para eliminar");
                    System.out.println("----------------------------------");
                    System.out.println("Presiona Enter para regresar");
                    scan.nextLine();
                    // regresar al menu
                    MenuCaracteristicas.main(new String[0]);
                }
            }

            System.out.println("-----------------------------------"); // Línea separadora final
        } catch (IOException e) {
        }
    }

}
