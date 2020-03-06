package lms;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @Autowired
    private UserSession userSession;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login(Model model, String username, String password, HttpServletResponse response) throws Exception {
        model.addAttribute("title", "BlogIt: Login");
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        System.out.println(userRepository.findAll());
        if (user.isPresent()) {
            userSession.setUser(user.get());
            System.out.println("LOGIN SUCCESS");
            if(userSession.getUser().getRole().equals("librarian")){
                return "/librarian_menu";
            }
            else if(userSession.getUser().getRole().equals("member")){
                return "/member_menu";
            }
            else {
                return "/index";
            }
            
        } else {
            userSession.setLoginFailed(true);
            System.out.println("LOGIN FAILED");
            return "index.html";
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws Exception {
        userSession.setUser(null);
        response.sendRedirect("/");
    }
}