package Pantallas;

import Conexion_BD.Conexion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Pantalla_Empleado extends JFrame implements ActionListener {

    private JPanel pArriba, pAbajo, pCentro, pControles;
    private JLabel lblEmpleado;
    private JButton btn1, btn2, btn3, btn4;
    private JLabel nombU, nombre, contraseña, cContraseña, telefono, turno, rol, salario;
    private JLabel[] Labels;
    private JTextField txtNomU, txtNombre, txtSalario, txtTel;
    private JPasswordField contra1, contra2;
    private JComboBox cmbPuesto, cmbTurnos;
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;
    private String usuarioOriginal = "";

    public Pantalla_Empleado() {
        configFrame();
        initComponents();
        cargarDatosTabla();
        setVisible(true);
    }

    public void configFrame() {
        setSize(800, 700);
        setTitle("Restaurante - Empleado");
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
        lblEmpleado = new JLabel("Gestión de empleado.");
        lblEmpleado.setForeground(Color.WHITE);
        lblEmpleado.setFont(new Font("Arial", Font.BOLD, 20));
        pArriba.add(lblEmpleado);
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
        btn2 = new JButton("Buscar");
        btn3 = new JButton("Actualizar");
        btn4 = new JButton("Eliminar");
        JButton[] arrbtn = new JButton[]{btn1, btn2, btn3, btn4,};
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
        modeloTabla.addColumn("Usuario");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Turno");
        modeloTabla.addColumn("Puesto");
        modeloTabla.addColumn("Salario");
        tablaEmpleados = new JTable(modeloTabla);
        JScrollPane sp = new JScrollPane(tablaEmpleados);
        sp.setBackground(Color.WHITE);
        sp.setBorder(BorderFactory.createTitledBorder("Lista de Empleados Registrados"));

        tablaEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tablaEmpleados.getSelectedRow();
                if (fila != -1) {
                    usuarioOriginal = modeloTabla.getValueAt(fila, 0).toString();
                    String usuario = modeloTabla.getValueAt(fila, 0).toString();
                    String nomEmpleado = modeloTabla.getValueAt(fila, 1).toString();
                    String tel = modeloTabla.getValueAt(fila, 2).toString();
                    String turnoSel = modeloTabla.getValueAt(fila, 3).toString();
                    String puestoSel = modeloTabla.getValueAt(fila, 4).toString();
                    String sueldo = modeloTabla.getValueAt(fila, 5).toString();

                    txtNomU.setText(usuario);
                    txtNombre.setText(nomEmpleado);
                    txtTel.setText(tel);
                    cmbTurnos.setSelectedItem(turnoSel);
                    cmbPuesto.setSelectedItem(puestoSel);
                    txtSalario.setText(sueldo);
                    buscarPassword(usuario);
                }
            }
        });
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
        nombU = new JLabel("Nombre de usuario:");
        pControles.add(nombU, gbc);
        gbc.gridy = 1;
        nombre = new JLabel("Nombre del empleado:");
        pControles.add(nombre, gbc);
        gbc.gridy = 2;
        contraseña = new JLabel("Contraseña:");
        pControles.add(contraseña, gbc);
        gbc.gridy = 3;
        cContraseña = new JLabel("Confirmar contraseña:");
        pControles.add(cContraseña, gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        turno = new JLabel("Turno a laborar:");
        pControles.add(turno, gbc);
        gbc.gridy = 1;
        telefono = new JLabel("Teléfono:");
        pControles.add(telefono, gbc);
        gbc.gridy = 2;
        rol = new JLabel("Puesto del empleado:");
        pControles.add(rol, gbc);
        gbc.gridy = 3;
        salario = new JLabel("Salario a percibir:");
        pControles.add(salario, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        txtNomU = new JTextField(15);
        pControles.add(txtNomU, gbc);
        gbc.gridy = 1;
        txtNombre = new JTextField(15);
        pControles.add(txtNombre, gbc);
        gbc.gridy = 2;
        contra1 = new JPasswordField(15);
        pControles.add(contra1, gbc);
        gbc.gridy = 3;
        contra2 = new JPasswordField(15);
        pControles.add(contra2, gbc);
        gbc.gridx = 4;
        gbc.gridy = 0;
        String[] turn = {"Seleccione", "Matutino", "Vespertino", "Tiempo Completo"};
        cmbTurnos = new JComboBox<>(turn);
        cmbTurnos.setBounds(0, 0, 20, 10);
        pControles.add(cmbTurnos, gbc);
        gbc.gridy = 1;
        txtTel = new JTextField(15);
        pControles.add(txtTel, gbc);
        gbc.gridy = 2;
        cmbPuesto = new JComboBox<>();
        cargarPuestos();
        cmbPuesto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccionado = cmbPuesto.getSelectedItem().toString();

                if (seleccionado.equals("Seleccione")) {
                    txtSalario.setText("");
                    return;
                }

                Connection cn = Conexion.obtenerConexion();
                if (cn != null) {
                    try {
                        String sql = "SELECT salario_base FROM puestos WHERE nombre_puesto = ?";
                        PreparedStatement pst = cn.prepareStatement(sql);
                        pst.setString(1, seleccionado);
                        ResultSet rs = pst.executeQuery();

                        if (rs.next()) {
                            txtSalario.setText(rs.getString("salario_base"));
                        }
                        cn.close();
                    } catch (SQLException ex) {
                        System.err.println("Error al obtener salario: " + ex.getMessage());
                    }
                }
            }
        });
        cmbPuesto.setBounds(0, 0, 20, 10);
        pControles.add(cmbPuesto, gbc);
        gbc.gridy = 3;
        txtSalario = new JTextField(15);
        txtSalario.setEditable(false);
        pControles.add(txtSalario, gbc);
        Labels = new JLabel[]{nombU, nombre, contraseña, cContraseña, telefono, rol, salario, turno};
        for (JLabel l : Labels) {
            l.setFont(new Font("Arial", Font.BOLD, 15));
        }
        return pControles;

    }

    private void cargarPuestos() {
        cmbPuesto.removeAllItems();
        cmbPuesto.addItem("Seleccione");

        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "SELECT nombre_puesto FROM puestos";
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    cmbPuesto.addItem(rs.getString("nombre_puesto"));
                }
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cargar los puestos: " + e.getMessage());
            }
        }
    }

    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "SELECT u.nombre_usuario, e.nombre, u.contrasena, e.telefono, e.turno, p.nombre_puesto, p.salario_base "
                        + "FROM empleados e "
                        + "JOIN usuarios u ON e.id_empleado = u.id_empleado "
                        + "JOIN puestos p ON e.id_puesto = p.id_puesto";
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    Object[] fila = new Object[6];
                    fila[0] = rs.getString("nombre_usuario");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getString("telefono");
                    fila[3] = rs.getString("turno");
                    fila[4] = rs.getString("nombre_puesto");
                    fila[5] = rs.getDouble("salario_base");
                    modeloTabla.addRow(fila);
                }
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar tabla: " + e.getMessage());
            }
        }
    }

    private void buscarPassword(String usuario) {
        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "SELECT contrasena FROM usuarios WHERE nombre_usuario = ?";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, usuario);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    String pass = rs.getString("contrasena");
                    contra1.setText(pass);
                    contra2.setText(pass);
                }
                cn.close();
            } catch (SQLException ex) {
                System.err.println("Error al recuperar password: " + ex.getMessage());
            }
        }
    }

    private void limpiarCampos() {
        txtNomU.setText("");
        txtNomU.setEditable(true);
        txtNombre.setText("");
        contra1.setText("");
        contra2.setText("");
        txtTel.setText("");
        cmbTurnos.setSelectedIndex(0);
        cmbPuesto.setSelectedIndex(0);
        txtSalario.setText("");
    }

    private void actualizarRegistro() {
        String nuevoUsuario = txtNomU.getText();
        String pass1 = new String(contra1.getPassword());
        String pass2 = new String(contra2.getPassword());

        if (nuevoUsuario.isEmpty() || usuarioOriginal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero selecciona un registro de la tabla");
            return;
        }
        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
            return;
        }

        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                cn.setAutoCommit(false);

                int idPuesto = 0;
                PreparedStatement pstP = cn.prepareStatement("SELECT id_puesto FROM puestos WHERE nombre_puesto = ?");
                pstP.setString(1, cmbPuesto.getSelectedItem().toString());
                ResultSet rsP = pstP.executeQuery();
                if (rsP.next()) {
                    idPuesto = rsP.getInt("id_puesto");
                }

                String sqlUser = "UPDATE usuarios SET nombre_usuario = ?, contrasena = ? WHERE nombre_usuario = ?";
                PreparedStatement pstU = cn.prepareStatement(sqlUser);
                pstU.setString(1, nuevoUsuario);
                pstU.setString(2, pass1);
                pstU.setString(3, usuarioOriginal);
                pstU.executeUpdate();

                String sqlEmp = "UPDATE empleados SET nombre = ?, telefono = ?, turno = ?, id_puesto = ? "
                        + "WHERE id_empleado = (SELECT id_empleado FROM usuarios WHERE nombre_usuario = ?)";
                PreparedStatement pstE = cn.prepareStatement(sqlEmp);
                pstE.setString(1, txtNombre.getText());
                pstE.setString(2, txtTel.getText());
                pstE.setString(3, cmbTurnos.getSelectedItem().toString());
                pstE.setInt(4, idPuesto);
                pstE.setString(5, nuevoUsuario);

                pstE.executeUpdate();

                cn.commit();
                JOptionPane.showMessageDialog(this, "Registro actualizado con éxito");

                usuarioOriginal = "";
                cargarDatosTabla();
                limpiarCampos();

            } catch (SQLException ex) {
                try {
                    cn.rollback();
                } catch (SQLException e1) {
                }
                JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
            }
        }
    }

    private void buscarRegistros() {
        String usuarioABuscar = txtNomU.getText().trim();
        String nombreABuscar = txtNombre.getText().trim();

        if (usuarioABuscar.isEmpty() && nombreABuscar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, escribe un Nombre de Usuario o Nombre de Empleado para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "SELECT u.nombre_usuario, e.nombre, e.telefono, e.turno, p.nombre_puesto, p.salario_base "
                        + "FROM empleados e "
                        + "JOIN usuarios u ON e.id_empleado = u.id_empleado "
                        + "JOIN puestos p ON e.id_puesto = p.id_puesto "
                        + "WHERE 1=1 ";
                if (!usuarioABuscar.isEmpty()) {
                    sql += "AND u.nombre_usuario LIKE ? ";
                }
                if (!nombreABuscar.isEmpty()) {
                    sql += "AND e.nombre LIKE ? ";
                }

                PreparedStatement pst = cn.prepareStatement(sql);
                int paramIndex = 1;
                if (!usuarioABuscar.isEmpty()) {
                    pst.setString(paramIndex++, "%" + usuarioABuscar + "%");
                }
                if (!nombreABuscar.isEmpty()) {
                    pst.setString(paramIndex++, "%" + nombreABuscar + "%");
                }

                ResultSet rs = pst.executeQuery();

                modeloTabla.setRowCount(0);
                int filasEncontradas = 0;

                while (rs.next()) {
                    Object[] fila = new Object[6];
                    fila[0] = rs.getString("nombre_usuario");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getString("telefono");
                    fila[3] = rs.getString("turno");
                    fila[4] = rs.getString("nombre_puesto");
                    fila[5] = rs.getDouble("salario_base");
                    modeloTabla.addRow(fila);
                    filasEncontradas++;
                }

                if (filasEncontradas > 0) {
                    JOptionPane.showMessageDialog(this, "Los resultados aparecerán en la tabla", "Búsqueda Exitosa", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontraron resultados", "Sin Resultados", JOptionPane.WARNING_MESSAGE);

                    cargarDatosTabla();
                }

                cn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean solicitarPasswordSeguridad() {
        JPasswordField pf = new JPasswordField();
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                pf,
                "Ingresa la contraseña de autorización:",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta == JOptionPane.OK_OPTION) {
            String password = new String(pf.getPassword());

            if (password.equals("Gerente_Admin2026")) {
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta. Acción denegada.", "Error de seguridad", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    private void eliminarRegistro() {
        if (usuarioOriginal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un registro de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar permanentemente a este empleado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                cn.setAutoCommit(false);

                int idEmpleado = 0;
                String sqlId = "SELECT id_empleado FROM usuarios WHERE nombre_usuario = ?";
                PreparedStatement pstId = cn.prepareStatement(sqlId);
                pstId.setString(1, usuarioOriginal);
                ResultSet rsId = pstId.executeQuery();
                if (rsId.next()) {
                    idEmpleado = rsId.getInt("id_empleado");
                } else {
                    JOptionPane.showMessageDialog(this, "Error: No se encontró el registro en la base de datos.");
                    return;
                }

                String sqlUser = "DELETE FROM usuarios WHERE nombre_usuario = ?";
                PreparedStatement pstU = cn.prepareStatement(sqlUser);
                pstU.setString(1, usuarioOriginal);
                pstU.executeUpdate();

                String sqlEmp = "DELETE FROM empleados WHERE id_empleado = ?";
                PreparedStatement pstE = cn.prepareStatement(sqlEmp);
                pstE.setInt(1, idEmpleado);
                pstE.executeUpdate();

                cn.commit();
                JOptionPane.showMessageDialog(this, "Registro eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                usuarioOriginal = "";
                cargarDatosTabla();
                limpiarCampos();

            } catch (SQLException ex) {
                try {
                    cn.rollback();
                } catch (SQLException e1) {
                }
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn1) {
            this.dispose();
            new Pantalla_Principal();
        }
        if (e.getSource() == btn2) {
            buscarRegistros();
        }
        if (e.getSource() == btn3) {
            if (txtNomU.getText().isEmpty()
                    && txtNombre.getText().isEmpty()
                    && cmbPuesto.getSelectedIndex() == 0
                    && cmbTurnos.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(this, "Primero hay que seleccionar un registro en la tabla", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                if (solicitarPasswordSeguridad()) {
                    actualizarRegistro();
                }
            }
        }

        if (e.getSource() == btn4) {
            if (txtNomU.getText().isEmpty()
                    && txtNombre.getText().isEmpty()
                    && cmbPuesto.getSelectedIndex() == 0
                    && cmbTurnos.getSelectedIndex() == 0) {

                JOptionPane.showMessageDialog(this, "Primero hay que seleccionar un registro en la tabla", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                if (solicitarPasswordSeguridad()) {
                    eliminarRegistro();
                }
            }
        }
    }

   
}
