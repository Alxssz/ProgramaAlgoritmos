package Menus;

import InformesEstasiticas.Estadisticas;
import InformesEstasiticas.InformeInventarioActual;
import InformesEstasiticas.InformeMovimientos;
import InformesEstasiticas.informePedido;
import java.util.Scanner;

public class MenuEstadisticas {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int opcionG;

        // Mostrar el menú de categorías
        System.out.println("");
        System.out.println("        INFORMES Y ESTADISTICAS       ");
        System.out.println("______________________________________");
        System.out.println("");
        System.out.println("1. Informe Inventario");
        System.out.println("2. Informe Movimientos");
        System.out.println("3. Informe Pedidos");
        System.out.println("4. Estadisticas ");
        System.out.println("5. Salir");
        System.out.println("-------------------------------------");
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
                InformeInventarioActual.main(new String[0]);
                break;
            case 2:
                InformeMovimientos.main(new String[0]);
                break;
            case 3:
                informePedido.main(new String[0]);
                break;
            case 4:
                Estadisticas.main(new String[0]);
                break;
            case 5:
                System.out.println("Volviendo al menu principal...");
                System.out.println("");
                MenuPrincipal.main(new String[0]);
        }
    }
}
