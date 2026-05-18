
package bite;

public class Empleados extends Base{

    private String id;
    
    public Empleados(String nombre, boolean Disponibilidad,String id) {
        super(nombre, Disponibilidad);
        this.id = id;
    }
    public void empezarTurno(){
        super.setDisponibilidad(true);
    }
    
    public void terminarTurno(){
        super.setDisponibilidad(false);
    }
    
}
