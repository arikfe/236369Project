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
			reports.add(new ReportResult(r.getUsername(), r.getTitle(), r
					.getContent(), format.format(r.getExpiration()),
					new Geometry(r.getCordinates())));
		for (final EvacuationEvent e : events)
			evacuationEvents.add(new EventResult(
					new Geometry(e.getCordinates()), format.format(e
							.getEstimated()), e.getMeans(), e.getCapacity(), e
							.getAmountLeft()));

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

		public ReportResult(final String user, final String title,
				final String content, final String expirationTime,
				final Geometry geometry)
		{
			super();
			this.user = user;
			this.title = title;
			this.content = content;
			this.expirationTime = expirationTime;
			this.geometry = geometry;
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

		public EventResult(final Geometry geometry, final String estimatedTime,
				final String meanOfEvacuation, final int capacity,
				final int registrationCount)
		{
			super();
			this.geometry = geometry;
			this.estimatedTime = estimatedTime;
			this.meanOfEvacuation = meanOfEvacuation;
			this.capacity = capacity;
			this.registrationCount = registrationCount;
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
