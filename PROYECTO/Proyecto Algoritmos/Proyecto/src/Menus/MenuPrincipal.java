package Menus;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipal {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int opcionMenu;

        while (true) {
            try {
                // Muestra el menú
                System.out.println("       MENU PRINCIPAL       ");
                System.out.println("_____________________________");
                System.out.println("");
                System.out.println("1. Gestion de productos");
                System.out.println("2. Control de existencias");
                System.out.println("3. Pedidos de compra");
                System.out.println("4. Informes y estadisticas");
                System.out.println("----------------------------");
                System.out.println("Ingrese una opcion:");

                // Opción seleccionada por el usuario
                opcionMenu = scan.nextInt();
                // Consumir el salto de línea pendiente
                scan.nextLine();

                // Verificar si la opción es válida
                while (opcionMenu < 1 || opcionMenu > 5) {
                    System.out.println("");
                    System.out.println("Opcion no valida, por favor ingrese una opcion valida.");
                    opcionMenu = scan.nextInt();
                    scan.nextLine();
                }

                // Procesar la opción válida
                switch (opcionMenu) {
                    case 1:
                        MenuGestionProductos.main(new String[0]);
                        break;
                    case 2:
                        System.out.println("");
                        System.out.println("control existencias");
                        System.out.println("______________________________");
                    case 3:
                        System.out.println("");
                        System.out.println("BIENVENIDO A PEDIDOS DE COMPRA");
                        System.out.println("______________________________");
                        break;
                    case 4:
                        System.out.println("");
                        System.out.println("BIENVENIDO A INFORMES Y ESTADÍSTICAS");
                        System.out.println("____________________________________");
                        break;

                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero entero.");
                System.out.println("");
                scan.nextLine(); // Limpiar el buffer
            }
        }
    }
}
