package com.national.nationalhospitalsg;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import com.national.nationalhospitalsg.model.entity.Patient;
import com.national.nationalhospitalsg.service.PatientServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockHttpServletRequest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NationalHospitalSingaporeApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PatientServices patientServices;

    @Mock
    private FacesContext facesContext;

    @Mock
    private HttpServletRequest request;

    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    public void setUp() {
        // Setup test data
        patient1 = new Patient();
        patient1.setId(1);
        patient1.setName("Ramesh");
        patient1.setAge(35);
        patient1.setMedicalHistory("Hypertension");
        patient1.setCreatedAt(LocalDateTime.now());
        patient1.setUpdatedAt(LocalDateTime.now());

        patient2 = new Patient();
        patient2.setId(2);
        patient2.setName("Rohit");
        patient2.setAge(28);
        patient2.setMedicalHistory("Diabetes");
        patient2.setCreatedAt(LocalDateTime.now());
        patient2.setUpdatedAt(LocalDateTime.now());

        List<Patient> allPatients = Arrays.asList(patient1, patient2);


        when(patientServices.getAllPatients()).thenReturn(allPatients);
        when(patientServices.createOrUpdatePatient(any(Patient.class))).thenAnswer(invocation -> {
            Patient p = invocation.getArgument(0);
            if (p.getId() == null) {
                p.setId(3);
            }
            return p;
        });
        when(patientServices.findPatientById(1L)).thenReturn(patient1);

        // Mock FacesContext and HttpServletRequest
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setSession(new MockHttpSession());
        when(request.getSession()).thenReturn(mockRequest.getSession());
      //  when(request.getSession().getId()).thenReturn("test-session-id");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
    }

    @Test
    @Disabled
    public void testGetAllPatients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patients.xhtml"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("parentsList", hasSize(2)))
                .andExpect(model().attribute("parentsList", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("name", is("John Doe"))
                        )
                )));

    }

    @Test
    @Disabled
    public void testSavePatient_NewPatient() throws Exception {
        Patient newPatient = new Patient();
        newPatient.setName("New Patient");
        newPatient.setAge(40);
        newPatient.setMedicalHistory("Asthma");

        mockMvc.perform(MockMvcRequestBuilders.post("/patients/save")
                        .flashAttr("selectedPatient", newPatient))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients.xhtml"));

        verify(patientServices, times(1)).createOrUpdatePatient(any(Patient.class));

        assertThat(LoggerFactory.getLogger("AUDIT_LOGGER").isInfoEnabled());
    }

    @Test
    @Disabled
    public void testSavePatient_UpdateExisting() throws Exception {
        patient1.setName("Updated Name");

        mockMvc.perform(MockMvcRequestBuilders.post("/patients/save")
                        .flashAttr("selectedPatient", patient1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients.xhtml"));

        verify(patientServices, times(1)).createOrUpdatePatient(any(Patient.class));
    }



}
