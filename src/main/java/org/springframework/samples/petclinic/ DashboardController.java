@Controller
public class DashboardController {
  
  private final JdbcTemplate jdbcTemplate;

  public DashboardController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping("/dashboard")
  public ModelAndView showDashboard() {
    ModelAndView mav = new ModelAndView("dashboard");
    
    // Get list of all cities
    List<String> cities = jdbcTemplate.queryForList("SELECT DISTINCT city FROM owners ORDER BY city ASC", String.class);
    mav.addObject("cities", cities);
    
    // Get number of owners in each city
    Map<String, Integer> ownersByCity = new HashMap<>();
    for (String city : cities) {
      int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM owners WHERE city=?", Integer.class, city);
      ownersByCity.put(city, count);
    }
    mav.addObject("ownersByCity", ownersByCity);
    
    return mav;
  }
  
}