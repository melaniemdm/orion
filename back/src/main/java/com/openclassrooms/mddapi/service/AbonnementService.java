package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.AbonnementDTO;
import com.openclassrooms.mddapi.model.Abonnement;
import com.openclassrooms.mddapi.repository.AbonnementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AbonnementService {

    @Autowired
    private AbonnementRepository abonnementRepository;

public List<AbonnementDTO>getAllAbonnement(){
    List<Abonnement> abonnements=(List<Abonnement>) abonnementRepository.findAll();
    List<AbonnementDTO> abonnementDTOS=new ArrayList<>();
    for (Abonnement abonnement : abonnements) {
        abonnementDTOS.add(entityToDto(abonnement));
    }

    return abonnementDTOS;
}

    public List<AbonnementDTO> getAbonnementByUserId(Long idUrl) {
        // 1) Récupère TOUS les abonnements
        List<Abonnement> abonnements = (List<Abonnement>) abonnementRepository.findAll();


        List<AbonnementDTO> resultat = new ArrayList<>();

        for (Abonnement abonnement : abonnements) {
            //  Convertir Long en Integer
            Integer userIdAsInteger = Math.toIntExact(idUrl);
            if (abonnement.getUser_id().equals(userIdAsInteger)) {
                resultat.add(entityToDto(abonnement));
            }
        }

        return resultat;
    }


    public AbonnementDTO createAbonnement(AbonnementDTO dto) {

        Abonnement abonnement = new Abonnement();
        abonnement.setId(dto.getId());
        abonnement.setTheme_id(dto.getTheme_id());
        abonnement.setUser_id(dto.getUser_id());

        Abonnement savedAbonnement = abonnementRepository.save(abonnement);

        AbonnementDTO abonnementDTO = new AbonnementDTO();
        abonnementDTO.setId(savedAbonnement.getId());
        abonnementDTO.setTheme_id(savedAbonnement.getTheme_id());
        abonnementDTO.setUser_id(savedAbonnement.getUser_id());


        return abonnementDTO;
    }

    public Optional<AbonnementDTO> deleteAbonnement(Long id){
        abonnementRepository.deleteById(id);
        return Optional.empty();
    }

    private AbonnementDTO entityToDto(Abonnement abonnement) {

        AbonnementDTO abonnementDTO = new AbonnementDTO();

        abonnementDTO.setId(abonnement.getId());
        abonnementDTO.setTheme_id(abonnement.getTheme_id());
        abonnementDTO.setUser_id(abonnement.getUser_id());

        return abonnementDTO;
    }


    private Abonnement dtoToEntity(AbonnementDTO abonnementDTO) {

        Abonnement abonnement = new Abonnement();

        abonnement.setId(abonnementDTO.getId());
        abonnement.setUser_id(abonnementDTO.getUser_id());
        abonnement.setTheme_id(abonnementDTO.getTheme_id());

        return abonnement;
    }
}
