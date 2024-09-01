package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();

    private int nextId = 1;

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Ivan Brovkin", "для прохождения практики",
                LocalDateTime.of(2024, Month.APRIL, 11, 11, 11, 11)));
        save(new Candidate(0, "Matvei Sinicin", "джуниор",
                LocalDateTime.of(2024, Month.MAY, 15, 12, 33, 29)));
        save(new Candidate(0, "Leonid Khaibullin", "крепкий мидл после job4j",
                LocalDateTime.of(2024, Month.JUNE, 28, 9, 48, 14)));
        save(new Candidate(0, "Pavel Yarsev", "опыт мидлом не менее 2 лет",
                LocalDateTime.of(2024, Month.JULY, 25, 8, 20, 35)));
        save(new Candidate(0, "Reso Nashvili", "имеет скилы синьора",
                LocalDateTime.of(2024, Month.JULY, 1, 9, 0, 3)));
        save(new Candidate(0, "Petr Arsentiev", "тим лид",
                LocalDateTime.of(2024, Month.JULY, 22, 12, 12, 25)));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) -> new Candidate(oldCandidate.getId(), candidate.getName(),
                        candidate.getDescription(), candidate.getCreationDate())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
