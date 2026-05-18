
package bite;
import java.util.Scanner;
public class Cajero extends Empleados{

    public Cajero(String nombre, boolean Disponibilidad, String id) {
        super(nombre, Disponibilidad, id);
    }
    
    Scanner entrada = new Scanner(System.in);
    
     public double calcularImpuesto(ListaPedidos lista, int numero_pedido){
        System.out.println("Con que vas a pagar : efectivo(1), tarjeta(2), tranferencia(3)");
        double impuesto = 0;
        while (lista.getFacturas(numero_pedido).getTipo_de_pago() < 3 && lista.getFacturas(numero_pedido).getTipo_de_pago() > 1){
        switch (lista.getFacturas(numero_pedido).getTipo_de_pago()) {
            case 1:
                impuesto = 0;
                break;
            case 2:
                impuesto = 0.025;
                break;
            case 3:
                impuesto = 0;
                break;
            default:
                System.out.println("Numero no valido, escribir un numero valido");
                System.out.println("Con que vas a pagar : efectivo(1), tarjeta(2), tranferencia(3)");
                lista.getFacturas(numero_pedido).getTipo_de_pago();
                break;
        }
    }   
        return impuesto*lista.getFacturas(numero_pedido).CalcularTotal();
}
     
     public void recibirPago(ListaPedidos lista, int numero_pedido){
         lista.getFacturas(numero_pedido).setEstado("pago");
         lista.HacerFactura(numero_pedido);
     }
     
     public double CalcularGananciasDia(ListaPedidos lista){
        double total_dia = 0;
        for(int i = 0; i<lista.getFacturasSize(); i++){
            total_dia = total_dia + (lista.getFacturas(i).CalcularTotal() - calcularImpuesto(lista,i));
        }
        return total_dia;
    }
     
}
