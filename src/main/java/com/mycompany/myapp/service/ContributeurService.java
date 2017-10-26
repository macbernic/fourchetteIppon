package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ContributeurDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Contributeur.
 */
public interface ContributeurService {

    /**
     * Save a contributeur.
     *
     * @param contributeurDTO the entity to save
     * @return the persisted entity
     */
    ContributeurDTO save(ContributeurDTO contributeurDTO);

    /**
     *  Get all the contributeurs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ContributeurDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" contributeur.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ContributeurDTO findOne(Long id);

    /**
     *  Delete the "id" contributeur.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
