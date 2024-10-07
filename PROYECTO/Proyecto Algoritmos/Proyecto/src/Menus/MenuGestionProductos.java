package Menus;

import GestiondeProductos.Caracteristicas.MenuCaracteristicas;
import GestiondeProductos.CategoriaProducto.MenuCategoria;
import GestiondeProductos.Especificaciones.MenuEspecificaciones;
import GestiondeProductos.Productos.MenuProductos;
import java.util.Scanner;

public class MenuGestionProductos {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int opcionG;

        // Mostrar el menú de categorías
        System.out.println("");
        System.out.println("       GESTION DE PRODUCTOS      ");
        System.out.println("_________________________________");
        System.out.println("");
        System.out.println("1. Categorias producto");
        System.out.println("2. Caracteristicas producto");
        System.out.println("3. Especificaciones de producto");
        System.out.println("4. Productos");
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
                MenuCategoria.main(new String[0]);
                break;
            case 2:
                MenuCaracteristicas.main(new String[0]);
                break;
            case 3:
                MenuEspecificaciones.main(new String[0]);
                break;
            case 4:
                MenuProductos.main(new String[0]);
                break;
            case 5:
                System.out.println("Volviendo al menu principal...");
                System.out.println("");
                MenuPrincipal.main(new String[0]);
        }
    }
}
