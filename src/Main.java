import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Crear una lista para almacenar productos
        List<Producto> productos = new ArrayList<>();

        // Crear un objeto Scanner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.printf("\nIngrese el tipo de producto que quiere crear: " +
                    "(1=bebida, 2=producto de limpieza, 3=carne, 4=listar productos) o 5=salir para terminar: ");

            // Validar la entrada para el tipo de producto
            int tipoProducto = -1;
            try {
                tipoProducto = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ENTRADA NO VÁLIDA, por favor ingrese el número asociado al producto.");
                continue;
            }

            if (tipoProducto == 5) {
                System.out.println("El programa ha finalizado");
                break;
            } else if (tipoProducto < 1 || tipoProducto > 4) {
                System.out.println("El producto elegido no existe, vuelva a intentarlo");
                continue; // Saltar a la siguiente iteración del bucle
            } else if (tipoProducto == 4) {
                if (productos.size() > 0) {
                    System.out.println("LISTA DE PRODUCTOS");
                    for (Producto producto : productos) {
                        System.out.println(producto);
                    }
                } else {
                    System.out.println("La lista está vacía");
                }
                continue;
            }

            // Solicitar detalles del producto al usuario
            System.out.println("Ingrese el nombre del producto: ");
            String nombre = scanner.nextLine();

            double precio = solicitarDouble(scanner, "Ingrese el precio del producto: ");
            if (precio == -1) continue; // Si la entrada es inválida, continuar con la siguiente iteración

            LocalDate fechaCaducidad = solicitarFecha(scanner, "Ingrese la fecha de caducidad del producto (YYYY-MM-DD): ");
            if (fechaCaducidad == null) continue; // Si la entrada es inválida, continuar con la siguiente iteración

            Producto producto = null; // Principio de polimorfismo
            switch (tipoProducto) {
                case 1:
                    int caloriasBebida = solicitarInt(scanner, "Ingrese las calorias de la bebida: ");
                    if (caloriasBebida == -1) continue; // Si la entrada es inválida, continuar con la siguiente iteración

                    double volumen = solicitarDouble(scanner, "Ingrese el volumen en litros: ");
                    if (volumen == -1) continue; // Si la entrada es inválida, continuar con la siguiente iteración

                    producto = new Bebida(nombre, precio, fechaCaducidad, caloriasBebida, volumen);
                    break;
                case 2:
                    producto = new ProductoLimpieza(nombre, precio, fechaCaducidad);
                    break;
                case 3:
                    int caloriasCarne = solicitarInt(scanner, "Ingrese las calorias de la carne: ");
                    if (caloriasCarne == -1) continue; // Si la entrada es inválida, continuar con la siguiente iteración

                    producto = new Carne(nombre, precio, fechaCaducidad, caloriasCarne);
                    break;
            }

            // Añadir el producto a la lista
            productos.add(producto);

            // Imprimir el producto creado
            System.out.println("Producto creado: " + producto);
        }

        // Calcular y mostrar los costos totales
        calcularYMostrarCostos(productos);

        // Cerrar el scanner
        scanner.close();
    }

    private static double solicitarDouble(Scanner scanner, String mensaje) {
        System.out.println(mensaje);
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("VALOR NO VÁLIDO, por favor ingrese un número.");
            return -1;
        }
    }

    private static int solicitarInt(Scanner scanner, String mensaje) {
        System.out.println(mensaje);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("VALOR NO VÁLIDO, por favor ingrese un número.");
            return -1;
        }
    }

    private static LocalDate solicitarFecha(Scanner scanner, String mensaje) {
        System.out.println(mensaje);
        try {
            return LocalDate.parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("FECHA NO VÁLIDA, por favor ingrese una fecha en el formato YYYY-MM-DD.");
            return null;
        }
    }

    private static void calcularYMostrarCostos(List<Producto> productos) {
        double totalCostoAlimentos = 0;
        double totalCostoNoAlimentos = 0;

        for (Producto producto : productos) {
            // Aplicar descuento en caso de que fuera de aplicación
            if (producto instanceof Carne) {
                ((Carne) producto).aplicarDescuento();
            }
            // Si es alimento sumo a totalCostoAlimentos el precio del producto
            if (producto instanceof EsAlimento && ((EsAlimento) producto).esAlimento()) {
                totalCostoAlimentos += producto.getPrecio();
            } else {
                totalCostoNoAlimentos += producto.getPrecio();
            }
        }

        System.out.println("\nTotal costo productos (alimentos): " + totalCostoAlimentos);
        System.out.println("Total costo productos (no alimentos): " + totalCostoNoAlimentos);
        System.out.println("Total costo productos (todos): " + (totalCostoAlimentos + totalCostoNoAlimentos));
    }
}
