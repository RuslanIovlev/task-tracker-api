package rus.iovlev.tasktrackerapi.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rus.iovlev.tasktrackerapi.store.entities.ProjectEntity;

import java.util.Optional;
import java.util.stream.Stream;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByName(String name);

    Stream<ProjectEntity> streamAll();

    Stream<ProjectEntity> streamAllByNameStartsWithIgnoreCase(String prefixName);
}
