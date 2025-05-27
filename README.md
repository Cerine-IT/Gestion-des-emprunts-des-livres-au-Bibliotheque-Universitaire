# Systeme-de-Gestion-de-Bibliotheque-Universitaire
Application JavaFX complète pour la gestion d'une bibliothèque universitaire. Elle permet de gérer les étudiants, les livres et les emprunts avec une interface utilisateur intuitive et des fonctionnalités CRUD complètes.

🌟 Fonctionnalités Principales

📚 Module Livres
- CRUD complet : Ajout, consultation, modification et suppression de livres
- Recherche avancée : Par titre, auteur ou catégorie
- Gestion des stocks : Suivi des quantités disponibles
- Interface riche : Tableau interactif avec sélection et édition directe

🎓 Module Étudiants
- Gestion des profils : Création et maintenance des dossiers étudiants
- Système de recherche : Par nom, prénom ou numéro d'étudiant
- Interface optimisée : ComboBox intelligente avec affichage formaté
- Validation des données : Contrôle des formats de saisie

🔄 Module Emprunts
- Gestion des prêts : Enregistrement des emprunts avec dates
- Suivi des retours : Marquage des livres retournés
- Visualisation : Liste des emprunts en cours avec statut
- Contrôle des dates : Validation de la cohérence des périodes

📦 Structure des Packages
``
src/
├── main/
│   ├── java/
│   │   ├── controller/       # Contrôleurs FXML
│   │   │   ├── DashboardController.java
│   │   │   ├── EmpruntController.java
│   │   │   ├── EtudiantController.java
│   │   │   ├── LivreController.java
│   │   │   └── MainController.java
│   │   ├── dao/             # Couche d'accès aux données
│   │   │   ├── EmpruntDao.java
│   │   │   ├── EtudiantDAO.java
│   │   │   ├── LivreDAO.java
│   │   │   └── OracleXEConnection.java
│   │   ├── model/           # Modèles de données
│   │   │   ├── Emprunt.java
│   │   │   ├── Etudiant.java
│   │   │   └── Livre.java
│   │   └── Main.java        # Point d'entrée
│   └── resources/
│       ├── emprunt/
│       │   ├── Emprunt.fxml
│       ├── etudiant/
│       │   ├── Etudiant.fxml
│       ├── livre/
│       │   ├── Livre.fxml
│       ├── Main.fxml
│       └── images/          # Ressources graphiques
``

⚙ Prérequis & Installation

📋 Prérequis Système
- Java JDK 11+
- Oracle Database XE 18c/21c
- JavaFX SDK (inclus avec JDK 11+)
- Maven 3.6+

  Configuration Initiale
1. Base de Données:
```
-- 1. Suppression sécurisée (ignore les erreurs si objets inexistants)
DROP TABLE Emprunt CASCADE CONSTRAINTS
/
DROP TABLE Livre CASCADE CONSTRAINTS
/
DROP TABLE Etudiant CASCADE CONSTRAINTS
/
DROP SEQUENCE emprunt_seq
/

-- 2. Création des tables
CREATE TABLE Etudiant (
    NumEtudiant INT PRIMARY KEY,
    Nom VARCHAR2(50) NOT NULL,
    Prenom VARCHAR2(50) NOT NULL,
    Email VARCHAR2(100) UNIQUE NOT NULL,
    Telephone VARCHAR2(15) UNIQUE
)
/

CREATE TABLE Livre (
    Code_Livre INT PRIMARY KEY,
    Titre VARCHAR2(100) NOT NULL,
    Auteur VARCHAR2(100) NOT NULL,
    Categorie VARCHAR2(50),
    Annee INT,
    Quantite_ INT NOT NULL
)
/

CREATE TABLE Emprunt (
    id INT PRIMARY KEY,
    NumEtudiant INT,
    id INT,
    dateEmprunt DATE NOT NULL,
    dateRetour DATE,
    estRetourne NUMBER(1) CHECK (estRetourne IN (0, 1)),
    FOREIGN KEY (NumEtudiant) REFERENCES Etudiant(NumEtudiant) ON DELETE CASCADE,
    FOREIGN KEY (id) REFERENCES Livre(Code_Livre) ON DELETE CASCADE
)
/
```
2.Configuration Application:
- Modifier les identifiants dans OracleXEConnection.java
- Vérifier le chemin JDBC dans la même classe


  * Interfaces Utilisateur
  
🏠 Écran Principal (Main.fxml)
- Fond d'écran thématique
- Boutons d'accès aux modules principaux
- Style élégant avec effets visuels

📚 Interface Livres
- Formulaire de saisie complète
- Tableau réactif
- Boutons d'action CRUD
- Barre de recherche intégrée

🎓 Interface Étudiants
- Sélection via ComboBox intelligente
- Formulaire détaillé
- Gestion complète des profils
- Boutons contextuels

📄 Licence
Ce projet est sous licence MIT. Libre d'utilisation pour tout usage éducatif ou commercial.
