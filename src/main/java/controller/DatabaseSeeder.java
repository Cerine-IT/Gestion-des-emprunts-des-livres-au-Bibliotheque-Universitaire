package controller;

// Importation des classes nécessaires pour la connexion à la BDD Oracle
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSeeder {

    // Méthode statique pour insérer des données exemples dans la BDD
    public static void insertSampleData() {
        // Chaîne de connexion JDBC vers la base Oracle locale
        String url = "jdbc:oracle:thin:@localhost:1521:XE"; // "XE" est souvent utilisé comme nom par défaut de la base
        String user = "system";      // Nom d'utilisateur Oracle
        String password = "oracle"; // Mot de passe de l'utilisateur

        // Bloc try-with-resources qui gère automatiquement la fermeture de la connexion
        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            // On désactive l’auto-commit pour pouvoir regrouper les insertions en une seule transaction
            conn.setAutoCommit(false);

            // ====== Insertion des étudiants dans la table "etudiants" ======

            // Requête SQL paramétrée avec des "?" pour éviter les injections SQL
            String sqlEtudiant = "INSERT INTO etudiants (id_etudiant, nom, prenom, email, niveau) VALUES (?, ?, ?, ?, ?)";

            // Préparation de la requête avec les paramètres à insérer
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEtudiant)) {
                // Premier étudiant
                pstmt.setInt(1, 1); // id_etudiant
                pstmt.setString(2, "Dupont"); // nom
                pstmt.setString(3, "Jean");   // prénom
                pstmt.setString(4, "jean.dupont@mail.com"); // email
                pstmt.setString(5, "Licence 3");            // niveau d'étude
                pstmt.executeUpdate(); // Exécution de l'insertion

                // Deuxieme étudiant
                pstmt.setInt(1, 2);
                pstmt.setString(2, "Martin");
                pstmt.setString(3, "Claire");
                pstmt.setString(4, "claire.martin@mail.com");
                pstmt.setString(5, "Master 1");
                pstmt.executeUpdate();

                // Troisième étudiant
                pstmt.setInt(1, 3);
                pstmt.setString(2, "Durand");
                pstmt.setString(3, "Lucas");
                pstmt.setString(4, "lucas.durand@mail.com");
                pstmt.setString(5, "Licence 2");
                pstmt.executeUpdate();
            }

            // ====== Insertion des livres dans la table "livres" ======

            // Requête SQL pour insérer des livres
            String sqlLivre = "INSERT INTO livres (id_livre, titre, auteur, annee_publication, disponible) VALUES (?, ?, ?, ?, ?)";

            // Préparation de la requête
            try (PreparedStatement pstmt = conn.prepareStatement(sqlLivre)) {
                // Premier livre
                pstmt.setInt(1, 101);                         // id_livre
                pstmt.setString(2, "Introduction à Java");   // titre
                pstmt.setString(3, "Michel Morel");          // auteur
                pstmt.setInt(4, 2019);                        // année de publication
                pstmt.setString(5, "O");                      // disponibilité ("O" pour Oui)
                pstmt.executeUpdate();

                // Deuxième livre
                pstmt.setInt(1, 102);
                pstmt.setString(2, "Oracle pour les nuls");
                pstmt.setString(3, "Sophie Laurent");
                pstmt.setInt(4, 2015);
                pstmt.setString(5, "O");
                pstmt.executeUpdate();

                // Troisième livre
                pstmt.setInt(1, 103);
                pstmt.setString(2, "Structures de données");
                pstmt.setString(3, "Alain Pascal");
                pstmt.setInt(4, 2021);
                pstmt.setString(5, "N"); // "N" pour Non disponible
                pstmt.executeUpdate();
            }

            // Validation de toutes les insertions faites jusque-là (commit de la transaction)
            conn.commit();

            // Message pour indiquer que tout s'est bien passé
            System.out.println("Données insérées avec succès !");
        } catch (SQLException e) {
            // Affiche les erreurs SQL s'il y en a (comme problème de connexion ou de requête)
            e.printStackTrace();
        }
    }
}
