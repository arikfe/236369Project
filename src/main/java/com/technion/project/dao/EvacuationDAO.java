package com.technion.project.dao;

import java.util.List;

import com.technion.project.model.EvacuationEvent;
import com.technion.project.model.User;

public interface EvacuationDAO
{

	public EvacuationEvent getByID(Long id);

	public void addEvecuationEvent(EvacuationEvent event);

	public void update(EvacuationEvent event);

	public void addUserToEvent(User user, long id);

	public void removeUserToEvent(User user, long id);

	public List<EvacuationEvent> getAll();

	public EvacuationEvent getClosest(float lat, float lng);

}