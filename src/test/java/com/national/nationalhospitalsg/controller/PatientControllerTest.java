package com.national.nationalhospitalsg.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.national.nationalhospitalsg.model.Patient;
import com.national.nationalhospitalsg.service.impl.PatientServicesImpl;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    @Mock
    private PatientServicesImpl patientServices;

    @Mock
    private HttpServletRequest request;

    @Mock
    private FacesContext facesContext;

    @Mock
    private ExternalContext externalContext;

    @Mock
    private Logger logger;

    @Mock
    private Logger auditLogger;

    @InjectMocks
    private PatientController patientController;

    private Patient testPatient;
    private List<Patient> testPatients;

    @BeforeEach
    public void setUp() {
        testPatient = new Patient();
        testPatient.setId(1);
        testPatient.setName("Test Patient");
        testPatients = new ArrayList<>();
        testPatients.add(testPatient);
        patientController.setSelectedPatient(new Patient());
    }

    @Test
    public void testLoadParents() {
        when(patientServices.getAllPatients()).thenReturn(testPatients);
        patientController.loadParents();
        assertEquals(1, patientController.getPatients().size());
        verify(patientServices).getAllPatients();
    }

    @Test
    public void testSavePatientCreateNew() {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            FacesContext facesContext = mock(FacesContext.class);
            ExternalContext externalContext = mock(ExternalContext.class);
            Map<String, Object> sessionMap = new HashMap<>();
            when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            when(externalContext.getSessionMap()).thenReturn(sessionMap);
            when(request.getSession()).thenReturn(mock(HttpSession.class));
            when(request.getRemoteAddr()).thenReturn("198.165.25.36");

            // Test logic
            Patient newPatient = new Patient();
            newPatient.setName("Ramesh Pongiannan");
            patientController.setSelectedPatient(newPatient);

            when(patientServices.createOrUpdatePatient(any(Patient.class))).thenReturn(newPatient);
            patientController.savePatient();
            verify(patientServices).createOrUpdatePatient(newPatient);
            assertNotNull(patientController.getSelectedPatient());
        }
    }

    @Test
    public void testUpdatePatient() {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            FacesContext facesContext = mock(FacesContext.class);
            ExternalContext externalContext = mock(ExternalContext.class);
            Map<String, Object> sessionMap = new HashMap<>();
            when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            when(externalContext.getSessionMap()).thenReturn(sessionMap);
            when(request.getSession()).thenReturn(mock(HttpSession.class));
            when(request.getRemoteAddr()).thenReturn("127.0.0.1");

            // Test logic
            Patient newPatient = new Patient();
            newPatient.setName("New Patient");
            patientController.setSelectedPatient(newPatient);

            when(patientServices.createOrUpdatePatient(any(Patient.class))).thenReturn(newPatient);
            patientController.savePatient();
            verify(patientServices).createOrUpdatePatient(newPatient);
            assertNotNull(patientController.getSelectedPatient());
        }
    }


    @Test
    public void testResetForm() {
        patientController.setSelectedPatient(testPatient);
        patientController.resetForm();
        assertNotEquals(testPatient, patientController.getSelectedPatient());
        assertNotNull(patientController.getSelectedPatient());
    }

    @Test
    public void testPrepareNew() {
        patientController.setSelectedPatient(testPatient);
        patientController.prepareNew();
        assertNotEquals(testPatient, patientController.getSelectedPatient());
        assertNotNull(patientController.getSelectedPatient());
    }

    @Test
    public void testDeleteParent() {
        try (MockedStatic<FacesContext> mockedStatic = mockStatic(FacesContext.class)) {
            // Mock FacesContext
            FacesContext facesContext = mock(FacesContext.class);
            ExternalContext externalContext = mock(ExternalContext.class);
            Map<String, Object> sessionMap = new HashMap<>();

            mockedStatic.when(FacesContext::getCurrentInstance).thenReturn(facesContext);
            when(facesContext.getExternalContext()).thenReturn(externalContext);
            when(externalContext.getSessionMap()).thenReturn(sessionMap);
            when(request.getSession()).thenReturn(mock(HttpSession.class));
            when(request.getRemoteAddr()).thenReturn("198.168.25.35");

            // Test logic
            when(patientServices.getAllPatients()).thenReturn(new ArrayList<>());

            patientController.deleteParent(testPatient);

            verify(patientServices).deletePatient(testPatient.getId());
            verify(patientServices).getAllPatients();
        }
    }

}