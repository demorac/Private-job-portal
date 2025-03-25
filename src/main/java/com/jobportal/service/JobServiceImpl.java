package com.jobportal.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobportal.dto.ApplicantDTO;
import com.jobportal.dto.Application;
import com.jobportal.dto.ApplicationStatus;
import com.jobportal.dto.JobDTO;
import com.jobportal.dto.ProfileDTO;
import com.jobportal.entity.Applicant;
import com.jobportal.entity.Job;
import com.jobportal.entity.Profile;
import com.jobportal.exception.JobPortalException;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.ProfileRepository;
import com.jobportal.utility.Utilities;

@Service("jobService")
public class JobServiceImpl implements JobService{

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private Utilities utilities; // Injected as a bean


    @Override
    public JobDTO postJob(JobDTO jobDTO) throws JobPortalException {
        if (jobDTO.getId() == null || jobDTO.getId() == 0) { // ✅ Ensure ID is generated
            Long newId = utilities.getNextSequence("jobs");  // ✅ Get next sequence
            jobDTO.setId(newId);
        }
    
        jobDTO.setPostTime(LocalDateTime.now());
    
        Job savedJob = jobRepository.save(jobDTO.toEntity());
        System.out.println("💾 Job saved with ID: " + savedJob.getId()); // Debugging log
        return savedJob.toDTO();
    }
    
    


    @Override
    public List<JobDTO> getAllJobs() {
       return jobRepository.findAll().stream().map((x)->x.toDTO()).toList();
    }


    @Override
    public JobDTO getJob(Long id) throws JobPortalException {
        return jobRepository.findById(id).orElseThrow(()-> new JobPortalException("JOB_NOT_FOUND")).toDTO();
    }


    @Override
    public void applyJob(Long id, ApplicantDTO applicantDTO) throws JobPortalException {
        Job job=jobRepository.findById(id).orElseThrow(()-> new JobPortalException("JOB_NOT_FOUND"));
        List<Applicant>applicants=job.getApplicants();
        if(applicants==null)applicants=new ArrayList<>();
        if(applicants.stream().filter((x)->x.getApplicantId()==applicantDTO.getApplicantId()).toList().size()>0)throw new JobPortalException("JOB_APPLIED_ALREADY");
        applicantDTO.setApplicationStatus(ApplicationStatus.APPLIED);
        applicants.add(applicantDTO.toEntity());
        job.setApplicants(applicants);
        jobRepository.save(job);
    }


    

    @Override
    public List<JobDTO> getJobsPostedBy(Long id) {
         return jobRepository.findByPostedBy(id).stream().map((x)->x.toDTO()).toList();
    }


    @Override
    public void changeAppStatus(Application application) throws JobPortalException {
        System.out.println("📩 Received Application Update: " + application);
    
        Job job = jobRepository.findById(application.getId())
                .orElseThrow(() -> new JobPortalException("JOB_NOT_FOUND"));
    
        List<Applicant> updatedApplicants = job.getApplicants().stream()
            .map(applicant -> {
                if (application.getApplicantId().equals(applicant.getApplicantId())) {
                    
    
                    applicant.setApplicationStatus(application.getApplicationStatus());
    
                    if (application.getApplicationStatus().equals(ApplicationStatus.INTERVIEWING)) {
                        if (application.getInterviewTime() != null) {
                            applicant.setInterviewTime(application.getInterviewTime());
                            
                        }
                        if (application.getInterviewLocation() != null) {
                            applicant.setInterviewLocation(application.getInterviewLocation());
                           
                        }
                    }
                }
                return applicant;
            })
            .collect(Collectors.toList());
    
        job.setApplicants(updatedApplicants);
        jobRepository.save(job); // ✅ Save updated job data
    
        System.out.println("💾 Job Updated Successfully!");
    }
    





    @Override
    public List<ProfileDTO> getMatchingProfiles(Long jobId) throws JobPortalException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobPortalException("JOB_NOT_FOUND: " + jobId));

        List<String> requiredSkills = job.getSkillsRequired() != null ? job.getSkillsRequired() : new ArrayList<>();
        List<String> requiredEducation = job.getEducationRequired() != null ? job.getEducationRequired() : new ArrayList<>();

        List<Profile> allProfiles = profileRepository.findAll();

        return allProfiles.stream()
                .map(profile -> {
                    List<String> profileSkills = profile.getSkills() != null ? profile.getSkills() : new ArrayList<>();
                    List<String> profileEducation = profile.getEducation() != null ? profile.getEducation() : new ArrayList<>();

                    int skillMatchCount = (int) profileSkills.stream().filter(requiredSkills::contains).count();
                    int educationMatchCount = (int) profileEducation.stream().filter(requiredEducation::contains).count();

                    double skillMatchPercentage = requiredSkills.isEmpty() 
                            ? 100.0 
                            : ((double) skillMatchCount / requiredSkills.size()) * 100;

                    double educationMatchPercentage = requiredEducation.isEmpty() 
                            ? 100.0 
                            : ((double) educationMatchCount / requiredEducation.size()) * 100;

                    double overallMatchPercentage = (skillMatchPercentage * 0.7) + (educationMatchPercentage * 0.3);

                    ProfileDTO profileDTO = profile.toDTO();
                    profileDTO.setMatchPercentages(
                        List.of(skillMatchPercentage, educationMatchPercentage, overallMatchPercentage)
                    );

                    return profileDTO;
                })
                .collect(Collectors.toList());
    }

}
