package GestiondeProductos.Productos;

//importaron metodos
import static GestiondeProductos.Caracteristicas.AgregarCaracteristica.caracteristicaExiste;
import static GestiondeProductos.Caracteristicas.AgregarCaracteristica.caracteristicasExistentes;
import static GestiondeProductos.CategoriaProducto.AgregarCategoria.categoriasExistentes;
import static GestiondeProductos.CategoriaProducto.modificarCategoria.categoriaExiste;
import static GestiondeProductos.Especificaciones.AgregarEspecificacion.especificacionesExistentes;

import java.io.*;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Scanner;

public class CrearProducto {
//rutas de archivos
    private static File archivoCategorias = new File("datos\\Categorias.txt");
    private static File archivoCaracteristicas = new File("datos\\Caracteristicas.txt");
    private static File archivoEspecificaciones = new File("datos\\Especificaciones.txt");
    private static File archivoProductos = new File("datos\\Productos.txt");

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String nombreProducto;
        String nombreCategoria;
        String caracteristicasSeleccionadas = "";
        String especificacionesSeleccionadas = "";
        String valoresCaracteristicas = "";
        String valoresEspecificaciones = "";

        // Crear archivo de productos si no existe
        try {
            if (archivoProductos.createNewFile()) {
                System.out.println("Archivo de productos creado con exito");
            }
        } catch (IOException e) {
            System.out.println("Error al crear el archivo de productos.");
        }

        // encabezado
        System.out.println("     CREAR PRODUCTO      ");
        System.out.println("_________________________");

        // Elegir la categoria 
        System.out.println("");
        System.out.println("Selecciona una categoria");
        categoriasExistentes();

        while (true) {
            System.out.println("-Ingrese el nombre de la categoria");
            nombreCategoria = scan.nextLine().trim();

            //si la categoria existe 
            if (categoriaExiste(nombreCategoria)) {
                break;
            } else {
                System.out.println("El nombre de la categoria no es valido.");
            }
        }

        // Pedir el nombre del producto
        do {
            System.out.println("- Ingresa el nombre del producto:");
            nombreProducto = scan.nextLine().trim();
            if (nombreProducto.isEmpty()) {
                System.out.println("El nombre del producto no puede estar vacío.");
            }
        } while (nombreProducto.isEmpty());

        // Elegir las características
        System.out.println("");
        System.out.println("Selecciona las caracteristicas del producto:");
        caracteristicasExistentes();

        while (true) {
            System.out.println("- Ingrese el nombre de la caracteristica (o escribe FIN para terminar):");
            String caracteristica = scan.nextLine().trim();

            //si se escribe fin ya no se adjuntan mas caracrteristicas
            if (caracteristica.equalsIgnoreCase("fin")) {
                break;

            } else if (caracteristicaExiste(caracteristica)) {
                //pide iungresar el valor de la caracteristica
                System.out.println("- Ingresa el valor para la caracteristica (" + caracteristica + "):");
                String valorCaracteristica = scan.nextLine().trim();

                // Concatenar características y valores como una cadena
                caracteristicasSeleccionadas += caracteristica + "-";
                valoresCaracteristicas += valorCaracteristica + "|";
            } else {
                System.out.println("La característica no es valida.");
            }
        }

        // Elegir las especificaciones
        System.out.println("");
        System.out.println("Selecciona las especificaciones del producto:");
        especificacionesExistentes();

