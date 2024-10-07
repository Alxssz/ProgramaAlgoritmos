package GestiondeProductos.CategoriaProducto;

import GestiondeProductos.Caracteristicas.EliminarCaracteristica;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EliminarCategoria {

    // Ruta del archivos
    private static File archivoCategorias = new File("datos\\Categorias.txt");
    private static File archivoProductos = new File("datos\\Productos.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Encabezado
        System.out.println("");
        System.out.println("      ELIMINAR CATEGORIA    ");
        System.out.println("____________________________");
        System.out.println("");

        // Muestra las categorías existentes
        categoriasExistentes();

        while (true) {
            // Solicitar el nombre de la categoria a eliminar
            System.out.println("-Ingrese el nombre de la categoria a eliminar:");
            String nombreCategoria = scan.nextLine().trim();

            // que no este vacia
            if (nombreCategoria.isEmpty()) {
                System.out.println("El nombre de la categoria no puede estar vacio.");
                continue; // Volver a solicitar
            }

            // Validar que la categoría existe
            if (!categoriaExiste(nombreCategoria)) {
                System.out.println("La categoria (" + nombreCategoria + ") no existe.");
                continue; // Volver a solicitar
            }

            // Confirmar la eliminacion
            System.out.println("Esta seguro de que desea eliminar la categoria (" + nombreCategoria + ")?");
            System.out.println("SI / NO:");
            //ingresar confimracion y eliminar espacios en blanco y convertir a minuscula
            String confirmacion = scan.nextLine().trim().toLowerCase();

            if (confirmacion.equals("no")) {
                System.out.println("Eliminacion cancelada.");
                MenuCategoria.main(new String[0]);
            }

            // Eliminar la categoría del archivo de texto
            eliminarCategoria(nombreCategoria);
            // Eliminar las asociaciones en el archivo de productos
            eliminarAsociaciones(nombreCategoria);
            // Regresar al menú
            System.out.println("La categoria ("+ nombreCategoria + ") se ha eliminado con exito");
            return;
        }

    }

    // Función para eliminar la categoría del archivo 
    public static void eliminarCategoria(String nombreCategoria) {
        try {

            //abrir para leer 
            FileReader fr = new FileReader(archivoCategorias);
            BufferedReader br = new BufferedReader(fr);

            //Copia de archivo
            //crea archivo
            File archivoCopia = new File("copia_Categorias.txt");
            //abrir para escritura
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);

            String linea = "";

            //leer achivo original 
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");

                if (datos[0].compareTo(nombreCategoria) != 0) {
                    bw.write(linea + "\n");
                }
            }
            // cerrar archivos 
            bw.close();
            br.close();

            //Remplaza el nombr del archivo, Archivo copia que la ruta tiene remplace el archivo original
            Files.move(archivoCopia.toPath(), archivoCategorias.toPath(), REPLACE_EXISTING);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }

    }

// Función para eliminar las asociaciones entre la categoría y los productos
    public static void eliminarAsociaciones(String nombreCategoria) {
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

                if (datos[0].compareTo(nombreCategoria) != 0) {
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

    // Función para verificar si una categoría existe en el archivo de texto
    public static boolean categoriaExiste(String nombreActual) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCategorias))) {
            String linea;
            //leer lineas
            while ((linea = br.readLine()) != null) {
                //convertir a cadena
                String[] datos = linea.split("\\|");
                if (datos[0].equals(nombreActual)) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    // Función para mostrar las categorías existentes en el archivo de texto
    public static void categoriasExistentes() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCategorias))) {
            String linea;
            Scanner scan = new Scanner(System.in);
            boolean hayCategorias = false;
            //encabezado 
            System.out.println("CATEGORIAS EXISTENTES");
            System.out.println("");

            //leer lineas y imprimir lo leido 
            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    System.out.println("- " + linea);
                    hayCategorias = true;
                }
            }
            // si no hay categorias
            if (!hayCategorias) {
                System.out.println("No hay categorias existentes para eliminar.");
                System.out.println("----------------------------------");
                System.out.println("Presiona Enter para regresar");
                scan.nextLine();
                // regresar al menu
                MenuCategoria.main(new String[0]);
            }

            System.out.println("-----------------------------------");
        } catch (IOException e) {
        }
    }
}