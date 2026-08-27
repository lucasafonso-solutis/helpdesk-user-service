import org.springframework.security.crypto.bcrypt.BCrypt;
public class GenHash {
    public static void main(String[] args) {
        System.out.println(BCrypt.hashpw("admin123", BCrypt.gensalt()));
    }
}
