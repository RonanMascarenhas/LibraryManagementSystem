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
    public String login(Model model) {
        model.addAttribute("title", "BlogIt: Login");
        if (userSession.isLoginFailed()) {
            model.addAttribute("error", "Username and Password not correct");
            userSession.setLoginFailed(false);
        }
        return "login.html";
    }

    @PostMapping("/login")
    public String doLogin(String username, String password, HttpServletResponse response) throws Exception {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            userSession.setUser(user.get());
            if(userSession.getUser().getRole().equals("librarian")){
                return ("/librarian_menu");
            }
            else if(userSession.getUser().getRole().equals("member")){
                return ("/member_menu");
            }
            else{
                return ("/index");
            }
           
        } else {
            userSession.setLoginFailed(true);
            return ("/index");
        }

    }
}