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
public class LMSController{
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
    public String search_resulString(Model model)   {
        //Artifact artifact = artifactRepository.getOne(id);
        //model.addAttribute("name", "Artifact: " + artifact.getName());
        //model.addAttribute("artifact", artifact);
        return "search_results.html";
    }

}