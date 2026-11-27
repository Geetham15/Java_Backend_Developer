package com.example.employeemanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Component 												// Registers the class as a Spring bean
@Validated												// Activates validation rules (spring-boot-starter-validation dependency required)
@ConfigurationProperties(prefix="employee.management")  // ConfigurationPropertie - Binds external properties to Java fields
public class EmployeeProperties {					    // prefix - identifies which properties to bind

	@NotBlank(message = "Application name cannot be empty")
	private String appName;
	
	@NotBlank(message = "Version cannot be empty")
	private String version;
	
	 @Min(value = 1, message = "Maximum records must be at least 1")
	private int maxRecords;
	private boolean enableAudit;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getMaxRecords() {
		return maxRecords;
	}
	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}
	public boolean isEnableAudit() {
		return enableAudit;
	}
	public void setEnableAudit(boolean enableAudit) {
		this.enableAudit = enableAudit;
	}
	
}
