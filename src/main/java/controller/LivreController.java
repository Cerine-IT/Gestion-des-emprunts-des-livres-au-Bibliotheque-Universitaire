package controller;

// Importation des classes nécessaires pour manipuler les données, l'interface et les événements
import dao.LivreDAO;
import model.Livre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

//Contrôleur JavaFX responsable de la gestion des livres
public class LivreController {
    // Champs liés à l'interface FXML
    @FXML
    private TextField searchField;
    @FXML
    private TextField idField;
    @FXML
    private TextField titreField;
    @FXML
    private TextField auteurField;
    @FXML
    private TextField categorieField;
    @FXML
    private TextField anneeField;
    @FXML
    private TextField quantiteField;
    // TableView et ses colonnes pour afficher la liste des livres
    @FXML
    private TableView<Livre> livresTable;
    @FXML
    private TableColumn<Livre, Integer> idColumn;
    @FXML
    private TableColumn<Livre, String> titreColumn;
    @FXML
    private TableColumn<Livre, String> auteurColumn;
    @FXML
    private TableColumn<Livre, String> categorieColumn;
    @FXML
    private TableColumn<Livre, Integer> anneeColumn;
    @FXML
    private TableColumn<Livre, Integer> quantiteColumn;


    // Objet DAO pour accéder à la base de données
    private final LivreDAO livreDAO = new LivreDAO();
    // Liste observable qui contient les livres à afficher dans la TableView
    private final ObservableList<Livre> livreList = FXCollections.observableArrayList();

    //Méthode appelée automatiquement au démarrage du contrôleur
     //elle initialise la TableView et charge les livres existants
    @FXML
    public void initialize() {
        // Associe chaque colonne de la TableView à un attribut du modèle Livre
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        anneeColumn.setCellValueFactory(new PropertyValueFactory<>("anneePublication"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        idField.setOnMouseEntered(event -> {
            Tooltip.install(idField, new Tooltip("Le Code doit être unique"));
        });

        // Ajoute des infobulles sur les champs pour informer l’utilisateur
        titreField.setOnMouseEntered(event -> {
            Tooltip.install(titreField, new Tooltip("Le Titre doit être unique"));
        });
        // Charge les livres depuis la BDD
        chargerLivres();
    }

    //charge tous les livres depuis la BDD et met à jour la TableView
    private void chargerLivres() {
        livreList.setAll(livreDAO.getTousLesLivres());
        livresTable.setItems(livreList);
    }

    //Ajoute un nouveau livre à la BDD si les champs sont valides
    @FXML
    private void ajouterLivre(ActionEvent event) {
        try {
            // Vérifie si tous les champs sont remplis
            if (idField.getText().isEmpty() || titreField.getText().isEmpty() ||
                    auteurField.getText().isEmpty() || categorieField.getText().isEmpty() ||
                    anneeField.getText().isEmpty() || quantiteField.getText().isEmpty()) {
                afficherAlerte("Champs manquants", "Veuillez remplir tous les champs.");
                return;
            }

            // Crée un objet Livre à partir des champs saisis
            Livre livre = new Livre(
                    Integer.parseInt(idField.getText()),
                    titreField.getText(),
                    auteurField.getText(),
                    categorieField.getText(),
                    Integer.parseInt(anneeField.getText()),
                    Integer.parseInt(quantiteField.getText())
            );

            // Ajoute le livre via le DAO
            boolean success = livreDAO.ajouterLivre(livre);
            if (success) {
                afficherAlerte("Succès", "Livre ajouté avec succès.");
                chargerLivres();
                initialiserChamps();
            } else {
                afficherAlerte("Erreur", "Le livre n’a pas pu être ajouté (peut-être déjà existant ?).");
            }

        } catch (NumberFormatException e) {
            // Gestion des erreurs si des champs numériques ne contiennent pas un nombre valide
            afficherAlerte("Erreur de format", "ID, année ou quantité doivent être des nombres valides.");
        } catch (Exception e) {
            afficherAlerte("Exception", "Erreur : " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Permet de modifier les informations du livre sélectionné dans la TableView
    @FXML
    private void modifierLivre(ActionEvent event) {
        Livre selected = livresTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Met à jour les champs du livre sélectionné
                selected.setTitre(titreField.getText());
                selected.setAuteur(auteurField.getText());
                selected.setCategorie(categorieField.getText());
                selected.setAnneePublication(Integer.parseInt(anneeField.getText()));
                selected.setQuantite(Integer.parseInt(quantiteField.getText()));

                if (livreDAO.modifierLivre(selected)) {
                    afficherAlerte("Succès", "Livre modifié avec succès.");
                    chargerLivres();
                } else {
                    afficherAlerte("Erreur", "Échec de la modification du livre.");
                }
            } catch (NumberFormatException e) {
                afficherAlerte("Erreur", "Veuillez entrer des valeurs valides pour les champs numériques.");
            }
        } else {
            afficherAlerte("Avertissement", "Veuillez sélectionner un livre à modifier.");
        }
    }

    //Supprime le livre sélectionné dans la TableView
    @FXML
    private void supprimerLivre(ActionEvent event) {
        Livre selected = livresTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (livreDAO.supprimerLivre(selected.getId())) {
                afficherAlerte("Succès", "Livre supprimé avec succès.");
                chargerLivres();
                initialiserChamps();
            } else {
                afficherAlerte("Erreur", "Échec de la suppression du livre.");
            }
        } else {
            afficherAlerte("Avertissement", "Veuillez sélectionner un livre à supprimer.");
        }
    }

    //Réinitialise tous les champs de texte
    @FXML
    private void initialiserChamps() {
        idField.clear();
        titreField.clear();
        auteurField.clear();
        categorieField.clear();
        anneeField.clear();
        quantiteField.clear();
    }

    //Retourne à la page principale
    @FXML
    private void retourMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaoracle/views/main.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Recherche les livres correspondant au mot-clé saisi dans la barre de recherche
    @FXML
    private void rechercherLivre(ActionEvent event) {
        String motCle = searchField.getText();
        if (!motCle.isEmpty()) {
            livreList.setAll(livreDAO.rechercherLivres(motCle));
            livresTable.setItems(livreList);
        } else {
            chargerLivres();
        }
    }

    //Affiche une alerte (popup) avec un message donné
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Remplit les champs de saisie avec les données du livre sélectionné dans la table
    @FXML
    private void remplirChampsDepuisTable() {
        Livre selected = livresTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            idField.setText(String.valueOf(selected.getId()));
            titreField.setText(selected.getTitre());
            auteurField.setText(selected.getAuteur());
            categorieField.setText(selected.getCategorie());
            anneeField.setText(String.valueOf(selected.getAnneePublication()));
            quantiteField.setText(String.valueOf(selected.getQuantite()));
        }
    }
}
