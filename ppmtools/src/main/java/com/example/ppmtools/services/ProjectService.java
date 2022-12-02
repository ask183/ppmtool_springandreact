package com.example.ppmtools.services;

import com.example.ppmtools.domain.Backlog;
import com.example.ppmtools.domain.Project;
import com.example.ppmtools.domain.User;
import com.example.ppmtools.exception.ProjectIdException;
import com.example.ppmtools.exception.ProjectNotFoundException;
import com.example.ppmtools.repositories.BacklogRepository;
import com.example.ppmtools.repositories.ProjectRepository;
import com.example.ppmtools.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username) {
        if(project.getId() != null){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if(existingProject !=null &&(!existingProject.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Project not found in your account");
            }else if(existingProject == null){
                throw new ProjectNotFoundException("Project with ID: '"+project.getProjectIdentifier()+"' cannot be updated because it doesn't exist");
            }
        }


        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier()));
            }

            return projectRepository.save(project);
        } catch (Exception e){
            throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exists");        }
    }

    public Project findProjectByIdentifier(String projectId, String username){
        //only want to return the project if the user looking for it is the owner

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException("Project ID '"+projectId+"' doesnt exists");
        }

        if (!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username){ //iterable returns json object that hass all the json elements(objects) within that list we want to display

        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username){
//        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
//
//        if (project == null){
//            throw new ProjectIdException("Cannot delete project with ID '" + projectId+ "'. This project does not exist");
//        }

        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }


}
