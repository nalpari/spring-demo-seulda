package net.devgrr.springdemo.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserRequest {
	private final String firstName;
	private final String lastName;
	private final String email;
	private final Integer age;
}
