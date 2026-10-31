package com.nulvora.nulvora.user.mapper;

import com.nulvora.nulvora.user.dto.request.UserRequest;
import com.nulvora.nulvora.user.dto.response.UserResponse;
import com.nulvora.nulvora.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest request);

    @Mapping(source = "profile.profileUrlImg", target = "profileImageUrl")
    UserResponse toUserResponse(User user);
}
