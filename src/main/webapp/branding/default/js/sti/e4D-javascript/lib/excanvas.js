if(!document.createElement("canvas").getContext){
(function(){
var m=Math;
var mr=m.round;
var ms=m.sin;
var mc=m.cos;
var _5=m.abs;
var _6=m.sqrt;
var Z=10;
var Z2=Z/2;
function getContext(){
return this.context_||(this.context_=new CanvasRenderingContext2D_(this));
};
var _9=Array.prototype.slice;
function bind(f,_b,_c){
var a=_9.call(arguments,2);
return function(){
return f.apply(_b,a.concat(_9.call(arguments)));
};
};
var _e={init:function(_f){
if(/MSIE/.test(navigator.userAgent)&&!window.opera){
var doc=_f||document;
doc.createElement("canvas");
doc.attachEvent("onreadystatechange",bind(this.init_,this,doc));
}
},init_:function(doc){
if(!doc.namespaces["g_vml_"]){
doc.namespaces.add("g_vml_","urn:schemas-microsoft-com:vml","#default#VML");
}
if(!doc.namespaces["g_o_"]){
doc.namespaces.add("g_o_","urn:schemas-microsoft-com:office:office","#default#VML");
}
if(!doc.styleSheets["ex_canvas_"]){
var ss=doc.createStyleSheet();
ss.owningElement.id="ex_canvas_";
ss.cssText="canvas{display:inline-block;overflow:hidden;"+"text-align:left;width:300px;height:150px}"+"g_vml_\\:*{behavior:url(#default#VML)}"+"g_o_\\:*{behavior:url(#default#VML)}";
}
var els=doc.getElementsByTagName("canvas");
for(var i=0;i<els.length;i++){
this.initElement(els[i]);
}
},initElement:function(el){
if(!el.getContext){
el.getContext=getContext;
el.innerHTML="";
el.attachEvent("onpropertychange",onPropertyChange);
el.attachEvent("onresize",onResize);
var _16=el.attributes;
if(_16.width&&_16.width.specified){
el.style.width=_16.width.nodeValue+"px";
}else{
el.width=el.clientWidth;
}
if(_16.height&&_16.height.specified){
el.style.height=_16.height.nodeValue+"px";
}else{
el.height=el.clientHeight;
}
}
return el;
}};
function onPropertyChange(e){
var el=e.srcElement;
switch(e.propertyName){
case "width":
el.style.width=el.attributes.width.nodeValue+"px";
el.getContext().clearRect();
break;
case "height":
el.style.height=el.attributes.height.nodeValue+"px";
el.getContext().clearRect();
break;
}
};
function onResize(e){
var el=e.srcElement;
if(el.firstChild){
el.firstChild.style.width=el.clientWidth+"px";
el.firstChild.style.height=el.clientHeight+"px";
}
};
_e.init();
var _1b=[];
for(var i=0;i<16;i++){
for(var j=0;j<16;j++){
_1b[i*16+j]=i.toString(16)+j.toString(16);
}
}
function createMatrixIdentity(){
return [[1,0,0],[0,1,0],[0,0,1]];
};
function matrixMultiply(m1,m2){
var _20=createMatrixIdentity();
for(var x=0;x<3;x++){
for(var y=0;y<3;y++){
var sum=0;
for(var z=0;z<3;z++){
sum+=m1[x][z]*m2[z][y];
}
_20[x][y]=sum;
}
}
return _20;
};
function copyState(o1,o2){
o2.fillStyle=o1.fillStyle;
o2.lineCap=o1.lineCap;
o2.lineJoin=o1.lineJoin;
o2.lineWidth=o1.lineWidth;
o2.miterLimit=o1.miterLimit;
o2.shadowBlur=o1.shadowBlur;
o2.shadowColor=o1.shadowColor;
o2.shadowOffsetX=o1.shadowOffsetX;
o2.shadowOffsetY=o1.shadowOffsetY;
o2.strokeStyle=o1.strokeStyle;
o2.globalAlpha=o1.globalAlpha;
o2.arcScaleX_=o1.arcScaleX_;
o2.arcScaleY_=o1.arcScaleY_;
o2.lineScale_=o1.lineScale_;
};
function processStyle(_27){
var str,_29=1;
_27=String(_27);
if(_27.substring(0,3)=="rgb"){
//var _2a=_27.indexOf("(",3);
var _2a= $.inArray("(", _27, 3);

//var end=_27.indexOf(")",_2a+1);
var end= $.inArray(")", _27, _2a+1);

var _2c=_27.substring(_2a+1,end).split(",");
str="#";
for(var i=0;i<3;i++){
str+=_1b[Number(_2c[i])];
}
if(_2c.length==4&&_27.substr(3,1)=="a"){
_29=_2c[3];
}
}else{
str=_27;
}
return {color:str,alpha:_29};
};
function processLineCap(_2e){
switch(_2e){
case "butt":
return "flat";
case "round":
return "round";
case "square":
default:
return "square";
}
};
function CanvasRenderingContext2D_(_2f){
this.m_=createMatrixIdentity();
this.mStack_=[];
this.aStack_=[];
this.currentPath_=[];
this.strokeStyle="#000";
this.fillStyle="#000";
this.lineWidth=1;
this.lineJoin="miter";
this.lineCap="butt";
this.miterLimit=Z*1;
this.globalAlpha=1;
this.canvas=_2f;
var el=_2f.ownerDocument.createElement("div");
el.style.width=_2f.clientWidth+"px";
el.style.height=_2f.clientHeight+"px";
el.style.overflow="hidden";
el.style.position="absolute";
_2f.appendChild(el);
this.element_=el;
this.arcScaleX_=1;
this.arcScaleY_=1;
this.lineScale_=1;
};
var _31=CanvasRenderingContext2D_.prototype;
_31.clearRect=function(){
this.element_.innerHTML="";
};
_31.beginPath=function(){
this.currentPath_=[];
};
_31.moveTo=function(aX,aY){
var p=this.getCoords_(aX,aY);
this.currentPath_.push({type:"moveTo",x:p.x,y:p.y});
this.currentX_=p.x;
this.currentY_=p.y;
};
_31.lineTo=function(aX,aY){
var p=this.getCoords_(aX,aY);
this.currentPath_.push({type:"lineTo",x:p.x,y:p.y});
this.currentX_=p.x;
this.currentY_=p.y;
};
_31.bezierCurveTo=function(_38,_39,_3a,_3b,aX,aY){
var p=this.getCoords_(aX,aY);
var cp1=this.getCoords_(_38,_39);
var cp2=this.getCoords_(_3a,_3b);
bezierCurveTo(this,cp1,cp2,p);
};
function bezierCurveTo(_41,cp1,cp2,p){
_41.currentPath_.push({type:"bezierCurveTo",cp1x:cp1.x,cp1y:cp1.y,cp2x:cp2.x,cp2y:cp2.y,x:p.x,y:p.y});
_41.currentX_=p.x;
_41.currentY_=p.y;
};
_31.quadraticCurveTo=function(_45,_46,aX,aY){
var cp=this.getCoords_(_45,_46);
var p=this.getCoords_(aX,aY);
var cp1={x:this.currentX_+2/3*(cp.x-this.currentX_),y:this.currentY_+2/3*(cp.y-this.currentY_)};
var cp2={x:cp1.x+(p.x-this.currentX_)/3,y:cp1.y+(p.y-this.currentY_)/3};
bezierCurveTo(this,cp1,cp2,p);
};
_31.arc=function(aX,aY,_4f,_50,_51,_52){
_4f*=Z;
var _53=_52?"at":"wa";
var _54=aX+mc(_50)*_4f-Z2;
var _55=aY+ms(_50)*_4f-Z2;
var _56=aX+mc(_51)*_4f-Z2;
var _57=aY+ms(_51)*_4f-Z2;
if(_54==_56&&!_52){
_54+=0.125;
}
var p=this.getCoords_(aX,aY);
var _59=this.getCoords_(_54,_55);
var _5a=this.getCoords_(_56,_57);
this.currentPath_.push({type:_53,x:p.x,y:p.y,radius:_4f,xStart:_59.x,yStart:_59.y,xEnd:_5a.x,yEnd:_5a.y});
};
_31.rect=function(aX,aY,_5d,_5e){
this.moveTo(aX,aY);
this.lineTo(aX+_5d,aY);
this.lineTo(aX+_5d,aY+_5e);
this.lineTo(aX,aY+_5e);
this.closePath();
};
_31.strokeRect=function(aX,aY,_61,_62){
var _63=this.currentPath_;
this.beginPath();
this.moveTo(aX,aY);
this.lineTo(aX+_61,aY);
this.lineTo(aX+_61,aY+_62);
this.lineTo(aX,aY+_62);
this.closePath();
this.stroke();
this.currentPath_=_63;
};
_31.fillRect=function(aX,aY,_66,_67){
var _68=this.currentPath_;
this.beginPath();
this.moveTo(aX,aY);
this.lineTo(aX+_66,aY);
this.lineTo(aX+_66,aY+_67);
this.lineTo(aX,aY+_67);
this.closePath();
this.fill();
this.currentPath_=_68;
};
_31.createLinearGradient=function(aX0,aY0,aX1,aY1){
var _6d=new CanvasGradient_("gradient");
_6d.x0_=aX0;
_6d.y0_=aY0;
_6d.x1_=aX1;
_6d.y1_=aY1;
return _6d;
};
_31.createRadialGradient=function(aX0,aY0,aR0,aX1,aY1,aR1){
var _74=new CanvasGradient_("gradientradial");
_74.x0_=aX0;
_74.y0_=aY0;
_74.r0_=aR0;
_74.x1_=aX1;
_74.y1_=aY1;
_74.r1_=aR1;
return _74;
};
_31.drawImage=function(_75,_76){
var dx,dy,dw,dh,sx,sy,sw,sh;
var _7f=_75.runtimeStyle.width;
var _80=_75.runtimeStyle.height;
_75.runtimeStyle.width="auto";
_75.runtimeStyle.height="auto";
var w=_75.width;
var h=_75.height;
_75.runtimeStyle.width=_7f;
_75.runtimeStyle.height=_80;
if(arguments.length==3){
dx=arguments[1];
dy=arguments[2];
sx=sy=0;
sw=dw=w;
sh=dh=h;
}else{
if(arguments.length==5){
dx=arguments[1];
dy=arguments[2];
dw=arguments[3];
dh=arguments[4];
sx=sy=0;
sw=w;
sh=h;
}else{
if(arguments.length==9){
sx=arguments[1];
sy=arguments[2];
sw=arguments[3];
sh=arguments[4];
dx=arguments[5];
dy=arguments[6];
dw=arguments[7];
dh=arguments[8];
}else{
throw Error("Invalid number of arguments");
}
}
}
var d=this.getCoords_(dx,dy);
var w2=sw/2;
var h2=sh/2;
var _86=[];
var W=10;
var H=10;
_86.push(" <g_vml_:group"," coordsize=\"",Z*W,",",Z*H,"\""," coordorigin=\"0,0\""," style=\"width:",W,"px;height:",H,"px;position:absolute;");
if(this.m_[0][0]!=1||this.m_[0][1]){
var _89=[];
_89.push("M11=",this.m_[0][0],",","M12=",this.m_[1][0],",","M21=",this.m_[0][1],",","M22=",this.m_[1][1],",","Dx=",mr(d.x/Z),",","Dy=",mr(d.y/Z),"");
var max=d;
var c2=this.getCoords_(dx+dw,dy);
var c3=this.getCoords_(dx,dy+dh);
var c4=this.getCoords_(dx+dw,dy+dh);
max.x=m.max(max.x,c2.x,c3.x,c4.x);
max.y=m.max(max.y,c2.y,c3.y,c4.y);
_86.push("padding:0 ",mr(max.x/Z),"px ",mr(max.y/Z),"px 0;filter:progid:DXImageTransform.Microsoft.Matrix(",_89.join(""),", sizingmethod='clip');");
}else{
_86.push("top:",mr(d.y/Z),"px;left:",mr(d.x/Z),"px;");
}
_86.push(" \">","<g_vml_:image src=\"",_75.src,"\""," style=\"width:",Z*dw,"px;"," height:",Z*dh,"px;\""," cropleft=\"",sx/w,"\""," croptop=\"",sy/h,"\""," cropright=\"",(w-sx-sw)/w,"\""," cropbottom=\"",(h-sy-sh)/h,"\""," />","</g_vml_:group>");
this.element_.insertAdjacentHTML("BeforeEnd",_86.join(""));
};
_31.stroke=function(_8e){
var _8f=[];
var _90=false;
var a=processStyle(_8e?this.fillStyle:this.strokeStyle);
var _92=a.color;
var _93=a.alpha*this.globalAlpha;
var W=10;
var H=10;
_8f.push("<g_vml_:shape"," filled=\"",!!_8e,"\""," style=\"position:absolute;width:",W,"px;height:",H,"px;\""," coordorigin=\"0 0\" coordsize=\"",Z*W," ",Z*H,"\""," stroked=\"",!_8e,"\""," path=\"");
var _96=false;
var min={x:null,y:null};
var max={x:null,y:null};
for(var i=0;i<this.currentPath_.length;i++){
var p=this.currentPath_[i];
var c;
switch(p.type){
case "moveTo":
c=p;
_8f.push(" m ",mr(p.x),",",mr(p.y));
break;
case "lineTo":
_8f.push(" l ",mr(p.x),",",mr(p.y));
break;
case "close":
_8f.push(" x ");
p=null;
break;
case "bezierCurveTo":
_8f.push(" c ",mr(p.cp1x),",",mr(p.cp1y),",",mr(p.cp2x),",",mr(p.cp2y),",",mr(p.x),",",mr(p.y));
break;
case "at":
case "wa":
_8f.push(" ",p.type," ",mr(p.x-this.arcScaleX_*p.radius),",",mr(p.y-this.arcScaleY_*p.radius)," ",mr(p.x+this.arcScaleX_*p.radius),",",mr(p.y+this.arcScaleY_*p.radius)," ",mr(p.xStart),",",mr(p.yStart)," ",mr(p.xEnd),",",mr(p.yEnd));
break;
}
if(p){
if(min.x==null||p.x<min.x){
min.x=p.x;
}
if(max.x==null||p.x>max.x){
max.x=p.x;
}
if(min.y==null||p.y<min.y){
min.y=p.y;
}
if(max.y==null||p.y>max.y){
max.y=p.y;
}
}
}
_8f.push(" \">");
if(!_8e){
var _9c=this.lineScale_*this.lineWidth;
if(_9c<1){
_93*=_9c;
}
_8f.push("<g_vml_:stroke"," opacity=\"",_93,"\""," joinstyle=\"",this.lineJoin,"\""," miterlimit=\"",this.miterLimit,"\""," endcap=\"",processLineCap(this.lineCap),"\""," weight=\"",_9c,"px\""," color=\"",_92,"\" />");
}else{
if(typeof this.fillStyle=="object"){
var _9d=this.fillStyle;
var _9e=0;
var _9f={x:0,y:0};
var _a0=0;
var _a1=1;
if(_9d.type_=="gradient"){
var x0=_9d.x0_/this.arcScaleX_;
var y0=_9d.y0_/this.arcScaleY_;
var x1=_9d.x1_/this.arcScaleX_;
var y1=_9d.y1_/this.arcScaleY_;
var p0=this.getCoords_(x0,y0);
var p1=this.getCoords_(x1,y1);
var dx=p1.x-p0.x;
var dy=p1.y-p0.y;
_9e=Math.atan2(dx,dy)*180/Math.PI;
if(_9e<0){
_9e+=360;
}
if(_9e<0.000001){
_9e=0;
}
}else{
var p0=this.getCoords_(_9d.x0_,_9d.y0_);
var _aa=max.x-min.x;
var _ab=max.y-min.y;
_9f={x:(p0.x-min.x)/_aa,y:(p0.y-min.y)/_ab};
_aa/=this.arcScaleX_*Z;
_ab/=this.arcScaleY_*Z;
var _ac=m.max(_aa,_ab);
_a0=2*_9d.r0_/_ac;
_a1=2*_9d.r1_/_ac-_a0;
}
var _ad=_9d.colors_;
_ad.sort(function(cs1,cs2){
return cs1.offset-cs2.offset;
});
var _b0=_ad.length;
var _b1=_ad[0].color;
var _b2=_ad[_b0-1].color;
var _b3=_ad[0].alpha*this.globalAlpha;
var _b4=_ad[_b0-1].alpha*this.globalAlpha;
var _b5=[];
for(var i=0;i<_b0;i++){
var _b6=_ad[i];
_b5.push(_b6.offset*_a1+_a0+" "+_b6.color);
}
_8f.push("<g_vml_:fill type=\"",_9d.type_,"\""," method=\"none\" focus=\"100%\""," color=\"",_b1,"\""," color2=\"",_b2,"\""," colors=\"",_b5.join(","),"\""," opacity=\"",_b4,"\""," g_o_:opacity2=\"",_b3,"\""," angle=\"",_9e,"\""," focusposition=\"",_9f.x,",",_9f.y,"\" />");
}else{
_8f.push("<g_vml_:fill color=\"",_92,"\" opacity=\"",_93,"\" />");
}
}
_8f.push("</g_vml_:shape>");
this.element_.insertAdjacentHTML("beforeEnd",_8f.join(""));
};
_31.fill=function(){
this.stroke(true);
};
_31.closePath=function(){
this.currentPath_.push({type:"close"});
};
_31.getCoords_=function(aX,aY){
var m=this.m_;
return {x:Z*(aX*m[0][0]+aY*m[1][0]+m[2][0])-Z2,y:Z*(aX*m[0][1]+aY*m[1][1]+m[2][1])-Z2};
};
_31.save=function(){
var o={};
copyState(this,o);
this.aStack_.push(o);
this.mStack_.push(this.m_);
this.m_=matrixMultiply(createMatrixIdentity(),this.m_);
};
_31.restore=function(){
copyState(this.aStack_.pop(),this);
this.m_=this.mStack_.pop();
};
function matrixIsFinite(m){
for(var j=0;j<3;j++){
for(var k=0;k<2;k++){
if(!isFinite(m[j][k])||isNaN(m[j][k])){
return false;
}
}
}
return true;
};
function setM(ctx,m,_c0){
if(!matrixIsFinite(m)){
return;
}
ctx.m_=m;
if(_c0){
var det=m[0][0]*m[1][1]-m[0][1]*m[1][0];
ctx.lineScale_=_6(_5(det));
}
};
_31.translate=function(aX,aY){
var m1=[[1,0,0],[0,1,0],[aX,aY,1]];
setM(this,matrixMultiply(m1,this.m_),false);
};
_31.rotate=function(_c5){
var c=mc(_c5);
var s=ms(_c5);
var m1=[[c,s,0],[-s,c,0],[0,0,1]];
setM(this,matrixMultiply(m1,this.m_),false);
};
_31.scale=function(aX,aY){
this.arcScaleX_*=aX;
this.arcScaleY_*=aY;
var m1=[[aX,0,0],[0,aY,0],[0,0,1]];
setM(this,matrixMultiply(m1,this.m_),true);
};
_31.transform=function(m11,m12,m21,m22,dx,dy){
var m1=[[m11,m12,0],[m21,m22,0],[dx,dy,1]];
setM(this,matrixMultiply(m1,this.m_),true);
};
_31.setTransform=function(m11,m12,m21,m22,dx,dy){
var m=[[m11,m12,0],[m21,m22,0],[dx,dy,1]];
setM(this,m,true);
};
_31.clip=function(){
};
_31.arcTo=function(){
};
_31.createPattern=function(){
return new CanvasPattern_;
};
function CanvasGradient_(_da){
this.type_=_da;
this.x0_=0;
this.y0_=0;
this.r0_=0;
this.x1_=0;
this.y1_=0;
this.r1_=0;
this.colors_=[];
};
CanvasGradient_.prototype.addColorStop=function(_db,_dc){
_dc=processStyle(_dc);
this.colors_.push({offset:_db,color:_dc.color,alpha:_dc.alpha});
};
function CanvasPattern_(){
};
G_vmlCanvasManager=_e;
CanvasRenderingContext2D=CanvasRenderingContext2D_;
CanvasGradient=CanvasGradient_;
CanvasPattern=CanvasPattern_;
})();
}
