package InformesEstasiticas;

import Menus.MenuExistencias;
import static Pedidos.CreacionPedido.mostrarProveedor;
import java.io.*;
import java.util.*;

public class informePedido {

    private static File archivoPedidos = new File("datos\\Pedidos.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String proveedor;

        mostrarProveedor();
        // Pedir al usuario el nombre del proveedor
        System.out.println("Ingrese el nombre del proveedor para generar el informe: ");
        proveedor = scan.nextLine().trim();

        // Mostrar el informe en pantalla
        StringBuilder informe = new StringBuilder(); // Usamos StringBuilder para guardar el informe
        mostrarPedido(proveedor, informe);

        // Preguntar si desea exportar el informe a CSV
        System.out.println("");
        System.out.println("----------------------------------------------------");
        System.out.println("Desea exportar el informe a un archivo CSV? (SI /NO)");
        String respuesta = scan.nextLine();
        if (respuesta.equalsIgnoreCase("si")) {
            try (PrintWriter pw = new PrintWriter(new FileWriter("informe_pedidos.csv"))) {
                pw.println("Numero de Pedido,ID Producto,Cantidad,Precio Unitario,Subtotal");
                pw.println(informe); // Exportamos el informe al CSV

                System.out.println("Informe exportado a informe_pedidos.csv");
            } catch (IOException e) {
                System.out.println("Error al exportar el informe.");
            }
        }

        // Volver al menú principal
        System.out.println("______________________________________________________________");
        System.out.println("Enter para regresar");
        scan.nextLine();
        MenuExistencias.main(new String[0]);
    }

    public static void mostrarPedido(String proveedor, StringBuilder informe) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoPedidos))) {
            String linea;
            boolean mostrarSiguientes = false;

            while ((linea = br.readLine()) != null) {
                // Separar los datos
                String[] datos = linea.split("\\:");

                // Verificar si la línea es un pedido del proveedor
                if (linea.startsWith("Proveedor:") && datos.length > 1 && datos[1].trim().equalsIgnoreCase(proveedor)) {
                    System.out.println(linea); 
                    informe.append(linea).append("\n"); // Agregar al informe
                    mostrarSiguientes = true; 
                    continue; 
                }

                if (mostrarSiguientes) {
                    if (linea.startsWith("Codigo Pedido:")) {
                        // Verificar si hay un nuevo pedido de otro proveedor
                        String[] datosPedido = linea.split("\\:");
                        if (!datosPedido[1].trim().equalsIgnoreCase(proveedor)) {
                            break; // Salir del bucle si encontramos un pedido de otro proveedor
                        }
                    }
                    System.out.println(linea); // Muestra la línea actual
                    informe.append(linea).append("\n"); // Agregar al informe
                }
            }
        } catch (IOException ex) {
            // No se muestra mensaje de error
        }
    }
}
