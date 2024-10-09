package Pedidos;

import static GestiondeProductos.Productos.BajaProductos.productosExistentes;
import static GestiondeProductos.Productos.ModificacionAltaProducto.productoExiste;
import Menus.MenuPedidos;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CreacionPedido {

    // Rutas de archivos
    private static File archivoPedidos = new File("datos\\Pedidos.txt");
    private static File archivoAltaProductos = new File("datos\\AltaProductos.txt");
    private static File archivoProveedores = new File("datos\\Proveedores.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String id, proveedor;
        int cantidadIngresada = 0;
        double precio = 0;
        double total = 0;
        String entrega;

        // Encabezado
        System.out.println("     CREAR PEDIDO    ");
        System.out.println("_____________________");

        // Mostrar proveedores
        mostrarProveedor();
        System.out.println("-----------------------");

        // Pedir el proveedor
        do {
            System.out.println("- Ingrese el proveedor:");
            proveedor = scan.nextLine().trim();
            if (proveedor.isEmpty()) {
                System.out.println("El proveedor no puede estar vacío.");
            }
            if (!proveedorExiste(proveedor)) {
                System.out.println("El Proveedor (" + proveedor + ") no existe.");
                proveedor = "";
            }
        } while (proveedor.isEmpty());

        // inicair pedido
        StringBuilder detallesPedido = new StringBuilder();//manipular las cadenas de texto
        boolean seguirAgregando = true; // que si agregamos

        while (seguirAgregando) {
            productosExistentes();

            // Pedir el ID del producto
            do {
                System.out.println("- Ingrese el ID del producto a pedir:");
                id = scan.nextLine().trim();
                if (id.isEmpty()) {
                    System.out.println("El ID del producto no puede estar vacío.");
                }
                // Verificar si el producto existe
                if (!productoExiste(id)) {
                    System.out.println("El ID (" + id + ") no está registrado.");
                    return; // Termina el programa si el producto no existe
                }
            } while (id.isEmpty());

            // Obtener el precio del producto
            precio = obtenerPrecio(id);

            // Elegir la cantidad a ingresar
            do {
                System.out.println("- Ingrese la cantidad de producto a comprar:");
                cantidadIngresada = scan.nextInt();
                scan.nextLine();  // Consumir el salto de línea después de nextInt()

                if (cantidadIngresada < 0) {
                    System.out.println("La cantidad no puede ser negativa.");
                }
            } while (cantidadIngresada < 0);

            // calcular el total
            total = total + (precio * cantidadIngresada);
            
            // Guardar los textos en la variable detalle del pedido
            detallesPedido.append("ID Producto: ").append(id)
                    .append(", Cantidad: ").append(cantidadIngresada)
                    .append(", Precio Unitario: ").append(precio)
                    .append(", Subtotal: ").append(precio * cantidadIngresada)
                    .append("\n");

            // Preguntar si desea agregar más productos
            System.out.println("agregar mas productos al pedido? (SI /NO )");
            String respuesta = scan.nextLine().trim().toLowerCase();
            if (respuesta.equals("no")) {
                seguirAgregando = false;
                //ya no agregar más
            }
        }

        do {
            //ingresar eltiempo estimado
            System.out.println("- Ingrese el tiempo estimado de entrega:");
            entrega = scan.nextLine().trim();
            //que no este vacio
            if (entrega.isEmpty()) {
                System.out.println("El tiempo estimado no puede estar vacio");
                entrega = "";
            }
        } while (entrega.isEmpty());

        // guardar el pedido en el archivo
        guardarPedido(proveedor, total, detallesPedido.toString(), entrega);
        System.out.println("El pedido se ha creado correctamente.");
        MenuPedidos.main(new String [0]);
    }

    public static void guardarPedido(String proveedor, double total, String detallesPedido, String entrega) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoPedidos, true))) {
            // El código del pedido será el que da el método 
            int codigoPedido = obtenerCodigo();
            // Variable fecha
            String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            // escribir el pedido en el archivo
            bw.write("Codigo Pedido: " + codigoPedido + "\n");
            bw.write("Proveedor: " + proveedor + "\n");
            bw.write("Fecha: " + fecha + "\n");
            bw.write("Detalles del Pedido:\n" + detallesPedido);
            bw.write("Total del Pedido: " + total + "\n");
            bw.write("Tiempo de entrega: " + entrega + "\n"); // Corregido aquí
            bw.write("Estado: Pendiente \n");
            bw.write("-------------------------\n");

        } catch (IOException ex) {
            System.out.println("Error al guardar el pedido.");
        }
    }

    // Método para obtener el último código de pedido
    private static int obtenerCodigo() {
        int codigo = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(archivoPedidos))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                //si la linea inica con hacer
                if (linea.startsWith("Codigo Pedido: ")) {
                    // convirir en cadenas
                    String[] datos = linea.split(":");
                    //el numero sera igual a  Convertir a entero la posicion 1
                    int numero = Integer.parseInt(datos[1].trim());
                    //si el numero es mayor o igual 
                    if (numero >= codigo) {
                        // se incrementa el codigo
                        codigo = numero + 1;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Error al leer el archivo de pedidos.");
        }
        return codigo; //retornamos el codigo ya con la suma
    }

    // metodo para obtener el precio del producto
    public static double obtenerPrecio(String id) {
        double precio = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(archivoAltaProductos))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos[0].equals(id)) {
                    precio = Double.parseDouble(datos[5]); // Obtener el precio del producto
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error al obtener el precio del producto.");
        }
        return precio;
    }

    //metodo de ver si el proveedor existe
    public static boolean proveedorExiste(String proveedor) {
        // Leer el archivo línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(archivoProveedores))) {
            String linea;
            // Recorre todas las líneas del archivo
            while ((linea = br.readLine()) != null) {
                if (linea.equalsIgnoreCase(proveedor)) { //si contiene el nombre
                    return true;
                }
            }
        } catch (IOException excepcion) {
        }
        return false; // Si no se encuentra el producto, devuelve false
    }

    // metodo para mostrar proveedores
    public static void mostrarProveedor() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoProveedores))) {
            String linea;
            System.out.println("PROVEEDORES");
            while ((linea = br.readLine()) != null) {
                System.out.println("- " + linea);
            }
        } catch (IOException ex) {
        }
    }
}
