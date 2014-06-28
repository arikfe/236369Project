package com.technion.ex5.Data;

import com.technion.ex5.Data.UserImpl.UserType;

public interface User
{

	public int getId();

	/**
	 * @return the name
	 */
	public abstract String getName();

	/**
	 * @return the password
	 */
	public abstract String getPassword();

	/**
	 * @return the username
	 */
	public abstract String getUsername();

	/**
	 * @return the address
	 */
	public abstract String getAddress();

	/**
	 * @return the type
	 */
	public abstract UserType getType();

	public void setPassword(String password);

	public void setRegistered(boolean b);

}