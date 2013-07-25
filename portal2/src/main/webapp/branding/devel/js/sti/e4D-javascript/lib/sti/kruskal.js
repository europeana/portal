var kruskal_infinity = 100000000000; // !!! beware, not allowable for many applications

function Edge2(a, b, w) { this.v1=a; this.v2=b; this.wt=w; }

function setEdgeAdjMatrix(i, j, w)
 { if(j > i) { var t=i; i=j; j=t; }//swap so that i >= j
   this.matrix[i][j] = w;
 }


function randomAdjMatrix(pr) // allocate random edges with probability 'pr'; LA
 { var i, j;
   for(i=0; i < this.Vertices; i++)
    { for(j=0; j < i; j++)
       { if(Math.random() <= pr)//LAllison, roll dice
            this.setEdge(i, j, 1+Math.round(Math.random()*8));  // wt in [1..9]
 }  }  }//randomAdjMatrix


function edgeWeightAdjMatrix(i,j)
 { return i >= j ? this.matrix[i][j] : this.matrix[j][i]; }

function adjacentAdjMatrix(i, j)
 { return this.edgeWeight(i,j) < kruskal_infinity; }


function AdjMatrix(Vrtcs)  // constructor for a Graph as an Adjacency Matrix
 { this.Vertices   = Vrtcs;                    // Vertices [0..Vrtcs-1]
   this.edgeWeight = edgeWeightAdjMatrix;      // These functions
   this.adjacent   = adjacentAdjMatrix;        // and values
   this.random     = randomAdjMatrix;
   this.setEdge    = setEdgeAdjMatrix;

   this.matrix = new Array(Vrtcs);             // the Graph representation...
   var i, j;
   for(i=0; i < Vrtcs; i++)                    // undirected graph so...
    { this.matrix[i] = new Array(i+1);         // symmetric => triangular !!!
      for(j=0; j <= i; j++)
         this.setEdge(i, j, kruskal_infinity); // no edges, yet
    }
 }//AdjMatrix

//-----------------------------------------------------------------------------

function AdjListCell(vert, w, next)
 { this.v = vert; this.wt = w; this.tl = next; }

function AdjListFromAdjMatrix(GbyMatrix)
 { var v1, v2;
   this.Vertices = GbyMatrix.Vertices;

   for(v1=0; v1 < this.Vertices; v1++)
    { this.vertex[v1] = null;  // clear

      for(v2=this.Vertices-1; v2 >= v1; v2--)  // v1 <= v2
       { if(GbyMatrix.adjacent(v1, v2))
          { var wt = GbyMatrix.edgeWeight(v1, v2);
            this.vertex[v1] = new AdjListCell(v2, wt, this.vertex[v1]);
          }
       }
    }
 }//AdjListFromAdjMatrix

function AdjList(Vrtcs) // Constructor for a Graph by Adjacency Lists         C
 { this.Vertices      = Vrtcs;                                             // o
   this.fromAdjMatrix = AdjListFromAdjMatrix;                              // m
                                                                           // p
   this.vertex   = new Array(Vrtcs);  // set up the adjacency lists           .
   var i;                                                                  // S
   for(i=0; i < Vrtcs; i++) this.vertex[i]=null;                           // c
 }//AdjList                                                                   i

//-----------------------------------------------------------------------------

function Prim(G)
// Post: T[] is a minimum spanning tree of G and
//       T[i-1] is the edge linking Vertex i into the tree
// Time complexity is O(|V|**2)
 { var T = new Array(G.Vertices-1);
   var i;
   var done = new Array(G.Vertices);

   done[0] = true;                               // initially T=<{0},{ }>
   for(i = 1; i < G.Vertices; i++)
    { T[i-1] = new Edge2(0, i, G.edgeWeight(0,i));
      done[i]=false;
    }

   var dontCare;
   for(dontCare = 1; dontCare < G.Vertices; dontCare++)// |V|-1 times...      L
   // Invariant: T is a min' spanning sub-Tree of vertices in done            A
    { // find the undone vertex that is closest to the Spanning (sub-)Tree    l
      var minDist = kruskal_infinity, closest = -1;                        // l
      for(i = 1; i < G.Vertices; i++)                                      // i
         if(! done[i] && T[i-1].wt <= minDist)                             // s
          { minDist = T[i-1].wt; closest = i; }                            // o
      done[closest] = true;                                                // n

      for(i=1; i < G.Vertices; i++) // recompute undone proximities to T
         if(! done[i])
          { var Gci = G.edgeWeight(closest, i);
            if(Gci < T[i-1].wt)
             { T[i-1].wt = Gci;
               T[i-1].v1 = closest;
    }     }  }

   return T;
 }//Prim

// ----------------------------------------------------------------------------

