package ar.edu.itba.paw.models.users;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.files.ProfilePicture;

import javax.management.relation.Role;
import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
    @SequenceGenerator(sequenceName = "users_user_id_seq", name = "users_user_id_seq", allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(length = 22)
    private String cbu;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled;

    @Column(length = 10)
    private String locale;

    @Column
    private String description;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = UserRoles.class)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Collection<UserRoles> roles;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ProfilePicture profilePicture;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ResetCode resetCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private EmailValidation emailValidation;


    User(){}

    public User(long userId, String email, String password, String firstName, String lastName, boolean isEnabled, Locale locale) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isEnabled = isEnabled;
        this.locale = locale.toLanguageTag();
    }


    public User(String email, String password, String firstName, String lastName, boolean isEnabled, Locale locale) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isEnabled = isEnabled;
        this.locale = locale.toLanguageTag();
    }

    public User(String email, String password, String firstName, String lastName, String cbu, boolean isEnabled, Locale locale, String description) {
        this(email, password, firstName, lastName, isEnabled, locale);
        this.cbu = cbu;
        this.description = description;
    }

    public User(Long userId, String email, String password, String firstName, String lastName, String cbu, boolean isEnabled, Locale locale, String description) {
        this(email, password, firstName, lastName, cbu, isEnabled, locale, description);
        this.userId = userId;
    }


    public long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCbu() {
        return cbu;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public Locale getLocale() {
        return Locale.forLanguageTag(locale);
    }

    public String getDescription(){
        return description;
    }

    public Collection<UserRoles> getRoles() {
        return roles;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public ResetCode getResetCode() {
        return resetCode;
    }

    public EmailValidation getEmailValidation() {
        return emailValidation;
    }


    public void setEmailValidation(EmailValidation emailValidation) {
        this.emailValidation = emailValidation;
    }

    public void setResetCode(ResetCode resetCode) {
        this.resetCode = resetCode;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setRoles(Collection<UserRoles> roles) {
        this.roles = roles;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }
}


