
package bite;

public abstract class Base {
    private String nombre;
    private boolean Disponibilidad;

    public Base(String nombre, boolean Disponibilidad) {
        this.nombre = nombre;
        this.Disponibilidad = Disponibilidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isDisponibilidad() {
        return Disponibilidad;
    }

    public void setDisponibilidad(boolean Disponibilidad) {
        this.Disponibilidad = Disponibilidad;
    }
    
}
