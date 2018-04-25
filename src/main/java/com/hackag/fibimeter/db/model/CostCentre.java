package com.hackag.fibimeter.db.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
public class CostCentre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCostCentre;
    private String name;
    private String description;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Boolean isActive;
    @OneToMany(mappedBy = "costCentre", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotNull
    private Set<OrgUnit> orgUnits;
    @OneToOne(mappedBy = "costCentreOwner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Employee owner;

    public CostCentre() {
    }

    public CostCentre(String name, String description, LocalDateTime validFrom, LocalDateTime validTo, Boolean isActive, Set<OrgUnit> orgUnits, Employee owner) {
        this.name = name;
        this.description = description;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.isActive = isActive;
        this.orgUnits = orgUnits;
        this.owner = owner;
    }

    public Long getIdCostCentre() {
        return idCostCentre;
    }

    public void setIdCostCentre(Long idCostCentre) {
        this.idCostCentre = idCostCentre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Set<OrgUnit> getOrgUnits() {
        return orgUnits;
    }

    public void setOrgUnits(Set<OrgUnit> orgUnits) {
        this.orgUnits = orgUnits;
    }

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        Assert.notNull(idCostCentre, "The business key should always be defined");
        if (this == o) return true;
        if (!(o instanceof CostCentre)) return false;
        CostCentre that = (CostCentre) o;
        return Objects.equals(idCostCentre, that.idCostCentre);
    }

    @Override
    public int hashCode() {
        if (idCostCentre == null) {
            LoggerFactory.getLogger(CostCentre.class).error("The business key should always be defined");
        }
        return Objects.hash(idCostCentre);
    }
}
