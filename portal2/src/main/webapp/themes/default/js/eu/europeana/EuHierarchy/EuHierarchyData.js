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
						"data": {
							"parentUrl":	"dataGen.parent1()",
							"index":        9,
							
							/* TODO - will an index of 1 on the root break the load?  */
							"xindex":	        0,
							
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

					"parentUrl": "dataGen.parentTop()",
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
						"index":        10,
						"XXXchildrenUrl":	"dataGen.p_1c_10children()",
						"total":		1
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

		p_1c_10children : function(){
			alert('caling in item 10 children....');
			return {
				"text": 'Load Test....',
				"data": {
					"parentUrl":	"dataGen.parent1_kids()[9]",
					"index": 1
				}
			}			
		},
		
		parentTop : function(){
			return {
				"text": 'The Abingdon Apocalypse',
				"data": {
					"childrenUrl":	"dataGen.parentTop_kids(dataGen.parentTop())",
					"total": 5,
					"index": 1
				}
			}
		},

		parentTop_kids : function(parentUrlIn){
			//return [dataGen.parent1()];
			return [
			        dataGen.parent1(),
			        /*
			        dataGen.apocalypse_vol_1('dataGen.parentTop_kids()[1]'),
			        dataGen.apocalypse_vol_2('dataGen.parentTop_kids()[2]')
			        */
			        dataGen.apocalypse_vol_1(),
			        dataGen.apocalypse_vol_2(),
			        dataGen.apocalypse_vol_3(),
			        dataGen.apocalypse_vol_4()

			];
		},
		

		apocalypse_vol_1 : function(){
			return {
				"id": "apocalypse_vol_1", 
				"text": "Volume 1",
				"data": {
					"parentUrl": 'dataGen.parentTop()',
					"childrenUrl":	"dataGen.apocalypse_vol_1_content()",
					"total": 3,
					"index": 2,
					"europeana" : {
						"url"  : "base=dataGen.apocalypse_vol_1()"
					}
				}

			}
		},
		
		apocalypse_vol_2 : function(){
			var id = "apocalypse_vol_2";
			return {
				"id": id,
				"text": "Volume 2",
				"data": {
					"parentUrl": 'dataGen.parentTop()',
					"index": 3,
					
					"childrenUrl":	"dataGen.apocalypse_vol_2_content('dataGen.apocalypse_vol_2()')",
					"total": 3,
					"europeana" : {
						"url"  : "base=dataGen." + id + "()"
					}
				}

			}
		},

		apocalypse_vol_3 : function(){
			var id = "apocalypse_vol_3";
			return {
				"id": id,
				"text": "Volume 3",
				"data": {
					"parentUrl": 'dataGen.parentTop()',
					"index": 4,
					"childrenUrl":	"dataGen.apocalypse_vol_3_content('dataGen.apocalypse_vol_3()')",
					"total": 3,
					"europeana" : {
						"url"  : "base=dataGen." + id + "()"
					}
				}

			}
		},

		apocalypse_vol_4 : function(){
			var id = "apocalypse_vol_4";
			return {
				"id": id,
				"text": "Volume 4",
				"data": {
					"parentUrl": 'dataGen.parentTop()',
					"index": 5,
					
					"childrenUrl":	"dataGen.apocalypse_vol_4_content('dataGen.apocalypse_vol_4()')",
					"total": 4,
					"europeana" : {
						"url"  : "base=dataGen." + id + "()"
					}
				}
			}
		},

		apocalypse_vol_1_content : function(){
			
			var parentUrl = 'dataGen.apocalypse_vol_1()';
			
			return [
			    {
			    	"id" : "apocalypse_vol_1_content_1",
					"text": 'Unfinished Miniature Of Martyrdoms, In \'The Abingdon Apocalypse\'',
					"data": {
						"parentUrl":	parentUrl,
						"index": 1,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_1_content()[0]"
								
						}
					}
				},
			    {
			    	"id" : "apocalypse_vol_1_content_2",
					"text": 'The Angel Appears To St. John On The Island Of Patmos, In \'The Abingdon Apocalypse\'',
					"data": {
						"parentUrl":	parentUrl,
						"index": 2,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_1_content()[1]"
						}
					}
				},
			    {
			    	"id" : "apocalypse_vol_1_content_3",
					"text": 'Miniature Representing The Idea That Those Who Despise The Warnings Of The Prophets Go To Hell, In \'The Abingdon Apocalypse\'',
					"data": {
						"parentUrl":	parentUrl,
						"index": 3,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_1_content()[2]"
						}
					}
				}
				]
		},
		
		apocalypse_vol_2_content : function(){
			
			var parentUrl = 'dataGen.apocalypse_vol_2()';
			
			return [
			    {
			    	"id" : "apocalypse_vol_2_content_1",
					"text": 'Drawing Of St. Christopher, Added To A Flyleaf Of \'The Abingdon Apocalypse\'',
					"data": {
						"parentUrl": parentUrl,
						"index": 1,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_2_content()[0]"
						}
					}
				},
			    {
			    	"id" : "apocalypse_vol_2_content_2",
					"text": 'Miniature Representing The Redemption Of The World, In \'The Abingdon Apocalypse\'',
					"data": {
						"parentUrl": parentUrl,
						"index": 2,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_2_content()[1]"
						}
					}
				},
			    {
			    	"id" : "apocalypse_vol_2_content_3",
					"text": 'Unfinished Drawing Of St. John The Evangelist, In \'The Abingdon Apocalypse\'',
					"data": {
						"parentUrl": parentUrl,
						"index": 3,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_2_content()[2]"
						}
					}
				}
				]
		},

		apocalypse_vol_3_content : function(parentUrlIn){
			
			var parentUrl = 'dataGen.apocalypse_vol_3()';
			
			return [
			    {
			    	"id" : "apocalypse_vol_3_content_1",
					"text": 'Inscriptions Added To A Flyleaf Of \'The Abingdon Apocalypse\'',
					"data": {
						"parentUrl":	parentUrl,
						"index": 1,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_3_content()[0]"
						}
					}
				},
			    {
			    	"id" : "apocalypse_vol_3_content_2",
					"text": 'Unfinished Miniature Illustrating Revelation 14:18, In \'The Abingdon Apocalypse\'',
					"data": {
						"parentUrl":	parentUrl,
						"index": 2,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_3_content()[1]"
						}
					}
				},
			    {
			    	"id" : "apocalypse_vol_3_content_3",
					"text": 'Unfinished Miniature Representing Revelation 11:7-8, In \'The Abingdon Apocalypse\'',
					"data": {
						"parentUrl":	parentUrl,
						"index": 3,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_3_content()[2]"
						}
					}
				}
				]
		},
		
		apocalypse_vol_4_content : function(){
			var parentUrl = 'dataGen.apocalypse_vol_4()';
			
			return [
			    {
			    	"id" : "apocalypse_vol_4_content_1",
					"text": 'Unfinished Miniature Representing The Commentary On Revelation 12:17-18, In \'The Abingdon Apocalypse\'',
					"data": {
						"parentUrl":	parentUrl,
						"index": 1,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_4_content()[0]"
						}
					}
				},
			    {
			    	"id" : "apocalypse_vol_4_content_2",
					"text": 'The Massacre Of The Innocents And The Flight Into Egypt, In \'The Abingdon Apocalypse\'',
					"data": {
						"parentUrl":	parentUrl,
						"index": 2,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_4_content()[1]"
						}
					}
				},
			    {
			    	"id" : "apocalypse_vol_4_content_3",
					"text": 'Extra Node 1',
					"data": {
						"parentUrl":	parentUrl,
						"index": 3,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_4_content()[2]"
						}
					}
				},
			    {
			    	"id" : "apocalypse_vol_4_content_4",
					"text": 'Extra Node 2',
					"data": {
						"parentUrl":	parentUrl,
						"index": 4,
						"europeana" : {
							"icon" : "image",
							"url"  : "base=dataGen.apocalypse_vol_4_content()[3]"
						}
					}
				}
				]
		},


		
		books : function(parentUrlIn){
		
			var t = new Date().getTime();

			var res = [];

			for(var i=1; i<15; i++){
				res.push({
					"id" : "book_" + i + "_" + t,
					"text": "Book " + i + " (8 volumes)",
					"data": {
						"parentUrl":	parentUrlIn,
						"childrenUrl":	"dataGen.volumes('dataGen.books()[" + (i-1) + "]')",
						"total": 8,
						"index": i,
						"europeana" : {
							"icon" : "TEXT"
						}
					}					
				});
			};
			console.log('Books - ' + JSON.stringify(res) + ', parent url was ' + parentUrlIn);
			return res;
		},
		
		volumes : function(parentUrlIn){
			
			var t = new Date().getTime();

			var res = [];

			for(var i=1; i<9; i++){
				res.push({
					"id" : "v_" + i + "_" + t,
					"text": "Volume " + i + " (2 chapters)",
					"data": {
						"parentUrl":	parentUrlIn,
						"childrenUrl":	"dataGen.chapters('dataGen.volumes()[" + (i-1) + "]')",
						"total": 2,
						"index": i
					}					
				});
			};
			console.log(JSON.stringify(res));
			return res;
		},
		
		chapters : function(parentUrlIn){
			
			var t = new Date().getTime();
			var res = [];
			for(var i=1; i<11; i++){
				res.push({
					"id" : "c_" + i + "_" + t,
					"text": 'Chapter ' + i + ' (10 verses)',
					"data": {
						"parentUrl":	parentUrlIn,
						"index": i,
						"childrenUrl":	"dataGen.verses('dataGen.chapters()[" + (i-1) + "]')",
						"total": 10,
					}
				 });
			}
			return res;
		},
		
		verses: function(parentUrlIn){
			var t   = new Date().getTime();
			var res = [];
			for(var i=1; i<11; i++){
				res.push({
					"id" : "vrs_" + i + "_" + t,
					"text": 'Verse ' + i,
					"data": {
						"parentUrl":	parentUrlIn,
						"index": i
					}
				 });
			}
			return res;
		}
		
	};
}();


