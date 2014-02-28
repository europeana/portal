var dataGen = function(){

	return {
		
		base : function(){
			return [
			        {
			        	"text" : "Cultural Heritage Library (5 books)",
			        	"id" : "root",
			        	"name" : "root",
			        	
						"data": {
							"url":	"dataGen.books()",
							"total": 14
						}			        	
			        }
			]
		},

		books : function(){
		
			return [
					 {
						"text": '<span>Book 1 (8 volumes) - a book with a title that is really long and wants to line wrap.</span>',
						"data": {
							"url":	"dataGen.volumes()[0]",
							"total": 8
						}
					 },
					 {	 
						"text": 'Book 2 (2 volumes)',
						"data": {
							"url":	"dataGen.volumes()[1]",
							"total": 2
						}
					 },
					 {	 
						"text": 'Book 3 (3 volumes)',
						"data": {
							"url":	"dataGen.volumes()[2]",
							"total": 3
						}

					 },
					 {
						"text": '<span>Book 4 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"url":	"dataGen.volumes()[0]",
							"total": 8
						}
					 },
					 {
							"text": '<span>Book 5 (Book 1 reprint - 8 volumes)</span>',
							"data": {
								"url":	"dataGen.volumes()[0]",
								"total": 8
							}
					 },
					 {
							"text": '<span>Book 6 (Book 1 reprint - 8 volumes)</span>',
							"data": {
								"url":	"dataGen.volumes()[0]",
								"total": 8
							}
					 },
					 {
							"text": '<span>Book 7 (Book 1 reprint - 8 volumes)</span>',
							"data": {
								"url":	"dataGen.volumes()[0]",
								"total": 8
							}
					 },
					 {
							"text": '<span>Book 8 (Book 1 reprint - 8 volumes)</span>',
							"data": {
								"url":	"dataGen.volumes()[0]",
								"total": 8
							}
					 },
					 {
							"text": '<span>Book 9 (Book 1 reprint - 8 volumes)</span>',
							"data": {
								"url":	"dataGen.volumes()[0]",
								"total": 8
							}
					 },
					 {
							"text": '<span>Book 10 (Book 1 reprint - 8 volumes)</span>',
							"data": {
								"url":	"dataGen.volumes()[0]",
								"total": 8
							}
					 },
					 {
							"text": '<span>Book 11 (Book 1 reprint - 8 volumes)</span>',
							"data": {
								"url":	"dataGen.volumes()[0]",
								"total": 8
							}
					 },
					 {
							"text": '<span>Book 12 (Book 1 reprint - 8 volumes)</span>',
							"data": {
								"url":	"dataGen.volumes()[0]",
								"total": 8
							}
					 },
					 {
							"text": '<span>Book 13 (Book 1 reprint - 8 volumes)</span>',
							"data": {
								"url":	"dataGen.volumes()[0]",
								"total": 8
							}
					 },
					 {
							"text": '<span>Book 14 (Book 1 reprint - 8 volumes)</span>',
							"data": {
								"url":	"dataGen.volumes()[0]",
								"total": 8
							}
					 }

			]
		},
		
		volumes : function(){
			
			return[
			       [
			
						{
							"text": 'Volume 1 (2 chapters) - a small volume with a large name.  Guaranteed to line wrap',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 2,
								"loaded" : 0
							}
						},
						{
							"text": 'Volume 2 (2 chapters)',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 2,
								"loaded" : 0
							}
						},
						{
							"text": 'Volume 3 (2 chapters)',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 2,
								"loaded" : 0
							}
						},
						{
							"text": 'Volume 4 (2 chapters)',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 2,
								"loaded" : 0
							}
						},
						{
							"text": 'Volume 5 (2 chapters)',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 2,
								"loaded" : 0
							}
						},
						{
							"text": 'Volume 6 (2 chapters)',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 2,
								"loaded" : 0
							}
						},
						{
							"text": 'Volume 7 (2 chapters)',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 2,
								"loaded" : 0
							}
						},
						{
							"text": 'Volume 8 (2 chapters)',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 2,
								"loaded" : 0
							}
						}
			        ],
			        /* vol 2 */
			        [
						{
							"text": 'Volume 1 (8 chapters)',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 8,
								"loaded" : 0
							}
						},
						{
							"text": 'Volume 2 (8 chapters)',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 8,
								"loaded" : 0
							}
						}
			        ],
			        /* vol 3 */
			        [
						{
							"text": 'Volume 1 (2 chapters)',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 2,
								"loaded" : 0
							}
						},
						{
							"text": '<a href="example2.html">Volume 2 (5 chapters)</a>',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 5,
								"loaded" : 0
							}
						},
						{
							"text": 'Volume 3 (7 chapters)',
							"data": {
								"url":	"dataGen.chapters()",
								"total": 7,
								"loaded" : 0
							}
						}			         
			        ]

				];
						
		},
		
		chapters : function(){
			
			return [
					 {
						"text": 'Chapter 1 (10 verses)',
						"data": {
							"url":	"dataGen.verses()",
							"total": 10
						}
					 },
					 {	 
						"text": 'Chapter 2 (10 verses)',
						"data": {
							"url":	"dataGen.verses()",
							"total": 10
						}
					 },
					 {	 
						"text": 'Chapter 3 (10 verses)',
						"data": {
							"url":	"dataGen.verses()",
							"total": 10
						}
					 },
					 {	 
						"text": 'Chapter 4 (10 verses)',
						"data": {
							"url":	"dataGen.verses()",
							"total": 10
						}
					 },
					 {	 
						"text": 'Chapter 5 (10 verses)',
						"data": {
							"url":	"dataGen.verses()",
							"total": 10
						}
					 },
					 {	 
						"text": 'Chapter 6 (10 verses)',
						"data": {
							"url":	"dataGen.verses()",
							"total": 10
						}
					 },
					 {	 
						"text": 'Chapter 7 (10 verses)',
						"data": {
							"url":	"dataGen.verses()",
							"total": 10
						}
					 },
					 {	 
						"text": 'Chapter 8 (10 verses)',
						"data": {
							"url":	"dataGen.verses()",
							"total": 10
						}
					 }
				];
		},
		
		verses: function(){
			
			return [
				 {
					"text": 'Verse 1',
				 },
				 {	 
					"text": 'Verse 2'
				 },
				 {	 
					"text": 'Verse 3'
				 },
				 {	 
					"text": 'Verse 4'
				 },
				 {	 
					"text": 'Verse 5'
				 },
				 {	 
					"text": 'Verse 6'
				 },
				 {	 
					"text": 'Verse 7'
				 },
				 {	 
					"text": 'Verse 8'
				 },
				 {	 
					"text": 'Verse 9'
				 },
				 {	 
					"text": 'Verse 10'
				 }
			] 
		}
		
	};
}();





