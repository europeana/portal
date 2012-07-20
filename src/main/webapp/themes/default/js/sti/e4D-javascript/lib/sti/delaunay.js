




function Vertex( x, y, categories ){
	this.x = x;
	this.y = y;
	this.radius;
	this.size = 0;
	this.elements = [];
	this.radii = [];
	this.weights = [];
	this.legal = true;
	if( categories != undefined ){
		for( var i=0; i<categories; i++ ){
			this.elements.push([]);
			this.weights.push(0);
		}
	}
}

Vertex.prototype.merge = function(v0,v1){
    for( var i=0; i<v0.elements.length; i++ ){
        this.elements[i] = v0.elements[i].concat( v1.elements[i] );
        this.weights[i] = v0.weights[i] + v1.weights[i];
        this.size += this.weights[i];
    }
}

Vertex.prototype.CalculateRadius = function( resolution ){
	this.radii = [];
	for( i in this.elements ){
		this.radii.push( STIStatic.getRadius(this.weights[i]) );
	}
	if( this.radii.length == 1 ){
		this.radius = this.radii[0] * resolution;
	}
	else {
		var count = 0;
		var max1 = 0;
		var max2 = 0;
		for( i in this.radii ){
			if( this.radii[i] != 0 ){
				count++;
			}
			if( this.radii[i] > max1 ){
				if( max1 > max2 ){
					max2 = max1;
				}
				max1 = this.radii[i];
			}
			else if( this.radii[i] > max2 ){
				max2 = this.radii[i];
			}
		}
		if( count == 1 ){
			this.radius = max1 * resolution;
		}
		else if( count == 2 ){
			this.radius = (max1+max2) * resolution;
		}
		else if( count == 3 ){
			var d = (2 / 3 * Math.sqrt(3) - 1)*max1;
			this.radius = (d+max1+max2) * resolution;
		}
		else if( count == 4 ){
			var d = (Math.sqrt(2) - 1)*max2;
			this.radius = (d+max1+max2) * resolution;
		}
	}
}

Vertex.prototype.addElement = function(e,weight,index){
	this.elements[index].push(e);
	this.size += weight;
	this.weights[index] += weight;
}

function Edge( v0, v1 ){
	this.v0 = v0;
	this.v1 = v1;
	this.leftFace;
	this.rightFace;
	this.legal = true;
	this.setLength();
}

Edge.prototype.setLength = function(){
	var dx = this.v0.x - this.v1.x;
	var dy = this.v0.y - this.v1.y;
	this.length = Math.sqrt( dx * dx + dy * dy );
}

Edge.prototype.contains = function(v){
    if( this.v0 == v || this.v1 == v ){
        return true;
    }
    return false;
}

Edge.prototype.replaceFace = function(f_old,f_new){
    if( this.leftFace == f_old ){
        this.leftFace = f_new;
    }
    else if( this.rightFace == f_old ){
        this.rightFace = f_new;
    }
}

Edge.prototype.setFace = function(f){
	if( f.leftOf(this) ){
		this.leftFace = f;
	}
	else {
		this.rightFace = f;
	}
}

Edge.prototype.setFaces = function(f1,f2){
	if( f1.leftOf(this) ){
		this.leftFace = f1;
		this.rightFace = f2;
	}
	else {
		this.leftFace = f2;
		this.rightFace = f1;
	}
}

Edge.prototype.removeFace = function(f){
	if( this.leftFace == f ){
		this.leftFace = null;
	}
	else {
		this.rightFace = null;
	}
}

Edge.prototype.equals = function(e){
	if( this.v0 == e.v0 && this.v1 == e.v1 || this.v0 == e.v1 && this.v1 == e.v0 ){
		return true;
	}
    return false;
}

function Triangle( edges ){
	this.edges = edges;
	this.setVertices();
	this.descendants = [];
}

Triangle.prototype.getTriple = function(e){
	var i = $.inArray(e, this.edges);
	return { e_s: this.edges[(i+1)%3], e_p: this.edges[(i+2)%3], u: this.vertices[(i+2)%3] };
}

