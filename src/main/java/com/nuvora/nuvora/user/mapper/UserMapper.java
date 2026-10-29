package com.nuvora.nuvora.user.mapper;

import com.nuvora.nuvora.user.dto.request.UserRequest;
import com.nuvora.nuvora.user.dto.response.UserResponse;
import com.nuvora.nuvora.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest request);

    @Mapping(source = "profile.profileUrlImg", target = "profileImageUrl")
    UserResponse toUserResponse(User user);
}
