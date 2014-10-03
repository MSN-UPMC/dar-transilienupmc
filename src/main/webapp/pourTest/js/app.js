/*global angular */

/**
 * The main TodoMVC app module
 *
 * @type {angular.Module}
 */
angular.module('todomvc', ['ngRoute'])
	.config(function ($routeProvider) {
		'use strict';

		$routeProvider.when('/', {
			controller: 'TodoCtrl',
			templateUrl: 'todomvc-index.html'
		}).when('/:status', {
			controller: 'TodoCtrl',
			templateUrl: 'todomvc-index.html'
		}).otherwise({
			redirectTo: '/'
		});
	});


/**
 * The main GareMVC app module
 *
 * @type {angular.Module}
 */
angular.module('garemvc', ['ngRoute'])
	.config(function ($routeProvider) {
		'use strict';

		$routeProvider.when('/', {
			controller: 'GareCtrl',
			templateUrl: 'garemvc-index.html'
		}).when('/:status', {
			controller: 'GareCtrl',
			templateUrl: 'garemvc-index.html'
		}).otherwise({
			redirectTo: '/'
		});
	});