Triangle.prototype.leftOf = function(e){
	var i = $.inArray(e, this.edges);
	if( this.vertices[i].y != this.vertices[(i+1)%3].y ){
		return this.vertices[i].y > this.vertices[(i+1)%3].y;
	}
	return this.vertices[i].y > this.vertices[(i+2)%3].y;
}

Triangle.prototype.getNext = function(v){
	//var i = this.vertices.indexOf(v);
	var i =  $.inArray(v, this.vertices);
	return this.vertices[(i+1)%3];
}

Triangle.prototype.oppositeEdge = function(v){
	//var i = this.vertices.indexOf(v);
	var i =  $.inArray(v, this.vertices);
	return this.edges[(i+1)%3];
}

Triangle.prototype.contains = function(v){
	var i =  $.inArray(v, this.vertices);
	return i != -1;
}

Triangle.prototype.replace = function(e_old,e_new){
	var i =  $.inArray(e_old, this.edges);
    //this.edges[this.edges.indexOf(e_old)] = e_new;
    this.edges[i] = e_new;
}

Triangle.prototype.setVertices = function(){
    if( this.edges[1].v0 == this.edges[0].v0 || this.edges[1].v1 == this.edges[0].v0 ){
        this.vertices = [ this.edges[0].v1, this.edges[0].v0 ];
    }
    else {
        this.vertices = [ this.edges[0].v0, this.edges[0].v1 ];
    }
    if( this.edges[2].v0 == this.vertices[0] ){
        this.vertices.push( this.edges[2].v1 );
    }
    else {
        this.vertices.push( this.edges[2].v0 );
    }
}

Triangle.prototype.replaceBy = function(triangles){
	this.descendants = triangles;
	this.edges[0].replaceFace(this,triangles[0]);
	this.edges[1].replaceFace(this,triangles[1]);
	this.edges[2].replaceFace(this,triangles[2]);
}

Triangle.prototype.CalcCircumcircle = function(){
	var v0 = this.vertices[0];
	var v1 = this.vertices[1];
	var v2 = this.vertices[2];
	var A = v1.x - v0.x; 
	var B = v1.y - v0.y; 
	var C = v2.x - v0.x;
	var D = v2.y - v0.y;
	var E = A*(v0.x + v1.x) + B*(v0.y + v1.y); 
	var F = C*(v0.x + v2.x) + D*(v0.y + v2.y);
	var G = 2.0*(A*(v2.y - v1.y)-B*(v2.x - v1.x));	
	var cx = (D*E - B*F) / G; 
	var cy = (A*F - C*E) / G;
	this.center = new Vertex( cx, cy );
	var dx = this.center.x - v0.x;
	var dy = this.center.y - v0.y;
	this.radius_squared = dx * dx + dy * dy;
};

Triangle.prototype.inCircumcircle = function(v){
	if( this.radius_squared == undefined ){
		this.CalcCircumcircle();
	}
	var dx = this.center.x - v.x;
	var dy = this.center.y - v.y;
	var dist_squared = dx * dx + dy * dy;
	return ( dist_squared <= this.radius_squared );	
};

Triangle.prototype.interior = function(v){
	var v0 = this.vertices[0];
	var v1 = this.vertices[1];
	var v2 = this.vertices[2];
	var dotAB = ( v.x - v0.x ) * ( v0.y - v1.y ) + ( v.y - v0.y ) * ( v1.x - v0.x );
	var dotBC = ( v.x - v1.x ) * ( v1.y - v2.y ) + ( v.y - v1.y ) * ( v2.x - v1.x );
	var dotCA = ( v.x - v2.x ) * ( v2.y - v0.y ) + ( v.y - v2.y ) * ( v0.x - v2.x );
	if( dotAB>0 || dotBC>0 || dotCA>0 ){
		return null;
	}
	else if( dotAB<0 && dotBC<0 && dotCA<0 ){
		return this;
	}
	else if( dotAB == 0 ){
		if( dotBC == 0 ){
			return this.vertices[1];
		}
		else if( dotCA == 0 ){
			return this.vertices[0];
		}
		return this.edges[0];
	}
	else if( dotBC == 0 ){
		if( dotCA == 0 ){
			return this.vertices[2];
		}
		return this.edges[1];
	}
	else if( dotCA == 0 ){
		return this.edges[2];
	}
};

