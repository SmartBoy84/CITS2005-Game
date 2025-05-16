Is it ok for me make it so that my makeMove function does nothing once
gameEnded?

Should I have the winner checking happening inside the makeTurn or *only when winner() or isOver() is called?

Should the PieceColour which won persist, even if makeMove called after?

Do I need to verify grid behaviour in GameTest? For example, do I need to verify
that? I.e., can I expect the behaviour asserted in MoveTest and GameTest to hold?

Does draw only occur once all spots are filled with no winner?