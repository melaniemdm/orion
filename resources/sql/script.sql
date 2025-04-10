CREATE TABLE `USERS` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_name` VARCHAR(40),
  `admin` BOOLEAN NOT NULL DEFAULT false,
  `email` VARCHAR(255),
  `password` VARCHAR(255)
);

CREATE TABLE `ARTICLES` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `title` VARCHAR(50),
  `description` VARCHAR(2000),
  `auteur_id` int,
  `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `theme_id` int
);

CREATE TABLE `COMMENTAIRES` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `auteur_id` int,
  `article_id` int,
  `commentary` VARCHAR(2000)
);

CREATE TABLE `ABONNEMENT_THEME` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT,
  `theme_id` INT
);

CREATE TABLE `THEME` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name_theme` VARCHAR(20),
  `description` VARCHAR(2000)
);

ALTER TABLE `ARTICLES` ADD FOREIGN KEY (`auteur_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `ARTICLES` ADD FOREIGN KEY (`theme_id`) REFERENCES `THEME` (`id`);
ALTER TABLE `ABONNEMENT_THEME` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `ABONNEMENT_THEME` ADD FOREIGN KEY (`theme_id`) REFERENCES `THEME` (`id`);
ALTER TABLE `COMMENTAIRES` ADD FOREIGN KEY (`auteur_id`) REFERENCES `USERS` (`id`);
ALTER TABLE `COMMENTAIRES` ADD FOREIGN KEY (`article_id`) REFERENCES `ARTICLES` (`id`);

INSERT INTO USERS (user_name, admin, email, password)
VALUES ('Admin', true, 'mdd@orion.com', 'Admin!12');
