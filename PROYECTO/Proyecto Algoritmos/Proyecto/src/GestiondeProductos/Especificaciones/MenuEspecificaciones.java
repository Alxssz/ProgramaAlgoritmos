package GestiondeProductos.Especificaciones;
import java.util.InputMismatchException;
import java.util.Scanner;
import Menus.MenuGestionProductos;

public class MenuEspecificaciones{
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in); 
        int opcion; 

        while (true) {
            try {
                // Mostrar el menú 
                System.out.println("");
                System.out.println("      MENU ESPECIFICACIONES     ");
                System.out.println("________________________________");
                System.out.println("");
                System.out.println("1. Agregar Especificacion");
                System.out.println("2. Modificar Especificacion");
                System.out.println("3. Eliminar Especificacion");
                System.out.println("4. Salir");
                System.out.println("--------------------------------");
                System.out.println("Ingrese una opcion: ");

                // Opción seleccionada por el usuario
                opcion = scan.nextInt();
                
                // Verificar si la opción es válida
                while (opcion < 1 || opcion > 4) {
                    System.out.println("");
                    System.out.println("Opcion no valida, por favor ingrese una opcion valida.");
                    opcion = scan.nextInt();
                }

                // Procesar la opción válida
                switch (opcion) {
                    case 1:
                        System.out.println("");
                        AgregarEspecificacion.main(new String[0]);
                        break;
                    case 2:
                        System.out.println("");
                        ModificarEspecificacion.main(new String[0]);
                        break;
                    case 3:
                        System.out.println("");
                        EliminarEspecificacion.main(new String[0]);
                        break; 
                    case 4:
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
