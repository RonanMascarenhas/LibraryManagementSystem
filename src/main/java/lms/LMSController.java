package lms;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LMSController  {
    @Autowired
    private ArtifactRepository artifactRepository;
    @Autowired
    private UserSession userSession;

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

    @GetMapping("/search_results")
    public String search_results(@RequestParam(name="artifactID") int artifactID, Model model)   {
        long artID = artifactID;
        Long artIDWrap = artID;
        try
        {
            Optional isArtifact = artifactRepository.findById(artIDWrap);
        } 
        catch (Exception e)
        {
            model.addAttribute("message",
            "There were no matching results for your search" );
            return "search_results.html";
        }

        Artifact artifact = artifactRepository.getOne(artID);
        model.addAttribute("message","Match found:" );
        model.addAttribute("name", artifact.getName());
        model.addAttribute("artifact", artifact);
        String artifactName = artifact.getName();
        System.out.print(artifactName);
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
}