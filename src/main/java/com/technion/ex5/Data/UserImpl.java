package com.technion.ex5.Data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserImpl implements User, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 4358931378125606424L;
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String password;
	private String username;
	private String address;
	private UserType type;
	private boolean registered;

	public UserImpl(int id)
	{
	}

	/**
	 * @return the registered
	 */
	public synchronized final boolean isRegistered()
	{
		return registered;
	}

	/**
	 * @param registered
	 *            the registered to set
	 */
	public synchronized final void setRegistered(boolean registered)
	{
		this.registered = registered;
	}

	UserImpl(String username)
	{
		this.username = username;
	}

	public enum UserType
	{
		Regular, Admin;
	}

	/**
	 * @return the id
	 */
	public synchronized final int getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public synchronized final void setId(int id)
	{
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public synchronized final String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public synchronized final void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public synchronized final String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public synchronized final void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the username
	 */
	public synchronized final String getUsername()
	{
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public synchronized final void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * @return the address
	 */
	public synchronized final String getAddress()
	{
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public synchronized final void setAddress(String address)
	{
		this.address = address;
	}

	/**
	 * @return the type
	 */
	public synchronized final UserType getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public synchronized final void setType(UserType type)
	{
		this.type = type;
	}

}
