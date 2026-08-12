package com.portfolio.fieldops.web;

import com.portfolio.fieldops.application.FieldOpsService;
import com.portfolio.fieldops.application.OfflineMutationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/v1")
public class FieldOpsController {
    private final FieldOpsService service; private final OfflineMutationService sync;
    public FieldOpsController(FieldOpsService service,OfflineMutationService sync){this.service=service;this.sync=sync;}

    @PutMapping("/technicians/{id}")
    FieldOpsService.TechnicianView technician(@PathVariable UUID id,@Valid @RequestBody TechnicianRequest r){return service.upsertTechnician(id,r.name(),r.latitude(),r.longitude(),r.available());}
    @GetMapping("/technicians/nearby")
    List<FieldOpsService.NearbyView> nearby(@RequestParam double latitude,@RequestParam double longitude,@RequestParam @Positive double radiusMeters,@RequestParam(defaultValue="10") @Min(1) @Max(100) int limit){return service.findNearby(latitude,longitude,radiusMeters,limit);}
    @PostMapping("/work-orders")
    ResponseEntity<FieldOpsService.WorkOrderView> create(@Valid @RequestBody WorkOrderRequest r){var created=service.createWorkOrder(r.summary(),r.latitude(),r.longitude());return ResponseEntity.created(URI.create("/api/v1/work-orders/"+created.id())).body(created);}
    @GetMapping("/work-orders/{id}")
    FieldOpsService.WorkOrderView find(@PathVariable UUID id){return service.findWorkOrder(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"work order not found"));}
    @PostMapping("/sync/mutations")
    OfflineMutationService.SyncResult mutate(@Valid @RequestBody MutationRequest r){return sync.apply(r.clientMutationId(),r.workOrderId(),r.expectedVersion(),r.status(),r.technicianId());}

    record TechnicianRequest(@NotBlank String name,@Min(-90) @Max(90) double latitude,@Min(-180) @Max(180) double longitude,boolean available){}
    record WorkOrderRequest(@NotBlank String summary,@Min(-90) @Max(90) double latitude,@Min(-180) @Max(180) double longitude){}
    record MutationRequest(@NotNull UUID clientMutationId,@NotNull UUID workOrderId,@Min(0) long expectedVersion,@NotBlank String status,UUID technicianId){}
}
