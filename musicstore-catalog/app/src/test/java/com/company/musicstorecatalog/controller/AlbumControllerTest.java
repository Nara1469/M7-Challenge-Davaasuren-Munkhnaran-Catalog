package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.repository.AlbumRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
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
@WebMvcTest(AlbumController.class)
public class AlbumControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumRepository albumRepository;

    @Autowired
    private ObjectMapper mapper;

    Album inputAlbum = new Album();
    Album outputAlbum = new Album();

    String inputJson;
    String outputJson;

    @Before
    public void setUp() throws Exception {

        inputJson = null;
        outputJson=null;

        inputAlbum.setTitle("Bad");
        inputAlbum.setListPrice(new BigDecimal(19.99));
        inputAlbum.setReleaseDate(LocalDate.parse("1987-01-01"));
        inputAlbum.setArtistId(5);
        inputAlbum.setLabelId(20);

        inputJson = mapper.writeValueAsString(inputAlbum);

        outputAlbum.setTitle("Bad");
        outputAlbum.setListPrice(new BigDecimal(19.99));
        outputAlbum.setReleaseDate(LocalDate.parse("1987-01-01"));
        outputAlbum.setArtistId(5);
        outputAlbum.setLabelId(20);
        outputAlbum.setAlbumId(1);

        outputJson = mapper.writeValueAsString(outputAlbum);
    }

    @Test
    public void shouldAddAlbum() throws  Exception{
        doReturn(outputAlbum).when(albumRepository).save(inputAlbum);

        //Act & Assert
        this.mockMvc.perform(post("/album")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldGetAllAlbums() throws Exception {
        List<Album> albumList = new ArrayList<>();
        albumList.add(outputAlbum);

        outputJson = mapper.writeValueAsString(albumList);

        doReturn(albumList).when(albumRepository).findAll();

        //Act & Assert
        this.mockMvc.perform(get("/album"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getAlbumById() throws Exception {
        doReturn(Optional.of(outputAlbum)).when(albumRepository).findById(1);

        //Act & Assert
        this.mockMvc.perform(get("/album/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldUpdateAlbum() throws Exception {
        doReturn(null).when(albumRepository).save(outputAlbum);

        //Act & Assert
        this.mockMvc.perform(put("/album")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteAlbum() throws Exception {
        doReturn(Optional.ofNullable(outputAlbum)).when(albumRepository).findById(1);

        //Act & Assert
        this.mockMvc.perform(delete("/album/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // ------------ NotFound Status Tests -------------

    @Test
    public void shouldStatus404ForNonExistentAlbumId() throws Exception{
        doReturn(Optional.empty()).when(albumRepository).findById(101);

        mockMvc.perform(
                        get("/album/101")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenReadEmptyAlbumTable() throws Exception{
        List<Album> emptyList = new ArrayList();

        doReturn(emptyList).when(albumRepository).findAll();

        mockMvc.perform(
                        get("/album")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenUpdateAlbumIdNotFound() throws Exception {
        Album anyAlbum = new Album();
        anyAlbum.setTitle("Thriller");
        anyAlbum.setListPrice(new BigDecimal(15.99));
        anyAlbum.setReleaseDate(LocalDate.parse("1982-01-01"));
        anyAlbum.setArtistId(5);
        anyAlbum.setLabelId(20);
        anyAlbum.setAlbumId(101);

        doReturn(null).when(albumRepository).save(anyAlbum);

        //Act & Assert
        this.mockMvc.perform(put("/album")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenDeleteAlbumIdNotFound() throws Exception{
        doReturn(Optional.ofNullable(null)).when(albumRepository).findById(101);
        mockMvc.perform(
                        get("/album/101")
                )
                .andExpect(status().isNotFound());
    }

    // ------------ UnProcessableEntity Status Tests -------------

    @Test
    public void shouldReturn422WhenAddAlbumDataNotValid() throws Exception{
        Album newAlbum = new Album();
        newAlbum.setTitle(null); // title is required
        newAlbum.setListPrice(new BigDecimal(0.00));
        newAlbum.setReleaseDate(LocalDate.parse("1987-01-01"));
        newAlbum.setArtistId(5);
        newAlbum.setLabelId(20);

        outputJson = mapper.writeValueAsString(newAlbum);

        doReturn(null).when(albumRepository).save(newAlbum);

        mockMvc.perform(post("/album")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturn422WhenUpdateAlbumDataNotValid() throws Exception{
        Album updatingAlbum = new Album();
        updatingAlbum.setTitle("Unknown Album"); // title is required
        updatingAlbum.setListPrice(new BigDecimal(0.00));
        updatingAlbum.setReleaseDate(LocalDate.parse("1900-01-01"));
        updatingAlbum.setArtistId(5);
        updatingAlbum.setLabelId(20);
        updatingAlbum.setAlbumId(2);

        doReturn(updatingAlbum).when(albumRepository).save(updatingAlbum);

        updatingAlbum.setTitle(null); // title is required

        outputJson = mapper.writeValueAsString(updatingAlbum);

        doReturn(null).when(albumRepository).save(updatingAlbum);

        mockMvc.perform(put("/album")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}