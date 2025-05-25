package controller;

import model.Etudiant;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/*Cette classe représente une boîte de dialogue personnalisée pour ajouter un étudiant
Elle affiche des champs de saisie dans une grille, et retourne
un objet Etudiant seulement si les données sont valides et que l’utilisateur confirme*/

public class EtudiantDialog extends Dialog<Etudiant> {

    public EtudiantDialog() {
        // Définition du titre de la boîte de dialogue
        setTitle("Ajouter un étudiant");

        // Texte d’en-tête affiché en haut de la boîte de dialogue
        setHeaderText("Entrez les informations de l'étudiant");

        // Création d’un bouton personnalisé "Ajouter" qui sera utilisé pour valider les données
        ButtonType addButton = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);

        // Ajout des boutons "Ajouter" et "Annuler" à la boîte de dialogue
        getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Création d’un conteneur en grille pour organiser les champs de saisie (2 colonnes : label + champ)
        GridPane grid = new GridPane();
        grid.setHgap(10); // Espace horizontal entre les colonnes
        grid.setVgap(10); // Espace vertical entre les lignes

        // Création des champs de texte pour saisir les données de l'étudiant
        TextField numField = new TextField();
        numField.setPromptText("Numéro étudiant"); // Texte d’aide affiché dans le champ

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField telField = new TextField();
        telField.setPromptText("Téléphone");

        // Ajout des éléments dans la grille (label à gauche, champ de saisie à droite)
        grid.add(new Label("Numéro:"), 0, 0);
        grid.add(numField, 1, 0);

        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nomField, 1, 1);

        grid.add(new Label("Prénom:"), 0, 2);
        grid.add(prenomField, 1, 2);

        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);

        grid.add(new Label("Téléphone:"), 0, 4);
        grid.add(telField, 1, 4);

        // Ajout de la grille dans la boîte de dialogue
        getDialogPane().setContent(grid);

        // Gestion du résultat quand l’utilisateur clique sur "Ajouter"
        setResultConverter(dialogButton -> {
            // Vérifie si le bouton cliqué est le bouton "Ajouter"
            if (dialogButton == addButton) {
                try {
                    // Tentative de conversion du numéro étudiant en entier
                    int num = Integer.parseInt(numField.getText());

                    // Création d’un nouvel objet Etudiant avec les données saisies
                    return new Etudiant(
                            num,
                            nomField.getText(),
                            prenomField.getText(),
                            emailField.getText(),
                            telField.getText()
                    );
                } catch (NumberFormatException e) {
                    // Si le numéro n’est pas un entier, on retourne null (dialogue échoue)
                    return null;
                }
            }
            // Si lutilisateur annule ou clique sur un autre bouton, retourne null
            return null;
        });
    }
}
