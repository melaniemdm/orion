package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.AbonnementDTO;
import com.openclassrooms.mddapi.model.Abonnement;
import com.openclassrooms.mddapi.repository.AbonnementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AbonnementService {

    @Autowired
    private AbonnementRepository abonnementRepository;

    /**
     * Retrieves the list of all subscriptions.
     *
     * @return A list of {@link AbonnementDTO} containing all available subscriptions.
     */
    public List<AbonnementDTO> getAllAbonnement() {
        List<Abonnement> abonnements = (List<Abonnement>) abonnementRepository.findAll();
        List<AbonnementDTO> abonnementDTOS = new ArrayList<>();
        for (Abonnement abonnement : abonnements) {
            abonnementDTOS.add(entityToDto(abonnement));
        }

        return abonnementDTOS;
    }

    /**
     * Retrieves the list of subscriptions for a specific user.
     *
     * @param idUrl The identifier of the user whose subscriptions are to be retrieved.
     * @return A list of {@link AbonnementDTO} corresponding to the given user's subscriptions.
     */
    public List<AbonnementDTO> getAbonnementByUserId(Long idUrl) {
        // Fetch all subscriptions from the database
        List<Abonnement> abonnements = (List<Abonnement>) abonnementRepository.findAll();

        // List to store filtered subscriptions
        List<AbonnementDTO> resultat = new ArrayList<>();

        // Convert the Long user ID to Integer
        Integer userIdAsInteger = Math.toIntExact(idUrl);
        // Filter subscriptions that belong to the given user ID
        for (Abonnement abonnement : abonnements) {
            if (abonnement.getUser_id().equals(userIdAsInteger)) {
                // Convert entity to DTO and add it to the result list
                resultat.add(entityToDto(abonnement));
            }
        }
        // Return the filtered list of subscriptions
        return resultat;
    }

    /**
     * Creates a new subscription by saving the information in the database.
     *
     * @param dto The {@link AbonnementDTO} object containing the subscription details to be created.
     * @return A {@link AbonnementDTO} object representing the newly created subscription with its generated ID.
     */
    public AbonnementDTO createAbonnement(AbonnementDTO dto) {

        Abonnement abonnement = new Abonnement();
        abonnement.setId(dto.getId());
        abonnement.setTheme_id(dto.getTheme_id());
        abonnement.setUser_id(dto.getUser_id());

        // Save the subscription in the database
        Abonnement savedAbonnement = abonnementRepository.save(abonnement);

        // Convert the saved entity into a DTO and return it
        AbonnementDTO abonnementDTO = new AbonnementDTO();
        abonnementDTO.setId(savedAbonnement.getId());
        abonnementDTO.setTheme_id(savedAbonnement.getTheme_id());
        abonnementDTO.setUser_id(savedAbonnement.getUser_id());

        return abonnementDTO;
    }

    /**
     * Deletes a subscription from the database based on its identifier.
     *
     * @param id The identifier of the subscription to be deleted.
     */
    public void deleteAbonnement(Long id) {
        abonnementRepository.deleteById(id);
    }

    /**
     * Converts an {@link Abonnement} entity into a {@link AbonnementDTO} object.
     *
     * @param abonnement The {@link Abonnement} entity to convert.
     * @return A {@link AbonnementDTO} object containing the same information as the entity.
     */
    private AbonnementDTO entityToDto(Abonnement abonnement) {
        // Create a new DTO instance
        AbonnementDTO abonnementDTO = new AbonnementDTO();
        // Copy values from entity to DTO
        abonnementDTO.setId(abonnement.getId());
        abonnementDTO.setTheme_id(abonnement.getTheme_id());
        abonnementDTO.setUser_id(abonnement.getUser_id());
        // Return the converted DTO
        return abonnementDTO;
    }

    /**
     * Converts a {@link AbonnementDTO} DTO object into an {@link Abonnement} entity.
     *
     * @param abonnementDTO The {@link AbonnementDTO} object to convert.
     * @return An {@link Abonnement} entity containing the same information as the DTO.
     */
    private Abonnement dtoToEntity(AbonnementDTO abonnementDTO) {
        // Create a new Abonnement entity instance
        Abonnement abonnement = new Abonnement();
        // Copy values from DTO to entity
        abonnement.setId(abonnementDTO.getId());
        abonnement.setUser_id(abonnementDTO.getUser_id());
        abonnement.setTheme_id(abonnementDTO.getTheme_id());
        // Return the converted entity
        return abonnement;
    }
}
