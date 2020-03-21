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
    private long loanid;
    private long artifactid;           //tracks id of item being loaned
    private long userid;            //tracks id of user who's loaned it
    private boolean reserved = false;
    private boolean loaned = false;
    @CreationTimestamp
    private Date dateLoaned;
    private Date dueDate;         //store date in ddmmyy format

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