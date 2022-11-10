package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/album")
public class AlbumController {
        @Autowired
        AlbumRepository albumRepository;

        @GetMapping()
        public List<Album> getAlbums() {
            List<Album> albumList = albumRepository.findAll();
            if (albumList.isEmpty() || albumList == null) {
                throw new IllegalArgumentException("Albums data is empty!");
            }
            return albumList; }

        @GetMapping("/{id}")
        public Album getAlbumById(@PathVariable Integer id) {
            Optional<Album> returnVal = albumRepository.findById(id);
            if (returnVal.isPresent()) {
                return returnVal.get();
            } else {
                throw new IllegalArgumentException("No album was found with Id: " + id);
            }
        }

        @PostMapping()
        @ResponseStatus(HttpStatus.CREATED)
        public Album addAlbum(@RequestBody @Valid Album album) {
            if (album==null) throw new IllegalArgumentException("No Album data is added! Album object is null!");
            return albumRepository.save(album);
        }

        @PutMapping()
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void updateAlbum(@RequestBody @Valid Album album) {
            // validate incoming Album data
            if (album==null)
                throw new IllegalArgumentException("No Album data is passed! Album object is null!");

            //make sure the album exists. and if not, throw exception...
            if (album.getAlbumId()==null)
                throw new IllegalArgumentException("No such album to update.");

            albumRepository.save(album);
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteAlbum(@PathVariable Integer id) {
            Optional<Album> album = albumRepository.findById(id);
            if(album.isPresent()) {
                albumRepository.deleteById(id);
            } else {
                throw new IllegalArgumentException("No album was found with Id: " + id);
            }
        }

    }
