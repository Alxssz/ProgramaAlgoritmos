package GestiondeProductos.CategoriaProducto;

import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class modificarCategoria {

    // Ruta de archivo
    private static File archivoCategorias = new File("datos/Categorias.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String nombreActual;
        String nuevoNombre;
        String nuevaDescripcion;

        // Encabezado
        System.out.println("        MODIFICAR CATEGORIA        ");
        System.out.println("___________________________________");
        System.out.println("");

        // Mostrar las categorías existentes
        categoriasExistentes();

        // Solicitar el nombre de la categoría a modificar
        while (true) {
            System.out.println("-Ingrese el nombre de la categoria a modificar:");
            nombreActual = scan.nextLine().trim();

            // Validar que el nombre no este vacio oya exista
            if (nombreActual.isEmpty()) {
                System.out.println("El nombre de la categoria no puede estar vacio.");
                System.out.println("");
            } else if (!categoriaExiste(nombreActual)) {
                System.out.println("La categoria (" + nombreActual + ") no existe.");
                System.out.println("");
            } else {
                break;
            }
        }

        // Solicitar el nuevo nombre de la categoría
        do {
            System.out.println("-Ingrese el nuevo nombre de la categoria");
            nuevoNombre = scan.nextLine().trim();

            // Validar que el nombre no este vacio o ya exista
            if (nuevoNombre.isEmpty()) {
                System.out.println("El nuevo nombre de la categoria no puede estar vacio.");
                System.out.println("");

            } else if (categoriaExiste(nuevoNombre) && !nuevoNombre.equals(nombreActual)) {
                System.out.println("Ya existe una categoria con ese nombre.");
                System.out.println("");
                nuevoNombre = "";
            }
        } while (nuevoNombre.isEmpty());

        
        // Solicitar la nueva descripción de la categoría
        System.out.println("-Ingrese una nueva descripcion para la categoria (opcional):");
        nuevaDescripcion = scan.nextLine().trim();

        
        // Modificar la categoría en el archivo de texto junto a las variables
        modificarArchivo(nombreActual, nuevoNombre, nuevaDescripcion);

        System.out.println("Categoria (" + nombreActual + ") modificada exitosamente.");
    }

    // metodo para modificar la categoria
    public static void modificarArchivo(String nombreActual, String nuevoNombre, String nuevaDescripcion) {
        // Leer todas las líneas del archivo
        try {
            //abrir para leer 
            FileReader fr = new FileReader(archivoCategorias);
            BufferedReader br = new BufferedReader(fr);

            //crea copia 
            File archivoCopia = new File("copia_Categorias.txt");
            //abrir para escritura 
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");

                if (datos[0].compareTo(nombreActual) == 0) {
                    linea = nuevoNombre + "|" + nuevaDescripcion;
                }
                bw.write(linea + "\n");
            }
            bw.close();
            br.close();

            // Mover archivo copia al archivo original
            Files.move(archivoCopia.toPath(), archivoCategorias.toPath(), REPLACE_EXISTING);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    // metodo para verificar si una categoria existe 
    public static boolean categoriaExiste(String nombreActual) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCategorias))) {
            String linea;
            //leer linea 
            while ((linea = br.readLine()) != null) {
                //separa en cadenas
                String[] datos = linea.split("\\|");
                
                //si la categoria esta
                if (datos[0].equals(nombreActual)) {
                    return true; //si esta retornar true
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    //Metodo ara mostrar las categorias existentes
    public static void categoriasExistentes() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCategorias))) {
            String linea;
            Scanner scan = new Scanner(System.in);
            boolean hayCategorias = false;

            //encabeazado
            System.out.println("CATEGORIAS EXISTENTES");
            System.out.println("");

            //que la linea no esta vacia y imprimir linea
            while ((linea = br.readLine()) != null) {
                if (!linea.isEmpty()) {
                    System.out.println("- " + linea);
                    hayCategorias = true;
                }
            }
//si no hay categorias regresar al menu
            if (!hayCategorias) {
                System.out.println("No hay categorias existentes para modificar.");
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
