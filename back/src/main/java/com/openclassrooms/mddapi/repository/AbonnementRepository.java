package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
}
