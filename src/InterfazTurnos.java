import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class InterfazTurnos extends JFrame {
    
    private GestorTurnos gestor;
    private JTextField txtNombre, txtId, txtBuscarId;
    private JComboBox<String> comboBoxTramite;
    private JCheckBox chkPreferencial;
    private JTextArea areaMostrar;
    
    private JLabel lblEnEspera;
    private JLabel lblAtendidos;
    private JLabel lblReloj;

    // Paleta de Colores Corporativos
    private final Color COLOR_PRIMARIO = new Color(13, 71, 161); // Azul Oscuro
    private final Color COLOR_SECUNDARIO = new Color(245, 245, 245); // Gris muy claro
    private final Color COLOR_EXITO = new Color(46, 125, 50); // Verde
    private final Color COLOR_PELIGRO = new Color(198, 40, 40); // Rojo
    private final Color COLOR_ADVERTENCIA = new Color(255, 143, 0); // Naranja
    private final Color COLOR_TEXTO_CLARO = Color.WHITE;
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 13);

    public InterfazTurnos() {
        gestor = new GestorTurnos();
        configurarVentana();
        crearComponentes();
        iniciarReloj();
        actualizarMetricas(); 
    }

    private void configurarVentana() {
        setTitle("Sistema de Turnos - Banco Central S.A.");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_SECUNDARIO);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gestor.guardarFilaEnArchivo(); 
                JOptionPane.showMessageDialog(null, "Datos de la fila guardados exitosamente.\nCerrando sistema...");
                System.exit(0); 
            }
        });
    }

    // Método auxiliar para crear botones bonitos y que Windows no los pinte de gris
    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // Pinta el fondo del color deseado
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Pinta el texto
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(getText());
                int stringHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 2);
                g2.dispose();
            }
        };
        boton.setBackground(colorFondo);
        boton.setForeground(COLOR_TEXTO_CLARO);
        boton.setFont(FUENTE_BOTON);
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(10, 15, 10, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(false); // Desactivamos el pintado por defecto
        boton.setContentAreaFilled(false); // Desactivamos el relleno por defecto
        return boton;
    }

    private void crearComponentes() {
        // ==========================================
        // 1. PANEL NORTE: CABECERA CORPORATIVA
        // ==========================================
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBackground(COLOR_PRIMARIO);
        panelCabecera.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblTitulo = new JLabel("🏦 BANCO CENTRAL");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_TEXTO_CLARO);
        
        JPanel panelMetricas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelMetricas.setBackground(COLOR_PRIMARIO);
        
        lblEnEspera = new JLabel("En Espera: 0");
        lblEnEspera.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblEnEspera.setForeground(new Color(255, 235, 59)); 
        
        lblAtendidos = new JLabel("Atendidos: 0");
        lblAtendidos.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAtendidos.setForeground(new Color(129, 199, 132)); 
        
        panelMetricas.add(lblEnEspera);
        panelMetricas.add(lblAtendidos);

        panelCabecera.add(lblTitulo, BorderLayout.WEST);
        panelCabecera.add(panelMetricas, BorderLayout.EAST);

        // ==========================================
        // 2. PANEL CENTRAL: FORMULARIO Y MONITOR
        // ==========================================
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(COLOR_SECUNDARIO);
        panelContenido.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Formulario de Registro
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBackground(COLOR_SECUNDARIO);
        panelRegistro.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(COLOR_PRIMARIO, 1), " Registrar Nuevo Cliente ", 
            0, 0, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARIO));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNombre = new JTextField();
        txtNombre.setFont(FUENTE_NORMAL);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY, 1), new EmptyBorder(5, 5, 5, 5)));
        panelRegistro.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Identificación:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtId = new JTextField();
        txtId.setFont(FUENTE_NORMAL);
        txtId.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY, 1), new EmptyBorder(5, 5, 5, 5)));
        panelRegistro.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelRegistro.add(new JLabel("Trámite:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        String[] tramites = {"Retiro", "Consignación", "Asesoría", "Pago de Servicios", "Créditos"};
        comboBoxTramite = new JComboBox<>(tramites);
        comboBoxTramite.setFont(FUENTE_NORMAL);
        panelRegistro.add(comboBoxTramite, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel panelBotonRegistro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonRegistro.setBackground(COLOR_SECUNDARIO);
        chkPreferencial = new JCheckBox("Cliente Preferencial");
        chkPreferencial.setBackground(COLOR_SECUNDARIO);
        chkPreferencial.setFont(FUENTE_NORMAL);
        panelBotonRegistro.add(chkPreferencial);

        JButton btnAgregar = crearBoton("➕ Generar Ticket", COLOR_EXITO);
        panelBotonRegistro.add(btnAgregar);
        panelRegistro.add(panelBotonRegistro, gbc);

        // Monitor del Sistema
        areaMostrar = new JTextArea();
        areaMostrar.setEditable(false);
        areaMostrar.setFont(new Font("Consolas", Font.PLAIN, 15));
        areaMostrar.setBackground(Color.WHITE);
        areaMostrar.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(areaMostrar);
        scroll.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(COLOR_PRIMARIO, 1), " Monitor del Sistema ", 
            0, 0, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARIO));

        panelContenido.add(panelRegistro);
        panelContenido.add(Box.createVerticalStrut(15));
        panelContenido.add(scroll);

        // ==========================================
        // 3. PANEL SUR: ACCIONES DEL CAJERO
        // ==========================================
        JPanel panelAcciones = new JPanel(new GridLayout(3, 3, 10, 10));
        panelAcciones.setBackground(COLOR_SECUNDARIO);
        panelAcciones.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(COLOR_PRIMARIO, 1), " Acciones del Cajero ", 
            0, 0, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARIO));

        JButton btnAtender = crearBoton("🔔 Atender Siguiente", COLOR_PRIMARIO);
        JButton btnMostrarFila = crearBoton("📋 Mostrar Fila", COLOR_PRIMARIO);
        JButton btnHistorial = crearBoton("📜 Ver Historial", COLOR_PRIMARIO);
        JButton btnGestionarHistorial = crearBoton("✏️ Editar Historial", COLOR_ADVERTENCIA); 
        JButton btnCancelar = crearBoton("❌ Cancelar Turno", COLOR_PELIGRO);
        JButton btnEstadisticas = crearBoton("📊 Estadísticas", COLOR_PRIMARIO);

        // Buscador Integrado
        JPanel panelBuscar = new JPanel(new BorderLayout(5, 0));
        panelBuscar.setBackground(COLOR_SECUNDARIO);
        txtBuscarId = new JTextField();
        txtBuscarId.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY, 1), new EmptyBorder(5, 5, 5, 5)));
        JButton btnBuscar = crearBoton("🔍 Buscar", COLOR_PRIMARIO);
        btnBuscar.setBorder(new EmptyBorder(5, 10, 5, 10));
        panelBuscar.add(txtBuscarId, BorderLayout.CENTER);
        panelBuscar.add(btnBuscar, BorderLayout.EAST);

        panelAcciones.add(btnAtender);
        panelAcciones.add(btnMostrarFila);
        panelAcciones.add(btnHistorial);
        panelAcciones.add(btnGestionarHistorial); 
        panelAcciones.add(panelBuscar); 
        panelAcciones.add(btnCancelar);
        panelAcciones.add(btnEstadisticas);

        // Reloj
        JPanel panelReloj = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelReloj.setBackground(COLOR_SECUNDARIO);
        lblReloj = new JLabel("Hora: --:--:--");
        lblReloj.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblReloj.setForeground(Color.DARK_GRAY);
        panelReloj.add(lblReloj);

        JPanel panelSurCompleto = new JPanel(new BorderLayout());
        panelSurCompleto.setBackground(COLOR_SECUNDARIO);
        panelSurCompleto.setBorder(new EmptyBorder(0, 25, 10, 25));
        panelSurCompleto.add(panelAcciones, BorderLayout.CENTER);
        panelSurCompleto.add(panelReloj, BorderLayout.SOUTH);

        // Agregar todo al JFrame principal
        add(panelCabecera, BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);
        add(panelSurCompleto, BorderLayout.SOUTH);

        // ==========================================
        // EVENTOS
        // ==========================================
        
        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String id = txtId.getText().trim();
            String tramite = comboBoxTramite.getSelectedItem().toString();
            boolean preferencial = chkPreferencial.isSelected();

            if (nombre.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Complete el nombre y la identificación.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (gestor.buscarCliente(id) != null) {
                JOptionPane.showMessageDialog(null, 
                    "❌ ERROR:\nLa identificación '" + id + "' ya está en la fila actual.", 
                    "Identificación Duplicada", JOptionPane.ERROR_MESSAGE);
                return; 
            }

            String turnoAsignado = gestor.agregarCliente(nombre, id, tramite, preferencial);
            actualizarMetricas(); 
            limpiarCampos();
            
            String mensajeTicket = "====================================\n"
                                 + "       BANCO CENTRAL S.A.           \n"
                                 + "       TICKET DE TURNO              \n"
                                 + "====================================\n"
                                 + " TURNO ASIGNADO: " + turnoAsignado + "\n"
                                 + "------------------------------------\n"
                                 + " Cliente: " + nombre + "\n"
                                 + " Cédula:  " + id + "\n"
                                 + " Trámite: " + tramite + "\n"
                                 + " Prioridad: " + (preferencial ? "SÍ (Preferencial)" : "No") + "\n"
                                 + " Hora: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n"
                                 + "====================================\n"
                                 + " Por favor, espere su turno en pantalla.";
            
            JOptionPane.showMessageDialog(null, mensajeTicket, "Impresión de Ticket", JOptionPane.INFORMATION_MESSAGE);

            areaMostrar.setText("✅ Turno " + turnoAsignado + " generado para: " + nombre + "\n\n");
            gestor.mostrarFilaEnArea(areaMostrar, "--- FILA ACTUAL ---");
        });

        btnAtender.addActionListener(e -> {
            gestor.atenderClienteEnArea(areaMostrar);
            actualizarMetricas();
        });

        btnMostrarFila.addActionListener(e -> {
            areaMostrar.setText("");
            gestor.mostrarFilaEnArea(areaMostrar, "--- FILA ACTUAL ---");
        });

        btnHistorial.addActionListener(e -> gestor.mostrarHistorialEnArea(areaMostrar));

        btnGestionarHistorial.addActionListener(e -> abrirVentanaHistorial());

        btnBuscar.addActionListener(e -> {
            String idBuscar = txtBuscarId.getText().trim();
            if (idBuscar.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese una identificación en el buscador.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Cliente encontrado = gestor.buscarCliente(idBuscar);
            if (encontrado != null) {
                JOptionPane.showMessageDialog(null, "Cliente encontrado en la fila:\n\n" + encontrado, "Resultado", JOptionPane.INFORMATION_MESSAGE);
                txtBuscarId.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "El cliente no está en la fila actual.", "Sin resultados", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> {
            String idCancel = JOptionPane.showInputDialog("Ingrese la identificación del turno a cancelar:");
            if (idCancel != null && !idCancel.trim().isEmpty()) {
                if (gestor.cancelarTurno(idCancel.trim())) {
                    JOptionPane.showMessageDialog(null, "Turno cancelado.");
                    actualizarMetricas();
                    areaMostrar.setText("❌ Turno cancelado.\n\n");
                    gestor.mostrarFilaEnArea(areaMostrar, "--- FILA ACTUAL ---");
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEstadisticas.addActionListener(e -> gestor.mostrarEstadisticas(areaMostrar));
    }

    // ==========================================
    // VENTANA PARA GESTIONAR EL HISTORIAL (CON BUSCADOR INTELIGENTE)
    // ==========================================
    private void abrirVentanaHistorial() {
        List<Cliente> historialOriginal = gestor.getHistorialAtendidos();
        
        if (historialOriginal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aún no hay clientes en el historial para editar.", "Historial Vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog ventanaHistorial = new JDialog(this, "Gestión de Historial", true);
        ventanaHistorial.setSize(650, 500);
        ventanaHistorial.setLocationRelativeTo(this);
        ventanaHistorial.getContentPane().setBackground(COLOR_SECUNDARIO);
        ventanaHistorial.setLayout(new BorderLayout(10, 10)); 

        // Panel principal con borde (para evitar el error de setBorder en JDialog)
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(COLOR_SECUNDARIO);
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10)); 

        // --- 1. PANEL SUPERIOR: BUSCADOR ---
        JPanel panelBuscador = new JPanel(new BorderLayout(10, 10));
        panelBuscador.setBackground(COLOR_SECUNDARIO);
        panelBuscador.add(new JLabel("🔍 Buscar por Cédula/Nombre:"), BorderLayout.WEST);
        
        JTextField txtFiltroHistorial = new JTextField();
        txtFiltroHistorial.setFont(FUENTE_NORMAL);
        txtFiltroHistorial.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_PRIMARIO, 1), new EmptyBorder(8, 8, 8, 8)));
        panelBuscador.add(txtFiltroHistorial, BorderLayout.CENTER);
        
        panelPrincipal.add(panelBuscador, BorderLayout.NORTH);

        // --- 2. PANEL CENTRAL: LISTA ---
        DefaultListModel<Cliente> modeloLista = new DefaultListModel<>();
        for (Cliente c : historialOriginal) {
            modeloLista.addElement(c);
        }

        JList<Cliente> listaVisual = new JList<>(modeloLista);
        listaVisual.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollLista = new JScrollPane(listaVisual);
        scrollLista.setBorder(new LineBorder(COLOR_PRIMARIO, 1));
        panelPrincipal.add(scrollLista, BorderLayout.CENTER);

        // --- EVENTO DEL BUSCADOR (Filtra en tiempo real) ---
        txtFiltroHistorial.getDocument().addDocumentListener(new DocumentListener() {
            private void filtrar() {
                String textoFiltro = txtFiltroHistorial.getText().trim().toLowerCase();
                modeloLista.clear(); 
                
                for (Cliente c : historialOriginal) {
                    if (c.getIdentificacion().toLowerCase().contains(textoFiltro) || 
                        c.getNombre().toLowerCase().contains(textoFiltro)) {
                        modeloLista.addElement(c);
                    }
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        // --- 3. PANEL INFERIOR: BOTÓN EDITAR ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setBackground(COLOR_SECUNDARIO);
        JButton btnEditar = crearBoton("✏️ Editar Cliente Seleccionado", COLOR_ADVERTENCIA);
        panelBotones.add(btnEditar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Agregamos el panel principal a la ventana
        ventanaHistorial.add(panelPrincipal);

        btnEditar.addActionListener(e -> {
            Cliente seleccionado = listaVisual.getSelectedValue();
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(ventanaHistorial, "Por favor, seleccione un cliente de la lista.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nuevoNombre = JOptionPane.showInputDialog(ventanaHistorial, "Editar Nombre:", seleccionado.getNombre());
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return; 

            String nuevoTramite = JOptionPane.showInputDialog(ventanaHistorial, "Editar Trámite:", seleccionado.getTipoTramite());
            if (nuevoTramite == null || nuevoTramite.trim().isEmpty()) return;

            seleccionado.setNombre(nuevoNombre);
            seleccionado.setTipoTramite(nuevoTramite);

            listaVisual.repaint(); 
            
            JOptionPane.showMessageDialog(ventanaHistorial, "Cliente actualizado exitosamente en el historial.");
        });

        ventanaHistorial.setVisible(true);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtId.setText("");
        comboBoxTramite.setSelectedIndex(0);
        chkPreferencial.setSelected(false);
        txtNombre.requestFocus();
    }

    private void actualizarMetricas() {
        lblEnEspera.setText("En Espera: " + gestor.getCantidadEnEspera());
        lblAtendidos.setText("Atendidos: " + gestor.getTotalAtendidos());
    }

    private void iniciarReloj() {
        Timer timer = new Timer(1000, e -> {
            LocalTime ahora = LocalTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
            lblReloj.setText("Hora: " + ahora.format(formato));
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfazTurnos().setVisible(true);
        });
    }
}