package com.example.ressy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class UserResponseMessage implements Serializable {
    private List<DoctorDTO> doctorDTOList;
    private String profession;

    public UserResponseMessage() {
    }

    public UserResponseMessage(List<DoctorDTO> doctorDTOList, String profession) {
        this.doctorDTOList = doctorDTOList;
        this.profession = profession;
    }

    public List<DoctorDTO> getDoctorDTOList() {
        return doctorDTOList;
    }

    public void setDoctorDTOList(List<DoctorDTO> doctorDTOList) {
        this.doctorDTOList = doctorDTOList;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponseMessage that = (UserResponseMessage) o;
        return Objects.equals(doctorDTOList, that.doctorDTOList) && Objects.equals(profession, that.profession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorDTOList, profession);
    }
}