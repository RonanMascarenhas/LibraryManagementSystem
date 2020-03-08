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
    private String type;
    private String name;
    //total number of artifacts

    public long getId() {
        return id;
    }

    public void setId(long id)   {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type)   {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)   {
        this.name = name;
    }
}