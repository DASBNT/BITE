
package bite;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class Restaurante {
    private LinkedList<Mesa> mesas = new LinkedList<>();
    
    public void addMesa(int cantidadMesas,int capacidad){
        Mesa e = new Mesa(capacidad);
        for(int i = 0;i<cantidadMesas;i++){
            mesas.add(e);
            //System.out.println("Se ha agregado una Mesa con capacidad de " + capacidad + " personas");
        }
    }
}
