/*global angular */

/**
 * The main controller for the app. The controller:
 * - retrieves and persists the model via the todoStorage service
 * - exposes the model to the template and provides event handlers
 */
angular.module('todomvc')
	.controller('TodoCtrl', function TodoCtrl($scope, $routeParams, $window, $filter, todoStorage) {
		'use strict';

        $scope.todos = [];

        $scope.loadTodos = function() {
            todoStorage.list(function(resp) {
                console.log(resp);
                console.log("todos api sucessfully called");
                if (resp.items != undefined) {
                    $scope.todos = resp.items;
                }
                $scope.$apply();
            });
        };

        $scope.loadTodos();

		$scope.newTodo = '';
		$scope.editedTodo = null;

        /**
         * Ajout pour fonctionner avec Google Cloud Endpoint
         * Fonction interceptant l'appel à window.init() effectué dans index.html
         */
        $window.init= function() {
            console.log("$window.init called");
            $scope.$apply($scope.load_gapi_todo_lib);
        };

        /**
         * Charge l'api todos
         */
        $scope.load_gapi_todo_lib = function() {
            console.log("load_todo_lib called");

            var rootApi = $window.location.origin + '/_ah/api';

            gapi.client.load('todos', 'v2', function() {
                console.log("todos api loaded");
                todoStorage.isBackEndReady = true;
                $scope.loadTodos();
            }, rootApi);
        };

		$scope.$watch('todos', function (newValue, oldValue) {
			$scope.remainingCount = $filter('filter')($scope.todos, { completed: false }).length;
			$scope.completedCount = $scope.todos.length - $scope.remainingCount;
			$scope.allChecked = !$scope.remainingCount;
		}, true);

		// Monitor the current route for changes and adjust the filter accordingly.
		$scope.$on('$routeChangeSuccess', function () {
			var status = $scope.status = $routeParams.status || '';

			$scope.statusFilter = (status === 'active') ?
				{ completed: false } : (status === 'completed') ?
				{ completed: true } : null;
		});

        $scope.addTodo = function () {
            var newTodoTile = $scope.newTodo.trim();
            if (!newTodoTile.length) {
                return;
            }

            var newTodo = {
                title: newTodoTile,
                completed: false
            };

            todoStorage.create(newTodo, function(todoResp) {
                $scope.todos.push({
                    id: todoResp.id,
                    title: todoResp.title,
                    completed: todoResp.completed
                });
                $scope.$apply();
            });

            $scope.newTodo = '';
        };

		$scope.editTodo = function (todo) {
			$scope.editedTodo = todo;
			// Clone the original todo to restore it on demand.
			$scope.originalTodo = angular.extend({}, todo);
		};

		$scope.doneEditing = function (todo) {
            $scope.editedTodo = null;
            todo.title = todo.title.trim();

            if (!todo.title) {
                $scope.removeTodo(todo);
            } else {
                todoStorage.update(todo, function(todo) {
                    console.log('todo with id ' + todo.result.id + ' successfully updated');
                });
            }
		};

		$scope.revertEditing = function (todo) {
			$scope.todos[$scope.todos.indexOf(todo)] = $scope.originalTodo;
			$scope.doneEditing($scope.originalTodo);
		};

        $scope.completeTodo = function (todo) {
            todo.completed = !todo.completed;
            todoStorage.update(todo, function() {
                $scope.$apply();
            })
        };

		$scope.removeTodo = function (todo) {
            $scope.todos.splice($scope.todos.indexOf(todo), 1);
            todoStorage.remove(todo, function() {
                $scope.$apply();
            })
		};

		$scope.clearCompletedTodos = function () {
			$scope.todos = $scope.todos.filter(function (val) {
				return !val.completed;
			});
		};

		$scope.markAll = function (completed) {
            $scope.todos.forEach(function (todo) {
				todo.completed = !completed;
			});
		};
	});
