package GestiondeProductos.Productos;
//importar metodos
import static GestiondeProductos.Caracteristicas.AgregarCaracteristica.caracteristicaExiste;
import static GestiondeProductos.Caracteristicas.AgregarCaracteristica.caracteristicasExistentes;
import static GestiondeProductos.CategoriaProducto.AgregarCategoria.categoriaExiste;
import static GestiondeProductos.CategoriaProducto.AgregarCategoria.categoriasExistentes;
import static GestiondeProductos.Especificaciones.AgregarEspecificacion.especificacionesExistentes;

import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Scanner;

public class ModificarProducto {

    //rutas de archivos
    private static File archivoProductos = new File("datos\\Productos.txt");
    private static File archivoCaracteristicas = new File("datos\\Caracteristicas.txt");
    private static File archivoEspecificaciones = new File("datos\\Especificaciones.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String nombreActual;
        String nuevaCategoria;

        // encabezado
        System.out.println("     MODIFICAR PRODUCTO      ");
        System.out.println("_____________________________");

        // Mostrar productos existentes
        System.out.println("");
        System.out.println("Selecciona un producto para modificar:");
        mostrarProductosExistentes();
        System.out.println("-------------------------------");

        while (true) {
            //pedir nombre del producto
            System.out.println("-Ingrese el nombre del producto a modificar:");
            nombreActual = scan.nextLine().trim();

            //que no este vacio y que exista
            if (nombreActual.isEmpty()) {
                System.out.println("El nombre del producto no puede estar vacio.");
                System.out.println("");
            } else if (!productoExiste(nombreActual)) {
                System.out.println("El producto (" + nombreActual + ") no existe.");
                System.out.println("");
            } else {
                break;
            }
        }

        // Ingresar nueva categoría
        while (true) {
            //elegir nueva categoria
            categoriasExistentes();
            System.out.println("-Ingrese nueva categoria del producto:");
            nuevaCategoria = scan.nextLine().trim();

            // Validar que la nueva categoría no esté vacía y que exista
            if (nuevaCategoria.isEmpty()) {
                System.out.println("La categoría no puede estar vacía.");
                System.out.println("");
            } else if (!categoriaExiste(nuevaCategoria)) {
                System.out.println("No existe una categoría con ese nombre.");
                System.out.println("");
            } else {
                break;
            }
        }

        // inicializar variables caracteristicas
        String caracteristicasSeleccionadas = "";
        String valoresCaracteristicas = "";

        System.out.println("");
        System.out.println("Selecciona las nuevas características del producto:");
        caracteristicasExistentes();

        while (true) {
            //nuevas Caracteristicas
            System.out.println("- Ingrese el nombre de la característica (o escribe FIN para terminar): ");
            String caracteristica = scan.nextLine().trim();

            if (caracteristica.equalsIgnoreCase("FIN")) {
                break;
                //ingresar su valor
            } else if (caracteristicaExiste(caracteristica)) {
                System.out.println("- Ingresa el nuevo valor para la característica (" + caracteristica + "): ");
                String valorCaracteristica = scan.nextLine().trim();

                // Concatenar características y valores como una cadena
                caracteristicasSeleccionadas += caracteristica + "-";
                valoresCaracteristicas += valorCaracteristica + "|";
            } else {
                System.out.println("La característica no es válida.");
            }
        }

        // inicaializar variables especificaciones
        String especificacionesSeleccionadas = "";
        String valoresEspecificaciones = "";

        System.out.println("");
        System.out.println("Selecciona las nuevas especificaciones del producto:");
        especificacionesExistentes();

        while (true) {
            System.out.println("- Ingrese el nombre de la especificación (o escribe FIN para terminar): ");
            String especificacion = scan.nextLine().trim();

            if (especificacion.equalsIgnoreCase("FIN")) {
                break;
                //que la especificacion no este vacia 
            } else if (!especificacion.isEmpty()) {
                
                //obtener el tipo de dato
                String tipoEspecificacion = obtenerTipoEspecificacion(especificacion);

                if (tipoEspecificacion != null) {
                    String valorEspecificacion = "";
                    //evaluar eleccion
                    switch (tipoEspecificacion.toLowerCase()) {
                        case "texto":
                            System.out.println("- Ingresa un valor de tipo texto: ");
                            valorEspecificacion = scan.nextLine().trim();
                            break;
                        case "numero":
                            System.out.println("- Ingresa un valor de tipo número: ");
                            valorEspecificacion = scan.nextLine().trim();
                            if (!valorEspecificacion.matches("\\d+")) {
                                System.out.println("El valor ingresado no es un número válido.");
                                continue; // Volver a preguntar
                            }
                            break;
                        case "fecha":
                            System.out.println("- Ingresa un valor de tipo fecha (YYYY-MM-DD): ");
                            valorEspecificacion = scan.nextLine().trim();
                            if (!valorEspecificacion.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                System.out.println("El valor ingresado no es una fecha válida.");
                                continue; // Volver a preguntar
                            }
                            break;
                        default:
                            System.out.println("Tipo de especificación desconocido.");
                            continue; // Volver a preguntar
                    }
                    //union de cadenas 
                    especificacionesSeleccionadas += especificacion + "-";
                    valoresEspecificaciones += valorEspecificacion + "|";
                } else {
                    System.out.println("La especificación no es válida.");
                }
            } else {
                System.out.println("La especificación no puede estar vacía.");
            }
        }

        // Llamar al método guardar el producto modificado
        guardarProductoModificado(nombreActual, nuevaCategoria, caracteristicasSeleccionadas, valoresCaracteristicas, especificacionesSeleccionadas, valoresEspecificaciones);
        System.out.println("El producto '" + nombreActual + "' ha sido modificado con éxito!");
    }

