package com.scm.scm20.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    private String userId;

    @NotBlank(message = "Name is required")
    @Size(min=4,message="Name must be at least 4 character")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @Getter(value=AccessLevel.NONE)

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

  //  @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{8,12}", message = "Phone number must be 8-12 digits")
    private String phoneNumber;

    private String about; 
    @Getter(value=AccessLevel.NONE)

    @Column(name = "profile_pic", length = 255)
    private String profilePic;

    private boolean enabled = true;
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    @Enumerated(EnumType.STRING)
    private Provider provider = Provider.SELF;

    private String providerUserId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contact> contacts = new ArrayList<>();

    @ElementCollection(fetch=FetchType.EAGER)
    private List<String> roleList=new ArrayList<>();
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Collection<SimpleGrantedAuthority>roles= roleList.stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
		return Collections.emptyList() ;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true ;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true ;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		return this.enabled;	
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	} 
}
