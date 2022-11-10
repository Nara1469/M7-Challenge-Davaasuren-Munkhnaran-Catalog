package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/artist")
public class ArtistController {
    @Autowired
    ArtistRepository artistRepository;

    @GetMapping()
    public List<Artist> getArtists() {
        List<Artist> artistList = artistRepository.findAll();
        if (artistList.isEmpty() || artistList == null) {
            throw new IllegalArgumentException("Artists data is empty!");
        }
        return artistList; }

    @GetMapping("/{id}")
    public Artist getArtistById(@PathVariable Integer id) {
        Optional<Artist> returnVal = artistRepository.findById(id);
        if (returnVal.isPresent()) {
            return returnVal.get();
        } else {
            throw new IllegalArgumentException("No artist was found with Id: " + id);
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Artist addArtist(@RequestBody @Valid Artist artist) {
        if (artist==null) throw new IllegalArgumentException("No Artist data is added! Artist object is null!");
        return artistRepository.save(artist);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateArtist(@RequestBody @Valid Artist artist) {
        // validate incoming Artist data
        if (artist==null)
            throw new IllegalArgumentException("No Artist data is passed! Artist object is null!");

        //make sure the artist exists. and if not, throw exception...
        if (artist.getArtistId()==null)
            throw new IllegalArgumentException("No such artist to update.");

        artistRepository.save(artist);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtist(@PathVariable Integer id) {
        Optional<Artist> artist = artistRepository.findById(id);
        if(artist.isPresent()) {
            artistRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No artist was found with Id: " + id);
        }
    }
}
