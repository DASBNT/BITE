
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
    
    public double CalcularGananciasDia(){
        double total_dia = 0;
        for(Pedido pedido : facturas){
            total_dia = pedido.CalcularTotal() - pedido.Impuesto();
        }
        return total_dia;
    }
}
