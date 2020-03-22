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
            if(tempArt.getType().equals(artifactName)){
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
                    //newLoan.setDueDate();
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
        
        /*if (latestLoan == -1)  { 
            model.addAttribute("message", "Item does not exist - please make sure you have entered the correct details");
            System.out.println("Item doesn't exist :(");
            return "reserve_search_results.html";
            //}
        }*/
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
        //ArrayList<Loan> currentUserLoans = new ArrayList<Loan>();
        ArrayList<Loan> currentUserLoans = new ArrayList<Loan>();

        List<Loan> listLoans = loanRepository.findAll();
        Iterator<Loan> listIterator = listLoans.iterator();
        //check all loans for any that are from user, store in a different list
        while (listIterator.hasNext() == true) {
            Loan currentLoan = listIterator.next();
            if (currentLoan.getUserLoanedid() == currentUser.getId())  {
                currentUserLoans.add(currentLoan);

                /*fetch relevant artifact details from repo
                Artifact tempArt = artifactRepository.getOne(currentLoan.getArtifactid());
                //store details of loan/artifact/user in lists
                loanids.add(currentLoan.getLoanid());
                artifactids.add(currentLoan.getArtifactid());
                datesLoaned.add(currentLoan.getDateLoaned());
                dueDates.add(currentLoan.getDueDate());
                artifactNames.add(tempArt.getName());
                artifactTypes.add(tempArt.getType());*/
            }
        }
        
        model.addAttribute("currentUserLoans", currentUserLoans);
        return "member_renew_item.html";
    }

    @GetMapping("/renew_search_results")
    public String renew_search_results(@RequestParam(name="loanID") long loanID, Model model) {
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
        List<Loan> listLoans; //= new List<Loan>();
        //ArrayList<Loan> listUsersLoans = new ArrayList<Loan>();
        ArrayList<Long> loanids = new ArrayList<Long>();
        ArrayList<Long> artifactids = new ArrayList<Long>();
        ArrayList<Date> datesLoaned = new ArrayList<Date>();
        ArrayList<Date> dueDates = new ArrayList<Date>();
        ArrayList<String> artifactNames = new ArrayList<String>();
        ArrayList<String> artifactTypes = new ArrayList<String>();

        //User currentUser = userSession.getUser();
        listLoans = loanRepository.findAll();
        Iterator<Loan> listIterator = listLoans.iterator();
        //check all loans for any that are from user, store in a different list
        while (listIterator.hasNext() == true) {
            Loan currentLoan = listIterator.next();
            if (currentLoan.getUserLoanedid() == userid)  {
                //fetch relevant artifact details from repo
                Artifact tempArt = artifactRepository.getOne(currentLoan.getArtifactid());
                //store details of loan/artifact/user in lists
                loanids.add(currentLoan.getLoanid());
                artifactids.add(currentLoan.getArtifactid());
                datesLoaned.add(currentLoan.getDateLoaned());
                dueDates.add(currentLoan.getDueDate());
                artifactNames.add(tempArt.getName());
                artifactTypes.add(tempArt.getType());
            }
        }
        //return lists with corresponding loan details
        model.addAttribute("loanids", loanids);
        model.addAttribute("artifactids", artifactids);
        model.addAttribute("datesLoaned", datesLoaned);
        model.addAttribute("dueDates", dueDates);
        model.addAttribute("artifactNames", artifactNames);
        model.addAttribute("artifactTypes", artifactTypes);

        return "librarian_viewLoansResults.html";
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
    public String librarian_renewResults(@RequestParam(name="loanid") Long loanid, Model model) {
        
        //fetch loan from repo 
        Loan currentLoan = loanRepository.getOne(loanid);
        //User currentUser = userSession.getUser();
        if (currentLoan.getReserved() == true)  {
            //item already reserved by user!
            /*if (currentLoan.getUserReservedid() == currentUser.getId())   {
                model.addAttribute("message", "You've already reserved this item!");
                System.out.println("Item already reserved by user - can't reserve again!");
                return "renew_search_results.html";
            }
            //item reserved by someone else, cannot be renewed
            else   {*/
                model.addAttribute("message", "Request denied - item already reserved");
                System.out.println("Item already reserved - cannot be reserved renewed:(");
                return "librarian_renewResults.html";
            //}
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

}