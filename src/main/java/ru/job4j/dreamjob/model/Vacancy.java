package ru.job4j.dreamjob.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Vacancy {

    private int id;
    private String title;
    private String description;
    private LocalDateTime creationDate = LocalDateTime.now();
    private int cityId;
    private boolean visible;

    public Vacancy(int id, String title, String description, LocalDateTime creationDate, boolean visible,
                   int cityId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.visible = visible;
        this.cityId = cityId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vacancy vacancy = (Vacancy) o;
        return id == vacancy.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
