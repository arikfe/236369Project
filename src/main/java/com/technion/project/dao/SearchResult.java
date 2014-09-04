package com.technion.project.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import com.google.common.collect.Lists;
import com.technion.project.model.EvacuationEvent;
import com.technion.project.model.Report;

public class SearchResult
{
	private List<ReportResult> reports;
	private List<EventResult> evacuationEvents;

	public void setReports(final List<ReportResult> reports)
	{
		this.reports = reports;
	}

	public void setEvacuationEvents(final List<EventResult> evacuationEvents)
	{
		this.evacuationEvents = evacuationEvents;
	}

	public List<ReportResult> getReports()
	{
		return reports;
	}

	public List<EventResult> getEvacuationEvents()
	{
		return evacuationEvents;
	}

	public SearchResult(final List<Report> _reports,
			final List<EvacuationEvent> events)
	{
		super();
		final SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss XXX");
		reports = Lists.newLinkedList();
		evacuationEvents = Lists.newLinkedList();
		for (final Report r : _reports)
			reports.add(new ReportResult(r, format));
		for (final EvacuationEvent e : events)
			evacuationEvents.add(new EventResult(e, format));

	}

	public SearchResult()
	{
		super();
		reports = Lists.newLinkedList();
		evacuationEvents = Lists.newLinkedList();
	}

	public class ReportResult
	{
		private String user;
		private String title;
		private String content;
		private String expirationTime;
		private Geometry geometry;
		private String address;

		public String getAddress()
		{
			return address;
		}

		public void setAddress(final String address)
		{
			this.address = address;
		}

		public void setUser(final String user)
		{
			this.user = user;
		}

		public void setTitle(final String title)
		{
			this.title = title;
		}

		public void setContent(final String content)
		{
			this.content = content;
		}

		public void setExpirationTime(final String expirationTime)
		{
			this.expirationTime = expirationTime;
		}

		public void setGeometry(final Geometry geometry)
		{
			this.geometry = geometry;
		}

		public Geometry getGeometry()
		{
			return geometry;
		}

		public ReportResult()
		{
			super();
			this.user = "";
			this.title = "";
			this.content = "";
			this.expirationTime = "";
			this.geometry = new Geometry();
		}

		public String getUser()
		{
			return user;
		}

		public String getTitle()
		{
			return title;
		}

		public String getContent()
		{
			return content;
		}

		public String getExpirationTime()
		{
			return expirationTime;
		}

		public ReportResult(final Report r, final SimpleDateFormat format)
		{
			super();
			this.user = r.getUsername();
			this.title = r.getTitle();
			this.content = r.getContent();
			this.expirationTime = format.format(r.getExpiration());
			this.geometry = new Geometry(r.getCordinates());
			this.address = r.getAddress();
		}

	}

	public class EventResult
	{
		private Geometry geometry;
		private String estimatedTime;
		private String meanOfEvacuation;
		private int capacity;

		public void setGeometry(final Geometry geometry)
		{
			this.geometry = geometry;
		}

		public void setEstimatedTime(final String estimatedTime)
		{
			this.estimatedTime = estimatedTime;
		}

		public void setMeanOfEvacuation(final String meanOfEvacuation)
		{
			this.meanOfEvacuation = meanOfEvacuation;
		}

		public void setCapacity(final int capacity)
		{
			this.capacity = capacity;
		}

		public EventResult()
		{
			super();
			this.geometry = new Geometry();
			this.estimatedTime = "";
			this.meanOfEvacuation = "";
			this.capacity = 0;
			this.registrationCount = 0;
		}

		public Geometry getGeometry()
		{
			return geometry;
		}

		public String getEstimatedTime()
		{
			return estimatedTime;
		}

		public String getMeanOfEvacuation()
		{
			return meanOfEvacuation;
		}

		public int getCapacity()
		{
			return capacity;
		}

		public int getRegistrationCount()
		{
			return registrationCount;
		}

		private final int registrationCount;
		private String address;

		public String getAddress()
		{
			return address;
		}

		public void setAddress(final String address)
		{
			this.address = address;
		}

		public EventResult(final EvacuationEvent e,
				final SimpleDateFormat format)
		{
			super();
			this.geometry = new Geometry(e.getCordinates());
			this.estimatedTime = format.format(e.getEstimated());
			this.meanOfEvacuation = e.getMeans();
			this.capacity = e.getCapacity();
			this.registrationCount = e.getAmountLeft();
			this.address = e.getAddress();
		}
	}

	public class Geometry
	{
		public Geometry(final float[] coordinates)
		{
			super();
			this.type = "Point";
			this.coordinates = coordinates;
		}

		public Geometry()
		{
			this.coordinates = new float[2];
			this.type = "Point";
		}

		private String type;
		private float[] coordinates;

		public void setType(final String type)
		{
			this.type = type;
		}

		public void setCoordinates(final float[] coordinates)
		{
			this.coordinates = coordinates;
		}

		public String getType()
		{
			return type;
		}

		public float[] getCoordinates()
		{
			return coordinates;
		}

	}
}
