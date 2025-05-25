package ucll.be.stub;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import ucll.be.model.Publication;
import ucll.be.repository.PublicationRepository;

import java.util.*;
import java.util.function.Function;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

public class PublicationRepositoryStub implements PublicationRepository {
    private final Map<Long, Publication> publications = new HashMap<>();
    private long nextId = 1;


    @Override
    public <S extends Publication> S save(S publication){
        if (publication.getId() == null){
            publication.setId(nextId++);
        }
        publications.put(publication.getId(), publication);
        return publication;
    }

    @Override
    public Optional<Publication> findById(Long id){
        return Optional.ofNullable(publications.get(id));
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public <S extends Publication> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<Publication> findAll(){
        return new ArrayList<>(publications.values());
    }

    @Override
    public List<Publication> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long id){
        publications.remove(id);
    }

    @Override
    public void delete(Publication entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Publication> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Publication> findByTitleContainingIgnoreCase(String title){
        List<Publication> list = new ArrayList<>();
        for (Publication p : publications.values()) {
            if (p.getTitle()
                    .toLowerCase()
                    .contains(title.toLowerCase())) {
                list.add(p);
            }
        }
        return list;
    }

    @Override
    public List<Publication> findByAvailableCopiesGreaterThan(int count){
        return publications.values().stream().filter(p -> p.getAvailableCopies() > count).collect(Collectors.toList());
    }

    @Override
    public List<Publication> findByClass(Class<? extends Publication> clazz) {
        return List.of();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Publication> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Publication> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Publication> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Publication getOne(Long aLong) {
        return null;
    }

    @Override
    public Publication getById(Long aLong) {
        return null;
    }

    @Override
    public Publication getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Publication> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Publication> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Publication> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Publication> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Publication> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Publication> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Publication, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Publication> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Publication> findAll(Pageable pageable) {
        return null;
    }
}
