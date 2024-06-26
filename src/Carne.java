import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Carne extends Producto implements Descuento, EsAlimento{
    protected int calorias;
    protected boolean descuentoAplicado;
    public Carne(String nombre, double precio, LocalDate fechaCaducidad, int calorias) {
        super(nombre, precio, fechaCaducidad);
        this.calorias = calorias;
        this.descuentoAplicado = false;
    }

    // Implementación del método aplicarDescuento de la interfaz Descuento
    @Override
    public void aplicarDescuento() {
        if(!descuentoAplicado){
            long diasHastaCaducidad = ChronoUnit.DAYS.between(LocalDate.now(), fechaCaducidad);
            if(diasHastaCaducidad <= 2){
                double descuento = 0.2;
                precio -= precio*descuento;
                descuentoAplicado = true;
            }
        }
    }

    @Override
    public boolean esAlimento() {
        return calorias > 0; // True si se cumple la condición
    }

    @Override
    public int getCalorias() {
        return 0;
    }

    @Override
    public String toString(){
        return "Carne: "+nombre+", precio $"+precio+", fecha de caducidad: "+fechaCaducidad+", calorias: "+calorias ;
    }
}
