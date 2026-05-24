package com.service;

import com.config.FileStorageProperties;
import com.dto.*;
import com.entity.*;
import com.enums.ApplicationStatus;
import com.enums.DocumentType;
import com.exception.ResourceNotFoundException;
import com.mapper.ApplicationMapper;
import com.repository.*;
import com.service.interfaces.ApplyService;
import com.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service @RequiredArgsConstructor @Transactional
public class ApplyServiceImpl implements ApplyService {

    private final ModelMapper  modelMapper;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final PersonalInfoRepository personalRepository;
    private final EducationalRepository educationalRepository;
    private final FinancialRepository financialRepository;
    private final BankRepository bankRepository;
    private final DocumentRepository documentRepository;
    private final ApplicationMapper applicationMapper;
    private final FileStorageService fileStorageService;
    private final FileStorageProperties fileStorageProperties;

    private UserEntity getCurrentUser(){
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    private ApplicationEntity getCurrentApplicationForCurrentUser(){
        UserEntity user = getCurrentUser();
        return applicationRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Username:"+ user.getFullname() +" not found"));
    }

    @Override
    public ApplicationResponseDTO start() {
        UserEntity currentUser = getCurrentUser();
        ApplicationEntity applicationEntity =  applicationRepository.findByUser(currentUser)
                .orElseGet(()-> {
                    ApplicationEntity application = new ApplicationEntity();
                    application.setUser(currentUser);
                    application.setStatus(ApplicationStatus.DRAFT);
                    return applicationRepository.save(application);
                });
        return applicationMapper.toDto(applicationEntity);
    }

    @Override
    public ApplicationResponseDTO personal(PersonalRequestDto personalRequestDto) {
        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        PersonalInfoEntity entity = personalRepository.findByApplication(app).orElseGet(PersonalInfoEntity::new);
        modelMapper.map(personalRequestDto,entity);
        entity.setApplication(app);
        personalRepository.save(entity);
        return applicationMapper.toDto(app);
    }

    @Override
    public ApplicationResponseDTO education(EducationRequestDto requestDto) {
        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        EducationInfoEntity entity = educationalRepository.findByApplication(app).orElseGet(EducationInfoEntity::new);
        modelMapper.map(requestDto,entity);
        entity.setApplication(app);
        educationalRepository.save(entity);
        return applicationMapper.toDto(app);
    }

    @Override
    public ApplicationResponseDTO financial(FinancialRequestDto requestDto) {
        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        FinancialInfoEntity entity = financialRepository.findByApplication(app).orElseGet(FinancialInfoEntity::new);
        modelMapper.map(requestDto,entity);
        entity.setApplication(app);
        financialRepository.save(entity);
        return applicationMapper.toDto(app);
    }

    @Override
    public ApplicationResponseDTO bank(BankRequestDto requestDto) {
        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        BankInfoEntity entity = bankRepository.findByApplication(app).orElseGet(BankInfoEntity::new);
        modelMapper.map(requestDto,entity);
        entity.setApplication(app);
        bankRepository.save(entity);
        return applicationMapper.toDto(app);
    }

    @Override
    public ApplicationResponseDTO document(DocumentRequestDto dto) throws IOException {
        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        uploadDocument(app, dto.getPassportPhoto(), DocumentType.PASSPORT_PHOTO);
        uploadDocument(app, dto.getIdentityCard(), DocumentType.IDENTITY_CARD);
        uploadDocument(app, dto.getTenthMarksheet(), DocumentType.TENTH_MARKSHEET);
        uploadDocument(app, dto.getTwelfthMarksheet(), DocumentType.TWELFTH_MARKSHEET);
        uploadDocument(app, dto.getPreviousSemesterMarksheet(), DocumentType.PREVIOUS_SEMESTER_MARKSHEET);
        return applicationMapper.toDto(app);
    }

    @Override
    public ApplicationResponseDTO submit() {
        ApplicationEntity app = getCurrentApplicationForCurrentUser();
        app.setStatus(ApplicationStatus.SUBMITTED);
        applicationRepository.save(app);
        return applicationMapper.toDto(app);
    }

    @Override
    public ApplicationResponseDTO getMyApplication() {
        return applicationMapper.toDto(getCurrentApplicationForCurrentUser());
    }



    private void uploadDocument(ApplicationEntity app, MultipartFile file, DocumentType type)
            throws IOException {

        if (file == null || file.isEmpty()) {
            return;
        }
        DocumentEntity entity = documentRepository.findByApplicationAndDocumentType(app, type).orElse(new DocumentEntity());
        entity.setApplication(app);
        entity.setDocumentType(type);
        String uploadDir = fileStorageProperties.getUploadDir() + app.getUser().getId();
        Files.createDirectories(
                Paths.get(uploadDir));
        String path = fileStorageService.save(file, uploadDir, type.name());
        entity.setFileUrl(path);
        documentRepository.save(entity);
    }


}
