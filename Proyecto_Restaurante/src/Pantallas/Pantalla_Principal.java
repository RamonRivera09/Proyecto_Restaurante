package Pantallas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Pantalla_Principal extends JFrame implements ActionListener {

    private JPanel pArriba, pCentro;
    private JLabel bienvenida;
    private JButton btn1, btn2, btn3, btn4, btn5, btn6;

    public Pantalla_Principal() {
        configFrame();
        initComponents();
        setVisible(true);
    }

    public void configFrame() {
        setSize(550, 400);
        setTitle("Restaurante - Inicio");
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icono = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Imagenes/logoRestaurante.png"));
        setIconImage(icono);
    }

    public void initComponents() {
        add(Arriba(), BorderLayout.NORTH);
        add(Centro(), BorderLayout.CENTER);
    }

    public JPanel Arriba() {
        pArriba = new JPanel();
        pArriba.setLayout(new FlowLayout());
        pArriba.setBackground(Color.red);
        bienvenida = new JLabel("Bienvenido al sistema. Elige una opción:");
        bienvenida.setFont(new Font("Arial", Font.BOLD, 25));
        bienvenida.setForeground(Color.WHITE);
        pArriba.add(bienvenida);
        return pArriba;
    }

    public JPanel Centro() {
        pCentro = new JPanel();
        pCentro.setBackground(Color.WHITE);
        pCentro.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        btn1 = new JButton("Gestionar Empleados");
        btn2 = new JButton("Gestionar Clientes");
        btn3 = new JButton("Tomar pedidos");
        btn4 = new JButton("Registrar Ventas");
        btn5 = new JButton("Cerrar Sesión");
        btn6=new JButton ("Menú");
        btn5.setForeground(Color.red);
        JButton[] botones = new JButton[]{btn1, btn2,btn6, btn3, btn4, btn5};
        int c = 0;
        for (JButton b : botones) {
            gbc.gridy = c;
            b.addActionListener(this);
            b.setFont(new Font("Arial", Font.BOLD, 20));
            pCentro.add(b, gbc);
            c++;
        }
        return pCentro;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn1) {
            this.dispose();
            new Pantalla_Empleado();
        }
        if(e.getSource()==btn2){
            this.dispose();
            new Pantalla_Cliente();
        }
        if(e.getSource()==btn6){
            this.dispose();
            new Pantalla_Menu();
        }
if (e.getSource()==btn3){
    this.dispose();
    new Pantalla_Pedidos();
}

if (e.getSource()==btn4){
    this.dispose();
    new Pantalla_Ventas();
}
        if (e.getSource() == btn5) {
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de cerrar sesión?",
                    "Confirmar Cierre de Sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                this.dispose();
                new Login.Inicio();
            }

        }
    }
}
