package rus.iovlev.tasktrackerapi.api.factories;

import org.springframework.stereotype.Component;
import rus.iovlev.tasktrackerapi.api.dto.ProjectDto;
import rus.iovlev.tasktrackerapi.store.entities.ProjectEntity;

@Component
public class ProjectDtoFactory {
    public ProjectDto makeProjectDto(ProjectEntity entity){
        return ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
