package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Ivan Brovkin", "для прохождения практики",
                LocalDateTime.of(2024, Month.APRIL, 11, 11, 11, 11), true, 1, 0));
        save(new Candidate(0, "Matvei Sinicin", "джуниор",
                LocalDateTime.of(2024, Month.MAY, 15, 12, 33, 29), true, 2, 0));
        save(new Candidate(0, "Leonid Khaibullin", "крепкий мидл после job4j",
                LocalDateTime.of(2024, Month.JUNE, 28, 9, 48, 14), true, 2, 0));
        save(new Candidate(0, "Pavel Yarsev", "опыт мидлом не менее 2 лет",
                LocalDateTime.of(2024, Month.JULY, 25, 8, 20, 35), true, 4, 0));
        save(new Candidate(0, "Reso Nashvili", "имеет скилы синьора",
                LocalDateTime.of(2024, Month.JULY, 1, 9, 0, 3), true, 3, 0));
        save(new Candidate(0, "Petr Arsentiev", "тим лид",
                LocalDateTime.of(2024, Month.JULY, 22, 12, 12, 25), true, 4, 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
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
                        candidate.getDescription(), candidate.getCreationDate(),
                        candidate.getVisible(), candidate.getCityId(), candidate.getFileId())) != null;
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
