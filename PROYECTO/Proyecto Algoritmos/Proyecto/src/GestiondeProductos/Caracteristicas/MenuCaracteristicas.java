package GestiondeProductos.Caracteristicas;
import java.util.InputMismatchException;
import java.util.Scanner;
import Menus.MenuGestionProductos;

public class MenuCaracteristicas {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in); 
        int opcion; 

        while (true) {
            try {
                // Mostrar el menú 
                System.out.println("");
                System.out.println("      MENU CARACTERISTICAS      ");
                System.out.println("________________________________");
                System.out.println("");
                System.out.println("1. Agregar Caracteristica");
                System.out.println("2. Modificar Caracteristica");
                System.out.println("3. Eliminar Caracteristica");
                System.out.println("4. Salir");
                System.out.println("--------------------------------");
                System.out.println("Ingrese una opcion: ");

                // Opción seleccionada
                opcion = scan.nextInt();
                
                // la opcion solo puede ser de 1 al 4
                while (opcion < 1 || opcion > 4) {
                    System.out.println("");
                    System.out.println("Opcion no valida, ingrese una opcion valida");
                    opcion = scan.nextInt();
                }

                // procesar de acuerdio a la opcion 
                switch (opcion) {
                    case 1:
                        System.out.println("");
                        AgregarCaracteristica.main(new String[0]);
                        break;
                    case 2:
                        System.out.println("");
                        modificarCaracteristica.main(new String[0]);
                        break;
                    case 3:
                        System.out.println("");
                        EliminarCaracteristica.main(new String[0]);
                        break; 
                    case 4:
                        //volver al menu de gestion productos
                        System.out.println("Volviendo..");
                        MenuGestionProductos.main(new String[0]);
                }
                
            // Mensaje si ingresa signos o numeros negativos
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero entero.");
                System.out.println("");
                scan.nextLine();
            }
        }
    }
}
