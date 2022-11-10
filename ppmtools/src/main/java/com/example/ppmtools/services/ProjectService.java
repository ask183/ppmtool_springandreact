package com.example.ppmtools.services;

import com.example.ppmtools.domain.Project;
import com.example.ppmtools.exception.ProjectIdException;
import com.example.ppmtools.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project) {
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        } catch (Exception e){
            throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exists");        }
    }

    public Project findProjectByIdentifier(String projectId){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException("Project ID '"+projectId+"' doesnt exists");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(){ //iterable returns json object that hass all the json elements(objects) within that list we want to display
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null){
            throw new ProjectIdException("Cannot delete project with ID '" + projectId+ "'. This project does not exist");
        }

        projectRepository.delete(project);
    }


}
