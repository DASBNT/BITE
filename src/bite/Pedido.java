
package bite;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class Pedido extends Base {
    private ArrayList<Producto> factura = new ArrayList<>();
    private String observaciones;
    private String estado;
    private String hora;
    private Cliente cliente;
    
    public Pedido(String observaciones, String estado, String hora, Cliente cliente, String nombre, boolean Disponibilidad) {
        super(nombre, Disponibilidad);
        this.observaciones = observaciones;
        this.estado = estado;
        this.hora = hora;
        this.cliente = cliente;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void addProducto(Producto producto){
        factura.add(producto);
    }
    
    public void deleteProducto(Producto producto){
        factura.remove(producto);
    }
    
    public double CalcularSubtotal(){
        double Subtotal = 0;
        for (Producto producto : factura){
            Subtotal = Subtotal + producto.getPrecio();
        }
        return Subtotal;
    }
    
    public double CalcularTotal(){
        double Total = CalcularSubtotal() + CalcularSubtotal() * 0.08;
        return Total;
    }
    
    public void HacerFactura(int numero_pedido){
        System.out.println("Pedido # " + numero_pedido);  
        for (Producto producto : factura) {
            System.out.println(producto.getNombre());
            System.out.print(" : " + producto.getPrecio());
            System.out.println(" INC : " + producto.getPrecio()*0.08);
        }
        System.out.println("El total es : " + CalcularTotal());
    }
}
