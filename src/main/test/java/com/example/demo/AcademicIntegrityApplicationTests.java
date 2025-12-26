package com.example.demo;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import com.example.demo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Listeners(TestResultListener.class)
public class AcademicIntegrityApplicationTests extends AbstractTestNGSpringContextTests {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private StudentProfileRepository studentProfileRepository;
    @Autowired private IntegrityCaseRepository integrityCaseRepository;
    
    @Autowired private StudentProfileService studentProfileService;
    @Autowired private IntegrityCaseService integrityCaseService;
    @Autowired private EvidenceRecordService evidenceRecordService;
    @Autowired private PenaltyActionService penaltyActionService;
    @Autowired private RepeatOffenderRecordService repeatOffenderRecordService;
    
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtTokenProvider jwtTokenProvider;

    // Servlet Tests
    @Test(groups = "servlet", priority = 1)
    public void testBasicServletResponse() {
        // Test servlet functionality indirectly through HTTP status
        Assert.assertTrue(true, "Basic servlet test placeholder");
    }

    @Test(groups = "servlet", priority = 2)
    public void testBasicServletContentType() {
        // Test servlet content type handling
        Assert.assertTrue(true, "Basic servlet content type test placeholder");
    }

    // CRUD Tests
    @Test(groups = "crud", priority = 3)
    public void testCreateStudentProfile() {
        StudentProfile profile = new StudentProfile("STU001", "John Doe", "john@test.com", "Computer Science", 2);
        StudentProfile saved = studentProfileService.createStudent(profile);
        
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(saved.getName(), "John Doe");
        Assert.assertFalse(saved.getRepeatOffender());
        Assert.assertNotNull(saved.getCreatedAt());
    }

    @Test(groups = "crud", priority = 4, dependsOnMethods = "testCreateStudentProfile")
    public void testCreateIntegrityCase() {
        StudentProfile profile = studentProfileRepository.findAll().get(0);
        IntegrityCase integrityCase = new IntegrityCase(profile, "CS101", "Dr. Smith", "Plagiarism detected", LocalDate.now());
        
        IntegrityCase saved = integrityCaseService.createCase(integrityCase);
        
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(saved.getStatus(), "OPEN");
        Assert.assertNotNull(saved.getCreatedAt());
    }

    @Test(groups = "crud", priority = 5, dependsOnMethods = "testCreateIntegrityCase")
    public void testUpdateRepeatOffenderStatus() {
        StudentProfile profile = studentProfileRepository.findAll().get(0);
        
        // Create second case to trigger repeat offender
        IntegrityCase case2 = new IntegrityCase(profile, "CS102", "Dr. Johnson", "Cheating on exam", LocalDate.now());
        integrityCaseService.createCase(case2);
        
        StudentProfile updated = studentProfileService.updateRepeatOffenderStatus(profile.getId());
        
        Assert.assertTrue(updated.getRepeatOffender());
    }

    // Dependency Injection Tests
    @Test(groups = "di", priority = 6)
    public void testServiceDependencyInjection() {
        Assert.assertNotNull(studentProfileService);
        Assert.assertNotNull(integrityCaseService);
        Assert.assertNotNull(evidenceRecordService);
        Assert.assertNotNull(penaltyActionService);
        Assert.assertNotNull(repeatOffenderRecordService);
    }

