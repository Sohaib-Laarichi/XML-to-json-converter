package com.example.ui;

import com.example.parser.XMLParser;
import org.json.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLToJSONConverterUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextArea xmlTextArea;
    private JTextArea jsonTextArea;
    private JButton loadButton;
    private JButton convertButton;
    private JButton saveButton;

    public XMLToJSONConverterUI() {
        setTitle("XML to JSON Converter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Créer les composants
        xmlTextArea = new JTextArea();
        jsonTextArea = new JTextArea();
        loadButton = new JButton("Load XML File");
        convertButton = new JButton("Convert to JSON");
        saveButton = new JButton("Save JSON");

        // Personnaliser les couleurs
        xmlTextArea.setBackground(new Color(240, 240, 240)); // Fond gris clair
        xmlTextArea.setForeground(new Color(0, 0, 0)); // Texte noir
        jsonTextArea.setBackground(new Color(240, 240, 240)); // Fond gris clair
        jsonTextArea.setForeground(new Color(0, 0, 0)); // Texte noir

        loadButton.setBackground(new Color(50, 150, 250)); // Fond bleu
        loadButton.setForeground(Color.WHITE); // Texte blanc
        convertButton.setBackground(new Color(50, 150, 250)); // Fond bleu
        convertButton.setForeground(Color.WHITE); // Texte blanc
        saveButton.setBackground(new Color(50, 150, 250)); // Fond bleu
        saveButton.setForeground(Color.WHITE); // Texte blanc

        // Ajouter des écouteurs d'événements
        loadButton.addActionListener(new LoadButtonListener());
        convertButton.addActionListener(new ConvertButtonListener());
        saveButton.addActionListener(new SaveButtonListener());

        // Ajouter des écouteurs pour les événements de survol des boutons
        loadButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loadButton.setBackground(new Color(30, 130, 230)); // Couleur plus foncée au survol
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loadButton.setBackground(new Color(50, 150, 250)); // Revenir à la couleur d'origine
            }
        });

        convertButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                convertButton.setBackground(new Color(30, 130, 230)); // Couleur plus foncée au survol
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                convertButton.setBackground(new Color(50, 150, 250)); // Revenir à la couleur d'origine
            }
        });

        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(30, 130, 230)); // Couleur plus foncée au survol
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveButton.setBackground(new Color(50, 150, 250)); // Revenir à la couleur d'origine
            }
        });

        // Créer un panneau pour les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(200, 200, 200)); // Fond gris
        buttonPanel.add(loadButton);
        buttonPanel.add(convertButton);
        buttonPanel.add(saveButton);

        // Créer un panneau pour les zones de texte
        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        textPanel.setBackground(new Color(200, 200, 200)); // Fond gris
        textPanel.add(new JScrollPane(xmlTextArea));
        textPanel.add(new JScrollPane(jsonTextArea));

        // Ajouter les composants à la fenêtre
        add(buttonPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);
    }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    StringBuilder xmlContent = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        xmlContent.append(line).append("\n");
                    }
                    xmlTextArea.setText(xmlContent.toString());
                    JOptionPane.showMessageDialog(null, "Fichier XML chargé avec succès !", "Information", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class ConvertButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String xmlContent = xmlTextArea.getText();
            if (xmlContent.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please load an XML file first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            new SwingWorker<JSONObject, Void>() {
                @Override
                protected JSONObject doInBackground() throws Exception {
                    XMLParser parser = new XMLParser(new java.io.ByteArrayInputStream(xmlContent.getBytes()));
                    return parser.document();
                }

                @Override
                protected void done() {
                    try {
                        JSONObject json = get();
                        jsonTextArea.setText(json.toString(2));
                        JOptionPane.showMessageDialog(null, "Conversion XML vers JSON terminée !", "Information", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        jsonTextArea.setText("");
                        JOptionPane.showMessageDialog(null, "Error converting XML to JSON: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String jsonContent = jsonTextArea.getText();
            if (jsonContent.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No JSON content to save.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save JSON File");
            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(".json")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".json");
                }
                try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                    fos.write(jsonContent.getBytes(StandardCharsets.UTF_8));
                    JOptionPane.showMessageDialog(null, "Fichier JSON sauvegardé avec succès !", "Information", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void launchUI() {
        SwingUtilities.invokeLater(() -> {
            XMLToJSONConverterUI ui = new XMLToJSONConverterUI();
            ui.setVisible(true);
        });
    }

    public static void main(String[] args) {
        launchUI();
    }
}