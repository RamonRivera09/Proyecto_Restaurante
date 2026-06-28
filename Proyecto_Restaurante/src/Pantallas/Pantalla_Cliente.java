
package Pantallas;

import Conexion_BD.Conexion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Pantalla_Cliente extends JFrame implements ActionListener {

    private JPanel pArriba, pAbajo, pCentro, Controles;
    private JLabel lblCliente, lblNombre, lblTelefono, lblCorreo;
    private JTextField txtNom, txtTel, txtCorr;
    private JButton btn1, btn2, btn3, btn4, btn5;
    
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private int idClienteOriginal = 0; 

    public Pantalla_Cliente() {
        configFrame();
        initComponents();
        cargarDatosTabla(); 
        setVisible(true);
    }

    public void configFrame() {
        setSize(800, 700);
        setTitle("Restaurante - Cliente");
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
        lblCliente = new JLabel("Gestión de clientes");
        lblCliente.setForeground(Color.WHITE);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 20));
        pArriba.add(lblCliente);
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
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Correo");
        
        tablaClientes = new JTable(modeloTabla);
        JScrollPane sp = new JScrollPane(tablaClientes);
        sp.setBackground(Color.WHITE);
        sp.setBorder(BorderFactory.createTitledBorder("Lista de Clientes Registrados"));
        
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tablaClientes.getSelectedRow();
                if (fila != -1) {
                    // Obtenemos los datos de la fila
                    idClienteOriginal = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                    txtNom.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtTel.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtCorr.setText(modeloTabla.getValueAt(fila, 3).toString());
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
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        lblNombre = new JLabel("Nombre del cliente:");
        lblTelefono = new JLabel("Teléfono:");
        lblCorreo = new JLabel("Correo:");
        
        int c = 0;
        JLabel[] labels = new JLabel[]{lblNombre, lblTelefono, lblCorreo};
        for (JLabel l : labels) {
            gbc.gridy = c;
            l.setFont(new Font("Arial", Font.BOLD, 15));
            Controles.add(l, gbc);
            c++;
        }
        
        c = 0;
        gbc.gridx = 1;
        txtNom = new JTextField(20);
        txtTel = new JTextField(20);
        txtCorr = new JTextField(20);
        
        JTextField[] fields = new JTextField[]{txtNom, txtTel, txtCorr};
        for (JTextField t : fields) {
            gbc.gridy = c;
            Controles.add(t, gbc);
            c++;
        }
        return Controles;
    }

    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0); 
        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "SELECT id_cliente, nombre, telefono, correo FROM clientes";
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    Object[] fila = new Object[4];
                    fila[0] = rs.getInt("id_cliente");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getString("telefono");
                    fila[3] = rs.getString("correo");
                    modeloTabla.addRow(fila);
                }
                cn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar tabla: " + e.getMessage());
            }
        }
    }

    private void registrarCliente() {
        if (txtNom.getText().isEmpty() || txtTel.getText().isEmpty() || txtCorr.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, llena todos los campos para registrar un cliente.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "INSERT INTO clientes (nombre, telefono, correo) VALUES (?, ?, ?)";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, txtNom.getText());
                pst.setString(2, txtTel.getText());
                pst.setString(3, txtCorr.getText());
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.");
                
                cargarDatosTabla();
                limpiarCampos();
                cn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage());
            }
        }
    }

    private void actualizarRegistro() {
        if (idClienteOriginal == 0) {
            JOptionPane.showMessageDialog(this, "Primero selecciona un registro de la tabla para actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (txtNom.getText().isEmpty() || txtTel.getText().isEmpty() || txtCorr.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los campos no pueden quedar vacíos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "UPDATE clientes SET nombre = ?, telefono = ?, correo = ? WHERE id_cliente = ?";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, txtNom.getText());
                pst.setString(2, txtTel.getText());
                pst.setString(3, txtCorr.getText());
                pst.setInt(4, idClienteOriginal);
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cliente actualizado con éxito.");
                
                cargarDatosTabla();
                limpiarCampos();
                cn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
            }
        }
    }

    private void eliminarRegistro() {
        if (idClienteOriginal == 0) {
            JOptionPane.showMessageDialog(this, "Primero selecciona un registro de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar a este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (confirmacion == JOptionPane.YES_OPTION) {
            Connection cn = Conexion.obtenerConexion();
            if (cn != null) {
                try {
                    String sql = "DELETE FROM clientes WHERE id_cliente = ?";
                    PreparedStatement pst = cn.prepareStatement(sql);
                    pst.setInt(1, idClienteOriginal);
                    pst.executeUpdate();
                    
                    JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                    
                    cargarDatosTabla();
                    limpiarCampos();
                    cn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: Elimina la venta o el pedido primero del sistema");
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    private void buscarRegistros() {
        String nomABuscar = txtNom.getText().trim();
        String telABuscar = txtTel.getText().trim();
        String corrABuscar = txtCorr.getText().trim();
        
        if (nomABuscar.isEmpty() && telABuscar.isEmpty() && corrABuscar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, escribe un Nombre, Teléfono o Correo para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "SELECT id_cliente, nombre, telefono, correo FROM clientes WHERE 1=1 ";
                
                if (!nomABuscar.isEmpty()) {
                    sql += "AND nombre LIKE ? ";
                }
                if (!telABuscar.isEmpty()) {
                    sql += "AND telefono LIKE ? ";
                }
                if (!corrABuscar.isEmpty()) {
                    sql += "AND correo LIKE ? ";
                }

                PreparedStatement pst = cn.prepareStatement(sql);
                
                int paramIndex = 1;
                if (!nomABuscar.isEmpty()) {
                    pst.setString(paramIndex++, "%" + nomABuscar + "%");
                }
                if (!telABuscar.isEmpty()) {
                    pst.setString(paramIndex++, "%" + telABuscar + "%");
                }
                if (!corrABuscar.isEmpty()) {
                    pst.setString(paramIndex++, "%" + corrABuscar + "%");
                }

                ResultSet rs = pst.executeQuery();
                
                modeloTabla.setRowCount(0); 
                int encontrados = 0;
                
                while (rs.next()) {
                    Object[] fila = new Object[4];
                    fila[0] = rs.getInt("id_cliente");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getString("telefono");
                    fila[3] = rs.getString("correo");
                    modeloTabla.addRow(fila);
                    encontrados++;
                }

                if (encontrados > 0) {
                    JOptionPane.showMessageDialog(this, "Los resultados aparecerán en la tabla.", "Búsqueda Exitosa", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontraron resultados.", "Sin Resultados", JOptionPane.WARNING_MESSAGE);
                    cargarDatosTabla(); 
                }
                
                cn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void limpiarCampos() {
        txtNom.setText("");
        txtTel.setText("");
        txtCorr.setText("");
        idClienteOriginal = 0; 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn1) {
            this.dispose();
            new Pantalla_Principal();
        }
        if (e.getSource() == btn5) { 
            registrarCliente();
        }
        if (e.getSource() == btn2) { 
            buscarRegistros();
        }
        if (e.getSource() == btn3) { 
            actualizarRegistro();
        }
        if (e.getSource() == btn4) { 
            eliminarRegistro();
        }
    }

    
}