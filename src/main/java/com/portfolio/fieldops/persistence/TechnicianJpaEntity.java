package com.portfolio.fieldops.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="technicians")
public class TechnicianJpaEntity {
    @Id public UUID id;
    @Column(nullable=false,length=120) public String name;
    @Column(nullable=false) public double latitude;
    @Column(nullable=false) public double longitude;
    @Column(nullable=false) public boolean available;
    @Column(name="updated_at",nullable=false) public Instant updatedAt;
    public TechnicianJpaEntity() {}
}
