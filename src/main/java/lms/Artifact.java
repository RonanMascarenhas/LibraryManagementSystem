package lms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
//@PersistenceConstructor
public class Artifact{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    //private Type contentType;
    private String name;
    //total number of artifacts

    /*public Artifact (long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Artifact () {
    }*/

    public long getId() {
        return id;
    }

    public void setId(long id)   {
        this.id = id;
    }

    /*public Type getContentType() {
        return contentType;
    }

    public void setContentType(Type contentType)   {
        this.contentType = contentType;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name)   {
        this.name = name;
    }
}