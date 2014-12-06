
//Capture Checking Strategy:	If neighbors of checked stone are opposite color, capture.
//								If one neighbor is empty, don't capture.
//								For each neighbor that's the same color, check that neighbor 
//								for capture not including the space starting the recursive call.
//								If no piece in the recursive stack finds an empty spot, capture all.
//								(Keep list of tiles checked recursively so that all can be either
//								captured or removed from the list of pieces to check, and to prevent
//								loops where a recursively checked tile checks its caller.)

var blackCap = 0;
var whiteCap = 0;
var finished = false;
var posits = ["eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
				"eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
				"eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
				"eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
				"eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
				"eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"];

$(document).ready(function f() {

	//Keeps board square when width is dynamically set.
	$(window).on("load resize scroll",function(e){
    	var cw = $('.board').width();
		$('.board').css({
    		'height': cw + 'px'
		});
	});


	var passed = false;
	f.turn = true;

	$("#pass").click(function () {

		if (passed) endgame();
		else {

			if (f.turn) {

				$(".marker").removeClass("black");
				$(".marker").addClass("white");
			} else {

				$(".marker").removeClass("white");
				$(".marker").addClass("black");
			}

			passed = true;
			f.turn = !f.turn;
		}
	});

	$("#suggest").click(function () {

		$("#suggest").html(". . .");

		var whichTurn = (f.turn ? "0" : "1");

		$.getJSON("http://127.0.0.1:8080/go.html/?"+"0"+whichTurn+"x"+boardString()+"x"
			+posits[0]+posits[1]+posits[2]+posits[3]+posits[4]+posits[5], showSuggest);
	});

	$("div.tile").click(function () {

		$("div.tile").each(function () {

			$(this).removeClass("outlineb");
			$(this).removeClass("outlinew");
		});

		if (finished) return;

		passed = false;

		if ((!($(this).hasClass("black"))&&(!($(this).hasClass("white")))))	//Turn Switching & Stone placement.
		{
			if (f.turn)
			{
				$(this).addClass("black");
				
				captureWhite($(this));
				var legal = captureBlack($(this));

				if (legal > 0) {

					posits.shift();
					posits.push(boardString());
					
					$(".marker").removeClass("black");
					$(".marker").addClass("white");
					f.turn = !f.turn;
				}
				else {$(this).removeClass("black");}
			}
			else
			{
				$(this).addClass("white");

				captureBlack($(this));
				var legal = captureWhite($(this));

				if (legal > 0) {

					posits.shift();
					posits.push(boardString());

					$(".marker").removeClass("white");
					$(".marker").addClass("black");
					f.turn = !f.turn;
				}
				else {$(this).removeClass("white");}
			}

			$("#score").html("Captures: "+blackCap.toString()+" - "+whiteCap.toString());
		}

		var whichTurn = (f.turn ? "0" : "1");

		$.getJSON("http://127.0.0.1:8080/go.html/?"+"1"+whichTurn+"x"+boardString()+"x"
			+posits[0]+posits[1]+posits[2]+posits[3]+posits[4]+posits[5], showTerr);
	});
});

function checkKo(pos) {

	if ($.inArray(pos, posits) > -1) { return false; }
	
	return true;
}

function captureWhite(recent) {	//Tests groups of stones for liberties and conditionally captures.

	var whitecheck = $(".tile.white");

	while (whitecheck.length > 0) {

		var $check = $("#"+whitecheck[0].id);

		var connected = [];

		connect($check, connected);

		var life = false;

		for (j = 0; j < connected.length; j++) {

			var tid = parseInt(connected[j].slice(1));
			var up = (tid - 7).toString();
	    	var down = (tid + 7).toString();
	    	var left = (tid - 1).toString();
			var right = (tid + 1).toString();

			if (!($("#t"+up).hasClass("black")) && !($("#t"+up).hasClass("white")) && $("#t" + up).length != 0) {life = true; break;}
			if (!($("#t"+down).hasClass("black")) && !($("#t"+down).hasClass("white")) && $("#t" + down).length != 0) {life = true; break;}
			if (!($("#t"+left).hasClass("black")) && !($("#t"+left).hasClass("white")) && left % 7 != 0) {life = true; break;}
			if (!($("#t"+right).hasClass("black")) && !($("#t"+right).hasClass("white")) && right % 7 != 1) {life = true; break;}
		}

		if (!life) { for (j = 0; j < connected.length; j++) {

			if (($.inArray(recent.attr('id'), connected) == -1) && checkKo(boardString(connected))) {

				blackCap++;
				$("#"+connected[j]).removeClass("white");
				whitecheck = whitecheck.not("#"+connected[j]);
			} else {

				return 0;
			}
		}}
		else {for (j = 0; j < connected.length; j++) { whitecheck = whitecheck.not("#"+connected[j]);}}
	}

	return 1;
}

