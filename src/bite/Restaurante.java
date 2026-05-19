package bite;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Restaurante {
    private static final String DATA_DIR = "data";
    private static final String MESAS_FILE = DATA_DIR + File.separator + "mesas.dat";
    private static final String MENU_FILE = DATA_DIR + File.separator + "menu.dat";

    private ArrayList<Mesa> mesas = new ArrayList<>();
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Producto> menu = new ArrayList<>();
    private ListaPedidos pedidos = new ListaPedidos();

    public Restaurante() {
        loadData();
    }

    public void addMesa(int cantidadMesas, int capacidad) {
        for (int i = 0; i < cantidadMesas; i++) {
            mesas.add(new Mesa(capacidad));
        }
        saveMesas();
    }

    public void deleteMesa(int index) {
        if (index >= 0 && index < mesas.size()) {
            mesas.remove(index);
            saveMesas();
        }
    }

    public Mesa getMesa(int mesa) {
        return mesas.get(mesa);
    }

    public List<Mesa> getMesas() {
        return Collections.unmodifiableList(mesas);
    }

    public void addCliente(Cliente cliente) {
        if (cliente != null) {
            clientes.add(cliente);
        }
    }

    public void deleteCliente(int index) {
        if (index >= 0 && index < clientes.size()) {
            Cliente cliente = clientes.get(index);
            removeClienteFromAllMesas(cliente);
            clientes.remove(index);
        }
    }

    public int getMesaIndexOfCliente(Cliente cliente) {
        if (cliente == null) {
            return -1;
        }
        for (int i = 0; i < mesas.size(); i++) {
            if (mesas.get(i).getClientes().contains(cliente)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isClienteAsignado(Cliente cliente) {
        return getMesaIndexOfCliente(cliente) >= 0;
    }

    public List<Cliente> getClientes() {
        return Collections.unmodifiableList(clientes);
    }

    public void addProducto(Producto producto) {
        if (producto != null) {
            menu.add(producto);
            saveMenu();
        }
    }

    public void deleteProducto(int index) {
        if (index >= 0 && index < menu.size()) {
            menu.remove(index);
            saveMenu();
        }
    }

    public List<Producto> getMenu() {
        return Collections.unmodifiableList(menu);
    }

    public void clearMenu() {
        menu.clear();
        saveMenu();
    }

    public void addPedido(Pedido pedido) {
        if (pedido != null) {
            pedidos.addPedido(pedido);
        }
    }

    public void deletePedido(int index) {
        if (index >= 0 && index < pedidos.getFacturasSize()) {
            pedidos.deletePedido(pedidos.getFacturas(index));
        }
    }

    public void assignClienteToMesa(int mesaIndex, Cliente cliente) {
        if (cliente == null || mesaIndex < 0 || mesaIndex >= mesas.size()) {
            return;
        }
        int currentIndex = getMesaIndexOfCliente(cliente);
        if (currentIndex == mesaIndex) {
            return;
        }
        if (currentIndex >= 0) {
            mesas.get(currentIndex).deleteCliente(cliente);
        }
        Mesa mesa = mesas.get(mesaIndex);
        if (!mesa.isFull()) {
            mesa.addCliente(cliente);
            cliente.setDisponibilidad(false);
            saveMesas();
        }
    }

    public void removeClienteFromMesa(int mesaIndex, Cliente cliente) {
        if (cliente == null || mesaIndex < 0 || mesaIndex >= mesas.size()) {
            return;
        }
        mesas.get(mesaIndex).deleteCliente(cliente);
        cliente.setDisponibilidad(true);
        saveMesas();
    }

    public void removeClienteFromAllMesas(Cliente cliente) {
        if (cliente == null) {
            return;
        }
        for (Mesa mesa : mesas) {
            mesa.deleteCliente(cliente);
        }
        cliente.setDisponibilidad(true);
        saveMesas();
    }

    public ListaPedidos getPedidos() {
        return pedidos;
    }

    private void loadData() {
        loadMesas();
        loadMenu();
    }

    private void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadMesas() {
        File file = new File(MESAS_FILE);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object object = in.readObject();
            if (object instanceof ArrayList) {
                mesas = (ArrayList<Mesa>) object;
            }
        } catch (IOException | ClassNotFoundException e) {
            // Ignorar si no se puede leer el archivo de mesas.
        }
    }

    @SuppressWarnings("unchecked")
    private void loadMenu() {
        File file = new File(MENU_FILE);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object object = in.readObject();
            if (object instanceof ArrayList) {
                menu = (ArrayList<Producto>) object;
            }
        } catch (IOException | ClassNotFoundException e) {
            // Ignorar si no se puede leer el archivo de menú.
        }
    }

    public void saveMesas() {
        ensureDataDirectory();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(MESAS_FILE))) {
            out.writeObject(mesas);
        } catch (IOException e) {
            // Ignorar error de guardado.
        }
    }

    public void saveMenu() {
        ensureDataDirectory();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(MENU_FILE))) {
            out.writeObject(menu);
        } catch (IOException e) {
            // Ignorar error de guardado.
        }
    }
}
