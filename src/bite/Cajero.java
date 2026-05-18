
package bite;
import java.util.Scanner;
public class Cajero extends Empleados{

    private int tipo_de_pago;
    
    public Cajero(String nombre, boolean Disponibilidad, String id) {
        super(nombre, Disponibilidad, id);
    }
    
    Scanner entrada = new Scanner(System.in);
    
     public double calcularImpuesto(Pedido pedido){
        System.out.println("Con que vas a pagar : efectivo(1), tarjeta(2), tranferencia(3)");
        tipo_de_pago = entrada.nextInt();
        double impuesto = 0;
        while (tipo_de_pago > 3 || tipo_de_pago < 1){
        switch (tipo_de_pago) {
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
                tipo_de_pago = entrada.nextInt();
                break;
        }
    }   
        return impuesto*pedido.CalcularTotal();
}
     public void recibirPago(Pedido pedido){
         pedido.setEstado("pago");
         pedido.HacerFactura(tipo_de_pago);
     }
     
     public double CalcularGananciasDia(ListaPedidos lista){
        double total_dia = 0;
        for(Pedido pedido : lista.getFacturas()){
            total_dia = pedido.CalcularTotal() - calcularImpuesto(pedido);
        }
        return total_dia;
    }
     
}
