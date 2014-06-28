package com.technion.ex5.Data;

import java.util.Date;

import com.technion.ex5.Data.EvacuationEventImpl.TransportMean;

public interface EvacuationEvent
{

	/**
	 * @return the geometry
	 */
	public abstract Shape getGeometry();

	/**
	 * @param date
	 *            - time to check
	 * @return true if contain in time window
	 */
	public boolean isInTime(Date date);

	/**
	 * @return the transportType
	 */
	public abstract TransportMean getTransportType();

	/**
	 * @return the capacity
	 */
	public abstract int getCapacity();

	/**
	 * @return the registrationCount
	 */
	public abstract int getRegistrationCount();

	public boolean add(User user);
}