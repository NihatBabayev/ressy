package com.example.ressy.dto;

import com.example.ressy.entity.Doctor;

import java.io.Serializable;
import java.util.Objects;

public class DoctorDTO implements Serializable {
    String firstname;
    String lastname;
    String phoneNumber;
    String profession;
    String photoBase64;
    String email;
    public static DoctorDTO fromEntity(Doctor doctor) {
        return new DoctorDTO(
                doctor.getUser().getFirstname(),
                doctor.getUser().getLastname(),
                doctor.getUser().getPhoneNumber(),
                doctor.getProfessionName(),
                doctor.getUser().getPhotoBase64(),
                doctor.getUser().getEmail()
        );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorDTO doctorDTO = (DoctorDTO) o;
        return Objects.equals(firstname, doctorDTO.firstname) && Objects.equals(lastname, doctorDTO.lastname) && Objects.equals(phoneNumber, doctorDTO.phoneNumber) && Objects.equals(profession, doctorDTO.profession) && Objects.equals(email, doctorDTO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, phoneNumber, profession, email);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public DoctorDTO() {
    }

    public DoctorDTO(String firstname, String lastname, String phoneNumber, String profession, String photoBase64, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.profession = profession;
        this.photoBase64 = photoBase64;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}