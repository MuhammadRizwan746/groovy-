package com.example.supabaseauth.dto;

public class ProfileResponse {
    private String id;
    private String email;
    private String role;
    private boolean email_verified;
    private String created_at;

    public ProfileResponse() {}

    public ProfileResponse(String id, String email, String role, boolean email_verified, String created_at) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.email_verified = email_verified;
        this.created_at = created_at;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isEmail_verified() { return email_verified; }
    public void setEmail_verified(boolean email_verified) { this.email_verified = email_verified; }
    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
}
