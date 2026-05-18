package bite;
import java.util.Scanner;
public class Cajero extends Empleados{

    public Cajero(String nombre, boolean Disponibilidad, String id) {
        super(nombre, Disponibilidad, id);
    }
    
    Scanner entrada = new Scanner(System.in);
    
    public double calcularImpuesto(ListaPedidos lista, int numero_pedido){
        // validar índice
        if (numero_pedido < 0 || numero_pedido >= lista.getFacturasSize()) {
            throw new IndexOutOfBoundsException("Número de pedido inválido: " + numero_pedido);
        }

        Pedido pedido = lista.getFacturas(numero_pedido);

        // obtener tipo de pago guardado en el pedido; si no está válido, pedirlo por consola y guardarlo
        int tipo = pedido.getTipo_de_pago();
        while (tipo < 1 || tipo > 3) {
            System.out.println("Con que vas a pagar : efectivo(1), tarjeta(2), transferencia(3)");
            try {
                tipo = entrada.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrada no válida. Intenta de nuevo.");
                entrada.nextLine(); // limpiar buffer
                tipo = -1;
                continue;
            }
            if (tipo >= 1 && tipo <= 3) {
                pedido.setTipo_de_pago(tipo);
            }
        }

        double impuestoRate;
        switch (tipo) {
            case 1: impuestoRate = 0.0; break; // efectivo
            case 2: impuestoRate = 0.025; break; // tarjeta
            case 3: impuestoRate = 0.0; break; // transferencia
            default: impuestoRate = 0.0; break;
        }

        return impuestoRate * pedido.CalcularTotal();
    }
     
    public void recibirPago(ListaPedidos lista, int numero_pedido){
        lista.getFacturas(numero_pedido).setEstado("pago");
        lista.HacerFactura(numero_pedido);
    }
     
    public double CalcularGananciasDia(ListaPedidos lista){
        double total_dia = 0;
        for (int i = 0; i < lista.getFacturasSize(); i++) {
            total_dia += (lista.getFacturas(i).CalcularTotal() - calcularImpuesto(lista, i));
        }
        return total_dia;
    }
     
}
