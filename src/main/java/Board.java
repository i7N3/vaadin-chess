import lombok.Getter;
import lombok.Setter;

public class Board {

    @Getter @Setter String fen;
    @Getter @Setter public Color moveColor;
    @Getter @Setter int moveNumber;
    Figure[][] figures;

    public Board(String fen)
    {
        this.fen = fen;
        this.figures = new Figure[8][8];
        Init();
    }

    void Init()
    {
        // "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        //  0                                           1 2    3 4 5

        String[] parts = fen.split(" ");
        if (parts.length != 6) return;

        InitFigures(parts[0]);
        moveColor = parts[1] == "b" ? Color.black : Color.white;
        moveNumber = Integer.parseInt(parts[5]);

        // SetFigureAt(new Square("a1"), Figure.whiteKing);
        // SetFigureAt(new Square("h8"), Figure.blackKing);
        // moveColor = Color.white;
    }

    void InitFigures(String data)
    {
        for (int j = 8; j >= 2; j--)
            data = data.replaceAll(Integer.toString(j), (j - 1) + "1");

        String[] lines = data.split("/");

        for (int y = 7; y >= 0; y--)
            for (int x = 0; x < 8; x++)
                this.figures[x][y] = Figure.getFigureType(lines[7 - y].charAt(x));
    }

    void GenerateFen()
    {
        this.fen = FenFigures() + " " +
                (moveColor == Color.white ? "w" : "b") +
                " - - 0 " + moveNumber;
    }

    String FenFigures()
    {
        StringBuilder sb = new StringBuilder();
        for (int y = 7; y >= 0; y--)
        {
            for (int x = 0; x < 8; x++)
                sb.append(figures[x][y] == Figure.none ? '1' : figures[x][y].figure);
            if (y > 0)
                sb.append("/");
        }
        return sb.toString();
    }

    public Figure GetFigureAt(Square square)
    {
        if(square.OnBoard() && figures[square.x][square.y] instanceof Figure)
            return figures[square.x][square.y];
        return Figure.none;
    }

    void SetFigureAt(Square square, Figure figure)
    {
        if(square.OnBoard())
            figures[square.x][square.y] = figure;
    }

    public Board Move(FigureMoving fm)
    {
        Board next = new Board(fen);
        next.SetFigureAt(fm.from, Figure.none);
        next.SetFigureAt(fm.to, fm.promotion == Figure.none ? fm.figure : fm.promotion);

        // Check this
        if(moveColor == Color.black)
            next.moveNumber++;

        next.moveColor = moveColor.FlipColor(moveColor);
        next.GenerateFen();
        return next;
    }
}
