package earlybird.earlybird.aws.alb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class ShowServerIpController {

    @GetMapping("/ip")
    public String showServerIp(Model model) throws UnknownHostException {
        model.addAttribute("ip", InetAddress.getLocalHost().getHostAddress());
        return "showServerIp";
    }
}
