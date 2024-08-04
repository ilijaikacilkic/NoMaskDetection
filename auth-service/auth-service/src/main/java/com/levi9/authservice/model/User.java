package com.levi9.authservice.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class User implements UserDetails {
	
	@Id
	@Column(name = "username", unique = true, nullable = false)
	@NotEmpty(message = "{validation.username.NotEmpty}")
	private String username;
	
	@Column(name = "password", nullable = false)
	@NotEmpty(message = "{validation.password.NotEmpty}")
	private String password;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_username", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private Collection<Authority> authorities;
	
	public User() {}
	
	public User(String username, String password, String name, String lastName, Collection<Authority> authorities) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastName = lastName;
		this.authorities = authorities;
	}	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}


	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}