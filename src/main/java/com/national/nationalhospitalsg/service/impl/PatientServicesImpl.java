package com.national.nationalhospitalsg.service.impl;

import com.national.nationalhospitalsg.model.entity.Patient;
import com.national.nationalhospitalsg.repos.PatientsRepos;
import com.national.nationalhospitalsg.service.PatientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServicesImpl implements PatientServices {

    @Autowired
    PatientsRepos patientsRepos;

    @Override
    public List<Patient> getAllPatients() {
        return patientsRepos.findAll();
    }

    @Override
    public Patient createOrUpdatePatient(Patient patient) {
        return patientsRepos.save(patient);
    }


    @Override
    public void deletePatient(int patientId) {
        patientsRepos.deleteById(patientId);
    }

    @Override
    public Patient findPatientById(long id) {
        return null;
    }
}
