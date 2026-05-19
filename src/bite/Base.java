
package bite;

import java.io.Serializable;

public abstract class Base implements Serializable {
    private static final long serialVersionUID = 1L;

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
