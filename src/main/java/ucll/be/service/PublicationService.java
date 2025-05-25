package ucll.be.service;

import org.springframework.stereotype.Service;
import ucll.be.model.Publication;
import ucll.be.repository.PublicationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicationService {
    private final PublicationRepository publicationRepository;

    public PublicationService(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    public List<Publication> getAllPublications(){
        return publicationRepository.findAll();
    }

    public List<Publication> searchByTitle(String title){
        return publicationRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Publication> getAvailablePublication(){
        return publicationRepository.findByAvailableCopiesGreaterThan(0);
    }

    public List<Publication> findPublicationByTitleAndType(String title, String type) {
        List<Publication> all = publicationRepository.findAll();
        List<Publication> filtered = new ArrayList<>();

        for(Publication p : all) {
                boolean matchesTitle = title == null || title.isEmpty()
                        || p.getTitle().toLowerCase().contains(title.toLowerCase());
                boolean matchesType = (type == null || type.isEmpty())
                        || p.getType().equalsIgnoreCase(type);
                if (matchesType && matchesTitle) {
                    filtered.add(p);
                }
        }
          if(filtered.isEmpty())

        {
            throw new RuntimeException("No publications match the provided filter.");
        }
          return filtered;
    }
    
    public List<Publication> filterPublication(Integer copies){
         if(copies == null){
             return publicationRepository.findAll()
                     .stream().filter(p -> p.getAvailableCopies() > 0)
                     .collect(Collectors.toList());
         }
        return publicationRepository.findAll().stream()
                .filter(pub -> pub.getAvailableCopies() >= copies)
                .collect(Collectors.toList());
    }


}
