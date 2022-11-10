package com.company.musicstorecatalog.repository;

import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.model.Track;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrackRepositoryTest {
    @Autowired
    TrackRepository trackRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    LabelRepository labelRepository;

    Artist newArtist = new Artist();
    Label newLabel = new Label();
    Album newAlbum = new Album();

    @Before
    public void setUp() throws Exception {
        trackRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();
        labelRepository.deleteAll();

        newArtist.setName("Celine Dion");
        newArtist.setInstagram("@CelineDion");
        newArtist.setTwitter("@CelineDion");

        newArtist = artistRepository.save(newArtist);

        newLabel.setName("Capital Records");
        newLabel.setWebsite("www.capital-records.com");

        newLabel = labelRepository.save(newLabel);

        newAlbum.setTitle("Falling Into You");
        newAlbum.setListPrice(new BigDecimal(19.99));
        newAlbum.setReleaseDate(LocalDate.parse("1996-01-01"));
        newAlbum.setArtistId(newArtist.getArtistId());
        newAlbum.setLabelId(newLabel.getLabelId());

        newAlbum = albumRepository.save(newAlbum);
    }

    @After
    public void cleanUp() {
        trackRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();
        labelRepository.deleteAll();
    }

    @Test
    public void shouldAddFindDeleteTrack() {
        //Arrange
        Track newTrack = new Track();
        newTrack.setTitle("Falling Into You");
        newTrack.setRunTime(50);
        newTrack.setAlbumId(newAlbum.getAlbumId());

        //Act
        newTrack = trackRepository.save(newTrack);

        Optional<Track> foundTrack = trackRepository.findById(newTrack.getTrackId());

        //Assert
        assertTrue(foundTrack.isPresent());

        //Arrange
        newTrack.setTitle("Falling Into You");
        newTrack.setRunTime(44);

        //Act
        trackRepository.save(newTrack);
        foundTrack = trackRepository.findById(newTrack.getTrackId());

        //Assert
        assertTrue(foundTrack.isPresent());

        //Act
        trackRepository.deleteById(newTrack.getTrackId());
        foundTrack = trackRepository.findById(newTrack.getTrackId());

        //Assert
        assertFalse(foundTrack.isPresent());
    }

    @Test
    public void shouldFindAllTrack() {
        //Arrange
        Track track1 = new Track();
        track1.setTitle("It's All Coming Back to Me Now");
        track1.setRunTime(10);
        track1.setAlbumId(newAlbum.getAlbumId());

        Track track2 = new Track();
        track2.setTitle("Because You Loved Me");
        track2.setRunTime(15);
        track2.setAlbumId(newAlbum.getAlbumId());

        //Act
        track1 = trackRepository.save(track1);
        track2 = trackRepository.save(track2);
        List<Track> allTrack = new ArrayList();
        allTrack.add(track1);
        allTrack.add(track2);

        //Act
        List<Track> foundAllTrack = trackRepository.findAll();

        //Assert
        assertEquals(allTrack.size(), foundAllTrack.size());
    }
}