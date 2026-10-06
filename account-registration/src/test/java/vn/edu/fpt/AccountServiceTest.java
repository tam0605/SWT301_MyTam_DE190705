package vn.edu.fpt;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.io.IOException;
class AccountServiceTest {
    private AccountService service;
    @BeforeAll static void startEvidence() { TestEvidence.start(); }
    @BeforeEach void setup() { service = new AccountService(); }
    @AfterAll static void saveEvidence() throws IOException { TestEvidence.save("account-results.csv"); }
    @ParameterizedTest(name = "CSV registration {index}: {0}")
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    void registerAccount_fromRequiredCsv(String username, String password, String email, boolean expected) {
        String id = username == null ? "A02" : switch (username) {
            case "john123" -> "A01"; case "alice" -> "A03"; case "bob123" -> "A04"; case "carol" -> "A05";
            default -> throw new IllegalArgumentException("Unmapped CSV test case");
        };
        TestEvidence.value(id, "registerAccount", "username=" + username + "; password=" + password + "; email=" + email,
            expected, service.registerAccount(username, password, email));
    }
    @ParameterizedTest(name = "{0} registration boundary/invalid")
    @CsvSource(value = {
        "A06, NULL, pass123, john@example.com, false",
        "A07, '   ', pass123, john@example.com, false",
        "A08, john, NULL, john@example.com, false",
        "A09, john, '', john@example.com, false",
        "A10, john, 123456, john@example.com, false",
        "A11, john, 1234567, john@example.com, true",
        "A12, john, 12345678, john@example.com, true",
        "A13, john, pass123, NULL, false",
        "A14, john, pass123, '', false",
        "A15, john, pass123, john@@example.com, false"
    }, nullValues = "NULL")
    void registerAccount_boundaryAndInvalid(String id, String username, String password, String email, boolean expected) {
        TestEvidence.value(id, "registerAccount", "username=" + username + "; password=" + password + "; email=" + email,
            expected, service.registerAccount(username, password, email));
    }
    @ParameterizedTest(name = "{0} email")
    @CsvSource(value = {
        "E01, john@example.com, true",
        "E02, carol@domain.com, true",
        "E03, john.doe+tag@sub.example.com, true",
        "E04, NULL, false",
        "E05, '', false",
        "E06, bobmail.com, false",
        "E07, john@@example.com, false",
        "E08, @example.com, false",
        "E09, john@, false",
        "E10, john@example, false",
        "E11, john example@example.com, false",
        "E12, ' john@example.com', false",
        "E13, .john@example.com, false",
        "E14, john..doe@example.com, false",
        "E15, john@-example.com, false",
        "E16, john@example-.com, false",
        "E17, john@example..com, false",
        "E18, john@example.c, false"
    }, nullValues = "NULL")
    void isValidEmail_validAndInvalid(String id, String email, boolean expected) {
        TestEvidence.value(id, "isValidEmail", "email=" + email, expected, service.isValidEmail(email));
    }
}
