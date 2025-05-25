package dao;

import model.Livre; //Import du modele Livre
import java.sql.*; // Import de tout ce qui touche à la base de données
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level; // Pour gérer les logs
import java.util.logging.Logger;

//La classe LivreDAO s’occupe uniquement des opérations liées à la base de données
// (principe du design pattern DAO)

public class LivreDAO {
    // Logger pour enregistrer les erreurs et informations dans les logs
    private static final Logger logger = Logger.getLogger(LivreDAO.class.getName());

    //Ajoute un nouveau livre à la BDD
    public boolean ajouterLivre(Livre livre) {
        // Requête SQL d'insertion avec des "?" comme placeholders pour les valeurs
        String sql = "INSERT INTO LIVRE (CODE_LIVRE, TITRE, AUTEUR, CATEGORIE, ANNEE_PUBLICATION, QUANTITE_DISPONIBLE) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        //(connexion à la BDD + Préparation de la requête pour éviter les injections SQL)
        try (Connection conn = OracleXEConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Remplissage des paramètres de la requête avec les valeurs du livre
            stmt.setInt(1, livre.getId());
            stmt.setString(2, livre.getTitre());
            stmt.setString(3, livre.getAuteur());
            stmt.setString(4, livre.getCategorie());
            stmt.setInt(5, livre.getAnneePublication());
            stmt.setInt(6, livre.getQuantite());

            int rowsInserted = stmt.executeUpdate(); //Exécution de la requete : retourne le nombre de lignes insérées
            return rowsInserted > 0; //true si au moins une ligne ajoutée
        } catch (SQLException e) {
            // En cas d'erreur SQL, on l'affiche dans les logs
            logger.log(Level.SEVERE, "Erreur lors de l'ajout du livre", e);
        }
        return false;// En cas d’erreur
    }
    //Récupère un livre selon son identifiant
    public Livre getLivreById(int idLivre) {
        String sql = "SELECT * FROM LIVRE WHERE CODE_LIVRE = ?";

        try (Connection conn = OracleXEConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLivre);// On remplit le placeholder avec l'identifiant
            ResultSet rs = stmt.executeQuery(); //On exécute la requete

            if (rs.next()) {
                // Si un livre est trouvé, on crée un objet Livre à partir des données
                return new Livre(
                        rs.getInt("CODE_LIVRE"),
                        rs.getString("TITRE"),
                        rs.getString("AUTEUR"),
                        rs.getString("CATEGORIE"),
                        rs.getInt("ANNEE_PUBLICATION"),
                        rs.getInt("QUANTITE_DISPONIBLE")
                );
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération du livre par ID", e);
        }
        return null;// Aucun livre trouvé
    }
    //Récupère tous les livres de la BDD
    public List<Livre> getTousLesLivres() {
        List<Livre> livres = new ArrayList<>(); //Liste pour stocker les livres
        String sql = "SELECT * FROM LIVRE"; // Requête pour récupérer tous les livres

        try (Connection conn = OracleXEConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                //Parcours de toutes les lignes retournees
                Livre livre = new Livre(
                        rs.getInt("CODE_LIVRE"),
                        rs.getString("TITRE"),
                        rs.getString("AUTEUR"),
                        rs.getString("CATEGORIE"),
                        rs.getInt("ANNEE_PUBLICATION"),
                        rs.getInt("QUANTITE_DISPONIBLE")
                );
                livres.add(livre);// Ajout du livre à la liste
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des livres", e);
        }
        return livres;// Retourne la liste de livres
    }
    //Modifie un livre existant dans la base
    public boolean modifierLivre(Livre livre) {
        // Requête SQL de mise à jour
        String sql = "UPDATE LIVRE SET TITRE = ?, AUTEUR = ?, CATEGORIE = ?, ANNEE_PUBLICATION = ?, QUANTITE_DISPONIBLE = ? WHERE CODE_LIVRE = ?";

        try (Connection conn = OracleXEConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Paramètres de la mise à jour
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getCategorie());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setInt(5, livre.getQuantite());
            stmt.setInt(6, livre.getId());

            int rowsUpdated = stmt.executeUpdate(); // Nombre de lignes modifiées
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la modification du livre", e);
        }
        return false;
    }
    //Supprimer un livre existant dans la base selon son CODE_LIVRE
    public boolean supprimerLivre(int id) {
        String sql = "DELETE FROM LIVRE WHERE CODE_LIVRE = ?";

        try (Connection conn = OracleXEConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); // L’identifiant du livre à supprimer
            int rowsDeleted = stmt.executeUpdate();// Nombre de lignes supprimées
            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la suppression du livre", e);
        }
        return false;
    }
    //Recherche des livres selon un mot-clé dans le titre, l’auteur ou la catégorie
    public List<Livre> rechercherLivres(String motCle) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM LIVRE WHERE LOWER(TITRE) LIKE ? OR LOWER(AUTEUR) LIKE ? OR LOWER(CATEGORIE) LIKE ?";

        try (Connection conn = OracleXEConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Transformation du mot clé en minuscules et ajout de % pour la recherche
            String query = "%" + motCle.toLowerCase() + "%"; //En SQL, % signifie “n’importe quoi avant ou après”
            stmt.setString(1, query);
            stmt.setString(2, query);
            stmt.setString(3, query);// remplace le 3 ième ? dans la requête SQL par query

            ResultSet rs = stmt.executeQuery();//Execution
            while (rs.next()) {
                Livre livre = new Livre(
                        rs.getInt("CODE_LIVRE"),
                        rs.getString("TITRE"),
                        rs.getString("AUTEUR"),
                        rs.getString("CATEGORIE"),
                        rs.getInt("ANNEE_PUBLICATION"),
                        rs.getInt("QUANTITE_DISPONIBLE")
                );
                livres.add(livre); //ajout à la liste
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la recherche de livres", e);
        }
        return livres;
    }
}