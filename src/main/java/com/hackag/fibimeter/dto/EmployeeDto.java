package com.hackag.fibimeter.dto;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.OrgUnit;
import com.hackag.fibimeter.db.model.enumeration.Gender;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeDto {

    private long idEmployee;
    private String firstName;
    private String surname;
    private String academicTitle;
    private Gender gender;
    private LocalDateTime dateOfBirth;
    private String emailAddress;
    private String street;
    private String city;
    private String zip;
    private String countryCode;
    private Long orgUnitId;
    private Set<Long> managedOrgUnitIds = new HashSet<>();
    private Long costCentreOwnerId;
    private String jobTitle;
    private Long jobCategory;
    private String function;
    private String managementLevel;

    public EmployeeDto() {
    }

    public EmployeeDto(long idEmployee, String firstName, String surname, String academicTitle, Gender gender, LocalDateTime dateOfBirth, String emailAddress, String street, String city, String zip, String countryCode, Long orgUnitId, Set<Long> managedOrgUnitIds, Long costCentreOwnerId, String jobTitle, Long jobCategory, String function, String managementLevel) {
        this.idEmployee = idEmployee;
        this.firstName = firstName;
        this.surname = surname;
        this.academicTitle = academicTitle;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.emailAddress = emailAddress;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.countryCode = countryCode;
        this.orgUnitId = orgUnitId;
        this.managedOrgUnitIds = managedOrgUnitIds;
        this.costCentreOwnerId = costCentreOwnerId;
        this.jobTitle = jobTitle;
        this.jobCategory = jobCategory;
        this.function = function;
        this.managementLevel = managementLevel;
    }

    public long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(long idEmployee) {
        this.idEmployee = idEmployee;
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

    public Long getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(Long orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public Set<Long> getManagedOrgUnitIds() {
        return managedOrgUnitIds;
    }

    public void setManagedOrgUnitIds(Set<Long> managedOrgUnitIds) {
        this.managedOrgUnitIds = managedOrgUnitIds;
    }

    public Long getCostCentreOwnerId() {
        return costCentreOwnerId;
    }

    public void setCostCentreOwnerId(Long costCentreOwnerId) {
        this.costCentreOwnerId = costCentreOwnerId;
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

    // Equality defined by ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeDto)) return false;
        EmployeeDto that = (EmployeeDto) o;
        return idEmployee == that.idEmployee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmployee);
    }

    public static EmployeeDto mapEmployee(Employee employee) {
        return new EmployeeDto(
            employee.getIdEmployee(),
            employee.getFirstName(),
            employee.getSurname(),
            employee.getAcademicTitle(),
            employee.getGender(),
            employee.getDateOfBirth(),
            employee.getEmailAddress(),
            employee.getStreet(),
            employee.getCity(),
            employee.getZip(),
            employee.getCountryCode(),
            employee.getOrgUnit() == null ? null : employee.getOrgUnit().getIdOrgUnit(),
            employee.getManagedOrgUnits().stream().map(OrgUnit::getIdOrgUnit).collect(Collectors.toSet()),
            employee.getCostCentreOwner() == null ? null : employee.getCostCentreOwner().getIdCostCentre(),
            employee.getJobTitle(),
            employee.getJobCategory(),
            employee.getFunction(),
            employee.getManagementLevel()
        );
    }
}
