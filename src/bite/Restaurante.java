
package bite;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class Restaurante {
    private ArrayList<Mesa> mesas = new ArrayList<>();
    
    public void addMesa(int cantidadMesas,int capacidad){
        Mesa e = new Mesa(capacidad);
        for(int i = 0;i<cantidadMesas;i++){
            mesas.add(e);
        }
    }
        public void deleteMesa(int cantidadMesas,int capacidad){
        Mesa e = new Mesa(capacidad);
        for(int i = 0;i<cantidadMesas;i++){
            mesas.remove(e);
        }
    }
}
