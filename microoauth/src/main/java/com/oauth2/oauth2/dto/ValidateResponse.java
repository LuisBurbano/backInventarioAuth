package com.oauth2.oauth2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidateResponse {

    @JsonProperty("role")
    private Role role;

    // Getter y Setter

    public static class Role {
        @JsonProperty("id")
        private Long id;
        @JsonProperty("name")
        private String name;

        // Getters y Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}