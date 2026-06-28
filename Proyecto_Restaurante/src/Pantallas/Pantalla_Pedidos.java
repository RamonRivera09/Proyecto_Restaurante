package Pantallas;

import Conexion_BD.Conexion;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Pantalla_Pedidos extends JFrame implements ActionListener {

    private JPanel pArriba, pAbajo, pCentro, Controles;
    private JButton btn1, btn2, btn3, btn4, btn5;
    private JLabel lblPedidos, lblNoPedido, lblNomClie, lblNomEmp, lblNomPla, lblCantidad, lblPrecio, lblEstado, lblFecha;
    private JTextField txtPedido, txtCliente, txtEmpleado, txtPlatillo, txtCantidad, txtPrecio, txtEstado;

    private JDateChooser fechaChooser;
    private JSpinner horaSpinner;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private int idPedidoSeleccionado = 0; // Guarda el ID para actualizar

    public Pantalla_Pedidos() {
        configFrame();
        initComponents();
        cargarDatosTabla();
        setVisible(true);
    }

    public void configFrame() {
        setSize(950, 700);
        setTitle("Restaurante - Pedidos");
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
        lblPedidos = new JLabel("Control de Pedidos");
        lblPedidos.setForeground(Color.WHITE);
        lblPedidos.setFont(new Font("Arial", Font.BOLD, 20));
        pArriba.add(lblPedidos);
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
        btn5 = new JButton("Registrar");
        btn2 = new JButton("Buscar");
        btn3 = new JButton("Actualizar");
        btn4 = new JButton("Eliminar");

        JButton[] arrbtn = new JButton[]{btn1, btn5, btn2, btn3, btn4};
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
        modeloTabla.addColumn("No. Pedido");
        modeloTabla.addColumn("Cliente");
        modeloTabla.addColumn("Empleado");
        modeloTabla.addColumn("Platillo");
        modeloTabla.addColumn("Cant.");
        modeloTabla.addColumn("Precio Unit.");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Fecha/Hora");

        tablaPedidos = new JTable(modeloTabla);
        JScrollPane sp = new JScrollPane(tablaPedidos);
        sp.setBackground(Color.WHITE);
        sp.setBorder(BorderFactory.createTitledBorder("Historial de Pedidos"));

        tablaPedidos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tablaPedidos.getSelectedRow();
                if (fila != -1) {
                    idPedidoSeleccionado = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                    txtPedido.setText(String.valueOf(idPedidoSeleccionado));
                    txtCliente.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtEmpleado.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtPlatillo.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtCantidad.setText(modeloTabla.getValueAt(fila, 4).toString());
                    txtPrecio.setText(modeloTabla.getValueAt(fila, 5).toString());
                    txtEstado.setText(modeloTabla.getValueAt(fila, 6).toString());

                    txtCliente.setEditable(false);
                    txtEmpleado.setEditable(false);
                }
            }
        });

        pCentro.add(sp, BorderLayout.CENTER);
        return pCentro;
    }

    public JPanel Controles() {
        Controles = new JPanel();
        Controles.setBackground(Color.WHITE);
        Controles.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        lblNoPedido = new JLabel("No. Pedido:");
        lblNomClie = new JLabel("Cliente que ordena:");
        lblNomEmp = new JLabel("Empleado que atiende:");
        lblNomPla = new JLabel("Platillo que ordena:");

        int c = 0;
        gbc.gridx = 0;
        JLabel[] labels = new JLabel[]{lblNoPedido, lblNomClie, lblNomEmp, lblNomPla};
        for (JLabel l : labels) {
            gbc.gridy = c;
            l.setFont(new Font("Arial", Font.BOLD, 15));
            Controles.add(l, gbc);
            c++;
        }

        c = 0;
        gbc.gridx = 2;
        lblCantidad = new JLabel("Cantidad:");
        lblPrecio = new JLabel("Precio Unitario:");
        lblEstado = new JLabel("Estado:");
        lblFecha = new JLabel("Fecha y Hora:");

        JLabel[] labels2 = new JLabel[]{lblCantidad, lblPrecio, lblEstado, lblFecha};
        for (JLabel ll : labels2) {
            gbc.gridy = c;
            ll.setFont(new Font("Arial", Font.BOLD, 15));
            Controles.add(ll, gbc);
            c++;
        }

        c = 0;
        gbc.gridx = 1;
        txtPedido = new JTextField(15);
        txtPedido.setEditable(false);
        txtCliente = new JTextField(15);
        txtEmpleado = new JTextField(15);
        txtPlatillo = new JTextField(15);

        JTextField[] cajas = new JTextField[]{txtPedido, txtCliente, txtEmpleado, txtPlatillo};
        for (JTextField t : cajas) {
            gbc.gridy = c;
            Controles.add(t, gbc);
            c++;
        }

        c = 0;
        gbc.gridx = 3;
        txtCantidad = new JTextField(15);
        txtPrecio = new JTextField(15);
        txtEstado = new JTextField(15);

        JTextField[] cajas2 = new JTextField[]{txtCantidad, txtPrecio, txtEstado};
        for (JTextField t2 : cajas2) {
            gbc.gridy = c;
            Controles.add(t2, gbc);
            c++;
        }

        gbc.gridy = c;

        fechaChooser = new JDateChooser();
        fechaChooser.setDate(new Date());
        fechaChooser.setDateFormatString("yyyy-MM-dd");

        SpinnerDateModel sm = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
        horaSpinner = new JSpinner(sm);
        JSpinner.DateEditor de = new JSpinner.DateEditor(horaSpinner, "HH:mm:ss");
        horaSpinner.setEditor(de);

        JPanel pFechaHora = new JPanel(new GridLayout(1, 2));
        pFechaHora.add(fechaChooser);
        pFechaHora.add(horaSpinner);

        Controles.add(pFechaHora, gbc);

        return Controles;
    }

    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "SELECT p.id_pedido, c.nombre AS cliente, e.nombre AS empleado, "
                        + "m.nombre_platillo, dp.cantidad, dp.precio_unitario_historico, p.estado, p.fecha_hora "
                        + "FROM pedidos p "
                        + "JOIN clientes c ON p.id_cliente = c.id_cliente "
                        + "JOIN empleados e ON p.id_empleado = e.id_empleado "
                        + "JOIN detalle_pedido dp ON p.id_pedido = dp.id_pedido "
                        + "JOIN menu m ON dp.id_platillo = m.id_platillo";
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{
                        rs.getInt("id_pedido"), rs.getString("cliente"), rs.getString("empleado"),
                        rs.getString("nombre_platillo"), rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario_historico"), rs.getString("estado"), rs.getString("fecha_hora")
                    });
                }
                cn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void registrarPedido() {
        if (txtCliente.getText().isEmpty() || txtEmpleado.getText().isEmpty()
                || txtPlatillo.getText().isEmpty() || txtCantidad.getText().isEmpty()
                || txtPrecio.getText().isEmpty() || txtEstado.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llene todos los campos para registrar el pedido.");
            return;
        }

        int idEmp = obtenerIdEmpleado(txtEmpleado.getText().trim());
        if (idEmp == -1) {
            JOptionPane.showMessageDialog(this, "El empleado no está registrado en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idPla = obtenerIdPlatillo(txtPlatillo.getText().trim());
        if (idPla == -1) {
            JOptionPane.showMessageDialog(this, "El platillo no existe en el menú.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idClie = validarOInsertarCliente(txtCliente.getText().trim());

        SimpleDateFormat sdfF = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm:ss");
        String fechaHora = sdfF.format(fechaChooser.getDate()) + " " + sdfH.format(horaSpinner.getValue());

        Connection cn = Conexion.obtenerConexion();
        try {
            cn.setAutoCommit(false);

            String sqlPedido = "INSERT INTO pedidos (id_cliente, id_empleado, estado, fecha_hora) VALUES (?,?,?,?)";
            PreparedStatement pst1 = cn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            pst1.setInt(1, idClie);
            pst1.setInt(2, idEmp);
            pst1.setString(3, txtEstado.getText());
            pst1.setString(4, fechaHora);
            pst1.executeUpdate();

            ResultSet rs = pst1.getGeneratedKeys();
            int idNuevoPedido = 0;
            if (rs.next()) {
                idNuevoPedido = rs.getInt(1);
            }

            String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, id_platillo, cantidad, precio_unitario_historico) VALUES (?,?,?,?)";
            PreparedStatement pst2 = cn.prepareStatement(sqlDetalle);
            pst2.setInt(1, idNuevoPedido);
            pst2.setInt(2, idPla);
            pst2.setInt(3, Integer.parseInt(txtCantidad.getText()));
            pst2.setDouble(4, Double.parseDouble(txtPrecio.getText()));
            pst2.executeUpdate();

            cn.commit();

            JOptionPane.showMessageDialog(this, "Pedido registrado exitosamente.");
            cargarDatosTabla();
            limpiarCampos();
            cn.close();
        } catch (Exception e) {
            try {
                if (cn != null) {
                    cn.rollback();
                }
            } catch (SQLException ex) {
            } // Si hay error, deshacemos todo
            JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage());
        }
    }

    private void actualizarRegistro() {
        if (idPedidoSeleccionado == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido de la tabla para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPla = obtenerIdPlatillo(txtPlatillo.getText().trim());
        if (idPla == -1) {
            JOptionPane.showMessageDialog(this, "El platillo especificado no existe en el menú.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat sdfF = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm:ss");
        String fechaHoraStr = sdfF.format(fechaChooser.getDate()) + " " + sdfH.format(horaSpinner.getValue());

        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                cn.setAutoCommit(false);

                String sql1 = "UPDATE pedidos SET estado=?, fecha_hora=? WHERE id_pedido=?";
                PreparedStatement pst1 = cn.prepareStatement(sql1);
                pst1.setString(1, txtEstado.getText());
                pst1.setString(2, fechaHoraStr);
                pst1.setInt(3, idPedidoSeleccionado);
                pst1.executeUpdate();

                String sql2 = "UPDATE detalle_pedido SET id_platillo=?, cantidad=?, precio_unitario_historico=? WHERE id_pedido=?";
                PreparedStatement pst2 = cn.prepareStatement(sql2);
                pst2.setInt(1, idPla);
                pst2.setInt(2, Integer.parseInt(txtCantidad.getText()));
                pst2.setDouble(3, Double.parseDouble(txtPrecio.getText()));
                pst2.setInt(4, idPedidoSeleccionado);
                pst2.executeUpdate();

                cn.commit();

                JOptionPane.showMessageDialog(this, "Pedido actualizado. Recuerde que el Cliente y Empleado no son modificables.");
                cargarDatosTabla();
                limpiarCampos();
                cn.close();
            } catch (SQLException ex) {
                try {
                    cn.rollback();
                } catch (SQLException e) {
                }
                JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Verifique que Cantidad y Precio sean números.");
            }
        }
    }

    private void eliminarRegistro() {
        if (idPedidoSeleccionado == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pedido de la tabla para eliminarlo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el pedido No. " + idPedidoSeleccionado + "?\nEsto también eliminará la Venta y los Detalles asociados.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

        if (conf == JOptionPane.YES_OPTION) {
            Connection cn = Conexion.obtenerConexion();
            try {
                cn.setAutoCommit(false);

                PreparedStatement pst1 = cn.prepareStatement("DELETE FROM ventas WHERE id_pedido = ?");
                pst1.setInt(1, idPedidoSeleccionado);
                pst1.executeUpdate();

                PreparedStatement pst2 = cn.prepareStatement("DELETE FROM detalle_pedido WHERE id_pedido = ?");
                pst2.setInt(1, idPedidoSeleccionado);
                pst2.executeUpdate();

                PreparedStatement pst3 = cn.prepareStatement("DELETE FROM pedidos WHERE id_pedido = ?");
                pst3.setInt(1, idPedidoSeleccionado);
                pst3.executeUpdate();
                cn.commit();

                JOptionPane.showMessageDialog(this, "Pedido, detalles y ventas eliminados correctamente.");
                cargarDatosTabla();
                limpiarCampos();
                cn.close();
            } catch (SQLException ex) {
                try {
                    cn.rollback();
                } catch (SQLException e) {
                }
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private void buscarRegistros() {
        String numPed = txtPedido.getText().trim();
        String nomCli = txtCliente.getText().trim();
        String nomEmp = txtEmpleado.getText().trim();
        String nomPla = txtPlatillo.getText().trim();
        String prec = txtPrecio.getText().trim();

        if (numPed.isEmpty() && nomCli.isEmpty() && nomEmp.isEmpty() && nomPla.isEmpty() && prec.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese al menos un dato para buscar.");
            return;
        }

        modeloTabla.setRowCount(0);
        Connection cn = Conexion.obtenerConexion();
        try {
            String sql = "SELECT p.id_pedido, c.nombre, e.nombre, m.nombre_platillo, dp.cantidad, dp.precio_unitario_historico, p.estado, p.fecha_hora "
                    + "FROM pedidos p "
                    + "JOIN clientes c ON p.id_cliente = c.id_cliente "
                    + "JOIN empleados e ON p.id_empleado = e.id_empleado "
                    + "JOIN detalle_pedido dp ON p.id_pedido = dp.id_pedido "
                    + "JOIN menu m ON dp.id_platillo = m.id_platillo "
                    + "WHERE 1=1 ";

            if (!numPed.isEmpty()) {
                sql += "AND p.id_pedido = ? ";
            }
            if (!nomCli.isEmpty()) {
                sql += "AND c.nombre LIKE ? ";
            }
            if (!nomEmp.isEmpty()) {
                sql += "AND e.nombre LIKE ? ";
            }
            if (!nomPla.isEmpty()) {
                sql += "AND m.nombre_platillo LIKE ? ";
            }
            if (!prec.isEmpty()) {
                sql += "AND dp.precio_unitario_historico = ? ";
            }

            PreparedStatement pst = cn.prepareStatement(sql);
            int i = 1;
            if (!numPed.isEmpty()) {
                pst.setInt(i++, Integer.parseInt(numPed));
            }
            if (!nomCli.isEmpty()) {
                pst.setString(i++, "%" + nomCli + "%");
            }
            if (!nomEmp.isEmpty()) {
                pst.setString(i++, "%" + nomEmp + "%");
            }
            if (!nomPla.isEmpty()) {
                pst.setString(i++, "%" + nomPla + "%");
            }
            if (!prec.isEmpty()) {
                pst.setDouble(i++, Double.parseDouble(prec));
            }

            ResultSet rs = pst.executeQuery();
            int encontrados = 0;
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getDouble(6), rs.getString(7), rs.getString(8)});
                encontrados++;
            }

            if (encontrados == 0) {
                JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
                cargarDatosTabla();
            }
            cn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en la búsqueda: " + ex.getMessage());
        }
    }

    private int obtenerIdEmpleado(String nombre) {
        int id = -1;
        Connection cn = Conexion.obtenerConexion();
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT id_empleado FROM empleados WHERE nombre = ?");
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id_empleado");
            }
            cn.close();
        } catch (SQLException e) {
        }
        return id;
    }

    private int validarOInsertarCliente(String nombre) {
        int id = -1;
        Connection cn = Conexion.obtenerConexion();
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT id_cliente FROM clientes WHERE nombre = ?");
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id_cliente");
            } else {
                PreparedStatement ins = cn.prepareStatement("INSERT INTO clientes (nombre, telefono, correo) VALUES (?, NULL, NULL)", Statement.RETURN_GENERATED_KEYS);
                ins.setString(1, nombre);
                ins.executeUpdate();
                ResultSet gk = ins.getGeneratedKeys();
                if (gk.next()) {
                    id = gk.getInt(1);
                }
                JOptionPane.showMessageDialog(this, "El cliente no existía. Se registró automáticamente: " + nombre);
            }
            cn.close();
        } catch (SQLException e) {
        }
        return id;
    }

    private int obtenerIdPlatillo(String nombre) {
        int id = -1;
        Connection cn = Conexion.obtenerConexion();
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT id_platillo FROM menu WHERE nombre_platillo = ?");
            pst.setString(1, nombre);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id_platillo");
            }
            cn.close();
        } catch (SQLException e) {
        }
        return id;
    }

    private void limpiarCampos() {
        txtPedido.setText("");
        txtCliente.setText("");
        txtEmpleado.setText("");
        txtPlatillo.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        txtEstado.setText("");

        txtCliente.setEditable(true);
        txtEmpleado.setEditable(true);

        fechaChooser.setDate(new Date());
        idPedidoSeleccionado = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn1) {
            this.dispose();
            new Pantalla_Principal();
        }
        if (e.getSource() == btn5) {
            registrarPedido();
        }
        if (e.getSource() == btn3) {
            actualizarRegistro();
        }
        if (e.getSource() == btn2) {
            buscarRegistros();
        }
        if (e.getSource() == btn4) {
            eliminarRegistro();
        }
    }

}
