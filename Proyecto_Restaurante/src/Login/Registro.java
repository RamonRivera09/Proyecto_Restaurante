package Login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import Conexion_BD.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class Registro extends JFrame implements ActionListener {

    private JPanel pArriba, pAbajo, pCentro;
    private JLabel registro, nombU, nombre, contraseña, cContraseña, telefono, turno, rol, salario;
    private JLabel[] Labels;
    private JButton atras, registrar;
    private JTextField txtNomU, txtNombre,  txtSalario, txtTel;
    private JPasswordField contra1, contra2;
    private JComboBox  cmbPuesto, cmbTurnos;

    public Registro() {
        configFrame();
        initComponents();
        setVisible(true);
    }

    public void configFrame() {
        setTitle("Restaurante - Registro");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagenes/logoRestaurante.png"));
        setIconImage(icono);
    }

    public void initComponents() {

        add(Arriba(), BorderLayout.NORTH);
        add(Abajo(), BorderLayout.SOUTH);
        add(Centro(), BorderLayout.CENTER);
        List<Component> listaFoco = new ArrayList<>();
        listaFoco.add(txtNomU);     
        listaFoco.add(txtNombre);
        listaFoco.add(contra1);     
        listaFoco.add(contra2);
        listaFoco.add(txtTel);      

        this.setFocusTraversalPolicy(new MiOrdenDeFoco(listaFoco));
    }

    public JPanel Arriba() {
        pArriba = new JPanel();
        pArriba.setBackground(Color.red);
        pArriba.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        registro = new JLabel("Registra a un empleado:");
        registro.setFont(new Font("Arial", Font.BOLD, 40));
        registro.setForeground(Color.WHITE);
        pArriba.add(registro, gbc);
        return pArriba;
    }

    public JPanel Abajo() {
        pAbajo = new JPanel();
        pAbajo.setBackground(Color.red);
        pAbajo.setLayout(new BorderLayout());
        atras = new JButton("Atrás");
        atras.setFont(new Font("Arial", Font.BOLD, 15));
        atras.addActionListener(this);
        pAbajo.add(atras, BorderLayout.WEST);
        registrar = new JButton("Registrar usuario");
        registrar.setFont(new Font("Arial", Font.BOLD, 15));
        registrar.addActionListener(this);
        pAbajo.add(registrar, BorderLayout.EAST);
        return pAbajo;
    }

    public JPanel Centro() {
        pCentro = new JPanel();
        pCentro.setBackground(Color.WHITE);
        pCentro.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nombU = new JLabel("Nombre de usuario:");
        pCentro.add(nombU, gbc);
        gbc.gridy = 1;
        nombre = new JLabel("Nombre del empleado:");
        pCentro.add(nombre, gbc);
        gbc.gridy = 2;
        contraseña = new JLabel("Contraseña:");
        pCentro.add(contraseña, gbc);
        gbc.gridy = 3;
        cContraseña = new JLabel("Confirmar contraseña:");
        pCentro.add(cContraseña, gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        turno=new JLabel("Turno a laborar:");
        pCentro.add(turno, gbc);
        gbc.gridy = 1;
        telefono = new JLabel("Teléfono:");
        pCentro.add(telefono, gbc);
        gbc.gridy = 2;
        rol = new JLabel("Puesto del empleado:");
        pCentro.add(rol, gbc);
        gbc.gridy = 3;
        salario = new JLabel("Salario a percibir:");
        pCentro.add(salario, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        txtNomU = new JTextField(15);
        pCentro.add(txtNomU, gbc);
        gbc.gridy = 1;
        txtNombre = new JTextField(15);
        pCentro.add(txtNombre, gbc);
        gbc.gridy = 2;
        contra1 = new JPasswordField(15);
        pCentro.add(contra1, gbc);
        gbc.gridy = 3;
        contra2 = new JPasswordField(15);
        pCentro.add(contra2, gbc);
        gbc.gridx = 4;
        gbc.gridy = 0;
        String []turn={"Seleccione", "Matutino", "Vespertino","Tiempo Completo"};
        cmbTurnos=new JComboBox<>(turn);
        cmbTurnos.setBounds(0, 0, 20, 10);
        pCentro.add(cmbTurnos, gbc);
        gbc.gridy = 1;
        txtTel = new JTextField(15);
        pCentro.add(txtTel, gbc);
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
                    // Buscamos el salario base del puesto que el usuario eligió
                    String sql = "SELECT salario_base FROM puestos WHERE nombre_puesto = ?";
                    PreparedStatement pst = cn.prepareStatement(sql);
                    pst.setString(1, seleccionado);
                    ResultSet rs = pst.executeQuery();
                    
                    if (rs.next()) {
                        // Ponemos el resultado en el JTextField
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
        pCentro.add(cmbPuesto, gbc);
        gbc.gridy = 3;
        txtSalario = new JTextField(15);
        txtSalario.setEditable(false);
        pCentro.add(txtSalario, gbc);
        Labels = new JLabel[]{registro, nombU, nombre, contraseña, cContraseña, telefono, rol, salario, turno};
        for (JLabel l : Labels) {
            l.setFont(new Font("Arial", Font.BOLD, 15));
        }
        return pCentro;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == atras){
            this.dispose();
            new Inicio();
        }
        
        if(e.getSource() == registrar){
            String usuario = txtNomU.getText();
            String nombreEmp = txtNombre.getText();
            String pass1 = new String(contra1.getPassword());
            String pass2 = new String(contra2.getPassword());
            String telefono = txtTel.getText();
            String turnoSel = cmbTurnos.getSelectedItem().toString();
            String puestoSel = cmbPuesto.getSelectedItem().toString();

            if(usuario.isEmpty() || nombreEmp.isEmpty() || pass1.isEmpty() || telefono.isEmpty()){
                JOptionPane.showMessageDialog(this, "Por favor, llena todos los campos obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(!pass1.equals(pass2)){
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JPasswordField pfMaestra = new JPasswordField();
            int accion = JOptionPane.showConfirmDialog(this, pfMaestra, "Autorización requerida\nIngrese la contraseña de Gerente:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (accion == JOptionPane.OK_OPTION) {
                String passMaestraIngresada = new String(pfMaestra.getPassword());
                
                if (!passMaestraIngresada.equals("Gerente_Admin2026")) { 
                    JOptionPane.showMessageDialog(this, "Contraseña incorrecta. Registro denegado.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                    return; 
                }
            } else {
                return; 
            }
            
            Connection cn = Conexion.obtenerConexion();
            if(cn != null){
                try {
                    
                    int idPuesto = 0;
                    String sqlBuscaPuesto = "SELECT id_puesto FROM puestos WHERE nombre_puesto = ?";
                    PreparedStatement pstBusca = cn.prepareStatement(sqlBuscaPuesto);
                    pstBusca.setString(1, puestoSel);
                    ResultSet rsPuesto = pstBusca.executeQuery();
                    
                    if(rsPuesto.next()){
                        idPuesto = rsPuesto.getInt("id_puesto"); 
                    } else {
                        String sqlSubePuesto = "INSERT INTO puestos (nombre_puesto, salario_base) VALUES (?, ?)";
                        PreparedStatement pstSubePuesto = cn.prepareStatement(sqlSubePuesto, Statement.RETURN_GENERATED_KEYS);
                        pstSubePuesto.setString(1, puestoSel);
                        pstSubePuesto.setDouble(2, 0.0); 
                        pstSubePuesto.executeUpdate();
                        ResultSet rsNuevoPuesto = pstSubePuesto.getGeneratedKeys();
                        if(rsNuevoPuesto.next()){
                            idPuesto = rsNuevoPuesto.getInt(1);
                        }
                        pstSubePuesto.close();
                    }
                    pstBusca.close();

                    
                    String sqlEmp = "INSERT INTO empleados (nombre, telefono, id_puesto, turno) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstEmp = cn.prepareStatement(sqlEmp, Statement.RETURN_GENERATED_KEYS);
                    pstEmp.setString(1, nombreEmp);
                    pstEmp.setString(2, telefono);
                    pstEmp.setInt(3, idPuesto);
                    pstEmp.setString(4, turnoSel);
                    pstEmp.executeUpdate();
                    
                    int idEmpleado = 0;
                    ResultSet rsEmp = pstEmp.getGeneratedKeys();
                    if(rsEmp.next()){
                        idEmpleado = rsEmp.getInt(1); 
                    }
                    pstEmp.close();

                    String sqlUsu = "INSERT INTO usuarios (nombre_usuario, contrasena, id_empleado) VALUES (?, ?, ?)";
                    PreparedStatement pstUsu = cn.prepareStatement(sqlUsu);
                    pstUsu.setString(1, usuario);
                    pstUsu.setString(2, pass1);
                    pstUsu.setInt(3, idEmpleado);
                    pstUsu.executeUpdate();
                    pstUsu.close();

                    JOptionPane.showMessageDialog(this, "¡Empleado registrado exitosamente en el sistema!");
                    this.dispose();
                    new Ingreso(); 
                    cn.close();
                    
                } catch(SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error en la Base de Datos:\n" + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class MiOrdenDeFoco extends FocusTraversalPolicy {

        List<Component> orden;

        public MiOrdenDeFoco(List<Component> orden) {
            this.orden = orden;
        }

        @Override
        public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
            int idx = (orden.indexOf(aComponent) + 1) % orden.size();
            return orden.get(idx);
        }

        @Override
        public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
            int idx = orden.indexOf(aComponent) - 1;
            if (idx < 0) {
                idx = orden.size() - 1;
            }
            return orden.get(idx);
        }

        @Override
        public Component getDefaultComponent(Container focusCycleRoot) {
            return orden.get(0);
        }

        @Override
        public Component getLastComponent(Container focusCycleRoot) {
            return orden.get(orden.size() - 1);
        }

        @Override
        public Component getFirstComponent(Container focusCycleRoot) {
            return orden.get(0);
        }
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
    

}