function captureBlack(recent) {	//Tests groups of stones for liberties and conditionally captures.

	var blackcheck = $(".tile.black");

	while (blackcheck.length > 0) {

		var $check = $("#"+blackcheck[0].id);

		var connected = [];

		connect($check, connected);

		var life = false;

		for (j = 0; j < connected.length; j++) {

			var tid = parseInt(connected[j].slice(1));
			var up = (tid - 7).toString();
	    	var down = (tid + 7).toString();
	    	var left = (tid - 1).toString();
			var right = (tid + 1).toString();

			if (!($("#t"+up).hasClass("white")) && !($("#t"+up).hasClass("black")) && $("#t" + up).length != 0) {life = true; break;}
			if (!($("#t"+down).hasClass("white")) && !($("#t"+down).hasClass("black")) && $("#t" + down).length != 0) {life = true; break;}
			if (!($("#t"+left).hasClass("white")) && !($("#t"+left).hasClass("black")) && left % 7 != 0) {life = true; break;}
			if (!($("#t"+right).hasClass("white")) && !($("#t"+right).hasClass("black")) && right % 7 != 1) {life = true; break;}
		}

		if (!life) { for (j = 0; j < connected.length; j++) {

			if (($.inArray(recent.attr('id'), connected) == -1) && checkKo(boardString(connected))) {

				whiteCap++;
				$("#"+connected[j]).removeClass("black");
				blackcheck = blackcheck.not("#"+connected[j]);

			} else {

				return 0;
			}
		}}
		else {for (j = 0; j < connected.length; j++) { blackcheck = blackcheck.not("#"+connected[j]);}}
	}

	return 1;
}

function connect(current, connected) {	//Generates a list of adjacent tiles with stones of the same color (group of connected stones).

	var tid = parseInt((current.attr('id')).slice(1));
	var up = (tid - 7).toString();
	var down = (tid + 7).toString();
	var left = (tid - 1).toString();
	var right = (tid + 1).toString();

	if (current.hasClass("black")) {

		connected.push("t"+tid);

		if (($.inArray("t"+up, connected) == -1) && (parseInt(up) > 0) && $("#t" + up).hasClass("black")) {

			connect($("#t"+up),connected);
		}
		if (($.inArray("t"+down, connected) == -1) && (parseInt(down) < 50) && $("#t"+down).hasClass("black")) {

			connect($("#t"+down),connected);
		}
		if (($.inArray("t"+left, connected) == -1) && (parseInt(left) % 7 != 0) && $("#t"+left).hasClass("black")) {

			connect($("#t"+left),connected);
		}
		if (($.inArray("t"+right, connected) == -1) && (parseInt(right) % 7 != 1) && $("#t"+right).hasClass("black")) {

			connect($("#t"+right),connected);
		}

	} else if (current.hasClass("white")) {

		connected.push("t"+tid);

		if (($.inArray("t"+up, connected) == -1) && (parseInt(up) > 0) && $("#t" + up).hasClass("white")) {

			connect($("#t"+up),connected);
		}
		if (($.inArray("t"+down, connected) == -1) && (parseInt(down) < 50) && $("#t"+down).hasClass("white")) {

			connect($("#t"+down),connected);
		}
		if (($.inArray("t"+left, connected) == -1) && (parseInt(left) % 7 != 0) && $("#t"+left).hasClass("white")) {

			connect($("#t"+left),connected);
		}
		if (($.inArray("t"+right, connected) == -1) && (parseInt(right) % 7 != 1) && $("#t"+right).hasClass("white")) {

			connect($("#t"+right),connected);
		}
	}
}

function showSuggest(suggestion) {

	$("#suggest").html("SUGGEST");

	var current = boardString();

	if(boardString() == suggestion.state) {

		for(i = 0; i < current.length; i++)
		{
			if (current.charAt(i) == 'e') {

				if (suggestion.result.charAt(i) == 'b') {

					$("#t"+(i+1).toString()).addClass("outlineb");
				}
				if (suggestion.result.charAt(i) == 'w') {

					$("#t"+(i+1).toString()).addClass("outlinew");
				}
			}
		}
	}
}

function showTerr(report) {

	var target = null;

	for(i = 0; i < report.result.length; i++) {

		if(report.result.charAt(i) == 'x') var target = i;
	}

	$("#terr").html("Territory: "+report.result.substring(0,target)+" - "+report.result.slice(target+1));
}

function boardString(except) {

	var tiles = $(".tile");
	var result = "";

	for (i = 0; i < tiles.length; i++) {

		if ($.inArray(tiles[i].id, except) > -1) {

			result = result + "e";
		} else {

			if ($("#"+tiles[i].id).hasClass("black")) result = result + "b";
			else if ($("#"+tiles[i].id).hasClass("white")) result = result + "w";
			else result = result + "e";
		}
	}

	return result;
}

function endgame() {

	$(".pass").html("FINISHED");
	finished = true;

	var blackTer = 0;
	var whiteTer = 0;

	result = boardString();

}
