package com.example.xmltojson;

import com.example.xmltojson.view.XMLToJSONView;

public class Main {
    public static void main(String[] args) {
        // Lancer l'interface graphique
        javax.swing.SwingUtilities.invokeLater(() -> {
            XMLToJSONView view = new XMLToJSONView();
            view.setVisible(true);
        });
    }
}