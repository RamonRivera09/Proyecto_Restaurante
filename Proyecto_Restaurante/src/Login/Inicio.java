
package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Inicio extends JFrame implements ActionListener{
    private JPanel pArriba, pCentro;
    private JLabel bienvenida, elige;
    private JButton iniciar, registrar;
    private Font fuente=new Font("Arial", Font.BOLD,20);
    public Inicio(){
        configFrame();
        initComponents();
        setVisible (true);
    }
    public void configFrame(){
        setTitle ("Restaurante - Inicio");
        setSize (400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagenes/logoRestaurante.png"));
        setIconImage(icono);
    }
    public void initComponents(){
        add(Arriba(), BorderLayout.NORTH);
        add(Centro(),BorderLayout.CENTER);
    }
    public JPanel Arriba(){
        pArriba = new JPanel();
        pArriba.setBackground(Color.red);
        pArriba.setLayout(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.insets=new Insets(10, 10, 10, 10);
        gbc.gridx=0;
        gbc.gridy=0;
        bienvenida=new JLabel ("Bienvenido al sistema");
        bienvenida.setFont(new Font("Arial",Font.BOLD, 20));
        bienvenida.setForeground(Color.WHITE);
        pArriba.add(bienvenida, gbc);
        return pArriba;
    }
    public JPanel Centro(){
        pCentro=new JPanel();
        pCentro.setBackground(Color.WHITE);
        pCentro.setLayout(new GridBagLayout());
        GridBagConstraints gbc= new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.insets=new Insets(10,10,30,10);
        elige=new JLabel("Elige una opción:");
        elige.setHorizontalAlignment(JLabel.CENTER);
        elige.setFont(new Font("Arial", Font.BOLD, 20));
        pCentro.add(elige, gbc);
        gbc.gridy=1;
        gbc.insets=new Insets(10, 10, 30, 10);
        iniciar=new JButton("Iniciar sesión");
        iniciar.setFont(fuente);
        iniciar.addActionListener(this);
        pCentro.add(iniciar,gbc);
        gbc.gridy=2;
        registrar=new JButton("Registrar usuario");
        registrar.setFont(fuente);
        registrar.addActionListener(this);
        pCentro.add(registrar, gbc);
        return pCentro;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==iniciar){
            this.dispose();
            new Ingreso();
        }
        if(e.getSource()==registrar){
            this.dispose();
            new Registro();
        }
    }
    public static void main(String[] args) {
        new Inicio();
    }
}
