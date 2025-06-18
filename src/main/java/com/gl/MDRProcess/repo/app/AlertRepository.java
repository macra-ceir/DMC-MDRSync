package com.gl.MDRProcess.repo.app;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gl.MDRProcess.model.app.AlertMessages;




@Repository
public interface AlertRepository extends JpaRepository<AlertMessages, Integer> {

	public AlertMessages findByAlertIdIn(String alertId);
}
