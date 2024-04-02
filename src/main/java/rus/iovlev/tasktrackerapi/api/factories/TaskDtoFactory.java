package rus.iovlev.tasktrackerapi.api.factories;

import org.springframework.stereotype.Component;
import rus.iovlev.tasktrackerapi.api.dto.TaskDto;
import rus.iovlev.tasktrackerapi.store.entities.TaskEntity;
@Component
public class TaskDtoFactory {
    public TaskDto makeTaskDto(TaskEntity entity){
        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .description(entity.getDescription())
                .build();
    }
}
