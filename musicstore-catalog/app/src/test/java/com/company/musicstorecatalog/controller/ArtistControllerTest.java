package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.repository.ArtistRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ArtistController.class)
public class ArtistControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistRepository artistRepository;

    @Autowired
    private ObjectMapper mapper;

    Artist inputArtist = new Artist();
    Artist outputArtist = new Artist();

    String inputJson;
    String outputJson;

    @Before
    public void setUp() throws Exception {

        inputJson = null;
        outputJson=null;

        inputArtist.setName("Celine Dion");
        inputArtist.setInstagram("@CelineDion");
        inputArtist.setTwitter("@CelineDion");

        inputJson = mapper.writeValueAsString(inputArtist);

        outputArtist.setName("Celine Dion");
        outputArtist.setInstagram("@CelineDion");
        outputArtist.setTwitter("@CelineDion");
        outputArtist.setArtistId(5);

        outputJson = mapper.writeValueAsString(outputArtist);
    }

    @Test
    public void shouldAddArtist() throws  Exception{
        doReturn(outputArtist).when(artistRepository).save(inputArtist);

        //Act & Assert
        this.mockMvc.perform(post("/artist")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldGetAllArtists() throws Exception {
        List<Artist> artistList = new ArrayList<>();
        artistList.add(outputArtist);

        outputJson = mapper.writeValueAsString(artistList);

        doReturn(artistList).when(artistRepository).findAll();

        //Act & Assert
        this.mockMvc.perform(get("/artist"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getArtistById() throws Exception {
        doReturn(Optional.of(outputArtist)).when(artistRepository).findById(5);

        //Act & Assert
        this.mockMvc.perform(get("/artist/5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldUpdateArtist() throws Exception {
        doReturn(null).when(artistRepository).save(outputArtist);

        //Act & Assert
        this.mockMvc.perform(put("/artist")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteArtist() throws Exception {
        doReturn(Optional.ofNullable(outputArtist)).when(artistRepository).findById(5);

        //Act & Assert
        this.mockMvc.perform(delete("/artist/5"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // ------------ NotFound Status Tests -------------

    @Test
    public void shouldStatus404ForNonExistentArtistId() throws Exception{
        doReturn(Optional.empty()).when(artistRepository).findById(101);

        mockMvc.perform(
                        get("/artist/101")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenReadEmptyArtistTable() throws Exception{
        List<Artist> emptyList = new ArrayList();

        doReturn(emptyList).when(artistRepository).findAll();

        mockMvc.perform(
                        get("/artist")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenUpdateArtistIdNotFound() throws Exception {
        Artist anyArtist = new Artist();
        anyArtist.setName("Eagles");
        anyArtist.setInstagram("@EaglesLegends");
        anyArtist.setTwitter("@EaglesLegends");
        anyArtist.setArtistId(101);

        doReturn(null).when(artistRepository).save(anyArtist);

        //Act & Assert
        this.mockMvc.perform(put("/artist")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenDeleteArtistIdNotFound() throws Exception{
        doReturn(Optional.ofNullable(null)).when(artistRepository).findById(101);
        mockMvc.perform(
                        get("/artist/101")
                )
                .andExpect(status().isNotFound());
    }

    // ------------ UnProcessableEntity Status Tests -------------

    @Test
    public void shouldReturn422WhenAddArtistDataNotValid() throws Exception{
        Artist newArtist = new Artist();
        newArtist.setName(null); // name is required
        newArtist.setInstagram("");
        newArtist.setTwitter("");

        outputJson = mapper.writeValueAsString(newArtist);

        doReturn(null).when(artistRepository).save(newArtist);

        mockMvc.perform(post("/artist")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturn422WhenUpdateArtistDataNotValid() throws Exception{
        Artist updatingArtist = new Artist();
        updatingArtist.setName("Unknown Artist");
        updatingArtist.setInstagram("");
        updatingArtist.setTwitter("");
        updatingArtist.setArtistId(10);

        doReturn(updatingArtist).when(artistRepository).save(updatingArtist);

        updatingArtist.setName(null); // name is required

        outputJson = mapper.writeValueAsString(updatingArtist);

        doReturn(null).when(artistRepository).save(updatingArtist);

        mockMvc.perform(put("/artist")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}