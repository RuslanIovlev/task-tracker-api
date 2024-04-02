package rus.iovlev.tasktrackerapi.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rus.iovlev.tasktrackerapi.store.entities.TaskStateEntity;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
}
