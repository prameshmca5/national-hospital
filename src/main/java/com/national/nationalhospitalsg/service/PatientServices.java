package com.national.nationalhospitalsg.service;

import com.national.nationalhospitalsg.model.Patient;

import java.util.List;

public interface PatientServices {

    public List<Patient> getAllPatients();

    public Patient createOrUpdatePatient(Patient patient);

    public void deletePatient(int patient);

    public Patient findPatientById(long id);
}
