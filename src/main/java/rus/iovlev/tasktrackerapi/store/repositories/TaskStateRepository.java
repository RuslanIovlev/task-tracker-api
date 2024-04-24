package rus.iovlev.tasktrackerapi.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rus.iovlev.tasktrackerapi.store.entities.TaskStateEntity;

import java.util.Optional;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {

    Optional<TaskStateEntity> findTaskStateEntitiesByProjectIdAndNameContainsIgnoreCase(Long projectId, String taskStateName);


}
