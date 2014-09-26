package com.upmc.transilien.model.todo;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

public class TodoRepository {

	private static TodoRepository todoRepository = null;

	static {
		ObjectifyService.register(Todo.class);
	}

	private TodoRepository() {
	}

	public static synchronized TodoRepository getInstance() {
		if (null == todoRepository) {
			todoRepository = new TodoRepository();
		}
		return todoRepository;
	}

	public Collection<Todo> findTodos() {
		List<Todo> todos = ofy().load().type(Todo.class).list();
		return todos;
	}

	public Todo create(Todo todo) {
		ofy().save().entity(todo).now();
		return todo;
	}

	public Todo update(Todo editedTodo) {
		if (editedTodo.getId() == null) {
			return null;
		}

		Todo todo = ofy().load()
				.key(Key.create(Todo.class, editedTodo.getId())).now();
		todo.setCompleted(editedTodo.isCompleted());
		todo.setTitle(editedTodo.getTitle());
		ofy().save().entity(todo).now();

		return todo;
	}

	public void remove(Long id) {
		if (id == null) {
			return;
		}
		ofy().delete().type(Todo.class).id(id).now();
	}
}
