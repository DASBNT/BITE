package bite;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Mesa implements Serializable {
    private static final long serialVersionUID = 1L;
    private int capacidad;
    private transient LinkedList<Cliente> puestos = new LinkedList<>();

    public Mesa(int capacidad) {
        this.capacidad = capacidad;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        puestos = new LinkedList<>();
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getPuestos() {
        return capacidad - puestos.size();
    }

    public int getOcupados() {
        return puestos.size();
    }

    public boolean isFull() {
        return puestos.size() >= capacidad;
    }

    public void EstadoMesa() {
        if (getPuestos() == 0) {
            System.out.println("No sobran puestos en la Mesa");
        } else {
            System.out.println("Sobran " + getPuestos() + " puestos en la Mesa");
        }
    }

    public void addCliente(Cliente cliente) {
        if (cliente == null) {
            return;
        }
        if (!isFull() && !puestos.contains(cliente)) {
            puestos.add(cliente);
        }
    }

    public void deleteCliente(Cliente cliente) {
        puestos.remove(cliente);
    }

    public List<Cliente> getClientes() {
        return Collections.unmodifiableList(puestos);
    }

    @Override
    public String toString() {
        return "Mesa(capacidad=" + capacidad + ", libres=" + getPuestos() + ", ocupados=" + getOcupados() + ")";
    }
}
