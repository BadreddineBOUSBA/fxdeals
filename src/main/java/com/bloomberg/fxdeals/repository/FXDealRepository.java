package com.bloomberg.fxdeals.repository;

import com.bloomberg.fxdeals.model.FXDeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FXDealRepository extends JpaRepository<FXDeal, Long> {

    boolean existsByDealId(String dealId);

    Optional<FXDeal> findByDealId(String dealId);
}
