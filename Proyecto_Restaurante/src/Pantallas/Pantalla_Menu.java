
package Pantallas;

import Conexion_BD.Conexion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Pantalla_Menu extends JFrame implements ActionListener {

    private JPanel pArriba, pAbajo, pCentro, Controles;
    private JButton btn1, btn2, btn3, btn4, btn5;
    private JLabel lblMenu, lblNombre, lblPrecio, lblCategoria;
    private JTextField txtNom, txtPrec;
    private JComboBox cmb1;
    
    private JTable tablaMenu;
    private DefaultTableModel modeloTabla;
    private int idPlatilloOriginal = 0; 

    public Pantalla_Menu() {
        configFrame();
        initComponents();
        cargarCategorias(); 
        cargarDatosTabla();
        setVisible(true);
    }

    public void configFrame() {
        setSize(800, 700);
        setTitle("Restaurante - Menú");
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
        lblMenu = new JLabel("Control del Menú");
        lblMenu.setForeground(Color.WHITE);
        lblMenu.setFont(new Font("Arial", Font.BOLD, 20));
        pArriba.add(lblMenu);
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
        modeloTabla.addColumn("Platillo");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Categoría");
        
        tablaMenu = new JTable(modeloTabla);
        JScrollPane sp = new JScrollPane(tablaMenu);
        sp.setBackground(Color.WHITE);
        sp.setBorder(BorderFactory.createTitledBorder("Platillos en el Sistema"));
        
        tablaMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tablaMenu.getSelectedRow();
                if (fila != -1) {
                    idPlatilloOriginal = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
                    txtNom.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtPrec.setText(modeloTabla.getValueAt(fila, 2).toString());
                    cmb1.setSelectedItem(modeloTabla.getValueAt(fila, 3).toString());
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
        
        lblNombre = new JLabel("Nombre del platillo:");
        lblPrecio = new JLabel("Precio ($):");
        lblCategoria = new JLabel("Categoría:");
        
        int c = 0;
        JLabel[] labels = new JLabel[]{lblNombre, lblPrecio, lblCategoria};
        for (JLabel l : labels) {
            gbc.gridy = c;
            l.setFont(new Font("Arial", Font.BOLD, 15));
            Controles.add(l, gbc);
            c++;
        }
        
        c = 0;
        gbc.gridx = 1;
        txtNom = new JTextField(20);
        txtPrec = new JTextField(20);
        cmb1 = new JComboBox(); 
        
        gbc.gridy = 0; Controles.add(txtNom, gbc);
        gbc.gridy = 1; Controles.add(txtPrec, gbc);
        gbc.gridy = 2; Controles.add(cmb1, gbc);
        
        return Controles;
    }

   

    private void cargarCategorias() {
        cmb1.removeAllItems();
        cmb1.addItem("Seleccione");
        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "SELECT nombre_categoria FROM categorias_menu";
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    cmb1.addItem(rs.getString("nombre_categoria"));
                }
                cn.close();
            } catch (SQLException e) { System.err.println("Error combo: " + e.getMessage()); }
        }
    }

    private void cargarDatosTabla() {
        modeloTabla.setRowCount(0);
        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "SELECT m.id_platillo, m.nombre_platillo, m.precio, c.nombre_categoria " +
                             "FROM menu m JOIN categorias_menu c ON m.id_categoria = c.id_categoria";
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{
                        rs.getInt("id_platillo"),
                        rs.getString("nombre_platillo"),
                        rs.getDouble("precio"),
                        rs.getString("nombre_categoria")
                    });
                }
                cn.close();
            } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error tabla: " + e.getMessage()); }
        }
    }

    private int obtenerIdCategoria(String nombreCat) {
        int id = -1;
        Connection cn = Conexion.obtenerConexion();
        try {
            PreparedStatement pst = cn.prepareStatement("SELECT id_categoria FROM categorias_menu WHERE nombre_categoria = ?");
            pst.setString(1, nombreCat);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) id = rs.getInt("id_categoria");
            cn.close();
        } catch (SQLException e) { }
        return id;
    }

    private void registrarPlatillo() {
        if (txtNom.getText().isEmpty() || txtPrec.getText().isEmpty() || cmb1.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        int idCat = obtenerIdCategoria(cmb1.getSelectedItem().toString());
        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "INSERT INTO menu (nombre_platillo, precio, id_categoria) VALUES (?, ?, ?)";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, txtNom.getText());
                pst.setDouble(2, Double.parseDouble(txtPrec.getText()));
                pst.setInt(3, idCat);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Platillo registrado.");
                cargarDatosTabla();
                limpiarCampos();
                cn.close();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        }
    }

    private void actualizarRegistro() {
        if (idPlatilloOriginal == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un platillo de la tabla.");
            return;
        }

        int idCat = obtenerIdCategoria(cmb1.getSelectedItem().toString());
        Connection cn = Conexion.obtenerConexion();
        if (cn != null) {
            try {
                String sql = "UPDATE menu SET nombre_platillo=?, precio=?, id_categoria=? WHERE id_platillo=?";
                PreparedStatement pst = cn.prepareStatement(sql);
                pst.setString(1, txtNom.getText());
                pst.setDouble(2, Double.parseDouble(txtPrec.getText()));
                pst.setInt(3, idCat);
                pst.setInt(4, idPlatilloOriginal);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Menú actualizado.");
                cargarDatosTabla();
                limpiarCampos();
                cn.close();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        }
    }

    private void eliminarRegistro() {
        if (idPlatilloOriginal == 0 || txtNom.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Debe seleccionar un platillo de la tabla para poder eliminarlo.", 
                "Campo vacío", 
                JOptionPane.WARNING_MESSAGE);
            return; 
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar este platillo?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            Connection cn = Conexion.obtenerConexion();
            try {
                PreparedStatement pst = cn.prepareStatement("DELETE FROM menu WHERE id_platillo = ?");
                pst.setInt(1, idPlatilloOriginal);
                pst.executeUpdate();
                cargarDatosTabla();
                limpiarCampos();
                cn.close();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        }
    }

    private void buscarRegistros() {
    String nomBusqueda = txtNom.getText().trim();
    String precBusqueda = txtPrec.getText().trim();
    String catBusqueda = cmb1.getSelectedItem().toString();

    if (nomBusqueda.isEmpty() && precBusqueda.isEmpty() && catBusqueda.equals("Seleccione")) {
        JOptionPane.showMessageDialog(this, "Escribe un Nombre, un Precio o elige una Categoría para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    Connection cn = Conexion.obtenerConexion();
    if (cn != null) {
        try {
            String sql = "SELECT m.id_platillo, m.nombre_platillo, m.precio, c.nombre_categoria " +
                         "FROM menu m " +
                         "JOIN categorias_menu c ON m.id_categoria = c.id_categoria " +
                         "WHERE 1=1 "; 

            if (!nomBusqueda.isEmpty()) {
                sql += "AND m.nombre_platillo LIKE ? ";
            }
            if (!precBusqueda.isEmpty()) {
                sql += "AND m.precio = ? "; 
            }
            if (!catBusqueda.equals("Seleccione")) {
                sql += "AND c.nombre_categoria = ? ";
            }

            PreparedStatement pst = cn.prepareStatement(sql);
            int i = 1;

            if (!nomBusqueda.isEmpty()) {
                pst.setString(i++, "%" + nomBusqueda + "%");
            }
            if (!precBusqueda.isEmpty()) {
                try {
                    pst.setDouble(i++, Double.parseDouble(precBusqueda));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "El precio debe ser un número válido (ej. 45.50)");
                    return;
                }
            }
            if (!catBusqueda.equals("Seleccione")) {
                pst.setString(i++, catBusqueda);
            }

            ResultSet rs = pst.executeQuery();
            modeloTabla.setRowCount(0); 

            int cont = 0;
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_platillo"),
                    rs.getString("nombre_platillo"),
                    rs.getDouble("precio"),
                    rs.getString("nombre_categoria")
                };
                modeloTabla.addRow(fila);
                cont++;
            }

            if (cont == 0) {
                JOptionPane.showMessageDialog(this, "No se encontraron resultados con esos filtros.");
                cargarDatosTabla(); 
            }
            
            cn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage());
        }
    }
}

    private void limpiarCampos() {
        txtNom.setText("");
        txtPrec.setText("");
        cmb1.setSelectedIndex(0);
        idPlatilloOriginal = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn1) { this.dispose(); new Pantalla_Principal(); }
        if (e.getSource() == btn5) registrarPlatillo();
        if (e.getSource() == btn2) buscarRegistros();
        if (e.getSource() == btn3) actualizarRegistro();
        if (e.getSource() == btn4) eliminarRegistro();
    }

    }