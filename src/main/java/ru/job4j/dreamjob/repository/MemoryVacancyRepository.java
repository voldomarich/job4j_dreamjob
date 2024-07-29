package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryVacancyRepository implements VacancyRepository {

    private static final MemoryVacancyRepository INSTANCE = new MemoryVacancyRepository();

    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "для прохождения практики",
                LocalDateTime.of(2024, Month.APRIL, 11, 15, 30, 30)));
        save(new Vacancy(0, "Junior Java Developer", "хорошая теоретическая база",
                LocalDateTime.of(2024, Month.MAY, 15, 11, 11, 11)));
        save(new Vacancy(0, "Junior+ Java Developer", "надо быть мидлом по факту",
                LocalDateTime.of(2024, Month.JUNE, 14, 9, 48, 14)));
        save(new Vacancy(0, "Middle Java Developer", "опыт мидлом не менее 2 лет",
                LocalDateTime.of(2024, Month.JULY, 24, 8, 7, 59)));
        save(new Vacancy(0, "Middle+ Java Developer", "нужны скилы синьора",
                LocalDateTime.of(2024, Month.JULY, 1, 9, 0, 0)));
        save(new Vacancy(0, "Senior Java Developer", "нужен архитектор",
                LocalDateTime.of(2024, Month.JULY, 22, 12, 12, 25)));
    }

    public static MemoryVacancyRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public void deleteById(int id) {
        vacancies.remove(id);
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(oldVacancy.getId(), vacancy.getTitle(),
        vacancy.getDescription(), vacancy.getCreationDate())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
