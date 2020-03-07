package lms;

import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Iterator;
import java.util.List;

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
    //private LookupTable tables;


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
            "There were no matching results for your search" );
            return "search_results.html";
        }

        //boolean isEqual = maybe
        if(artifactCheck.isPresent() == false) {
            model.addAttribute("message",
            "There were no matching results for your search" );
            return "search_results.html";
        }

        Artifact artifact = artifactRepository.getOne(artifactID);
        model.addAttribute("message","Match found:" );
        model.addAttribute("name", artifact.getName());
        model.addAttribute("artifact", artifact);
        //String artifactName = artifact.getName();
        //System.out.print(artifactName);
        return "search_results.html";

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

    @GetMapping("/search_results_name")
    public String search_results_name(@RequestParam(name="artifactName") String artifactName, Model model)   {
        Artifact artifactCheck;
        try
        {
            artifactCheck = artifactRepository.findByName(artifactName);
        } 
        catch (Exception e)
        {
            model.addAttribute("message",
            "There were no matching results for your search" );
            return "search_results.html";
        }

        /*if(artifactCheck.isPresent() == false) {
            model.addAttribute("message",
            "There were no matching results for your search" );
            return "search_results.html";
        }*/

        //Artifact artifact = artifactRepository.getOne(artifactID);
        model.addAttribute("message","Match found:" );
        model.addAttribute("name", artifactCheck.getName());
        model.addAttribute("artifact", artifactCheck);
        //String artifactName = artifact.getName();
        //System.out.print(artifactName);
        return "search_results.html";
    }

    @GetMapping("/register")
    public String register(){
        return "/register";
    }

    @PostMapping("/register_complete")
    public String register(Model model,  HttpServletResponse response, 
    @RequestParam(name="username") String username,
    @RequestParam(name="password") String password)  throws IOException {
    
            User newUser = new User();
            newUser.setName(username);
            newUser.setPassword(password);
            newUser.setRole("member");
            userRepository.save(newUser);
            return ("/register_complete");
       
    }

    @GetMapping("/member_menu")
    public String member_menu(Model model) {
        return "member_menu.html";
    }

    @GetMapping("/member_view_loans")
    public String member_view_loans(Model model) {
        //loanRepository.find
        
        return "member_view_loans.html";
    }

    @GetMapping("/member_reserve_item")
    public String member_reserve_item(Model model) {
        //loanRepository.find
        
        return "member_reserve_item.html";
    }

    @GetMapping("/reserve_search_results")
    public String reserve_search_results(@RequestParam(name="artifactID") Long artifactID, Model model) {
        ArrayList<Long> ids = new ArrayList<Long>();
        //store id in array list
        //arraylist method for last id,
        //loanrepo search repo for matching loanids
        //return list of matching loanids
        //ids.add(artifactID);
        //int idIndex = ids.indexOf(artifactID);
        List<Loan> listLoans; //= new List<Loan>();
        listLoans = loanRepository.findAll();
        int idIndex = listLoans.lastIndexOf(artifactID);
        
        if (idIndex == -1)  {
            //no previous record of item was found in loanRepo
            model.addAttribute("message", "Not reserved yet - We can reserve it!");
            System.out.println("Not reserved yet - We can reserve it!");
        }

        else    {
            Loan currentLoan = listLoans.get(idIndex);
            if (currentLoan.getReloaned() == false) {
                model.addAttribute("message", "Not reserved yet - We can reserve it!");
                System.out.println("Not reserved yet - We can reserve it!");
            }
            else    {
                model.addAttribute("message", "Item already reserved :(");
                System.out.println("Item already reserved :(");
            }
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
        
        return "reserve_search_results.html";
    }

    @GetMapping("/librarian_menu")
    public String librarian_menu(Model model) {
        return "librarian_menu.html";
    }

    @GetMapping("/librarian_search")
    public String librarian_search(Model model) {
        return "librarian_search.html";
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
            newArtifact.setName(artifactName);
            artifactRepository.save(newArtifact);
            //model.addAttribute("message", "Artifact successfully added" );
            response.sendRedirect("/");
        
        }
}