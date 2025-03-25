package com.jobportal.entity;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jobportal.dto.Certification;
import com.jobportal.dto.Experience; // ✅ Fix: Changed from Experirnce to Experience
import com.jobportal.dto.ProfileDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "profiles")
public class Profile {
    @Id
    private Long id;
    private String name;
    private String email;
    private String jobTitle;
    private String company;
    private String location;
    private String about;
    private byte[] picture;
    private Long totalExp;
    private String gender;
    private List<String> skills;
    private List<String> education;
    private List<Long> savedJobs = new ArrayList<>();
    


    // ✅ Fix: Renamed from "experirnces" to "experiences"
    private List<Experience> experiences = new ArrayList<>();

    // ✅ Fix: Renamed from "certifications" to "certifications" (for JSON consistency)
    private List<Certification> certifications = new ArrayList<>();

    public ProfileDTO toDTO() {
        return new ProfileDTO(
            this.id, this.name, this.email, this.jobTitle, this.company, this.location, this.about,
            this.picture != null ? Base64.getEncoder().encodeToString(this.picture) : null,
            this.totalExp,this.gender,
            this.skills != null ? new ArrayList<>(this.skills) : new ArrayList<>(),
            this.education != null ? new ArrayList<>(this.education) : new ArrayList<>(),
            this.savedJobs != null ? new ArrayList<>(this.savedJobs) : new ArrayList<>(),
            this.experiences != null ? new ArrayList<>(this.experiences) : new ArrayList<>(),
            this.certifications != null ? new ArrayList<>(this.certifications) : new ArrayList<>(),
            new ArrayList<>() // ✅ Add an empty list for match percentages
        );
    }
    

}
