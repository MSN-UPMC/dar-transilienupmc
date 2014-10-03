/*global angular */

/**
 * Services that persists and retrieves TODOs from localStorage
 */
angular.module('todomvc')
	.factory('todoStorage', function () {
		'use strict';

		var STORAGE_ID = 'todos-angularjs';

		return {

            isBackEndReady: false,

            list: function (callback) {
                if (!this.isBackEndReady) {
                    console.log("todos api is not ready");
                    return;
                }
                console.log("getting todos list");
                gapi.client.todos.list().execute(callback);
            },

            create: function (todo, callback) {
                if (!this.isBackEndReady) {
                    console.log("todos api is not ready");
                    return;
                }
                console.log("create :" + todo);
                gapi.client.todos.create(todo).execute(callback);
            },

            update: function (todo, callback) {
                if (!this.isBackEndReady) {
                    console.log("todos api is not ready");
                    return;
                }
                console.log("update :" + todo);
                gapi.client.todos.update(todo).execute(callback);
            },

            remove: function (todo, callback) {
                if (!this.isBackEndReady) {
                    console.log("todos api is not ready");
                    return;
                }
                console.log("remove :" + todo);
                gapi.client.todos.remove({"id": todo.id}).execute(callback);
            }

		};
	});
