var dataGen = function(){

	return {

		oldBase : function(){
			return [
			        {
			        	"text" : "Cultural Heritage Library (14 books)",
			        	"id" : "root",
			        	"name" : "root",
			        	
						"data": {
							"childrenUrl":	"dataGen.books()",
							"total": 14
						}			        	
			        }
			]
		},

		base : function(){
			
			return {
		        	"text" : "Cultural Heritage Library (14 books - child 9)",
			        	"id" : "root",
			        	"name" : "root",     							
						"data": {
							"parentUrl":	"dataGen.parent1()",
							"index":        9,
							"childrenUrl":	"dataGen.books('dataGen.base()')",
							"total":		14
						}
						/*
						, "children": [
									     { "text":	"title 1"},
									     { "text":	"title 2"},
									     { "text":	"title 3"}
						]
						*/
			        
			        }
			
		},
		
		parent1 : function(){
			return {
				"id": "parent_1",
				"text": 'Parent 1',
				"data": {
					"childrenUrl" :		 "dataGen.parent1_kids()",
					"total":     17,

					"parentUrl": "dataGen.parent2()",
					"index":     1
				}
			}

		},
		
		parent1_kids : function(){
			return [
			    {   
			    	"text": 'Parent 1 Child 1',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        1
					}
				},
			    {   
			    	"text": 'Parent 1 Child 2',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        2
					}
				},
				{   
					"text": 'Parent 1 Child 3',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        3
					}
				},				
			    {
			    	"text": 'Parent 1 Child 4',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        4
					}
				},
				{   
					"text": 'Parent 1 Child 5',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        5
					}
				},
				{   
					"text": 'Parent 1 Child 6',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        6
					}
				},
				{   
					"text": 'Parent 1 Child 7',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        7
					}
				},
				{   
					"text": 'Parent 1 Child 8',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        8
					}
				},
				
				dataGen.base(),
				
				{   
					"text": 'Parent 1 Child 10',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        10
					}
				},
				{   
					"text": 'Parent 1 Child 11',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        11
					}
				},
				{   
					"text": 'Parent 1 Child 12',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        12
					}
				},
				{   
					"text": 'Parent 1 Child 13',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        13
					}
				},
				{   
					"text": 'Parent 1 Child 14',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        14
					}
				},
				{   
					"text": 'Parent 1 Child 15',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        15
					}
				},
				{   
					"text": 'Parent 1 Child 16',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        16
					}
				},
				{   
					"text": 'Parent 1 Child 17',
					"data": {
						"parentUrl":	"dataGen.parent1()",
						"index":        17
					}
				}

			]

		},

		parent2 : function(){
			return {
				"text": 'Parent 2',
				
				"ZZZurl" :"",

				"data": {
					"childrenUrl":	"dataGen.parent2_kids()",
					"total": 1,
					"index": 1
				}
			}
		},

		parent2_kids : function(){
			return [dataGen.parent1()];
		},
		
		books : function(parentUrlIn){
		
			return [
					 {
						"text": '<span>Book 1 (8 volumes) - a book with a title that is really long and wants to line wrap.</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[0]')[0]",
							"total": 8,
							"index": 1,
						}
					 },
					 {	 
						"text": 'Book 2 (2 volumes)',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[1]')[1]",
							"total": 2,
							"index": 2
						}
					 },
					 {	 
						"text": 'Book 3 (3 volumes)',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[2]')[2]",
							"total": 3,
							"index": 3
						}

					 },
					 {
						"text": '<span>Book 4 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[3]')[0]",
							"total": 8,
							"index": 4
						}
					 },
					 {
						"text": '<span>Book 5 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[4]')[0]",
							"total": 8,
							"index": 5
						}
					 },
					 {
						"text": '<span>Book 6 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[5]')[0]",
							"total": 8,
							"index": 6
						}
					 },
					 {
						"text": '<span>Book 7 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[6]')[0]",
							"total": 8,
							"index": 7
						}
					 },
					 {
						"text": '<span>Book 8 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[7]')[0]",
							"total": 8,
							"index": 8
						}
					 },
					 {
						"text": '<span>Book 9 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[8]')[0]",
							"total": 8,
							"index": 9
						}
					 },
					 {
						"text": '<span>Book 10 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[9]')[0]",
							"total": 8,
							"index": 10
						}
					 },
					 {
						"text": '<span>Book 11 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[10]')[0]",
							"total": 8,
							"index": 11
						}
					 },
					 {
						"text": '<span>Book 12 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[11]')[0]",
							"total": 8,
							"index": 12
						}
					 },
					 {
						"text": '<span>Book 13 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[12]')[0]",
							"total": 8,
							"index": 13
						}
					 },
					 {
						"text": '<span>Book 14 (Book 1 reprint - 8 volumes)</span>',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.volumes('dataGen.books()[13]')[0]",
							"total": 8,
							"index": 14
						}
					 }

			]
		},
		
		volumes : function(parentUrlIn){
			
			return[
			       [
			
						{
							"text": 'Volume 1 (2 chapters) - a small volume with a large name.  Guaranteed to line wrap',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[0][0]')",
								"total": 2,
								"loaded" : 0,
								"index": 1
							}
						},
						{
							"text": 'Volume 2 (2 chapters)',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[0][1]')",
								"total": 2,
								"loaded" : 0,
								"index": 2
							}
						},
						{
							"text": 'Volume 3 (2 chapters)',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[0][2]')",
								"total": 2,
								"loaded" : 0,
								"index": 3
							}
						},
						{
							"text": 'Volume 4 (2 chapters)',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[0][3]')",
								"total": 2,
								"loaded" : 0,
								"index": 4
							}
						},
						{
							"text": 'Volume 5 (2 chapters)',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[0][4]')",
								"total": 2,
								"loaded" : 0,
								"index": 5
							}
						},
						{
							"text": 'Volume 6 (2 chapters)',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[0][5]')",
								"total": 2,
								"loaded" : 0,
								"index": 6
							}
						},
						{
							"text": 'Volume 7 (2 chapters)',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[0][6]')",
								"total": 2,
								"loaded" : 0,
								"index": 7
							}
						},
						{
							"text": 'Volume 8 (2 chapters)',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[0][7]')",
								"total": 2,
								"loaded" : 0,
								"index": 8
							}
						}
			        ],
			        /* vol 2 */
			        [
						{
							"text": 'V2 Volume 1 (8 chapters)',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[1][0]')",
								"total": 8,
								"loaded" : 0,
								"index": 1
							}
						},
						{
							"text": 'V2 Volume 2 (8 chapters)',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[1][1]')",
								"total": 8,
								"loaded" : 0,
								"index": 2
							}
						}
			        ],
			        /* vol 3 */
			        [
						{
							"text": 'Volume 1 (2 chapters)',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[1][2]')",
								"total": 2,
								"loaded" : 0,
								"index": 1
							}
						},
						{
							"text": '<a href="example2.html">Volume 2 (5 chapters)</a>',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[1][3]')",
								"total": 5,
								"loaded" : 0,
								"index": 2
							}
						},
						{
							"text": 'Volume 3 (7 chapters)',
							"data": {
								"parentUrl":	parentUrlIn,
								"childrenUrl":	"dataGen.chapters('dataGen.volumes()[1][4]')",
								"total": 7,
								"loaded" : 0,
								"index": 3
							}
						}			         
			        ]

				];
		},
		
		chapters : function(parentUrlIn){
			
			return [
					 {
						"text": 'Chapter 1 (10 verses)',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.verses('dataGen.chapters()[0]')",
							"total": 10,
							"index": 1
						}
					 },
					 {	 
						"text": 'Chapter 2 (10 verses)',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.verses('dataGen.chapters()[1]')",
							"total": 10,
							"index": 2
						}
					 },
					 {	 
						"text": 'Chapter 3 (10 verses)',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.verses('dataGen.chapters()[2]')",
							"total": 10,
							"index": 3
						}
					 },
					 {	 
						"text": 'Chapter 4 (10 verses)',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.verses('dataGen.chapters()[3]')",
							"total": 10,
							"index": 4
						}
					 },
					 {	 
						"text": 'Chapter 5 (10 verses)',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.verses('dataGen.chapters()[4]')",
							"total": 10,
							"index": 5
						}
					 },
					 {	 
						"text": 'Chapter 6 (10 verses)',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.verses('dataGen.chapters()[5]')",
							"total": 10,
							"index": 6
						}
					 },
					 {	 
						"text": 'Chapter 7 (10 verses)',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.verses('dataGen.chapters()[6]')",
							"total": 10,
							"index": 7
						}
					 },
					 {	 
						"text": 'Chapter 8 (10 verses)',
						"data": {
							"parentUrl":	parentUrlIn,
							"childrenUrl":	"dataGen.verses('dataGen.chapters()[7]')",
							"total": 10,
							"index": 8
						}
					 }
				];
		},
		
		verses: function(parentUrlIn){
			
			return [
				 {
					"text": 'Verse 1',
					"data": {
						"parentUrl":	parentUrlIn,
						"index": 1
					}
				 },
				 {
					"text": 'Verse 2',
					"data": {
						"parentUrl":	parentUrlIn,
						"index": 2
					}
				 },
				 {	 
					"text": 'Verse 3',
					"data": {
						"parentUrl":	parentUrlIn,
						"index": 3
					}
				 },
				 {	 
					"text": 'Verse 4',
					"data": {
						"parentUrl":	parentUrlIn,
						"index": 4
					}
				 },
				 {	 
					"text": 'Verse 5',
					"data": {
						"parentUrl":	parentUrlIn,
						"index": 5
					}
				 },
				 {	 
					"text": 'Verse 6',
					"data": {
						"parentUrl":	parentUrlIn,
						"index": 6
					}
				 },
				 {	 
					"text": 'Verse 7',
					"data": {
						"parentUrl":	parentUrlIn,
						"index": 7
					}
				 },
				 {	 
					"text": 'Verse 8',
					"data": {
						"parentUrl":	parentUrlIn,
						"index": 8
					}
				 },
				 {	 
					"text": 'Verse 9',
					"data": {
						"parentUrl":	parentUrlIn,
						"index": 9
					}
				 },
				 {	 
					"text": 'Verse 10',
					"data": {
						"parentUrl":	parentUrlIn,
						"index": 10
					}
				 }
			] 
		}
		
	};
}();