function PartitionMerge(s1, s2)
// merge the smaller of subsets s1 and s2 into the larger of them.            M
 { var drop, keep;                                                         // o
   if(this.subset[s1].nElts < this.subset[s2].nElts) // drop the smaller      n
    { drop=s1; keep=s2; }                                                  // a
   else                                                                    // s
    { drop=s2; keep=s1; }                                                  // h

   var dropElt = this.subset[drop].firstElt; // each element in smaller subset
   var done=false;
   while(! done)
    { this.elt[dropElt].setNumber = keep; // move the element to larger subset
      if( this.elt[dropElt].nextElt < 0)
         done = true;
      else
         dropElt = this.elt[dropElt].nextElt;
    }//end while

   this.elt[dropElt].nextElt  = this.subset[keep].firstElt;      // join lists
   this.subset[keep].firstElt = this.subset[drop].firstElt;
   this.subset[keep].nElts   += this.subset[drop].nElts;

   this.subset[drop].firstElt = -1;  // clear smaller subset
   this.subset[drop].nElts = 0;

   this.size --;                           // one less subset in the partition
 }//PartitionMerge


function PartitionEltCell(sn, nxt) { this.setNumber = sn; this.nextElt = nxt; }

function PartitionSetCell(ne, fst) { this.nElts = ne; this.firstElt = fst; }


function PartitionSingletons() // make {{0}, {1}, {2}, ...}
 { var i;
   this.size = (this.elt).length; // # of subsets in the partition
   for(i=0; i < this.size; i++)
    { this.elt[i] = new PartitionEltCell(i, -1);
      this.subset[i] = new PartitionSetCell(1, i); // i.e. i is in {i}
 }  }


function Partition(N)                                                      // A
 { this.elt    = new Array(N); // elements -> subsets                         l
   this.subset = new Array(N); // subsets -> elements                         l
                                                                           // i
   this.singletons = PartitionSingletons;                                  // s
   this.merge      = PartitionMerge;                                       // o
 }//Partition constructor                                                     n


function upHeap(e) // see PriorityQ                                           L
// add e to the (smallest on top) heap, arr[1..size]                          A
//  PRE: arr[1..size] is a (smallest at top) heap / priority Q                l
// POST: arr[1..size] is a (bigger) heap, with e added                        l
 { this.size++;                                                            // i
   var child = this.size,  parent;                                         // s
   while(child > 1)//i.e. not top of heap                                  // o
    { parent = Math.floor(child / 2);                                      // n

      if(this.arr[parent].wt > e.wt)
         this.arr[child] = this.arr[parent];   // move parent down
      else
         break;                                // found a place for e
      child = parent;
    }

   // either child==1, i.e. parentless, or parent is less than or eq to e
   this.arr[child] = e;
 }//upHeap, L.Allison, Comp Sci and Software Engineering, Monash University


function downHeap(e)
// add e to the (smallest on top) heap, arr[2..size]
//  PRE: arr[2..size] is a heap
// POST: arr[1..size] is a heap
 { var parent = 1,  child;                                                 // A
   while(2*parent <= this.size)                                            // U
    { child = 2*parent; // left child is 2*parent, right is 2*parent+1     // S
                                                                           // T
      if(child < this.size && this.arr[child+1].wt < this.arr[child].wt)   // R
         child++; // right child is smaller then left child                // A
                                                                           // L
      if(this.arr[child].wt < e.wt)                                        // I
         this.arr[parent] = this.arr[child];   // move the child up        // I
      else                                                                 // A
         break;                                // found a place for e
      parent = child;
    }

   // either parent childless or child(ren) of parent are greater or eq to e
   this.arr[parent] = e;
 }//downHeap, L.Allison, Comp Sci and Software Engineering, Monash University


function topHeap()
// PRE: this.size > 0
 { var ans = this.arr[1];                                                  // C
   this.size--;                                                            // S
   if(this.size <= 0) return ans;                                          // S
   //else                                                                  // E
   this.downHeap(this.arr[this.size+1]);
   return ans;
 }


function PriorityQ()                                                       // A
 { this.arr  = new Array(); // NB. never use arr[0]                           l
   this.size = 0; // so far                                                   l
                                                                           // i
   this.pushq = upHeap;                                                    // s
   this.popq  = topHeap; this.downHeap = downHeap;                         // o
 }//constructor                                                               n


function Kruskal(G)                                                        // L
// G is a graph implemented by adjacency lists!                               A
// return a minimum spanning tree, T                                          l
 { var i;                                                                  // l
   var T = new Array();         // to be the min' spanning tree               i
                                                                           // s
   var Q = new PriorityQ();                                                // o
                                                                           // n
   for(i=0; i < G.Vertices; i++)//first make a PriorityQ of all the edges of G
    { var Gi = G.vertex[i];
      while(Gi != null)         // i.e. for each edge {i,v}
       { Q.pushq(new Edge2(i, Gi.v, Gi.wt));
         Gi = Gi.tl;
       }
    }

   var n = 0;                                               // Computer Science
   var P = new Partition(G.Vertices);                                      // M
   P.singletons();                                                         // o
   while(P.size > 1)            // the min' spanning tree algorithm proper    n
    { if(Q.size <= 0)           // G disconnected                             a
        break;                                                             // s
                                                                           // h
      var e = Q.popq();         // shortest edge
      if(P.elt[e.v1].setNumber != P.elt[e.v2].setNumber)                   // U
       { T[n] = e;  n++;                                                   // n
         P.merge(P.elt[e.v1].setNumber, P.elt[e.v2].setNumber);            // i
       }
    }

   return T;
 }//Kruskal,  NB. can work on adjacency matrix, but is best on sparse graphs.
