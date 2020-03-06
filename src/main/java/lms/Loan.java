package lms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.format.DateTimeFormatter;

@Entity
//@PersistenceConstructor
public class Loan{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long loanid;
    private long artifactid;           //tracks id of item being loaned
    private long userid;            //tracks id of user who's loaned it
    private DateTimeFormatter returnDate;              //store date in ddmmyy format
    private boolean reloaned;
    //private Type contentType;
    //total number of artifacts

    /*public Artifact (long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Artifact () {
    }*/

    public long getLoanid() {
        return loanid;
    }

    public void setLoanid(long loanid)   {
        this.loanid = loanid;
    }

    public long getArtifactid() {
        return artifactid;
    }

    public void setArtifactid(long artifactid)   {
        this.artifactid = artifactid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid)   {
        this.userid = userid;
    }

    public DateTimeFormatter getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(DateTimeFormatter returnDate)   {
        this.returnDate = returnDate;
    }

    public boolean getReloaned() {
        return reloaned;
    }

    public void setReloaned(boolean reloaned)   {
        this.reloaned = reloaned;
    }
}