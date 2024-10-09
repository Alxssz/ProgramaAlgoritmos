package Pedidos;

import static ControlExistencia.EntradaProductos.guardarProducto;
import static GestiondeProductos.Productos.BajaProductos.productosExistentes;
import static GestiondeProductos.Productos.ModificacionAltaProducto.productoExiste;
import Menus.MenuPedidos;
import static Pedidos.GestionPedido.mostrarDetallesPedido;
import static Pedidos.GestionPedido.mostrarPedidos;
import static Pedidos.GestionPedido.pedidoExiste;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecepcionPedido {

    // Rutas de archivos
    private static File archivoProductos = new File("datos\\AltaProductos.txt");
    private static File archivoPedidos = new File("datos\\Pedidos.txt");
    private static File archivoProveedores = new File("datos\\Proveedores.txt");
    private static File archivoRecepcion = new File("datos\\Recepcion.txt"); // Archivo para registro de recepción

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String id;
        int cantidadRecibida;
        int cantidadIngresada;

        // Encabezado
        System.out.println("   RECEPCION PEDIDOS   ");
        System.out.println("_______________________");

        // Mostrar todos los pedidos
        mostrarPedidos();
        System.out.println("----------------------");

        // seleccione un pedido
        do {
            System.out.println("- Ingrese codigo de pedido para ver detalles");
            id = scan.nextLine().trim();
            if (id.isEmpty()) {
                System.out.println("El codigo del pedido no puede estar vacío.");
            }
            // Verificar si el pedido existe
            if (!pedidoExiste(id)) {
                System.out.println("El codigo (" + id + ") no está registrado.");
                return; // Termina el programa si el pedido no existe
            }
        } while (id.isEmpty());

        boolean continuar = true; // controlar el bucle

        while (continuar) {
            // mostrar detalles del pedido
            mostrarDetallesPedido(id);

            // ingresar cantidad recibida
            do {
                System.out.println("Ingrese la cantidad recibida para el producto del pedido " + id);
                cantidadRecibida = scan.nextInt();
                if (cantidadRecibida < 0) {
                    System.out.println("Ingrese una cantidad positiva.");
                }
            } while (cantidadRecibida < 0);

            // verificar cantidades
            int cantidadSolicitada = obtenerCantidadSolicitada(id); // lllamamos metodo para obtener la cantidad solicitada
            if (cantidadRecibida > cantidadSolicitada) {
                //si hay exceso
                System.out.println("Exceso de productos recibidos. Se esperaba: " + cantidadSolicitada + ", se recibieron: " + cantidadRecibida);
                MenuPedidos.main(new String [0]);
            } else if (cantidadRecibida < cantidadSolicitada) {
                //si faltan
                System.out.println("Faltan productos. Se esperaba: " + cantidadSolicitada + ", se recibieron: " + cantidadRecibida);
                MenuPedidos.main(new String [0]);
            } else {
                //si la cantidad recibida es corrects
                System.out.println("Cantidad recibida correcta, Actualizando estado del pedido a (completado).");
                modificarEstadoPedido(id);
                scan.nextLine();
                productosExistentes();

                // Pedir el ID del producto
                String idProducto;  
                do {
                    System.out.println("- Ingrese el ID del producto:");
                    idProducto = scan.nextLine().trim();
                    if (idProducto.isEmpty()) {
                        System.out.println("El ID del producto no puede estar vacío.");
                    }
                } while (idProducto.isEmpty());

                // si el producto existe
                if (!productoExiste(idProducto)) {
                    System.out.println("El ID (" + idProducto + ") no está registrado.");
                    return; // Termina el programa si el producto no existe
                }

                // cantidad a ingresar
                do {
                    System.out.println("- Ingrese la cantidad de producto a ingresar:");
                    while (!scan.hasNextInt()) {
                        System.out.println("Por favor, ingrese un numero entero positivo.");
                        scan.next(); // Limpiar entrada incorrecta
                    }
                    cantidadIngresada = scan.nextInt();
                    // si la cantidad es negativa
                    if (cantidadIngresada < 0) {
                        System.out.println("La cantidad no puede ser negativa.");
                    }
                } while (cantidadIngresada < 0);

                // metodo guardarProducto
                guardarProducto(id, cantidadIngresada);
                System.out.println("La entrada de producto se ha realizado correctamente");
                System.out.println("");
            }

            // si desea realizar otra entrada de stock
            System.out.println("Realizar otra entrada de stock? (SI/NO):");
            scan.nextLine(); 
            String confirmacion = scan.nextLine().trim().toLowerCase();

            if (confirmacion.equals("no")) {
                continuar = false; // saliir bucle 
                System.out.println("Pedido registrado");
                //regresar al menu
                registrarRecepcion(id, cantidadRecibida);
                MenuPedidos.main(new String[0]);
            }
        }
    }

    // metodo para obtener la cantidad solicitada de un pedido
    private static int obtenerCantidadSolicitada(String id) {
        int cantidad = 0; // Variable para almacenar la cantidad solicitada

        try (BufferedReader br = new BufferedReader(new FileReader(archivoPedidos))) {
            String linea;
            boolean pedidoEncontrado = false; // Bandera para verificar si se encontró el pedido

            while ((linea = br.readLine()) != null) {
                // contiene el código del pedido
                if (linea.startsWith("Codigo Pedido:") && linea.split(":")[1].trim().equals(id)) {
                    pedidoEncontrado = true; //encontrado el pedido
                    // Leer líneas hasta encontrar el total del pedido
                    while ((linea = br.readLine()) != null && !linea.startsWith("Total del Pedido:")) {
                        if (linea.startsWith("ID Producto:")) {
                            // Obtener la cantidad solicitada
                            String[] detalles = linea.split(", ");
                            cantidad += Integer.parseInt(detalles[1].split(": ")[1]); // Sumar cantidades
                        }
                    }
                }
            }

            // si no se encontro el pedido y si se obtuvo alguna cantidad
            if (!pedidoEncontrado) {
                System.out.println("El pedido con codigo " + id + " no fue encontrado.");
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de pedidos.");
        }

        return cantidad; // Retornar la cantidad solicitada
    }

    // metodo para guardar recepcion
    private static void registrarRecepcion(String id, int cantidadRecibida) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoRecepcion, true))) {
            //varibles en fomrato fecha y hora
            String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
            //se escribe la linea
            bw.write("Fecha: " + fecha + "Hora: " + hora + ", Codigo Pedido: " + id + ", Cantidad Recibida: " + cantidadRecibida + "\n");
        } catch (IOException e) {
            System.out.println("Error al registrar la recepcion de productos.");
        }
    }

    // Modificar estado
    public static void modificarEstadoPedido(String id) {
        try {
            // Abrir archivo para leer
            FileReader fr = new FileReader(archivoPedidos);
            BufferedReader br = new BufferedReader(fr);

            // Crear copia
            File archivoCopia = new File("copia_pedidos.txt");
            // Abrir archivo copia 
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);
            String linea;

            while ((linea = br.readLine()) != null) {
                // Verificar si se encuentra el pedido por su codigo en la pocision 1
                if (linea.startsWith("Codigo Pedido:") && linea.split(":")[1].trim().equals(id)) {
                    // esvrinir lineas hasta encontrar el estado
                    bw.write(linea + "\n"); // Escribir la línea del pedido

                    while ((linea = br.readLine()) != null) {
                        // escribit las líneas hasta encontrar el estado
                        if (linea.startsWith("Estado:")) {
                            // escribit el nuevo estado
                            bw.write("Estado: Completado\n");
                            break; // Salir del bucle
                        }
                        bw.write(linea + "\n"); // copiar las líneas anteriores
                    }
                } else {
                    // Si no es el pedido que queremos modificar lo copiamos
                    bw.write(linea + "\n");
                }
            }
            bw.close();
            br.close();

            // archivo copia al archivo original
            Files.move(archivoCopia.toPath(), archivoPedidos.toPath(), REPLACE_EXISTING);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }
}
