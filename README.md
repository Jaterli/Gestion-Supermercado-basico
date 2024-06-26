# Gestión Supermercado

Este es un sistema para gestionar los productos de un supermercado. Implementa una jerarquía de clases que incluye una clase abstracta `Producto` con subclases `Bebida`, `ProductoLimpieza` y `Carne`. Además, utiliza interfaces para representar características especiales de los productos.

## Funcionalidades

1. **Clase Abstracta `Producto`**:
    - Representa los productos que se venden en el supermercado.
    - Propiedades: `nombre`, `precio` y `fechaCaducidad`.
    - Métodos para establecer y obtener estas propiedades.

2. **Subclases de `Producto`**:
    - `Bebida`: Propiedades adicionales `volumen` y `calorías`.
    - `ProductoLimpieza`: No tiene propiedades adicionales.
    - `Carne`: Propiedad adicional `calorías`.

3. **Interfaces**:
    - `Descuento`: Incluye métodos para aplicar descuentos a los productos.
    - `EsAlimento`: Indica si un producto es un alimento (si tiene calorías > 0) y devuelve un booleano.

4. **Programa Interactivo**:
    - Permite al usuario crear objetos de productos según su elección.
    - Solicita detalles de los productos por consola.
    - Maneja entradas inválidas lanzando excepciones adecuadas y proporcionando mensajes informativos.
    - Permite listar los productos creados.
    - Calcula y muestra el costo total de los productos que son alimentos y los que no lo son.
    - Implementa una regla de negocio: las carnes con dos días o menos de caducidad tienen un descuento del 20%.

## Instrucciones

### Clases y Estructura del Código

#### Clase Abstracta `Producto`
```java
public abstract class Producto {
    protected String nombre;
    protected double precio;
    protected LocalDate fechaCaducidad;

    public Producto(String nombre, double precio, LocalDate fechaCaducidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.fechaCaducidad = fechaCaducidad;
    }

    // Métodos getter y setter
}


#### Subclases de `Producto`

##### `Bebida`
```java
public class Bebida extends Producto implements EsAlimento, Descuento {
    private double volumen;
    private int calorias;

    public Bebida(String nombre, double precio, LocalDate fechaCaducidad, int calorias, double volumen) {
        super(nombre, precio, fechaCaducidad);
        this.calorias = calorias;
        this.volumen = volumen;
    }

    // Métodos getter y setter, implementación de interfaces
}
```

##### `ProductoLimpieza`
```java
public class ProductoLimpieza extends Producto {
    public ProductoLimpieza(String nombre, double precio, LocalDate fechaCaducidad) {
        super(nombre, precio, fechaCaducidad);
    }
}
```

##### `Carne`
```java
public class Carne extends Producto implements EsAlimento, Descuento {
    private int calorias;

    public Carne(String nombre, double precio, LocalDate fechaCaducidad, int calorias) {
        super(nombre, precio, fechaCaducidad);
        this.calorias = calorias;
    }

    @Override
    public void aplicarDescuento() {
        if (ChronoUnit.DAYS.between(LocalDate.now(), fechaCaducidad) <= 2) {
            precio *= 0.8; // Aplicar un 20% de descuento
        }
    }

    // Métodos getter y setter, implementación de interfaces
}
```

#### Interfaces

##### `Descuento`
```java
public interface Descuento {
    void aplicarDescuento();
}
```

##### `EsAlimento`
```java
public interface EsAlimento {
    boolean esAlimento();
    int getCalorias();
}
```

### Programa Principal

```java
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Producto> productos = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.printf("\nIngrese el tipo de producto que quiere crear: " +
                    "(1=bebida, 2=producto de limpieza, 3=carne, 4=listar productos) o 5=salir para terminar: ");

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
                continue;
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

            System.out.println("Ingrese el nombre del producto: ");
            String nombre = scanner.nextLine();

            double precio = solicitarDouble(scanner, "Ingrese el precio del producto: ");
            if (precio == -1) continue;

            LocalDate fechaCaducidad = solicitarFecha(scanner, "Ingrese la fecha de caducidad del producto (YYYY-MM-DD): ");
            if (fechaCaducidad == null) continue;

            Producto producto = null;
            switch (tipoProducto) {
                case 1:
                    int caloriasBebida = solicitarInt(scanner, "Ingrese las calorias de la bebida: ");
                    if (caloriasBebida == -1) continue;

                    double volumen = solicitarDouble(scanner, "Ingrese el volumen en litros: ");
                    if (volumen == -1) continue;

                    producto = new Bebida(nombre, precio, fechaCaducidad, caloriasBebida, volumen);
                    break;
                case 2:
                    producto = new ProductoLimpieza(nombre, precio, fechaCaducidad);
                    break;
                case 3:
                    int caloriasCarne = solicitarInt(scanner, "Ingrese las calorias de la carne: ");
                    if (caloriasCarne == -1) continue;

                    producto = new Carne(nombre, precio, fechaCaducidad, caloriasCarne);
                    break;
            }

            productos.add(producto);
            System.out.println("Producto creado: " + producto);
        }

        calcularYMostrarCostos(productos);
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
            if (producto instanceof Carne) {
                ((Carne) producto).aplicarDescuento();
            }
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
```

## Ejecución

Para ejecutar el programa, compila y ejecuta el archivo `Main.java`. Sigue las instrucciones en la consola para crear productos y gestionar la lista de productos del supermercado.

```sh
javac Main.java
java Main
```

## Contribuciones

¡Las contribuciones son bienvenidas! Si encuentras algún problema o tienes alguna sugerencia, no dudes en abrir un issue o enviar un pull request.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo `LICENSE` para obtener más información.
```

Este `README.md` proporciona una descripción completa y clara del proyecto, sus funcionalidades, y cómo utilizarlo, asegurando que los desarrolladores puedan entender y colaborar fácilmente.
