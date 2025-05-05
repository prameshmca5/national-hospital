package com.national.nationalhospitalsg.model.dto;



import lombok.Data;

import java.time.LocalDateTime;

public record PatientDto(Integer id, String name, Integer age, String medicalHistory, LocalDateTime createdAt, LocalDateTime updatedAt ) {
}
