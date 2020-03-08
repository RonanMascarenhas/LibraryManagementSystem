package lms;

import java.io.Serializable;
import java.util.HashMap;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ArtifactLoanTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int number;
    private HashMap<Long, Long> artifactLoanTable = new HashMap<Long, Long>();

    public ArtifactLoanTable (int number) {
        this.number = number;
    }
    
    public ArtifactLoanTable () {
    }

    //HashMap<Long, Long> userLoanTable = new HashMap<Long, Long>();

    public long getId() {
        return id;
    }

    public void setId(long id)   {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number)   {
        this.number = number;
    }

    public Long getLoanOfArtifact(Long artifactid) {
        return artifactLoanTable.get(artifactid);
    }

    public void setLoanOfArtifact(Long loanid, Long artifactid)   {
        artifactLoanTable.put(loanid, artifactid);
    }

}