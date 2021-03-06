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
import org.springframework.data.jpa.repository.Query;
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
        userSession.setUser(null);
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
        model.addAttribute("artifacts", artifactRepository.findAll());
        //enter name of artifact you want
        //match input name with db artifact name (search db)

        return "member_search.html";  
    }

    @GetMapping("/librarian_search")
    public String librarian_search(Model model) {
        model.addAttribute("artifacts", artifactRepository.findAll());
        return "librarian_search.html";
    }
    
    @GetMapping("/search_results_ID")
    public String search_results_ID(@RequestParam(name="artifactID") Long artifactID, Model model)   {

        if(artifactID == null){
            if(userSession.getUser() == null){
                return "guest_search.html";
            }
            else if (userSession.getUser().getRole().equals("member")){
                return "member_search.html";
            }
            else {
                return "librarian_search.html";
            }
        }

        Optional<Artifact> artifactCheck = artifactRepository.findById(artifactID);

        if(artifactCheck.isPresent() == false) {
            model.addAttribute("message",
            "There were no matching results for your search" );
            if(userSession.getUser() == null){
                return "guest_id_results.html";
            }
            else if (userSession.getUser().getRole().equals("member")){
                return "member_id_results.html";
            }
            else {
                return "librarian_id_results.html";
            }
        }
        else{
            Artifact artifact = artifactRepository.getOne(artifactID);
            model.addAttribute("message","Match found:" );
            model.addAttribute("name", artifact.getName());
            model.addAttribute("id", artifact.getId());
            model.addAttribute("type", artifact.getType());
            if(userSession.getUser() == null){
                return "guest_id_results.html";
            }
            else if (userSession.getUser().getRole().equals("member")){
                return "member_id_results.html";
            }
            else {
                return "librarian_id_results.html";
            }
        }
    }

    @GetMapping("/search_results_type")
    public String search_results_type(Model model, @RequestParam(name="artifactType") String artifactType)   {
        
        ArrayList<Artifact> results = new ArrayList<Artifact>();

        artifactType = artifactType.toLowerCase();

        for(Artifact tempArt : artifactRepository.findAll()) {
            if(tempArt.getType().equals(artifactType)){
                results.add(tempArt);
            }
        }

        if(results.size() != 0){

            model.addAttribute("message","Match found:" );
            model.addAttribute("artifacts", results);
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
        ArrayList<Artifact> results = new ArrayList<Artifact>();

        artifactName = artifactName.toLowerCase();

        for(Artifact tempArt : artifactRepository.findAll()) {
            if(tempArt.getName().equals(artifactName)){
                results.add(tempArt);
            }
        }

        if(results.size() != 0){
            model.addAttribute("message","Match found:" );
            model.addAttribute("artifacts", results);
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
       
        ArrayList<Loan> listLoans = new ArrayList<Loan>(); 
        
        for(Loan tempLoan: loanRepository.findAll()){
            if(tempLoan.getUserLoanedid() == userSession.getUser().getId()){
                listLoans.add(tempLoan);
            }
        }
        if(listLoans.size() != 0){
            model.addAttribute("message","Match found:" );
            model.addAttribute("loans", listLoans);
            return "member_view_loans.html";
        }
        else {
            model.addAttribute("message", "There were no matching results for your search" );
            return "member_view_loans.html";
        }
    }

    @GetMapping("/member_loanout_item")
    public String member_loanout_item(Model model) {
        return "member_loanout_item.html";
    }

    @GetMapping("/loanout_search_results")
    public String loanout_search_results(@RequestParam(name="artifactID") Long artifactID, Model model) {

        if(artifactID == null){
            return "member_loanout_item.html";
        }

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
                    newLoan.setUserLoanedid(currentUser.getId());
                    newLoan.setArtifactName(currentArtifact.getName());
                    newLoan.setArtifactType(currentArtifact.getType());
                    //newLoan.setDueDate();
                    loanRepository.save(newLoan);
                    newLoan.setDueDate();
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
                //item is being loaned - check if user is the one taking it out
                if (currentLoan.getUserLoanedid() == userSession.getUser().getId()) {
                    model.addAttribute("message", "You're already loaning the item!");
                    System.out.println("Current user is loaning item, not allowed to reserve it!");
                    return "reserve_search_results.html";
                }
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

    @GetMapping("/member_reserve_item")
    public String member_reserve_item(Model model) {
        model.addAttribute("message", "Enter ID of item you want to reserve");
        return "member_reserve_item.html";
    }


    @GetMapping("/reserve_search_results")
    public String reserve_search_results(@RequestParam(name="artifactID") Long artifactID, Model model) {
        if(artifactID == null){
            return "member_reserve_item.html";
        }
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
                    model.addAttribute("message", "Item is not on loan - can be taken out now");
                    System.out.println("No previous loans for item/Item exists - We can loan it out!");
                    Loan newLoan = new Loan();
                    newLoan.setArtifactid(artifactID);
                    newLoan.setLoaned(true);
                    User currentUser = userSession.getUser();
                    newLoan.setUserLoanedid(currentUser.getId());
                    loanRepository.save(newLoan);
                    newLoan.setDueDate();
                    loanRepository.save(newLoan);
                    return "reserve_search_results.html";
                }
            }

            if(artifactExists == false) {
                //input artifact id doesnt belong to existing artifact - cannot reserve
                model.addAttribute("message", "Item does not exist - please make sure you have entered the correct details");
                System.out.println("Item doesn't exist :(");
            }
            return "reserve_search_results.html";
        }

        //there IS a previous record of item being loaned
        else    {
            //fetching latest record of the item being loaned out
            int loanIndex = ((int)latestLoan - 1);
            Loan currentLoan = listLoans.get(loanIndex);

            if (currentLoan.getUserLoanedid() == userSession.getUser().getId()) {
                model.addAttribute("message", "You're already loaning the item!");
                System.out.println("Current user is loaning item, not allowed to reserve it!");
                return "reserve_search_results.html";
            }
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
                currentLoan.setUserReservedid(userSession.getUser().getId());
                loanRepository.save(currentLoan);
                return "reserve_search_results";
            }
        }
    } 
    
    @GetMapping("/member_renew_item")
    public String member_renew_item(Model model) {
        User currentUser = userSession.getUser();
        ArrayList<Loan> currentUserLoans = new ArrayList<Loan>();

        List<Loan> listLoans = loanRepository.findAll();
        Iterator<Loan> listIterator = listLoans.iterator();
        //check all loans for any that are from user, store in a different list
        while (listIterator.hasNext() == true) {
            Loan currentLoan = listIterator.next();
            if (currentLoan.getUserLoanedid() == currentUser.getId())  {
                currentUserLoans.add(currentLoan);
            }
        }
        
        model.addAttribute("currentUserLoans", currentUserLoans);
        return "member_renew_item.html";
    }

    @GetMapping("/renew_search_results")
    public String renew_search_results(@RequestParam(name="loanID") Long loanID, Model model) {
        if(loanID ==null){
            return "member_renew_item.html";
        }
        //fetch loan from repo 
        Loan currentLoan = loanRepository.getOne(loanID);
        User currentUser = userSession.getUser();
        if (currentLoan.getReserved() == true)  {
            //item already reserved by user!
            if (currentLoan.getUserReservedid() == currentUser.getId())   {
                model.addAttribute("message", "You've already reserved this item!");
                System.out.println("Item already reserved by user - can't reserve again!");
                return "renew_search_results.html";
            }
            //item reserved by someone else, cannot be renewed
            else    {
                model.addAttribute("message", "Request denied - item already reserved by someone else");
                System.out.println("Item already reserved by someone else - cannot be reserved :(");
                return "renew_search_results.html";
            }
        }
        //item not reserved - CAN be renewed
        else    {
            model.addAttribute("message", "Item renewed");
            System.out.println("No reservation - item renewed :)");
            currentLoan.setReserved(true);
            currentLoan.setUserReservedid(currentUser.getId());
            loanRepository.save(currentLoan);
            currentLoan.setDueDate();
            loanRepository.save(currentLoan);
            return "renew_search_results.html";
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

    @GetMapping("/librarian_viewMemberLoans")
    public String librarian_viewMemberLoans(Model model)    {

        ArrayList<User> members = new ArrayList<User>();
        List<User> listUsers = userRepository.findAll();
        Iterator<User> listIterator = listUsers.iterator();
        //search user repo, store all ids belonging to members
        while (listIterator.hasNext() == true) {
            User currentUser = listIterator.next();
            if (currentUser.getRole() == "member")  {
                members.add(currentUser);
            }
        }
        
        model.addAttribute("members", members);
        return "librarian_viewMemberLoans.html";
    }

    @GetMapping("/librarian_viewLoansResults")
    public String librarian_viewLoansResults(@RequestParam(name="userid") Long userid, Model model) {
        if(userid == null){
            return "librarain_viewMemberLoans.html";
        }
        ArrayList<Loan> listLoans = new ArrayList<Loan>(); 
        
        for(Loan tempLoan: loanRepository.findAll()){
            if(tempLoan.getUserLoanedid() == userid){
                listLoans.add(tempLoan);
            }
        }
        if(listLoans.size() != 0){
            model.addAttribute("message","Match found:" );
            model.addAttribute("loans", listLoans);
            return "librarian_viewLoansResults.html";
        }
        else {
            model.addAttribute("message", "There were no matching results for your search" );
            return "librarian_viewLoansResults.html";
        }
    }

    @GetMapping("/librarian_useridInput")
    public String librarian_useridInput(Model model) {
        return "librarian_useridInput.html";
    }

    @GetMapping("/librarian_loanRenew")
    public String librarian_loanRenew(@RequestParam(name="userid") Long userid, Model model) {
        //ArrayList<Long> memberids = new ArrayList<Long>();
        //make sure that the inputted id belongs to a member
        List<User> listUsers = userRepository.findAll();
        Iterator<User> listIterator = listUsers.iterator();
        User selectedUser = null;

        while (listIterator.hasNext() == true) {
            User currentUser = listIterator.next();
            //matching member found
            if (currentUser.getId().equals(userid) && currentUser.getRole().equals("member"))  {
                selectedUser = currentUser; 
            }
        }
        //id doesnt match any members
        if (selectedUser == null)   {
            model.addAttribute("message", "Inputted id doesnt match any members on record. Make sure the id you entered is correct.");
            return "librarian_renewResults.html";
        }
        //id matches a member - fetch all loanids relating to that member
        else    {
        List<Loan> listLoans; //= new List<Loan>();
        ArrayList<Loan> loans = new ArrayList<Loan>();
        boolean loansExist = false;        //check if member has any loans

        listLoans = loanRepository.findAll();
        Iterator<Loan> listIteratorTwo = listLoans.iterator();
        //check all loans for any that are from user, store in a different list
        while (listIteratorTwo.hasNext() == true) {
            Loan currentLoan = listIteratorTwo.next();
            if (currentLoan.getUserLoanedid() == selectedUser.getId())  {
                loansExist = true;      //member has at least one loan
                loans.add(currentLoan);      
            }
        }

        //member exists but hasnt taken any loans out
        if  (loansExist == false)    {
            model.addAttribute("message", "No loans associated with the member.");
            return "librarian_renewResults.html";
        }
        else    {
            //return list with corresponding loanids belonging to that member
            model.addAttribute("loans", loans);
            return "librarian_loanRenew.html";
        }
    }

    }

    @GetMapping("/librarian_renewResults")
    public String librarian_renewResults(@RequestParam(name="loanID") Long loanID, Model model) {
        if(loanID == null){
            return"librarian_loanRenew.html";
        }
        //fetch loan from repo 
        Loan currentLoan = loanRepository.getOne(loanID);
        //User currentUser = userSession.getUser();
        if (currentLoan.getReserved() == true)  {
            //item already reserved by user!
                model.addAttribute("message", "Request denied - item already reserved");
                System.out.println("Item already reserved - cannot be reserved renewed:(");
                return "librarian_renewResults.html";
        }
        //item not reserved - CAN be renewed
        else    {
            model.addAttribute("message", "Item renewed");
            System.out.println("No reservation - item renewed :)");
            currentLoan.setReserved(true);
            currentLoan.setUserReservedid(currentLoan.getUserLoanedid());   //since member thats loaning it is going to renew it
            loanRepository.save(currentLoan);   
            currentLoan.setDueDate();
            loanRepository.save(currentLoan);
            return "librarian_renewResults.html";
        }
        //return "librarian_renewResults.html";
    }

    @GetMapping("/librarian_recordArtifactOnLoan")
    public String librarian_recordArtifactOnLoan(Model model) {
        return "librarian_recordArtifactOnLoan.html";
    }

    @GetMapping("/librarian_recordLoanResults")
    public String librarian_recordLoanResults(Model model, @RequestParam(name="artifactid") Long artifactid, @RequestParam(name="userid") Long userid)  {
        
        if(artifactid == null){
            return "librarian_recordLoanResults.html";
        }
        if(userid == null){
            return "librarian_recordLoanResults.html";
        }
        //check if artifact entered exists
        boolean artifactExists = false;
        Artifact latestArtifact = null;
        List<Artifact> listArtifacts;
        listArtifacts = artifactRepository.findAll();
        Iterator<Artifact> listArtIterator = listArtifacts.iterator();

        while (listArtIterator.hasNext() == true) {
            Artifact currentArtifact = listArtIterator.next();
            if (currentArtifact.getId() == artifactid)  {
                //artifact exists
                artifactExists = true;
                latestArtifact = currentArtifact;
            }
        }
            
        //artifact does NOT exist - return error
        if (artifactExists == false)    {
            model.addAttribute("message", "The artifact you entered doesn't exist. Ensure the details are correct.");
            System.out.println("artifact doesnt exist :(");
            return "librarian_recordLoanResults.html";
        }
        //artifact DOES exist - check if it's already being loaned (fetch latest loan of artifact)
        else    {
            Loan latestLoan = null;
            List<Loan> listLoans; //= new List<Loan>();
            listLoans = loanRepository.findAll();
            Iterator<Loan> listLoanIterator = listLoans.iterator();
            while (listLoanIterator.hasNext() == true) {
                Loan currentLoan = listLoanIterator.next();
                if (currentLoan.getArtifactid() == artifactid)  {
                    latestLoan = currentLoan;
                }
            }

            //no previous loans for this artifact - create new loan with userid as loaner
            if (latestLoan == null) {
                Loan newLoan = new Loan();
                newLoan.setArtifactid(artifactid);
                newLoan.setLoaned(true);
                //User currentUser = userSession.getUser();
                newLoan.setUserLoanedid(userid);
                newLoan.setArtifactName(latestArtifact.getName());
                newLoan.setArtifactType(latestArtifact.getType());
                //newLoan.setDueDate();
                loanRepository.save(newLoan);
                newLoan.setDueDate();
                loanRepository.save(newLoan);
                model.addAttribute("message", "New loan created - artifact successfully recorded as on loan");
                System.out.println("artifact recorded on loan - new loan created :)");
                return "librarian_recordLoanResults.html";
            }
            else    {
                //artifact is already being loaned out - cant be recorded as on loan
                if (latestLoan.getLoaned() == true) {
                    model.addAttribute("message", "Artifact is already on loan - can't be recorded as on loan.");
                    System.out.println("artifact already on loan - cant be recorded :(");
                    return "librarian_recordLoanResults.html";
                }
                //artifact is NOT on loan - record it as on loan, set userid as id of person that loaned it 
                //(assumming person may not be registered on LMS, can have unregistered member id)
                else    {
                    latestLoan.setLoaned(true);
                    latestLoan.setUserLoanedid(userid);
                    model.addAttribute("message", "Artifact successfully recorded as on loan");
                    System.out.println("artifact recorded on loan :)");
                    return "librarian_recordLoanResults.html";
                }
            }  
        }
    }

    @GetMapping("/librarian_recordArtifactReturned")
    public String librarian_recordArtifactReturned(Model model) {
        return "librarian_recordArtifactReturned.html";
    }

    @GetMapping("/librarian_recordReturnedResults")
    public String librarian_recordReturnedResults(@RequestParam(name="artifactid") Long artifactid, Model model)    {
        if(artifactid == null){ 
            return "librarian_recordArtifactReturned.html";
        }
        //check if artifact entered exists
        boolean artifactExists = false;
        Artifact latestArtifact = null;
        List<Artifact> listArtifacts;
        listArtifacts = artifactRepository.findAll();
        Iterator<Artifact> listArtIterator = listArtifacts.iterator();

        while (listArtIterator.hasNext() == true) {
            Artifact currentArtifact = listArtIterator.next();
            if (currentArtifact.getId() == artifactid)  {
                //artifact exists
                artifactExists = true;
                latestArtifact = currentArtifact;
            }
        }

        //artifact does NOT exist - return error
        if (artifactExists == false)    {
            model.addAttribute("message", "The artifact you entered doesn't exist. Ensure the details are correct.");
            System.out.println("artifact doesnt exist :(");
            return "librarian_recordReturnedResults.html";
        }
        //artifact DOES exist - check if it's already being loaned (fetch latest loan of artifact)
        else    {
            Loan latestLoan = null;
            List<Loan> listLoans; //= new List<Loan>();
            listLoans = loanRepository.findAll();
            Iterator<Loan> listLoanIterator = listLoans.iterator();
            while (listLoanIterator.hasNext() == true) {
                Loan currentLoan = listLoanIterator.next();
                if (currentLoan.getArtifactid() == artifactid)  {
                    latestLoan = currentLoan;
                }
            }

            //no previous loans for this artifact - cannot record as returned
            if (latestLoan == null) {
                model.addAttribute("message", "No prior loan records of artifact exist - cannot be recorded as returned");
                System.out.println("artifact not loaned before - cant record returned :(");
                return "librarian_recordReturnedResults.html";
            }
            //check if item was loaned out
            else    {
                //item not loaned out - cant be returned
                if (latestLoan.getLoaned() == false)    {
                    model.addAttribute("message", "Artifact not on loan - cannot be recorded as returned");
                    System.out.println("artifact not on loan - cant record returned :(");
                    return "librarian_recordReturnedResults.html";
                }
                //item is on loan, is now being returned
                else    {
                    //item has been reserved - reallocate the item
                    if (latestLoan.getReserved() == true)   {
                        Loan newLoan = new Loan();
                        newLoan.setUserLoanedid(latestLoan.getUserReservedid());
                        newLoan.setArtifactid(latestLoan.getArtifactid());
                        newLoan.setArtifactName(latestLoan.getArtifactName());
                        newLoan.setArtifactType(latestArtifact.getType());
                        newLoan.setLoaned(true);
                        loanRepository.save(newLoan);
                        newLoan.setDueDate();
                        loanRepository.save(newLoan);
                        loanRepository.delete(latestLoan);

                        model.addAttribute("message", "Artifact is reserved - reallocating it to new member");
                        System.out.println("artifact reserved - sent to new member :)");
                        return "librarian_recordReturnedResults.html";
                    }
                    //item not reserved - recording it as returned
                    else    {
                        latestLoan.setLoaned(false);
                        latestLoan.setUserLoanedid(-1);
                        latestLoan.setDateLoaned(null);
                        loanRepository.delete(latestLoan);      //delete record of loan
                        model.addAttribute("message", "Artifact not reserved - recording it as returned");
                        System.out.println("artifact returned to library :)");
                        return "librarian_recordReturnedResults.html";
                    }
                    
                }
            }
        }


    }

}