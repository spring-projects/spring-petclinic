package org.springframework.samples.petclinic.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.dto.PetAttributeDto;
import org.springframework.samples.petclinic.model.PetTypeAttribute;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.samples.petclinic.repository.PetTypeAttributeRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

/**
 * Service layer for PetTypeAttribute operations. Contains business logic,
 * validations, and logging.
 * 
 * @Author: Sujeet Sharma
 * @Date: 08--9-2025
 */

@Service
public class PetTypeAttributeServiceImpl implements PetTypeAttributeService {

	 /** Repository for PetTypeAttribute entity */
    private final PetTypeAttributeRepository attrRepo;

    /** Repository for PetType entity */
    private final PetTypeRepository typeRepo;

    /** Mapper for converting between DTOs and entities */
    private final ModelMapper modelMapper;

    /** Logger for service operations */
    private final Logger logger = LoggerFactory.getLogger(PetTypeAttributeServiceImpl.class);
    

    
    public PetTypeAttributeServiceImpl(PetTypeAttributeRepository attrRepo,
                                       PetTypeRepository typeRepo,
                                       ModelMapper modelMapper) {
        this.attrRepo = attrRepo;
        this.typeRepo = typeRepo;
        this.modelMapper = modelMapper;
    }

    /**
     * Create a new attribute for a given PetType.
     *
     * @param typeId PetType ID
     * @param dto    DTO containing attribute details
     * @return Created PetAttributeDto
     */
    @Transactional
    @Override
    public PetAttributeDto create(Integer typeId, PetAttributeDto attrDto) {
        logger.info("Creating PetAttribute for PetType id={}", typeId);

        PetType type = typeRepo.findById(typeId)
                .orElseThrow(() -> {
                    logger.error("No PetType found with id={}", typeId);
                    return new ResourceNotFoundException("No PetType found with id " + typeId);
                });

        PetTypeAttribute entity = modelMapper.map(attrDto, PetTypeAttribute.class);
        entity.setPetType(type);

        PetTypeAttribute saved = attrRepo.save(entity);
        logger.debug("PetTypeAttribute saved with id={}", saved.getId());

        return modelMapper.map(saved, PetAttributeDto.class);
    }

    /**
     * Fetch an attribute by its ID.
     *
     * @param id Attribute ID
     * @return PetAttributeDto if found
     * @throws ResourceNotFoundException if attribute does not exist
     */
    @Override
    public PetAttributeDto getById(Integer id) {
    	 logger.info("Fetching PetAttribute id={}", id);

         PetTypeAttribute entity = attrRepo.findById(id)
                 .orElseThrow(() -> {
                     logger.error("PetAttribute not found with id={}", id);
                     return new ResourceNotFoundException("No PetTypeAttribute found with id " + id);
                 });

         return modelMapper.map(entity, PetAttributeDto.class);
    }

    /**
     * Fetch all attributes for a given PetType.
     *
     * @param typeId PetType ID
     * @return List of PetAttributeDto
     */
    @Override
    public List<PetAttributeDto> getByTypeId(Integer typeId) {
    	 logger.info("Fetching PetAttributes for PetType id={}", typeId);

         List<PetTypeAttribute> attributes = attrRepo.findByPetType_Id(typeId);
         logger.debug("Found {} PetAttributes for PetType id={}", attributes.size(), typeId);

         return attributes.stream()
                 .map(attr -> modelMapper.map(attr, PetAttributeDto.class))
                 .collect(Collectors.toList());
    }

    /**
     * Update an existing attribute.
     *
     * @param id  Attribute ID
     * @param dto DTO containing updated values
     * @return Updated PetAttributeDto
     */
    @Transactional
    @Override
    public PetAttributeDto update(Integer id, PetAttributeDto dto) {
    	  logger.info("Updating PetAttribute id={}", id);

          PetTypeAttribute existing = attrRepo.findById(id)
                  .orElseThrow(() -> {
                      logger.error("PetAttribute not found with id={}", id);
                      return new ResourceNotFoundException("No PetTypeAttribute found with id " + id);
                  });

          existing.setTemperament(dto.getTemperament());
          existing.setLength(dto.getLength());
          existing.setWeight(dto.getWeight());

          PetTypeAttribute updated = attrRepo.save(existing);
          logger.debug("Updated PetAttribute id={}", updated.getId());

          return modelMapper.map(updated, PetAttributeDto.class);
    }

    /**
     * Delete an attribute by ID.
     *
     * @param id Attribute ID
     * @throws ResourceNotFoundException if attribute does not exist
     */
    @Override
    public void delete(Integer id) {
    	 logger.warn("Deleting PetAttribute id={}", id);

         if (!attrRepo.existsById(id)) {
             logger.error("PetAttribute not found with id={}", id);
             throw new ResourceNotFoundException("PetTypeAttribute not found with id " + id);
         }
         attrRepo.deleteById(id);
         logger.info("Deleted PetAttribute id={}", id);
    }

    /**
     * Custom exception for handling resource not found cases.
     */
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String msg) {
            super(msg);
        }
    }
}



