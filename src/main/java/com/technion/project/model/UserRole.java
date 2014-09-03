package com.technion.project.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.technion.project.Constants;

@Entity
@Table(name = "user_roles", catalog = Constants.SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames =
{ "role", "username" }))
public class UserRole implements BaseModel
{

	private int userRoleId;

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UserRole other = (UserRole) obj;
		if (role == null)
		{
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (user == null)
		{
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	private User user;
	private String role;

	public UserRole()
	{
	}

	public UserRole(final User user, final String role)
	{
		this.user = user;
		this.role = role;
	}

	@Override
	public String toString()
	{
		return "UserRole [user=" + user + ", role=" + role + "]";
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_role_id", unique = true, nullable = false)
	public int getUserRoleId()
	{
		return this.userRoleId;
	}

	public void setUserRoleId(final int userRoleId)
	{
		this.userRoleId = userRoleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", nullable = false)
	public User getUser()
	{
		return this.user;
	}

	public void setUser(final User user)
	{
		this.user = user;
	}

	@Column(name = "role", nullable = false, length = 45)
	public String getRole()
	{
		return this.role;
	}

	public void setRole(final String role)
	{
		this.role = role;
	}

}