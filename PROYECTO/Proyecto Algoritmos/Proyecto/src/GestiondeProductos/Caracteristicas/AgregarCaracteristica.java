package GestiondeProductos.Caracteristicas;

import java.util.Scanner;
import java.io.*;

public class AgregarCaracteristica {

    // Ruta de archivo 
    private static File archivoCaracteristica = new File("datos\\Caracteristicas.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String nombreCaracteristica;
        String descripcionCaracteristica;

        // Verificación de si el archivo ya está creado, si no, se crea uno nuevo
        try {
            if (archivoCaracteristica.createNewFile()) {
                System.out.println("El archivo Caracteristicas se ha creado con exito.");
            }
        } catch (IOException excepcion) {
        }

        // Encabezado del menú
        System.out.println("");
        System.out.println("       AGREGAR CARACTERISTICA     ");
        System.out.println("__________________________________");
        System.out.println("");

        // Muestra las caracteristicas existentes
        caracteristicasExistentes();

        // validar que el nombre de la nueva caracteristica no este vacia ni repetida
        do {
            System.out.println("-Ingrese nombre de la nueva caracteristica:");
            nombreCaracteristica = scan.nextLine().trim();
            //que no este vacio
            if (nombreCaracteristica.isEmpty()) {
                System.out.println("El nombre de la caracteristica no puede estar vacio");
                // si ya existe
            } else if (caracteristicaExiste(nombreCaracteristica)) {
                System.out.println("Ya existe una caracteristica con ese nombre");
                nombreCaracteristica = "";
            }
        } while (nombreCaracteristica.isEmpty());

        // Solicita descripción de la característica
        System.out.println("-Ingrese una descripcion para la caracteristica (opcional):");
        descripcionCaracteristica = scan.nextLine().trim(); // Elimina espacios en blanco

        // Llama al método para guardar la nueva característica con las variables 
        guardarCaracteristica(nombreCaracteristica, descripcionCaracteristica);
        System.out.println("Caracteristica (" + nombreCaracteristica + ") agregada exitosamente.");
    }

    // Método que guarda la nueva característica en el archivo
    public static void guardarCaracteristica(String nombreCategoria, String descripcionCaracteristica) {
        //escribir en el archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCaracteristica, true))) {
            bw.write((nombreCategoria) + "|" + (descripcionCaracteristica) + "\n"); // Escribe la descripción
        } catch (IOException e) {
        }
    }

    //Metodo para ver si la caracteristica 
    public static boolean caracteristicaExiste(String nombreCaracteristica) {
        //leer el archivo línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCaracteristica))) {
            String linea;
            // Recorre todas las líneas del archivo
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos[0].compareTo(nombreCaracteristica) == 0) {;
                    return true;
                }
            }
        } catch (IOException excepcion) {
        }
        return false; // Si no se encuentra la característica, devuelve false
    }

    // Método que muestra todas las características existentes 
    public static void caracteristicasExistentes() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCaracteristica))) {
            String linea;
            boolean hayCaracteristicas = false; // Falso si no hay caracteristicas

            System.out.println("CARACTERISTICAS EXISTENTES");
            System.out.println("");

            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    System.out.println("- " + linea); // Imprime la línea si no esta vacía
                    hayCaracteristicas = true; // Marca que hay características
                }
            }

            // Verifica si no se encontraron caracteristicas después de leer todas las líneas
            if (!hayCaracteristicas) {
                System.out.println("No hay caracteristicas existentes.");
            }

            System.out.println("-----------------------------------"); // Línea separadora final
        } catch (IOException e) {
        }
    }
}

