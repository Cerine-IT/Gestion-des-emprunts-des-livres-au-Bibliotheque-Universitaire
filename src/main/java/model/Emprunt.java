package model;

import java.time.LocalDate;
import javafx.beans.property.*;

/* Cette classe représente un emprunt de livre dans la bibliothèque.
Elle relie un étudiant à un livre avec les dates d’emprunt et de retour,
et un indicateur de retour effectué ou non */

//Elle contient les identifiants de l’emprunt, de l’étudiant, du livre,
// les dates (emprunt et retour), et un booléen estRetourne
// Elle permet de suivre l'historique et le statut de chaque emprunt

public class Emprunt {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty numEtudiant = new SimpleIntegerProperty();
    private final IntegerProperty idLivre = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> dateEmprunt = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateRetour = new SimpleObjectProperty<>();
    private final BooleanProperty estRetourne = new SimpleBooleanProperty();

    // Constructeur vide (utilisé si on veut créer un emprunt sans initialisation immédiate)
    public Emprunt() {
    }

    // Constructeur avec paramètres pour créer un emprunt complet dès l'initialisation
    public Emprunt(int id, int numEtudiant, int idLivre, LocalDate dateEmprunt, LocalDate dateRetour, boolean estRetourne) {
        this.id.set(id);
        this.numEtudiant.set(numEtudiant);
        this.idLivre.set(idLivre);
        this.dateEmprunt.set(dateEmprunt);
        this.dateRetour.set(dateRetour);
        this.estRetourne.set(estRetourne);
    }

    // Getters standards
    public int getId() {
        return id.get();
    }

    public int getNumEtudiant() {
        return numEtudiant.get();
    }

    public int getIdLivre() {
        return idLivre.get();
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt.get();
    }

    public LocalDate getDateRetour() {
        return dateRetour.get();
    }

    public boolean isEstRetourne() {
        return estRetourne.get();
    }

    // Setters standards
    public void setId(int id) {
        this.id.set(id);
    }

    public void setNumEtudiant(int numEtudiant) {
        this.numEtudiant.set(numEtudiant);
    }

    public void setIdLivre(int idLivre) {
        this.idLivre.set(idLivre);
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt.set(dateEmprunt);
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour.set(dateRetour);
    }

    public void setEstRetourne(boolean estRetourne) {
        this.estRetourne.set(estRetourne);
    }

    // Propriétés JavaFX pour la liaison avec TableView
    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty numEtudiantProperty() {
        return numEtudiant;
    }

    public IntegerProperty idLivreProperty() {
        return idLivre;
    }

    public ObjectProperty<LocalDate> dateEmpruntProperty() {
        return dateEmprunt;
    }

    public ObjectProperty<LocalDate> dateRetourProperty() {
        return dateRetour;
    }

    public BooleanProperty estRetourneProperty() {
        return estRetourne;
    }

    // Affichage textuel de l'objet emprunt
    @Override
    public String toString() {
        return "Emprunt{" +
                "id=" + getId() +
                ", numEtudiant=" + getNumEtudiant() +
                ", idLivre=" + getIdLivre() +
                ", dateEmprunt=" + getDateEmprunt() +
                ", dateRetour=" + getDateRetour() +
                ", estRetourne=" + isEstRetourne() +
                '}';
    }
}