package com.kaanege.iae.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaanege.iae.model.Configuration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigurationFileService {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean exportConfiguration(File file, Configuration configuration) {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(configuration, writer);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Configuration importConfiguration(File file) {
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, Configuration.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}