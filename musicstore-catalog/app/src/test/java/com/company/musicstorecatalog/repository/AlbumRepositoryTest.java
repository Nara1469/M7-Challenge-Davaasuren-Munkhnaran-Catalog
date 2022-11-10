package com.company.musicstorecatalog.repository;

import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Label;
import org.junit.After;
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
public class AlbumRepositoryTest {
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    LabelRepository labelRepository;

    Artist newArtist = new Artist();
    Label newLabel = new Label();

    @Before
    public void setUp() throws Exception {
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
    }

    @After
    public void cleanUp() {
        albumRepository.deleteAll();
        artistRepository.deleteAll();
        labelRepository.deleteAll();
    }

    @Test
    public void shouldAddFindDeleteAlbum() {
        //Arrange
        Album newAlbum = new Album();
        newAlbum.setTitle("Falling Into You");
        newAlbum.setListPrice(new BigDecimal(19.99));
        newAlbum.setReleaseDate(LocalDate.parse("1996-01-01"));
        newAlbum.setArtistId(newArtist.getArtistId());
        newAlbum.setLabelId(newLabel.getLabelId());

        //Act
        newAlbum = albumRepository.save(newAlbum);

        Optional<Album> foundAlbum = albumRepository.findById(newAlbum.getAlbumId());

        //Assert
        assertTrue(foundAlbum.isPresent());

        //Arrange
        newAlbum.setListPrice(new BigDecimal(20.00));
        newAlbum.setReleaseDate(LocalDate.parse("1996-05-05"));

        //Act
        albumRepository.save(newAlbum);
        foundAlbum = albumRepository.findById(newAlbum.getAlbumId());

        //Assert
        assertTrue(foundAlbum.isPresent());

        //Act
        albumRepository.deleteById(newAlbum.getAlbumId());
        foundAlbum = albumRepository.findById(newAlbum.getAlbumId());

        //Assert
        assertFalse(foundAlbum.isPresent());
    }

    @Test
    public void shouldFindAllAlbum() {
        //Arrange
        Album album1 = new Album();
        album1.setTitle("Falling Into You");
        album1.setListPrice(new BigDecimal(19.99));
        album1.setReleaseDate(LocalDate.parse("1996-06-06"));
        album1.setArtistId(newArtist.getArtistId());
        album1.setLabelId(newLabel.getLabelId());

        Album album2 = new Album();
        album2.setTitle("Let's Talk About Love");
        album2.setListPrice(new BigDecimal(22.99));
        album2.setReleaseDate(LocalDate.parse("1997-07-07"));
        album2.setArtistId(newArtist.getArtistId());
        album2.setLabelId(newLabel.getLabelId());

        //Act
        album1 = albumRepository.save(album1);
        album2 = albumRepository.save(album2);
        List<Album> allAlbum = new ArrayList();
        allAlbum.add(album1);
        allAlbum.add(album2);

        //Act
        List<Album> foundAllAlbum = albumRepository.findAll();

        //Assert
        assertEquals(allAlbum.size(), foundAllAlbum.size());
    }
}