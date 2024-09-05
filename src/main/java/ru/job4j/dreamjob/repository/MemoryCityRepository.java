package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.City;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryCityRepository implements CityRepository {
    private final Map<Integer, City> cities = new HashMap<>() {
        {
            put(1, new City(1, "Москва"));
            put(2, new City(2, "Санкт-Петербург"));
            put(3, new City(3, "Екатеринбург"));
            put(4, new City(4, "Казань"));
        }
    };

    @Override
    public Collection<City> findAll() {
        return cities.values();
    }
}
