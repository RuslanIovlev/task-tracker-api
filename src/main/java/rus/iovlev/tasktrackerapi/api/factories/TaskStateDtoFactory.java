package rus.iovlev.tasktrackerapi.api.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rus.iovlev.tasktrackerapi.api.dto.TaskStateDto;
import rus.iovlev.tasktrackerapi.store.entities.TaskStateEntity;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TaskStateDtoFactory {

    private final TaskDtoFactory taskDtoFactory;

    public TaskStateDto makeTaskStateDto(TaskStateEntity entity) {
        return TaskStateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .leftTaskStateId(entity.getLeftTaskState().map(TaskStateEntity::getId).orElse(null))
                .rightTaskStateId(entity.getRightTaskState().map(TaskStateEntity::getId).orElse(null))
                .tasks(entity.getTasks().stream().map(taskDtoFactory::makeTaskDto).collect(Collectors.toList()))
                .build();
    }
}
