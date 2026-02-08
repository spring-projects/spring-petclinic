package org.springframework.samples.petclinic.feature;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "feature_flags", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class FeatureFlag {

	public enum Strategy {

		BOOLEAN, PERCENTAGE, WHITELIST, BLACKLIST

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotEmpty
	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false)
	private boolean enabled = false;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Strategy strategy = Strategy.BOOLEAN;

	@Column
	private int percentage = 100;

	@Column(length = 2000)
	private String userList;

	@Column(length = 500)
	private String description;

	// Getters & Setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = Math.max(0, Math.min(100, percentage));
	}

	public String getUserList() {
		return userList;
	}

	public void setUserList(String userList) {
		this.userList = userList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
