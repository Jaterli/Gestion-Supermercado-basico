import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Bebida extends Producto implements EsAlimento, Descuento {
    protected double volumen;
    protected int calorias;
    protected boolean descuentoAplicado;

    public Bebida(String nombre, double precio, LocalDate fechaCaducidad, int calorias, double volumen) {
        super(nombre, precio, fechaCaducidad);
        this.volumen = volumen;
        this.calorias = calorias;
        this.descuentoAplicado = false;
    }

    public double getVolumen() {
        return volumen;
    }

    public void setVolumen(double volumen) {
        this.volumen = volumen;
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
        return calorias;
    }

    @Override
    public String toString(){
        return "Bebida: "+nombre+", precio $"+precio+", fecha de caducidad: "+fechaCaducidad+", calorias: "+calorias+", volumen: "+volumen ;
    }
}
