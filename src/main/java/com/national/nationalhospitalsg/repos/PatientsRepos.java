package com.national.nationalhospitalsg.repos;

import com.national.nationalhospitalsg.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientsRepos extends JpaRepository<Patient, Integer> {
}
