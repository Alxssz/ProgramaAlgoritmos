package GestiondeProductos.Productos;

import static GestiondeProductos.CategoriaProducto.AgregarCategoria.categoriaExiste;
import static GestiondeProductos.CategoriaProducto.AgregarCategoria.categoriasExistentes;
import static GestiondeProductos.Productos.ModificarProducto.mostrarProductosExistentes;
import java.util.Scanner;
import java.io.*;

public class AltaProductos {

    // Ruta de archivo
    private static File archivoAltaProducto = new File("datos\\AltaProductos.txt");
    private static File archivoCategorias = new File("datos\\Categorias.txt");
    private static File archivoCaracteristicas = new File("datos\\Caracteristicas.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String nombreProducto;
        String categoriaProducto;
        String descripcion;
        String caracteristicas = "";
        double precio;
        int stock;

        // Verificación de archivo creado
        try {
            if (archivoAltaProducto.createNewFile()) {
                System.out.println("El archivo de productos se ha creado con éxito.");
            }
        } catch (IOException excepcion) {
            System.out.println("Error al crear el archivo de productos.");
        }

        // Encabezado
        System.out.println("");
        System.out.println("      ALTA PRODUCTO     ");
        System.out.println("________________________");
        System.out.println("");

        mostrarProductosExistentes();
        // Validación de nombre del producto
        do {
            System.out.println("- Ingrese el nombre del producto:");
            nombreProducto = scan.nextLine().trim();
            if (nombreProducto.isEmpty()) {
                System.out.println("El nombre del producto no puede estar vacio.");
            }
        } while (nombreProducto.isEmpty());

        // Selección de la categoría
        do {
            // Mostrar las categorías existentes
            System.out.println("");
            categoriasExistentes();
            System.out.println("-Ingrese nombre de la categoria del producto:");
            categoriaProducto = scan.nextLine().trim();
            if (!categoriaExiste(categoriaProducto)) {
                System.out.println("La categoría no existe elija una valida.");
            }
        } while (!categoriaExiste(categoriaProducto));

        // Solicitar la descripción del producto
        System.out.println("- Ingrese una descripcion para el producto (opcional):");
        descripcion = scan.nextLine().trim();

        // Solicitar características del producto
        System.out.println("- Ingrese las características del producto");
        caracteristicas = scan.nextLine().trim();

        // verificar el precio de venta
        do {
            System.out.println("- Ingrese el precio de venta del producto:");
            while (!scan.hasNextDouble()) {
                System.out.println("Por favor, ingrese un valor numérico para el precio.");
                scan.next();
            }
            precio = scan.nextDouble();
            scan.nextLine();
            //que el precio no sea negativo
            if (precio < 0) {
                System.out.println("El precio de venta no puede ser negativo.");
            }
        } while (precio < 0);

        // verificar la cantidad de stock
        do {
            System.out.println("- Ingrese la cantidad inicial de stock del producto:");
            while (!scan.hasNextInt()) {
                System.out.println("Por favor, ingrese un número entero positivo para el stock.");
                scan.next();
            }
            stock = scan.nextInt();
            scan.nextLine();
            //que el stock no sea negativo
            if (stock < 0) {
                System.out.println("La cantidad de stock no puede ser negativa.");
            }
        } while (stock < 0);

        // Guardar el producto en el archivo
        guardarProducto(nombreProducto, categoriaProducto, descripcion, caracteristicas, precio, stock);
        System.out.println("Producto (" + nombreProducto + ") creado correctamente.");
    }


    // Guardar el nuevo producto en el archivo
    public static void guardarProducto(String nombreProducto, String categoria, String descripcion,
    String caracteristicas, double precio, int stock) {
        int contador = 1;
        String reorden = "10";

        try {
            // Leer el archivo para obtener el último contador
            FileReader fr = new FileReader(archivoAltaProducto);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            //leer lineas
            while ((linea = br.readLine()) != null) {
                
                //separar en cadena
                String[] datos = linea.split("\\|");
                // el numero actual es igual al converitr el dato en numero 
                int numeroActual = Integer.parseInt(datos[0]);
                if (numeroActual >= contador) { //si ID mayor o igual a contador
                    contador = numeroActual + 1; // Incrementar el contador 1
                }
            }
            br.close(); // Cerrar 

            // Escribir en el archivo
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoAltaProducto, true))) {
                
                bw.write(contador + "|" + (nombreProducto) + "|" + (categoria) + "|" + (descripcion) + "|" + (caracteristicas) + "|" + (precio) + "|" + (stock) + reorden + "\n");
            }
        } catch (IOException e) {
        }
    }
}
