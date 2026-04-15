package com.islamicapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.islamicapp.model.Azkar;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AzkarService {

    private List<Azkar> azkarList;

    public AzkarService() {
        loadAzkarData();
    }

    private void loadAzkarData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ClassPathResource resource = new ClassPathResource("static/data/azkar.json");
            azkarList = mapper.readValue(resource.getInputStream(), new TypeReference<List<Azkar>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Azkar> getAllAzkar() {
        return azkarList;
    }

    public Azkar getAzkarByCategory(String category) {
        return azkarList.stream().filter(a -> a.getCategory().equalsIgnoreCase(category)).findFirst().orElse(null);
    }
}