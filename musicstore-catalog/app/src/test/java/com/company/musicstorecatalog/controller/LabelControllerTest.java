package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.repository.LabelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LabelController.class)
public class LabelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper mapper;

    Label inputLabel = new Label();
    Label outputLabel = new Label();

    String inputJson;
    String outputJson;

    @Before
    public void setUp() throws Exception {

        inputJson = null;
        outputJson=null;

        inputLabel.setName("Sony Music");
        inputLabel.setWebsite("www.sony-music.com");

        inputJson = mapper.writeValueAsString(inputLabel);

        outputLabel.setName("Sony Music");
        outputLabel.setWebsite("www.sony-music.com");
        outputLabel.setLabelId(20);

        outputJson = mapper.writeValueAsString(outputLabel);
    }

    @Test
    public void shouldAddLabel() throws  Exception{
        doReturn(outputLabel).when(labelRepository).save(inputLabel);

        //Act & Assert
        this.mockMvc.perform(post("/label")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldGetAllLabels() throws Exception {
        List<Label> labelList = new ArrayList<>();
        labelList.add(outputLabel);

        outputJson = mapper.writeValueAsString(labelList);

        doReturn(labelList).when(labelRepository).findAll();

        //Act & Assert
        this.mockMvc.perform(get("/label"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getLabelById() throws Exception {
        doReturn(Optional.of(outputLabel)).when(labelRepository).findById(20);

        //Act & Assert
        this.mockMvc.perform(get("/label/20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldUpdateLabel() throws Exception {
        doReturn(null).when(labelRepository).save(outputLabel);

        //Act & Assert
        this.mockMvc.perform(put("/label")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteLabel() throws Exception {
        doReturn(Optional.ofNullable(outputLabel)).when(labelRepository).findById(20);

        //Act & Assert
        this.mockMvc.perform(delete("/label/20"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // ------------ NotFound Status Tests -------------

    @Test
    public void shouldStatus404ForNonExistentLabelId() throws Exception{
        doReturn(Optional.empty()).when(labelRepository).findById(101);

        mockMvc.perform(
                        get("/label/101")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenReadEmptyLabelTable() throws Exception{
        List<Label> emptyList = new ArrayList();

        doReturn(emptyList).when(labelRepository).findAll();

        mockMvc.perform(
                        get("/label")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenUpdateLabelIdNotFound() throws Exception {
        Label anyLabel = new Label();
        anyLabel.setName("Capital Records");
        anyLabel.setWebsite("www.capital-records.com");
        anyLabel.setLabelId(101);

        doReturn(null).when(labelRepository).save(anyLabel);

        //Act & Assert
        this.mockMvc.perform(put("/label")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenDeleteLabelIdNotFound() throws Exception{
        doReturn(Optional.ofNullable(null)).when(labelRepository).findById(101);
        mockMvc.perform(
                        get("/label/101")
                )
                .andExpect(status().isNotFound());
    }

    // ------------ UnProcessableEntity Status Tests -------------

    @Test
    public void shouldReturn422WhenAddLabelDataNotValid() throws Exception{
        Label newLabel = new Label();
        newLabel.setName(null); // name is required
        newLabel.setWebsite("");

        outputJson = mapper.writeValueAsString(newLabel);

        doReturn(null).when(labelRepository).save(newLabel);

        mockMvc.perform(post("/label")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturn422WhenUpdateLabelDataNotValid() throws Exception{
        Label updatingLabel = new Label();
        updatingLabel.setName("Unknown Label");
        updatingLabel.setWebsite("");
        updatingLabel.setLabelId(10);

        doReturn(updatingLabel).when(labelRepository).save(updatingLabel);

        updatingLabel.setName(null); // name is required

        outputJson = mapper.writeValueAsString(updatingLabel);

        doReturn(null).when(labelRepository).save(updatingLabel);

        mockMvc.perform(put("/label")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}