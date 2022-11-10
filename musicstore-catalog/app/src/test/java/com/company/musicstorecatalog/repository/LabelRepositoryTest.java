package com.company.musicstorecatalog.repository;

import com.company.musicstorecatalog.model.Label;
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
public class LabelRepositoryTest {
    @Autowired
    LabelRepository labelRepository;

    @Before
    public void setUp() throws Exception {
        labelRepository.deleteAll();
    }

    @After
    public void cleanUp() {
        labelRepository.deleteAll();
    }

    @Test
    public void shouldAddFindDeleteLabel() {
        //Arrange
        Label newLabel = new Label();
        newLabel.setName("Capital Records");
        newLabel.setWebsite("www.capital-records.com");

        //Act
        newLabel = labelRepository.save(newLabel);

        Optional<Label> foundLabel = labelRepository.findById(newLabel.getLabelId());

        //Assert
        assertTrue(foundLabel.isPresent());

        //Arrange
        newLabel.setName("Sony Music");
        newLabel.setWebsite("www.sony-music");

        //Act
        labelRepository.save(newLabel);
        foundLabel = labelRepository.findById(newLabel.getLabelId());

        //Assert
        assertTrue(foundLabel.isPresent());

        //Act
        labelRepository.deleteById(newLabel.getLabelId());
        foundLabel = labelRepository.findById(newLabel.getLabelId());

        //Assert
        assertFalse(foundLabel.isPresent());
    }

    @Test
    public void shouldFindAllLabel() {
        //Arrange
        Label label1 = new Label();
        label1.setName("Red Hill Records");
        label1.setWebsite("www.redhillrecords.com");

        Label label2 = new Label();
        label2.setName("Atlantic Records");
        label2.setWebsite("www.atlanticrecords.com");

        //Act
        label1 = labelRepository.save(label1);
        label2 = labelRepository.save(label2);
        List<Label> allLabel = new ArrayList();
        allLabel.add(label1);
        allLabel.add(label2);

        //Act
        List<Label> foundAllLabel = labelRepository.findAll();

        //Assert
        assertEquals(allLabel.size(), foundAllLabel.size());
    }
}