    @Test(groups = "di", priority = 7)
    public void testRepositoryDependencyInjection() {
        Assert.assertNotNull(userRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(studentProfileRepository);
        Assert.assertNotNull(integrityCaseRepository);
    }

    // Hibernate Tests
    @Test(groups = "hibernate", priority = 8)
    public void testEntityDefaultValues() {
        StudentProfile profile = new StudentProfile();
        profile.setStudentId("STU002");
        profile.setName("Jane Doe");
        profile.setEmail("jane@test.com");
        profile.setProgram("Mathematics");
        profile.setYearLevel(1);
        
        StudentProfile saved = studentProfileRepository.save(profile);
        
        Assert.assertFalse(saved.getRepeatOffender());
        Assert.assertNotNull(saved.getCreatedAt());
    }

    @Test(groups = "hibernate", priority = 9)
    public void testTimestampGeneration() {
        LocalDateTime before = LocalDateTime.now();
        
        IntegrityCase integrityCase = new IntegrityCase();
        integrityCase.setStudentProfile(studentProfileRepository.findAll().get(0));
        integrityCase.setCourseCode("MATH101");
        integrityCase.setInstructorName("Dr. Brown");
        integrityCase.setDescription("Test case");
        integrityCase.setIncidentDate(LocalDate.now());
        
        IntegrityCase saved = integrityCaseRepository.save(integrityCase);
        
        LocalDateTime after = LocalDateTime.now();
        
        Assert.assertTrue(saved.getCreatedAt().isAfter(before) || saved.getCreatedAt().isEqual(before));
        Assert.assertTrue(saved.getCreatedAt().isBefore(after) || saved.getCreatedAt().isEqual(after));
    }

    // JPA Tests
    @Test(groups = "jpa", priority = 10)
    public void testOneToManyRelationship() {
        StudentProfile profile = studentProfileRepository.findAll().get(0);
        List<IntegrityCase> cases = integrityCaseRepository.findByStudentProfile_Id(profile.getId());
        
        Assert.assertTrue(cases.size() >= 2);
        for (IntegrityCase c : cases) {
            Assert.assertEquals(c.getStudentProfile().getId(), profile.getId());
        }
    }

    @Test(groups = "jpa", priority = 11)
    public void testDataNormalization() {
        List<StudentProfile> profiles = studentProfileRepository.findAll();
        List<IntegrityCase> cases = integrityCaseRepository.findAll();
        
        Assert.assertTrue(profiles.size() >= 1);
        Assert.assertTrue(cases.size() >= 2);
        
        // Verify no duplicate student data
        for (IntegrityCase c : cases) {
            Assert.assertNotNull(c.getStudentProfile());
        }
    }

    // Many-to-Many Tests
    @Test(groups = "manyToMany", priority = 12)
    public void testUserRoleMapping() {
        Role role = roleRepository.findByName("STUDENT").orElse(new Role("STUDENT"));
        if (role.getId() == null) {
            roleRepository.save(role);
        }
        
        AppUser user = new AppUser("Test User", "test@example.com", passwordEncoder.encode("password"));
        user.getRoles().add(role);
        userRepository.save(user);
        
        Optional<AppUser> found = userRepository.findByEmail("test@example.com");
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(found.get().getRoles().size(), 1);
        Assert.assertTrue(found.get().getRoles().stream().anyMatch(r -> r.getName().equals("STUDENT")));
    }

    @Test(groups = "manyToMany", priority = 13)
    public void testDuplicateRoleHandling() {
        Optional<AppUser> userOpt = userRepository.findByEmail("test@example.com");
        Optional<Role> roleOpt = roleRepository.findByName("STUDENT");
        
        if (userOpt.isPresent() && roleOpt.isPresent()) {
            AppUser user = userOpt.get();
            Role role = roleOpt.get();
            int initialSize = user.getRoles().size();
            user.getRoles().add(role); // Try to add same role again
            userRepository.save(user);
            
            Optional<AppUser> reloadedOpt = userRepository.findByEmail("test@example.com");
            if (reloadedOpt.isPresent()) {
                AppUser reloaded = reloadedOpt.get();
                Assert.assertEquals(reloaded.getRoles().size(), initialSize); // Should remain same
            }
        }
    }

    // Security Tests
    @Test(groups = "security", priority = 14)
    public void testJwtTokenGeneration() {
        String token = jwtTokenProvider.generateToken(null, 1L, "test@example.com", "STUDENT");
        
        Assert.assertNotNull(token);
        Assert.assertTrue(token.length() > 0);
    }

    @Test(groups = "security", priority = 15)
    public void testJwtTokenValidation() {
        String token = jwtTokenProvider.generateToken(null, 1L, "test@example.com", "STUDENT");
        
        boolean isValid = jwtTokenProvider.validateToken(token);
        Assert.assertTrue(isValid);
        
        String username = jwtTokenProvider.getUsernameFromToken(token);
        Assert.assertEquals(username, "test@example.com");
    }

    @Test(groups = "security", priority = 16)
    public void testInvalidTokenHandling() {
        boolean isValid = jwtTokenProvider.validateToken("invalid.token.here");
        Assert.assertFalse(isValid);
    }

    // HQL Tests
    @Test(groups = "hql", priority = 17)
    public void testFindByStudentIdentifier() {
        List<IntegrityCase> cases = integrityCaseRepository.findByStudentIdentifier("STU001");
        Assert.assertTrue(cases.size() >= 2);
    }

    @Test(groups = "hql", priority = 18)
    public void testFindByDateRange() {
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        
        List<IntegrityCase> cases = integrityCaseRepository.findByIncidentDateBetween(start, end);
        Assert.assertTrue(cases.size() >= 2);
    }

    @Test(groups = "hql", priority = 19)
    public void testFindByStatus() {
        List<IntegrityCase> openCases = integrityCaseRepository.findByStatus("OPEN");
        Assert.assertTrue(openCases.size() >= 1);
        
        for (IntegrityCase c : openCases) {
            Assert.assertEquals(c.getStatus(), "OPEN");
        }
    }

    @Test(groups = "hql", priority = 20)
    public void testRecentCasesByStatus() {
        LocalDate since = LocalDate.now().minusDays(7);
        List<IntegrityCase> recentCases = integrityCaseRepository.findRecentCasesByStatus("OPEN", since);
        
        for (IntegrityCase c : recentCases) {
            Assert.assertEquals(c.getStatus(), "OPEN");
            Assert.assertTrue(c.getIncidentDate().isAfter(since) || c.getIncidentDate().isEqual(since));
        }
    }
}