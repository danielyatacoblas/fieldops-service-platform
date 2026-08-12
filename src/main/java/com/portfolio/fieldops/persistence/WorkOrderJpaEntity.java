package com.portfolio.fieldops.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="work_orders")
public class WorkOrderJpaEntity {
    @Id public UUID id;
    @Column(nullable=false,length=200) public String summary;
    @Column(nullable=false,length=30) public String status;
    @Column(name="technician_id") public UUID technicianId;
    @Column(nullable=false) public double latitude;
    @Column(nullable=false) public double longitude;
    @Version public long version;
    @Column(name="created_at",nullable=false) public Instant createdAt;
    @Column(name="updated_at",nullable=false) public Instant updatedAt;
    public WorkOrderJpaEntity() {}
}
