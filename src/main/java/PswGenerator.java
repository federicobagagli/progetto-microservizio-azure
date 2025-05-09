
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PswGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("riccardo");
        System.out.println(hash);
    }
}
