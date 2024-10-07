package GestiondeProductos.Especificaciones;

import GestiondeProductos.CategoriaProducto.MenuCategoria;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ModificarEspecificacion {

    //ruta de archivo 
    private static File archivoEspecificaciones = new File("datos\\Especificaciones.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String nombreActual;
        String nuevoNombre;
        String nuevaDescripcion;
        String nuevoDato;

        // Encabezado
        System.out.println("     MODIFICAR ESPECIFICACION      ");
        System.out.println("___________________________________");
        System.out.println("");

        // Mostrar las especificaciones existentes
        especificacionesExistentes();

        // Solicitar el nombre de la especificacion a modificar
        while (true) {
            System.out.println("-Ingrese el nombre de la especificacion a modificar");
            nombreActual = scan.nextLine().trim();

            // Validar el nombre ingresado no vacio y si existe
            if (nombreActual.isEmpty()) {
                System.out.println("El nombre de la especificacion no puede estar vacio.");
                System.out.println("");
            } else if (!especificacionExiste(nombreActual)) {
                System.out.println("La especificacion (" + nombreActual + ") no existe.");
                System.out.println("");
            } else {
                break;
            }
        }

        // Solicitar el nuevo nombre de la especificacion
        do {
            System.out.println("-Ingrese el nuevo nombre de la especificacion:");
            nuevoNombre = scan.nextLine().trim();

            // Validar que el nuevo nombre no este vacio y que no exista otra con el mismo nombre
            if (nuevoNombre.isEmpty()) {
                System.out.println("El nuevo nombre de la especificacion no puede estar vacio.");
                System.out.println("");
                //si no si contiene el nombre 
            } else if (especificacionExiste(nuevoNombre)) {
                System.out.println("Ya existe una especificacion con ese nombre.");
                System.out.println("");
                nuevoNombre = ""; // pedir el nombre de nuevo
            }
            //hasta que el nombre este vacio
        } while (nuevoNombre.isEmpty());

        // Solicitar la nueva descripcion 
        System.out.println("-Ingrese una nueva descripcion para la especificacion (Opcional) ");
        nuevaDescripcion = scan.nextLine().trim();
        
        //ingresar el tipo de dato nuevp
        do {
            System.out.println("- Ingrese el nuevo tipo de dato (texto, numero, fecha):");
            nuevoDato = scan.nextLine().trim().toLowerCase();

            if (!nuevoDato.equals("texto") && !nuevoDato.equals("numero") && !nuevoDato.equals("fecha")) {
                System.out.println("Ingrese un tipo de dato valido");
            }
        } while (nuevoDato.isEmpty());
        
        // Modificar la especificacion en el archivo de texto
        modificarArchivo(nombreActual, nuevoNombre, nuevaDescripcion, nuevoDato);

        System.out.println("Especificacion (" + nombreActual + ") modificada exitosamente.");
    }

    //metodo para modificar archivo
    public static void modificarArchivo(String nombreActual, String nuevoNombre, String nuevaDescripcion, String nuevoDato) {
        // Leer todas las líneas del archivo
        try {
            //abrir para leer 
            FileReader fr = new FileReader(archivoEspecificaciones);
            BufferedReader br = new BufferedReader(fr);

            //crea copia 
            File archivoCopia = new File("copia_Especificaciones.txt");
            //abrir para escritura 
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);
            String linea;

            while ((linea = br.readLine()) != null) {
                //separa los datos en cadena 0 1 2 3 
                String[] datos = linea.split("\\|");

                if (datos[0].compareTo(nombreActual) == 0) {
                    //imprime las lineas con separadoar 
                    linea = nuevoNombre + "|" + nuevaDescripcion + "|" + nuevoDato + "\n";
                }
                bw.write(linea + "\n");
            }
            bw.close();
            br.close();

            // Mover archivo copia al archivo original
            Files.move(archivoCopia.toPath(), archivoEspecificaciones.toPath(), REPLACE_EXISTING);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    // metodo para verificar si una especificación ya existe en el archivo de texto
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

    // metodo para mostrar las especificaciones existentes en el archivo de texto
    public static void especificacionesExistentes() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoEspecificaciones))) {
            String linea;
            Scanner scan = new Scanner (System.in);
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
                System.out.println("No hay especificaciones existentes para modificar.");
                System.out.println("----------------------------------");
                System.out.println("Presiona Enter para regresar");
                scan.nextLine();
                // regresar al menu
                MenuEspecificaciones.main(new String[0]);
            }

            System.out.println("-----------------------------------"); // Línea separadora final
        } catch (IOException e) {
        }
    }
}
