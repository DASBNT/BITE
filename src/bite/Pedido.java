package bite;

import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base {
    private ArrayList<LineaPedido> factura = new ArrayList<>();
    private String observaciones;
    private String estado;
    private String hora;
    private Cliente cliente;
    private int tipo_de_pago;

    public Pedido(String observaciones, String estado, String hora, Cliente cliente, String nombre, boolean Disponibilidad) {
        super(nombre, Disponibilidad);
        this.observaciones = observaciones;
        this.estado = estado;
        this.hora = hora;
        this.cliente = cliente;
    }

    public List<LineaPedido> getFactura() {
        return factura;
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

    public void addProducto(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) {
            return;
        }
        for (LineaPedido linea : factura) {
            if (linea.getProducto().equals(producto)) {
                linea.setCantidad(linea.getCantidad() + cantidad);
                return;
            }
        }
        factura.add(new LineaPedido(producto, cantidad));
    }

    public void deleteProducto(LineaPedido linea) {
        factura.remove(linea);
    }

    public int getTipo_de_pago() {
        return tipo_de_pago;
    }

    public void setTipo_de_pago(int tipo_de_pago) {
        this.tipo_de_pago = tipo_de_pago;
    }

    public double CalcularSubtotal() {
        double subtotal = 0;
        for (LineaPedido linea : factura) {
            subtotal += linea.getSubtotal();
        }
        return subtotal;
    }

    public double CalcularTotal() {
        return CalcularSubtotal() * 1.08;
    }

    public double getTotal() {
        return CalcularTotal();
    }

    @Override
    public String toString() {
        String clienteNombre = cliente != null ? cliente.getNombre() : "Sin cliente";
        return getNombre() + " - " + clienteNombre + " [" + estado + "]";
    }
}
