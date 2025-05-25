package controller;

// Importation des classes nécessaires (modèles, DAO, JavaFX)
import dao.EmpruntDao;
import dao.EtudiantDAO;
import dao.LivreDAO;
import model.Etudiant;
import model.Livre;
import model.Emprunt;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class EmpruntController {
    // Instanciation des DAO pour interagir avec la BDD
    private final EmpruntDao empruntDao = new EmpruntDao();
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();
    private final LivreDAO livreDAO = new LivreDAO();

    // Liaison des éléments FXML de l’interface
    @FXML private ComboBox<Etudiant> comboEtudiant;
    @FXML private ComboBox<Livre> comboLivre;
    @FXML private DatePicker dateEmpruntPicker;
    @FXML private DatePicker dateRetourPicker;
    @FXML private Label messageLabel;
    @FXML private TableView<Emprunt> tableEmprunts;
    @FXML private TableColumn<Emprunt, Integer> colId;
    @FXML private TableColumn<Emprunt, String> colEtudiant;
    @FXML private TableColumn<Emprunt, String> colLivre;
    @FXML private TableColumn<Emprunt, LocalDate> colDateEmprunt;
    @FXML private TableColumn<Emprunt, LocalDate> colDateRetour;
    @FXML private TableColumn<Emprunt, String> colStatut;

    // Méthode appelée automatiquement après le chargement du FXML
    @FXML
    public void initialize() {
        configurerTableau();       // Configuration des colonnes de la table
        chargerEtudiants();        // Chargement des étudiants dans la comboBox
        chargerLivres();           // Chargement des livres disponibles
        chargerEmpruntsActifs();   // Chargement des emprunts non retournés
    }

    //configurer les colonnes de la table des emprunts
    private void configurerTableau() {
        // Associer la colonne ID à l'attribut "id" de la classe Emprunt
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Afficher les noms complets des étudiants à partir de leur numéro
        colEtudiant.setCellValueFactory(cellData -> {
            Etudiant etudiant = etudiantDAO.getEtudiantByNum(cellData.getValue().getNumEtudiant());
            return new SimpleStringProperty(etudiant != null ? etudiant.toString() : "Inconnu");
        });

        // Afficher le titre du livre emprunté
        colLivre.setCellValueFactory(cellData -> {
            Livre livre = livreDAO.getLivreById(cellData.getValue().getIdLivre());
            return new SimpleStringProperty(livre != null ? livre.getTitre() : "Inconnu");
        });

        // Associer les dates d’emprunt et de retour
        colDateEmprunt.setCellValueFactory(new PropertyValueFactory<>("dateEmprunt"));
        colDateRetour.setCellValueFactory(new PropertyValueFactory<>("dateRetour"));

        // Statut : Affiché comme "En cours" ou "Retourné"
        colStatut.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isEstRetourne() ? "Retourné" : "En cours"));
    }

    // Chargement de tous les étudiants dans la comboBox
    private void chargerEtudiants() {
        try {
            List<Etudiant> etudiants = etudiantDAO.getAllEtudiants();
            comboEtudiant.setItems(FXCollections.observableArrayList(etudiants));
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement des étudiants");
            e.printStackTrace();
        }
    }

    // Chargement des livres disponibles dans la comboBox
    private void chargerLivres() {
        try {
            List<Livre> livres = livreDAO.getTousLesLivres();
            comboLivre.setItems(FXCollections.observableArrayList(livres));
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement des livres");
            e.printStackTrace();
        }
    }

    // Affichage des emprunts non retournés dans la table
    private void chargerEmpruntsActifs() {
        try {
            List<Emprunt> emprunts = empruntDao.getEmpruntsActifs();
            tableEmprunts.setItems(FXCollections.observableArrayList(emprunts));
        } catch (Exception e) {
            afficherErreur("Erreur lors du chargement des emprunts");
            e.printStackTrace();
        }
    }

    //méthode déclenchée lorsqu'on clique sur "Ajouter un emprunt"
    @FXML
    private void ajouterEmprunt(ActionEvent event) {
        try {
            // Récupération des valeurs sélectionnées ou saisies
            Etudiant etudiant = comboEtudiant.getValue();
            Livre livre = comboLivre.getValue();
            LocalDate dateEmprunt = dateEmpruntPicker.getValue();
            LocalDate dateRetour = dateRetourPicker.getValue();

            // Vérification des champs obligatoires
            if (etudiant == null || livre == null || dateEmprunt == null || dateRetour == null) {
                afficherErreur("Veuillez remplir tous les champs.");
                return;
            }

            // Vérification de la cohérence des dates
            if (dateRetour.isBefore(dateEmprunt)) {
                afficherErreur("La date de retour doit être après la date d'emprunt");
                return;
            }

            // Création d’un objet Emprunt (l’ID sera généré automatiquement)
            // Ne pas générer l'ID manuellement, laissez la base de données le faire
            Emprunt emprunt = new Emprunt(0, etudiant.getNumEtudiant(), livre.getId(),
                    dateEmprunt, dateRetour, false);

            // Ajout dans la base de données via DAO
            if (empruntDao.ajouterEmprunt(emprunt)) {
                afficherSucces("Emprunt ajouté avec succès !");
                reinitialiserFormulaire();     // Vide le formulaire
                chargerEmpruntsActifs();
            } else {
                afficherErreur("Erreur lors de l'ajout de l'emprunt");
            }
        } catch (Exception e) {
            afficherErreur("Une erreur est survenue");
            e.printStackTrace();
        }
    }

    // Méthode appelée pour retourner un livre (modifier le statut)
    @FXML
    private void retournerLivre(ActionEvent event) {
        // Récupération de l’emprunt sélectionné dans la table
        Emprunt empruntSelectionne = tableEmprunts.getSelectionModel().getSelectedItem();
        if (empruntSelectionne == null) {
            afficherErreur("Veuillez sélectionner un emprunt à retourner");
            return;
        }

        try {
            // Marque l’emprunt comme retourné dans la base
            if (empruntDao.enregistrerRetour(empruntSelectionne.getId())) {
                afficherSucces("Retour enregistré avec succès !");
                chargerEmpruntsActifs();   // Mise à jour de la table
            } else {
                afficherErreur("Erreur lors de l'enregistrement du retour");
            }
        } catch (Exception e) {
            afficherErreur("Une erreur est survenue");
            e.printStackTrace();
        }
    }

    // Retour vers le menu principal (main.fxml)
    @FXML
    private void retourMenu(ActionEvent event) {
        try {
            // Chargement du fichier de la scène principale
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javaoracle/views/main.fxml"));
            Parent root = loader.load();

            // Remplacement de la scène actuelle par celle du menu
            Stage stage = (Stage) comboEtudiant.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            afficherErreur("Erreur lors du retour au menu");
            e.printStackTrace();
        }
    }

    // Réinitialisation des champs du formulaire
    private void reinitialiserFormulaire() {
        comboEtudiant.setValue(null);
        comboLivre.setValue(null);
        dateEmpruntPicker.setValue(null);
        dateRetourPicker.setValue(null);
    }

    // Affiche un message de succès en vert
    private void afficherSucces(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green;");
    }

    // Affiche un message derreur en rouge
    private void afficherErreur(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red;");
    }
}
