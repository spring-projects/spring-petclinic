package org.springframework.samples.petclinic.core.usecases.owner;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PagedResult<T> {

	private final List<T> content;

	private final long totalElements;

	private final int totalPages;

	private final int currentPage;

}
