package bite;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class Mesa {
    private int capacidad;
    private LinkedList<Cliente> puestos = new LinkedList<>();
    
    public Mesa(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getPuestos() {
    return (capacidad - puestos.size()); 
    }
    
    public void EstadoMesa() {
        if (getPuestos() == 0){
            System.out.println("No sobran puestos en la Mesa");
        }
        else {
            System.out.println("Sobran " + getPuestos() + " puestos en la Mesa");
        }
    }
    
    public void addCliente(Cliente cliente){
        puestos.remove(cliente);
    }
    
    public void deleteCliente(Cliente cliente){
        puestos.remove(cliente);
    }
    
}
