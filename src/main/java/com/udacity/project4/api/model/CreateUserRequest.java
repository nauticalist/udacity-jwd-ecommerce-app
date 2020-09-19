package com.udacity.project4.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.udacity.project4.infrastructure.validation.IFieldMatchValidator;
import com.udacity.project4.infrastructure.validation.IUniqueUsername;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@IFieldMatchValidator(baseField = "password", matchField = "confirmPassword", message = "Passwords does not match")
public class CreateUserRequest {

	@NotEmpty(message = "Username cannot be null or empty")
	@NotNull
	@IUniqueUsername()
	@JsonProperty
	private String username;

	@NotEmpty(message = "Password cannot be null or empty")
	@NotNull
	@Size(min=8, max=128, message = "Passwords must contain at least eight characters.")
	@JsonProperty
	private String password;

	@NotEmpty(message = "Confirm password cannot be null or empty")
	@NotNull
	@Size(min=8, max=128, message = "Passwords must contain at least eight characters.")
	@JsonProperty
	private String confirmPassword;

	public CreateUserRequest() {
	}

	public CreateUserRequest(@NotEmpty(message = "Username cannot be null or empty") @NotNull String username, @NotEmpty(message = "Password cannot be null or empty") @NotNull @Size(min = 8, max = 128, message = "Passwords must contain at least eight characters.") String password, @NotEmpty(message = "Confirm password cannot be null or empty") @NotNull @Size(min = 8, max = 128, message = "Passwords must contain at least eight characters.") String confirmPassword) {
		this.username = username;
		this.password = password;
		this.confirmPassword = confirmPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof CreateUserRequest)) return false;

		CreateUserRequest that = (CreateUserRequest) o;

		return new EqualsBuilder()
				.append(getUsername(), that.getUsername())
				.append(getPassword(), that.getPassword())
				.append(getConfirmPassword(), that.getConfirmPassword())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(getUsername())
				.append(getPassword())
				.append(getConfirmPassword())
				.toHashCode();
	}

	@Override
	public String toString() {
		return "CreateUserRequest{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", confirmPassword='" + confirmPassword + '\'' +
				'}';
	}
}
