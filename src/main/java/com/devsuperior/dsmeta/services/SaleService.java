package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleMinDTO> getReport(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate startDate = (minDate == null) ? today.minusYears(1) : LocalDate.parse(minDate);
		LocalDate endDate = (maxDate == null) ? today : LocalDate.parse(maxDate);

		String searchName = (name == null) ? "" : name;

		return repository.findSalesReport(startDate, endDate, searchName, pageable);
	}

	public Page<SaleSummaryDTO> getSummary(String minDate, String maxDate, String name, Double total, Pageable pageable) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate startDate = (minDate == null) ? today.minusYears(1) : LocalDate.parse(minDate);
		LocalDate endDate = (maxDate == null) ? today : LocalDate.parse(maxDate);

		String searchName = (name == null) ? "" : name;

		return repository.findSalesSummaryWithPagination(startDate, endDate, searchName, total, pageable);
	}
}
