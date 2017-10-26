package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ContributeurService;
import com.mycompany.myapp.domain.Contributeur;
import com.mycompany.myapp.repository.ContributeurRepository;
import com.mycompany.myapp.service.dto.ContributeurDTO;
import com.mycompany.myapp.service.mapper.ContributeurMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Contributeur.
 */
@Service
@Transactional
public class ContributeurServiceImpl implements ContributeurService{

    private final Logger log = LoggerFactory.getLogger(ContributeurServiceImpl.class);

    private final ContributeurRepository contributeurRepository;

    private final ContributeurMapper contributeurMapper;

    public ContributeurServiceImpl(ContributeurRepository contributeurRepository, ContributeurMapper contributeurMapper) {
        this.contributeurRepository = contributeurRepository;
        this.contributeurMapper = contributeurMapper;
    }

    /**
     * Save a contributeur.
     *
     * @param contributeurDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContributeurDTO save(ContributeurDTO contributeurDTO) {
        log.debug("Request to save Contributeur : {}", contributeurDTO);
        Contributeur contributeur = contributeurMapper.toEntity(contributeurDTO);
        contributeur = contributeurRepository.save(contributeur);
        return contributeurMapper.toDto(contributeur);
    }

    /**
     *  Get all the contributeurs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ContributeurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contributeurs");
        return contributeurRepository.findAll(pageable)
            .map(contributeurMapper::toDto);
    }

    /**
     *  Get one contributeur by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ContributeurDTO findOne(Long id) {
        log.debug("Request to get Contributeur : {}", id);
        Contributeur contributeur = contributeurRepository.findOne(id);
        return contributeurMapper.toDto(contributeur);
    }

    /**
     *  Delete the  contributeur by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contributeur : {}", id);
        contributeurRepository.delete(id);
    }
}
