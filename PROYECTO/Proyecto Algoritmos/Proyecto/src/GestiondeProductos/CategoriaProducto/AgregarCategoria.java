package GestiondeProductos.CategoriaProducto;

import java.util.Scanner;
import java.io.*;

public class AgregarCategoria {

    // Ruta de archivo
    private static File archivoCategorias = new File("datos\\Categorias.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String nombreCategoria;
        String descripcionCategoria;
        String base = "";

        try {
            if (archivoCategorias.createNewFile()) {
                System.out.println("El archivo Categorias se ha creado con exito.");
            }
        } catch (IOException excepcion) {
            excepcion.printStackTrace(System.out);
        }

        // Encabezado
        System.out.println("");
        System.out.println("        AGREGAR CATEGORIA        ");
        System.out.println("_________________________________");
        System.out.println("");

        // Mostrar las categorías existentes
        categoriasExistentes();

        // Ingresar nombre de nueva categoría
        do {
            System.out.println("-Ingrese nombre de la nueva categoria:");
            nombreCategoria = scan.nextLine().trim();

            // si el nombre está vacío o ya existe
            if (nombreCategoria.isEmpty()) {
                System.out.println("El nombre de la categoría no puede estar vacío.");
            } else if (categoriaExiste(nombreCategoria)) {
                System.out.println("Ya existe una categoría con ese nombre.");
                nombreCategoria = ""; // Volver a pedir el nombre
            }
        } while (nombreCategoria.isEmpty());

        // ingresa la descripción de la categoría
        System.out.println("-Ingrese una descripcion para la categoria (Opcional) ");
        descripcionCategoria = scan.nextLine().trim();

        // Guardar la categoria
        guardarCategoria(nombreCategoria, descripcionCategoria);
        System.out.println("Categoria (" + nombreCategoria + ") agregada exitosamente.");

    }

    // metodo para guardar categoria
    public static void guardarCategoria(String nombreCategoria, String descripcionCategoria) {
        // Escribir en el archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCategorias, true))) {
            bw.write((nombreCategoria) + "|" + (descripcionCategoria) + "\n"); // Escribe la descripción
        } catch (IOException e) {
        }
    }

    // Función para verificar si una categoría ya existe en el archivo de texto
    public static boolean categoriaExiste(String nombreCategoria) {
        // Leer el archivo línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCategorias))) {
            String linea;
            // Recorre todas las líneas del archivo
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos[0].compareTo(nombreCategoria) == 0) {
                    return true;// si encuentra la categoria true 
                }
            }
        } catch (IOException excepcion) {
        }
        return false; // Si no se encuentra la característica, devuelve false
    }

    // metodo para mostrar las categorías existentes
    public static void categoriasExistentes() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCategorias))) {
            String linea;
            boolean hayCategorias = false; // falso si no hay categorias

            System.out.println("CATEGORIAS EXISTENTES");
            System.out.println("");

            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    System.out.println("- " + linea); // Imprime la línea si no está vacía
                    hayCategorias = true; // Marca que hay categorias
                }
            }

            // Si no se encontraron categorias
            if (!hayCategorias) {
                System.out.println("No hay categorias existentes.");
            }

            System.out.println("-----------------------------------"); // Línea separadora final
        } catch (IOException e) {
        }
    }
}
