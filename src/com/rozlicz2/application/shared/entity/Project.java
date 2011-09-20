package com.rozlicz2.application.shared.entity;

import java.util.List;

import javax.persistence.Embedded;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class Project extends ProjectList {

	@Embedded
	private List<ParticipantEntity> participants;

	public Project() {
	}

	public List<ParticipantEntity> getParticipants() {
		return participants;
	}

	public void setParticipants(List<ParticipantEntity> participants) {
		this.participants = participants;
	}

}
