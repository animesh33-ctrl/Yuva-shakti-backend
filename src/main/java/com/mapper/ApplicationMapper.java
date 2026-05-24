package com.mapper;

import com.dto.ApplicationResponseDTO;
import com.entity.ApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(target = "applicationId", source = "id")
     ApplicationResponseDTO toDto(ApplicationEntity application);
}
