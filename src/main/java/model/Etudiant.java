package model;

import javafx.beans.property.*;

//Cette classe représente un étudiant dans le système de gestion de bibliothèque
//Elle contient des propriétés observables JavaFX : numéro étudiant, nom, prénom, email, téléphone
//Elle permet d’utiliser le data binding avec JavaFX (affichage automatique dans des TableView, TextField...)

public class Etudiant {
    private final IntegerProperty numEtudiant = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telephone = new SimpleStringProperty();

    public Etudiant(int numEtudiant, String nom, String prenom, String email, String telephone) {
        setNumEtudiant(numEtudiant);
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
        setTelephone(telephone);
    }

    // Getters (property)
    public IntegerProperty numEtudiantProperty() { return numEtudiant; }
    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public StringProperty emailProperty() { return email; }
    public StringProperty telephoneProperty() { return telephone; }

    // Getters (standard)
    public int getNumEtudiant() { return numEtudiant.get(); }
    public String getNom() { return nom.get(); }
    public String getPrenom() { return prenom.get(); }
    public String getEmail() { return email.get(); }
    public String getTelephone() { return telephone.get(); }

    // Setters
    public void setNumEtudiant(int num) { numEtudiant.set(num); }
    public void setNom(String value) { nom.set(value); }
    public void setPrenom(String value) { prenom.set(value); }
    public void setEmail(String value) { email.set(value); }
    public void setTelephone(String value) { telephone.set(value); }
    @Override
    public String toString() {
        return getNumEtudiant() + " - " + getNom() +" " + getPrenom();
    }

}