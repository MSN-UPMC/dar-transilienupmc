package com.upmc.transilien.v1.endPoint;

import java.util.Collection;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.upmc.transilien.v1.model.Todo;
import com.upmc.transilien.v1.repository.TodoRepository;

@Api(name = "todos", version = "v1")
public class TodoEndPoint {

	@ApiMethod(name = "list", httpMethod = ApiMethod.HttpMethod.GET, path = "list")
	public Collection<Todo> getTodos() {
		return TodoRepository.getInstance().findTodos();
	}

	@ApiMethod(name = "create", httpMethod = ApiMethod.HttpMethod.POST, path = "create")
	public Todo create(Todo todo) {
		return TodoRepository.getInstance().create(todo);
	}

	@ApiMethod(name = "update", httpMethod = ApiMethod.HttpMethod.PUT, path = "update")
	public Todo update(Todo editedTodo) {
		return TodoRepository.getInstance().update(editedTodo);
	}

	@ApiMethod(name = "remove", httpMethod = ApiMethod.HttpMethod.DELETE, path = "remove")
	public void remove(@Named("id") Long id) {
		TodoRepository.getInstance().remove(id);
	}

}
