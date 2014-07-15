package com.technion.project.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.technion.project.Constants;

@Entity
@Table(name = "evacuation", catalog = Constants.SCHEMA)
public class EvacuationEvent
{

	private long id;
	private Date estimated;
	private String means;
	private int capacity;
	private float geolat;
	private float geolng;
	private Set<User> registeredUsers;

	public float getGeolat()
	{
		return geolat;
	}

	public void setGeolat(final float geolat)
	{
		this.geolat = geolat;
	}

	public float getGeolng()
	{
		return geolng;
	}

	public void setGeolng(final float geolng)
	{
		this.geolng = geolng;
	}

	public EvacuationEvent(final long id, final Date estimated,
			final String means, final int capacity,
			final Set<User> registeredUsers)
	{
		super();
		this.id = id;
		this.estimated = estimated;
		this.means = means;
		this.capacity = capacity;
		this.registeredUsers = registeredUsers;
	}

	@Id
	@GeneratedValue
	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public Date getEstimated()
	{
		return estimated;
	}

	public void setEstimated(final Date estimated)
	{
		this.estimated = estimated;
	}

	public String getMeans()
	{
		return means;
	}

	public void setMeans(final String means)
	{
		this.means = means;
	}

	public int getCapacity()
	{
		return capacity;
	}

	public void setCapacity(final int capacity)
	{
		this.capacity = capacity;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "evacuation_user", joinColumns =
	{ @JoinColumn(name = "id") }, inverseJoinColumns =
	{ @JoinColumn(name = "username") })
	public Set<User> getRegisteredUsers()
	{
		return registeredUsers;
	}

	public void setRegisteredUsers(final Set<User> registeredUsers)
	{
		this.registeredUsers = registeredUsers;
	}

	public EvacuationEvent()
	{
		super();
	}

	public void addUser(final User user)
	{
		registeredUsers.add(user);
	}

	public void removeUser(final User user)
	{
		registeredUsers.remove(user);
	}

	@Transient
	public int getAmountLeft()
	{
		return capacity - registeredUsers.size();
	}
}
