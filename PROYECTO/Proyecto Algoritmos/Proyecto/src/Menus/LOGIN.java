package Menus;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class LOGIN {

    static String archivoUsuarios = "datos\\RegistroUsuarios.txt";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String usuario, contraseña;

        while (true) { // Agregamos un bucle while
            // Muestra el menú
            System.out.println("           LOGIN             ");
            System.out.println("_____________________________");
            System.out.println("          USUARIOS           ");
            System.out.println("");
            System.out.println("Administrador");
            System.out.println("Vendedor");
            System.out.println("Encargado de bodega");
            System.out.println("-----------------------------");

            System.out.println("INGRESE USUARIO: ");
            System.out.println("si quier salir del programa ingrese SALIR ");
            usuario = scan.nextLine().trim();

            // Verifica si el usuario quiere salir
            if (usuario.equalsIgnoreCase("salir")) {
                System.out.println("saliendo del programa");
                registrarSalida(usuario);
                System.exit(0); // Termina el programa
            }

            // Solicitar y verificar el ingreso de la contraseña
            System.out.println("INGRESE CONTRASENA: ");
            contraseña = scan.nextLine().trim();

            System.out.println("__________________________________");

            // Verificar las credenciales ingresadas
            if (usuario.equalsIgnoreCase("administrador") && contraseña.equals("123")) {
                registrarAcceso(usuario); // Registrar acceso
                System.out.println("Bienvenido Administrador");
                System.out.println("");
                MenuPrincipal.main(new String[0]);

            } else if (usuario.equalsIgnoreCase("vendedor") && contraseña.equals("123")) {
                registrarAcceso(usuario); // Registrar acceso
                System.out.println("Bienvenido Vendedor");
                System.out.println("");
                MenuPrincipal.main(new String[0]);

            } else if (usuario.equalsIgnoreCase("encargado de bodega") && contraseña.equals("123")) {
                registrarAcceso(usuario); // Registrar acceso
                System.out.println("Bienvenido Encargado de bodega");
                System.out.println("");
                MenuPrincipal.main(new String[0]);

            } else {
                System.out.println("Usuario o contrasena incorrectos.");
            }
        }
    }

    // Método para registrar la fecha y hora de acceso
    public static void registrarAcceso(String usuario) {
        try (FileWriter bw = new FileWriter(archivoUsuarios, true)) {
            //variables
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaHoraAcceso = ahora.format(formatter);

            bw.write("Usuario: " + usuario + " - Acceso: " + fechaHoraAcceso);
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de registro.");
        }
    }

    // Método para registrar la fecha y hora de salida
    public static void registrarSalida(String usuario) {
        try (FileWriter bw = new FileWriter(archivoUsuarios, true)) {
            //variales
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaHoraSalida = ahora.format(formatter);
            bw.write(" - Salida: " + fechaHoraSalida + "\n");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de registro.");
        }
    }
}
