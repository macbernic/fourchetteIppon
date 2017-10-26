package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.RatingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Rating and its DTO RatingDTO.
 */
@Mapper(componentModel = "spring", uses = {RestaurantMapper.class, ContributeurMapper.class})
public interface RatingMapper extends EntityMapper<RatingDTO, Rating> {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "contributeur.id", target = "contributeurId")
    RatingDTO toDto(Rating rating); 

    @Mapping(source = "restaurantId", target = "restaurant")
    @Mapping(source = "contributeurId", target = "contributeur")
    Rating toEntity(RatingDTO ratingDTO);

    default Rating fromId(Long id) {
        if (id == null) {
            return null;
        }
        Rating rating = new Rating();
        rating.setId(id);
        return rating;
    }
}
