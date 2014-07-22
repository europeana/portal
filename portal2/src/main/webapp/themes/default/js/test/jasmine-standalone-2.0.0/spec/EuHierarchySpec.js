


describe("Hello world", function() {
	
	var hierarchy;
	var fixtures = jasmine.getFixtures();

    beforeEach(function(done) {
    
    	loadFixtures('hierarchies.html');
    	
		hierarchy = new EuHierarchy($('#hierarchy'), 8, $('.hierarchy-objects'));
		hierarchy.init('localhost:3000/0/self.json', true);
		
		// wait for it to initialise
		
		var initId = setInterval(function(){			
			if(hierarchy.getInitialised()){
				window.clearInterval(initId);
				done();			
			}
			else{
				console.log('awaiting initialiation to complete');
			}
		}, 1000)
    }, 100000);


    
    it("Has data:", function(done) {
    	
    	var data = hierarchy.getTreeData();
    	
    	console.log( JSON.stringify(data, null, 2));
    	
        expect(		data		).toEqual("Hello world!");        
        
        done();
        
    }, 100000);
    
    
});



