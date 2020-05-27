package com.etu.infrastructure.file;

import com.etu.infrastructure.state.convert.project.ProjectStateFactory;
import com.etu.infrastructure.state.convert.project.SerializableProjectStateFactory;
import com.etu.infrastructure.state.dto.runtime.project.ProjectState;
import com.etu.infrastructure.state.dto.serializable.project.SerializableProjectState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class FileServiceImpl implements FileService {

    @Autowired
    private SerializableProjectStateFactory serializableProjectStateFactory;
    @Autowired
    private ProjectStateFactory projectStateFactory;

    @Override
    public ProjectState loadProjectState(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SerializableProjectState serializableProjectState = objectMapper.readValue(file, SerializableProjectState.class);

            ProjectState projectState = projectStateFactory.create(serializableProjectState);
            projectState.setProjectFile(file);

            return projectState;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveProjectState(ProjectState projectState) {
        try {
            String serializedProjectState = serialize(projectState);

            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(projectState.getProjectFile()), StandardCharsets.UTF_8));
            writer.write(serializedProjectState);
            writer.close();
        } catch (IOException ex) {
            // TODO: 30.12.2019 logging
            System.out.println("Exception occurred!");
        }
    }

    @Override
    public void saveSql(File file, String text) {
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

            writer.write(text);
            writer.close();
        } catch (IOException ex) {
            // TODO: 30.12.2019 logging
            System.out.println("Exception occurred!");
        }
    }

    private String serialize(ProjectState projectState) throws JsonProcessingException {
        SerializableProjectState serializableProjectState = serializableProjectStateFactory.create(projectState);

        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(serializableProjectState);

        System.out.print(content);
        return content;
    }
}
