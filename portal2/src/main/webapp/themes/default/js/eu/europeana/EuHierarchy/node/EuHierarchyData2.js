var dataGen = function(){

    var data = {"data":[]};
    
    var log = function(msg){
        console.log(msg);
    }
    
    var wrapTitle = function(title){
        return  {
                "def": [ title ]
        }
    }


    /*
    BOOKS
        VOLUMES
            CHAPTERS
                VERSES
    */          

    for(var i=1; i<=15; i++){
        
        data.data[data.data.length] = { "id" : i+"", "index":i, "title" : wrapTitle("Book " + i), "type" : "text", "data" : [] };
            
        for(var j=1; j<=15; j++){

            var book = data.data[data.data.length-1];
            book.data[book.data.length] = { "id" : i+"-"+j, "index":j, "title" : wrapTitle("Volume " + j + ' (b' + i + ')'), "type" : "text", "data" : [] };
            
            for(var k=1; k<=14; k++){
                
                var volume = book.data[book.data.length-1];
                volume.data[volume.data.length] = { "id" : i+"-"+j+"-"+k, "index":k, "title" : wrapTitle("Chapter " + k + ' (b' + i + ', v' + j + ')'), "type" : "text", "data" : [] };

                for(var l=1; l<=14; l++){
                    
                    var chapter = volume.data[volume.data.length-1];
                    chapter.data[chapter.data.length] = { "id" : i+"-"+j+"-"+k+"-"+l, "index": l,  "title" : wrapTitle("Verse " + l + ' (b' + i + ', v' + j + ', c' + k + ')'), "type" : "text", "data" : []  };

                    
                       for(var m=1; m<=14; m++){
                            var verse = chapter.data[chapter.data.length-1];
                            verse.data[verse.data.length] = { "id" : i+"-"+j+"-"+k+"-"+l+"-"+m, "index": m,  "title" : wrapTitle("Word " + l + ' (b' + i + ', v' + j + ', c' + k + ' (' + m + '))'), "type" : "text" };
                        }

                }
            }
        }
    }

    // utility for result write
    var parentData = function(path, limit){
        return {
            "id": path.slice(0, path.length-1).join('-'),
            "title": search(path.slice(0, path.length-1).join('-'), 'self.json', limit).self.title,
            "type": "TEXT",
            "index": path[path.length-2],
            "hasChildren": true,
            "relBefore": path[path.length-2]<11
        }
    }

    // utility for result write
    var coreData = function(data, suppressChildData){
        if(!data){
            return;
        }
        var res = {
            "id" :              data.id,
            "title" :           data.title,
            "type":             data.type,
            "index":            data.index,
            "hasChildren":      suppressChildData ? null : typeof data.data==='object',
            "relBefore":        data.index<11
        }
        if(res.hasChildren && !suppressChildData){
            res.childrenCount = data.data.length;
        }
        return res;
    }

    
        
    // search
    var search = function(id, action, limit){
        
        log('search on id: ' + id + ' with action: ' + action + ' and limit: ' + limit + ' typeof ' + typeof limit);
        
        var res   = { "action" : action };
        var path  = id.split('-');
        var sData = data;

        if(!sData){
            return false;
        }
        
        for(var i=0; i<path.length; i++){
            log('parseInt(path[i]) = ' + parseInt(path[i]) + '    --> ' + sData.data.length );
            sData = sData.data[parseInt(path[i])-1];    // change for indexing from 1
        }

        /*
            SELF
        */
        if(action==="self.json"){
            res.self    =  coreData(sData);
            
            if(res.self){
                if(path.length>1){
                    res.self.parent = parentData(path, limit).id;
                }
            }
        }
        
        /*
            CHILDREN
        */

        else if(action==="children.json"){
            
            res.self = { id:id  };
            
            if(path.length>1){
                res.self.parent  = parentData(path, limit).id;
            }
            
            if(sData.data){
                res.children = [];          
                
                var loop = typeof limit == 'undefined' ? sData.data.length : Math.min(limit, sData.data.length);
                
                for(var i=0; i<loop; i++){
                    
                    //for(var i=0; i< Math.min(limit, sData.data.length); i++){
                    res.children.push(coreData(sData.data[i], true))
                }
//              res.childrenCount = sData.data.length;      // redundant?       
            }
            else{
//              res.childrenCount = 0;      // redundant?
            }
        }
        
        /*
            FOLLOWING
        */

        else if(action==="following-siblings.json"){
            
            res.self = { id:id };
            
            if(path.length>1){

                res.self.parent          = parentData(path, limit).id;
                
                var parentPath      = path.slice(0, path.length-1).join('-');
                var parentChildren  = search(path.slice(0, path.length-1).join('-'), 'children.json');                  
                var start           = false;
                var added           = 0;
                
                if(parentChildren.children.length){
                    if(parentChildren.children.length>1){
                        res['following-siblings'] = [];
                    }
                }           
                for(var i=0; i<parentChildren.children.length; i++){
                    if(start){
                        var followingSibling         = coreData(parentChildren.children[i]);
                        var siblingData              = search(followingSibling.id, 'self.json', 1);
                        
                        followingSibling.hasChildren = siblingData.self.hasChildren;
                        
                        if(followingSibling.hasChildren){
                            followingSibling.childrenCount = siblingData.self.childrenCount;        
                        }
                        
                        res['following-siblings'].push(followingSibling);
                        
                        added ++;
                        if(added==limit){
                            break;
                        }
                    }
                    if(!start && parentChildren.children[i].id == id){
                        start = true;
                    }
                }           
            }
            if(sData.data){
//              res.childrenCount = sData.data.length;      // redundant?                   
            }
            
        }
        
        /*
            PRECEDING
        */

        else if(action==="preceding-siblings.json"){

            res.self = { id:id };
            
            if(path.length>1){
                
                res.self.parent          = parentData(path, limit).id;
                
                var parentChildren  = search(path.slice(0, path.length-1).join('-'), 'children.json');
                var start           = false;
                var added           = 0;
                
                if(parentChildren.children.length>1){
                    res['preceding-siblings'] = [];
                }           
                for(var i=parentChildren.children.length-1; i>=0; i--){
                    
                    if(start){
                        var precedingSibling         = coreData(parentChildren.children[i]);
                        var siblingData              = search(precedingSibling.id, 'self.json', 1);

                        //var subSrc = search(parentChildren.children[i].id, 'self.json', 1);
                        
                        precedingSibling.hasChildren = siblingData.self.hasChildren;
                        if(precedingSibling.hasChildren){
                            precedingSibling.childrenCount = siblingData.self.childrenCount;                            
                        }
                        
                        res['preceding-siblings'].push(precedingSibling);

                        added ++;
                        if(added==limit){
                            break;
                        }
                    }
                    if(!start && parentChildren.children[i].id == id){
                        start = true;
                    }
                }
            }
        }
        else if(action==="ancestor-self-siblings.json"){
            
            res.self    =  coreData(sData);

            var preceding = search(path.join('-'), 'preceding-siblings.json', 8);
            res['preceding-siblings'] = preceding['preceding-siblings'];
            var following = search(path.join('-'), 'following-siblings.json', 8);
            res['following-siblings']  = following['following-siblings'];

            console.log('typeof preceding ' + typeof preceding['preceding-siblings']);

            console.log('preceding ' + (typeof preceding['preceding-siblings'] == 'undefined' ? 'null' : preceding['preceding-siblings'].length ) );
            console.log('following ' + (typeof following['following-siblings'] == 'undefined' ? 'null' : following['following-siblings'].length ) );
            
            if(path.length>1){
                res.self.parent = parentData(path, limit).id;
            }
            
            
            path.pop();
            if(path.length){                
                res.ancestors = [];
                while(path.length){
                    res.ancestors.push(
                        search(path.join('-'), 'self.json', 1)
                    );
                    path.pop();
                }
            }
        }
        
        
        //return res.reverse();
        return res;
    }
        
    return {
        "search":search
    }
        
}();


if(typeof (module) != 'undefined' && module.exports){
    module.exports = exports = dataGen; 
}

