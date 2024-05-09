@SpringBootApplication

@Controller
@RestController

@Component
@Autowired

@RequestBody
@RequestHeader
@PathVariable

@ResponseBody
@ResponseStatus

@RequestMapping("api")
@GetMapping("/")
@PostMapping("/")
@PutMapping("/")
@DeleteMapping("/")
@PatchMapping("/")

@Data
@Entity
@Table
@Id
@Column
@GeneratedValue
@GenericGenerator
@ManyToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "fk_user_id")

@Repository [Optional]
@Query

@Service

@Value