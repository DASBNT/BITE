package bite;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RestauranteUI extends JFrame {
    private Restaurante baseRestaurante;

    private DefaultListModel<Mesa> modeloMesas = new DefaultListModel<>();
    private DefaultListModel<Cliente> modeloClientes = new DefaultListModel<>();
    private DefaultListModel<Producto> modeloProductos = new DefaultListModel<>();
    private DefaultListModel<Pedido> modeloPedidos = new DefaultListModel<>();
    private DefaultListModel<LineaPedido> modeloLineasPedido = new DefaultListModel<>();
    private DefaultListModel<Cliente> modeloClientesEnMesa = new DefaultListModel<>();

    private JList<Mesa> listadoMesas = new JList<>(modeloMesas);
    private JList<Cliente> listadoClientesMesa = new JList<>(modeloClientesEnMesa);
    private JList<Cliente> listadoClientes = new JList<>(modeloClientes);
    private JComboBox<Cliente> comboClienteMesa = new JComboBox<>();
    private JList<Producto> listadoProductos = new JList<>(modeloProductos);
    private JList<Pedido> listadoPedidos = new JList<>(modeloPedidos);
    private JList<LineaPedido> listadoLineasPedido = new JList<>(modeloLineasPedido);

    private JTextField campoCapacidadMesa = new JTextField(8);
    private JLabel etiquetaMesaInfo = new JLabel("Seleccione una mesa");

    private JTextField campoNombreCliente = new JTextField(12);
    private JLabel etiquetaMesaCliente = new JLabel("No asignado");
    private JComboBox<Mesa> comboMesasCliente = new JComboBox<>();

    private JTextField campoNombreProducto = new JTextField(12);
    private JTextField campoTipoProducto = new JTextField(12);
    private JTextField campoPrecioProducto = new JTextField(8);
    private JCheckBox casillaDisponibleProducto = new JCheckBox("Disponible");

    private JComboBox<Cliente> comboClientePedido = new JComboBox<>();
    private JTextField campoEstadoPedido = new JTextField("Pendiente", 12);
    private JTextField campoHoraPedido = new JTextField(8);
    private JTextArea areaObservacionesPedido = new JTextArea(3, 18);
    private JComboBox<Producto> comboProductoPedido = new JComboBox<>();
    private JTextField campoCantidadProducto = new JTextField("1", 4);
    private JLabel etiquetaTotalPedido = new JLabel("$0.00");
    private JComboBox<String> comboPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia"});
    private DefaultComboBoxModel<Pedido> modeloPedidoFactura = new DefaultComboBoxModel<>();
    private JComboBox<Pedido> comboPedidoFactura = new JComboBox<>(modeloPedidoFactura);
    private JTextArea areaFactura = new JTextArea(10, 32);
    private JLabel etiquetaTotalDia = new JLabel("$0.00");

    public RestauranteUI(Restaurante restaurante) {
        super("Gestión de Restaurante");
        this.baseRestaurante = restaurante;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(930, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Mesas", crearPanelMesas());
        pestañas.addTab("Clientes", crearPanelClientes());
        pestañas.addTab("Productos", crearPanelProductos());
        pestañas.addTab("Pedidos", crearPanelPedidos());
        pestañas.addTab("Facturación", crearPanelFacturacion());

        add(pestañas, BorderLayout.CENTER);

        actualizarPantalla();
    }

    private JPanel crearPanelMesas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        listadoMesas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listadoMesas.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Mesa) {
                    setText("Mesa " + (index + 1) + " - " + value.toString());
                }
                return this;
            }
        });
        listadoMesas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarSeleccionMesa();
                }
            }
        });

        JScrollPane listaScroll = new JScrollPane(listadoMesas);
        listaScroll.setPreferredSize(new Dimension(320, 0));
        panel.add(listaScroll, BorderLayout.WEST);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(crearPanelEtiqueta("Capacidad:", campoCapacidadMesa));
        form.add(Box.createVerticalStrut(8));
        form.add(etiquetaMesaInfo);
        form.add(Box.createVerticalStrut(16));
        form.add(new JLabel("Clientes en esta mesa:"));
        JScrollPane mesaClientsScroll = new JScrollPane(listadoClientesMesa);
        mesaClientsScroll.setPreferredSize(new Dimension(280, 100));
        form.add(mesaClientsScroll);
        form.add(Box.createVerticalStrut(8));
        form.add(crearPanelEtiqueta("Cliente:", comboClienteMesa));
        form.add(Box.createVerticalStrut(8));
        JPanel mesaClientButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton assignClientButton = new JButton("Asignar cliente");
        JButton removeClientButton = new JButton("Quitar cliente");
        mesaClientButtons.add(assignClientButton);
        mesaClientButtons.add(removeClientButton);
        form.add(mesaClientButtons);
        form.add(Box.createVerticalStrut(16));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Añadir mesa");
        JButton updateButton = new JButton("Modificar capacidad");
        JButton deleteButton = new JButton("Eliminar mesa");
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        form.add(buttons);
        form.add(Box.createVerticalGlue());

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarMesa();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarMesa();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarMesa();
            }
        });

        assignClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                asignarClienteAMesa();
            }
        });

        removeClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerClienteDeMesa();
            }
        });

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        listadoClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listadoClientes.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarSeleccionCliente();
                }
            }
        });

        JScrollPane listaScroll = new JScrollPane(listadoClientes);
        listaScroll.setPreferredSize(new Dimension(320, 0));
        panel.add(listaScroll, BorderLayout.WEST);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(crearPanelEtiqueta("Nombre:", campoNombreCliente));
        form.add(Box.createVerticalStrut(8));
        form.add(crearPanelEtiqueta("Mesa asignada:", etiquetaMesaCliente));
        form.add(Box.createVerticalStrut(8));
        form.add(crearPanelEtiqueta("Asignar a mesa:", comboMesasCliente));
        form.add(Box.createVerticalStrut(8));
        JButton assignMesaButton = new JButton("Asignar a mesa");
        form.add(assignMesaButton);
        form.add(Box.createVerticalStrut(16));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Añadir cliente");
        JButton updateButton = new JButton("Modificar cliente");
        JButton deleteButton = new JButton("Eliminar cliente");
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        form.add(buttons);
        form.add(Box.createVerticalGlue());

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarCliente();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarCliente();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });

        assignMesaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                asignarClienteDesdeCliente();
            }
        });

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        listadoProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listadoProductos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarSeleccionProducto();
                }
            }
        });

        JScrollPane listaScroll = new JScrollPane(listadoProductos);
        listaScroll.setPreferredSize(new Dimension(320, 0));
        panel.add(listaScroll, BorderLayout.WEST);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(crearPanelEtiqueta("Nombre:", campoNombreProducto));
        form.add(Box.createVerticalStrut(8));
        form.add(crearPanelEtiqueta("Tipo:", campoTipoProducto));
        form.add(Box.createVerticalStrut(8));
        form.add(crearPanelEtiqueta("Precio:", campoPrecioProducto));
        form.add(Box.createVerticalStrut(8));
        form.add(casillaDisponibleProducto);
        form.add(Box.createVerticalStrut(16));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Añadir producto");
        JButton updateButton = new JButton("Modificar producto");
        JButton deleteButton = new JButton("Eliminar producto");
        JButton newMenuButton = new JButton("Nuevo menú");
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(newMenuButton);
        form.add(buttons);
        form.add(Box.createVerticalGlue());

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarProducto();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto();
            }
        });

        newMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarMenu();
            }
        });

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelPedidos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        listadoPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listadoPedidos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    actualizarSeleccionPedido();
                }
            }
        });

        JScrollPane listaScroll = new JScrollPane(listadoPedidos);
        listaScroll.setPreferredSize(new Dimension(320, 0));
        panel.add(listaScroll, BorderLayout.WEST);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(crearPanelEtiqueta("Cliente:", comboClientePedido));
        form.add(Box.createVerticalStrut(8));
        campoHoraPedido.setEditable(false);
        form.add(crearPanelEtiqueta("Hora:", campoHoraPedido));
        form.add(Box.createVerticalStrut(8));
        form.add(crearPanelEtiqueta("Estado:", campoEstadoPedido));
        form.add(Box.createVerticalStrut(8));
        form.add(crearPanelEtiqueta("Observaciones:", new JScrollPane(areaObservacionesPedido)));
        form.add(Box.createVerticalStrut(8));
        form.add(crearPanelEtiqueta("Producto:", comboProductoPedido));
        form.add(Box.createVerticalStrut(4));
        form.add(crearPanelEtiqueta("Cantidad:", campoCantidadProducto));
        form.add(Box.createVerticalStrut(4));
        JPanel orderButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addOrderButton = new JButton("Crear pedido");
        JButton updateOrderButton = new JButton("Guardar pedido");
        JButton deleteOrderButton = new JButton("Eliminar pedido");
        orderButtons.add(addOrderButton);
        orderButtons.add(updateOrderButton);
        orderButtons.add(deleteOrderButton);
        form.add(orderButtons);
        form.add(Box.createVerticalStrut(12));
        form.add(new JLabel("Productos en el pedido:"));
        JScrollPane itemsScroll = new JScrollPane(listadoLineasPedido);
        itemsScroll.setPreferredSize(new Dimension(0, 140));
        form.add(itemsScroll);
        form.add(Box.createVerticalStrut(8));
        
        JPanel productButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addProductButton = new JButton("Agregar producto");
        JButton removeProductButton = new JButton("Quitar producto");
        productButtons.add(addProductButton);
        productButtons.add(removeProductButton);
        form.add(productButtons);
        form.add(Box.createVerticalStrut(12));
        form.add(new JLabel("Total:"));
        form.add(etiquetaTotalPedido);
        form.add(Box.createVerticalGlue());

        addOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearPedido();
            }
        });

        updateOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPedido();
            }
        });

        deleteOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPedido();
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProductoAPedido();
            }
        });

        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerProductoPedido();
            }
        });

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelFacturacion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(crearPanelEtiqueta("Pedido:", comboPedidoFactura));
        form.add(Box.createVerticalStrut(8));
        form.add(crearPanelEtiqueta("Método de pago:", comboPago));
        form.add(Box.createVerticalStrut(8));
        JButton generarFacturaButton = new JButton("Generar factura");
        JButton totalDiaButton = new JButton("Calcular total del día");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(generarFacturaButton);
        buttons.add(totalDiaButton);
        form.add(buttons);
        form.add(Box.createVerticalStrut(12));
        form.add(new JLabel("Factura generada:"));
        areaFactura.setEditable(false);
        areaFactura.setLineWrap(true);
        areaFactura.setWrapStyleWord(true);
        JScrollPane facturaScroll = new JScrollPane(areaFactura);
        facturaScroll.setPreferredSize(new Dimension(0, 180));
        form.add(facturaScroll);
        form.add(Box.createVerticalStrut(12));
        form.add(crearPanelEtiqueta("Total del día:", etiquetaTotalDia));
        form.add(Box.createVerticalGlue());

        generarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarFactura();
            }
        });

        totalDiaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularTotalDia();
            }
        });

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelEtiqueta(String labelText, Component component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panel.add(new JLabel(labelText));
        panel.add(component);
        return panel;
    }

    private void actualizarPantalla() {
        recargarMesas();
        recargarClientes();
        recargarProductos();
        recargarPedidos();
        llenarCombosPedido();
        recargarPedidosFactura();
    }

    private void recargarMesas() {
        modeloMesas.clear();
        for (Mesa mesa : baseRestaurante.getMesas()) {
            modeloMesas.addElement(mesa);
        }
        llenarComboClientesMesa();
    }

    private void recargarClientes() {
        modeloClientes.clear();
        comboClientePedido.removeAllItems();
        for (Cliente cliente : baseRestaurante.getClientes()) {
            modeloClientes.addElement(cliente);
            comboClientePedido.addItem(cliente);
        }
        llenarComboMesasCliente();
        llenarComboClientesMesa();
    }

    private void recargarProductos() {
        modeloProductos.clear();
        comboProductoPedido.removeAllItems();
        for (Producto producto : baseRestaurante.getMenu()) {
            modeloProductos.addElement(producto);
            comboProductoPedido.addItem(producto);
        }
    }

    private void recargarPedidos() {
        modeloPedidos.clear();
        for (int i = 0; i < baseRestaurante.getPedidos().getFacturasSize(); i++) {
            modeloPedidos.addElement(baseRestaurante.getPedidos().getFacturas(i));
        }
        recargarPedidosFactura();
    }

    private void recargarClientesDeMesa(Mesa mesa) {
        modeloClientesEnMesa.clear();
        if (mesa != null) {
            for (Cliente cliente : mesa.getClientes()) {
                modeloClientesEnMesa.addElement(cliente);
            }
        }
    }

    private void recargarLineasPedido(Pedido pedido) {
        modeloLineasPedido.clear();
        if (pedido != null) {
            for (LineaPedido linea : pedido.getFactura()) {
                modeloLineasPedido.addElement(linea);
            }
        }
    }

    private void llenarComboClientesMesa() {
        comboClienteMesa.removeAllItems();
        for (Cliente cliente : baseRestaurante.getClientes()) {
            comboClienteMesa.addItem(cliente);
        }
    }

    private void llenarComboMesasCliente() {
        comboMesasCliente.removeAllItems();
        for (Mesa mesa : baseRestaurante.getMesas()) {
            comboMesasCliente.addItem(mesa);
        }
        comboMesasCliente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Mesa) {
                    int displayIndex = index;
                    if (displayIndex < 0) {
                        displayIndex = comboMesasCliente.getSelectedIndex();
                    }
                    if (displayIndex >= 0) {
                        setText("Mesa " + (displayIndex + 1));
                    } else {
                        setText("Seleccione una mesa");
                    }
                }
                return this;
            }
        });
    }

    private void llenarCombosPedido() {
        comboClientePedido.removeAllItems();
        for (Cliente cliente : baseRestaurante.getClientes()) {
            comboClientePedido.addItem(cliente);
        }
        comboProductoPedido.removeAllItems();
        for (Producto producto : baseRestaurante.getMenu()) {
            comboProductoPedido.addItem(producto);
        }
    }

    private void recargarPedidosFactura() {
        modeloPedidoFactura.removeAllElements();
        for (Pedido pedido : baseRestaurante.getPedidos().getFacturasList()) {
            modeloPedidoFactura.addElement(pedido);
        }
        if (modeloPedidoFactura.getSize() > 0) {
            comboPedidoFactura.setSelectedIndex(0);
        }
    }

    private void actualizarSeleccionMesa() {
        Mesa mesa = listadoMesas.getSelectedValue();
        if (mesa != null) {
            campoCapacidadMesa.setText(String.valueOf(mesa.getCapacidad()));
            etiquetaMesaInfo.setText("Libres: " + mesa.getPuestos() + ", Ocupados: " + mesa.getOcupados());
            recargarClientesDeMesa(mesa);
            llenarComboClientesMesa();
        } else {
            campoCapacidadMesa.setText("");
            etiquetaMesaInfo.setText("Seleccione una mesa");
            modeloClientesEnMesa.clear();
            comboClienteMesa.removeAllItems();
        }
    }

    private void actualizarSeleccionCliente() {
        Cliente cliente = listadoClientes.getSelectedValue();
        if (cliente != null) {
            campoNombreCliente.setText(cliente.getNombre());
            int mesaIndex = baseRestaurante.getMesaIndexOfCliente(cliente);
            if (mesaIndex >= 0) {
                etiquetaMesaCliente.setText("Mesa " + (mesaIndex + 1));
            } else {
                etiquetaMesaCliente.setText("No asignado");
            }
        } else {
            campoNombreCliente.setText("");
            etiquetaMesaCliente.setText("No asignado");
        }
    }

    private void actualizarSeleccionProducto() {
        Producto producto = listadoProductos.getSelectedValue();
        if (producto != null) {
            campoNombreProducto.setText(producto.getNombre());
            campoTipoProducto.setText(producto.getTipo());
            campoPrecioProducto.setText(String.valueOf(producto.getPrecio()));
            casillaDisponibleProducto.setSelected(producto.isDisponibilidad());
        } else {
            campoNombreProducto.setText("");
            campoTipoProducto.setText("");
            campoPrecioProducto.setText("");
            casillaDisponibleProducto.setSelected(false);
        }
    }

    private void actualizarSeleccionPedido() {
        Pedido pedido = listadoPedidos.getSelectedValue();
        if (pedido != null) {
            comboClientePedido.setSelectedItem(pedido.getCliente());
            campoEstadoPedido.setText(pedido.getEstado());
            campoHoraPedido.setText(pedido.getHora());
            areaObservacionesPedido.setText(pedido.getObservaciones());
            recargarLineasPedido(pedido);
            etiquetaTotalPedido.setText(formatearMoneda(pedido.getTotal()));
        } else {
            campoEstadoPedido.setText("Pendiente");
            campoHoraPedido.setText("");
            areaObservacionesPedido.setText("");
            modeloLineasPedido.clear();
            etiquetaTotalPedido.setText("$0.00");
        }
    }

    private void generarFactura() {
        Pedido pedido = (Pedido) comboPedidoFactura.getSelectedItem();
        if (pedido == null) {
            mostrarError("Seleccione un pedido para generar la factura.");
            return;
        }
        int tipoPago = comboPago.getSelectedIndex();
        pedido.setTipo_de_pago(tipoPago);
        pedido.setEstado("Pago");

        comboPedidoFactura.setSelectedItem(pedido);
        comboPedidoFactura.repaint();
        listadoPedidos.repaint();
        actualizarSeleccionPedido();

        StringBuilder factura = new StringBuilder();
        factura.append("Factura para: ").append(pedido.getCliente().getNombre()).append("\n");
        factura.append("Pedido: ").append(pedido.getNombre()).append("\n");
        factura.append("Hora: ").append(pedido.getHora()).append("\n");
        factura.append("Pago: ").append(comboPago.getSelectedItem()).append("\n\n");
        factura.append("Productos:\n");
        for (LineaPedido linea : pedido.getFactura()) {
            factura.append("- ")
                    .append(linea.getProducto().getNombre())
                    .append(" x")
                    .append(linea.getCantidad())
                    .append(" : ")
                    .append(formatearMoneda(linea.getSubtotal()))
                    .append("\n");
        }
        factura.append("\nSubtotal: ").append(formatearMoneda(pedido.CalcularSubtotal())).append("\n");
        factura.append("Impuesto 8%: ").append(formatearMoneda(pedido.CalcularTotal() - pedido.CalcularSubtotal())).append("\n");
        factura.append("Total: ").append(formatearMoneda(pedido.getTotal())).append("\n");

        areaFactura.setText(factura.toString());
    }

    private void calcularTotalDia() {
        double totalDia = 0;
        for (Pedido pedido : baseRestaurante.getPedidos().getFacturasList()) {
            totalDia += pedido.getTotal();
        }
        etiquetaTotalDia.setText(formatearMoneda(totalDia));
    }

    private String formatearMoneda(double value) {
        return String.format("$%.2f", value);
    }

    private void agregarMesa() {
        try {
            int capacidad = Integer.parseInt(campoCapacidadMesa.getText().trim());
            if (capacidad <= 0) {
                mostrarError("La capacidad debe ser un número mayor que cero.");
                return;
            }
            baseRestaurante.addMesa(1, capacidad);
            recargarMesas();
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese un valor numérico válido para la capacidad.");
        }
    }

    private void modificarMesa() {
        Mesa mesa = listadoMesas.getSelectedValue();
        if (mesa == null) {
            mostrarError("Seleccione una mesa para modificarla.");
            return;
        }
        try {
            int capacidad = Integer.parseInt(campoCapacidadMesa.getText().trim());
            if (capacidad <= 0) {
                mostrarError("La capacidad debe ser un número mayor que cero.");
                return;
            }
            mesa.setCapacidad(capacidad);
            baseRestaurante.saveMesas();
            recargarMesas();
            listadoMesas.setSelectedValue(mesa, true);
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese un valor numérico válido para la capacidad.");
        }
    }

    private void eliminarMesa() {
        int index = listadoMesas.getSelectedIndex();
        if (index < 0) {
            mostrarError("Seleccione una mesa para eliminarla.");
            return;
        }
        baseRestaurante.deleteMesa(index);
        recargarMesas();
    }

    private void asignarClienteAMesa() {
        int mesaIndex = listadoMesas.getSelectedIndex();
        Cliente cliente = (Cliente) comboClienteMesa.getSelectedItem();
        if (mesaIndex < 0 || cliente == null) {
            mostrarError("Seleccione una mesa y un cliente para asignar.");
            return;
        }
        baseRestaurante.assignClienteToMesa(mesaIndex, cliente);
        recargarClientesDeMesa(baseRestaurante.getMesa(mesaIndex));
    }

    private void removerClienteDeMesa() {
        int mesaIndex = listadoMesas.getSelectedIndex();
        Cliente cliente = listadoClientesMesa.getSelectedValue();
        if (mesaIndex < 0 || cliente == null) {
            mostrarError("Seleccione una mesa y un cliente para quitar.");
            return;
        }
        baseRestaurante.removeClienteFromMesa(mesaIndex, cliente);
        recargarClientesDeMesa(baseRestaurante.getMesa(mesaIndex));
    }

    private void agregarCliente() {
        String nombre = campoNombreCliente.getText().trim();
        if (nombre.isEmpty()) {
            mostrarError("Ingrese el nombre del cliente.");
            return;
        }
        Cliente cliente = new Cliente(nombre, true);
        baseRestaurante.addCliente(cliente);
        recargarClientes();
    }

    private void modificarCliente() {
        Cliente cliente = listadoClientes.getSelectedValue();
        if (cliente == null) {
            mostrarError("Seleccione un cliente para modificarlo.");
            return;
        }
        String nombre = campoNombreCliente.getText().trim();
        if (nombre.isEmpty()) {
            mostrarError("Ingrese el nombre del cliente.");
            return;
        }
        cliente.setNombre(nombre);
        recargarClientes();
        listadoClientes.setSelectedValue(cliente, true);
        llenarCombosPedido();
    }

    private void eliminarCliente() {
        int index = listadoClientes.getSelectedIndex();
        if (index < 0) {
            mostrarError("Seleccione un cliente para eliminarlo.");
            return;
        }
        baseRestaurante.deleteCliente(index);
        recargarClientes();
        llenarCombosPedido();
    }

    private void asignarClienteDesdeCliente() {
        Cliente cliente = listadoClientes.getSelectedValue();
        Mesa mesa = (Mesa) comboMesasCliente.getSelectedItem();
        int mesaIndex = comboMesasCliente.getSelectedIndex();
        if (cliente == null || mesa == null || mesaIndex < 0) {
            mostrarError("Seleccione un cliente y una mesa para asignar.");
            return;
        }
        baseRestaurante.assignClienteToMesa(mesaIndex, cliente);
        actualizarPantalla();
        listadoClientes.setSelectedValue(cliente, true);
    }

    private void agregarProducto() {
        String nombre = campoNombreProducto.getText().trim();
        String tipo = campoTipoProducto.getText().trim();
        String precioTexto = campoPrecioProducto.getText().trim();
        if (nombre.isEmpty() || tipo.isEmpty() || precioTexto.isEmpty()) {
            mostrarError("Ingrese nombre, tipo y precio del producto.");
            return;
        }
        try {
            double precio = Double.parseDouble(precioTexto);
            if (precio < 0) {
                mostrarError("El precio debe ser un valor positivo.");
                return;
            }
            Producto producto = new Producto(tipo, precio, nombre, casillaDisponibleProducto.isSelected());
            baseRestaurante.addProducto(producto);
            recargarProductos();
            llenarCombosPedido();
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese un precio válido.");
        }
    }

    private void modificarProducto() {
        Producto producto = listadoProductos.getSelectedValue();
        if (producto == null) {
            mostrarError("Seleccione un producto para modificarlo.");
            return;
        }
        String nombre = campoNombreProducto.getText().trim();
        String tipo = campoTipoProducto.getText().trim();
        String precioTexto = campoPrecioProducto.getText().trim();
        if (nombre.isEmpty() || tipo.isEmpty() || precioTexto.isEmpty()) {
            mostrarError("Ingrese nombre, tipo y precio del producto.");
            return;
        }
        try {
            double precio = Double.parseDouble(precioTexto);
            if (precio < 0) {
                mostrarError("El precio debe ser un valor positivo.");
                return;
            }
            producto.setNombre(nombre);
            producto.setTipo(tipo);
            producto.setPrecio(precio);
            producto.setDisponibilidad(casillaDisponibleProducto.isSelected());
            baseRestaurante.saveMenu();
            recargarProductos();
            listadoProductos.setSelectedValue(producto, true);
            llenarCombosPedido();
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese un precio válido.");
        }
    }

    private void eliminarProducto() {
        int index = listadoProductos.getSelectedIndex();
        if (index < 0) {
            mostrarError("Seleccione un producto para eliminarlo.");
            return;
        }
        baseRestaurante.deleteProducto(index);
        recargarProductos();
        llenarCombosPedido();
    }

    private void limpiarMenu() {
        baseRestaurante.clearMenu();
        recargarProductos();
        llenarCombosPedido();
    }

    private void crearPedido() {
        Cliente cliente = (Cliente) comboClientePedido.getSelectedItem();
        if (cliente == null) {
            mostrarError("Seleccione un cliente para el pedido.");
            return;
        }
        String estado = campoEstadoPedido.getText().trim();
        if (estado.isEmpty()) {
            estado = "Pendiente";
        }
        String hora = new SimpleDateFormat("HH:mm").format(new Date());
        String nombre = "Pedido " + (baseRestaurante.getPedidos().getFacturasSize() + 1);
        Pedido pedido = new Pedido(areaObservacionesPedido.getText().trim(), estado, hora, cliente, nombre, true);
        baseRestaurante.addPedido(pedido);
        recargarPedidos();
        listadoPedidos.setSelectedValue(pedido, true);
    }

    private void guardarPedido() {
        Pedido pedido = listadoPedidos.getSelectedValue();
        if (pedido == null) {
            mostrarError("Seleccione un pedido para guardar los cambios.");
            return;
        }
        Cliente cliente = (Cliente) comboClientePedido.getSelectedItem();
        if (cliente == null) {
            mostrarError("Seleccione un cliente para el pedido.");
            return;
        }
        pedido.setCliente(cliente);
        pedido.setEstado(campoEstadoPedido.getText().trim());
        pedido.setObservaciones(areaObservacionesPedido.getText().trim());
        recargarPedidos();
        listadoPedidos.setSelectedValue(pedido, true);
    }

    private void eliminarPedido() {
        int index = listadoPedidos.getSelectedIndex();
        if (index < 0) {
            mostrarError("Seleccione un pedido para eliminarlo.");
            return;
        }
        baseRestaurante.deletePedido(index);
        recargarPedidos();
    }

    private void agregarProductoAPedido() {
        Pedido pedido = listadoPedidos.getSelectedValue();
        Producto producto = (Producto) comboProductoPedido.getSelectedItem();
        if (pedido == null || producto == null) {
            mostrarError("Seleccione un pedido y un producto para agregar.");
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(campoCantidadProducto.getText().trim());
            if (cantidad <= 0) {
                mostrarError("Ingrese una cantidad mayor que cero.");
                return;
            }
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese una cantidad válida.");
            return;
        }
        pedido.addProducto(producto, cantidad);
        recargarLineasPedido(pedido);
        etiquetaTotalPedido.setText(formatearMoneda(pedido.getTotal()));
    }

    private void removerProductoPedido() {
        Pedido pedido = listadoPedidos.getSelectedValue();
        LineaPedido linea = listadoLineasPedido.getSelectedValue();
        if (pedido == null || linea == null) {
            mostrarError("Seleccione un pedido y un producto para eliminar.");
            return;
        }
        pedido.deleteProducto(linea);
        recargarLineasPedido(pedido);
        etiquetaTotalPedido.setText(formatearMoneda(pedido.getTotal()));
    }

    private void mostrarError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.WARNING_MESSAGE);
    }
}
