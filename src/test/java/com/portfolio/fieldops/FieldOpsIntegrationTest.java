package com.portfolio.fieldops;

import static org.assertj.core.api.Assertions.assertThat;

import com.portfolio.fieldops.application.FieldOpsService;
import com.portfolio.fieldops.application.OfflineMutationService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers(disabledWithoutDocker=true)
class FieldOpsIntegrationTest {
 @Container static final PostgreSQLContainer postgres=new PostgreSQLContainer(
     DockerImageName.parse("postgis/postgis:17-3.5-alpine").asCompatibleSubstituteFor("postgres"));
 @DynamicPropertySource static void db(DynamicPropertyRegistry r){r.add("spring.datasource.url",postgres::getJdbcUrl);r.add("spring.datasource.username",postgres::getUsername);r.add("spring.datasource.password",postgres::getPassword);}
 @Autowired FieldOpsService service; @Autowired OfflineMutationService sync;

 @Test void findsNearestAndDetectsOfflineConflict(){
  UUID near=UUID.randomUUID(),far=UUID.randomUUID();
  service.upsertTechnician(near,"Near",-12.0464,-77.0428,true);
  service.upsertTechnician(far,"Far",-12.20,-77.20,true);
  var nearby=service.findNearby(-12.0465,-77.0430,5000,10);
  assertThat(nearby).extracting(FieldOpsService.NearbyView::id).contains(near).doesNotContain(far);
  var order=service.createWorkOrder("Repair router",-12.0465,-77.0430);
  var applied=sync.apply(UUID.randomUUID(),order.id(),0,"ASSIGNED",near);
  var conflict=sync.apply(UUID.randomUUID(),order.id(),0,"IN_PROGRESS",near);
  assertThat(applied.outcome()).isEqualTo("APPLIED");
  assertThat(conflict.outcome()).isEqualTo("CONFLICT");
  assertThat(conflict.version()).isEqualTo(1);
 }
}
