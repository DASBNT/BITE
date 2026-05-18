
package bite;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class ListaPedidos {
    private ArrayList<Pedido> facturas = new ArrayList<>();
    
    public void addPedido(Pedido pedido){
        facturas.add(pedido);
    }
    
    public void deletePedido(Pedido pedido){
        facturas.remove(pedido);
    }

    public Pedido getFacturas(int numero_pedido) {
        return facturas.get(numero_pedido);
    }
    
    public int getFacturasSize() {
        return facturas.size();
    }
    
    public void HacerFactura(int numero_pedido){
        System.out.println("Pedido # " + numero_pedido);
        for (int i = 0; i < facturas.get(numero_pedido).getFactura().size(); i++) {
            System.out.println(facturas.get(numero_pedido).getFactura().get(i).getNombre());
            System.out.print(" : " + facturas.get(numero_pedido).getFactura().get(i).getPrecio());
            System.out.println(" INC : " + facturas.get(numero_pedido).getFactura().get(i).getPrecio()*0.08);
        }
        System.out.println("El total es : " + facturas.get(numero_pedido).CalcularTotal());
    }
}
