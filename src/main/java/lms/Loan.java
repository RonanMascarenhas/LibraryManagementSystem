package lms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import java.util.Calendar;
//import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.Duration;
import java.time.Instant;

@Entity
//@PersistenceConstructor
public class Loan{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long loanID;
    private long artifactid;           //tracks id of item being loaned
    private String artifactName;
    private String artifactType;
    private long userLoanedid;            //tracks id of user who's loaned it
    private long userReservedid;        //tracks id of user who's reserved it
    private boolean reserved = false;
    private boolean loaned = false;
    @CreationTimestamp
    private Date dateLoaned;
    private Date dueDate;         //store date in ddmmyy format

    public long getLoanid() {
        return loanID;
    }
    public void setLoanid(long loanID)   {
        this.loanID = loanID;
    }

    public long getArtifactid() {
        return artifactid;
    }

    public void setArtifactid(long artifactid)   {
        this.artifactid = artifactid;
    }

    public String getArtifactName() {
        return artifactName;
    }

    public void setArtifactName(String artifactName)   {
        this.artifactName = artifactName;
    }

    public String getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(String artifactType)   {
        this.artifactType = artifactType;
    }

    public long getUserLoanedid() {
        return userLoanedid;
    }

    public void setUserLoanedid(long userLoanedid)   {
        this.userLoanedid = userLoanedid;
    }

    public long getUserReservedid() {
        return userReservedid;
    }

    public void setUserReservedid(long userReservedid)   {
        this.userReservedid = userReservedid;
    }
    
    public boolean getReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved)   {
        this.reserved = reserved;
    }

    public boolean getLoaned() {
        return loaned;
    }

    public void setLoaned(boolean loaned)   {
        this.loaned = loaned;
    }

    public Date getDateLoaned() {
        return dateLoaned;
    }

    public void setDateLoaned(Date dateLoaned)   {
        this.dateLoaned = dateLoaned;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate()   {
        int addDays = 14;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateLoaned);
        calendar.add(Calendar.DAY_OF_YEAR, addDays);
        this.dueDate = calendar.getTime();
        
        //this.dueDate = dueDate;
    }

    //Date.from(dateLoaned.toInstant().plus(Duration.ofDays(14))); 
}