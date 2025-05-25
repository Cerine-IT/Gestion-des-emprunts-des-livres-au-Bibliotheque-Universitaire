package dao;

import model.Emprunt;// Importation du modèle Emprunt
// Importation des classes Java pour interagir avec la BDD
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
// Logger pour gérer les messages d’erreur et les logs
import java.util.logging.Level;
import java.util.logging.Logger;

//Classe DAO pour gérer les operations sur la table Emprunt
public class EmpruntDao {
    private static final Logger logger = Logger.getLogger(EmpruntDao.class.getName());

    // Ajouter un nouvel emprunt
    public boolean ajouterEmprunt(Emprunt emprunt) throws SQLException {
        String sql = "INSERT INTO EMPRUNT (ID, NUM_ETUDIANT, ID_LIVRE, DATE_EMPRUNT, DATE_RETOUR, EST_RETOURNE) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = OracleXEConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Récupérer un ID unique depuis la séquence Oracle SEQ_EMPRUNT_ID
            int generatedId = 0;
            try (Statement seqStmt = conn.createStatement();
                 ResultSet rs = seqStmt.executeQuery("SELECT SEQ_EMPRUNT_ID.NEXTVAL FROM DUAL")) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }

            // Ajouter les paramètres à la requête
            stmt.setInt(1, generatedId);
            stmt.setInt(2, emprunt.getNumEtudiant());
            stmt.setInt(3, emprunt.getIdLivre());
            stmt.setDate(4, Date.valueOf(emprunt.getDateEmprunt()));
            stmt.setDate(5, Date.valueOf(emprunt.getDateRetour()));
            stmt.setInt(6, emprunt.isEstRetourne() ? 1 : 0); // Convertit boolean -> int

            //executer l'insertion
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return false;

            //Mettre à jour l'objet Java avec l'ID géneré
            emprunt.setId(generatedId);

            //Valider linsertion
            OracleXEConnection.commit();
            return true;
        } catch (SQLException e) {
            OracleXEConnection.rollback(); // En cas d'erreur, annuler la transaction
            logger.log(Level.SEVERE, "Erreur lors de l'ajout de l'emprunt", e);
            throw e;
        }
    }


    // Enregistrer un retour d'un livre avec une date donnée
    public boolean enregistrerRetour(int idEmprunt, LocalDate dateRetour) throws SQLException {
        String sql = "UPDATE EMPRUNT SET EST_RETOURNE = 1, DATE_RETOUR = ? WHERE ID = ?";

        try (Connection conn = OracleXEConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dateRetour));// Date réelle de retour
            stmt.setInt(2, idEmprunt); // ID de l'emprunt concerné

            int rows = stmt.executeUpdate(); // mise a jour
            OracleXEConnection.commit(); //Commit si réussie
            return rows > 0;
        } catch (SQLException e) {
            OracleXEConnection.rollback();// Rollback en cas derreur
            logger.log(Level.SEVERE, "Erreur lors de l'enregistrement du retour", e);
            throw e;
        }
    }

    // Enregistrer un retour (date actuelle; celle d'aujoud'hui)
    public boolean enregistrerRetour(int idEmprunt) throws SQLException {
        return enregistrerRetour(idEmprunt, LocalDate.now());
    }

    // Récupérer tous les emprunts (retournés ou pas)
    public List<Emprunt> getTousLesEmprunts() throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM EMPRUNT ORDER BY DATE_EMPRUNT DESC";

        try (Connection conn = OracleXEConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                emprunts.add(creerEmpruntDepuisResultSet(rs)); // Creation à partir du ResultSet
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des emprunts", e);
            throw e;
        }

        return emprunts;
    }

    // Récupérer que les emprunts non retournés; encore actifs
    public List<Emprunt> getEmpruntsActifs() throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM EMPRUNT WHERE EST_RETOURNE = 0 ORDER BY DATE_EMPRUNT DESC";

        try (Connection conn = OracleXEConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                emprunts.add(creerEmpruntDepuisResultSet(rs)); // Ajouter chaque emprunt non retourné
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des emprunts actifs", e);
            throw e;
        }

        return emprunts;
    }

    // Méthode utilitaire pour créer un objet Emprunt à partir d'un ResultSet
    private Emprunt creerEmpruntDepuisResultSet(ResultSet rs) throws SQLException {
        Emprunt emprunt = new Emprunt();
        emprunt.setId(rs.getInt("ID"));
        emprunt.setNumEtudiant(rs.getInt("NUM_ETUDIANT"));
        emprunt.setIdLivre(rs.getInt("ID_LIVRE"));
        emprunt.setDateEmprunt(rs.getDate("DATE_EMPRUNT").toLocalDate());

        Date dateRetourSql = rs.getDate("DATE_RETOUR");
        emprunt.setDateRetour(dateRetourSql != null ? dateRetourSql.toLocalDate() : null);

        emprunt.setEstRetourne(rs.getInt("EST_RETOURNE") == 1); //convertit int -> boolean
        return emprunt;
    }
}