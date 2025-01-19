package convertir;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Lecture du fichier XML
            FileInputStream input = new FileInputStream("test.xml");
            
            // Initialisation du parser
            XMLParser parser = new XMLParser(input);
            
            try {
                // Parsing du XML
                parser.parse();
                
                // Récupération du JSON
                String json = parser.getJSON();
                
                // Écriture dans un fichier
                try (FileWriter writer = new FileWriter("output.json")) {
                    writer.write(json);
                }
                
                // Affichage du résultat
                System.out.println("Conversion réussie !");
                System.out.println("JSON généré :");
                System.out.println(json);
                
            } catch (ParseException e) {
                System.err.println("Erreur de parsing : " + e.getMessage());
            }
            
        } catch (IOException e) {
            System.err.println("Erreur d'entrée/sortie : " + e.getMessage());
        }
    }
}