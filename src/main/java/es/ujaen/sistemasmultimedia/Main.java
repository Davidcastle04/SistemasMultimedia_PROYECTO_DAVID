package es.ujaen.sistemasmultimedia;

import com.formdev.flatlaf.FlatDarkLaf;
import es.ujaen.sistemasmultimedia.INTERFAZ.UI;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {



        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UI interfaz = new UI();
            interfaz.setTitle("MultiStudio V1.0");
            interfaz.setLocationRelativeTo(null);
            interfaz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            interfaz.setResizable(false);
            interfaz.setVisible(true);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }


}