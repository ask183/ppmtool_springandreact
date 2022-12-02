package com.example.ppmtools.services;

import com.example.ppmtools.domain.Backlog;
import com.example.ppmtools.domain.Project;
import com.example.ppmtools.domain.ProjectTask;
import com.example.ppmtools.exception.ProjectNotFoundException;
import com.example.ppmtools.repositories.BacklogRepository;
import com.example.ppmtools.repositories.ProjectRepository;
import com.example.ppmtools.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);
            Integer BacklogSequence = backlog.getPTSequence();
            BacklogSequence++;
            backlog.setPTSequence(BacklogSequence);

            projectTask.setProjectSequence(backlog.getProjectIdentifier()+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if(projectTask.getStatus()=="" || projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }

            if(projectTask.getPriority()==null || projectTask.getPriority()==0){//projectTask.getPriority()==0 ||
                projectTask.setPriority(3);
            }

            return projectTaskRepository.save(projectTask);

    }

    public Iterable<ProjectTask> findBacklogById(String id, String username){

        projectService.findProjectByIdentifier(id, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username){

        //make sure we are searching on the right backlog
        projectService.findProjectByIdentifier(backlog_id, username);

        //make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask==null){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
        }

        //make sure that the backlog/project id in the path corresponds to the right project
        if (!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id);
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
        //update project task
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTask = updatedTask;
        return projectTaskRepository.save(projectTask);
        //find existing project task
        //replace it with updated task
        //save updated
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTaskRepository.delete(projectTask);
    }
}

