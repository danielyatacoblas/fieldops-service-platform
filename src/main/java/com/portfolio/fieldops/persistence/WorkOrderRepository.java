package com.portfolio.fieldops.persistence;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface WorkOrderRepository extends JpaRepository<WorkOrderJpaEntity,UUID> {}
