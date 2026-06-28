package Pantallas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import Conexion_BD.Conexion;
import com.toedter.calendar.JDateChooser;

public class Pantalla_Ventas extends JFrame implements ActionListener {
    private JPanel pArriba, pAbajo, pCentro, pControles;
    private JLabel lblVentas, lblPedido, lblCliente, lblTotal, lblMetodo, lblEfectivo, lblCambio, lblFecha;
    private JTextField txtCliente, txtPedido, txtEfectivo, txtCambio, txtTotal;
    private JButton btn1, btn2, btn3;
    private JComboBox<String> cmbPago;
    private JDateChooser fechaChooser;
    private JSpinner horaSpinner;
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;

    public Pantalla_Ventas() {
        configFrame();
        initComponents();
    txtEfectivo.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            try {
                double total = Double.parseDouble(txtTotal.getText());
                double recibido = Double.parseDouble(txtEfectivo.getText());
                double cambio = recibido - total;
                txtCambio.setText(String.format("%.2f", cambio));
            } catch (Exception e) {
                txtCambio.setText("0.00");
            }
        }
    });
        setVisible(true);
    }

    public void configFrame() {
        setSize(950, 700);
        setTitle("Restaurante - Ventas");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagenes/logoRestaurante.png"));
        setIconImage(icono);
    }

    public void initComponents() {
        add(Arriba(), BorderLayout.NORTH);
        add(Abajo(), BorderLayout.SOUTH);
        add(Centro(), BorderLayout.CENTER);
    }

    public JPanel Arriba() {
        pArriba = new JPanel();
        pArriba.setBackground(Color.red);
        pArriba.setLayout(new FlowLayout());
        lblVentas = new JLabel("Control de Ventas");
        lblVentas.setForeground(Color.WHITE);
        lblVentas.setFont(new Font("Arial", Font.BOLD, 20));
        pArriba.add(lblVentas);
        return pArriba;
    }

    public JPanel Abajo() {
        pAbajo = new JPanel();
        pAbajo.setBackground(Color.red);
        pAbajo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        btn1 = new JButton("Atrás");
        btn2 = new JButton("Cobrar");
        btn3 = new JButton("Buscar");

        JButton[] arrbtn = new JButton[]{btn1, btn2, btn3};
        int c = 0;
        for (JButton b : arrbtn) {
            gbc.gridx = c;
            b.setFont(new Font("Arial", Font.BOLD, 15));
            b.addActionListener(this);
            pAbajo.add(b, gbc);
            c++;
        }
        return pAbajo;
    }

    public JPanel Centro() {
        pCentro = new JPanel();
        pCentro.setLayout(new BorderLayout());
        pCentro.add(Controles(), BorderLayout.NORTH);
        
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Platillo");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Precio Unit.");
        modeloTabla.addColumn("Subtotal");
        
        tablaVentas = new JTable(modeloTabla);
        JScrollPane sp = new JScrollPane(tablaVentas);
        sp.setBorder(BorderFactory.createTitledBorder("Detalle de Consumo del Pedido"));
        
        pCentro.add(sp, BorderLayout.CENTER); 
        
        return pCentro;
    }

    public JPanel Controles() {
        pControles = new JPanel();
        pControles.setBackground(Color.WHITE);
        pControles.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        lblPedido = new JLabel("No. Pedido:");
        lblPedido.setFont(new Font("Arial", Font.BOLD, 15));
        lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Arial", Font.BOLD, 15));
        pControles.add(lblPedido, gbc);
        gbc.gridy = 1;
        pControles.add(lblCliente, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        txtPedido = new JTextField(10);
        pControles.add(txtPedido, gbc);
        gbc.gridy = 1;
        txtCliente = new JTextField(10);
        txtCliente.setEditable(false); 
        pControles.add(txtCliente, gbc);
        
        gbc.gridx = 2;
        int c = 0;
        lblTotal = new JLabel("TOTAL A PAGAR: $");
        lblMetodo = new JLabel("Método de pago:");
        lblEfectivo = new JLabel("Efectivo recibido $:");
        lblCambio = new JLabel("Cambio $:");
        lblFecha = new JLabel("Fecha de Venta:");
        
        JLabel[] labels = new JLabel[]{lblTotal, lblMetodo, lblEfectivo, lblCambio, lblFecha};
        for (JLabel l : labels) {
            l.setFont(new Font("Arial", Font.BOLD, 15));
            gbc.gridy = c;
            pControles.add(l, gbc);
            c++;
        }
        
        gbc.gridx = 3;
        
        gbc.gridy = 0;
        txtTotal = new JTextField(15);
        txtTotal.setEditable(false);
        txtTotal.setFont(new Font("Arial", Font.BOLD, 14));
        txtTotal.setForeground(Color.RED);
        pControles.add(txtTotal, gbc);
        
        gbc.gridy = 1;
        String[] opciones = new String[]{"Seleccione", "Efectivo", "Tarjeta"};
        cmbPago = new JComboBox<>(opciones);
        pControles.add(cmbPago, gbc);
        
        gbc.gridy = 2;
        txtEfectivo = new JTextField(15);
        pControles.add(txtEfectivo, gbc);
        
        gbc.gridy = 3;
        txtCambio = new JTextField(15);
        txtCambio.setEditable(false); 
        pControles.add(txtCambio, gbc);
        
        gbc.gridy = 4;
        fechaChooser = new JDateChooser();
        fechaChooser.setDate(new Date());
        fechaChooser.setDateFormatString("yyyy-MM-dd");
        
        SpinnerDateModel sm = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
        horaSpinner = new JSpinner(sm);
        JSpinner.DateEditor de = new JSpinner.DateEditor(horaSpinner, "HH:mm:ss");
        horaSpinner.setEditor(de);
        
        JPanel pFechaHora = new JPanel(new GridLayout(1, 2));
        pFechaHora.setBackground(Color.WHITE); 
        pFechaHora.add(fechaChooser);
        pFechaHora.add(horaSpinner);
        
        pControles.add(pFechaHora, gbc);

        return pControles;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn3) { 
            buscarPedido();
        } else if (e.getSource() == btn2) { 
            registrarCobro();
        } else if (e.getSource() == btn1) { 
            this.dispose(); 
            new Pantalla_Principal();
        }
    }

    private void buscarPedido() {
        String idBuscado = txtPedido.getText().trim();
        if (idBuscado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de pedido.");
            return;
        }

        modeloTabla.setRowCount(0); 
        double sumaTotal = 0;

        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sqlPedido = "SELECT c.nombre FROM pedidos p JOIN clientes c ON p.id_cliente = c.id_cliente WHERE p.id_pedido = ?";
                PreparedStatement pstP = cn.prepareStatement(sqlPedido);
                pstP.setInt(1, Integer.parseInt(idBuscado));
                ResultSet rsP = pstP.executeQuery();

                if (rsP.next()) {
                    txtCliente.setText(rsP.getString("nombre"));

                    String sqlDetalle = "SELECT m.nombre_platillo, dp.cantidad, dp.precio_unitario_historico " +
                                       "FROM detalle_pedido dp JOIN menu m ON dp.id_platillo = m.id_platillo " +
                                       "WHERE dp.id_pedido = ?";
                    PreparedStatement pstD = cn.prepareStatement(sqlDetalle);
                    pstD.setInt(1, Integer.parseInt(idBuscado));
                    ResultSet rsD = pstD.executeQuery();

                    while (rsD.next()) {
                        double precio = rsD.getDouble("precio_unitario_historico");
                        int cant = rsD.getInt("cantidad");
                        double subtotal = precio * cant;
                        sumaTotal += subtotal;

                        modeloTabla.addRow(new Object[]{
                            rsD.getString("nombre_platillo"), cant, precio, subtotal
                        });
                    }
                    txtTotal.setText(String.valueOf(sumaTotal));
                } else {
                    JOptionPane.showMessageDialog(this, "El pedido no existe o ya fue borrado.");
                }
                cn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage());
            }
        }
    }

    private void registrarCobro() {
        if (txtPedido.getText().isEmpty() || cmbPago.getSelectedIndex() == 0 || txtTotal.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Faltan datos para realizar el cobro.");
            return;
        }

        double total = Double.parseDouble(txtTotal.getText());
        String metodo = cmbPago.getSelectedItem().toString();
        
        if (metodo.equals("Efectivo")) {
            double efectivo = Double.parseDouble(txtEfectivo.getText());
            if (efectivo < total) {
                JOptionPane.showMessageDialog(this, "El efectivo es insuficiente.");
                return;
            }
        }

        Connection cn = Conexion.obtenerConexion();
        try {
            cn.setAutoCommit(false); 

            String sqlVenta = "INSERT INTO ventas (id_pedido, monto_total, metodo_pago, fecha_pago) VALUES (?,?,?,?)";
            PreparedStatement pstV = cn.prepareStatement(sqlVenta);
            pstV.setInt(1, Integer.parseInt(txtPedido.getText()));
            pstV.setDouble(2, total);
            pstV.setString(3, metodo);
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fechaHora = new java.text.SimpleDateFormat("yyyy-MM-dd").format(fechaChooser.getDate()) + " " +
                               new java.text.SimpleDateFormat("HH:mm:ss").format(horaSpinner.getValue());
            pstV.setString(4, fechaHora);
            pstV.executeUpdate();

            PreparedStatement pstU = cn.prepareStatement("UPDATE pedidos SET estado = 'Pagado' WHERE id_pedido = ?");
            pstU.setInt(1, Integer.parseInt(txtPedido.getText()));
            pstU.executeUpdate();

            cn.commit();
            JOptionPane.showMessageDialog(this, "¡Venta realizada con éxito!");
            limpiarCaja();
            cn.close();
        } catch (Exception ex) {
            try { cn.rollback(); } catch (Exception e) {}
            JOptionPane.showMessageDialog(this, "Error al cobrar: " + ex.getMessage());
        }
    }

    private void limpiarCaja() {
        txtPedido.setText("");
        txtCliente.setText("");
        txtTotal.setText("");
        txtEfectivo.setText("");
        txtCambio.setText("");
        cmbPago.setSelectedIndex(0);
        modeloTabla.setRowCount(0);
        fechaChooser.setDate(new java.util.Date());
    }
    
}