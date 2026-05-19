package bite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaPedidos {
    private ArrayList<Pedido> facturas = new ArrayList<>();

    public void addPedido(Pedido pedido) {
        facturas.add(pedido);
    }

    public void deletePedido(Pedido pedido) {
        facturas.remove(pedido);
    }

    public Pedido getFacturas(int numero_pedido) {
        return facturas.get(numero_pedido);
    }

    public int getFacturasSize() {
        return facturas.size();
    }

    public List<Pedido> getFacturasList() {
        return Collections.unmodifiableList(facturas);
    }

    public void HacerFactura(int numero_pedido) {
        System.out.println("Pedido # " + numero_pedido);
        for (int i = 0; i < facturas.get(numero_pedido).getFactura().size(); i++) {
            LineaPedido linea = facturas.get(numero_pedido).getFactura().get(i);
            System.out.println(linea.getProducto().getNombre());
            System.out.print(" : " + linea.getProducto().getPrecio());
            System.out.println(" INC : " + linea.getProducto().getPrecio() * 0.08);
        }
        System.out.println("El total es : " + facturas.get(numero_pedido).CalcularTotal());
    }
}
