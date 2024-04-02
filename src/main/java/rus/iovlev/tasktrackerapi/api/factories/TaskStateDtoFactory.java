package rus.iovlev.tasktrackerapi.api.factories;

import org.springframework.stereotype.Component;
import rus.iovlev.tasktrackerapi.api.dto.TaskStateDto;
import rus.iovlev.tasktrackerapi.store.entities.TaskStateEntity;
@Component
public class TaskStateDtoFactory {
    public TaskStateDto makeTaskStateDto(TaskStateEntity entity) {
        return TaskStateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .ordinal(entity.getOrdinal())
                .build();
    }
}
