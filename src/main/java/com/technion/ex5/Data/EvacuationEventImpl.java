package com.technion.ex5.Data;

import java.util.Date;
import java.util.Set;

public class EvacuationEventImpl implements EvacuationEvent
{
	private long id;
	private Shape geometry;
	private Date startTime;
	private Date duration;
	private TransportMean transportType;
	private int capacity;
	private int registrationCount;
	private Set<User> registerdUsers;

	public EvacuationEventImpl(long id)
	{
	}

	public enum TransportMean
	{
		Car("Car"), Bus("bus");
		private String name;

		TransportMean(String name)
		{
			this.name = name;
		}

		/**
		 * @return the name
		 */
		public synchronized final String getName()
		{
			return name;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.technion.ex5.Data.EvacuationEvent#getGeometry()
	 */
	public synchronized final Shape getGeometry()
	{
		return geometry;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.technion.ex5.Data.EvacuationEvent#getStartTime()
	 */
	public synchronized final Date getStartTime()
	{
		return startTime;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.technion.ex5.Data.EvacuationEvent#getTransportType()
	 */
	public synchronized final TransportMean getTransportType()
	{
		return transportType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.technion.ex5.Data.EvacuationEvent#getCapacity()
	 */
	public synchronized final int getCapacity()
	{
		return capacity;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.technion.ex5.Data.EvacuationEvent#getRegistrationCount()
	 */
	public synchronized final int getRegistrationCount()
	{
		return registrationCount;
	}

	public boolean isInTime(Date date)
	{
		return startTime.getTime() < date.getTime()
				&& startTime.getTime() + duration.getTime() > date.getTime();
	}

	public synchronized boolean add(User user)
	{
		if (registerdUsers.size() < capacity)
		{
			registerdUsers.add(user);
			user.setRegistered(true);
			return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	public long getId()
	{
		return id;
	}

}
