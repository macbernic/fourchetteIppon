package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ContributeurDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Contributeur and its DTO ContributeurDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContributeurMapper extends EntityMapper<ContributeurDTO, Contributeur> {

    

    @Mapping(target = "ratings", ignore = true)
    Contributeur toEntity(ContributeurDTO contributeurDTO);

    default Contributeur fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contributeur contributeur = new Contributeur();
        contributeur.setId(id);
        return contributeur;
    }
}
