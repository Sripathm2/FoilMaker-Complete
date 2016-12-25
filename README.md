"FoilMaker-Complete" 

Foilmaker is a game played between 2 or more people. The game flows as a question is given to all the players and each of them suggest an 
possible answer (but not the correct one). The computer shuffles all the answers and adds them along with the correct answer to the list
of choices. this list is presented to all the players and their options are taken. The result is calculated upon the number of players one
is successful in fooling and for getting the correct answer. 


Server - it is the main controller which controls the flow of game.
it implements threads and hence a new object is created for each new player. the input method does the main task of reading computing and 
presenting the result. All the player's ids are stored in two arraylists for ensuring concurrency.

Client - it is the part of code which every player has while playing.
It follows MVC format in which every java code has its own model, view and controller. Model is implemented by the method Call , view is
defined in the constructor and the controller is given to call1 method. 
