package com.jobportal.dto;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.jobportal.entity.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private Long id;
    private String name;
    private String email;
    private String jobTitle;
    private String company;
    private String location;
    private String about;
    private String picture;
    private Long totalExp;
    private String gender;
    private List<String> skills;
    private List<String> education;
    private List<Long> savedJobs = new ArrayList<>();
    private List<Experience> experiences = new ArrayList<>();
    private List<Certification> certifications = new ArrayList<>();

    // ✅ Add match percentages (skill, education, overall)
    private List<Double> matchPercentages;

    public Profile toEntity() {
        return new Profile(
            this.id, this.name, this.email, this.jobTitle, this.company, this.location, this.about,
            this.picture != null ? Base64.getDecoder().decode(this.picture) : null,
            this.totalExp,this.gender,
            this.skills != null ? new ArrayList<>(this.skills) : new ArrayList<>(),
            this.education != null ? new ArrayList<>(this.education) : new ArrayList<>(),
            this.savedJobs != null ? new ArrayList<>(this.savedJobs) : new ArrayList<>(),
            this.experiences != null ? new ArrayList<>(this.experiences) : new ArrayList<>(),
            this.certifications != null ? new ArrayList<>(this.certifications) : new ArrayList<>()
        );
    }
}
