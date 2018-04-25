package com.hackag.fibimeter.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackag.fibimeter.FibimeterApplication;
import com.hackag.fibimeter.db.model.CostCentre;
import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.OrgUnit;
import com.hackag.fibimeter.db.model.enumeration.Gender;
import com.hackag.fibimeter.db.repository.CostCentreRepository;
import com.hackag.fibimeter.db.repository.EmployeeRepository;
import com.hackag.fibimeter.db.repository.OrgUnitRepository;
import com.hackag.fibimeter.mail.MailHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class JsonToDatabaseParser {

    private static Logger log = LoggerFactory.getLogger(JsonToDatabaseParser.class);

    private static EmployeeRepository employeeRepository;
    private static CostCentreRepository costCentreRepository;
    private static OrgUnitRepository orgUnitRepository;

    // Successfully parsed values before saving entities
    private Map<Long, Employee> employeeOldIdMap = new HashMap<>();
    private Map<Long, CostCentre> costCentreOldIdMap = new HashMap<>();
    private Map<Long, OrgUnit> orgUnitOldIdMap = new HashMap<>();
    // Intermediate
    private Map<OrgUnit, Long> orgUnitsParentIdMap = new HashMap<>();
    private Map<OrgUnit, Long> orgUnitOldToIdMap = new HashMap<>();
    // Prepared foreign keys for joining in last phase
    private Map<Long, List<Employee>> orgUnitEmployeesOld = new HashMap<>();
    // Mapping to saved entities with new IDs
    private Map<Employee, Employee> employeesNew = new HashMap<>();
    private Map<CostCentre, CostCentre> costCentresNew = new HashMap<>();
    private Map<OrgUnit, OrgUnit> orgUnitsNew = new HashMap<>();

    /**
     * Run this to import JSON example data to DB.
     *
     * @param args Example data paths
     */
    public static void main(String[] args) throws IOException {
        Path[] paths = new Path[3];
        if (args.length == 1) {
            paths[0] = Paths.get(args[0] + "/EmployeesData.json");
            paths[1] = Paths.get(args[0] + "/CostCentresData.json");
            paths[2] = Paths.get(args[0] + "/OrgUnitsData.json");
        } else if (args.length == 3) {
            paths[0] = Paths.get(args[0]);
            paths[1] = Paths.get(args[1]);
            paths[2] = Paths.get(args[2]);
        } else {
            log.warn("Usage: please provide filenames for parsing as 3 parameters in the following order: " +
                "\"EmployeesData.json CostCentresData.json OrgUnitsData.json\", or alternatively provide only " +
                "1 parameter representing a directory path where all 3 files with such names are located.");
            log.info("Assuming path \"database\" containing all 3 JSON files with default names.");
            paths = null;
        }
        // Spring application most likely restarts with the provided arguments
        SpringApplication.run(FibimeterApplication.class, args);
        MailHelper.EMAILS_DISABLED = true;
        StaticContextAccessor.getBean(JsonToDatabaseParser.class).parse(paths);
        MailHelper.EMAILS_DISABLED = false;
    }

    private void prepareEmployeeForeignKeys(Employee employee, Long idOrgUnit, Long idCostCentre) {
        if (!this.orgUnitEmployeesOld.containsKey(idOrgUnit)) {
            this.orgUnitEmployeesOld.put(idOrgUnit, new ArrayList<>());
        }
        this.orgUnitEmployeesOld.get(idOrgUnit).add(employee);
        // CostCentre is already mapped from the opposite side
    }

    @Transactional
    public void parse(Path[] paths) throws IOException {
        if (paths == null) {
            paths = new Path[3];
            paths[0] = Paths.get("database/EmployeesData.json");
            paths[1] = Paths.get("database/CostCentresData.json");
            paths[2] = Paths.get("database/OrgUnitsData.json");
        }
        log.info("Parsing JSON to DB from the following 3 files:");
        for (Path path : paths) {
            log.info(path.getFileName().toFile().getAbsolutePath());
        }
        parse(
            new String(Files.readAllBytes(paths[0]), StandardCharsets.UTF_8),
            new String(Files.readAllBytes(paths[1]), StandardCharsets.UTF_8),
            new String(Files.readAllBytes(paths[2]), StandardCharsets.UTF_8)
        );
    }

    @Transactional
    public void parse(String employeesData, String costCentresData, String orgUnitsData) throws IOException {
        JsonToDatabaseParser.employeeRepository = StaticContextAccessor.getBean(EmployeeRepository.class);
        JsonToDatabaseParser.costCentreRepository = StaticContextAccessor.getBean(CostCentreRepository.class);
        JsonToDatabaseParser.orgUnitRepository = StaticContextAccessor.getBean(OrgUnitRepository.class);
        ObjectMapper mapper = new ObjectMapper();
        List<Employee> employeesOld = parseEmployees(mapper.readTree(employeesData));
        log.info("Saving employees to DB");
        employeesOld.forEach(employee -> {
                Employee duplicateOfExisting = employeeRepository.findByEmailAddress(employee.getEmailAddress());
                if (duplicateOfExisting != null) {
                    // Duplicate employee e-mail issue workaround
                    employee.setIdEmployee(duplicateOfExisting.getIdEmployee());
                    this.employeesNew.put(
                        employee, duplicateOfExisting
                    );
                    log.error("Employee with e-mail " + employee.getEmailAddress() + " has a duplicate, skipping");
                } else {
                    this.employeesNew.put(
                        employee, employeeRepository.saveAndFlush(employee)
                    );
                }
            }
        );

        parseCostCentres(mapper.readTree(costCentresData));
        log.info("Saved costCentres to DB");

        List<OrgUnit> orgUnitsOld = parseOrgUnits(mapper.readTree(orgUnitsData));
        log.info("Saved orgUnits to DB");

        log.info("Configuring " + this.orgUnitsParentIdMap.size() + " orgUnit parents");
        long configuredParents = 0;
        for (OrgUnit childOld : orgUnitsOld) {
            Long oldParentId = this.orgUnitsParentIdMap.get(childOld);
            OrgUnit child = orgUnitRepository.getOne(this.orgUnitsNew.get(childOld).getIdOrgUnit());
            child.setParent(this.orgUnitOldIdMap.get(oldParentId));
            orgUnitRepository.saveAndFlush(child);
            configuredParents++;
        }
        log.info(configuredParents + " orgUnit parents successfully set and saved");

        // Creating additional mappings that could not have been made before both relevant tables were filled
        // Old IDs parsed from JSON are used for reference, but new IDs assigned to stored entities are applied
        orgUnitsOld.forEach(orgUnit -> {
            // Following entry has to be mapped on ManyToOne side
            //orgUnit.setEmployees(this.orgUnitEmployeesOld.get(orgUnit.getIdOrgUnit()));
            List<Employee> orgUnitEmployees = this.orgUnitEmployeesOld.get(this.orgUnitOldToIdMap.get(orgUnit));
            if (orgUnitEmployees != null) {
                orgUnitEmployees.forEach(
                    orgUnitEmployee ->
                        this.employeesNew.get(orgUnitEmployee).setOrgUnit(this.orgUnitsNew.get(orgUnit))
                );
            }
        });
        log.info("Saving updated employees with foreign key mappings for the second time to DB");
        this.employeesNew.values().forEach(employee ->
            employeeRepository.saveAndFlush(employee)
        );
        log.info("Parsing and saving finished");
    }

    private List<Employee> parseEmployees(JsonNode root) {
        List<Employee> employees = new ArrayList<>();
        for (JsonNode node : root) {
            log.debug("Parsing employee node: " + node.toString());
            Employee employee = new Employee(
                "",
                false,
                node.get("firstName").asText(),
                node.get("surname").asText(),
                node.get("academicTitle").asText(),
                Enum.valueOf(Gender.class, node.get("gender").asText()),
                parseDate(node.get("dateOfBirth")),
                node.get("displayName").asText(),
                node.get("emailAddress").asText(),
                node.get("street").asText(),
                node.get("city").asText(),
                node.get("zip").asText(),
                node.get("countryCode").asText(),
                null,
                new HashSet<>(),
                parseDate(node.get("dateOfCompanyEntry")),
                null,
                node.get("jobTitle").asText(),
                node.get("jobCategory").asLong(),
                node.get("function").asText(),
                node.get("managementLevel").asText(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
            );
            prepareEmployeeForeignKeys(employee, node.get("orgUnitId").asLong(), node.get("costCentre").asLong());
            employees.add(employee);
            this.employeeOldIdMap.put(node.get("employeeId").asLong(), employee);
        }
        log.info("Parsed " + employees.size() + " employees");
        return employees;
    }

    private List<CostCentre> parseCostCentres(JsonNode root) {
        List<CostCentre> costCentres = new ArrayList<>();
        for (JsonNode node : root) {
            log.debug("Parsing costCentre node: " + node.toString());
            CostCentre costCentre = new CostCentre(
                node.get("name").asText(),
                node.get("description").asText(),
                parseDate(node.get("validFrom")),
                parseDate(node.get("validTo")),
                node.get("isActive").asBoolean(),
                new HashSet<>(),
                null
            );
            this.costCentresNew.put(
                costCentre, costCentreRepository.saveAndFlush(costCentre)
            );
            // The owner is mapped on the Employee side
            try {
                Employee employee = employeeRepository.getOne(
                    this.employeesNew.get(
                        this.employeeOldIdMap.get(
                            node.get("owner").get("employeeId").asLong()
                        )
                    ).getIdEmployee()
                );
                employee.setCostCentreOwner(costCentre);
                employeeRepository.saveAndFlush(employee);
            } catch (NullPointerException e) {
                log.error("Cannot map owner of costCentre with idEmployee "
                    + node.get("owner").get("employeeId").asLong()
                    + ", skipping entry idCostCentre");
                continue;
            }
            costCentres.add(costCentre);
            this.costCentreOldIdMap.put(node.get("number").asLong(), costCentre);
        }
        log.info("Parsed " + costCentres.size() + " costCentres");
        return costCentres;
    }

    private List<OrgUnit> parseOrgUnits(JsonNode root) {
        List<OrgUnit> orgUnits = new ArrayList<>();
        for (JsonNode node : root) {
            log.debug("Parsing orgUnit node: " + node.toString());
            OrgUnit orgUnit = new OrgUnit(
                node.get("name").asText(),
                node.get("shortName").asText(),
                null,
                new HashSet<>(),
                parseDate(node.get("manager").get("validFrom")),
                parseDate(node.get("manager").get("validTo")),
                this.costCentresNew.get(
                    this.costCentreOldIdMap.get(
                        node.get("costCentreNumber").asLong()
                    )
                ),
                // Conversion to new employee entity instance once used to cause InvalidDataAccessApiUsageException:
                // Multiple representations of the same entity Employee are being merged
                this.employeesNew.get(
                    this.employeeOldIdMap.get(
                        node.get("manager").get("employeeId").asLong()
                    )
                ),
                new HashSet<>()
            );
            this.orgUnitsNew.put(
                orgUnit, orgUnitRepository.saveAndFlush(orgUnit)
            );
            orgUnitsParentIdMap.put(orgUnit, node.get("parentId").asLong());
            orgUnits.add(orgUnit);
            this.orgUnitOldIdMap.put(node.get("orgUnitId").asLong(), orgUnit);
            this.orgUnitOldToIdMap.put(orgUnit, node.get("orgUnitId").asLong());
        }
        log.info("Parsed " + orgUnits.size() + " orgUnits");
        return orgUnits;
    }

    private LocalDateTime parseDate(JsonNode dateNode) {
        log.trace("Parsing dateTime " + dateNode.asText());
        if (dateNode.isNull()) {
            return null;
        }
        return LocalDateTime.parse(dateNode.asText(), DateTimeFormatter.ISO_DATE_TIME);
    }
}
