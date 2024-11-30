package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleMinDTO(s.id, s.amount, s.date, s.seller.name) " +
            "FROM Sale s " +
            "WHERE (:minDate IS NULL OR s.date >= :minDate) " +
            "AND (:maxDate IS NULL OR s.date <= :maxDate) " +
            "AND (:name IS NULL OR LOWER(s.seller.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<SaleMinDTO> findSalesReport(
            @Param("minDate") LocalDate minDate,
            @Param("maxDate") LocalDate maxDate,
            @Param("name") String name,
            Pageable pageable);

    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(s.seller.name, SUM(s.amount)) " +
            "FROM Sale s " +
            "WHERE (:name IS NULL OR LOWER(s.seller.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:total IS NULL OR SUM(s.amount) >= :total) " +
            "AND (:minDate IS NULL OR s.date >= :minDate) " +
            "AND (:maxDate IS NULL OR s.date <= :maxDate) " +
            "GROUP BY s.seller.name")
    Page<SaleSummaryDTO> findSalesSummaryWithPagination(
            @Param("minDate") LocalDate minDate,
            @Param("maxDate") LocalDate maxDate,
            @Param("name") String name,
            @Param("total") Double total,
            Pageable pageable);

}
