package rus.iovlev.tasktrackerapi.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rus.iovlev.tasktrackerapi.api.controllers.helpers.ControllerHelper;
import rus.iovlev.tasktrackerapi.api.dto.AskDto;
import rus.iovlev.tasktrackerapi.api.dto.TaskStateDto;
import rus.iovlev.tasktrackerapi.api.exceptions.BadRequestException;
import rus.iovlev.tasktrackerapi.api.exceptions.NotFoundException;
import rus.iovlev.tasktrackerapi.api.factories.TaskStateDtoFactory;
import rus.iovlev.tasktrackerapi.store.entities.ProjectEntity;
import rus.iovlev.tasktrackerapi.store.entities.TaskStateEntity;
import rus.iovlev.tasktrackerapi.store.repositories.TaskStateRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@RestController
public class TaskStateController {

    private final TaskStateRepository taskStateRepository;

    private final TaskStateDtoFactory taskStateDtoFactory;

    private final ControllerHelper controllerHelper;

    public static final String CREATE_TASK_STATE = "/api/projects/{project_id}/task-states";
    public static final String GET_TASK_STATES = "/api/projects/{project_id}/task-states";
    public static final String UPDATE_TASK_STATES = "/api/task-states/{task_state_id}";
    public static final String DELETE_TASK_STATE = "/api/task-states/{task_state_id}";

    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDto> getTaskStates(@PathVariable(name = "project_id") Long projectId){

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        return project
                .getTaskStates()
                .stream()
                .map(taskStateDtoFactory::makeTaskStateDto)
                .collect(Collectors.toList());
    }

    @PostMapping(CREATE_TASK_STATE)
    public TaskStateDto createTaskState(@PathVariable(name = "project_id") Long project_id,
                                        @RequestParam(name = "task_state_name") String taskStateName){

        if (taskStateName.isBlank()){
            throw new BadRequestException("Task state can't be empty");
        }

        ProjectEntity project = controllerHelper.getProjectOrThrowException(project_id);

        Optional<TaskStateEntity> optionalAnotherTaskState = Optional.empty();

        for (TaskStateEntity taskState: project.getTaskStates()){
            if (taskState.getName().equalsIgnoreCase(taskStateName)) {
                throw new BadRequestException(String.format("Task state \"%s\" already exists.", taskStateName));
            }

            if (taskState.getRightTaskState().isEmpty()) {
                optionalAnotherTaskState = Optional.of(taskState);
                break;
            }
        }

        TaskStateEntity taskState = taskStateRepository.saveAndFlush(
                TaskStateEntity.builder()
                        .name(taskStateName)
                        .project(project)
                        .build());

        optionalAnotherTaskState
                .ifPresent(anotherTaskState -> {
                    taskState.setLeftTaskState(anotherTaskState);
                    anotherTaskState.setRightTaskState(taskState);
                    taskStateRepository.saveAndFlush(anotherTaskState);
                });

        final TaskStateEntity savedTaskState = taskStateRepository.saveAndFlush(taskState);

        return taskStateDtoFactory.makeTaskStateDto(savedTaskState);
    }

    @PatchMapping(UPDATE_TASK_STATES)
    public TaskStateDto updateTaskState(@PathVariable(name = "task_state_id") Long taskStateId,
                                        @RequestParam(name = "task_state_name") String taskStateName){
        if (taskStateName.isBlank()){
            throw new BadRequestException("Task state can't be empty");
        }

        TaskStateEntity taskState = getTaskStateOrThrowException(taskStateId);

        taskStateRepository
                .findTaskStateEntitiesByProjectIdAndNameContainsIgnoreCase(
                        taskState.getProject().getId(),
                        taskStateName
                )
                .filter(anotherTaskState -> anotherTaskState.getId().equals(taskStateId))
                .ifPresent(anotherTaskState -> {
                    throw new BadRequestException(String.format("Task state \"%s\" already exists.", taskStateName));
                });

        taskState.setName(taskStateName);
        taskState = taskStateRepository.saveAndFlush(taskState);

        return taskStateDtoFactory.makeTaskStateDto(taskState);

    }

    @DeleteMapping(DELETE_TASK_STATE)
    public AskDto deleteTaskState(@PathVariable(name = "task_state_id") Long taskStateId) {

        TaskStateEntity changeTaskState = getTaskStateOrThrowException(taskStateId);

        replaceOldTaskStatePosition(changeTaskState);

        taskStateRepository.delete(changeTaskState);

        return AskDto.builder().answer(true).build();


    }

    private void replaceOldTaskStatePosition(TaskStateEntity changeTaskState) {

        Optional<TaskStateEntity> optionalOldLeftTaskState = changeTaskState.getLeftTaskState();
        Optional<TaskStateEntity> optionalOldRightTaskState = changeTaskState.getRightTaskState();

        optionalOldLeftTaskState
                .ifPresent(it -> {

                    it.setRightTaskState(optionalOldRightTaskState.orElse(null));

                    taskStateRepository.saveAndFlush(it);
                });

        optionalOldRightTaskState
                .ifPresent(it -> {

                    it.setLeftTaskState(optionalOldLeftTaskState.orElse(null));

                    taskStateRepository.saveAndFlush(it);
                });
    }

    private TaskStateEntity getTaskStateOrThrowException(Long taskSTateId) {
        return taskStateRepository
                .findById(taskSTateId)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Task state with \"%s\" id doesn't exist.", taskSTateId)));
    }


}
