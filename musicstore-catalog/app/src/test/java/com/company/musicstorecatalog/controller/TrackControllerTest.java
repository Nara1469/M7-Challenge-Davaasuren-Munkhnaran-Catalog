package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Track;
import com.company.musicstorecatalog.repository.TrackRepository;
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
@WebMvcTest(TrackController.class)
public class TrackControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackRepository trackRepository;

    @Autowired
    private ObjectMapper mapper;

    Track inputTrack = new Track();
    Track outputTrack = new Track();

    String inputJson;
    String outputJson;

    @Before
    public void setUp() throws Exception {
        inputJson = null;
        outputJson=null;

        inputTrack.setTitle("Hotel California");
        inputTrack.setRunTime(30);
        inputTrack.setAlbumId(2);

        inputJson = mapper.writeValueAsString(inputTrack);

        outputTrack.setTitle("Hotel California");
        outputTrack.setRunTime(30);
        outputTrack.setAlbumId(2);
        outputTrack.setTrackId(11);

        outputJson = mapper.writeValueAsString(outputTrack);
    }

    @Test
    public void shouldAddTrack() throws  Exception{
        doReturn(outputTrack).when(trackRepository).save(inputTrack);

        //Act & Assert
        this.mockMvc.perform(post("/track")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldGetAllTracks() throws Exception {
        List<Track> trackList = new ArrayList<>();
        trackList.add(outputTrack);

        outputJson = mapper.writeValueAsString(trackList);

        doReturn(trackList).when(trackRepository).findAll();

        //Act & Assert
        this.mockMvc.perform(get("/track"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void getTrackById() throws Exception {
        doReturn(Optional.of(outputTrack)).when(trackRepository).findById(11);

        //Act & Assert
        this.mockMvc.perform(get("/track/11"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldUpdateTrack() throws Exception {
        doReturn(null).when(trackRepository).save(outputTrack);

        //Act & Assert
        this.mockMvc.perform(put("/track")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteTrack() throws Exception {
        doReturn(Optional.ofNullable(outputTrack)).when(trackRepository).findById(11);

        //Act & Assert
        this.mockMvc.perform(delete("/track/11"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // ------------ NotFound Status Tests -------------

    @Test
    public void shouldStatus404ForNonExistentTrackId() throws Exception{
        doReturn(Optional.empty()).when(trackRepository).findById(999);

        mockMvc.perform(
                        get("/track/999")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenReadEmptyTrackTable() throws Exception{
        List<Track> emptyList = new ArrayList();

        doReturn(emptyList).when(trackRepository).findAll();

        mockMvc.perform(
                        get("/track")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenUpdateTrackIdNotFound() throws Exception {
        Track anyTrack = new Track();
        inputTrack.setTitle("The Bodyguard");
        inputTrack.setRunTime(50);
        inputTrack.setAlbumId(10);
        anyTrack.setTrackId(999);

        doReturn(null).when(trackRepository).save(anyTrack);

        //Act & Assert
        this.mockMvc.perform(put("/track")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenDeleteTrackIdNotFound() throws Exception{
        doReturn(Optional.ofNullable(null)).when(trackRepository).findById(999);
        mockMvc.perform(
                        get("/track/999")
                )
                .andExpect(status().isNotFound());
    }

    // ------------ UnProcessableEntity Status Tests -------------

    @Test
    public void shouldReturn422WhenAddTrackDataNotValid() throws Exception{
        Track newTrack = new Track();
        newTrack.setTitle(null); // title is required
        newTrack.setRunTime(0); // runtime is greater than 0
        newTrack.setAlbumId(5);

        outputJson = mapper.writeValueAsString(newTrack);

        doReturn(null).when(trackRepository).save(newTrack);

        mockMvc.perform(post("/track")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturn422WhenUpdateTrackDataNotValid() throws Exception{
        Track updatingTrack = new Track();
        updatingTrack.setTitle("Unknown Track");
        updatingTrack.setRunTime(1);
        updatingTrack.setAlbumId(5);
        updatingTrack.setTrackId(53);

        doReturn(updatingTrack).when(trackRepository).save(updatingTrack);

        updatingTrack.setTitle(null); // title is required

        outputJson = mapper.writeValueAsString(updatingTrack);

        doReturn(null).when(trackRepository).save(updatingTrack);

        mockMvc.perform(put("/track")
                        .content(outputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}