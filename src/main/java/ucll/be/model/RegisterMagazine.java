package ucll.be.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Magazine")
public class RegisterMagazine extends Publication {
    private String editor;
    private String ISSN;

    public RegisterMagazine(){
        super();
    }

    public RegisterMagazine(String title, int publicationYear, int available, String editor, String ISSN){
        super(title,publicationYear,available);
        setEditor(editor);
        setISSN(ISSN);
    }

    public void setEditor(String editor) {
        if (editor == null || editor.trim().isEmpty()) {
            throw new RuntimeException("Editor is required and cannot be empty.");
        }
        this.editor = editor;
    }

    public void setISSN(String ISSN) {
        if (ISSN == null || ISSN.trim().isEmpty()){
            throw new RuntimeException("ISSN can't be empty and is required must be 8 numbers.");
        }

        if (!ISSN.matches("\\d{4}-\\d{4}")){
            throw new RuntimeException("ISSN must be in the format xxxx-xxxx, where x is a digit.");
        }

        this.ISSN = ISSN;
    }

    public String getEditor(){
        return editor;
    }

    public String getISSN(){
        return ISSN;
    }

    @Override
    public String getType(){
        return "Magazine: " + getTitle() + "ISSN: " + getISSN() + "Editor: " + getEditor();
    }

}
