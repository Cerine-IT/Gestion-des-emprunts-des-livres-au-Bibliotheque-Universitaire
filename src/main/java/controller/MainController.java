package controller;

// Importation des classes JavaFX nécessaires
import javafx.fxml.FXML;              // Annotation pour lier les méthodes aux éléments FXML
import javafx.fxml.FXMLLoader;       // Pour charger les fichiers FXML
import javafx.scene.Parent;          // Élément racine de l'interface FXML
import javafx.scene.Scene;           // Représente l'affichage de la fenêtre
import javafx.stage.Stage;           // Représente la fenêtre (stage) actuelle
import javafx.event.ActionEvent;     // Événement déclenché par une action utilisateur (ex. clic bouton)
import javafx.scene.Node;            // Utilisé pour récupérer la scène actuelle à partir de l'événement
import java.io.IOException;          // Gérer les erreurs liées au chargement de fichiers

//CETTE CLASSE soccupe de gérer les événements provenant de l'interface utilisateur (les clics..)
public class MainController {

    // Constantes pour les chemins des vues(interfaces)
    private static final String VIEW_PATH = "/com/example/javaoracle/views/";
    private static final String LIVRE_VIEW = VIEW_PATH + "Livre/Livre.fxml";
    private static final String ETUDIANT_VIEW = VIEW_PATH + "etudiant/Etudiant.fxml";
    private static final String EMPRUNT_VIEW = VIEW_PATH + "emprunt/Emprunt.fxml";
    private static final String MENU_PRINCIPAL_VIEW = VIEW_PATH + "MenuPrincipal.fxml";

    //Appelée quand l’utilisateur clique sur le bouton "Gérer Livres"
    // Elle charge l’interface Livre.fxml
    @FXML
    private void gererLivres(ActionEvent event) throws IOException {
        chargerInterface(event, LIVRE_VIEW, "Gestion des Livres");
    }

    //Charge l’interface pour gérer les étudiants
    @FXML
    private void gererEtudiants(ActionEvent event) throws IOException {
        chargerInterface(event, ETUDIANT_VIEW, "Gestion des Étudiants");
    }

    //Charge l’interface pour gérer les emprunts
    @FXML
    private void gererEmprunts(ActionEvent event) throws IOException {
        chargerInterface(event, EMPRUNT_VIEW, "Gestion des Emprunts");
    }

    //Ces deux méthodes pour retourner au menu principal
    // Elles font la même chose (parfois on nomme différemment selon le bouton qui appelle)
    @FXML
    private void retourMenu(ActionEvent event) throws IOException {
        naviguerVersMenuPrincipal(event);
    }

    @FXML
    private void handleRetour(ActionEvent event) throws IOException {
        naviguerVersMenuPrincipal(event);
    }

    //Méthode générique pour charger une interface selon son chemin et son titre
    // Évite de répéter le code pour chaque bouton
    private void chargerInterface(ActionEvent event, String cheminFXML, String titreFenetre) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(cheminFXML)); // Charge le fichier FXML
        Parent root = loader.load();                                            // Charge l’arborescence de l’interface

        Stage stage = obtenirStageDepuisEvent(event);                           // Récupère la fenêtre actuelle
        stage.setTitle(titreFenetre);                                           // Change le titre de la fenêtre
        stage.setScene(new Scene(root));                                        // Définit la nouvelle scène
        stage.show();                                                           // Affiche la scène
    }

    //Revenir au menu
    private void naviguerVersMenuPrincipal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MENU_PRINCIPAL_VIEW)); // Charge le menu principal
        Parent root = loader.load();

        Stage stage = obtenirStageDepuisEvent(event); // Récupère la fenêtre
        stage.setScene(new Scene(root));              // Affiche le menu principal dans la même fenêtre
        stage.show();
    }

    //Cette méthode récupère la fenêtre actuelle (Stage) à partir de l’événement déclenché (clic sur un bouton par exemple)
    // C’est indispensable pour modifier la scène affichée dans la même fenêtre
    private Stage obtenirStageDepuisEvent(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}