function DynamicDelaunay( xMin, yMin, xMax, yMax ){
	this.triangles = [];
	this.newTriangles = [];
	this.bbox = { x1: xMin, y1: yMin, x2: xMax, y2: yMax };
	this.CreateBoundingTriangle(); 
	this.edges = [];
	this.vertices = [];
}

DynamicDelaunay.prototype.locate = function(v){
	if( this.boundingTriangle.descendants.length == 0 ){
		return this.boundingTriangle;
	}
	var triangles = this.boundingTriangle.descendants;
	while( true ){		
		for( var i=0;i<triangles.length; i++ ){
			var simplex = triangles[i].interior(v);
			if( simplex == null ){
				continue;
			}
			if( simplex instanceof Vertex || this.isLeaf(triangles[i]) ){
				return simplex;
			}
			triangles = triangles[i].descendants;
			break;
		}
	}
}

DynamicDelaunay.prototype.legalize = function(v,e,t0_old){
	if( !e.v0.legal && !e.v1.legal ){
		return;
	}
	var flip = false;
	var t1_old, tr1;
	if( e.leftFace == t0_old && e.rightFace.inCircumcircle(v) ){
		flip = true;
		t1_old = e.rightFace;
	}
	else if( e.rightFace == t0_old && e.leftFace.inCircumcircle(v) ){
		flip = true;
		t1_old = e.leftFace;
	}
	if( flip ){
		var tr0 = t0_old.getTriple(e);
		var tr1 = t1_old.getTriple(e);
		var e_flip = new Edge( tr0.u, tr1.u );
		var poly = [];
		poly.push(e.v0);
		poly.push(e_flip.v0);
		poly.push(e.v1);
		poly.push(e_flip.v1);
		if( !this.JordanTest(poly,e_flip) ){
			return;
		}
		e.legal = false;
		this.edges.push(e_flip);
		var t0_new = new Triangle( [ e_flip, tr0.e_p, tr1.e_s ] );
		var t1_new = new Triangle( [ e_flip, tr1.e_p, tr0.e_s ] );		
		e_flip.setFaces(t0_new,t1_new);
		tr0.e_p.replaceFace(t0_old,t0_new);
		tr1.e_s.replaceFace(t1_old,t0_new);
		tr1.e_p.replaceFace(t1_old,t1_new);
		tr0.e_s.replaceFace(t0_old,t1_new);
		t0_old.descendants = [t0_new,t1_new];
		t1_old.descendants = [t0_new,t1_new];
		this.legalize(v,t0_new.edges[2],t0_new);
		this.legalize(v,t1_new.edges[1],t1_new);
	}
}

DynamicDelaunay.prototype.add = function(v){
	this.addVertex(v,this.locate(v));
}

