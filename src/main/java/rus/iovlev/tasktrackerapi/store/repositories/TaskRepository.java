package rus.iovlev.tasktrackerapi.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rus.iovlev.tasktrackerapi.store.entities.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
