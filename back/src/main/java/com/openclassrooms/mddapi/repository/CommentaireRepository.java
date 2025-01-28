package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentaireRepository extends JpaRepository <Commentaire, Long> {
}
