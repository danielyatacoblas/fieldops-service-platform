package com.portfolio.fieldops.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="applied_mutations")
public class AppliedMutationJpaEntity {
    @Id @Column(name="client_mutation_id") public UUID clientMutationId;
    @Column(name="work_order_id",nullable=false) public UUID workOrderId;
    @Column(name="result_version",nullable=false) public long resultVersion;
    @Column(name="applied_at",nullable=false) public Instant appliedAt;
    public AppliedMutationJpaEntity() {}
}
