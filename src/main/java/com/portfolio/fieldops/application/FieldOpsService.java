package com.portfolio.fieldops.application;

import com.portfolio.fieldops.persistence.TechnicianJpaEntity;
import com.portfolio.fieldops.persistence.TechnicianRepository;
import com.portfolio.fieldops.persistence.WorkOrderJpaEntity;
import com.portfolio.fieldops.persistence.WorkOrderRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FieldOpsService {
    private final TechnicianRepository technicians;
    private final WorkOrderRepository workOrders;
    private final Clock clock;

    public FieldOpsService(TechnicianRepository technicians, WorkOrderRepository workOrders, Clock clock) {
        this.technicians=technicians; this.workOrders=workOrders; this.clock=clock;
    }

    @Transactional
    public TechnicianView upsertTechnician(UUID id,String name,double latitude,double longitude,boolean available) {
        validateCoordinates(latitude,longitude);
        TechnicianJpaEntity entity=technicians.findById(id).orElseGet(TechnicianJpaEntity::new);
        entity.id=id; entity.name=requireText(name); entity.latitude=latitude; entity.longitude=longitude;
        entity.available=available; entity.updatedAt=clock.instant();
        return toView(technicians.save(entity));
    }

    @Transactional(readOnly=true)
    public List<NearbyView> findNearby(double latitude,double longitude,double radiusMeters,int limit) {
        validateCoordinates(latitude,longitude);
        if(radiusMeters<=0 || limit<1 || limit>100) throw new IllegalArgumentException("invalid radius or limit");
        return technicians.findNearby(latitude,longitude,radiusMeters,limit).stream()
            .map(t->new NearbyView(t.getId(),t.getName(),t.getLatitude(),t.getLongitude(),t.getDistanceMeters())).toList();
    }

    @Transactional
    public WorkOrderView createWorkOrder(String summary,double latitude,double longitude) {
        validateCoordinates(latitude,longitude);
        Instant now=clock.instant(); WorkOrderJpaEntity entity=new WorkOrderJpaEntity();
        entity.id=UUID.randomUUID(); entity.summary=requireText(summary); entity.status="OPEN";
        entity.latitude=latitude; entity.longitude=longitude; entity.createdAt=now; entity.updatedAt=now;
        return toView(workOrders.save(entity));
    }

    @Transactional(readOnly=true)
    public Optional<WorkOrderView> findWorkOrder(UUID id) { return workOrders.findById(id).map(FieldOpsService::toView); }

    static WorkOrderView toView(WorkOrderJpaEntity e) {
        return new WorkOrderView(e.id,e.summary,e.status,e.technicianId,e.latitude,e.longitude,e.version,e.updatedAt);
    }
    private static TechnicianView toView(TechnicianJpaEntity e) { return new TechnicianView(e.id,e.name,e.latitude,e.longitude,e.available,e.updatedAt); }
    private static String requireText(String value){if(value==null||value.isBlank())throw new IllegalArgumentException("text is required");return value.trim();}
    static void validateCoordinates(double lat,double lon){if(lat < -90||lat>90||lon < -180||lon>180)throw new IllegalArgumentException("invalid coordinates");}

    public record TechnicianView(UUID id,String name,double latitude,double longitude,boolean available,Instant updatedAt){}
    public record NearbyView(UUID id,String name,double latitude,double longitude,double distanceMeters){}
    public record WorkOrderView(UUID id,String summary,String status,UUID technicianId,double latitude,double longitude,long version,Instant updatedAt){}
}
