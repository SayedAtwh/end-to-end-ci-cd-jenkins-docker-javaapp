package com.islamicapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.islamicapp.model.Surah;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class QuranService {

    private List<Surah> surahs;

    public QuranService() {
        loadQuranData();
    }

    private void loadQuranData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ClassPathResource resource = new ClassPathResource("static/data/quran.json");
            surahs = mapper.readValue(resource.getInputStream(), new TypeReference<List<Surah>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Surah> getAllSurahs() {
        return surahs;
    }

    public Surah getSurahByNumber(int number) {
        return surahs.stream().filter(s -> s.getNumber() == number).findFirst().orElse(null);
    }
}