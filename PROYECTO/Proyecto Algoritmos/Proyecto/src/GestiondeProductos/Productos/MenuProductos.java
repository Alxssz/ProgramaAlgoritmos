package GestiondeProductos.Productos;

import java.util.InputMismatchException;
import java.util.Scanner;
import Menus.MenuGestionProductos;

public class MenuProductos {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int opcion;

        while (true) {
            try {
                // Mostrar el menú 
                System.out.println("");
                System.out.println("          MENU PRODUCTO         ");
                System.out.println("________________________________");
                System.out.println("");
                System.out.println("1. Agregar Producto");
                System.out.println("2. Modificar Producto");
                System.out.println("3. Alta Producto");
                System.out.println("4. Baja Producto");
                System.out.println("5. Modificar Alta Producto");
                System.out.println("6. Salir");
                System.out.println("--------------------------------");
                System.out.println("Ingrese una opcion: ");

                // Opción seleccionada por el usuario
                opcion = scan.nextInt();

                // Verificar si la opción es válida
                while (opcion < 1 || opcion > 6) {
                    System.out.println("");
                    System.out.println("Opcion no valida, por favor ingrese una opcion valida.");
                    opcion = scan.nextInt();
                }

                // Procesar la opción válida
                switch (opcion) {
                    case 1:
                        System.out.println("");
                        CrearProducto.main(new String[0]);
                        break;
                    case 2:
                        System.out.println("");
                        ModificarProducto.main(new String[0]);
                        break;
                    case 3:
                        System.out.println("");
                        AltaProductos.main(new String[0]);
                        break;
                    case 4:
                        System.out.println("");
                        BajaProductos.main(new String[0]);
                        break;
                    case 5:
                        System.out.println("");
                        ModificacionAltaProducto.main(new String[0]);
                        break;
                    case 6:
                        System.out.println("Volviendo..");
                        MenuGestionProductos.main(new String[0]);

                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero entero.");
                System.out.println("");
                scan.nextLine();
            }
        }
    }
}