DynamicDelaunay.prototype.addVertex = function(v,simplex){
	if( simplex instanceof Vertex ){
        simplex.merge(simplex,v);
	}
	else if( simplex instanceof Edge ){
		this.vertices.push(v);
        simplex.legal = false;
		var tr0 = simplex.leftFace.getTriple(simplex);
		var tr1 = simplex.rightFace.getTriple(simplex);
		var e0 = new Edge( v, tr0.u );
		var e1 = new Edge( v, simplex.leftFace.getNext(tr0.u) );
		var e2 = new Edge( v, tr1.u );
		var e3 = new Edge( v, simplex.rightFace.getNext(tr1.u) );
		var t0 = new Triangle( [ e0, tr0.e_p, e1 ] );
		var t1 = new Triangle( [ e1, tr1.e_s, e2 ] );
		var t2 = new Triangle( [ e2, tr1.e_p, e3 ] );
		var t3 = new Triangle( [ e3, tr0.e_s, e0 ] );
		simplex.leftFace.descendants = [t0,t3];
		simplex.rightFace.descendants = [t1,t2];
		this.edges.push(e0);
		this.edges.push(e1);
		this.edges.push(e2);
		this.edges.push(e3);
		e0.setFaces(t0,t3);
		e1.setFaces(t0,t1);
		e2.setFaces(t1,t2);
		e3.setFaces(t2,t3);
		tr0.e_p.replaceFace(simplex.leftFace,t0);
		tr1.e_s.replaceFace(simplex.rightFace,t1);
		tr1.e_p.replaceFace(simplex.rightFace,t2);
		tr0.e_s.replaceFace(simplex.leftFace,t3);
		this.legalize(v,tr0.e_p,t0);
		this.legalize(v,tr1.e_s,t1);
		this.legalize(v,tr1.e_p,t2);
		this.legalize(v,tr0.e_s,t3);
	}
	else {
		this.vertices.push(v);
		var e_i = new Edge(simplex.vertices[0],v);
		var e_j = new Edge(simplex.vertices[1],v);
		var e_k = new Edge(simplex.vertices[2],v);
		this.edges.push(e_i);
		this.edges.push(e_j);
		this.edges.push(e_k);
		var t0 = new Triangle([e_i,simplex.edges[0],e_j]);
		var t1 = new Triangle([e_j,simplex.edges[1],e_k]);
		var t2 = new Triangle([e_k,simplex.edges[2],e_i]);
		e_i.setFaces(t0,t2);
		e_j.setFaces(t0,t1);
		e_k.setFaces(t1,t2);
		simplex.replaceBy([t0,t1,t2]);
		this.legalize(v,simplex.edges[0],t0);
		this.legalize(v,simplex.edges[1],t1);
		this.legalize(v,simplex.edges[2],t2);
	}
}

DynamicDelaunay.prototype.isLeaf = function(t){
	return t.descendants.length == 0;
}

DynamicDelaunay.prototype.CreateBoundingTriangle = function(){
	var dx = ( this.bbox.x2 - this.bbox.x1 ) * 10;
	var dy = ( this.bbox.y2 - this.bbox.y1 ) * 10;
	
	var v0 = new Vertex( this.bbox.x1 - dx,   this.bbox.y1 - dy*3 );
	var v1 = new Vertex( this.bbox.x2 + dx*3, this.bbox.y2 + dy   );
	var v2 = new Vertex( this.bbox.x1 - dx,   this.bbox.y2 + dy   );
	
	var e0 = new Edge( v1, v0 );
	var e1 = new Edge( v0, v2 );
	var e2 = new Edge( v2, v1 );

	v0.legal = false;
	v1.legal = false;
	v2.legal = false;
	this.boundingTriangle = new Triangle( [ e0, e1, e2 ] );
	
    var inf = new Triangle( [ e0, e1, e2 ] );
	
	e0.setFaces(this.boundingTriangle,inf);
	e1.setFaces(this.boundingTriangle,inf);
	e2.setFaces(this.boundingTriangle,inf);
}
    
