package org.springframework.samples.petclinic.service;

import java.util.Collection;

public interface BaseService<E,D> {
	public E dtoToEntity(D dto);
	public D entityToDTO(E entity);
	public Collection<D> entitiesToDTOS(Collection<E> entities);
	public Collection<E> dtosToEntities(Collection<D> dtos);
}
