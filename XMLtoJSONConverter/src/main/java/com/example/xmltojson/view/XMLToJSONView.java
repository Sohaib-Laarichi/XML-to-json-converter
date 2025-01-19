package com.example.xmltojson.view;

import com.example.xmltojson.controller.XMLToJSONController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class XMLToJSONView extends JFrame {
    private JTextArea xmlTextArea;
    private JTextArea jsonTextArea;
    private JButton openButton;
    private JButton convertButton;
    private JButton saveButton;
    private File selectedFile;

    public XMLToJSONView() {
        setTitle("XML to JSON Converter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Zones de texte pour afficher XML et JSON
        xmlTextArea = new JTextArea();
        jsonTextArea = new JTextArea();
        JScrollPane xmlScrollPane = new JScrollPane(xmlTextArea);
        JScrollPane jsonScrollPane = new JScrollPane(jsonTextArea);

        // Boutons
        openButton = new JButton("Ouvrir un fichier XML");
        convertButton = new JButton("Convertir en JSON");
        saveButton = new JButton("Sauvegarder JSON");

        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(convertButton);
        buttonPanel.add(saveButton);

        // Panneau pour les zones de texte
        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        textPanel.add(xmlScrollPane);
        textPanel.add(jsonScrollPane);

        // Ajouter les panneaux à la fenêtre
        add(buttonPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);

        // Gestion des événements
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    try {
                        java.util.Scanner scanner = new java.util.Scanner(selectedFile);
                        StringBuilder xmlContent = new StringBuilder();
                        while (scanner.hasNextLine()) {
                            xmlContent.append(scanner.nextLine()).append("\n");
                        }
                        scanner.close();
                        xmlTextArea.setText(xmlContent.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    try {
                        String json = XMLToJSONController.convertXMLToJSON(selectedFile);
                        jsonTextArea.setText(json);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jsonTextArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Aucun JSON à sauvegarder !");
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        java.io.FileWriter writer = new java.io.FileWriter(file);
                        writer.write(jsonTextArea.getText());
                        writer.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}