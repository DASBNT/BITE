
package bite;
import java.util.LinkedList;
public class Mesero extends Empleados{

    private LinkedList<Mesa> asignaciones = new LinkedList<> ();
    
    public Mesero(String nombre, boolean Disponibilidad, String id) {
        super(nombre, Disponibilidad, id);
    }
    public void asignarMesa(int mesa, Restaurante restaurante){
        asignaciones.add(restaurante.getMesa(mesa));
    }
    public void desasignarMesa(int mesa, Restaurante restaurante){
        asignaciones.remove(restaurante.getMesa(mesa));
    }
}