DynamicDelaunay.prototype.DelaunayEdgeCollapse = function(e)
{

	   var s0 = e.v0.size;
	   var s1 = e.v1.size;
	   var x = e.v0.x * s0 / ( s0 + s1 ) + e.v1.x * s1 / ( s0 + s1 );
	   var y = e.v0.y * s0 / ( s0 + s1 ) + e.v1.y * s1 / ( s0 + s1 );
	   var v = new Vertex(x,y,e.v0.elements.length);
	   v.merge( e.v0, e.v1 );

	   e.v0.legal = false;
	   e.v1.legal = false;   

	   var hole = [];
	   var oldFacets = [];
	   e.legal = false;

	    var vertices = [];
	   var traverse = function(eLeft, eRight, triangle)
	   {
	      eLeft.legal = false;
	      do
	      {
	         var triple;
	         if( eLeft.leftFace == triangle )
	         {
	            triple = eLeft.rightFace.getTriple(eLeft);
	            oldFacets.push(eLeft.rightFace);
	            triple.e_s.removeFace(eLeft.rightFace);
	            triangle = eLeft.rightFace;
	         }
	         else
	         {
	            triple = eLeft.leftFace.getTriple(eLeft);
	            oldFacets.push(eLeft.leftFace);
	            triple.e_s.removeFace(eLeft.leftFace);
	            triangle = eLeft.leftFace;
	         }
	         
	     	 var i = $.inArray(triple.e_s, hole);
	         //if( hole.indexOf(triple.e_s) == - 1 )
        	 if( i == - 1 )
	         {
	            hole.push(triple.e_s);
	         }
	         vertices.push(triple.u);
	         eLeft = triple.e_p;
	         eLeft.legal = false;
	      }
	      while( eLeft != eRight );
	   }
	   var tr0 = e.leftFace.getTriple(e);
	   var tr1 = e.rightFace.getTriple(e);
		oldFacets.push(e.leftFace);
		oldFacets.push(e.rightFace);
	   traverse(tr0.e_p, tr1.e_s, e.leftFace);
	   traverse(tr1.e_p, tr0.e_s, e.rightFace);

	   var hd = new DynamicDelaunay( this.bbox.x1 - 10, this.bbox.y1 - 10, this.bbox.x2 + 10, this.bbox.y2 + 10 );
	    var hull = [];
	    for( i in hole ){
	        if( !(hole[i].leftFace == null && hole[i].rightFace == null) ){
	            hull.push(hole[i].v0);
	            hull.push(hole[i].v1);
	        }
	    }
	    var hullVertices = [];
	    var distinct = [];
	    for(var i = 0; i< vertices.length;  i++){
	    		
	    		
	    		//if( distinct.indexOf(vertices[i]) == -1 ){
    			if( $.inArray(vertices[i], distinct) == -1 ){
		            hd.add(vertices[i]);
	    			distinct.push(vertices[i]);
	    		}
	            //if( hull.indexOf(vertices[i]) != -1 ){
            	if( $.inArray(vertices[i], hull) != -1 ){
	                hullVertices.push(vertices[i]);
	            }
	    }

	   var newFacets = [];
	   var isBoundary = function(e)
	   {
	      for( var i = 0; i < hole.length; i ++ )
	      {
	         if( hole[i].equals(e) )
	         {
	            return i;
	         }
	      }
	      return - 1;
	   }

		var holeEdges = new Array(hole.length);
		var nonHoleEdges = [];

	   for( var i = 0; i < hd.edges.length; i++ ){
	      	var e = hd.edges[i];
	      	var b = isBoundary(e);
	      	if( b != - 1 ){
	      		if( !e.legal ){
				var t1 = e.leftFace.getTriple(e);
				var t2 = e.rightFace.getTriple(e);
				var edge = new Edge(t1.u,t2.u);
				for( var j = 0; j < hd.edges.length; j++ ){
					if( hd.edges[j].equals(edge) && hd.edges[j].legal ){
						hd.edges[j].legal = false;
						break;
					}
				}
				t1.e_p.setFace(e.leftFace);
				t1.e_s.setFace(e.leftFace);
				t2.e_p.setFace(e.rightFace);
				t2.e_s.setFace(e.rightFace);
				e.legal = true;
	      		}
			holeEdges[b] = e;
		}
	       	else {
			nonHoleEdges.push(e);
		}
	   }

	   for( var i = 0; i < holeEdges.length; i++ ){
		var e = holeEdges[i];
		if( hole[i].leftFace == null ){
	            	hole[i].leftFace = e.leftFace;
	            	hole[i].leftFace.replace(e, hole[i]);
	            	
		       	    //if( newFacets.indexOf(hole[i].leftFace) == - 1 )
	       	    	if( $.inArray(hole[i].leftFace, newFacets) == - 1 )
		       	    {
					     newFacets.push(hole[i].leftFace);
		            }
		}
	        if( hole[i].rightFace == null ){
	            	hole[i].rightFace = e.rightFace;
	            	hole[i].rightFace.replace(e, hole[i]);
		       	    //if( newFacets.indexOf(hole[i].rightFace) == - 1 ){
	       	    	if( $.inArray(hole[i].rightFace, newFacets) == - 1 ){
					     newFacets.push(hole[i].rightFace);
		            }
		}
	   }

	   for( var i = 0; i < nonHoleEdges.length; i++ ){
	      	var e = nonHoleEdges[i];
	      	if( !e.legal ){
	         	continue;
	      	}
	        if( this.JordanTest( hullVertices, e ) ){
		      	this.edges.push(e);
	       	    //if( newFacets.indexOf(e.rightFace) == - 1 ){
       	    	if( $.inArray(e.rightFace, newFacets) == - 1 ){
				     newFacets.push(e.rightFace);
	            }
	            //if( newFacets.indexOf(e.leftFace) == - 1 ){
            	if( $.inArray(e.leftFace, newFacets) == - 1 ){
	                newFacets.push(e.leftFace);
	            }
	        }
	   }

	   for( i in oldFacets ){
	        oldFacets[i].descendants = newFacets;
	   }

		for( var i=0;i<newFacets.length; i++ ){
			var simplex = newFacets[i].interior(v);
			if( simplex == null ){
				continue;
			}
			else {
				this.addVertex(v,simplex);
				break;
			}
		}

		return v;	
	
}

