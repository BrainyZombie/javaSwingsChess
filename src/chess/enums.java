
package chess;
enum cellColor{
    white,
    grey,
    red,
    green,
    blue,
    yellow,
    NULL
}
enum playerType{
    human,
    ai
}
enum pieceType{
    king,
    queen,
    rook,
    bishop,
    knight,
    pawn,
    NULL
}
enum pieceColor{
    blue,
    yellow,
    NULL
}

enum moveType{
    recursive,
    single,
    cannotCapture,
    onlyCapture
}

enum specialActionType{
    elPassantRight,
    elPassantLeft,
    pawnDouble,
    castleRight,
    castleLeft,
}

class pieceMove{
    public
    int x;
    int y;
    moveType type;
    pieceMove(int x, int y, moveType type){
        this.x=x;
        this.y=y;
        this.type=type;
    }
    pieceMove(pieceMove previous){
        x = previous.x;
        y = previous.y;
        type = previous.type;
    }
}

abstract class specialAction extends pieceMove{
    specialActionType type; 
    specialAction(int x, int y, moveType type){
        super(x, y, type);
    }
    abstract boolean validateAction();
    abstract void postClick();
}