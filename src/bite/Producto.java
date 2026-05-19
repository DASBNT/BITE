package bite;

import java.io.Serializable;

public class Producto extends Base implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tipo;
    private double precio;

    public Producto(String tipo, double precio, String nombre, boolean Disponibilidad) {
        super(nombre, Disponibilidad);
        this.tipo = tipo;
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return getNombre() + " [" + tipo + "] $" + String.format("%.2f", precio) + " (" + (isDisponibilidad() ? "Disponible" : "No disponible") + ")";
    }
}
