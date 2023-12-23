package com.example.ressy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Objects;


public class HomeRequestMessage implements Serializable {
    private String profession;

    public HomeRequestMessage() {
    }

    public HomeRequestMessage(String profession) {
        this.profession = profession;
    }

    public String getProfession() {
        return profession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeRequestMessage that = (HomeRequestMessage) o;
        return Objects.equals(profession, that.profession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profession);
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
