package lms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PublicUser {
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
}