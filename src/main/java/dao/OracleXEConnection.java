package dao;

import java.sql.Connection;//represente une connexion à une BDD
import java.sql.DriverManager;// permet de se connecter à la base via un driver
import java.sql.SQLException; //en case de problème SQL
import java.util.TimeZone;//utilisé ici pour fixer un fuseau horaire par défaut

public class OracleXEConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE"; //l’adresse JDBC pour accéder à Oracle XE
    //identifiants pour se connecter à la base
    private static final String USER = "system";
    private static final String PASSWORD = "123456789";

    static {
        // Set the default timezone
        // on force le fuseau horaire en UTC pour éviter des problèmes de date/heure
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        // Charger le driver JDBC
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("Driver JDBC chargé avec succès !");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver JDBC non trouvé !"); //affichage de l'erreur si il est manquant
            e.printStackTrace();
        }
    }

    //Test du fonctionnement de la connexion
    public static void main(String[] args) {
        if (testConnection()) {
            System.out.println("✅ Test de connexion réussi !");
        } else {
            System.out.println("❌ Test de connexion échoué !");
        }
    }

    //Crée et retourne une connexion Oracle grâce au DriverManager
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    //Méthode pour tester la connexion
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) { //fermer automatiquement la connexion
            System.err.println("❌ Erreur de connexion !");
            e.printStackTrace();
            return false;
        }
    }

    //Méthode pour confirmer ou valider une transaction
    public static void commit() {
        try (Connection connection = getConnection()) {
            if (connection != null && !connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du commit");
            e.printStackTrace();
        }
    }

    // 4. Méthode pour annuler une transaction (modifications en cours)
    public static void rollback() {
        try (Connection connection = getConnection()) {
            if (connection != null && !connection.getAutoCommit()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du rollback");
            e.printStackTrace();
        }
    }

    //Les 2 dernieres méthodes sont utiles au cas où autoCommit est désactivé

    //Méthode pour activer/désactiver l'auto-commit
    public static void setAutoCommit(boolean autoCommit) {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                connection.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du setAutoCommit");
            e.printStackTrace();
        }
    }

    // vérifier si l'auto-commit est activé
    public static boolean isAutoCommit() {
        try (Connection connection = getConnection()) {
            return connection != null && connection.getAutoCommit();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'auto-commit");
            e.printStackTrace();
            return true;
        }
    }

    //Méthode pour fermer une connexion
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.getAutoCommit()) {
                    connection.commit();//Effectue un commit() automatique si nécessaire avant la fermeture
                }
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion");
                e.printStackTrace();
            }
        }
    }

    //Méthode pour obtenir des métadonnées sur la base de données
    public static String getDatabaseMetadata() {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                //Récupère des informations générales (nom, version, driver) sur la BDD via getMetaData()
                return "Database: " + connection.getMetaData().getDatabaseProductName() + "\n" +
                        "Version: " + connection.getMetaData().getDatabaseProductVersion() + "\n" +
                        "Driver: " + connection.getMetaData().getDriverName() + " " +
                        connection.getMetaData().getDriverVersion();



            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des métadonnées");
            e.printStackTrace();
        }
        return "Impossible de récupérer les métadonnées de la base de données";
    }
}