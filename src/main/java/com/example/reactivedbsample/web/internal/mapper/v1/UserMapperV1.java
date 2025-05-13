package com.example.reactivedbsample.web.internal.mapper.v1;

import com.example.reactivedbsample.bl.domain.api.model.ExampleUser;
import com.example.reactivedbsample.web.api.dto.v1.ExampleUserDtoV1;
import org.mapstruct.Mapper;

@Mapper
public abstract class UserMapperV1 {
    public abstract ExampleUser mapFromDto(ExampleUserDtoV1 user);
    public abstract ExampleUserDtoV1 mapToDto(ExampleUser user);
}
