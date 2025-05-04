package com.national.nationalhospitalsg.controller;


import com.national.nationalhospitalsg.model.Patient;
import com.national.nationalhospitalsg.service.PatientServices;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Named("patientBean")
@Component(value = "patientBean")
@ViewScoped
@Data
public class PatientController implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT_LOGGER");

    @Autowired
    private PatientServices patientServices;

    @Autowired
    private HttpServletRequest request;

    private List<Patient> parentsList;
    @Getter
    private Patient selectedPatient = new Patient(); // For create/edit

    @PostConstruct
    public void init() {
        loadParents();
    }

    public void setupAuditContext() {
        // Get current user from session
        String userId = (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("username");

        MDC.put("userId", userId != null ? userId : "ANONYMOUS");
        MDC.put("sessionId", request.getSession().getId());
        MDC.put("clientIP", request.getRemoteAddr());
    }

    public void loadParents() {
        parentsList = new ArrayList<>();
        parentsList = patientServices.getAllPatients();
    }

    public List<Patient> getPatients() {
        return parentsList;
    }

    public String logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext()
                .redirect("/logout");
        FacesContext.getCurrentInstance().responseComplete();
        return null;
    }


    public void savePatient() {
        setupAuditContext();
        try {
            logger.info("Attempting to save patient: {}", selectedPatient);

            if (selectedPatient.getId() == null) {
                auditLogger.info("CREATE_PATIENT | Attempt | {}", selectedPatient);
                logger.debug("Creating new patient");
                patientServices.createOrUpdatePatient(selectedPatient);
                auditLogger.info("CREATE_PATIENT | Success | ID: {}", selectedPatient.getId());
            } else {
                logger.debug("Updating existing patient ID: {}", selectedPatient.getId());
                auditLogger.info("UPDATE_PATIENT | Attempt | ID: {}", selectedPatient.getId());
                patientServices.createOrUpdatePatient(selectedPatient);
                auditLogger.info("UPDATE_PATIENT | Success | Changes: {}", selectedPatient);
            }

            loadParents();
            selectedPatient = new Patient();
            logger.info("Patient saved successfully");

        } catch (Exception e) {
            auditLogger.error("OPERATION_FAILED | Patient | Error: {}", e.getMessage());
            logger.error("Error saving patient: {}", e.getMessage(), e);
            // Handle error appropriately
        }
    }

    public void editPatient(Patient patient) {
        setupAuditContext();
        this.selectedPatient = patient;
    }

    public void resetForm() {
        selectedPatient = new Patient();
    }

    public void prepareNew() {
        selectedPatient = new Patient(); // for "Add New"
    }

    public void deleteParent(Patient parent) {
        setupAuditContext();
        auditLogger.info("DELETE_PATIENT | Attempt | ID: {}", parent.getId());
        logger.info("Deleting patient with ID: {}", parent.getId());
        try {
            patientServices.deletePatient(parent.getId());
            loadParents();
            logger.info("Patient deleted successfully");
        } catch (Exception e) {
            auditLogger.error("DELETE_FAILED | Patient ID: {} | {}", parent.getId(), e.getMessage());
            logger.error("Error deleting patient ID {}: {}", parent.getId(), e.getMessage(), e);
        }
    }
}