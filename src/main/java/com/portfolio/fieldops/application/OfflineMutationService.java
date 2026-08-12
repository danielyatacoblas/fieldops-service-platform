package com.portfolio.fieldops.application;

import com.portfolio.fieldops.persistence.AppliedMutationJpaEntity;
import com.portfolio.fieldops.persistence.AppliedMutationRepository;
import com.portfolio.fieldops.persistence.TechnicianRepository;
import com.portfolio.fieldops.persistence.WorkOrderJpaEntity;
import com.portfolio.fieldops.persistence.WorkOrderRepository;
import java.time.Clock;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OfflineMutationService {
    private final WorkOrderRepository workOrders;
    private final TechnicianRepository technicians;
    private final AppliedMutationRepository mutations;
    private final Clock clock;
    public OfflineMutationService(WorkOrderRepository w,TechnicianRepository t,AppliedMutationRepository m,Clock c){workOrders=w;technicians=t;mutations=m;clock=c;}

    @Transactional
    public SyncResult apply(UUID mutationId,UUID workOrderId,long expectedVersion,String status,UUID technicianId) {
        var replay=mutations.findById(mutationId);
        if(replay.isPresent()) return new SyncResult("REPLAYED",replay.orElseThrow().resultVersion,null);
        WorkOrderJpaEntity order=workOrders.findById(workOrderId).orElseThrow(()->new IllegalArgumentException("work order not found"));
        if(order.version!=expectedVersion) return new SyncResult("CONFLICT",order.version,FieldOpsService.toView(order));
        validateTransition(order.status,status);
        if(technicianId!=null && !technicians.existsById(technicianId)) throw new IllegalArgumentException("technician not found");
        order.status=status; order.technicianId=technicianId; order.updatedAt=clock.instant();
        WorkOrderJpaEntity saved=workOrders.saveAndFlush(order);
        AppliedMutationJpaEntity applied=new AppliedMutationJpaEntity(); applied.clientMutationId=mutationId;
        applied.workOrderId=workOrderId; applied.resultVersion=saved.version; applied.appliedAt=clock.instant(); mutations.save(applied);
        return new SyncResult("APPLIED",saved.version,FieldOpsService.toView(saved));
    }
    private static void validateTransition(String current,String next){
        boolean valid=switch(current){case "OPEN"->"ASSIGNED".equals(next);case "ASSIGNED"->"IN_PROGRESS".equals(next);case "IN_PROGRESS"->"COMPLETED".equals(next);case "COMPLETED"->false;default->false;};
        if(!valid)throw new IllegalArgumentException("invalid transition from "+current+" to "+next);
    }
    public record SyncResult(String outcome,long version,FieldOpsService.WorkOrderView serverState){}
}
