package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.RatingService;
import com.mycompany.myapp.domain.Rating;
import com.mycompany.myapp.repository.RatingRepository;
import com.mycompany.myapp.service.dto.RatingDTO;
import com.mycompany.myapp.service.mapper.RatingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Rating.
 */
@Service
@Transactional
public class RatingServiceImpl implements RatingService{

    private final Logger log = LoggerFactory.getLogger(RatingServiceImpl.class);

    private final RatingRepository ratingRepository;

    private final RatingMapper ratingMapper;

    public RatingServiceImpl(RatingRepository ratingRepository, RatingMapper ratingMapper) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
    }

    /**
     * Save a rating.
     *
     * @param ratingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RatingDTO save(RatingDTO ratingDTO) {
        log.debug("Request to save Rating : {}", ratingDTO);
        Rating rating = ratingMapper.toEntity(ratingDTO);
        rating = ratingRepository.save(rating);
        return ratingMapper.toDto(rating);
    }

    /**
     *  Get all the ratings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RatingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ratings");
        return ratingRepository.findAll(pageable)
            .map(ratingMapper::toDto);
    }

    /**
     *  Get one rating by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RatingDTO findOne(Long id) {
        log.debug("Request to get Rating : {}", id);
        Rating rating = ratingRepository.findOne(id);
        return ratingMapper.toDto(rating);
    }

    /**
     *  Delete the  rating by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rating : {}", id);
        ratingRepository.delete(id);
    }
}
