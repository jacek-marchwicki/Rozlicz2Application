package com.rozlicz2.application.server.service;

import java.util.List;

import com.rozlicz2.application.shared.entity.Expense;
import com.rozlicz2.application.shared.entity.ParticipantEntity;

public interface ExpenseDao {

	public abstract double calculateSum(Expense expense);

	public abstract List<Expense> findByProjectId(String projectId);

	public abstract double refreshParticipantsForProject(
			List<ParticipantEntity> participants, String projectId);

	public abstract Expense uFind(String id);

	public abstract List<Expense> uFindByProjectId(String projectId);

	public abstract void uSave(Expense list);

	public abstract Expense uSaveAndReturn(Expense expense);

}