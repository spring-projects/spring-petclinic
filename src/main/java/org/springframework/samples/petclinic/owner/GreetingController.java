@RestController
@RequestMapping("/api")
public class GreetingController {

    @GetMapping("/greetings")
    public String greet(@RequestParam(required = false) String name) {
        if (name == null || name.isEmpty()) {
            return "Hello, World!";
        } else {
            return "Hello, " + name + "!";
        }
    }
}