    // Obtener el tipo de la especificación
    private static String obtenerTipoEspecificacion(String nombreActual) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoEspecificaciones))) {
            String linea;
            
            //ller linea
            while ((linea = br.readLine()) != null) {
                //Separar en cadenas
                  String[] datos = linea.split("\\|");
                if (datos[0].equalsIgnoreCase(nombreActual)) {
                    return datos[2]; // Retorna el tipo de especificación
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de especificaciones.");
        }
        return null; // Retorna null si no se encuentra
    }

    // Guardar el producto modificado
public static void guardarProductoModificado(String nombreProducto, String categoria, String caracteristicas, String valoresCaracteristicas,
        String especificaciones, String valoresEspecificaciones) {
    try {
        FileReader fr = new FileReader(archivoProductos);
        BufferedReader br = new BufferedReader(fr);

        // Crear copia del archivo
        File archivoCopia = new File("copia_Productos.txt");
        FileWriter fw = new FileWriter(archivoCopia);
        BufferedWriter bw = new BufferedWriter(fw);
        String linea;

        // Copiar contenido existente y modificar el producto correspondiente
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split("\\|");
            if (!datos[0].equals(nombreProducto)) { // Comparar con el nombre del producto
                // Escribir la línea sin cambios
                bw.write(linea + "\n");
            } else {
                // Escribir el producto modificado
                String nuevoProducto = nombreProducto + "|" + categoria + "|" + caracteristicas + valoresCaracteristicas + especificaciones + valoresEspecificaciones;
                bw.write(nuevoProducto + "\n");
            }
        }

        //cierre de archivos
        bw.close();
        br.close();

        // Reemplazar el archivo original con la copia
        Files.move(archivoCopia.toPath(), archivoProductos.toPath(), REPLACE_EXISTING);
    } catch (IOException ex) {
    }
}

    // Mostrar productos existentes
    public static void mostrarProductosExistentes() {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoProductos))) {
            String linea;
            //leer lineas
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                System.out.println("- " + datos[0]); // Mostrar solo el nombre del producto
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de productos.");
        }
    }

    public static boolean productoExiste(String nombreActual) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoProductos))) {
            String linea;
            //leer lineas
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                //si tiene el nombre 
                if (datos[0].equals(nombreActual)) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }
}
