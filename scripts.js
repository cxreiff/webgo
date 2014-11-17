
//Capture Checking Strategy:	If neighbors of checked stone are opposite color, capture.
//								If one neighbor is empty, don't capture.
//								For each neighbor that's the same color, check that neighbor 
//								for capture not including the space starting the recursive call.
//								If no piece in the recursive stack finds an empty spot, capture all.
//								(Keep list of tiles checked recursively so that all can be either
//								captured or removed from the list of pieces to check, and to prevent
//								loops where a recursively checked tile checks its caller.)

$(document).ready(function f() {

	$(window).on("load resize scroll",function(e){	//Keeps board square when width is dynamically set.
    	var cw = $('.board').width();
		$('.board').css({
    		'height': cw + 'px'
		});
	});

	f.turn = true;

	$("div.tile").click(function () {

		if ((!($(this).hasClass("black"))&&(!($(this).hasClass("white")))))	//Turn Switching & Stone placement.
		{
			if (f.turn)
			{
				$(this).removeClass("black white");
				$(this).addClass("black");
				$(".marker").removeClass("black");
				$(".marker").addClass("white");

				captureWhite();

				//TODO	Insert code here for checking if stone just placed is captured.
				//		If so, remove without changing score and do not change turn (invalid move).

				f.turn = !f.turn;
			}
			else
			{
				$(this).removeClass("black white");
				$(this).addClass("white");
				$(".marker").removeClass("white");
				$(".marker").addClass("black");

				captureBlack();

				//TODO	Insert code here for checking if stone just placed is captured.
				//		If so, remove without changing score and do not change turn (invalid move).

				f.turn = !f.turn;
			}
		}
	});
});

function captureWhite() {

	var whitecheck = $(".tile.white");

	for(i = 0; i < whitecheck.length; i++) {

		var $check = $("#"+whitecheck[i].id);

		var connected = [];

		connect($check, connected, 0);

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

		if (!life) {for (j = 0; j < connected.length; j++) { $("#"+connected[j]).removeClass("white");}}
		else {for (j = 0; j < connected.length; j++) { /*whitecheck.not("#"+connected[j]); i--;*/}}
	}
}

function captureBlack() {

	var blackcheck = $(".tile.black");

	for(i = 0; i < blackcheck.length; i++) {

		var $check = $("#"+blackcheck[i].id);

		var connected = [];

		connect($check, connected, 0);

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

		if (!life) {for (j = 0; j < connected.length; j++) { $("#"+connected[j]).removeClass("black");}}
		else {for (j = 0; j < connected.length; j++) { /*blackcheck.not("#"+connected[j]); i--;*/}}
	}
}

function connect(current, connected, parent) {	//Generates a list of adjacent tiles with stones of the same color (group of connected stones).

	var tid = parseInt((current.attr('id')).slice(1));
	var up = (tid - 7).toString();
	var down = (tid + 7).toString();
	var left = (tid - 1).toString();
	var right = (tid + 1).toString();

	if (current.hasClass("black")) {

		if ((parseInt(up) != parent) && $("#t" + up).hasClass("black")) {

			connect($("#t"+up),connected,tid);
		}
		if ((parseInt(down) != parent) && $("#t"+down).hasClass("black")) {

			connect($("#t"+down),connected,tid);
		}
		if ((parseInt(left) != parent) && $("#t"+left).hasClass("black")) {

			connect($("#t"+left),connected,tid);
		}
		if ((parseInt(right) != parent) && $("#t"+right).hasClass("black")) {

			connect($("#t"+right),connected,tid);
		}

		connected.push("t"+tid);

	} else if (current.hasClass("white")) {

		if ((parseInt(up) != parent) && $("#t" + up).hasClass("white")) {

			connect($("#t"+up),connected,tid);
		}
		if ((parseInt(down) != parent) && $("#t"+down).hasClass("white")) {

			connect($("#t"+down),connected,tid);
		}
		if ((parseInt(left) != parent) && $("#t"+left).hasClass("white")) {

			connect($("#t"+left),connected,tid);
		}
		if ((parseInt(right) != parent) && $("#t"+right).hasClass("white")) {

			connect($("#t"+right),connected,tid);
		}

		connected.push("t"+tid);

	}
}
