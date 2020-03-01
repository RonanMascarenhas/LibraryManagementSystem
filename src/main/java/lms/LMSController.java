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
    private ArtifactRepository artRepo;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "LMS: Home");
        //model.addAttribute("user", userSession.getUser());
        model.addAttribute("artifacts", artRepo.findAll());
        return "index.html";
    }

    @GetMapping("/guest_search")
    public String guest_search(@RequestParam("id") long id, Model model) {
        Artifact artifact = artRepo.getOne(id);
        model.addAttribute("name", "Artifact: " + artifact.getName());
        model.addAttribute("artifact", artifact);
        //Artifact artifact = artRepo.getOne(1);
        return "guest_search.html";
    }

    /*@GetMapping("/artifact")
    public String artifact(@RequestParam("id") long id, Model model)    {
        Artifact artifact = artRepo.getOne(id);
        model.addAttribute("name", "Artifact: " + artifact.getName());
        model.addAttribute("artifact", artifact);
        return "guest_search.html";
    }*/
}