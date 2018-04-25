package com.hackag.fibimeter.db.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class OrgUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrgUnit;
    private String name;
    private String shortName;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private OrgUnit parent;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotNull
    private Set<OrgUnit> children = new HashSet<>();
    private LocalDateTime managerValidFrom;
    private LocalDateTime managerValidTo;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CostCentre costCentre;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Employee manager;
    @OneToMany(mappedBy = "orgUnit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotNull
    private Set<Employee> employees = new HashSet<>();

    public OrgUnit() {
    }

    public OrgUnit(String name, String shortName, OrgUnit parent, Set<OrgUnit> children, LocalDateTime managerValidFrom, LocalDateTime managerValidTo, CostCentre costCentre, Employee manager, Set<Employee> employees) {
        this.name = name;
        this.shortName = shortName;
        this.parent = parent;
        this.children = children;
        this.managerValidFrom = managerValidFrom;
        this.managerValidTo = managerValidTo;
        this.costCentre = costCentre;
        this.manager = manager;
        this.employees = employees;
    }

    public Long getIdOrgUnit() {
        return idOrgUnit;
    }

    public void setIdOrgUnit(Long idOrgUnit) {
        this.idOrgUnit = idOrgUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public OrgUnit getParent() {
        return parent;
    }

    public void setParent(OrgUnit parent) {
        this.parent = parent;
    }

    public Set<OrgUnit> getChildren() {
        return children;
    }

    public void setChildren(Set<OrgUnit> children) {
        this.children = children;
    }

    public LocalDateTime getManagerValidFrom() {
        return managerValidFrom;
    }

    public void setManagerValidFrom(LocalDateTime managerValidFrom) {
        this.managerValidFrom = managerValidFrom;
    }

    public LocalDateTime getManagerValidTo() {
        return managerValidTo;
    }

    public void setManagerValidTo(LocalDateTime managerValidTo) {
        this.managerValidTo = managerValidTo;
    }

    public CostCentre getCostCentre() {
        return costCentre;
    }

    public void setCostCentre(CostCentre costCentre) {
        this.costCentre = costCentre;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        Assert.notNull(idOrgUnit, "The business key should always be defined");
        if (this == o) return true;
        if (!(o instanceof OrgUnit)) return false;
        OrgUnit orgUnit = (OrgUnit) o;
        return Objects.equals(idOrgUnit, orgUnit.idOrgUnit);
    }

    @Override
    public int hashCode() {
        if (idOrgUnit == null) {
            LoggerFactory.getLogger(OrgUnit.class).error("The business key should always be defined");
        }
        return Objects.hash(idOrgUnit);
    }
}
