package com.hackag.fibimeter.db.model;

import com.hackag.fibimeter.db.model.enumeration.Gender;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmployee;
    private String password;
    private boolean admin;
    private String firstName;
    private String surname;
    private String academicTitle;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDateTime dateOfBirth;
    private String displayName;
    @Column(unique = true)
    @Email
    private String emailAddress;
    private String street;
    private String city;
    private String zip;
    private String countryCode;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private OrgUnit orgUnit;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotNull
    private Set<OrgUnit> managedOrgUnits = new HashSet<>();
    private LocalDateTime dateOfCompanyEntry;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CostCentre costCentreOwner;
    private String jobTitle;
    private Long jobCategory;
    private String function;
    private String managementLevel;
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotNull
    private Set<FeedbackAccess> employeeFeedbackAccesses = new HashSet<>();
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotNull
    private Set<FeedbackRound> managerFeedbackRounds = new HashSet<>();
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotNull
    private Set<Feedback> feedbacks = new HashSet<>();
    @ManyToMany(mappedBy = "adminSelectedPeers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    private Set<FeedbackRound> adminSelectedPeerInFeedbackRounds = new HashSet<>();
    @ManyToMany(mappedBy = "peers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    private Set<FeedbackRound> peerInFeedbackRounds = new HashSet<>();

    public Employee() {
    }

    public Employee(String password, boolean admin, String firstName, String surname, String academicTitle, Gender gender, LocalDateTime dateOfBirth, String displayName, String emailAddress, String street, String city, String zip, String countryCode, OrgUnit orgUnit, Set<OrgUnit> managedOrgUnits, LocalDateTime dateOfCompanyEntry, CostCentre costCentreOwner, String jobTitle, Long jobCategory, String function, String managementLevel, Set<FeedbackAccess> employeeFeedbackAccesses, Set<FeedbackRound> managerFeedbackRounds, Set<Feedback> feedbacks, Set<FeedbackRound> adminSelectedPeerInFeedbackRounds, Set<FeedbackRound> peerInFeedbackRounds) {
        this.password = password;
        this.admin = admin;
        this.firstName = firstName;
        this.surname = surname;
        this.academicTitle = academicTitle;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.countryCode = countryCode;
        this.orgUnit = orgUnit;
        this.managedOrgUnits = managedOrgUnits;
        this.dateOfCompanyEntry = dateOfCompanyEntry;
        this.costCentreOwner = costCentreOwner;
        this.jobTitle = jobTitle;
        this.jobCategory = jobCategory;
        this.function = function;
        this.managementLevel = managementLevel;
        this.employeeFeedbackAccesses = employeeFeedbackAccesses;
        this.managerFeedbackRounds = managerFeedbackRounds;
        this.feedbacks = feedbacks;
        this.adminSelectedPeerInFeedbackRounds = adminSelectedPeerInFeedbackRounds;
        this.peerInFeedbackRounds = peerInFeedbackRounds;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public Set<OrgUnit> getManagedOrgUnits() {
        return managedOrgUnits;
    }

    public void setManagedOrgUnits(Set<OrgUnit> managedOrgUnits) {
        this.managedOrgUnits = managedOrgUnits;
    }

    public LocalDateTime getDateOfCompanyEntry() {
        return dateOfCompanyEntry;
    }

    public void setDateOfCompanyEntry(LocalDateTime dateOfCompanyEntry) {
        this.dateOfCompanyEntry = dateOfCompanyEntry;
    }

    public CostCentre getCostCentreOwner() {
        return costCentreOwner;
    }

    public void setCostCentreOwner(CostCentre costCentreOwner) {
        this.costCentreOwner = costCentreOwner;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(Long jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getManagementLevel() {
        return managementLevel;
    }

    public void setManagementLevel(String managementLevel) {
        this.managementLevel = managementLevel;
    }

    public Set<FeedbackAccess> getEmployeeFeedbackAccesses() {
        return employeeFeedbackAccesses;
    }

    public void setEmployeeFeedbackAccesses(Set<FeedbackAccess> employeeFeedbackAccesses) {
        this.employeeFeedbackAccesses = employeeFeedbackAccesses;
    }

    public Set<FeedbackRound> getManagerFeedbackRounds() {
        return managerFeedbackRounds;
    }

    public void setManagerFeedbackRounds(Set<FeedbackRound> managerFeedbackRounds) {
        this.managerFeedbackRounds = managerFeedbackRounds;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Set<FeedbackRound> getAdminSelectedPeerInFeedbackRounds() {
        return adminSelectedPeerInFeedbackRounds;
    }

    public void setAdminSelectedPeerInFeedbackRounds(Set<FeedbackRound> adminSelectedPeerInFeedbackRounds) {
        this.adminSelectedPeerInFeedbackRounds = adminSelectedPeerInFeedbackRounds;
    }

    public Set<FeedbackRound> getPeerInFeedbackRounds() {
        return peerInFeedbackRounds;
    }

    public void setPeerInFeedbackRounds(Set<FeedbackRound> peerInFeedbackRounds) {
        this.peerInFeedbackRounds = peerInFeedbackRounds;
    }

    @Override
    public boolean equals(Object o) {
        Assert.notNull(idEmployee, "The business key should always be defined");
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(idEmployee, employee.idEmployee);
    }

    @Override
    public int hashCode() {
        if (idEmployee == null) {
            LoggerFactory.getLogger(Employee.class).error("The business key should always be defined");
        }
        return Objects.hash(idEmployee);
    }
}
