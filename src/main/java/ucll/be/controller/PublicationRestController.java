package ucll.be.controller;

import org.springframework.web.bind.annotation.*;
import ucll.be.model.Publication;
import ucll.be.repository.PublicationRepository;
import ucll.be.service.PublicationService;

import java.util.List;


@RestController
public class PublicationRestController {
    private final PublicationService publicationService;

    public PublicationRestController(PublicationService publicationService){
        this.publicationService = publicationService;
    }
    @GetMapping("/publications")
    public List<Publication> getPublication(@RequestParam(required = false) String title, @RequestParam(required = false) String type){
        return publicationService.findPublicationByTitleAndType(title, type);
    }

    @GetMapping("/publications/stock/{availableCopies}")
    public List<Publication> getAvailable(@PathVariable("availableCopies") int copies){
        if(copies < 0) {
            throw new RuntimeException("Copies went to a negative this is an internal error.");
        }
        return publicationService.filterPublication(copies);
    }

    @GetMapping("/publication/stock/{number}")
    public List<Publication> getStocked(@PathVariable(name = "number", required = false) Integer number){
        if ((number <0) && number != null){
            throw new RuntimeException("");
        }
         return publicationService.filterPublication(number);
    }
}
