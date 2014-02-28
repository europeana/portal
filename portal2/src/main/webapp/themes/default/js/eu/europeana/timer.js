/*global js, eu, jQuery */
/*jslint browser: true, white: true */
/**
 * a universal timer that can be used by all scripts to run callbacks
 * during the given interval
 *
 * var timer = eu.europeana.timer.addCallback({
 *   timer: 100,
 *   fn: myFunction,
 *   context: null   //could also be this, window, or any other object
 * });
 */
(function( $ ) {
	'use strict';
	js.utils.registerNamespace( 'eu.europeana.timer' );

	eu.europeana.timer = {

		/**
		 * an object of timers running at different intervals
		 *
		 * each interval object holds an object collection of callbacks that are
		 * called when the interval’s function runs
		 */
		timers: {
			100: {
				timer: setInterval( function() { eu.europeana.timer.processTimers( 100 ); }, 100 ),
				callbacks: {}
			},
			500: {
				timer: setInterval( function() { eu.europeana.timer.processTimers( 500 ); }, 500 ),
				callbacks: {}
			}
		},

		/**
		 * @param {object} params
		 *
		 * @param {string} params.timer
		 * name of the timer that the args should be applied to
		 *
		 * @param {string} params.fn
		 * string representation of the function
		 *
		 * @param {object} params.context
		 * the this object to be used in the apply statement
		 *
		 * @param {array} params.args
		 * the arguments to be used in the apply statement
		 *
		 * @returns {object}
		 * an object that can be passed to the removeCallback method
		 */
		addCallback: function( params ) {
			if ( !$.isFunction( params.fn ) ) {
				throw new Error( 'params.fn is not a function or a reference to a function' );
			}

			if ( typeof params.context !== 'object' ) {
				throw new Error( 'context is not an object' );
			}

			if ( this.timers[params.timer] === undefined ) {
				throw new Error( 'timer does not exist' );
			}

			var uuid = js.utils.guid();

			this.timers[params.timer].callbacks[uuid] = {
				args: params.args,
				fn: params.fn,
				context: params.context
			};

			return {
				timer: params.timer,
				index: uuid
			};
		},

		processTimers: function( interval ) {
			$.each( this.timers[interval].callbacks, function() {
				var callback = this;
				callback.fn.apply( callback.context, callback.args );
			});
		},

		/**
		 * remove a callback registered with a timer
		 *
		 * @param {object} params
		 *
		 * @param {int} params.timer
		 * the timer the callback is registered with
		 *
		 * @param {int} params.index
		 * the index of the callback that should be removed
		 *
		 * @returns {bool}
		 */
		removeCallback: function( params ) {
			if ( typeof params !== 'object' || this.timers[params.timer] === undefined  ) {
				throw new Error( 'timer [' + params.timer + '] does not exist' );
			}

			if ( typeof params.index !== 'string' ) {
				throw new Error( 'index [' + params.index + '] is not a valid index' );
			}

			return delete this.timers[params.timer].callbacks[params.index];
		}

	};

}( jQuery ));
