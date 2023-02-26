package com.example.demo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestBody {
  @NotNull(message = "name must be specified")
  @NotEmpty(message = "name must not be empty")
  private String name;
  @NotNull(message = "credit must be specified")
  private Integer credit;
}
