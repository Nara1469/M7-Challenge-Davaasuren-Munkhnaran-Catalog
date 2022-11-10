package com.company.musicstorecatalog.repository;

import com.company.musicstorecatalog.model.Artist;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArtistRepositoryTest {
    @Autowired
    ArtistRepository artistRepository;

    @Before
    public void setUp() throws Exception {
        artistRepository.deleteAll();
    }

    @After
    public void cleanUp() {
        artistRepository.deleteAll();
    }

    @Test
    public void shouldAddFindDeleteArtist() {
        //Arrange
        Artist newArtist = new Artist();
        newArtist.setName("Celine Dion");
        newArtist.setInstagram("@CelineDion");
        newArtist.setTwitter("@CelineDion");

        //Act
        newArtist = artistRepository.save(newArtist);

        Optional<Artist> foundArtist = artistRepository.findById(newArtist.getArtistId());

        //Assert
        assertTrue(foundArtist.isPresent());

        //Arrange
        newArtist.setInstagram("@CelineDionInstaGram");
        newArtist.setTwitter("@CelineDionTwitter");

        //Act
        artistRepository.save(newArtist);
        foundArtist = artistRepository.findById(newArtist.getArtistId());

        //Assert
        assertTrue(foundArtist.isPresent());

        //Act
        artistRepository.deleteById(newArtist.getArtistId());
        foundArtist = artistRepository.findById(newArtist.getArtistId());

        //Assert
        assertFalse(foundArtist.isPresent());
    }

    @Test
    public void shouldFindAllArtist() {
        //Arrange
        Artist artist1 = new Artist();
        artist1.setName("Celine Dion");
        artist1.setInstagram("@CelineDion");
        artist1.setTwitter("@CelineDion");

        Artist artist2 = new Artist();
        artist2.setName("Micheal Jackson");
        artist2.setInstagram("@MichealJackson");
        artist2.setTwitter("");

        //Act
        artist1 = artistRepository.save(artist1);
        artist2 = artistRepository.save(artist2);
        List<Artist> allArtist = new ArrayList();
        allArtist.add(artist1);
        allArtist.add(artist2);

        //Act
        List<Artist> foundAllArtist = artistRepository.findAll();

        //Assert
        assertEquals(allArtist.size(), foundAllArtist.size());
    }
}