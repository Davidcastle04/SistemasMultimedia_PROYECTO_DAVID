package es.ujaen.sistemasmultimedia;

import com.formdev.flatlaf.FlatDarkLaf;
import es.ujaen.sistemasmultimedia.INTERFAZ.UI;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UI interfaz = new UI();
            interfaz.setTitle("MultiStudio V1.0  DAVID CASTILLO SERRANO");
            interfaz.setLocationRelativeTo(null);
            interfaz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            interfaz.setResizable(false);
            interfaz.setVisible(true);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }


}