var Ellipsis = function(cmp, ops) {

	var $cmp	= $(cmp);
        var $inner 	= $cmp.find('.ellipsis-inner');
	var text	= [];

	var tail	= ops && ops.tail ? ops.tail : "...";
	var fixed	= false;


	if(ops && ops.fixed){
		fixed = ops.fixed;
	}


	var fn = function(){
		return ( $inner[0].offsetHeight > $cmp.height()+1 );/* chrome +1 for border */
	};

	var respond = function(){

		$inner.html(text.join('') + (fixed ? fixed : ""));
		if(fn()){

			$inner.html("");

			var str	= "";
			var i	= 0;

			while(i<text.length){
				if(fn()){
					var lastChar = "X";
					while(lastChar.trim() != "" && str.length>0){
						str = str.substring(0, str.length>0 ? str.length-1 : str.length); // subtract last
						lastChar = str.substring(str.length-2, str.length-1);
					}
					$inner.html(str + tail + (fixed ? fixed : "") );

					i=text.length; // return
				}
				else{
					str += text[i];
					$inner.html(str + tail + (fixed ? fixed : "") );
					i++;
				}
			}
		}

		if(fixed){
			var $fixed = $cmp.find('.fixed');
			$fixed.css("position",	"absolute");
			$fixed.css("right",		"0px");
			$fixed.css("bottom",	"0px");
			//$fixed.css("float",	"right");
			console.log("floating...");
	
		}
	};





	var init = function(){

		if($inner.length==0){ // initialise dom
			var content = $cmp.html();
			$cmp.html("");
			$inner = jQuery('<div class="ellipsis-inner"></div>').appendTo($cmp);
			$inner.append(content);
		}


		var innerHtml = $inner.html().trim();
		for(var i=0; i<innerHtml.length; i++){ // initialise text
			text[i]=innerHtml.substr(i, 1);
		}

		respond();
	};

	init();

	$(window).bind('resize', function(){
		respond();
	});

};



