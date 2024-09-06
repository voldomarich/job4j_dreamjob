package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryVacancyRepository implements VacancyRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "для прохождения практики",
                LocalDateTime.of(2024, Month.APRIL, 11, 15, 30, 30), true, 1, 0));
        save(new Vacancy(0, "Junior Java Developer", "хорошая теоретическая база",
                LocalDateTime.of(2024, Month.MAY, 15, 11, 11, 11), true, 2, 0));
        save(new Vacancy(0, "Junior+ Java Developer", "надо быть мидлом по факту",
                LocalDateTime.of(2024, Month.JUNE, 14, 9, 48, 14), true, 3, 0));
        save(new Vacancy(0, "Middle Java Developer", "опыт мидлом не менее 2 лет",
                LocalDateTime.of(2024, Month.JULY, 24, 8, 7, 59), true, 4, 0));
        save(new Vacancy(0, "Middle+ Java Developer", "нужны скилы синьора",
                LocalDateTime.of(2024, Month.JULY, 1, 9, 0, 0), true, 4, 0));
        save(new Vacancy(0, "Senior Java Developer", "нужен архитектор",
                LocalDateTime.of(2024, Month.JULY, 22, 12, 12, 25), true, 3, 0));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.incrementAndGet());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy) -> {
            return new Vacancy(oldVacancy.getId(), vacancy.getTitle(), vacancy.getDescription(),
                    vacancy.getCreationDate(), vacancy.getVisible(), vacancy.getCityId(), vacancy.getFileId());
        }) != null;
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
