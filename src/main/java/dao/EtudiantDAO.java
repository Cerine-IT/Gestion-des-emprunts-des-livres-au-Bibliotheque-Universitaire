package dao;

import model.Etudiant;// Importation du modèle Etudiant
// Importation des classes Java pour interagir avec la BDD
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// Logger pour gérer les messages d’erreur et les logs
import java.util.logging.Level;
import java.util.logging.Logger;

//Classe DAO pour gérer les operations sur la table Etudiant
public class EtudiantDAO {
    //Initialisation du logger pour la classe
    private static final Logger logger = Logger.getLogger(EtudiantDAO.class.getName());

    //AJouter un etudiant à la BDD
    public boolean ajouterEtudiant(Etudiant etudiant) {
        String sql = "INSERT INTO ETUDIANT (NUM_ETUDIANT, NOM, PRENOM, EMAIL, TELEPHONE) VALUES (?, ?, ?, ?, ?)";

        try (
                // Connexion à la base Oracle
                Connection conn = OracleXEConnection.getConnection();
                // Préparation de la requête avec paramètres
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            //Remplissage des 5 paramètres avec les données de l'objet étudiant
            stmt.setInt(1, etudiant.getNumEtudiant());
            stmt.setString(2, etudiant.getNom());
            stmt.setString(3, etudiant.getPrenom());
            stmt.setString(4, etudiant.getEmail());
            stmt.setString(5, etudiant.getTelephone());

            int rowsAffected = stmt.executeUpdate(); //execution de INSERT
            OracleXEConnection.commit();//Validation de la transaction
            return rowsAffected > 0;// Retourne vrai si l'étudiant a bien été inséré

        } catch (SQLException e) {
            // Gestion des erreurs SQL : log + rollback de la transaction
            logger.log(Level.SEVERE, "Erreur lors de l'ajout de l'étudiant", e);
            OracleXEConnection.rollback();
            return false;
        }
    }
    //Recupérer tous les étudiants
    public List<Etudiant> getAllEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        //requête SQL pour tout récupérer en ordre alphabétique
        String sql = "SELECT * FROM ETUDIANT ORDER BY NOM, PRENOM";

        try (Connection conn = OracleXEConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Parcours du résultat ligne par ligne
            while (rs.next()) {
                // Création d'un objet Etudiant à partir des colonnes
                Etudiant etudiant = new Etudiant(
                        rs.getInt("NUM_ETUDIANT"),
                        rs.getString("NOM"),
                        rs.getString("PRENOM"),
                        rs.getString("EMAIL"),
                        rs.getString("TELEPHONE")
                );
                etudiants.add(etudiant);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des étudiants", e);
        }
        return etudiants;
    }
    //Modifier un etudiant
    public boolean modifierEtudiant(Etudiant etudiant) {
        // Mise à jour des infos sauf la cle primaire
        String sql = "UPDATE ETUDIANT SET NOM = ?, PRENOM = ?, EMAIL = ?, TELEPHONE = ? WHERE NUM_ETUDIANT = ?";

        try (Connection conn = OracleXEConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, etudiant.getNom());
            stmt.setString(2, etudiant.getPrenom());
            stmt.setString(3, etudiant.getEmail());
            stmt.setString(4, etudiant.getTelephone());
            stmt.setInt(5, etudiant.getNumEtudiant());

            int rowsAffected = stmt.executeUpdate();
            OracleXEConnection.commit();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la modification de l'étudiant", e);
            OracleXEConnection.rollback();
            return false;
        }
    }
    //Supprimer un étudiant
    public boolean supprimerEtudiant(int numEtudiant) {
        String sql = "DELETE FROM ETUDIANT WHERE NUM_ETUDIANT = ?";

        try (Connection conn = OracleXEConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numEtudiant);
            int rowsAffected = stmt.executeUpdate();
            OracleXEConnection.commit();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la suppression de l'étudiant", e);
            OracleXEConnection.rollback();
            return false;
        }
    }
    //Rechercher par nom cle
    public List<Etudiant> rechercherEtudiants(String keyword) {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM ETUDIANT WHERE LOWER(NOM) LIKE ? OR LOWER(PRENOM) LIKE ? OR LOWER(EMAIL) LIKE ? ORDER BY NOM, PRENOM";

        try (Connection conn = OracleXEConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            //Creation du motif de recherche
            String searchPattern = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Etudiant etudiant = new Etudiant(
                            rs.getInt("NUM_ETUDIANT"),
                            rs.getString("NOM"),
                            rs.getString("PRENOM"),
                            rs.getString("EMAIL"),
                            rs.getString("TELEPHONE")
                    );
                    etudiants.add(etudiant);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la recherche d'étudiants", e);
        }
        return etudiants;
    }
    //recherhcer par numEtudiant
    public Etudiant getEtudiantByNum(int numEtudiant) {
        String sql = "SELECT * FROM ETUDIANT WHERE NUM_ETUDIANT = ?";

        try (Connection conn = OracleXEConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numEtudiant);
            ResultSet rs = stmt.executeQuery();

            // Si un résultat est trouvé, on retourne l’objet Etudiant
            if (rs.next()) {
                return new Etudiant(
                        rs.getInt("NUM_ETUDIANT"),
                        rs.getString("NOM"),
                        rs.getString("PRENOM"),
                        rs.getString("EMAIL"),
                        rs.getString("TELEPHONE")
                );
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération de l'étudiant par numéro", e);
        }

        return null;
    }
}