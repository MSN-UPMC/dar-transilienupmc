/*global angular */

/**
 * Services that persists and retrieves GAREs from localStorage
 */
angular.module('garemvc')
	.factory('gareStorage', function () {
		'use strict';

		var STORAGE_ID = 'gares-angularjs';

		return {

            isBackEndReady: false,

            list: function (callback) {
                if (!this.isBackEndReady) {
                    console.log("gares api is not ready");
                    return;
                }
                console.log("getting gares list");
                gapi.client.gares.list().execute(callback);
            },

            create: function (gare, callback) {
                if (!this.isBackEndReady) {
                    console.log("gares api is not ready");
                    return;
                }
                console.log("create :" + gare);
                gapi.client.gares.create(gare).execute(callback);
            },

            update: function (gare, callback) {
                if (!this.isBackEndReady) {
                    console.log("gares api is not ready");
                    return;
                }
                console.log("update :" + gare);
                gapi.client.gares.update(gare).execute(callback);
            },

            remove: function (gare, callback) {
                if (!this.isBackEndReady) {
                    console.log("gares api is not ready");
                    return;
                }
                console.log("remove :" + gares);
                gapi.client.gares.remove({"id": gare.id}).execute(callback);
            }

		};
	});
