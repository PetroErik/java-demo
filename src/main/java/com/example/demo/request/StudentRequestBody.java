package com.example.demo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequestBody {
  @NotNull(message = "name must be specified")
  @NotEmpty(message = "name must not be empty")
  private String name;
  @NotNull(message = "email must be specified")
  @NotEmpty(message = "email must not be empty")
  private String email;
}
