package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.RestaurantDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Restaurant and its DTO RestaurantDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface RestaurantMapper extends EntityMapper<RestaurantDTO, Restaurant> {

    @Mapping(source = "location.id", target = "locationId")
    RestaurantDTO toDto(Restaurant restaurant); 

    @Mapping(source = "locationId", target = "location")
    @Mapping(target = "ratings", ignore = true)
    Restaurant toEntity(RestaurantDTO restaurantDTO);

    default Restaurant fromId(Long id) {
        if (id == null) {
            return null;
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        return restaurant;
    }
}
