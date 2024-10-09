package Pedidos;

import Menus.MenuPedidos;
import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Scanner;

public class GestionPedido {

    // Rutas de archivos
    private static File archivoPedidos = new File("datos\\Pedidos.txt");
    private static File archivoProveedores = new File("datos\\Proveedores.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String id;
        int opcion = 0;
        String nuevoEstado;

        // Encabezado
        System.out.println("   GESTION PEDIDOS   ");
        System.out.println("_____________________");

        // mostrar todos los pedidos
        mostrarPedidos();
        System.out.println("----------------------");

        // Pedir seleccione un pedido
        do {
            System.out.println("- Ingrese codigo de pedido para ver detalles");
            id = scan.nextLine().trim();
            if (id.isEmpty()) {
                //si esta vacio
                System.out.println("El codigo del pedido no puede estar vacío.");
            }
            // si el pedido existe
            if (!pedidoExiste(id)) {
                System.out.println("El codigo (" + id + ") no está registrado.");
                return; // Termina el programa si el pedido no existe
            }
        } while (id.isEmpty());

        //metoo que muestra los detalles de pedido
        mostrarDetallesPedido(id);

        //menu para realizar accio
        System.out.println("Ingrese la accion a realizar");
        System.out.println("1. Modificar Estado");
        System.out.println("2. Cancelar Pedido");

        do {
            opcion = scan.nextInt();
            scan.nextLine(); // Consumir linea

            switch (opcion) {
                case 1:
                    do {
                        // mdificar el estado del pedido
                        System.out.println("- Ingrese el nuevo estado para el pedido (pendiente, en curso, completado):");
                        nuevoEstado = scan.nextLine().trim().toLowerCase();

                        // si el nuevo estado es valido
                        if (nuevoEstado.isEmpty()) {
                            System.out.println("El estado no puede estar vacio.");
                            
                            //que el nuevo estado contenga solo 
                        } else if (nuevoEstado.equals("completado") || nuevoEstado.equals("en curso") || nuevoEstado.equals("pendiente")) {
                            modificarEstadoPedido(id, nuevoEstado);
                            System.out.println("Estado de Pedido (" + id + ") modificado con exito.");
                            MenuPedidos.main(new String [0]);
                            break; 
                        } else {
                            System.out.println("Estado no valido.");
                            nuevoEstado="";
                        }

                    } while (nuevoEstado.isEmpty());
                    break;

                case 2:
                    // Cancelar un pedido
                    System.out.println("- Desea cancelar el pedido? (si/no):");
                    String respuesta = scan.nextLine().trim().toLowerCase();
                    if (respuesta.equals("si")) {
                        //metodo que se borra el pedido
                        cancelarPedido(id);
                    } else {
                        System.out.println("El pedido"+ id +" no ha sido cancelado.");
                    }
                    MenuPedidos.main(new String [0]) ;
                    break;

                default:
                    System.out.println("Opcion no valida. Por favor elija 1 o 2.");
            }
        } while (opcion != 1 && opcion != 2);

    }

    public static void mostrarDetallesPedido(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoPedidos))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                // Verifica si se encontró el pedido
                if (linea.startsWith("Codigo Pedido:") && linea.split(":")[1].trim().equals(id)) {
                    System.out.println("Detalles del Pedido:");
                    System.out.println(linea); // Muestra el código del pedido

                    // leer el bloque de detalles del pedido
                    while ((linea = br.readLine()) != null && !linea.trim().equals("-------------------------")) {
                        System.out.println(linea); // mostrar cada línea del bloque
                    }
                    System.out.println(); // Línea en blanco después de mostrar el pedido
                    break; // Sale del bucle principal después de mostrar el pedido
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de pedidos.");
        }
    }

    // Modificar estado
    public static void modificarEstadoPedido(String id, String nuevoEstado) {
        try {
            // Abrir archivo para leer
            FileReader fr = new FileReader(archivoPedidos);
            BufferedReader br = new BufferedReader(fr);

            // Crear copia
            File archivoCopia = new File("copia_pedidos.txt");
            // abrir archivo copia 
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);
            String linea;

            while ((linea = br.readLine()) != null) {
                //  si se encuentra el pedido por su codigo
                if (linea.startsWith("Codigo Pedido:") && linea.split(":")[1].trim().equals(id)) {
                    // escribir lineas
                    bw.write(linea + "\n");

                    while ((linea = br.readLine()) != null && !linea.startsWith("Estado:")) {
                        bw.write(linea + "\n");
                    }
                    // Escribir el nuevo estado
                    bw.write("Estado: " + nuevoEstado + "\n");
                } else {
                    // Si no es el pedido que queremos modificar lo copiamos
                    bw.write(linea + "\n");
                }
            }

            bw.close();
            br.close();

            // Mover archivo copia al archivo original
            Files.move(archivoCopia.toPath(), archivoPedidos.toPath(), REPLACE_EXISTING);
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no encontrado.");
        } catch (IOException ex) {
            System.out.println("Error al procesar el archivo.");
        }
    }

    // metodo para cancelar un pedido
    public static void cancelarPedido(String id) {
        try {
            // Abrir archivo para leer 
            FileReader fr = new FileReader(archivoPedidos);
            BufferedReader br = new BufferedReader(fr);

            // Crear copia 
            File archivoCopia = new File("copia_pedidos.txt");
            // Abrir archivo copia para escritura 
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);
            String linea;
            boolean pedidoEncontrado = false;

            while ((linea = br.readLine()) != null) {
                // Verificar si se encuentra el pedido por su código
                if (linea.startsWith("Codigo Pedido:") && linea.split(":")[1].trim().equals(id)) {
                    pedidoEncontrado = true;
                    // Si se encuentra el pedido, saltamos todas las líneas hasta el separador
                    while ((linea = br.readLine()) != null && !linea.trim().equals("-------------------------")) {
                    }
                } else {
                    // Se copian los pedidos que no cancelamos
                    bw.write(linea + "\n");
                }
            }
            bw.close();
            br.close();

            // Mover archivo copia al archivo original
            Files.move(archivoCopia.toPath(), archivoPedidos.toPath(), REPLACE_EXISTING);

            if (pedidoEncontrado) {
                System.out.println("El pedido ha sido cancelado.");
            } else {
                System.out.println("No se encontró el pedido con ID: " + id);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no encontrado.");
        } catch (IOException ex) {
            System.out.println("Error al procesar el archivo.");
        }
    }

    public static void mostrarPedidos() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoPedidos))) {
            String linea;
            System.out.println("PEDIDOS EXISTENTES");
            System.out.println("");
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("Codigo Pedido:")) {
                    System.out.println("- " + linea); // Muestra el código del pedido
                }
            }
        } catch (IOException ex) {
            System.out.println("Error al leer el archivo de pedidos."); // Manejo de errores
        }
    }

    public static boolean pedidoExiste(String id) {
        // Leer el archivo línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(archivoPedidos))) {
            String linea;
            // Recorre todas las líneas del archivo
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("Codigo Pedido:") && linea.split(":")[1].trim().equals(id)) {
                    return true;
                }
            }
        } catch (IOException excepcion) {
            System.out.println("Error al leer el archivo de pedidos."); // Manejo de errores
        }
        return false; // Si no se encuentra el pedido, devuelve false
    }
}
