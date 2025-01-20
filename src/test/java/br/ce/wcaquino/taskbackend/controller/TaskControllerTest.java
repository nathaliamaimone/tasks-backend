package br.ce.wcaquino.taskbackend.controller;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.utils.ValidationException;

public class TaskControllerTest {
		
	@Test
	public void shouldFailToSaveTaskWithoutDescription() {
		Task todo = new Task();
//		todo.setTask("Description");
		todo.setDueDate(LocalDate.now());
		TaskController controller = new TaskController();
		try {
			controller.save(todo);
		} catch (ValidationException e) {
			Assert.assertEquals("Fill the task description", e.getMessage());
		}
	} 

	@Test
	public void shouldFailToSaveTaskWithoutDueDate() {
		Task todo = new Task();
		todo.setTask("Description");
//		todo.setDueDate(LocalDate.now());
		TaskController controller = new TaskController();
		try {
			controller.save(todo);
		} catch (ValidationException e) {
			Assert.assertEquals("Fill the due date", e.getMessage());
		}
	}

	@Test
	public void shouldFailToSaveTaskWithPastDueDate() {
		Task todo = new Task();
		todo.setTask("Description");
		todo.setDueDate(LocalDate.of(2010,01,01));
		TaskController controller = new TaskController();
		try {
			controller.save(todo);
		} catch (ValidationException e) {
			Assert.assertEquals("Due date must not be in past", e.getMessage());
		}
	}
	
}
