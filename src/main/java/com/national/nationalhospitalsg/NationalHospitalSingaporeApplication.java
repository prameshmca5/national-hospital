package com.national.nationalhospitalsg;

import com.national.nationalhospitalsg.model.entity.Patient;
import com.national.nationalhospitalsg.repos.PatientsRepos;
import com.national.nationalhospitalsg.service.PatientServices;
import jakarta.faces.webapp.FacesServlet;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class NationalHospitalSingaporeApplication {

    @Autowired
    PatientServices patientServices;

    public static void main(String[] args) {
        SpringApplication.run(NationalHospitalSingaporeApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(PatientsRepos repos) {
        return args -> {
          // create Dummy Data to Patient tables
            Patient patient = new Patient();
            patient.setId(null);
            patient.setName("Ramesh Pongiannan");
            patient.setAge(35);
            patient.setMedicalHistory("HeadAche");
           patient.setCreatedAt(LocalDateTime.now());
            patient.setUpdatedAt(LocalDateTime.now());
            patientServices.createOrUpdatePatient(patient);
            Patient patient2 = new Patient();
            patient2.setId(null);
            patient2.setName("Rohit");
            patient2.setAge(28);
            patient2.setMedicalHistory("Fever");
            patient2.setCreatedAt(LocalDateTime.now());
            patient2.setUpdatedAt(LocalDateTime.now());
            patientServices.createOrUpdatePatient(patient2);
            System.out.println("Patient created");
        };
    }

    @Bean
    public ServletRegistrationBean<Servlet> facesServlet() {
        ServletRegistrationBean<Servlet> servletRegistrationBean =
                new ServletRegistrationBean<>(new FacesServlet(), "*.xhtml");
        servletRegistrationBean.setLoadOnStartup(1);
        return servletRegistrationBean;
    }
}
