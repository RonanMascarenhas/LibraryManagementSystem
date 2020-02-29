//package ;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PublicUser {
<<<<<<< HEAD
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int publicUserID;
    private String publicUsername;


public int getPublicUserID() {
    return publicUserID;
}

public void setPublicUserID(int publicUserID) {
    this.publicUserID = publicUserID;
}

public String getPublicUsername() {
    return publicUsername;
}

public void setpublicUsername(String publicUsername) {
    this.publicUsername = publicUsername;
}
=======
     private int id;
 
  

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
>>>>>>> 71a74f6d319411a51488d728b6f8c41bb5d021ea
}