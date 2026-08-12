package com.portfolio.fieldops.persistence;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AppliedMutationRepository extends JpaRepository<AppliedMutationJpaEntity,UUID> {}
