package org.springframework.samples.petclinic.service;

import java.util.Collection;

public interface BaseService<E, D> {

	/**
	 * Convert Data Transfert Object to Entity Model
	 * @param dto DTO
	 * @return Entity Model
	 */
	public E dtoToEntity(D dto);

	/**
	 * Convert Entity Model to Data Transfert Object
	 * @param entity Entity Model
	 * @return DTO
	 */
	public D entityToDTO(E entity);

	/**
	 * Convert Entities Models Collection to Data Transfert Object Collection
	 * @param entities Collection of Entity Model
	 * @return Collection of DTO
	 */
	public Collection<D> entitiesToDTOS(Collection<E> entities);

	/**
	 * Convert Entities Models Collection to Data Transfert Object Collection
	 * @param dtos Collection of DTO
	 * @return Collection of Entity Model
	 */
	public Collection<E> dtosToEntities(Collection<D> dtos);

}
