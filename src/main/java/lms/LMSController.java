package lms;

import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Iterator;
import java.util.List;
import java.util.Date;

import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lms.User;
import lms.Artifact;

@Controller
public class LMSController {//implements Iterable<T> {
    @Autowired
    private ArtifactRepository artifactRepository;
    @Autowired
    private UserSession userSession;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Library: Home");
        model.addAttribute("user", userSession.getUser());
        return "index.html";
    }

    @GetMapping("/guest_search")
    public String guest_search(Model model) {
        model.addAttribute("artifacts", artifactRepository.findAll());
        //enter name of artifact you want
        //match input name with db artifact name (search db)
        return "guest_search.html";
    }

    @GetMapping("/member_search")
    public String member_search(Model model) {

        //enter name of artifact you want
        //match input name with db artifact name (search db)

        return "member_search.html";  
    }

    @GetMapping("/librarian_search")
    public String librarian_search(Model model) {
        return "librarian_search.html";
    }
    
    @GetMapping("/search_results_ID")
    public String search_results_ID(@RequestParam(name="artifactID") Long artifactID, Model model)   {
        Optional artifactCheck;
        try
        {
            artifactCheck = artifactRepository.findById(artifactID);
        } 
        catch (Exception e)
        {
            model.addAttribute("message",
            "There were no matching results for your search");
            if(userSession.getUser() == null){
                return "guest_search_results.html";
            }
            else if (userSession.getUser().getRole().equals("member")){
                return "member_search_results.html";
            }
            else {
                return "librarian_search_results.html";
            }
        }

        if(artifactCheck.isPresent() == false) {
            model.addAttribute("message",
            "There were no matching results for your search" );
            if(userSession.getUser() == null){
                return "guest_search_results.html";
            }
            else if (userSession.getUser().getRole().equals("member")){
                return "member_search_results.html";
            }
            else {
                return "librarian_search_results.html";
            }
        }

        Artifact artifact = artifactRepository.getOne(artifactID);
        model.addAttribute("message","Match found:" );
        model.addAttribute("name", artifact.getName());
        model.addAttribute("artifact", artifact);
        if(userSession.getUser() == null){
            return "guest_search_results.html";
        }
        else if (userSession.getUser().getRole().equals("member")){
            return "member_search_results.html";
        }
        else {
            return "librarian_search_results.html";
        }
    }

    @GetMapping("/search_results_type")
    public String search_results_type(Model model, @RequestParam(name="artifactType") String artifactType)   {
        
        Artifact artifactCheck = artifactRepository.findByType(artifactType);

        if(artifactCheck != null){
            model.addAttribute("message","Match found:" );
            model.addAttribute("name", artifactCheck.getName());
            model.addAttribute("type", artifactCheck.getType());
            model.addAttribute("artifact", artifactCheck);
            if(userSession.getUser() == null){
                return "guest_search_results.html";
            }
            else if (userSession.getUser().getRole().equals("member")){
                return "member_search_results.html";
            }
            else {
                return "librarian_search_results.html";
            }
        }
        else {
            model.addAttribute("message",
            "There were no matching results for your search" );
            if(userSession.getUser() == null){
                return "guest_search_results.html";
            }
            else if (userSession.getUser().getRole().equals("member")){
                return "member_search_results.html";
            }
            else {
                return "librarian_search_results.html";
            }
        }
    }

    @GetMapping("/search_results_name")
    public String search_results_name(Model model, @RequestParam(name="artifactName") String artifactName)   {
        
        Artifact artifactCheck = artifactRepository.findByName(artifactName.toLowerCase());

        if(artifactCheck != null){
            model.addAttribute("message","Match found:" );
            model.addAttribute("name", artifactCheck.getName());
            model.addAttribute("artifact", artifactCheck);
            if(userSession.getUser() == null){
                return "guest_search_results.html";
            }
            else if (userSession.getUser().getRole().equals("member")){
                return "member_search_results.html";
            }
            else {
                return "librarian_search_results.html";
            }
        }
        else {
            model.addAttribute("message",
            "There were no matching results for your search" );
            if(userSession.getUser() == null){
                return "guest_search_results.html";
            }
            else if (userSession.getUser().getRole().equals("member")){
                return "member_search_results.html";
            }
            else {
                return "librarian_search_results.html";
            }
        }
    }

    @GetMapping("/register")
    public String register(){
        return "register.html";
    }

    @PostMapping("/register_complete")
    public String register(Model model,  HttpServletResponse response, 
    @RequestParam(name="username") String username,
    @RequestParam(name="password") String password)  throws IOException {

        System.out.println(username);
        System.out.println(password);

        if(username == "" || password == ""){
            return "register.html";
        }
        else{
            User newUser = new User();
            newUser.setName(username);
            newUser.setPassword(password);
            newUser.setRole("member");
            userRepository.save(newUser);
            return ("register_complete.html");
        }
    }

    @GetMapping("/member_menu")
    public String member_menu(Model model) {
        return "member_menu.html";
    }

    @GetMapping("/member_view_loans")
    public String member_view_loans(Model model) {

        /*
        private long loanid;
    private long artifactid;           //tracks id of item being loaned
    private long userid;            //tracks id of user who's loaned it
    private boolean reserved = false;
    private boolean loaned = false;
    @CreationTimestamp
    private Date dateLoaned;
        */

        List<Loan> listLoans; //= new List<Loan>();
        //ArrayList<Loan> listUsersLoans = new ArrayList<Loan>();
        ArrayList<Long> loanids = new ArrayList<Long>();
        ArrayList<Long> artifactids = new ArrayList<Long>();
        ArrayList<Date> datesLoaned = new ArrayList<Date>();
        User currentUser = userSession.getUser();
        listLoans = loanRepository.findAll();
        Iterator<Loan> listIterator = listLoans.iterator();
        //check all loans for any that are from user, store in a different list
        while (listIterator.hasNext() == true) {
            Loan currentLoan = listIterator.next();
            if (currentLoan.getUserid() == currentUser.getId())  {
                //store details of loans in lists
                loanids.add(currentLoan.getLoanid());
                artifactids.add(currentLoan.getArtifactid());
                datesLoaned.add(currentLoan.getDateLoaned());
            }
        }
        //return lists with corresponding loan details
        model.addAttribute("loanids", loanids);
        model.addAttribute("artifactids", artifactids);
        model.addAttribute("datesLoaned", datesLoaned);
        return "member_view_loans.html";
    }

    @GetMapping("/member_loanout_item")
    public String member_loanout_item(Model model) {
        return "member_loanout_item.html";
    }

    @GetMapping("/loanout_search_results")
    public String loanout_search_results(@RequestParam(name="artifactID") Long artifactID, Model model) {
        long latestLoan = -1;
        List<Loan> listLoans; //= new List<Loan>();
        listLoans = loanRepository.findAll();
        Iterator<Loan> listLoanIterator = listLoans.iterator();
        while (listLoanIterator.hasNext() == true) {
            Loan currentLoan = listLoanIterator.next();
            if (currentLoan.getArtifactid() == artifactID)  {
                latestLoan = currentLoan.getLoanid();
            }
        }

        //no previous record of item was found in loanRepo - need to check if item exists
        if (latestLoan == -1)  { 
            boolean artifactExists = false;
            List<Artifact> listArtifacts;
            listArtifacts = artifactRepository.findAll();
            Iterator<Artifact> listArtIterator = listArtifacts.iterator();
            while (listArtIterator.hasNext() == true) {
                Artifact currentArtifact = listArtIterator.next();
                if (currentArtifact.getId() == artifactID)  {
                    //artifact exists and hasnt been loaned out before - we can loan it out
                    artifactExists = true;
                    model.addAttribute("message", "Loan out request successful");
                    System.out.println("No previous loans for item/Item exists - We can loan it out!");
                    Loan newLoan = new Loan();
                    newLoan.setArtifactid(artifactID);
                    newLoan.setLoaned(true);
                    User currentUser = userSession.getUser();
                    newLoan.setUserid(currentUser.getId());
                    loanRepository.save(newLoan);
                    return "loanout_search_results.html";
                }
            }

            if(artifactExists == false) {
                //input artifact id doesnt belong to existing artifact - cannot reserve
                model.addAttribute("message", "Item does not exist - please make sure you have entered the correct details");
                System.out.println("Item doesn't exist :(");
            }
            return "loanout_search_results.html";
        }
        //there IS a previous record of item being loaned
        else    {
            //fetching latest record of the item being loaned out
            int loanIndex = ((int)latestLoan - 1);
            Loan currentLoan = listLoans.get(loanIndex);
            //item is out on loan - check if we can reserve it
            if (currentLoan.getLoaned() == true)
            {
                //item it loaned out and reserved - cant do anything
                if (currentLoan.getReserved() == true)
                {    
                    model.addAttribute("message", "Item is out on loan and has already been reserved");
                    //model.addAttribute("artifactID", artifactID);
                    System.out.println("On loan AND has already been reserved - cant do anything :(");
                    return "loanout_search_results.html";
                }
                //item out on loan but NOT reserved - redirect to reserve item
                else
                {
                    model.addAttribute("message", "Item is out on loan but hasnt been reserved yet - re enter ID to reserve it");
                    System.out.println("On loan but HASNT been reserved - can reserve :D");
                    return "member_reserve_item";
                }
            }
            return "loanout_search_results.html";
        }
    }



    @GetMapping("/reserve_search_results")
    public String reserve_search_results(@RequestParam(name="artifactID") Long artifactID, Model model) {

        long latestLoan = -1;
        List<Loan> listLoans; //= new List<Loan>();
        listLoans = loanRepository.findAll();
        Iterator<Loan> listLoanIterator = listLoans.iterator();
        while (listLoanIterator.hasNext() == true) {
            Loan currentLoan = listLoanIterator.next();
            if (currentLoan.getArtifactid() == artifactID)  {
                latestLoan = currentLoan.getLoanid();
            }
        }

        //no previous record of item was found in loanRepo -artifact doesnt exist
        if (latestLoan == -1)  { 
            model.addAttribute("message", "Item does not exist - please make sure you have entered the correct details");
            System.out.println("Item doesn't exist :(");
            return "reserve_search_results.html";
            //}
        }
        //there IS a previous record of item being loaned
        else    {
            //fetching latest record of the item being loaned out
            int loanIndex = ((int)latestLoan - 1);
            Loan currentLoan = listLoans.get(loanIndex);
            //item is out on loan - check if we can reserve it
            //item it loaned out and reserved - cant do anything
            if (currentLoan.getReserved() == true)
            {    
                model.addAttribute("message", "Item is out on loan and has already been reserved");
                //model.addAttribute("artifactID", artifactID);
                System.out.println("On loan AND has already been reserved - cant do anything :(");
                return "reserve_search_results.html";
            }
            //item out on loan but NOT reserved - reserve item
            else
            {
                model.addAttribute("message", "Your item has been reserved");
                System.out.println("On loan but HASNT been reserved - will reserve now :D");
                currentLoan.setReserved(true);
                loanRepository.save(currentLoan);
                return "reserve_search_results";
            }
        }
    }   

    @GetMapping("/librarian_menu")
    public String librarian_menu(Model model) {
        return "librarian_menu.html";
    }

    @GetMapping("/librarian_addRemoveArtifacts")
    public String librarian_addRemoveArtifacts(Model model) {
        return "librarian_addRemoveArtifacts.html";
    }

    @GetMapping("/artifact_remove_ID")
    public String artifact_remove_ID(@RequestParam(name="artifactID") Long artifactID, Model model) {
        Optional artifactCheck;
        try
        {
            artifactCheck = artifactRepository.findById(artifactID);
        } 
        catch (Exception e)
        {
            model.addAttribute("message",
            "There were no matching results for your search" );
            return "artifact_not_found.html";
        }

        if(artifactCheck.isPresent() == false) {
            model.addAttribute("message",
            "There were no matching results for your search" );
            return "artifact_not_found.html";
        }
        artifactRepository.deleteById(artifactID);
        model.addAttribute("message","Artifact has been successfully removed" );
        return "artifact_remove_ID.html";
    }

    @PostMapping("/artifact_add")
    public String artifact_add(
        @RequestParam(name="artifactName") String artifactName, 
        @RequestParam(name="artifactType") String artifactType,
        Model model, HttpServletResponse response) throws IOException {
            Artifact newArtifact = new Artifact();

            if(artifactName == "" || artifactType == ""){
                return "librarian_addRemoveArtifacts.html";
            }
            else{
                newArtifact.setName(artifactName.toLowerCase());
                newArtifact.setType(artifactType.toLowerCase());
                artifactRepository.save(newArtifact);
                return "librarian_menu.html";
            }
        }
}