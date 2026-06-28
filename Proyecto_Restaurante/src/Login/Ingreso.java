
package Login;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import Conexion_BD.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Pantallas.Pantalla_Principal;

public class Ingreso extends JFrame implements ActionListener{
    private JPanel pArriba, pAbajo, pCentro;
    private JButton atras,iniciar; 
    private JLabel Inicio, nombre, contraseña;
    private JTextField txtUsuario;
    private JPasswordField contra;
    public Ingreso(){
        configFrame();
        initComponents();
        setVisible(true);
    }
    public void configFrame(){
        setTitle("Restaurante - Ingreso");
        setSize(400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagenes/logoRestaurante.png"));
        setIconImage(icono);
        
    }
    public void initComponents(){
        add(Arriba(), BorderLayout.NORTH);
        add(Abajo(), BorderLayout.SOUTH);
        add(Centro(), BorderLayout.CENTER);
    }
    public JPanel Arriba(){
        pArriba=new JPanel();
        pArriba.setBackground(Color.red);
        pArriba.setLayout(new GridBagLayout());
        GridBagConstraints gbc =new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.insets=new Insets(10, 10, 10, 10);
        Inicio=new JLabel("Iniciar sesión:");
        Inicio.setFont(new Font("Arial", Font.BOLD, 20));
        Inicio.setForeground(Color.WHITE);
        pArriba.add(Inicio, gbc);
        return pArriba;
    }
    public JPanel Abajo(){
        pAbajo=new JPanel();
        pAbajo.setBackground(Color.red);
        pAbajo.setLayout(new BorderLayout());
        atras=new JButton("Atrás");
        atras.setFont(new Font("Arial", Font.BOLD, 15));
        atras.addActionListener(this);
        pAbajo.add(atras, BorderLayout.WEST);
        iniciar=new JButton("Iniciar sesión");
        iniciar.setFont(new Font("Arial", Font.BOLD, 15));
        iniciar.addActionListener(this);
        pAbajo.add(iniciar, BorderLayout.EAST);
        return pAbajo;
    }
    public JPanel Centro(){
        pCentro=new JPanel();
        pCentro.setBackground(Color.WHITE);
        pCentro.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.insets=new Insets(10, 10, 10, 10);
        nombre=new JLabel("Nombre de Usuario:");
        nombre.setFont(new Font("Arial", Font.BOLD, 20));
        nombre.setHorizontalAlignment(JLabel.CENTER);
        pCentro.add(nombre, gbc);
        gbc.gridy=1;
        txtUsuario=new JTextField(20);
        pCentro.add(txtUsuario,gbc);
        gbc.gridy=2;
        contraseña=new JLabel("Contraseña:");
        contraseña.setFont(new Font("Arial", Font.BOLD, 20));
        contraseña.setHorizontalAlignment(JLabel.CENTER);
        pCentro.add(contraseña, gbc);
        gbc.gridy=3;
        contra=new JPasswordField(20);
        pCentro.add(contra, gbc);
        return pCentro;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == atras){
            this.dispose();
            new Inicio();
        }
        
        if(e.getSource() == iniciar){
            String user = txtUsuario.getText();
            String pass = new String(contra.getPassword());

            if(user.isEmpty() || pass.isEmpty()){
                JOptionPane.showMessageDialog(this, "Por favor, ingresa tu usuario y contraseña.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Connection cn = Conexion.obtenerConexion();
            if(cn != null){
                try {
                    String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
                    PreparedStatement pst = cn.prepareStatement(sql);
                    pst.setString(1, user);
                    pst.setString(2, pass);
                    
                    ResultSet rs = pst.executeQuery();
                    
                    if(rs.next()){
                        JOptionPane.showMessageDialog(this, "¡Acceso concedido! Bienvenido, " + user + ".");
                        this.dispose();
                        new Pantalla_Principal();
                    } else {
                        JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    rs.close();
                    pst.close();
                    cn.close();
                    
                } catch(SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al consultar la Base de Datos:\n" + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
}
