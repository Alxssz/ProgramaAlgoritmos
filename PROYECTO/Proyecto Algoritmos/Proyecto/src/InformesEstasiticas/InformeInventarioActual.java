package InformesEstasiticas;

import Menus.MenuExistencias;
import java.io.*;
import java.util.*;

public class InformeInventarioActual {

    private static File archivoAltaProductos = new File("datos\\AltaProductos.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Leer todos los productos y almacenarlos en una lista
        List<String[]> productos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoAltaProductos))) {
            String linea;

            // filtre por categoria
            System.out.println("Ingrese la categoria a filtrar (dejar vaco para mostrar todo): ");
            String categoria = scan.nextLine();

            // Leer y filtrar productos por categoría
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (!datos[0].isEmpty()) {
                    String categoriaProducto = datos[2]; // Asumiendo que la categoría está en el índice 2

                    // Si el filtro está vacío o la categoría coincide, se agrega a la lista
                    if (categoria.isEmpty() || categoriaProducto.equalsIgnoreCase(categoria)) {
                        productos.add(datos);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de productos.");
        }

        // Si no hay productos para mostrar, regresar al menú
        if (productos.isEmpty()) {
            System.out.println("No hay productos que coincidan.");
            System.out.println("Enter para regresar ");
            scan.nextLine();
            MenuExistencias.main(new String[0]);
            return;
        }

        // Pedir al usuario el criterio de ordenación
        System.out.println("Seleccione el criterio de ordenación: 1 - Nombre, 2 - Stock, 3 - Valor Total del Stock");
        System.out.println("1. Nombre");
        System.out.println("2. Stock");
        System.out.println("3. Valor Total del Stock");
        int criterio = Integer.parseInt(scan.nextLine());

        // Ordenar los productos según el criterio seleccionado
        Comparator<String[]> comparador;
        switch (criterio) {
            case 1:
                comparador = Comparator.comparing(o -> o[1]); // Ordenar por nombre (índice 1)
                break;
            case 2:
                comparador = Comparator.comparingInt(o -> Integer.parseInt(o[5])); // Ordenar por stock (índice 5)
                break;
            case 3:
                comparador = Comparator.comparingDouble(o -> Double.parseDouble(o[4]) * Integer.parseInt(o[5])); // Ordenar por valor total
                break;
            default:
                comparador = Comparator.comparing(o -> o[1]); // Por defecto, ordenar por nombre
        }

        productos.sort(comparador);

        // Mostrar el informe en pantalla
        System.out.println("                 INFORME INVENTARIO ACTUAL                    ");
        System.out.println("______________________________________________________________");
        System.out.println("Producto    | Categoria      | Stock  | Valor Stock | Reorden ");

        for (String[] datos : productos) {
            double precio = Double.parseDouble(datos[4]);
            int stock = Integer.parseInt(datos[5]);
            double valorStock = precio * stock;
            System.out.println(datos[1] + "      | " + datos[2] + "           | " + datos[5] + "     | " + valorStock + "      | " + datos[6]);
        }

        // Preguntar si desea exportar el informe a CSV
        System.out.println("¿Desea exportar el informe a un archivo CSV? (S/N)");
        String respuesta = scan.nextLine();

        if (respuesta.equalsIgnoreCase("Si")) {
            try (PrintWriter pw = new PrintWriter(new FileWriter("informe_inventario.csv"))) {
                pw.println("Producto,Categoria,Stock,ValorStock,Reorden");

                for (String[] datos : productos) {
                    double precio = Double.parseDouble(datos[4]);
                    int stock = Integer.parseInt(datos[5]);
                    double valorStock = precio * stock;
                    pw.println(datos[1] + "," + datos[2] + "," + datos[5] + "," + valorStock + "," + datos[6]);
                }

                System.out.println("Informe exportado a informe_inventario.csv");
            } catch (IOException e) {
                System.out.println("Error al exportar el informe.");
            }
        }

        // Volver al menú principal
        System.out.println("______________________________________________________________");
        System.out.println("Enter para regresar ");
        scan.nextLine();
        MenuExistencias.main(new String[0]);
    }
}
