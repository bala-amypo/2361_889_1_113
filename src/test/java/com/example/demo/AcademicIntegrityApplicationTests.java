package com.example.demo;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import com.example.demo.servlet.BasicServlet;
import com.example.demo.util.RepeatOffenderCalculator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Listeners(TestResultListener.class)
public class AcademicIntegrityApplicationTests {

    // region Mocks and services

    @Mock
    private StudentProfileRepository studentProfileRepository;
    @Mock
    private IntegrityCaseRepository integrityCaseRepository;
    @Mock
    private EvidenceRecordRepository evidenceRecordRepository;
    @Mock
    private PenaltyActionRepository penaltyActionRepository;
    @Mock
    private RepeatOffenderRecordRepository repeatOffenderRecordRepository;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;

    private StudentProfileService studentProfileService;
    private IntegrityCaseService integrityCaseService;
    private EvidenceRecordService evidenceRecordService;
    private PenaltyActionService penaltyActionService;
    private RepeatOffenderRecordService repeatOffenderRecordService;
    private AuthServiceImpl authServiceImpl;

    private RepeatOffenderCalculator calculator;

    @BeforeClass
    public void init() {
        MockitoAnnotations.openMocks(this);
        calculator = new RepeatOffenderCalculator();

        studentProfileService = new StudentProfileServiceImpl(
                studentProfileRepository,
                integrityCaseRepository,
                repeatOffenderRecordRepository,
                calculator
        );

        integrityCaseService = new IntegrityCaseServiceImpl(
                integrityCaseRepository,
                studentProfileRepository
        );

        evidenceRecordService = new EvidenceRecordServiceImpl(
                evidenceRecordRepository,
                integrityCaseRepository
        );

        penaltyActionService = new PenaltyActionServiceImpl(
            penaltyActionRepository,
            integrityCaseRepository
        );

        repeatOffenderRecordService = new RepeatOffenderRecordServiceImpl(
                studentProfileRepository,
                integrityCaseRepository,
                repeatOffenderRecordRepository,
                calculator
        );

        authServiceImpl = new AuthServiceImpl(
                appUserRepository,
                roleRepository,
                passwordEncoder,
                authenticationManager,
                jwtTokenProvider
        );
    }

