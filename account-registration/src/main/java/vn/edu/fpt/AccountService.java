package vn.edu.fpt;
import java.util.regex.Pattern;

/** Pure validation for Lab 2; no database or duplicate-account requirement. */
public class AccountService {
    // Teaching scope: common ASCII email form, with a dotted domain and alphabetic TLD.
    private static final Pattern EMAIL = Pattern.compile(
        "[A-Za-z0-9_+%-]+(?:\\.[A-Za-z0-9_+%-]+)*@"
        + "[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?"
        + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)*\\.[A-Za-z]{2,}");
    public boolean registerAccount(String username, String password, String email) {
        return username != null && !username.isBlank()
            && password != null && password.length() > 6 && isValidEmail(email);
    }
    public boolean isValidEmail(String email) {
        return email != null && EMAIL.matcher(email).matches();
    }
}