        while (true) {
            System.out.println("- Ingresa el nombre de la especificacion (o escribe FIN para terminar):");
            String especificacion = scan.nextLine().trim();

            //si se escrube fin ya no se adjuntan mas especificaciones
            if (especificacion.equalsIgnoreCase("FIN")) {
                break;
              // que la especificacion no este vacia 
            } else if (!especificacion.isEmpty()) {
                
                //obtener el tipo de dato
                String tipoEspecificacion = obtenerTipoEspecificacion(especificacion); // Obtener el tipo de especificación

                if (tipoEspecificacion != null) {
                    String valorEspecificacion = "";

                    // Validar el tipo de especificación 
                    switch (tipoEspecificacion.toLowerCase()) {
                        case "texto":
                            System.out.println("- Ingresa un valor de tipo texto:");
                            valorEspecificacion = scan.nextLine().trim();
                            break;
                        case "numero":
                            System.out.println("- Ingresa un valor de tipo numero:");
                            valorEspecificacion = scan.nextLine().trim();
                            if (!valorEspecificacion.matches("\\d+")) { // Validar que sea un número
                                System.out.println("El valor ingresado no es un número válido.");
                                continue; // Regresar al inicio del bucle
                            }
                            break;
                        case "fecha":
                            System.out.println("- Ingresa un valor de tipo fecha (YYYY-MM-DD):");
                            valorEspecificacion = scan.nextLine().trim();
                            if (!valorEspecificacion.matches("\\d{4}-\\d{2}-\\d{2}")) { // Validar formato de fecha
                                System.out.println("El valor ingresado no es una fecha válida.");
                                continue; // Regresar al inicio del bucle
                            }
                            break;
                        default:
                            System.out.println("Tipo de especificación desconocido.");
                            continue; // Regresar al inicio del bucle
                    }

                    // Concatenar especificaciones y valores
                    especificacionesSeleccionadas += especificacion + "-";
                    valoresEspecificaciones += valorEspecificacion + "|";
                } else {
                    System.out.println("La especificación no es válida.");
                }
            } else {
                System.out.println("La especificacion no es valida.");
            }
        }

        // Guardar el producto
        guardarProducto(nombreProducto, nombreCategoria, caracteristicasSeleccionadas, valoresCaracteristicas, especificacionesSeleccionadas, valoresEspecificaciones);
        System.out.println("El producto (" + nombreProducto + ") ha sido agregado con éxito!");
    }

    
    // Método para obtener el tipo de la especificación
    private static String obtenerTipoEspecificacion(String nombreEspecificacion) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoEspecificaciones))) {
            String linea;
            //lee las lineas
            while ((linea = br.readLine()) != null) {
                //convierte los datos a cadenas
                String[] datos = linea.split("\\|");
                //si la posicion 0 tiene la especificacion
                if (datos[0].equalsIgnoreCase(nombreEspecificacion)) {
                   //retorna el tipo de dato de la posicion 2
                   return datos[2];
                }
            }
        } catch (IOException e) {
        }
        return null; // Retorna null si no se encuentra
    }

    // Guardar el producto en el archivo
    public static void guardarProducto(String nombreProducto, String categoria, String caracteristicas, String valoresCaracteristicas,
            String especificaciones, String valoresEspecificaciones) {
        try {
            FileReader fr = new FileReader(archivoProductos);
            BufferedReader br = new BufferedReader(fr);

            // Crear copia del archivo
            File archivoCopia = new File("copia_Productos.txt");
            FileWriter fw = new FileWriter(archivoCopia);
            BufferedWriter bw = new BufferedWriter(fw);
            String linea;

            // Copiar contenido existente
            while ((linea = br.readLine()) != null) {
                bw.write(linea + "\n");
            }

            // Escribir el nuevo producto
            String nuevoProducto = nombreProducto + "|" + categoria + "|" + caracteristicas + valoresCaracteristicas + especificaciones + valoresEspecificaciones;
            bw.write(nuevoProducto + "\n");

            bw.close();
            br.close();

            // Reemplazar el archivo original con la copia
            Files.move(archivoCopia.toPath(), archivoProductos.toPath(), REPLACE_EXISTING);
        } catch (IOException ex) {
        }
    }

    // Mostrar las opciones de un archivo
    public static void mostrarOpciones(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int contador = 1;
             //leer linea 
            while ((linea = br.readLine()) != null) {
                //separar en cadena
                String[] datos = linea.split("\\|");
                
                //mostrar el contador y aumento del mismo + los datos en posicion 0
                System.out.println(contador + ". " + datos[0]);
                contador++;
            }
        } catch (IOException e) {
        }
    }
}
