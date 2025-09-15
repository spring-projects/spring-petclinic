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
public class PetAttributeServiceImpl implements PetAttributeService {

	 /** Repository for PetTypeAttribute entity */
    private final PetTypeAttributeRepository attrRepo;

    /** Repository for PetType entity */
    private final PetTypeRepository typeRepo;

    /** Mapper for converting between DTOs and entities */
    private final ModelMapper modelMapper;

    /** Logger for service operations */
    private final Logger logger = LoggerFactory.getLogger(PetAttributeServiceImpl.class);
    

    
    public PetAttributeServiceImpl(PetTypeAttributeRepository attrRepo,
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
        logger.info("PetAttributeServiceImpl :: create :: Creating PetAttribute for PetType id={}", typeId);

        PetType type = typeRepo.findById(typeId)
                .orElseThrow(() -> {
                    logger.error("PetAttributeServiceImpl :: create ::No PetType found with id={}", typeId);
                    return new ResourceNotFoundException("No PetType found with id " + typeId);
                });

        PetTypeAttribute entity = modelMapper.map(attrDto, PetTypeAttribute.class);
        entity.setPetType(type);

        PetTypeAttribute saved = attrRepo.save(entity);
        logger.debug("PetAttributeServiceImpl :: create ::PetTypeAttribute saved with id={}", saved.getId());

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
    	 logger.info("PetAttributeServiceImpl :: getById :: Fetching PetAttribute id={}", id);

         PetTypeAttribute entity = attrRepo.findById(id)
                 .orElseThrow(() -> {
                     logger.error("PetAttributeServiceImpl :: getById :: PetAttribute not found with id={}", id);
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
    	 logger.info("PetAttributeServiceImpl :: getByTypeId ::Fetching PetAttributes for PetType id={}", typeId);

         List<PetTypeAttribute> attributes = attrRepo.findByPetType_Id(typeId);
         logger.debug("PetAttributeServiceImpl :: getByTypeId :: Found {} PetAttributes for PetType id={}", attributes.size(), typeId);

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
    	  logger.info("PetAttributeServiceImpl :: update :: Updating PetAttribute id={}", id);

          PetTypeAttribute existing = attrRepo.findById(id)
                  .orElseThrow(() -> {
                      logger.error("PetAttributeServiceImpl :: update :: PetAttribute not found with id={}", id);
                      return new ResourceNotFoundException("No PetTypeAttribute found with id " + id);
                  });

          existing.setTemperament(dto.getTemperament());
          existing.setLength(dto.getLength());
          existing.setWeight(dto.getWeight());

          PetTypeAttribute updated = attrRepo.save(existing);
          logger.debug("PetAttributeServiceImpl :: update :: Updated PetAttribute id={}", updated.getId());

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
    	 logger.warn("PetAttributeServiceImpl :: delete ::Deleting PetAttribute id before save={}", id);

         if (!attrRepo.existsById(id)) {
             logger.error("PetAttributeServiceImpl :: delete ::PetAttribute not found with id={}", id);
             throw new ResourceNotFoundException("PetTypeAttribute not found with id " + id);
         }
         attrRepo.deleteById(id);
         logger.info("PetAttributeServiceImpl :: delete :: Deleted PetAttribute id successfully={}", id);
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



