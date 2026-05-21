package com.service;

import com.dto.*;
import com.entity.*;
import com.enums.ApplicationStatus;
import com.exception.ResourceNotFoundException;
import com.repository.*;
import com.service.interfaces.ApplyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class ApplyServiceImpl implements ApplyService {

    private final ModelMapper  modelMapper;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final PersonalInfoRepository personalRepository;
    private final EducationalRepository educationalRepository;
    private final FinancialRepository financialRepository;
    private final BankRepository bankRepository;
    private final DocumentRepository documentRepository;

    private UserEntity getCurrentUser(){
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    private ApplicationEntity getCurrentApplicationForCurrentUser(){
        UserEntity user = getCurrentUser();
        return applicationRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Username"+ user.getFullname() +" not found"));
    }

    @Override
    public ApplicationResponseDTO start() {
        UserEntity currentUser = getCurrentUser();
        return applicationRepository.findByUser(currentUser).map(e -> modelMapper.map(e,ApplicationResponseDTO.class))
                .orElseGet(()-> {
                    ApplicationEntity application = ApplicationEntity.builder().user(currentUser).status(ApplicationStatus.DRAFT).build();
                    applicationRepository.saveAndFlush(application);
                    return modelMapper.map(application,ApplicationResponseDTO.class);
                });

    }

    @Override
    public ApplicationResponseDTO personal(PersonalRequestDto personalRequestDto) {
        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        PersonalInfoEntity entity = app.getPersonalInfo() != null ? app.getPersonalInfo() : new PersonalInfoEntity();
        modelMapper.map(personalRequestDto,entity);

        entity.setApplication(app);
        app.setPersonalInfo(entity);

        personalRepository.saveAndFlush(entity);
        return modelMapper.map(app, ApplicationResponseDTO.class);
    }

    @Override
    public ApplicationResponseDTO education(EducationRequestDto requestDto) {
        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        EducationInfoEntity entity = app.getEducationInfo() != null ? app.getEducationInfo() : new EducationInfoEntity();
        modelMapper.map(requestDto,entity);

        entity.setApplication(app);
        app.setEducationInfo(entity);

        educationalRepository.saveAndFlush(entity);
        return modelMapper.map(app, ApplicationResponseDTO.class);
    }

    @Override
    public ApplicationResponseDTO financial(FinancialRequestDto requestDto) {
        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        FinancialInfoEntity entity = app.getFinancialInfo() != null ? app.getFinancialInfo() : new FinancialInfoEntity();
        modelMapper.map(requestDto,entity);

        entity.setApplication(app);
        app.setFinancialInfo(entity);

        financialRepository.saveAndFlush(entity);
        return modelMapper.map(app, ApplicationResponseDTO.class);
    }

    @Override
    public ApplicationResponseDTO bank(BankRequestDto requestDto) {
        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        BankInfoEntity entity = app.getBankInfo() != null ? app.getBankInfo() : new BankInfoEntity();
        modelMapper.map(requestDto,entity);

        entity.setApplication(app);
        app.setBankInfo(entity);

        bankRepository.saveAndFlush(entity);
        return modelMapper.map(app, ApplicationResponseDTO.class);
    }

    @Override
    public ApplicationResponseDTO document( DocumentRequestDto requestDto) throws IOException {

        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        DocumentsEntity entity = app.getDocuments() != null
                ? app.getDocuments() : new DocumentsEntity();
        entity.setApplication(app);

        String uploadDir = "uploads/" + app.getUser().getFullname();
        Files.createDirectories(Paths.get(uploadDir));

        entity.setPassportPhoto(saveFile(requestDto.getPassportPhoto(), uploadDir, "passportPhoto"));
        entity.setIdentityCard(saveFile(requestDto.getIdentityCard(), uploadDir, "identityCard"));
        entity.setTenthMarksheet(saveFile(requestDto.getTenthMarksheet(), uploadDir, "tenthMarksheet"));
        entity.setTwelfthMarksheet(saveFile(requestDto.getTwelfthMarksheet(), uploadDir, "twelfthMarksheet"));
        entity.setPreviousSemesterMarksheet(saveFile(requestDto.getPreviousSemesterMarksheet(), uploadDir, "previousSemesterMarksheet"));

        documentRepository.saveAndFlush(entity);
        return modelMapper.map(app, ApplicationResponseDTO.class);
    }

    @Override
    public ApplicationResponseDTO submit() {
        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        app.setStatus(ApplicationStatus.SUBMITTED);
        return modelMapper.map(app, ApplicationResponseDTO.class);
    }

    @Override
    public ApplicationResponseDTO getMyApplication() {
        return modelMapper.map(getCurrentApplicationForCurrentUser(), ApplicationResponseDTO.class);
    }

    private String saveFile(MultipartFile file, String dir, String prefix) throws IOException {
        String ext = file.getOriginalFilename() != null
                ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'))
                : "";
        String filename = prefix + "_" + UUID.randomUUID() + ext;
        Path path = Paths.get(dir, filename);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return path.toString();
    }




}
