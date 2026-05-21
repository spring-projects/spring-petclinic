/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.core.usecases.vet;

import java.util.Collection;

import org.springframework.samples.petclinic.core.domain.vet.Vet;
import org.springframework.samples.petclinic.core.gateways.vet.VetRepositoryGateway;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: listar veterinários (com ou sem paginação).
 */
@Service
public class ListVetsUseCase {

	private final VetRepositoryGateway vetRepositoryGateway;

	public ListVetsUseCase(VetRepositoryGateway vetRepositoryGateway) {
		this.vetRepositoryGateway = vetRepositoryGateway;
	}

	public Collection<Vet> listAll() {
		return vetRepositoryGateway.findAll();
	}

	/**
	 * @param page número da página (1-based, como vem da UI)
	 * @param pageSize tamanho da página
	 */
	public PagedResult<Vet> listPaginated(int page, int pageSize) {
		return vetRepositoryGateway.findAll(page - 1, pageSize);
	}

}
