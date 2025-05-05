package com.national.nationalhospitalsg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.national.nationalhospitalsg.model.entity.Patient;
import com.national.nationalhospitalsg.repos.PatientsRepos;
import com.national.nationalhospitalsg.service.impl.PatientServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PatientServicesImplTest {

    @Mock
    private PatientsRepos patientsRepos;

    @InjectMocks
    private PatientServicesImpl patientServices;

    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        patient1 = new Patient();
        patient1.setId(1);
        patient1.setName("Ramesh Pongiannan");
        patient1.setAge(35);
        patient2 = new Patient();
        patient2.setId(2);
        patient2.setName("Rohit");
        patient2.setAge(25);
    }

    @Test
    void getAllPatients_ShouldReturnAllPatients() {
        List<Patient> expectedPatients = Arrays.asList(patient1, patient2);
        when(patientsRepos.findAll()).thenReturn(expectedPatients);
        // Act
        List<Patient> actualPatients = patientServices.getAllPatients();
        // Assert
        verify(patientsRepos, times(1)).findAll();
    }

    @Test
    void createOrUpdatePatient_ShouldSavePatient() {
        // Arrange
        when(patientsRepos.save(patient1)).thenReturn(patient1);

        // Act
        Patient result = patientServices.createOrUpdatePatient(patient1);

        // Assert
        assertEquals(patient1, result);
        verify(patientsRepos, times(1)).save(patient1);
    }

    @Test
    void deletePatient_ShouldDeletePatient() {
        // Arrange - no return needed for void methods
        int patientId = 1;

        // Act
        patientServices.deletePatient(patientId);

        // Assert
        verify(patientsRepos, times(1)).deleteById(patientId);
    }

}