DynamicDelaunay.prototype.JordanTest = function( pol, e ){
	var p = new Vertex( (e.v0.x+e.v1.x)/2, (e.v0.y+e.v1.y)/2 );
	var inside = false;
	var i, j = pol.length-1;
	for (i = 0; i < pol.length; j = i++) {
		var p1 = pol[i];
		var p2 = pol[j];
		if ((((p1.y <= p.y) && (p.y < p2.y)) || ((p2.y <= p.y) && (p.y < p1.y))) && (p.x < (p2.x - p1.x) * (p.y - p1.y) / (p2.y - p1.y) + p1.x))
          		inside = !inside;
	}
	return inside;
}

DynamicDelaunay.prototype.insertByWeight = function(e){
	if( this.deleteEdges.length == 0 ){
		this.deleteEdges.push(e);
	}
else {
    var mid;
    var min = 0;
    var max = this.deleteEdges.length-1;
    do {
        mid = min + Math.floor( (max-min)/2 );
        if( e.weight > this.deleteEdges[mid].weight ){
            max = mid - 1;
        }
        else {
            min = mid + 1;
        }
        if( min > max ){
        	break;
        }
    }
    while( true );
    if( this.deleteEdges[mid].weight > e.weight ){
    	this.deleteEdges.splice(mid+1,0,e);
    }
   	this.deleteEdges.splice(mid,0,e);
}
}

DynamicDelaunay.prototype.mergeForResolution = function( resolution ){
	this.deleteEdges = [];
    this.weightEdges( resolution );
    while( this.deleteEdges.length > 0 ){
    	var e = this.deleteEdges[0];
    	if( e.legal ){
    		var l = this.edges.length;
            var newVertex = this.DelaunayEdgeCollapse(e);
            newVertex.CalculateRadius(resolution);
            var lNew = this.edges.length;
            var newEdges = this.edges.slice(l);
        	this.deleteEdges.splice(0,1);
            for( i in newEdges ){
            	var eNew = newEdges[i];
            	if( eNew.legal ){
                	eNew.weight = ( eNew.v0.radius + eNew.v1.radius + STIStatic.circleGap*resolution ) / eNew.length;
                	if( eNew.weight > 1 ){
                		this.insertByWeight(eNew);
                	}
            	}
            }
    	}
    	else {
        	this.deleteEdges.splice(0,1);    		
    	}
    }
}

