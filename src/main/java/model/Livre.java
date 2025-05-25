package model;
// Importation des classes nécessaires pour créer des propriétés JavaFX
import javafx.beans.property.*;

/*Ce fichier définit la classe Livre, qui représente un livre dans la bibliothèque
Chaque objet Livre contient des propriétés (id, titre, auteur..)
Ces propriétés sont utilisées pour la liaison de données (data binding) avec l'interface JavaFX
*/

public class Livre {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty titre = new SimpleStringProperty();
    private final StringProperty auteur = new SimpleStringProperty();
    private final StringProperty categorie = new SimpleStringProperty();
    private final IntegerProperty anneePublication = new SimpleIntegerProperty();
    private final IntegerProperty quantite = new SimpleIntegerProperty();

    //Constructeur = initialisation lors de la création
    public Livre(int id, String titre, String auteur, String categorie, int anneePublication, int quantite) {
        setId(id);
        setTitre(titre);
        setAuteur(auteur);
        setCategorie(categorie);
        setAnneePublication(anneePublication);
        setQuantite(quantite);
    }

    // Property getters; facilite leur utilisation dans JavaFX
    public IntegerProperty idProperty() { return id; }
    public StringProperty titreProperty() { return titre; }
    public StringProperty auteurProperty() { return auteur; }
    public StringProperty categorieProperty() { return categorie; }
    public IntegerProperty anneePublicationProperty() { return anneePublication; }
    public IntegerProperty quantiteProperty() { return quantite; }

    // Standard getters
    public int getId() { return id.get(); }
    public String getTitre() { return titre.get(); }
    public String getAuteur() { return auteur.get(); }
    public String getCategorie() { return categorie.get(); }
    public int getAnneePublication() { return anneePublication.get(); }
    public int getQuantite() { return quantite.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setTitre(String titre) { this.titre.set(titre); }
    public void setAuteur(String auteur) { this.auteur.set(auteur); }
    public void setCategorie(String categorie) { this.categorie.set(categorie); }
    public void setAnneePublication(int anneePublication) { this.anneePublication.set(anneePublication); }
    public void setQuantite(int quantite) { this.quantite.set(quantite); }

    @Override
    public String toString() {
        return getTitre() + " (" + getAuteur() + ")";
    }
}