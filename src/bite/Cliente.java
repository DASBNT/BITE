package bite;

public class Cliente extends Base {

    public Cliente(String nombre, boolean Disponibilidad) {
        super(nombre, Disponibilidad);
    }

    @Override
    public String toString() {
        return getNombre() + " (" + (isDisponibilidad() ? "Disponible" : "No disponible") + ")";
    }
}