DynamicDelaunay.prototype.weightEdges = function( resolution ){
	for( var i=0; i<this.vertices.length; i++ ){
		this.vertices[i].CalculateRadius(resolution);
	}
	for( var i=this.edges.length-1; i>-1; i-- ){
		var e = this.edges[i];
		if( e.legal ){
			if( !e.v0.legal || !e.v1.legal ){
				e.weight = 0;			
			}
			else {
				e.weight = ( e.v0.radius + e.v1.radius + STIStatic.circleGap*resolution ) / e.length;
				if( e.weight > 1 ){
					this.deleteEdges.push(e);
				}
			}
		}
		else {
			this.edges.splice(i,1);
		}
	}
	var sortByEdgeWeight = function( e1, e2 ){
		if ( e1.weight > e2.weight ){
			return -1;
		}
		if ( e1.weight < e2.weight ){
			return 1;
		}
		return 0;
	}
	this.deleteEdges.sort(sortByEdgeWeight);
}

DynamicDelaunay.prototype.ValidityTest = function(){
console.info("Test 1: Valid Delaunay ...");
/*
	var leafs = [];
	var triangles = this.boundingTriangle.descendants;
	var j = 0;
	while( triangles.length > j ){		
		var t = triangles[j];
		if( t.taken == undefined ){
			t.taken = true;
		if( this.isLeaf(t) ){
			leafs.push(t);
		}
		else {
			triangles = triangles.concat(t.descendants);
		}
		}
		j++;
	}
console.info("  Number of Triangles: "+leafs.length);

var c = 0;
for( i in this.edges ){
    if( this.edges[i].legal ){
        c++;
    }
}
console.info("  Number of Edges: "+c);*/
/*

for( var i=0; i<leafs.length; i++ ){
	for( var j=0; j<vertices.length; j++ ){
		if( !leafs[i].contains(vertices[j]) && leafs[i].inCircumcircle(vertices[j]) ){
			console.info(leafs[i],vertices[j]);

		}
	}
}
*/

//console.info("Test 2: Edges Facets (null) ...");
//for( i in this.edges ){
for(var i=0; i<this.edges.length; i++){
	var e = this.edges[i];
	if( e.leftFace == null || e.rightFace == null ){
		console.info(e);
		alert();
	}
}

//console.info("Test 3: Edges Facets ...");
var leftOf = function(v1,v2,v)
    {
        var x2 = v1.x - v2.x;
        var x3 = v1.x - v.x;
        var y2 = v1.y - v2.y;
        var y3 = v1.y - v.y;
        if(x2 * y3 - y2 * x3 < 0)
        {
            return true;
        }
	return false;
    } 
var c = 0;
//for( i in this.edges ){
for(var i=0; i<this.edges.length; i++){
	
	var e = this.edges[i];
	var t1 = e.leftFace.getTriple(e);
	var t2 = e.rightFace.getTriple(e);	
	if( e.v0.y == e.v1.y ){
		if( t1.u.y > t2.u.y ){
			console.info("equal y conflict ...");
			console.info(e);
		alert();
			c++;
		}
	}
	else {
		var v1, v2;
		if( e.v0.y > e.v1.y ){
			v1 = e.v0;
			v2 = e.v1;
		}
		else {
			v1 = e.v1;
			v2 = e.v0;
		}
		if( !leftOf(v1,v2,t1.u) ){
			console.info("left right conflict ... left is right");
			console.info(e);
		alert();
			c++;
		}
		if( leftOf(v1,v2,t2.u) ){
			console.info("left right conflict ... right is left");
			console.info(e);
		alert();
			c++;
		}
	}
}
//console.info("Number of Edges: "+this.edges.length);
//console.info("Number of Conflicts: "+c);

   //for( i in this.edges ){
   for(var i=0; i<this.edges.length; i++ ){
        if( this.edges[i].legal ){
            var e = this.edges[i];
           var tr0 = e.leftFace.getTriple(e);
            var tr1 = e.rightFace.getTriple(e);
            if( !tr0.e_p.legal || !tr0.e_s.legal || !tr1.e_p.legal || !tr1.e_s.legal ){
                console.info(e);
                    console.info("conflict in edge continuity");                
                return;
            }
        }
   }

}
