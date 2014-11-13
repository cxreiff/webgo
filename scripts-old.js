 
$(document).ready(function f() {
	f.turn = true;
	
	$("div.tile").click(function () {
		if (f.turn)
		{
			$(this).removeClass("black white");
			$(this).addClass("black");
			$(".marker").removeClass("black");
			$(".marker").addClass("white");

			f.turn = !f.turn;
		}
		else
		{
			$(this).removeClass("black white");
			$(this).addClass("white");
			$(".marker").removeClass("white");
			$(".marker").addClass("black");

			f.turn = !f.turn;
		}

		$(".tile").each(function () {
			if ($(this).hasClass("black")) {
				var tid = parseInt($(this).getAttribute('id'));
				var up = (tid - 7).toString();
                var down = (tid + 7).toString();
                var left = (tid - 1).toString();
                var left = (tid - 1).toString();
				var right = (tid + 1).toString();
				if ($("t" + up).hasClass("white") && $("t"+down).hasClass("white") && $("t"+left).hasClass("white") && $("t"+right).hasClass("white"))
				{
					$(this).removeClass("black");
				}
			}
			else if($(this).hasClass("white"))
			{

			}
		});
		});
});