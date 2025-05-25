// Import des classes JavaFX nécessaires pour construire l'interface graphique
import javafx.application.Application;      // Classe de base de toute application JavaFX
import javafx.fxml.FXMLLoader;              // Pour charger les fichiers FXML (interface utilisateur)
import javafx.scene.Parent;                 // Racine de l’arbre de la scène
import javafx.scene.Scene;                  // Représente la "scène" JavaFX (conteneur de l’interface)
import javafx.scene.image.Image;            // Classe pour gérer les images
import javafx.scene.image.ImageView;        // Pour afficher les images dans l’interface
import javafx.scene.layout.StackPane;       // Un conteneur qui empile les éléments les uns sur les autres
import javafx.stage.Stage;                  // Représente la fenêtre principale de l'application

public class Main extends Application {     // La classe hérite de Application (obligatoire pour une app JavaFX)

    @Override
    public void start(Stage stage) { // Méthode appelée automatiquement au lancement de l'app
        try {
            // 1. Charger le fichier FXML (interface graphique) depuis les ressources
            // Ce fichier définit visuellement l'interface avec des balises XML (comme du HTML)
            Parent fxmlRoot = FXMLLoader.load(getClass().getResource("resources/Main.fxml"));

            // 2. Charger une image de fond depuis les ressources du projet
            // Elle est située dans src/main/resources/com/example/javaoracle/images/bookshelf.jpg
            Image backgroundImage = new Image(getClass().getResourceAsStream("images/bookshelf.jpg"));

            // 3. Créer un composant ImageView pour afficher cette image dans l'interface
            ImageView backgroundView = new ImageView(backgroundImage);

            // 4. Configurer l’image (préserver les proportions pour éviter qu’elle soit déformée)
            backgroundView.setPreserveRatio(true);

            // 5. Appliquer une légère transparence à l'image
            backgroundView.setOpacity(0.9);

            // 6. Créer un StackPane (pile d’éléments) pour superposer l’image et l’interface
            StackPane root = new StackPane();
            root.getChildren().addAll(backgroundView, fxmlRoot); // Image en bas, interface FXML au-dessus

            // 7. Créer la scène principale avec une taille fixe (800x600 pixels ici)
            Scene scene = new Scene(root, 800, 600);

            // 8. Configurer la fenêtre (stage)
            stage.setTitle("Application de Gestion de Bibliothèque"); // Titre de la fenêtre
            stage.setScene(scene);    // Définir la scène à afficher
            stage.setResizable(false); // Empêche l'utilisateur de redimensionner la fenêtre
            stage.show();             // Affiche la fenêtre

        } catch (Exception e) {
            // Si une erreur survient (par exemple si le fichier FXML ou limage est introuvable), elle sera affichée ici
            System.err.println("Erreur lors du chargement de l'application : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Point d’entrée principal de l’application JavaFX
    public static void main(String[] args) {
        launch(args); // Lance l’application JavaFX, ce qui appelle la méthode start()
    }
}
