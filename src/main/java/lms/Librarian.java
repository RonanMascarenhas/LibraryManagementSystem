package lms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Librarian{ 

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    private Role role; 
    
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(){
        this.name=name;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(){
        this.password=password;
    }

    public Role getRole(){
        return role;
    }

    public void setRole(){
        this.role=role;
    }
}