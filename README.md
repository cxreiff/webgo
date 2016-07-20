webgo
=====

Web based version of the board game Go, with a simple AI service.

Game and interface written in HTML, CSS, Javascript, with AI in Java.

The AI uses Monte Carlo Tree Search with UCB as the tree policy.

AI suggests moves for whoever's turn it currently is, marking the
suggested location with a black or white circle outline, as well as
updating the running count of each player's territory. For AI to
function, GoAI.java and the files it depends on must be compiled and
run locally so that the page can access it at 127.0.0.1:8080.
Otherwise, territory will remain at zero, and suggestions will not
be available.

Published at jaxuru.me/webgo/go.html via Github Pages.

Developed for COSC331 - Games at Amherst College.
