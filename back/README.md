# Orion Backend

This project is the backend for the MDD application, developed using Java and Spring Boot. It manages the application's core business logic, database interactions, and API endpoints for communication with the frontend

--- 

## Prérequis

- Java 11
- Node.js 16+
- Angular CLI 14+
- MySQL 8+
- Maven 3

## Installation

Clone the project, then install the dependencies and configure the database:

```bash
  git https://github.com/melaniemdm/orion.git
```
Go inside folder:
```bash
  cd back
```

Install the project dependencies:
```bash
  mvn clean install
```

## Installation de MySQL

1. Téléchargez et installez MySQL depuis
   le [ https://dev.mysql.com/downloads/installer/](https://dev.mysql.com/downloads/installer/).
2. Suivez les instructions d'installation et configurez un mot de passe pour l'utilisateur `root`.
3. Une fois l'installation terminée, démarrez le serveur MySQL.

## Configuration de la base de données

1. Connectez-vous à MySQL en utilisant le terminal ou un client comme MySQL Workbench :
```bash
  mysql -u root -p
```
2. Créez une base de données pour l'application :
```sql
CREATE DATABASE oriondb;
```
3. Importez le script SQL de structure et de données initiales depuis le fichier `script.sql` dans le dossier
   `ressources/sql` :
```bash
  mysql -u root -p oriondb < resources/sql/script.sql
```
   
4. Assurez-vous que le serveur MySQL est en cours d'exécution.

## Installer les dépendances et compiler le projet

Pour installer les dépendances et compiler le projet, exécutez :

```bash
  mvn clean install
```

## Start the Development Server

```bash
  mvn spring-boot:run
```

The application will be accessible at http://localhost:8080.

## Project Architecture

- The backend follows a layered architecture to ensure separation of concerns and maintainability:

- controllers: Contains REST controllers that handle API requests and responses.

- services: Contains the core business logic of the application.

- repositories: Contains interfaces for database operations using Spring Data JPA.

- models: Contains entity classes and data transfer objects (DTOs).

- config: Contains configuration files for security, database, and other application settings.

## Implemented Features

- User Management: Handles user registration, authentication, and authorization.

- Data Validation: Validates input data to ensure the integrity of the database and business logic.

- Database Interaction: Uses MySQL for persistent data storage.

## Key Code Sections

- SecurityConfig.java: Configures Spring Security for JWT-based authentication.
- UserController.java: Handles user-related API endpoints such as registration and login.


## License
This project is licensed under the MIT License - see the LICENSE file for details.