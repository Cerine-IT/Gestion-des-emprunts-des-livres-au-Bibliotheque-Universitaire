package controller;

import dao.EtudiantDAO;// Accès aux données des étudiants
import model.Etudiant;// Modèle Etudiant
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML; // Pour lier les éléments FXML
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // Fenêtre de message
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EtudiantController {

    // Champs liés au fichier FXML (interface utilisateur)
    @FXML private TextField numEtudiantField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private ComboBox<Etudiant> etudiantComboBox;

    // DAO pour les opérations sur la BDD
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();
    // Liste observable pour les données de la ComboBox
    private final ObservableList<Etudiant> etudiantsList = FXCollections.observableArrayList();

    // Méthode appelée automatiquement à l'initialisation du contrôleur
    @FXML
    public void initialize() {
        // Charger la liste des étudiants au démarrage
        loadEtudiants();

        // Configurer la ComboBox pour afficher le nom et prénom de l'étudiant
        etudiantComboBox.setCellFactory(param -> new ListCell<Etudiant>() {
            @Override
            protected void updateItem(Etudiant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); // Ne rien afficher si vide
                } else {
                    setText(item.getNumEtudiant() + " - " + item.getNom() + " " + item.getPrenom());
                }
            }
        });

        // Définir comment l'élément sélectionné est affiché
        etudiantComboBox.setButtonCell(new ListCell<Etudiant>() {
            @Override
            protected void updateItem(Etudiant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNumEtudiant() + " - " + item.getNom() + " " + item.getPrenom());
                }
            }
        });

        // Lorsqu'un étudiant est sélectionné, remplir les champs
        etudiantComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillFieldsWithEtudiant(newVal); // Remplir les champs
            }
        });
    }

    //charger tous les étudiants depuis la BDD
    private void loadEtudiants() {
        etudiantsList.clear(); // Vider la liste existante
        etudiantsList.addAll(etudiantDAO.getAllEtudiants());// Ajouter les étudiants récupérés depuis la base
        etudiantComboBox.setItems(etudiantsList); // Affecter la liste à la ComboBox
    }

    //Remplir les champs avec les infos d’un étudiant sélectionné
    private void fillFieldsWithEtudiant(Etudiant etudiant) {
        numEtudiantField.setText(String.valueOf(etudiant.getNumEtudiant()));
        nomField.setText(etudiant.getNom());
        prenomField.setText(etudiant.getPrenom());
        emailField.setText(etudiant.getEmail());
        telephoneField.setText(etudiant.getTelephone());
    }

    //retourner au menu principal
    @FXML
    private void retourMenu(ActionEvent event) {
        try {
            // Charger la vue du menu principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaoracle/views/main.fxml"));
            Parent root = loader.load();

            // Récupérer la fenêtre actuelle et changer la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();// En cas d’erreur de chargement
        }
    }

    //Ajout dun nouvel etudiant
    @FXML
    private void handleAddEtudiant() {
        try {
            // Récupérer et valider les données entrées par l’utilisateur
            int numetd = Integer.parseInt(numEtudiantField.getText());
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            String telephone = telephoneField.getText();

            // Créer un objet Etudiant
            Etudiant etudiant = new Etudiant(numetd, nom, prenom, email, telephone);
            // Ajouter l’étudiant via le DAO
            boolean success = etudiantDAO.ajouterEtudiant(etudiant);

            if (success) {
                showAlert("Succès", "Étudiant ajouté avec succès !");
                clearFields();//vider les champs
                loadEtudiants(); // Rafraîchir la liste
            } else {
                showAlert("Erreur", "Échec de l'ajout de l'étudiant.");
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Le numéro de l'étudiant doit être un entier.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    //modifier les informations d’un étudiant existant
    @FXML
    private void handleUpdateEtudiant() {
        // Récupérer l’étudiant sélectionné dans la ComboBox
        Etudiant selected = etudiantComboBox.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Aucun étudiant sélectionné !");
            return;
        }

        try {
            // Récupérer les nouvelles valeurs
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            String telephone = telephoneField.getText();

            // Créer un nouvel objet Etudiant avec les valeurs mises à jour
            Etudiant updatedEtudiant = new Etudiant(selected.getNumEtudiant(), nom, prenom, email, telephone);
            // Modifier dans la base
            boolean success = etudiantDAO.modifierEtudiant(updatedEtudiant);

            if (success) {
                showAlert("Succès", "Étudiant modifié avec succès !");
                clearFields();
                loadEtudiants(); // Rafraîchir la liste
            } else {
                showAlert("Erreur", "Échec de la modification de l'étudiant.");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    //supprimer un étudiant sélectionné
    @FXML
    private void handleDeleteEtudiant() {
        // Vérifier qu’un étudiant est sélectionné
        Etudiant selected = etudiantComboBox.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Aucun étudiant sélectionné !");
            return;
        }

        // Supprimer l’étudiant via le DAO
        boolean success = etudiantDAO.supprimerEtudiant(selected.getNumEtudiant());
        if (success) {
            showAlert("Succès", "Étudiant supprimé avec succès !");
            clearFields();
            loadEtudiants(); // Rafraîchir la liste
        } else {
            showAlert("Erreur", "Échec de la suppression de l'étudiant.");
        }
    }

    //afficher une alerte à l'utilisateur
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);//Type d’alerte : information
        alert.setTitle(titre);
        alert.setHeaderText(null);//pas de sous-titre
        alert.setContentText(message);//message principal
        alert.showAndWait();// Attendre que l’utilisateur ferme la fenêtre
    }

    //vider tous les champs de saisie
    private void clearFields() {
        numEtudiantField.clear();
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        telephoneField.clear();
        etudiantComboBox.getSelectionModel().clearSelection();// Désélectionner l’étudiant
    }

    // Méthode liée à un bouton pour initialiser les champs
    @FXML
    private void initialiserChamps() {
        clearFields();
    }
}