    // Helper subclass to make servlet methods public for testing
    private static class TestableServlet extends BasicServlet {
        @Override
        public void doGet(HttpServletRequest req, HttpServletResponse resp) {
            try {
                super.doGet(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void doPost(HttpServletRequest req, HttpServletResponse resp) {
            try {
                super.doPost(req, resp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Helper methods to create sample entities

    private StudentProfile sampleStudent(Long id) {
        StudentProfile s = new StudentProfile();
        s.setId(id);
        s.setStudentId("S" + id);
        s.setName("Student " + id);
        s.setEmail("student" + id + "@test.com");
        s.setProgram("CS");
        s.setYearLevel(2);
        s.setRepeatOffender(false);
        s.setCreatedAt(LocalDateTime.now());
        return s;
    }

    private IntegrityCase sampleCase(Long id, StudentProfile student) {
        IntegrityCase c = new IntegrityCase();
        c.setId(id);
        c.setStudentProfile(student);
        c.setCourseCode("CS101");
        c.setInstructorName("Dr. Smith");
        c.setDescription("Cheating on exam");
        c.setStatus("OPEN");
        c.setIncidentDate(LocalDate.now());
        c.setCreatedAt(LocalDateTime.now());
        return c;
    }

    private EvidenceRecord sampleEvidence(Long id, IntegrityCase c) {
        EvidenceRecord e = new EvidenceRecord();
        e.setId(id);
        e.setIntegrityCase(c);
        e.setEvidenceType("TEXT");
        e.setContent("Evidence details");
        e.setSubmittedBy("Faculty");
        e.setSubmittedAt(LocalDateTime.now());
        return e;
    }

    private PenaltyAction samplePenalty(Long id, IntegrityCase c) {
        PenaltyAction p = new PenaltyAction();
        p.setId(id);
        p.setIntegrityCase(c);
        p.setPenaltyType("WARNING");
        p.setDetails("First warning");
        p.setIssuedBy("Committee");
        p.setIssuedAt(LocalDateTime.now());
        return p;
    }

    // ------------------------------------------------------------------------
    // 1) Develop and deploy a simple servlet using Tomcat Server (1-8)
    // ------------------------------------------------------------------------

    @Test(groups = "servlet", priority = 1)
    public void testServletDoGetReturnsOk() throws Exception {
        TestableServlet servlet = new TestableServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        writer.flush();
        Assert.assertTrue(sw.toString().contains("Servlet is running"));
    }

    @Test(groups = "servlet", priority = 2)
    public void testServletDoPostReturnsCreated() throws Exception {
        TestableServlet servlet = new TestableServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        writer.flush();
        Assert.assertTrue(sw.toString().contains("Servlet POST handled"));
    }

    @Test(groups = "servlet", priority = 3)
    public void testServletDoGetHandlesNullRequest() throws Exception {
        TestableServlet servlet = new TestableServlet();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.doGet(null, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test(groups = "servlet", priority = 4)
    public void testServletHandlesMultipleSequentialCalls() throws Exception {
        TestableServlet servlet = new TestableServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.doGet(request, response);
        servlet.doGet(request, response);
        servlet.doPost(request, response);

        verify(response, atLeast(3)).getWriter();
    }

    @Test(groups = "servlet", priority = 5)
    public void testServletResponseWriterIsRequested() throws Exception {
        TestableServlet servlet = new TestableServlet();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.doGet(null, response);

        verify(response).getWriter();
    }

    @Test(groups = "servlet", priority = 6)
    public void testServletGetContentNotEmpty() throws Exception {
        TestableServlet servlet = new TestableServlet();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        servlet.doGet(null, response);

        Assert.assertFalse(sw.toString().isEmpty());
    }

    @Test(groups = "servlet", priority = 7)
    public void testServletPostContentNotEmpty() throws Exception {
        TestableServlet servlet = new TestableServlet();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        servlet.doPost(null, response);

        Assert.assertFalse(sw.toString().isEmpty());
    }

    @Test(groups = "servlet", priority = 8, expectedExceptions = RuntimeException.class)
    public void testServletHandlesWriterExceptionGracefully() throws Exception {
        TestableServlet servlet = new TestableServlet();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenThrow(new RuntimeException("IO error"));

        servlet.doGet(null, response);
    }

    // ------------------------------------------------------------------------
    // 2) Implement CRUD operations using Spring Boot and REST APIs (9-23)
    // ------------------------------------------------------------------------

    @Test(groups = "crud", priority = 9)
    public void testCreateStudentProfileSuccess() {
        StudentProfile s = sampleStudent(1L);
        when(studentProfileRepository.save(any(StudentProfile.class))).thenReturn(s);

        StudentProfile created = studentProfileService.createStudent(s);

        Assert.assertNotNull(created);
        Assert.assertEquals(created.getStudentId(), "S1");
        verify(studentProfileRepository).save(any(StudentProfile.class));
    }

    @Test(groups = "crud", priority = 10)
    public void testCreateStudentProfileSetsRepeatOffenderFalse() {
        StudentProfile s = sampleStudent(2L);
        s.setRepeatOffender(true);
        when(studentProfileRepository.save(any(StudentProfile.class))).thenAnswer(invocation -> {
            StudentProfile arg = invocation.getArgument(0);
            return arg;
        });

        StudentProfile created = studentProfileService.createStudent(s);

        Assert.assertFalse(created.getRepeatOffender());
    }

    @Test(groups = "crud", priority = 11)
    public void testGetStudentByIdFound() {
        StudentProfile s = sampleStudent(3L);
        when(studentProfileRepository.findById(3L)).thenReturn(Optional.of(s));

        StudentProfile result = studentProfileService.getStudentById(3L);

        Assert.assertEquals(result.getId(), Long.valueOf(3L));
    }

    @Test(groups = "crud", priority = 12, expectedExceptions = ResourceNotFoundException.class)
    public void testGetStudentByIdNotFoundThrows() {
        when(studentProfileRepository.findById(99L)).thenReturn(Optional.empty());
        studentProfileService.getStudentById(99L);
    }

    @Test(groups = "crud", priority = 13)
    public void testGetAllStudentsReturnsList() {
        when(studentProfileRepository.findAll())
                .thenReturn(Arrays.asList(sampleStudent(1L), sampleStudent(2L)));

        List<StudentProfile> students = studentProfileService.getAllStudents();

        Assert.assertEquals(students.size(), 2);
    }

    @Test(groups = "crud", priority = 14)
    public void testUpdateRepeatOffenderStatusWithTwoCasesMarksRepeat() {
        StudentProfile s = sampleStudent(4L);
        IntegrityCase c1 = sampleCase(1L, s);
        IntegrityCase c2 = sampleCase(2L, s);

        when(studentProfileRepository.findById(4L)).thenReturn(Optional.of(s));
        when(integrityCaseRepository.findByStudentProfile(s)).thenReturn(Arrays.asList(c1, c2));
        when(repeatOffenderRecordRepository.findByStudentProfile(s)).thenReturn(Optional.empty());
        when(repeatOffenderRecordRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(studentProfileRepository.save(any(StudentProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentProfile updated = studentProfileService.updateRepeatOffenderStatus(4L);

        Assert.assertTrue(updated.getRepeatOffender());
    }

    @Test(groups = "crud", priority = 15)
    public void testUpdateRepeatOffenderStatusWithNoCasesNotRepeat() {
        StudentProfile s = sampleStudent(5L);
        when(studentProfileRepository.findById(5L)).thenReturn(Optional.of(s));
        when(integrityCaseRepository.findByStudentProfile(s)).thenReturn(Collections.emptyList());
        when(repeatOffenderRecordRepository.findByStudentProfile(s)).thenReturn(Optional.empty());
        when(repeatOffenderRecordRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(studentProfileRepository.save(any(StudentProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentProfile updated = studentProfileService.updateRepeatOffenderStatus(5L);

        Assert.assertFalse(updated.getRepeatOffender());
    }

    @Test(groups = "crud", priority = 16)
    public void testCreateIntegrityCaseWithValidStudent() {
        StudentProfile s = sampleStudent(6L);
        IntegrityCase integrityCase = sampleCase(10L, s);
        integrityCase.setStudentProfile(s);

        when(studentProfileRepository.findById(6L)).thenReturn(Optional.of(s));
        when(integrityCaseRepository.save(any(IntegrityCase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IntegrityCase created = integrityCaseService.createCase(integrityCase);

        Assert.assertEquals(created.getStatus(), "OPEN");
        Assert.assertEquals(created.getStudentProfile().getId(), Long.valueOf(6L));
    }

    @Test(groups = "crud", priority = 17, expectedExceptions = IllegalArgumentException.class)
    public void testCreateIntegrityCaseMissingStudentThrows() {
        IntegrityCase integrityCase = new IntegrityCase();
        integrityCaseService.createCase(integrityCase);
    }

    @Test(groups = "crud", priority = 18)
    public void testUpdateIntegrityCaseStatusSuccess() {
        StudentProfile s = sampleStudent(7L);
        IntegrityCase c = sampleCase(20L, s);
        when(integrityCaseRepository.findById(20L)).thenReturn(Optional.of(c));
        when(integrityCaseRepository.save(any(IntegrityCase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IntegrityCase updated = integrityCaseService.updateCaseStatus(20L, "RESOLVED");

        Assert.assertEquals(updated.getStatus(), "RESOLVED");
    }

    @Test(groups = "crud", priority = 19)
    public void testGetCasesByStudentReturnsList() {
        StudentProfile s = sampleStudent(8L);
        List<IntegrityCase> list = Arrays.asList(sampleCase(1L, s), sampleCase(2L, s));
        when(integrityCaseRepository.findByStudentProfile_Id(8L)).thenReturn(list);

        List<IntegrityCase> result = integrityCaseService.getCasesByStudent(8L);

        Assert.assertEquals(result.size(), 2);
    }

    @Test(groups = "crud", priority = 20)
    public void testGetCaseByIdPresent() {
        StudentProfile s = sampleStudent(9L);
        IntegrityCase c = sampleCase(30L, s);
        when(integrityCaseRepository.findById(30L)).thenReturn(Optional.of(c));

        Optional<IntegrityCase> result = integrityCaseService.getCaseById(30L);

        Assert.assertTrue(result.isPresent());
    }

    @Test(groups = "crud", priority = 21)
    public void testGetCaseByIdAbsentReturnsEmpty() {
        when(integrityCaseRepository.findById(40L)).thenReturn(Optional.empty());

        Optional<IntegrityCase> result = integrityCaseService.getCaseById(40L);

        Assert.assertFalse(result.isPresent());
    }

    @Test(groups = "crud", priority = 22)
    public void testSubmitEvidenceSuccess() {
        StudentProfile s = sampleStudent(10L);
        IntegrityCase c = sampleCase(50L, s);
        EvidenceRecord e = sampleEvidence(1L, c);

        when(integrityCaseRepository.findById(50L)).thenReturn(Optional.of(c));
        when(evidenceRecordRepository.save(any(EvidenceRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EvidenceRecord saved = evidenceRecordService.submitEvidence(e);

        Assert.assertEquals(saved.getIntegrityCase().getId(), Long.valueOf(50L));
    }

    @Test(groups = "crud", priority = 23)
    public void testAddPenaltyMovesCaseToUnderReview() {
        StudentProfile s = sampleStudent(11L);
        IntegrityCase c = sampleCase(60L, s);
        c.setStatus("OPEN");
        PenaltyAction p = samplePenalty(1L, c);

        when(integrityCaseRepository.findById(60L)).thenReturn(Optional.of(c));
        when(integrityCaseRepository.save(any(IntegrityCase.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(penaltyActionRepository.save(any(PenaltyAction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PenaltyAction result = penaltyActionService.addPenalty(p);

        Assert.assertEquals(result.getIntegrityCase().getStatus(), "UNDER_REVIEW");
    }

    // ------------------------------------------------------------------------
    // 3) Dependency Injection and IoC using Spring Framework (24-31)
    // ------------------------------------------------------------------------

   
    @Test(groups = "di", priority = 25)
    public void testIntegrityCaseServiceUsesStudentRepositoryOnCreate() {
        StudentProfile s = sampleStudent(13L);
        IntegrityCase c = sampleCase(70L, s);
        when(studentProfileRepository.findById(13L)).thenReturn(Optional.of(s));
        when(integrityCaseRepository.save(any(IntegrityCase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        c.setStudentProfile(s);
        IntegrityCase created = integrityCaseService.createCase(c);

        Assert.assertNotNull(created);
        verify(studentProfileRepository, times(1)).findById(13L);
    }

  

    @Test(groups = "di", priority = 29)
    public void testAuthServiceRegisterEncodesPasswordAndSavesUser() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("faculty@test.com");
        req.setPassword("plain");
        req.setFullName("Faculty User");
        req.setRole("FACULTY");

        Role role = new Role("FACULTY");

        when(appUserRepository.existsByEmail("faculty@test.com")).thenReturn(false);
        when(roleRepository.findByName("FACULTY")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("plain")).thenReturn("ENCODED");

        authServiceImpl.register(req);

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(captor.capture());
        Assert.assertEquals(captor.getValue().getPassword(), "ENCODED");
    }

    @Test(groups = "di", priority = 30, expectedExceptions = IllegalArgumentException.class)
    public void testAuthServiceRegisterDuplicateEmailThrows() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("faculty@test.com");
        req.setPassword("plain");
        req.setFullName("Faculty User");
        req.setRole("FACULTY");

        when(appUserRepository.existsByEmail("faculty@test.com")).thenReturn(true);

        authServiceImpl.register(req);
    }

    @Test(groups = "di", priority = 31)
    public void testAuthServiceLoginReturnsJwtResponse() {
        LoginRequest login = new LoginRequest();
        login.setEmail("faculty@test.com");
        login.setPassword("plain");

        AppUser user = new AppUser();
        user.setId(1L);
        user.setEmail("faculty@test.com");
        user.setPassword("ENCODED");
        Role role = new Role("FACULTY");
        user.getRoles().add(role);

        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(appUserRepository.findByEmail("faculty@test.com")).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(eq(auth), eq(1L), eq("faculty@test.com"), eq("FACULTY"))).thenReturn("TOKEN");

        JwtResponse response = authServiceImpl.login(login);

        Assert.assertEquals(response.getToken(), "TOKEN");
        Assert.assertEquals(response.getEmail(), "faculty@test.com");
        Assert.assertEquals(response.getRole(), "FACULTY");
    }

    // ------------------------------------------------------------------------
    // 4) Hibernate configs, generator classes, annotations, CRUD (32-41)
    // ------------------------------------------------------------------------

    @Test(groups = "hibernate", priority = 32)
    public void testStudentProfileDefaults() {
        StudentProfile s = new StudentProfile();
        s.setStudentId("S100");
        s.setName("Name");
        s.setEmail("email@test.com");
        s.setProgram("CS");
        s.setYearLevel(1);

        Assert.assertNotNull(s.getCreatedAt());
        Assert.assertFalse(s.getRepeatOffender());
    }

    @Test(groups = "hibernate", priority = 33)
    public void testIntegrityCaseDefaultStatusOpen() {
        StudentProfile s = sampleStudent(20L);
        IntegrityCase c = new IntegrityCase();
        c.setStudentProfile(s);
        c.setCourseCode("CS101");
        c.setInstructorName("Inst");
        c.setDescription("desc");
        c.setIncidentDate(LocalDate.now());

        Assert.assertEquals(c.getStatus(), "OPEN");
    }

    @Test(groups = "hibernate", priority = 34)
    public void testEvidenceRecordHasTimestamp() {
        StudentProfile s = sampleStudent(21L);
        IntegrityCase c = sampleCase(200L, s);
        EvidenceRecord e = new EvidenceRecord();
        e.setIntegrityCase(c);
        e.setEvidenceType("TEXT");
        e.setContent("content");
        e.setSubmittedBy("faculty");

        Assert.assertNotNull(e.getSubmittedAt());
    }

    @Test(groups = "hibernate", priority = 35)
    public void testPenaltyActionHasTimestamp() {
        StudentProfile s = sampleStudent(22L);
        IntegrityCase c = sampleCase(210L, s);
        PenaltyAction p = new PenaltyAction();
        p.setIntegrityCase(c);
        p.setPenaltyType("WARNING");
        p.setDetails("details");
        p.setIssuedBy("committee");

        Assert.assertNotNull(p.getIssuedAt());
    }

    @Test(groups = "hibernate", priority = 36)
    public void testRepeatOffenderCalculatorSeverityLowForOneCase() {
        StudentProfile s = sampleStudent(23L);
        IntegrityCase c = sampleCase(220L, s);

        RepeatOffenderRecord record = calculator.computeRepeatOffenderRecord(s, List.of(c));

        Assert.assertEquals(record.getTotalCases().intValue(), 1);
        Assert.assertEquals(record.getFlagSeverity(), "LOW");
    }

    @Test(groups = "hibernate", priority = 37)
    public void testRepeatOffenderCalculatorSeverityMediumForTwoCases() {
        StudentProfile s = sampleStudent(24L);
        IntegrityCase c1 = sampleCase(230L, s);
        IntegrityCase c2 = sampleCase(231L, s);

        RepeatOffenderRecord record = calculator.computeRepeatOffenderRecord(s, List.of(c1, c2));

        Assert.assertEquals(record.getFlagSeverity(), "MEDIUM");
    }

    @Test(groups = "hibernate", priority = 38)
    public void testRepeatOffenderCalculatorSeverityHighForFourCases() {
        StudentProfile s = sampleStudent(25L);
        List<IntegrityCase> cases = Arrays.asList(
                sampleCase(240L, s),
                sampleCase(241L, s),
                sampleCase(242L, s),
                sampleCase(243L, s)
        );

        RepeatOffenderRecord record = calculator.computeRepeatOffenderRecord(s, cases);

        Assert.assertEquals(record.getFlagSeverity(), "HIGH");
    }

    @Test(groups = "hibernate", priority = 39)
    public void testIntegrityCaseLinksToStudentProfile() {
        StudentProfile s = sampleStudent(26L);
        IntegrityCase c = sampleCase(250L, s);

        Assert.assertEquals(c.getStudentProfile(), s);
    }

    @Test(groups = "hibernate", priority = 40)
    public void testEvidenceRecordLinksToIntegrityCase() {
        StudentProfile s = sampleStudent(27L);
        IntegrityCase c = sampleCase(260L, s);
        EvidenceRecord e = sampleEvidence(5L, c);

        Assert.assertEquals(e.getIntegrityCase(), c);
    }

    @Test(groups = "hibernate", priority = 41)
    public void testPenaltyActionLinksToIntegrityCase() {
        StudentProfile s = sampleStudent(28L);
        IntegrityCase c = sampleCase(270L, s);
        PenaltyAction p = samplePenalty(6L, c);

        Assert.assertEquals(p.getIntegrityCase(), c);
    }

    // ------------------------------------------------------------------------
    // 5) JPA mapping with normalization (1NF, 2NF, 3NF) (42-49)
    // ------------------------------------------------------------------------

    @Test(groups = "jpa", priority = 42)
    public void testStudentProfileHasCasesCollectionNotNull() {
        StudentProfile s = new StudentProfile();
        Assert.assertNotNull(s.getIntegrityCases());
    }

    @Test(groups = "jpa", priority = 43)
    public void testRepeatOffenderRecordReferencesValidStudent() {
        StudentProfile s = sampleStudent(29L);
        RepeatOffenderRecord record = new RepeatOffenderRecord();
        record.setStudentProfile(s);
        record.setTotalCases(2);
        record.setFlagSeverity("MEDIUM");

        Assert.assertEquals(record.getStudentProfile(), s);
    }

    @Test(groups = "jpa", priority = 44)
    public void testEvidenceRecordReferencesCaseNotNull() {
        StudentProfile s = sampleStudent(30L);
        IntegrityCase c = sampleCase(280L, s);
        EvidenceRecord e = sampleEvidence(7L, c);

        Assert.assertNotNull(e.getIntegrityCase());
    }

    @Test(groups = "jpa", priority = 45)
    public void testPenaltyRecordReferencesCaseNotNull() {
        StudentProfile s = sampleStudent(31L);
        IntegrityCase c = sampleCase(290L, s);
        PenaltyAction p = samplePenalty(8L, c);

        Assert.assertNotNull(p.getIntegrityCase());
    }

    @Test(groups = "jpa", priority = 46)
    public void testStudentProfileDoesNotDuplicateCaseAttributes() {
        StudentProfile s = sampleStudent(32L);
        // Normalization: no courseCode or incidentDate in StudentProfile
        // We just ensure the fields we expect exist and are separate.
        Assert.assertNotNull(s.getProgram());
        Assert.assertNull(s.getIntegrityCases().isEmpty() ? null : s.getIntegrityCases().get(0).getIncidentDate());
    }

    @Test(groups = "jpa", priority = 47)
    public void testCaseDoesNotDuplicatePenaltyAttributes() {
        StudentProfile s = sampleStudent(33L);
        IntegrityCase c = sampleCase(300L, s);
        // Case has penalties list; penalties own "details" and "issuedBy"
        Assert.assertNotNull(c.getPenalties());
    }

    @Test(groups = "jpa", priority = 48)
    public void testYearLevelNotNullConstraintLogicalCheck() {
        StudentProfile s = sampleStudent(34L);
        Assert.assertNotNull(s.getYearLevel());
    }

    @Test(groups = "jpa", priority = 49)
    public void testRepeatOffenderStatusConsistentWithRecord() {
        StudentProfile s = sampleStudent(35L);
        IntegrityCase c1 = sampleCase(310L, s);
        IntegrityCase c2 = sampleCase(311L, s);
        List<IntegrityCase> cases = Arrays.asList(c1, c2);

        RepeatOffenderRecord rec = calculator.computeRepeatOffenderRecord(s, cases);
        s.setRepeatOffender(rec.getTotalCases() >= 2);

        Assert.assertTrue(s.getRepeatOffender());
        Assert.assertEquals(rec.getFlagSeverity(), "MEDIUM");
    }

    // ------------------------------------------------------------------------
    // 6) Many-to-Many relationships and associations in Spring Boot (50-57)
    // ------------------------------------------------------------------------

    @Test(groups = "manyToMany", priority = 50)
    public void testUserCanHaveSingleRole() {
        AppUser user = new AppUser();
        user.setEmail("user@test.com");
        user.setPassword("p");
        Role role = new Role("FACULTY");
        user.getRoles().add(role);

        Assert.assertEquals(user.getRoles().size(), 1);
    }

    @Test(groups = "manyToMany", priority = 51)
    public void testUserCanHaveMultipleRoles() {
        AppUser user = new AppUser();
        Role r1 = new Role("FACULTY");
        Role r2 = new Role("ADMIN");
        user.getRoles().add(r1);
        user.getRoles().add(r2);

        Assert.assertEquals(user.getRoles().size(), 2);
    }

    @Test(groups = "manyToMany", priority = 52)
    public void testRolesPersistedViaRepositoryMock() {
        Role r1 = new Role("FACULTY");
        when(roleRepository.save(any(Role.class))).thenReturn(r1);

        Role saved = roleRepository.save(r1);

        Assert.assertEquals(saved.getName(), "FACULTY");
    }

    @Test(groups = "manyToMany", priority = 53)
    public void testUserRepositoryFindByEmailReturnsUser() {
        AppUser user = new AppUser();
        user.setEmail("user@test.com");
        when(appUserRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        Optional<AppUser> found = appUserRepository.findByEmail("user@test.com");

        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(found.get().getEmail(), "user@test.com");
    }

    @Test(groups = "manyToMany", priority = 54)
    public void testRoleRepositoryFindByNameReturnsRole() {
        Role role = new Role("REVIEWER");
        when(roleRepository.findByName("REVIEWER")).thenReturn(Optional.of(role));

        Optional<Role> found = roleRepository.findByName("REVIEWER");

        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(found.get().getName(), "REVIEWER");
    }

    @Test(groups = "manyToMany", priority = 55)
    public void testCustomUserDetailsServiceMapsRolesToAuthorities() {
        AppUser user = new AppUser();
        user.setEmail("user@test.com");
        user.setPassword("ENC");
        user.getRoles().add(new Role("FACULTY"));
        user.getRoles().add(new Role("ADMIN"));

        when(appUserRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        CustomUserDetailsService uds = new CustomUserDetailsService(appUserRepository);
        UserDetails details = uds.loadUserByUsername("user@test.com");

        Assert.assertEquals(details.getUsername(), "user@test.com");
        Assert.assertEquals(details.getAuthorities().size(), 2);
    }

    @Test(groups = "manyToMany", priority = 56)
    public void testUserWithNoRolesStillValidEntity() {
        AppUser user = new AppUser();
        user.setEmail("norole@test.com");
        user.setPassword("ENC");

        Assert.assertNotNull(user.getRoles());
        Assert.assertEquals(user.getRoles().size(), 0);
    }

    @Test(groups = "manyToMany", priority = 57)
    public void testDuplicateRolesNotAddedBecauseSet() {
        AppUser user = new AppUser();
        Role r = new Role("FACULTY");
        user.getRoles().add(r);
        user.getRoles().add(r);

        Assert.assertEquals(user.getRoles().size(), 1);
    }

    // ------------------------------------------------------------------------
    // 7) Basic security controls and JWT token-based authentication (58-64)
    // ------------------------------------------------------------------------

    @Test(groups = "security", priority = 58)
    public void testJwtTokenContainsEmailClaim() {
        JwtTokenProvider provider = new JwtTokenProvider(
                "MyVerySecretKeyForJwt123456789012345",
                60000L
        );

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@test.com");
        String token = provider.generateToken(auth, 1L, "user@test.com", "FACULTY");

        String email = provider.getUsernameFromToken(token);
        Assert.assertEquals(email, "user@test.com");
    }

    @Test(groups = "security", priority = 59)
    public void testJwtTokenIsValid() {
        JwtTokenProvider provider = new JwtTokenProvider(
                "MyVerySecretKeyForJwt123456789012345",
                60000L
        );
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@test.com");

        String token = provider.generateToken(auth, 2L, "user@test.com", "ADMIN");

        Assert.assertTrue(provider.validateToken(token));
    }

    @Test(groups = "security", priority = 60)
    public void testJwtTokenInvalidWhenTampered() {
        JwtTokenProvider provider = new JwtTokenProvider(
                "MyVerySecretKeyForJwt123456789012345",
                60000L
        );
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@test.com");

        String token = provider.generateToken(auth, 3L, "user@test.com", "ADMIN");
        String tampered = token + "x";

        Assert.assertFalse(provider.validateToken(tampered));
    }

    @Test(groups = "security", priority = 61)
    public void testCustomUserDetailsServiceThrowsForUnknownUser() {
        when(appUserRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());
        CustomUserDetailsService uds = new CustomUserDetailsService(appUserRepository);

        try {
            uds.loadUserByUsername("unknown@test.com");
            Assert.fail("Expected exception");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("User not found"));
        }
    }

    @Test(groups = "security", priority = 62)
    public void testJwtTokenIncludesUserIdAndRoleClaims() {
        JwtTokenProvider provider = new JwtTokenProvider(
                "MyVerySecretKeyForJwt123456789012345",
                60000L
        );
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@test.com");

        String token = provider.generateToken(auth, 5L, "user@test.com", "REVIEWER");

        Assert.assertTrue(provider.validateToken(token));
        // We validate mostly by successful parsing; detailed claim decoding is internal.
    }

    @Test(groups = "security", priority = 63, expectedExceptions = IllegalArgumentException.class)
    public void testAuthServiceLoginFailsWhenUserNotFound() {
        LoginRequest login = new LoginRequest();
        login.setEmail("missing@test.com");
        login.setPassword("plain");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(appUserRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        authServiceImpl.login(login);
    }

  

    // ------------------------------------------------------------------------
    // 8) Use HQL and HCQL to perform advanced data querying (65-70)
    // ------------------------------------------------------------------------

    @Test(groups = "hql", priority = 65)
    public void testFindByStudentIdentifierReturnsCases() {
        StudentProfile s = sampleStudent(40L);
        IntegrityCase c1 = sampleCase(400L, s);
        List<IntegrityCase> list = List.of(c1);

        when(integrityCaseRepository.findByStudentIdentifier("S40")).thenReturn(list);

        List<IntegrityCase> result = integrityCaseRepository.findByStudentIdentifier("S40");

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getStudentProfile().getStudentId(), "S40");
    }

    @Test(groups = "hql", priority = 66)
    public void testFindRecentCasesByStatusReturnsFilteredList() {
        StudentProfile s = sampleStudent(41L);
        IntegrityCase recent = sampleCase(410L, s);

        when(integrityCaseRepository.findRecentCasesByStatus(eq("OPEN"), any(LocalDate.class)))
                .thenReturn(List.of(recent));

        List<IntegrityCase> result = integrityCaseRepository.findRecentCasesByStatus("OPEN", LocalDate.now().minusDays(7));

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getStatus(), "OPEN");
    }

    @Test(groups = "hql", priority = 67)
    public void testHqlQueryReturnsEmptyForUnknownStudent() {
        when(integrityCaseRepository.findByStudentIdentifier("UNKNOWN")).thenReturn(Collections.emptyList());

        List<IntegrityCase> result = integrityCaseRepository.findByStudentIdentifier("UNKNOWN");

        Assert.assertTrue(result.isEmpty());
    }

    @Test(groups = "hql", priority = 68)
    public void testHqlQueryMultipleCasesForStudent() {
        StudentProfile s = sampleStudent(42L);
        IntegrityCase c1 = sampleCase(420L, s);
        IntegrityCase c2 = sampleCase(421L, s);

        when(integrityCaseRepository.findByStudentIdentifier("S42")).thenReturn(List.of(c1, c2));

        List<IntegrityCase> result = integrityCaseRepository.findByStudentIdentifier("S42");

        Assert.assertEquals(result.size(), 2);
    }

    @Test(groups = "hql", priority = 69)
    public void testHqlDateRangeQuerySimulation() {
        StudentProfile s = sampleStudent(43L);
        IntegrityCase c1 = sampleCase(430L, s);
        List<IntegrityCase> list = List.of(c1);

        when(integrityCaseRepository.findByIncidentDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(list);

        List<IntegrityCase> result = integrityCaseRepository.findByIncidentDateBetween(
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        );

        Assert.assertEquals(result.size(), 1);
    }

    @Test(groups = "hql", priority = 70)
    public void testHqlStatusFilterReturnsOnlyMatchingStatus() {
        StudentProfile s = sampleStudent(44L);
        IntegrityCase openCase = sampleCase(440L, s);
        openCase.setStatus("OPEN");
        IntegrityCase resolvedCase = sampleCase(441L, s);
        resolvedCase.setStatus("RESOLVED");

        when(integrityCaseRepository.findByStatus("OPEN")).thenReturn(List.of(openCase));

        List<IntegrityCase> openList = integrityCaseRepository.findByStatus("OPEN");

        Assert.assertEquals(openList.size(), 1);
        Assert.assertEquals(openList.get(0).getStatus(), "OPEN");
    }
}
