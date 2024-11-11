package net.devgrr.springdemo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserResponse {
	private final Long id;
	private final String firstName;
	private final String lastName;
	private final String email;
	private final Integer age;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final String createdDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final String updatedDate;
}
