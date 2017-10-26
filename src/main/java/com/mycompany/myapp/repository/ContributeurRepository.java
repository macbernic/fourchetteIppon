package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Contributeur;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Contributeur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContributeurRepository extends JpaRepository<Contributeur, Long> {

}
