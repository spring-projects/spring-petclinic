@RestController
@RequestMapping("/api")
public class GreetingController {

    @GetMapping("/greetings")
    public String greet(@RequestParam(required = false) String name) {
        if (name == null || name.isEmpty()) {
            return "Welcome to the PetClinic!";
        } else {
            return "Welcome to the PetClinic, " + name + "!";
        }
    }
}