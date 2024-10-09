package Menus;

import ControlExistencia.AlertaStock;
import ControlExistencia.EntradaProductos;
import ControlExistencia.HistorialMovimientos;
import ControlExistencia.VentaProductos;
import java.util.Scanner;

public class MenuExistencias {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int opcionG;

        // Mostrar el menú de categorías
        System.out.println("");
        System.out.println("  CONTROL DE EXISTENCIAS EXISTENCIAS  ");
        System.out.println("______________________________________");
        System.out.println("");
        System.out.println("1. Entrada Productos");
        System.out.println("2. Salida Productos");
        System.out.println("3. Alerta Stock");
        System.out.println("4. Historial Movimiento");
        System.out.println("5. Salir");
        System.out.println("---------------------------------");
        System.out.println("Ingrese una opcion:");

        // Opción seleccionada por el usuario
        opcionG = scan.nextInt();
        scan.nextLine(); // Consumir el salto de línea pendiente

        // Verificar si la opción es válida
        while (opcionG < 1 || opcionG > 5) {
            System.out.println("Opcion no válida, por favor ingrese una opción valida:");
            opcionG = scan.nextInt();
            scan.nextLine(); // Consumir el salto de línea pendiente
        }

        // Procesar la opción válida
        switch (opcionG) {
            case 1:
                EntradaProductos.main(new String[0]);
                break;
            case 2:
                VentaProductos.main(new String[0]);
                break;
            case 3:
                AlertaStock.main(new String[0]);
                break;
            case 4:
                HistorialMovimientos.main(new String[0]);
                break;
            case 5:
                System.out.println("Volviendo al menu principal...");
                System.out.println("");
                MenuPrincipal.main(new String[0]);
        }
    }
}
