package com.technion.project.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.technion.project.Constants;

@Entity
@Table(name = "users", catalog = Constants.SCHEMA)
public class User
{

	private String username;
	private String password;
	private boolean enabled;
	private String fname;
	private String lname;

	private Set<UserRole> userRole = new HashSet<UserRole>(0);

	public User()
	{
	}

	public User(final String username, final String password,
			final boolean enabled, final String fname, final String lname,
			final Set<UserRole> userRole)
	{
		super();
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.fname = fname;
		this.lname = lname;
		this.userRole = userRole;
	}

	@Column(name = "fname", nullable = false, length = 60)
	public String getFname()
	{
		return fname;
	}

	public void setFname(final String fname)
	{
		this.fname = fname;
	}

	@Column(name = "lname", nullable = false, length = 60)
	public String getLname()
	{
		return lname;
	}

	public void setLname(final String lname)
	{
		this.lname = lname;
	}

	@Id
	@Column(name = "username", unique = true, nullable = false, length = 45)
	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(final String username)
	{
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 60)
	public String getPassword()
	{
		return this.password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	@Column(name = "enabled", nullable = false)
	public boolean isEnabled()
	{
		return this.enabled;
	}

	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<UserRole> getUserRole()
	{
		return this.userRole;
	}

	public void setUserRole(final Set<UserRole> userRole)
	{
		this.userRole = userRole;
	}

}
