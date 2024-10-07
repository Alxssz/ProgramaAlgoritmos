package GestiondeProductos.Caracteristicas;

import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;

public class modificarCaracteristica {

    // Ruta de archivo
    private static File archivoCaracteristica = new File("datos\\Caracteristicas.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String nombreActual;
        String nuevoNombre;
        String nuevaDescripcion;

        // Encabezado
        System.out.println("     MODIFICAR CARACTERISTICA      ");
        System.out.println("___________________________________");
        System.out.println("");

        // Mostrar las caracteristicas existentes
        caracteristicasExistentes();

        // Solicitar el nombre de la caracteristica a modificar
        while (true) {
            System.out.println("-Ingrese el nombre de la caracteristica a modificar:");
            nombreActual = scan.nextLine().trim();

            // que no este vacio
            if (nombreActual.isEmpty()) {
                System.out.println("El nombre de la caracteristica no puede estar vacio.");
                System.out.println("");
                
                //si exste
            } else if (!caracteristicaExiste(nombreActual)) {
                System.out.println("La caracteristica (" + nombreActual + ") no existe.");
                System.out.println("");
            } else {
                break;
            }
        }

        // Solicitar el nuevo nombre de la caracteristica
        do {
            System.out.println("-Ingrese el nuevo nombre de la caracteristica:");
            nuevoNombre = scan.nextLine().trim();

            // Validar que el nuevo nombre no este vacio y que no exista otra con el mismo nombre
            if (nuevoNombre.isEmpty()) {
                System.out.println("El nuevo nombre de la caracteristica no puede estar vacio.");
                System.out.println("");

            } else if (caracteristicaExiste(nuevoNombre) && !nuevoNombre.equals(nombreActual)) {
                System.out.println("Error: Ya existe una caracteristica con ese nombre.");
                System.out.println("");
                nuevoNombre = ""; // pedir el nombre de nuevo si existe
            }
        } while (nuevoNombre.isEmpty());

        // Solicitar la nueva descripcion de la caracteristica
        System.out.println("-Ingrese una nueva descripcion para la caracteristica (Opcional) ");
        nuevaDescripcion = scan.nextLine().trim();

        // Modificar la caracteristica en el archivo de texto
        modificarArchivo(nombreActual, nuevoNombre, nuevaDescripcion);

        System.out.println("Caracteristica (" + nombreActual + ") modificada exitosamente.");
    }



// modificacion del archivo caracteristicas
    public static void modificarArchivo(String nombreActual, String nuevoNombre, String nuevaDescripcion) {
        // Leer todas las líneas del archivo
        try {

            //abrir para leer 
            FileReader fr = new FileReader(archivoCaracteristica);
            BufferedReader br = new BufferedReader(fr);

            //crea copia 
            File archivoCopia = new File("copia_Caracteristicas.txt");
            //abrir para escritura 
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);
            String linea;

            //lee archivo original
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                //si la posicion 0 tiene el nombre de la caracteristica
                if (datos[0].compareTo(nombreActual) == 0) {
                    linea = nuevoNombre + "|" + nuevaDescripcion;
                }
                bw.write(linea + "\n");
            }
            bw.close();
            br.close();

            Files.move(archivoCopia.toPath(),archivoCaracteristica.toPath(), REPLACE_EXISTING);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    
       //Metodo para ver si la caracteristica existe
    public static boolean caracteristicaExiste(String nombreActual) {
        //leer el archivo línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCaracteristica))) {
            String linea;
            // Recorre todas las líneas del archivo
            while ((linea = br.readLine()) != null) {
                //separa en cadena
                String[] datos = linea.split("\\|");
                if (datos[0].compareTo(nombreActual) == 0) {
                    return true; //retorma true si esta la caracteristica
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
            Scanner scan= new Scanner (System.in);

            System.out.println("CARACTERISTICAS EXISTENTES");
            System.out.println("");

            while ((linea = br.readLine()) != null) {

                //si la longitud del dato es mayor a 0 y no esta vacio la posicion 0  
                if (!linea.isEmpty()) {
                    System.out.println("- " + linea); // imprime 
                } else { // Si no se imprimieron características
                    System.out.println("No hay caracteristicas para modificar");
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
