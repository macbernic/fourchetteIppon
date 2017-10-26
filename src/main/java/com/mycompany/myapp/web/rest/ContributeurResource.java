package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.service.ContributeurService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.service.dto.ContributeurDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Contributeur.
 */
@RestController
@RequestMapping("/api")
public class ContributeurResource {

    private final Logger log = LoggerFactory.getLogger(ContributeurResource.class);

    private static final String ENTITY_NAME = "contributeur";

    private final ContributeurService contributeurService;

    public ContributeurResource(ContributeurService contributeurService) {
        this.contributeurService = contributeurService;
    }

    /**
     * POST  /contributeurs : Create a new contributeur.
     *
     * @param contributeurDTO the contributeurDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contributeurDTO, or with status 400 (Bad Request) if the contributeur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contributeurs")
    @Timed
    public ResponseEntity<ContributeurDTO> createContributeur(@Valid @RequestBody ContributeurDTO contributeurDTO) throws URISyntaxException {
        log.debug("REST request to save Contributeur : {}", contributeurDTO);
        if (contributeurDTO.getId() != null) {
            throw new BadRequestAlertException("A new contributeur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContributeurDTO result = contributeurService.save(contributeurDTO);
        return ResponseEntity.created(new URI("/api/contributeurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contributeurs : Updates an existing contributeur.
     *
     * @param contributeurDTO the contributeurDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contributeurDTO,
     * or with status 400 (Bad Request) if the contributeurDTO is not valid,
     * or with status 500 (Internal Server Error) if the contributeurDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contributeurs")
    @Timed
    public ResponseEntity<ContributeurDTO> updateContributeur(@Valid @RequestBody ContributeurDTO contributeurDTO) throws URISyntaxException {
        log.debug("REST request to update Contributeur : {}", contributeurDTO);
        if (contributeurDTO.getId() == null) {
            return createContributeur(contributeurDTO);
        }
        ContributeurDTO result = contributeurService.save(contributeurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contributeurDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contributeurs : get all the contributeurs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contributeurs in body
     */
    @GetMapping("/contributeurs")
    @Timed
    public ResponseEntity<List<ContributeurDTO>> getAllContributeurs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Contributeurs");
        Page<ContributeurDTO> page = contributeurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contributeurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contributeurs/:id : get the "id" contributeur.
     *
     * @param id the id of the contributeurDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contributeurDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contributeurs/{id}")
    @Timed
    public ResponseEntity<ContributeurDTO> getContributeur(@PathVariable Long id) {
        log.debug("REST request to get Contributeur : {}", id);
        ContributeurDTO contributeurDTO = contributeurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contributeurDTO));
    }

    /**
     * DELETE  /contributeurs/:id : delete the "id" contributeur.
     *
     * @param id the id of the contributeurDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contributeurs/{id}")
    @Timed
    public ResponseEntity<Void> deleteContributeur(@PathVariable Long id) {
        log.debug("REST request to delete Contributeur : {}", id);
        contributeurService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
