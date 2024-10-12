package InformesEstasiticas;

import static GestiondeProductos.Productos.BajaProductos.productosExistentes;
import Menus.MenuExistencias;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InformeMovimientos {

    //rutas
    private static File archivoMovimientosStock = new File("datos\\MovimientosStock.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // Pedir al usuario el ID del producto
        productosExistentes();

        System.out.println("Ingrese el ID del producto para generar el informe: ");
        String id = scan.nextLine();

        // Pedir rango de fechas
        System.out.println("Ingrese la fecha de inicio (dd/MM/yyyy): ");
        String fechaInicioStr = scan.nextLine();
        System.out.println("Ingrese la fecha de fin (dd/MM/yyyy): ");
        String fechaFinStr = scan.nextLine();

        Date fechaInicio, fechaFin;
        try {
            fechaInicio = new SimpleDateFormat("dd/MM/yyyy").parse(fechaInicioStr);
            fechaFin = new SimpleDateFormat("dd/MM/yyyy").parse(fechaFinStr);
        } catch (ParseException e) {
            System.out.println("Error: formato de fecha incorrecto.");
            return;
        }

        List<String[]> movimientos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoMovimientosStock))) {
            String linea;
            //inicializamos variable en formato fecha
            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");

            while ((linea = br.readLine()) != null) {
                //separamos en cadenas 
                String[] datos = linea.split("\\|");
                //obtenemos los datos de cada cadena 
                String idProducto = datos[0];
                //obtenemos fecha
                String fechaProducto = datos[1];
                //damos el valor a fechaOperacion el formato en fehca de fecha producto
                Date fechaOperacion = fecha.parse(fechaProducto);
                
                String hora = datos[2];
                String operacion = datos[3];
                String cantidad = datos[4];
                String usuario = datos[5];

                // si el id producto contiene el id y la fecha de operacion no esta antes de la inico y la fecha operacion no esta despues
                if (idProducto.equalsIgnoreCase(id) && !fechaOperacion.before(fechaInicio) && !fechaOperacion.after(fechaFin)) {
                    //a movimientos se agregan los valores obtenidos
                    movimientos.add(new String[]{fechaProducto, hora, operacion, cantidad, usuario});
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error al leer el archivo de movimientos.");
            return;
        }

        
        // s no hay movimientos que mostrar, regresar al men
        if (movimientos.isEmpty()) {
            System.out.println("No hay movimientos que coincidan.");
            System.out.println("Enter para regresar ");
            scan.nextLine();
            MenuExistencias.main(new String[0]);
            return;
        }
        
        // Mostrar el informe en 
        System.out.println("                 INFORME MOVIMIENTOS DE STOCK                 ");
        System.out.println("______________________________________________________________");
        System.out.println("Fecha      |Hora           | Operacion | Cantidad | Usuario ");

        //recorre cada elemento de movimiento y se agrega cada valor 
        for (String[] datos : movimientos) {
            System.out.println(datos[0] + " |" + datos[1] + "       | " + datos[2] + "   | " + datos[3] + "       | " + datos[4]);
        }

        // Preguntar si desea exportar el informe a CSV
        System.out.println("Desea exportar el informe a un archivo CSV? (SI /NO )");
        String confirmacion = scan.nextLine().trim().toLowerCase();

        if (confirmacion.equalsIgnoreCase("si")) {
            try (PrintWriter pw = new PrintWriter(new FileWriter("datos\\informe_movimientos_stock.csv"))) {
                pw.println("Fecha,Hora,Operación,Cantidad,Usuario");

                for (String[] datos : movimientos) {
                    pw.println(datos[0] + "," + datos[1] + "," + datos[2] + "," + datos[3] + "," + datos[4]);
                }

                System.out.println("Informe exportado");
            } catch (IOException e) {
                System.out.println("Error al exportar el informe");
            }
        }

        // Volver al menú principal
        System.out.println("______________________________________________________________");
        System.out.println("Enter para regresar ");
        scan.nextLine();
        MenuExistencias.main(new String[0]);
    }
}
