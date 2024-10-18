package Menus;

import Pedidos.CreacionPedido;
import Pedidos.GestionPedido;
import Pedidos.RecepcionPedido;
import java.util.Scanner;

public class MenuPedidos {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int opcionG;

        // Mostrar el menú de categorías
        System.out.println("");
        System.out.println("         PEDIDOS DE COMPRA        ");
        System.out.println("__________________________________");
        System.out.println("");
        System.out.println("1. Crear Pedido ");
        System.out.println("2. Gestionar Pedido");
        System.out.println("3. Recepcion Pedido");
        System.out.println("4. Salir");
        System.out.println("---------------------------------");
        System.out.println("Ingrese una opcion:");

        // Opción seleccionada por el usuario
        opcionG = scan.nextInt();
        scan.nextLine(); // Consumir el salto de línea pendiente

        // Verificar si la opción es válida
        while (opcionG < 1 || opcionG > 4) {
            System.out.println("Opcion no válida, por favor ingrese una opción valida:");
            opcionG = scan.nextInt();
            scan.nextLine(); // Consumir el salto de línea pendiente
        }

        // Procesar la opción válida
        switch (opcionG) {
            case 1:
                CreacionPedido.main(new String[0]);
                break;
            case 2:
                GestionPedido.main(new String[0]);
                break;
            case 3:
                RecepcionPedido.main(new String[0]);
                break;
            case 4:
                System.out.println("Volviendo al menu principal...");
                System.out.println("");
                MenuPrincipal.main(new String[0]);
        }
    }
}
