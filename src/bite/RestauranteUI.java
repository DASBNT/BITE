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
    private Restaurante restaurante;

    private DefaultListModel<Mesa> mesaModel = new DefaultListModel<>();
    private DefaultListModel<Cliente> clienteModel = new DefaultListModel<>();
    private DefaultListModel<Producto> productoModel = new DefaultListModel<>();
    private DefaultListModel<Pedido> pedidoModel = new DefaultListModel<>();
    private DefaultListModel<LineaPedido> pedidoItemsModel = new DefaultListModel<>();
    private DefaultListModel<Cliente> mesaClientesModel = new DefaultListModel<>();

    private JList<Mesa> mesaList = new JList<>(mesaModel);
    private JList<Cliente> mesaClientesList = new JList<>(mesaClientesModel);
    private JList<Cliente> clienteList = new JList<>(clienteModel);
    private JComboBox<Cliente> mesaClienteCombo = new JComboBox<>();
    private JList<Producto> productoList = new JList<>(productoModel);
    private JList<Pedido> pedidoList = new JList<>(pedidoModel);
    private JList<LineaPedido> pedidoItemsList = new JList<>(pedidoItemsModel);

    private JTextField mesaCapacidadField = new JTextField(8);
    private JLabel mesaInfoLabel = new JLabel("Seleccione una mesa");

    private JTextField clienteNombreField = new JTextField(12);
    private JLabel clienteMesaAsignadaLabel = new JLabel("No asignado");
    private JComboBox<Mesa> clienteMesaCombo = new JComboBox<>();

    private JTextField productoNombreField = new JTextField(12);
    private JTextField productoTipoField = new JTextField(12);
    private JTextField productoPrecioField = new JTextField(8);
    private JCheckBox productoDisponibleCheck = new JCheckBox("Disponible");

    private JComboBox<Cliente> orderClienteCombo = new JComboBox<>();
    private JTextField orderEstadoField = new JTextField("Pendiente", 12);
    private JTextField orderHoraField = new JTextField(8);
    private JTextArea orderObservacionesArea = new JTextArea(3, 18);
    private JComboBox<Producto> orderProductoCombo = new JComboBox<>();
    private JTextField orderCantidadField = new JTextField("1", 4);
    private JLabel orderTotalLabel = new JLabel("$0.00");
    private JComboBox<String> pagoCombo = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia"});
    private DefaultComboBoxModel<Pedido> facturaPedidoModel = new DefaultComboBoxModel<>();
    private JComboBox<Pedido> facturaPedidoCombo = new JComboBox<>(facturaPedidoModel);
    private JTextArea facturaArea = new JTextArea(10, 32);
    private JLabel totalDiaLabel = new JLabel("$0.00");

    public RestauranteUI(Restaurante restaurante) {
        super("Gestión de Restaurante");
        this.restaurante = restaurante;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(930, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Mesas", createMesasPanel());
        tabs.addTab("Clientes", createClientesPanel());
        tabs.addTab("Productos", createProductosPanel());
        tabs.addTab("Pedidos", createPedidosPanel());
        tabs.addTab("Facturación", createFacturacionPanel());

        add(tabs, BorderLayout.CENTER);

        reloadAll();
    }

    private JPanel createMesasPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        mesaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mesaList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Mesa) {
                    setText("Mesa " + (index + 1) + " - " + value.toString());
                }
                return this;
            }
        });
        mesaList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateMesaSelection();
                }
            }
        });

        JScrollPane listScroll = new JScrollPane(mesaList);
        listScroll.setPreferredSize(new Dimension(320, 0));
        panel.add(listScroll, BorderLayout.WEST);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(createLabelledPanel("Capacidad:", mesaCapacidadField));
        form.add(Box.createVerticalStrut(8));
        form.add(mesaInfoLabel);
        form.add(Box.createVerticalStrut(16));
        form.add(new JLabel("Clientes en esta mesa:"));
        JScrollPane mesaClientsScroll = new JScrollPane(mesaClientesList);
        mesaClientsScroll.setPreferredSize(new Dimension(280, 100));
        form.add(mesaClientsScroll);
        form.add(Box.createVerticalStrut(8));
        form.add(createLabelledPanel("Cliente:", mesaClienteCombo));
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
                addMesa();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMesa();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMesa();
            }
        });

        assignClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignClienteAMesa();
            }
        });

        removeClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeClienteDeMesa();
            }
        });

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createClientesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        clienteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clienteList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateClienteSelection();
                }
            }
        });

        JScrollPane listScroll = new JScrollPane(clienteList);
        listScroll.setPreferredSize(new Dimension(320, 0));
        panel.add(listScroll, BorderLayout.WEST);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(createLabelledPanel("Nombre:", clienteNombreField));
        form.add(Box.createVerticalStrut(8));
        form.add(createLabelledPanel("Mesa asignada:", clienteMesaAsignadaLabel));
        form.add(Box.createVerticalStrut(8));
        form.add(createLabelledPanel("Asignar a mesa:", clienteMesaCombo));
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
                addCliente();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCliente();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCliente();
            }
        });

        assignMesaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignClienteToMesaDesdeCliente();
            }
        });

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProductosPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        productoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productoList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateProductoSelection();
                }
            }
        });

        JScrollPane listScroll = new JScrollPane(productoList);
        listScroll.setPreferredSize(new Dimension(320, 0));
        panel.add(listScroll, BorderLayout.WEST);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(createLabelledPanel("Nombre:", productoNombreField));
        form.add(Box.createVerticalStrut(8));
        form.add(createLabelledPanel("Tipo:", productoTipoField));
        form.add(Box.createVerticalStrut(8));
        form.add(createLabelledPanel("Precio:", productoPrecioField));
        form.add(Box.createVerticalStrut(8));
        form.add(productoDisponibleCheck);
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
                addProducto();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProducto();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProducto();
            }
        });

        newMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearMenu();
            }
        });

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPedidosPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        pedidoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pedidoList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updatePedidoSelection();
                }
            }
        });

        JScrollPane listScroll = new JScrollPane(pedidoList);
        listScroll.setPreferredSize(new Dimension(320, 0));
        panel.add(listScroll, BorderLayout.WEST);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(createLabelledPanel("Cliente:", orderClienteCombo));
        form.add(Box.createVerticalStrut(8));
        orderHoraField.setEditable(false);
        form.add(createLabelledPanel("Hora:", orderHoraField));
        form.add(Box.createVerticalStrut(8));
        form.add(createLabelledPanel("Estado:", orderEstadoField));
        form.add(Box.createVerticalStrut(8));
        form.add(createLabelledPanel("Observaciones:", new JScrollPane(orderObservacionesArea)));
        form.add(Box.createVerticalStrut(8));
        form.add(createLabelledPanel("Producto:", orderProductoCombo));
        form.add(Box.createVerticalStrut(4));
        form.add(createLabelledPanel("Cantidad:", orderCantidadField));
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
        JScrollPane itemsScroll = new JScrollPane(pedidoItemsList);
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
        form.add(orderTotalLabel);
        form.add(Box.createVerticalGlue());

        addOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPedido();
            }
        });

        updateOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePedido();
            }
        });

        deleteOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePedido();
            }
        });

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductoAPedido();
            }
        });

        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProductoDePedido();
            }
        });

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFacturacionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        form.add(createLabelledPanel("Pedido:", facturaPedidoCombo));
        form.add(Box.createVerticalStrut(8));
        form.add(createLabelledPanel("Método de pago:", pagoCombo));
        form.add(Box.createVerticalStrut(8));
        JButton generarFacturaButton = new JButton("Generar factura");
        JButton totalDiaButton = new JButton("Calcular total del día");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(generarFacturaButton);
        buttons.add(totalDiaButton);
        form.add(buttons);
        form.add(Box.createVerticalStrut(12));
        form.add(new JLabel("Factura generada:"));
        facturaArea.setEditable(false);
        facturaArea.setLineWrap(true);
        facturaArea.setWrapStyleWord(true);
        JScrollPane facturaScroll = new JScrollPane(facturaArea);
        facturaScroll.setPreferredSize(new Dimension(0, 180));
        form.add(facturaScroll);
        form.add(Box.createVerticalStrut(12));
        form.add(createLabelledPanel("Total del día:", totalDiaLabel));
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

    private JPanel createLabelledPanel(String labelText, Component component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panel.add(new JLabel(labelText));
        panel.add(component);
        return panel;
    }

    private void reloadAll() {
        reloadMesas();
        reloadClientes();
        reloadProductos();
        reloadPedidos();
        populateOrderCombos();
        reloadFacturaPedidos();
    }

    private void reloadMesas() {
        mesaModel.clear();
        for (Mesa mesa : restaurante.getMesas()) {
            mesaModel.addElement(mesa);
        }
        populateClienteMesaCombo();
    }

    private void reloadClientes() {
        clienteModel.clear();
        orderClienteCombo.removeAllItems();
        for (Cliente cliente : restaurante.getClientes()) {
            clienteModel.addElement(cliente);
            orderClienteCombo.addItem(cliente);
        }
        populateMesaClientCombo();
        populateClienteMesaCombo();
    }

    private void reloadProductos() {
        productoModel.clear();
        orderProductoCombo.removeAllItems();
        for (Producto producto : restaurante.getMenu()) {
            productoModel.addElement(producto);
            orderProductoCombo.addItem(producto);
        }
    }

    private void reloadPedidos() {
        pedidoModel.clear();
        for (int i = 0; i < restaurante.getPedidos().getFacturasSize(); i++) {
            pedidoModel.addElement(restaurante.getPedidos().getFacturas(i));
        }
        reloadFacturaPedidos();
    }

    private void reloadMesaClients(Mesa mesa) {
        mesaClientesModel.clear();
        if (mesa != null) {
            for (Cliente cliente : mesa.getClientes()) {
                mesaClientesModel.addElement(cliente);
            }
        }
    }

    private void populateMesaClientCombo() {
        mesaClienteCombo.removeAllItems();
        for (Cliente cliente : restaurante.getClientes()) {
            mesaClienteCombo.addItem(cliente);
        }
    }

    private void populateClienteMesaCombo() {
        clienteMesaCombo.removeAllItems();
        for (Mesa mesa : restaurante.getMesas()) {
            clienteMesaCombo.addItem(mesa);
        }
        clienteMesaCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Mesa) {
                    int displayIndex = index;
                    if (displayIndex < 0) {
                        displayIndex = clienteMesaCombo.getSelectedIndex();
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

    private void populateOrderCombos() {
        orderClienteCombo.removeAllItems();
        for (Cliente cliente : restaurante.getClientes()) {
            orderClienteCombo.addItem(cliente);
        }
        orderProductoCombo.removeAllItems();
        for (Producto producto : restaurante.getMenu()) {
            orderProductoCombo.addItem(producto);
        }
    }

    private void reloadFacturaPedidos() {
        facturaPedidoModel.removeAllElements();
        for (Pedido pedido : restaurante.getPedidos().getFacturasList()) {
            facturaPedidoModel.addElement(pedido);
        }
        if (facturaPedidoModel.getSize() > 0) {
            facturaPedidoCombo.setSelectedIndex(0);
        }
    }

    private void updateMesaSelection() {
        Mesa mesa = mesaList.getSelectedValue();
        if (mesa != null) {
            mesaCapacidadField.setText(String.valueOf(mesa.getCapacidad()));
            mesaInfoLabel.setText("Libres: " + mesa.getPuestos() + ", Ocupados: " + mesa.getOcupados());
            reloadMesaClients(mesa);
            populateMesaClientCombo();
        } else {
            mesaCapacidadField.setText("");
            mesaInfoLabel.setText("Seleccione una mesa");
            mesaClientesModel.clear();
            mesaClienteCombo.removeAllItems();
        }
    }

    private void updateClienteSelection() {
        Cliente cliente = clienteList.getSelectedValue();
        if (cliente != null) {
            clienteNombreField.setText(cliente.getNombre());
            int mesaIndex = restaurante.getMesaIndexOfCliente(cliente);
            if (mesaIndex >= 0) {
                clienteMesaAsignadaLabel.setText("Mesa " + (mesaIndex + 1));
            } else {
                clienteMesaAsignadaLabel.setText("No asignado");
            }
        } else {
            clienteNombreField.setText("");
            clienteMesaAsignadaLabel.setText("No asignado");
        }
    }

    private void updateProductoSelection() {
        Producto producto = productoList.getSelectedValue();
        if (producto != null) {
            productoNombreField.setText(producto.getNombre());
            productoTipoField.setText(producto.getTipo());
            productoPrecioField.setText(String.valueOf(producto.getPrecio()));
            productoDisponibleCheck.setSelected(producto.isDisponibilidad());
        } else {
            productoNombreField.setText("");
            productoTipoField.setText("");
            productoPrecioField.setText("");
            productoDisponibleCheck.setSelected(false);
        }
    }

    private void updatePedidoSelection() {
        Pedido pedido = pedidoList.getSelectedValue();
        if (pedido != null) {
            orderClienteCombo.setSelectedItem(pedido.getCliente());
            orderEstadoField.setText(pedido.getEstado());
            orderHoraField.setText(pedido.getHora());
            orderObservacionesArea.setText(pedido.getObservaciones());
            reloadPedidoItems(pedido);
            orderTotalLabel.setText(formatCurrency(pedido.getTotal()));
        } else {
            orderEstadoField.setText("Pendiente");
            orderHoraField.setText("");
            orderObservacionesArea.setText("");
            pedidoItemsModel.clear();
            orderTotalLabel.setText("$0.00");
        }
    }

    private void generarFactura() {
        Pedido pedido = (Pedido) facturaPedidoCombo.getSelectedItem();
        if (pedido == null) {
            showError("Seleccione un pedido para generar la factura.");
            return;
        }
        int tipoPago = pagoCombo.getSelectedIndex();
        pedido.setTipo_de_pago(tipoPago);
        pedido.setEstado("Pago");

        facturaPedidoCombo.setSelectedItem(pedido);
        facturaPedidoCombo.repaint();
        pedidoList.repaint();
        updatePedidoSelection();

        StringBuilder factura = new StringBuilder();
        factura.append("Factura para: ").append(pedido.getCliente().getNombre()).append("\n");
        factura.append("Pedido: ").append(pedido.getNombre()).append("\n");
        factura.append("Hora: ").append(pedido.getHora()).append("\n");
        factura.append("Pago: ").append(pagoCombo.getSelectedItem()).append("\n\n");
        factura.append("Productos:\n");
        for (LineaPedido linea : pedido.getFactura()) {
            factura.append("- ")
                    .append(linea.getProducto().getNombre())
                    .append(" x")
                    .append(linea.getCantidad())
                    .append(" : ")
                    .append(formatCurrency(linea.getSubtotal()))
                    .append("\n");
        }
        factura.append("\nSubtotal: ").append(formatCurrency(pedido.CalcularSubtotal())).append("\n");
        factura.append("Impuesto 8%: ").append(formatCurrency(pedido.CalcularTotal() - pedido.CalcularSubtotal())).append("\n");
        factura.append("Total: ").append(formatCurrency(pedido.getTotal())).append("\n");

        facturaArea.setText(factura.toString());
    }

    private void calcularTotalDia() {
        double totalDia = 0;
        for (Pedido pedido : restaurante.getPedidos().getFacturasList()) {
            totalDia += pedido.getTotal();
        }
        totalDiaLabel.setText(formatCurrency(totalDia));
    }

    private String formatCurrency(double value) {
        return String.format("$%.2f", value);
    }

    private void reloadPedidoItems(Pedido pedido) {
        pedidoItemsModel.clear();
        if (pedido != null) {
            for (LineaPedido linea : pedido.getFactura()) {
                pedidoItemsModel.addElement(linea);
            }
        }
    }

    private void addMesa() {
        try {
            int capacidad = Integer.parseInt(mesaCapacidadField.getText().trim());
            if (capacidad <= 0) {
                showError("La capacidad debe ser un número mayor que cero.");
                return;
            }
            restaurante.addMesa(1, capacidad);
            reloadMesas();
        } catch (NumberFormatException ex) {
            showError("Ingrese un valor numérico válido para la capacidad.");
        }
    }

    private void updateMesa() {
        Mesa mesa = mesaList.getSelectedValue();
        if (mesa == null) {
            showError("Seleccione una mesa para modificarla.");
            return;
        }
        try {
            int capacidad = Integer.parseInt(mesaCapacidadField.getText().trim());
            if (capacidad <= 0) {
                showError("La capacidad debe ser un número mayor que cero.");
                return;
            }
            mesa.setCapacidad(capacidad);
            restaurante.saveMesas();
            reloadMesas();
            mesaList.setSelectedValue(mesa, true);
        } catch (NumberFormatException ex) {
            showError("Ingrese un valor numérico válido para la capacidad.");
        }
    }

    private void deleteMesa() {
        int index = mesaList.getSelectedIndex();
        if (index < 0) {
            showError("Seleccione una mesa para eliminarla.");
            return;
        }
        restaurante.deleteMesa(index);
        reloadMesas();
    }

    private void assignClienteAMesa() {
        int mesaIndex = mesaList.getSelectedIndex();
        Cliente cliente = (Cliente) mesaClienteCombo.getSelectedItem();
        if (mesaIndex < 0 || cliente == null) {
            showError("Seleccione una mesa y un cliente para asignar.");
            return;
        }
        restaurante.assignClienteToMesa(mesaIndex, cliente);
        reloadMesaClients(restaurante.getMesa(mesaIndex));
    }

    private void removeClienteDeMesa() {
        int mesaIndex = mesaList.getSelectedIndex();
        Cliente cliente = mesaClientesList.getSelectedValue();
        if (mesaIndex < 0 || cliente == null) {
            showError("Seleccione una mesa y un cliente para quitar.");
            return;
        }
        restaurante.removeClienteFromMesa(mesaIndex, cliente);
        reloadMesaClients(restaurante.getMesa(mesaIndex));
    }

    private void addCliente() {
        String nombre = clienteNombreField.getText().trim();
        if (nombre.isEmpty()) {
            showError("Ingrese el nombre del cliente.");
            return;
        }
        Cliente cliente = new Cliente(nombre, true);
        restaurante.addCliente(cliente);
        reloadClientes();
    }

    private void updateCliente() {
        Cliente cliente = clienteList.getSelectedValue();
        if (cliente == null) {
            showError("Seleccione un cliente para modificarlo.");
            return;
        }
        String nombre = clienteNombreField.getText().trim();
        if (nombre.isEmpty()) {
            showError("Ingrese el nombre del cliente.");
            return;
        }
        cliente.setNombre(nombre);
        reloadClientes();
        clienteList.setSelectedValue(cliente, true);
        populateOrderCombos();
    }

    private void deleteCliente() {
        int index = clienteList.getSelectedIndex();
        if (index < 0) {
            showError("Seleccione un cliente para eliminarlo.");
            return;
        }
        restaurante.deleteCliente(index);
        reloadClientes();
        populateOrderCombos();
    }

    private void assignClienteToMesaDesdeCliente() {
        Cliente cliente = clienteList.getSelectedValue();
        Mesa mesa = (Mesa) clienteMesaCombo.getSelectedItem();
        int mesaIndex = clienteMesaCombo.getSelectedIndex();
        if (cliente == null || mesa == null || mesaIndex < 0) {
            showError("Seleccione un cliente y una mesa para asignar.");
            return;
        }
        restaurante.assignClienteToMesa(mesaIndex, cliente);
        reloadAll();
        clienteList.setSelectedValue(cliente, true);
    }

    private void addProducto() {
        String nombre = productoNombreField.getText().trim();
        String tipo = productoTipoField.getText().trim();
        String precioTexto = productoPrecioField.getText().trim();
        if (nombre.isEmpty() || tipo.isEmpty() || precioTexto.isEmpty()) {
            showError("Ingrese nombre, tipo y precio del producto.");
            return;
        }
        try {
            double precio = Double.parseDouble(precioTexto);
            if (precio < 0) {
                showError("El precio debe ser un valor positivo.");
                return;
            }
            Producto producto = new Producto(tipo, precio, nombre, productoDisponibleCheck.isSelected());
            restaurante.addProducto(producto);
            reloadProductos();
            populateOrderCombos();
        } catch (NumberFormatException ex) {
            showError("Ingrese un precio válido.");
        }
    }

    private void updateProducto() {
        Producto producto = productoList.getSelectedValue();
        if (producto == null) {
            showError("Seleccione un producto para modificarlo.");
            return;
        }
        String nombre = productoNombreField.getText().trim();
        String tipo = productoTipoField.getText().trim();
        String precioTexto = productoPrecioField.getText().trim();
        if (nombre.isEmpty() || tipo.isEmpty() || precioTexto.isEmpty()) {
            showError("Ingrese nombre, tipo y precio del producto.");
            return;
        }
        try {
            double precio = Double.parseDouble(precioTexto);
            if (precio < 0) {
                showError("El precio debe ser un valor positivo.");
                return;
            }
            producto.setNombre(nombre);
            producto.setTipo(tipo);
            producto.setPrecio(precio);
            producto.setDisponibilidad(productoDisponibleCheck.isSelected());
            restaurante.saveMenu();
            reloadProductos();
            productoList.setSelectedValue(producto, true);
            populateOrderCombos();
        } catch (NumberFormatException ex) {
            showError("Ingrese un precio válido.");
        }
    }

    private void deleteProducto() {
        int index = productoList.getSelectedIndex();
        if (index < 0) {
            showError("Seleccione un producto para eliminarlo.");
            return;
        }
        restaurante.deleteProducto(index);
        reloadProductos();
        populateOrderCombos();
    }

    private void clearMenu() {
        restaurante.clearMenu();
        reloadProductos();
        populateOrderCombos();
    }

    private void createPedido() {
        Cliente cliente = (Cliente) orderClienteCombo.getSelectedItem();
        if (cliente == null) {
            showError("Seleccione un cliente para el pedido.");
            return;
        }
        String estado = orderEstadoField.getText().trim();
        if (estado.isEmpty()) {
            estado = "Pendiente";
        }
        String hora = new SimpleDateFormat("HH:mm").format(new Date());
        String nombre = "Pedido " + (restaurante.getPedidos().getFacturasSize() + 1);
        Pedido pedido = new Pedido(orderObservacionesArea.getText().trim(), estado, hora, cliente, nombre, true);
        restaurante.addPedido(pedido);
        reloadPedidos();
        pedidoList.setSelectedValue(pedido, true);
    }

    private void savePedido() {
        Pedido pedido = pedidoList.getSelectedValue();
        if (pedido == null) {
            showError("Seleccione un pedido para guardar los cambios.");
            return;
        }
        Cliente cliente = (Cliente) orderClienteCombo.getSelectedItem();
        if (cliente == null) {
            showError("Seleccione un cliente para el pedido.");
            return;
        }
        pedido.setCliente(cliente);
        pedido.setEstado(orderEstadoField.getText().trim());
        pedido.setObservaciones(orderObservacionesArea.getText().trim());
        reloadPedidos();
        pedidoList.setSelectedValue(pedido, true);
    }

    private void deletePedido() {
        int index = pedidoList.getSelectedIndex();
        if (index < 0) {
            showError("Seleccione un pedido para eliminarlo.");
            return;
        }
        restaurante.deletePedido(index);
        reloadPedidos();
    }

    private void addProductoAPedido() {
        Pedido pedido = pedidoList.getSelectedValue();
        Producto producto = (Producto) orderProductoCombo.getSelectedItem();
        if (pedido == null || producto == null) {
            showError("Seleccione un pedido y un producto para agregar.");
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(orderCantidadField.getText().trim());
            if (cantidad <= 0) {
                showError("Ingrese una cantidad mayor que cero.");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Ingrese una cantidad válida.");
            return;
        }
        pedido.addProducto(producto, cantidad);
        reloadPedidoItems(pedido);
        orderTotalLabel.setText(formatCurrency(pedido.getTotal()));
    }

    private void removeProductoDePedido() {
        Pedido pedido = pedidoList.getSelectedValue();
        LineaPedido linea = pedidoItemsList.getSelectedValue();
        if (pedido == null || linea == null) {
            showError("Seleccione un pedido y un producto para eliminar.");
            return;
        }
        pedido.deleteProducto(linea);
        reloadPedidoItems(pedido);
        orderTotalLabel.setText(formatCurrency(pedido.getTotal()));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.WARNING_MESSAGE);
    }
}
