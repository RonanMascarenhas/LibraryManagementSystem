package lms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Artifact{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Type contentType;
    private String name;
    //total number of artifacts

    public long getId() {
        return id;
    }

    public void setId(long id)   {
        this.id = id;
    }

    public Type getContentType() {
        return contentType;
    }

    public void setContentType(Type contentType)   {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)   {
        this.name = name;
    }
}