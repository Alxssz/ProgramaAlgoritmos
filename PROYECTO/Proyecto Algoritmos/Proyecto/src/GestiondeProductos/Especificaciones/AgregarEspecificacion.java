package GestiondeProductos.Especificaciones;

import java.util.Scanner;
import java.io.*;

public class AgregarEspecificacion {

    // Ruta de archivo 
    private static File archivoEspecificaciones = new File("datos\\Especificaciones.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String nombreEspecificacion;
        String descripcionEspecificacion;
        
        //crear archivo

        try {
            if (archivoEspecificaciones.createNewFile()) {
                System.out.println("El archivo Especificaciones se ha creado con exito.");
            }
        } catch (IOException excepcion) {
            excepcion.printStackTrace(System.out);
        }

        // Encabezado
        System.out.println("");
        System.out.println("      AGREGAR ESPECIFICACION     ");
        System.out.println("_________________________________");
        System.out.println("");

        // Mostrar las especificaciones existentes
        especificacionesExistentes();

        // Ingresa nombre de nueva especificacion
        do {
            System.out.println("- Ingrese nombre de la nueva especificacion");
            nombreEspecificacion = scan.nextLine().trim();

            // si esta vacio o ta existe
            if (nombreEspecificacion.isEmpty()) {
                System.out.println("El nombre de la especificacion no puede estar vacio");
            } else if (especificacionExiste(nombreEspecificacion)) {
                System.out.println("Ya existe una especificacion con ese nombre");
                nombreEspecificacion = ""; // vuelve a pedir el nombre
            }
        } while (nombreEspecificacion.isEmpty());

        // ingresar descripcion
        System.out.println("- Ingrese una descripcion para la especificacion (Opcional): ");
        descripcionEspecificacion = scan.nextLine().trim();
        
        //variable declarada
        String dato;
        
        //validar el tipo de dato
        do {
            System.out.println("- Ingrese el tipo de dato (texto, numero, fecha):");
            dato = scan.nextLine().trim().toLowerCase();

            if (!dato.equals("texto") && !dato.equals("numero") && !dato.equals("fecha")) {
                System.out.println("Ingrese un tipo de dato valido");
            }
        } while (dato.isEmpty());

        // Guardar la nueva especificación
        guardarEspecificacion(nombreEspecificacion, descripcionEspecificacion, dato);
        System.out.println("Especificacion (" + nombreEspecificacion + ") agregada exitosamente.");
    }

    // metodo para guardar la nueva especificacion
    public static void guardarEspecificacion(String nombreEspecificacion, String descripcionEspecificacion, String tipoDato) {
        // Escribir en el archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoEspecificaciones, true))) {
            bw.write((nombreEspecificacion) + "|" + (descripcionEspecificacion) + "|" + (tipoDato) + "\n"); // Escribe la descripción
        } catch (IOException e) {
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
            boolean hayCategorias = false; // Falso si no hay especificaciones

            System.out.println("ESPECIFICACIONES EXISTENTES");
            System.out.println("");

            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    System.out.println("- " + linea); // Imprime la línea si no está vacía
                    hayCategorias = true; // Marca que hay especificaciones
                }
            }
            // Si no se encontraron especificaciones
            if (!hayCategorias) {
                System.out.println("No hay especificaciones existentes.");
            }

            System.out.println("-----------------------------------"); // Línea separadora final
        } catch (IOException e) {
        }
    }
}
