package GestiondeProductos.Productos;

//metodos importads
import static GestiondeProductos.CategoriaProducto.AgregarCategoria.categoriaExiste;
import static GestiondeProductos.CategoriaProducto.AgregarCategoria.categoriasExistentes;
import static GestiondeProductos.Productos.BajaProductos.productosExistentes;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ModificacionAltaProducto {

    // Ruta de archivo
    private static File archivoAltaProductos = new File("datos\\AltaProductos.txt");
    private static File archivoCategorias = new File("datos\\Categorias.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String id, nuevoNombre, categoria, nuevaDescripcion, nuevaCaracteristica;
        int nuevoStock;
        double nuevoPrecio;

        // Encabezado
        System.out.println("      MODIFICAR ALTA PRODUCTOS    ");
        System.out.println("__________________________________");

        // Mostrar los productos existentes
        productosExistentes();

        // Solicitar el ID del producto a modificar
        while (true) {
            System.out.println("-Ingrese el id del producto a modificar:");
            id = scan.nextLine().trim();

            //que el ID no este vacio
            if (id.isEmpty()) {
                System.out.println("El ID del producto no puede estar vacio");
                //si el ID no existe
            } else if (!productoExiste(id)) {
                System.out.println("El nombre (" + id + ") no existe.");
            } else { //pero si existe
                // Mostrar los datos actuales del producto
                System.out.println("Datos actuales del producto");
                mostrarDatosProducto(id);
                break;
            }
        }

        // Solicitar nuevo nombre del producto
        do {
            System.out.println("-Ingrese el nuevo nombre del producto:");
            nuevoNombre = scan.nextLine().trim();
            //que no este vacio o que exista otro con el msimo nombre
            if (nuevoNombre.isEmpty()) {
                System.out.println("El nuevo nombre no puede estar vacio");
            } else if (productoExiste(nuevoNombre) && !nuevoNombre.equals(id)) {
                System.out.println("Ya existe un producto con ese nombre");
                nuevoNombre = "";
            }
        } while (nuevoNombre.isEmpty());

        // Solicitar nuevo nombre de la categoría
        do {
            categoriasExistentes();
            System.out.println("-Ingrese el nuevo nombre de la categoria:");
            categoria = scan.nextLine().trim();
             // que no este vacio
            if (categoria.isEmpty()) {
                System.out.println("El nuevo nombre de la categoria no puede estar vacio."); 
            //si existe
            } else if (!categoriaExiste(categoria)) {
                System.out.println("La categoria ingresada no existe.");
                categoria = ""; //pedir categoria otra vez
            }
        } while (categoria.isEmpty());

        // ingresar descripcion
        System.out.println("-Ingrese una nueva descripcion para el producto (opcional):");
        nuevaDescripcion = scan.nextLine().trim();

        // ingresar caracteristicas
        System.out.println("-Ingrese nuevas caracteristicas:");
        nuevaCaracteristica = scan.nextLine().trim();

        // Solicitar nuevo precio
        do {
            System.out.println("-Ingrese nuevo precio:");
            nuevoPrecio = scan.nextDouble();
            if (nuevoPrecio < 0) {
                System.out.println("El precio debe ser un valor positivo"); //evalua que sea un numero positivo
            }
        } while (nuevoPrecio < 0);

        // Solicitar nuevo stock
        do {
            System.out.println("-Ingrese nueva cantidad de stock:");
            nuevoStock = scan.nextInt();
            if (nuevoStock < 0) {
                System.out.println("El stock debe ser un valor positivo.\n");
            }
        } while (nuevoStock < 0);

        // Modificar el producto en el archivo
        modificarArchivo(id, categoria, nuevaDescripcion, nuevoNombre, nuevaCaracteristica, nuevoPrecio, nuevoStock);

        //mensaje de que si se modifico
        System.out.println("Producto (" + id + ") modificado exitosamente.");
    }

    // metodo para verificar si un producto existe en el archivo de texto
    public static boolean productoExiste(String id) {
        // Leer el archivo línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(archivoAltaProductos))) {
            String linea;
            // Recorre todas las líneas del archivo
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos[0].equals(id)) { //si contiene el id 
                    return true;
                }
            }
        } catch (IOException excepcion) {
        }
        return false; // Si no se encuentra el producto, devuelve false
    }

    
    
//metodo para modificar archivo
    public static void modificarArchivo(String id, String categoria, String nuevaDescripcion, String nuevoNombre, String nuevaCaracteristica, double nuevoPrecio, int nuevoStock) {
        // Leer todas las líneas del archivo
        try {
            //abrir para leer 
            FileReader fr = new FileReader(archivoAltaProductos);
            BufferedReader br = new BufferedReader(fr);

            //crea copia 
            File archivoCopia = new File("copia_archivoAltaProductos.txt");
            //abrir para escritura 
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);
            String linea;

            while ((linea = br.readLine()) != null) {
                //separa los datos en cadena 0 1 2 3 
                String[] datos = linea.split("\\|");
                 //imprimir lineas
                if (datos[0].compareTo(id) == 0) {
                    linea = datos[0] + "|" + nuevoNombre + "|" + categoria + "|" + nuevaDescripcion + "|" + nuevaCaracteristica
                            + "|" + nuevoPrecio + "|" + nuevoStock+ "\n";
                }
                bw.write(linea + "\n");
            }
            bw.close();
            br.close();

            // Mover archivo copia al archivo original
            Files.move(archivoCopia.toPath(), archivoAltaProductos.toPath(), REPLACE_EXISTING);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    // Función para mostrar los datos actuales del producto
    public static void mostrarDatosProducto(String id) {
        try {
            FileReader fr = new FileReader(archivoAltaProductos);
            BufferedReader br = new BufferedReader(fr);
            String linea = "";

            //mostrar lineas por su posicion
            while ((linea = br.readLine()) != null) {
                 String[] datos = linea.split("\\|");
                if (linea.contains(id)) {
                    System.out.println("");
                    System.out.println("Nombre Producto: "+ datos[1]);
                    System.out.println("Categoria: "+ datos[2]);
                    System.out.println("Descripcion: "+ datos[3]);
                    System.out.println("Caracteristicas "+ datos[4]);
                    System.out.println("Precio: "+ datos[5]);
                    System.out.println("Stock: "+ datos[6]);
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

}
