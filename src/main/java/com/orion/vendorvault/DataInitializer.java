package com.orion.vendorvault;

import com.orion.vendorvault.model.entity.*;
import com.orion.vendorvault.model.enums.*;
import com.orion.vendorvault.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final ContractRepository contractRepository;
    private final ApprovalWorkflowRepository workflowRepository;
    private final ApprovalStepRepository stepRepository;
    private final PerformanceEvaluationRepository performanceEvaluationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking database initialization...");

        // 1. Initialize Roles
        // 1. Initialize Roles
        Role adminRole = createRoleIfNotFound("ROLE_SYSTEM_ADMIN");
        createRoleIfNotFound("ROLE_PROCUREMENT_MANAGER");
        createRoleIfNotFound("ROLE_FINANCE_CONTROLLER");
        createRoleIfNotFound("ROLE_LEGAL_COUNSEL");
        createRoleIfNotFound("ROLE_EXECUTIVE_DIRECTOR");
        createRoleIfNotFound("ROLE_DEPARTMENT_HEAD");

        // 2. Initialize Default Admin User
        userRepository.findByEmail("admin@orion.com").ifPresentOrElse(
            admin -> {
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                if (admin.getRoles() == null) {
                    admin.setRoles(new HashSet<>());
                }
                admin.getRoles().add(adminRole);
                userRepository.save(admin);
                log.info("Default Admin User synchronized with password and role.");
            },
            () -> {
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);

                User admin = User.builder()
                        .employeeId("SYS-001")
                        .fullName("System Administrator")
                        .email("admin@orion.com")
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .active(true)
                        .accountLocked(false)
                        .roles(roles)
                        .build();

                userRepository.save(admin);
                log.info("Default Admin User created: admin@orion.com / admin123");
            }
        );

        // 3. Seed random users if needed
        long userCount = userRepository.count();
        if (userCount < 10) {
            log.info("Seeding 45 random users...");
            String[] rolesList = {"ROLE_PROCUREMENT_MANAGER", "ROLE_FINANCE_CONTROLLER", "ROLE_LEGAL_COUNSEL", "ROLE_EXECUTIVE_DIRECTOR", "ROLE_DEPARTMENT_HEAD"};
            for (int i = 1; i <= 45; i++) {
                String roleStr = rolesList[i % rolesList.length];
                Role role = createRoleIfNotFound(roleStr);
                Set<Role> userRoles = new HashSet<>();
                userRoles.add(role);

                User testUser = User.builder()
                        .employeeId(String.format("EMP-%03d", i))
                        .fullName("Test User " + i)
                        .email("user" + i + "@orion.com")
                        .passwordHash(passwordEncoder.encode("password123"))
                        .active(true)
                        .accountLocked(false)
                        .roles(userRoles)
                        .build();
                userRepository.save(testUser);
            }
            log.info("Successfully seeded 45 test users.");
        }

        // 4. Seed Vendors
        if (vendorRepository.count() == 0) {
            log.info("Seeding 15 vendors...");
            String[] vendorNames = {
                "Tata Consultancy Services", "Infosys Limited", "Wipro Tech", "HCL Technologies",
                "Reliance Industries", "Airtel Business", "Adani Global", "L&T Construction",
                "Mahindra Logistics", "Apollo Hospitals", "Godrej Properties", "ICICI Bank Corp",
                "Amazon AWS India", "Microsoft India", "Google Cloud Asia"
            };
            String[] categories = {"IT Services", "Logistics", "Infrastructure", "Consulting", "Cloud Services"};
            
            for (int i = 0; i < vendorNames.length; i++) {
                Vendor vendor = Vendor.builder()
                        .vendorCode("VND-" + (1000 + i))
                        .name(vendorNames[i])
                        .category(categories[i % categories.length])
                        .pan("ABCDE" + (1234 + i) + "F")
                        .gstin("27ABCDE" + (1234 + i) + "F1Z" + i)
                        .msmeCategory(i % 5 == 0 ? MsmeCategory.MEDIUM : MsmeCategory.NONE)
                        .status(i % 10 == 9 ? VendorStatus.UNDER_REVIEW : VendorStatus.ACTIVE)
                        .city(i % 2 == 0 ? "Mumbai" : "Bangalore")
                        .state(i % 2 == 0 ? "Maharashtra" : "Karnataka")
                        .pincode("400001")
                        .contactPerson("Manager " + vendorNames[i].split(" ")[0])
                        .contactEmail("contact@" + vendorNames[i].toLowerCase().replace(" ", "") + ".com")
                        .contactPhone("987654321" + (i % 10))
                        .build();
                vendorRepository.save(vendor);
            }
            log.info("Successfully seeded 15 vendors.");
        }

        // 5. Seed Contracts
        if (contractRepository.count() == 0) {
            log.info("Seeding 35 contracts...");
            List<Vendor> vendors = vendorRepository.findAll();
            User admin = userRepository.findByEmail("admin@orion.com").orElse(null);
            
            String[] commonTitles = {
                "Cloud Infrastructure Support", "Software Licensing Agreement", "Logistics & Supply Chain",
                "FMS Maintenance Contract", "Annual Security Audit", "Legal Consultancy Services",
                "Talent Acquisition Managed Services", "Facility Management Agreement"
            };

            Random random = new Random();
            for (int i = 1; i <= 35; i++) {
                Vendor vendor = vendors.get(random.nextInt(vendors.size()));
                ContractStatus status;
                if (i <= 25) status = ContractStatus.ACTIVE; // 25 active (some will be expiring soon based on date)
                else if (i <= 30) status = ContractStatus.PENDING_APPROVAL;
                else status = ContractStatus.DRAFT;

                LocalDate startDate = LocalDate.now().minusMonths(random.nextInt(12));
                LocalDate endDate;
                if (i <= 5) {
                    // Force these to be "Expiring Soon" (within 30 days)
                    endDate = LocalDate.now().plusDays(random.nextInt(25) + 2);
                } else {
                    endDate = startDate.plusYears(1 + random.nextInt(2));
                }

                Contract contract = Contract.builder()
                        .contractNumber("CTR-" + (2024000 + i))
                        .title(commonTitles[i % commonTitles.length] + " - " + i)
                        .vendor(vendor)
                        .contractType(ContractType.values()[random.nextInt(ContractType.values().length)])
                        .status(status)
                        .startDate(startDate)
                        .endDate(endDate)
                        .contractValue(new BigDecimal(50000 + random.nextInt(950000)))
                        .currency("INR")
                        .department(vendor.getCategory())
                        .createdBy(admin)
                        .paymentTerms("NET 30 days upon invoice receipt")
                        .build();
                contract = contractRepository.save(contract);

                // If pending approval, create a workflow and step
                if (status == ContractStatus.PENDING_APPROVAL) {
                    ApprovalWorkflow workflow = ApprovalWorkflow.builder()
                            .contract(contract)
                            .workflowType(WorkflowType.CONTRACT_APPROVAL)
                            .status(WorkflowStatus.IN_PROGRESS)
                            .totalSteps(1)
                            .currentStep(1)
                            .initiatedBy(admin)
                            .initiatedAt(LocalDateTime.now())
                            .build();
                    workflow = workflowRepository.save(workflow);

                    ApprovalStep step = ApprovalStep.builder()
                            .workflow(workflow)
                            .stepOrder(1)
                            .roleRequired("ROLE_SYSTEM_ADMIN")
                            .assignedToUser(admin)
                            .action(ApprovalAction.PENDING)
                            .dueAt(LocalDateTime.now().plusDays(3))
                            .build();
                    stepRepository.save(step);
                }
            }
            log.info("Successfully seeded 35 contracts and 5 approval tasks.");
        }

        // 6. Seed Performance Evaluations
        if (performanceEvaluationRepository.count() == 0) {
            log.info("Seeding 5 performance evaluations...");
            List<Vendor> vendors = vendorRepository.findAll();
            User admin = userRepository.findByEmail("admin@orion.com").orElse(null);
            Random random = new Random();
            
            for (int i = 0; i < 5; i++) {
                Vendor v = vendors.get(i);
                PerformanceEvaluation eval = PerformanceEvaluation.builder()
                        .vendor(v)
                        .evaluationPeriod("Q1 2024")
                        .qualityScore(70 + random.nextInt(25))
                        .deliveryScore(65 + random.nextInt(30))
                        .communicationScore(80 + random.nextInt(15))
                        .complianceScore(90 + random.nextInt(10))
                        .overallScore(new BigDecimal(80 + random.nextInt(15)))
                        .status(EvaluationStatus.FINALIZED)
                        .evaluator(admin)
                        .finalizedAt(LocalDateTime.now())
                        .build();
                performanceEvaluationRepository.save(eval);
            }
            log.info("Successfully seeded 5 performance evaluations.");
        }
    }

    private Role createRoleIfNotFound(String roleName) {
        return roleRepository.findByRoleName(roleName).orElseGet(() -> {
            Role role = Role.builder().roleName(roleName).build();
            return roleRepository.save(role);
        });
    }
}
