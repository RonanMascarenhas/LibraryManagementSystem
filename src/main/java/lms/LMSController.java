package lms;

import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Iterator;
import java.util.List;

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

    //@Autowired
    //private ArtifactLoanTable artifactLoanTable;


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
        //model.addAttribute("artifacts", artifactRepository.findAll());
        //enter name of artifact you want
        //match input name with db artifact name (search db)
        return "member_search.html";  
    }


    @GetMapping("/search_results_ID")
    public String search_results_ID(@RequestParam(name="artifactID") Long artifactID, Model model)   {
        //long artID = artifactID;
        //Long artIDWrap = artID;
        //if artifactID
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
            else {
                return "search_results.html";
            }
        }

        //boolean isEqual = maybe
        if(artifactCheck.isPresent() == false) {
            model.addAttribute("message",
            "There were no matching results for your search" );
            if(userSession.getUser() == null){
                return "guest_search_results.html";
            }
            else {
                return "search_results.html";
            }
        }

        Artifact artifact = artifactRepository.getOne(artifactID);
        model.addAttribute("message","Match found:" );
        model.addAttribute("name", artifact.getName());
        model.addAttribute("artifact", artifact);
        //String artifactName = artifact.getName();
        //System.out.print(artifactName);
        if(userSession.getUser() == null){
            return "guest_search_results.html";
        }
        else {
            return "search_results.html";
        }

        //artifactRepository.find
        //Long artID = artifactID;
        //Optional isArtifact = artifactRepository.findById(artifactID);
        //Long nullCheck = artifact.getId();
        /*if (isArtifact == null) {
        //(artifact.getId() == 0 || artifact.getName() == null) {
            model.addAttribute("message",
            "There were no matching results for your search" );
            return "search_results.html";
        }
        else    {*/
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
            else {
                return "search_results.html";
            } 
        }
        else {
            model.addAttribute("message",
            "There were no matching results for your search" );
            if(userSession.getUser() == null){
                return "guest_search_results.html";
            }
            else {
                return "search_results.html";
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
            else {
                return "search_results.html";
            } 
        }
        else {
            model.addAttribute("message",
            "There were no matching results for your search" );
            if(userSession.getUser() == null){
                return "guest_search_results.html";
            }
            else {
                return "search_results.html";
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
            return "/register";
        }
        else{
            User newUser = new User();
            newUser.setName(username);
            newUser.setPassword(password);
            newUser.setRole("member");
            userRepository.save(newUser);
            return ("/register_complete");
        }
    }

    @GetMapping("/member_menu")
    public String member_menu(Model model) {
        return "member_menu.html";
    }

    @GetMapping("/member_view_loans")
    public String member_view_loans(Model model) {
        List<Loan> listLoans; //= new List<Loan>();
        ArrayList<Loan> listUsersLoans = new ArrayList<Loan>();
        ArrayList<Long> artifactids = new ArrayList<Long>();
        User currentUser = userSession.getUser();
        listLoans = loanRepository.findAll();
        Iterator<Loan> listIterator = listLoans.iterator();
        //check all loans for any that are from user, store in a different list
        while (listIterator.hasNext() == true) {
            Loan currentLoan = listIterator.next();
            if (currentLoan.getUserid() == currentUser.getId())  {
                artifactids.add(currentLoan.getArtifactid());
                //listUsersLoans.add(currentLoan);
            }
        }
        model.addAttribute("artifactids", artifactids);
        return "member_view_loans.html";
    }

    @GetMapping("/member_reserve_item")
    public String member_reserve_item(Model model) {
        //loanRepository.find
        
        return "member_reserve_item.html";
    }

    @GetMapping("/reserve_search_results")
    public String reserve_search_results(@RequestParam(name="artifactID") Long artifactID, Model model) {
        //ArrayList<Long> listLoanids = new ArrayList<Long>();
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
        //int idIndex = listLoans.
        //int idIndex = listLoans.lastIndexOf(artifactID);

        //no previous record of item was found in loanRepo - need to check if item exists
        if (latestLoan == -1)  { 
            boolean artifactExists = false;
            List<Artifact> listArtifacts;
            listArtifacts = artifactRepository.findAll();
            Iterator<Artifact> listArtIterator = listArtifacts.iterator();
            while (listArtIterator.hasNext() == true) {
                Artifact currentArtifact = listArtIterator.next();
                if (currentArtifact.getId() == artifactID)  {
                    //artifact exists - we can reserve it
                    artifactExists = true;
                    model.addAttribute("message", "Your item has been reserved");
                    System.out.println("No previous loans for item/Item exists - We can reserve it!");
                    Loan newLoan = new Loan();
                    newLoan.setArtifactid(artifactID);
                    newLoan.setReloaned(false);
                    User currentUser = userSession.getUser();
                    newLoan.setUserid(currentUser.getId());
                    loanRepository.save(newLoan);
                    return "reserve_search_results.html";
                }
            }

            if(artifactExists == false) {
                //input artifact id doesnt belong to existing artifact - cannot reserve
                model.addAttribute("message", "Item does not exist - please make sure you have entered the correct details");
                System.out.println("Item doesn't exist :(");
                return "reserve_search_results.html";
            }
            //no previous record of item was found in loanRepo - not been loaned out yet
            /*model.addAttribute("message", "Your item has been reserved");
            System.out.println("No previous loans for item - We can reserve it!");
            Loan newLoan = new Loan();
            newLoan.setArtifactid(artifactID);
            newLoan.setReloaned(false);
            User currentUser = userSession.getUser();
            newLoan.setUserid(currentUser.getId());
            loanRepository.save(newLoan);*/
            //add entry into lookup table
            //artifactLoanTable.setLoanOfArtifact(currentUser.getId(), newLoan.getLoanid());
            //currentUser.setCurrentLoanid(newLoan.getLoanid()); 
        }
        //there IS a previous record of item being loaned
        else    {
            int loanIndex = ((int)latestLoan - 1);
            Loan currentLoan = listLoans.get(loanIndex);
            //item is out on loan AND has already been reserved (maybe even by the current user) - cannot reserve 
            if (currentLoan.getReloaned() == true || currentLoan.getUserid() == userSession.getUser().getId()) {    
                model.addAttribute("message", "Item cannot be reserved - is out on loan and has already been reserved");
                model.addAttribute("artifactID", artifactID);
                System.out.println("On loan AND has already been reserved - cannot reserve :(");
            }
            else if (currentLoan.getReloaned() == false) {
                //item is out on loan but has not been reserved
                model.addAttribute("message", "Item out on loan - are you sure you want to reserve it?");
                model.addAttribute("artifactID", artifactID);
                System.out.println("On loan but not reloaned yet - We can reserve it!");
                return "member_reserve_confirm";     //should bring up confirmation screen - M4
                /*Loan newLoan = new Loan();
                newLoan.setArtifactid(artifactID);
                newLoan.setReloaned(false);
                loanRepository.save(newLoan);*/
            }
            else    {
                //item is currently reserved - unkown why
                model.addAttribute("message", "Item cannot be reserved");
                System.out.println("Item cannot be reserved :(");
            }
        }
        return "reserve_search_results.html";
    }

    @PostMapping("/reserve_search_results")
    public String member_reserve_confirm(Model model) {
        //loanRepository.find
        model.addAttribute("message", "Your item has been reserved");
        //NOTE: Unable to get artifactID from here, cant assign it to loan record
            Loan newLoan = new Loan();
            //item is being reserved - cant be reserved again
            newLoan.setReloaned(true);
            User currentUser = userSession.getUser();
            newLoan.setUserid(currentUser.getId());
            loanRepository.save(newLoan);
            //add entry into lookup table
           // artifactLoanTable.setLoanOfArtifact(currentUser.getId(), newLoan.getLoanid());
        return "reserve_search_results.html";
    }

        //ids.iterator().
        //ids.add(artifactID);
        //Iterable<Long> iterable;
        //iterable.iterator() = ids.iterator();
        //iterable = ids.iterator();
        //loanRepository.findAll(Example<S> artifactID);
        
        
        /*tables.artifactLoanTable.forEach(long id)   {
            if (artifac
        };
        Iterable<Long> listids = new ArrayList(artifactID);
        loanRepository.findAllById(artifactID);
        //loanRepository.find*/
        

    @GetMapping("/librarian_menu")
    public String librarian_menu(Model model) {
        return "librarian_menu.html";
    }

    @GetMapping("/librarian_search")
    public String librarian_search(Model model) {
        return "/librarian_menu";
    }

    /*@PostMapping("/register_complete")
    public String register_complete(Credentials credentials, Model model)   {
        //newUser = User.new(5, credentials.getUsername(), credentials.getPassword(), "member");
        //userRepository.save(newUser);
    }*/

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
        //Artifact artifact = artifactRepository.getOne(artifactID);
        model.addAttribute("message","Artifact has been successfully removed" );
        //model.addAttribute("name", artifact.getName());
        //model.addAttribute("artifact", artifact);
        //String artifactName = artifact.getName();
        //System.out.print(artifactName);

        return "artifact_remove_ID.html";
    }

    @PostMapping("/artifact_add")
    public void artifact_add(
        @RequestParam(name="artifactName") String artifactName, 
        //@RequestParam(name="artifactType") String artifactType,
        Model model, HttpServletResponse response) throws IOException {
            //artifactRepository.save(new Artifact(101, artifactName));
            Artifact newArtifact = new Artifact();
            //newArtifact.setId(5);
            newArtifact.setName(artifactName.toLowerCase());
            artifactRepository.save(newArtifact);
            //model.addAttribute("message", "Artifact successfully added" );
            response.sendRedirect("/librarian_menu");
        
        }
}