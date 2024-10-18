package GestiondeProductos.Productos;

import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class BajaProductos {

    // Ruta del archivo de productos
    private static File archivoAltaProductos = new File("datos\\AltaProductos.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Encabezado
        System.out.println("");
        System.out.println("      ELIMINAR PRODUCTO    ");
        System.out.println("____________________________");
        System.out.println("");

        // Muestra los productos existentes
        productosExistentes();

        while (true) {
            // Solicitar el ID del producto a eliminar
            System.out.println("- Ingrese el ID del producto a eliminar:");
            String id = scan.nextLine().trim();

            // que no este vacio
            if (id.isEmpty()) {
                System.out.println("El ID no puede estar vacio.");
                continue; // Volver a solicitar
            }

            // que el producto existe
            if (!productoExiste(id)) {
                System.out.println("El producto (" + id + ") no existe.");
                continue; // Volver a solicitar
            }

            // Confirmar la eliminacion
            System.out.println("Está seguro de que desea eliminar el producto con ID (" + id + ")?");
            System.out.println("SI / NO:");
            String confirmacion = scan.nextLine().trim().toLowerCase();

            if (confirmacion.equals("no")) {
                System.out.println("Eliminación cancelada.");
                continue;
            }
            // Eliminar el producto del archivo de texto
            eliminarProducto(id);
            System.out.println("Se elimino producto.");
            // Regresar al menú
            return;
        }
    }

    // metodo para verificar si un producto existe en el archivo de texto
    public static boolean productoExiste(String id) {
        // Leer el archivo línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(archivoAltaProductos))) {
            String linea;
            // Recorre todas las líneas del archivo
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos[0].equals(id)) { //si la posicion 0 contiene el ID
                    return true; // si se encuentra true
                }
            }
        } catch (IOException excepcion) {
        }
        return false; // Si no se encuentra el producto, devuelve false
    }

    // metodo para eliminar el producto del archivo 
    public static void eliminarProducto(String id) {
        try {
            // Abrir para leer 
            FileReader fr = new FileReader(archivoAltaProductos);
            BufferedReader br = new BufferedReader(fr);

            // Crear copia del archivo
            File archivoCopia = new File("copia_ArchivoAltaProductos.txt");
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);

            String linea;

            // Leer archivo original 
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (!datos[0].equals(id)) { // si contiene el ID
                    bw.write(linea + "\n");//escribir lineas que no 
                }
            }

            // Cerrar archivos 
            bw.close();
            br.close();

            // Reemplazar el archivo original con la copia
            Files.move(archivoCopia.toPath(), archivoAltaProductos.toPath(), REPLACE_EXISTING);
        } catch (IOException ex) {
        }
    }

    // metodo para mostrar los productos existentes
    public static void productosExistentes() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoAltaProductos))) {
            String linea;
            Scanner scan = new Scanner(System.in);

            System.out.println("PRODUCTOS EXISTENTES");
            System.out.println("");
            //ller lineas
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue; // Omitir líneas vacías
                }
                //converir a cadenas
                String[] datos = linea.split("\\|");
                    System.out.println("ID:" + datos[0] + "|" + datos[1]); // Imprimir el ID y el nombre del producto
                }
            System.out.println("-----------------------------------"); // Línea separadora final
        } catch (IOException e) {
        }
    }
}
