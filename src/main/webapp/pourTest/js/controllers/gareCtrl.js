/*global angular */

/**
 * The main controller for the app. The controller: - retrieves and persists the
 * model via the gareStorage service - exposes the model to the template and
 * provides event handlers
 */
angular.module('garemvc').controller(
		'GareCtrl',
		function GareCtrl($scope, $routeParams, $window, $filter, gareStorage) {
			'use strict';

			$scope.gares = [];

			$scope.loadGares = function() {
				gareStorage.list(function(resp) {
					console.log(resp);
					console.log("gares api sucessfully called");
					if (resp.items != undefined) {
						$scope.gares = resp.items;
					}
					$scope.$apply();
				});
			};

			$scope.loadGares();

			$scope.newGare = '';
			$scope.editedGare = null;

			/**
			 * Ajout pour fonctionner avec Google Cloud Endpoint Fonction
			 * interceptant l'appel à window.init() effectué dans index.html
			 */
			$window.init = function() {
				console.log("$window.init called");
				$scope.$apply($scope.load_gapi_gare_lib);
			};

			/**
			 * Charge l'api gares
			 */
			$scope.load_gapi_gare_lib = function() {
				console.log("load_gare_lib called");

				var rootApi = $window.location.origin + '/_ah/api';

				gapi.client.load('gares', 'v1', function() {
					console.log("gares api loaded");
					gareStorage.isBackEndReady = true;
					$scope.loadGares();
				}, rootApi);
			};

			$scope.addGare = function() {
				var newGareName = $scope.newGare.trim();
				if (!newGareName.length) {
					return;
				}

				var newGare = {
					nom : newGareName,
					codeUIC : 0,
					longitude : 0,
					lattitude : 0
				};

				gareStorage.create(newGare, function(gareResp) {
					$scope.gares.push({
						id : gareResp.id,
						nom : gareResp.nom,
						codeUIC : gareResp.codeUIC,
						longitude : gareResp.longitude,
						lattitude : gareResp.lattitude
					});
					$scope.$apply();
				});

				$scope.newGare = '';
			};

			$scope.editGare = function(gare) {
				$scope.editedGare = gare;
				// Clone the original gare to restore it on demand.
				$scope.originalGare = angular.extend({}, gare);
			};

			$scope.doneEditing = function(gare) {
				$scope.editedGare = null;
				gare.nom = gare.nom.trim();

				if (!gare.nom) {
					$scope.removeGare(gare);
				} else {
					gareStorage.update(gare, function(gare) {
						console.log('gare with id ' + gare.result.id
								+ ' successfully updated');
					});
				}
			};

			$scope.removeGare = function(gare) {
				$scope.gares.splice($scope.gares.indexOf(gare), 1);
				gareStorage.remove(gare, function() {
					$scope.$apply();
				})
			};
		});
