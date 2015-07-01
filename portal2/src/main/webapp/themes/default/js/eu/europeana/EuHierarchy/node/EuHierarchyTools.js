var EuHierarchyTools = function(hierarchy) {

    var self            = this; 
    self.hierarchy      = hierarchy;
    self.container      = hierarchy.getContainer();

    // debug vars
    var locked = true;
    var spin   = false;

    $('.load-more').click(function(){ alert('no handler'); });
    $('.view-next').click(function(){ alert('no handler'); });
    
    $('.lock').click(function() {
        self.hierarchy.setLocked(!self.hierarchy.getLocked());
        
        if (self.hierarchy.getLocked()) {
            $(this).html('[unlock]');
            self.container.css('overflow', 'hidden');
        } else {
            $(this).html('[lock]');
            self.container.css('overflow', 'auto');
        }
    });
    
    $('.scroll-top').click(function() {
        var val = $('#chunk').val();
        console.log('call scroll top with ' + val);
        self.hierarchy.scrollTop( val )
        //$('.jstree-disabled').each(function(){
        //  var node = self.treeCmp.jstree( 'get_node', $(this).closest('.jstree-node') );          
        //  self.treeCmp.jstree("enable_node", node );
        //});       
    });
    
    $('#chunk').keypress(function(e){
        if(e.which==13){
            $('.scroll-top').click();       
        }
    });
    
        
    $('.tba').click(function() {
        self.hierarchy.brokenArrows();
    });

    $('.visible-nodes').click(function(){
        
        console.log('get limits...');
        
        var limits = self.hierarchy.getVisibleNodes();
        
        console.log(limits[0].id + ' <--> ' + limits[1].id);
        
        $('#' +  limits[0].id).find('>a').css('background-color', 'red');
        $('#' +  limits[0].id).find('>a').css('color',            'pink');
        $('#' +  limits[1].id).find('>a').css('background-color', 'blue');          
        
        setTimeout(function(){
            $('#' +  limits[0].id).find('>a').css('background-color', 'white');
            $('#' +  limits[0].id).find('>a').css('color',            'black');
            $('#' +  limits[1].id).find('>a').css('background-color', 'white');
        }, 5000);
    });


    $('.expand').click(function(){
        self.hierarchy.expandAll();
    });

    $('.collapse').click(function(){
        self.hierarchy.collapseAll();
    });
    
    $('.delay').click(function(){
        self.hierarchy.setDefaultDelay(10000);
    });
    
    $('.t-start').click(function(){
        self.hierarchy.startTimer();
    });

    $('.t-stop').click(function(){
        self.hierarchy.stopTimer();
    });

    $('.tr-ul').click(function(){
        self.hierarchy.addTransitionClasses();
    });
    
    $('.off-bel').click(function(){
        var node = self.hierarchy.getVisibleNodes()[1];
        
        /*
        var next = $('#' + bottom.id).next();
        
        if(next.length==0){
            
            var closestLi = $('#' + bottom.id).parent().closest('li');
            console.log('no length - closest li =   ' + closestLi + '   ' + closestLi.length );

            if(closestLi.length > 0){
                console.log('parent li = ' + closestLi.attr('id'))
            }
            next = closestLi.next();
        }
        console.log('bottom.id ' + bottom.id + ', next =  ' + next.attr('id') );
        */
        
        var next = $('#' + node.id).next();        
        while(next.length==0){
            
            var closestLi = $('#' + node.id).parent().closest('li');

            if(closestLi.length == 0){
                break;
            }
            node = closestLi;
            next = closestLi.next();
        }
        console.log('next =  ' + next.attr('id') );
        
        
    });
    
    $('.gcp').click(function(){ 
        
        var visible = self.hierarchy.getVisibleNodes();
        
        var cp = self.hierarchy.getCommonParent(visible[0], visible[1]);

        if(cp){
            $('#' + cp.id).css('background-color', 'purple');       
            $('.debug-area').html( 'common parent is ' + cp.text );
        }
        else{
            console.log('Error: no common parent found for ' + visible[0].id + ' and ' +  visible[1].id);
        }       
    });
    
    $('.gtd').click(function(){
        alert(JSON.stringify(self.hierarchy.getTreeData(), null, 2));
    });
    
};


