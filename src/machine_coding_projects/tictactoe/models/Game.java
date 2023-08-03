package machine_coding_projects.tictactoe.models;

import javafx.util.Pair;
import machine_coding_projects.tictactoe.exceptions.InvalidGameConstructionException;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Board board;
    private List<Player> players;

    private int currentPlayerIndex;
    private GameStatus gameStatus;
    private List<Move> moves;

    private Game(GameBuilder gameBuilder) {
        this.players = gameBuilder.players;
        this.gameStatus = gameBuilder.gameStatus;
        this.board = gameBuilder.board;
        this.moves = gameBuilder.moves;
        this.currentPlayerIndex = gameBuilder.currentPlayerIndex;
    }


    public void makeMove(){
        /*
        1. Get the cell on which the player wants to move,
        2. Validate the cell
        3. If the cell looks good, then make the move
        4. Check for win or draw
        5. If there is a win or a draw, update the game status accordingly
        6. Else increment currentPlayerIndex
         */
        Player currentPlayer = players.get(currentPlayerIndex);
        Pair<Integer, Integer> nextMove = currentPlayer.getNextMove(board);

        while(!validateMove(nextMove)) {
            System.out.println("Not a valid move, please choose another cell");
            nextMove = currentPlayer.getNextMove(board);
        }
        //make the move
        // 1. Set cell status to Occupied
        Cell cell = board.getCell(nextMove.getKey(), nextMove.getValue());
        cell.setPlayer(currentPlayer);
        // 2. Create a move obj and add it to the list of moves
        moves.add(new Move(cell, currentPlayer));
        if(checkForWin()){
            gameStatus = GameStatus.ENDED;
            return;
        }
        if(checkForDraw()){
            gameStatus = GameStatus.DRAW;
            return;
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

    }

    private boolean checkForWin(){
        /*
        1. First check for all the rows if the row is filled completely then check if contains only one type of symbol
        2. Second check for all the columns & do the same as above
        3. Last check for the diagonals & do the same
         */
        boolean win = false;
        List<List<Cell>> grid = board.getCells();

        for(int i = 0;i < grid.size();i++){
            boolean flag = true;
            Character symbol = null;
            for(int j = 0;j < grid.get(i).size();j++){
                if(grid.get(i).get(j).getCellStatus().equals(CellStatus.AVAILABLE)){
                    flag = false;
                    break;
                }
                else{
                    if(symbol == null){
                        symbol = grid.get(i).get(j).getPlayer().getSymbol();
                    }
                    else{
                        if(symbol != grid.get(i).get(j).getPlayer().getSymbol()){
                            flag = false;
                            break;
                        }
                    }
                }
            }

            if(flag){
                return true;
            }
        }

        for(int i = 0;i < grid.size();i++){
            boolean flag = true;
            Character symbol = null;
            for(int j = 0;j < grid.get(i).size();j++){
                if(grid.get(j).get(i).getCellStatus().equals(CellStatus.AVAILABLE)){
                    flag = false;
                    break;
                }
                else{
                    if(symbol == null){
                        symbol = grid.get(j).get(i).getPlayer().getSymbol();
                    }
                    else{
                        if(symbol != grid.get(j).get(i).getPlayer().getSymbol()){
                            flag = false;
                            break;
                        }
                    }
                }
            }

            if(flag){
                return true;
            }
        }
        int i = 0;
        int j = 0;
        boolean flag = true;
        Character symbol = null;
        while(i < grid.size() && j < grid.size()){
            if(grid.get(i).get(j).getCellStatus().equals(CellStatus.AVAILABLE)){
                flag = false;
                break;
            }
            else{
                if(symbol == null){
                    symbol = grid.get(i).get(j).getPlayer().getSymbol();
                }
                else{
                    if(symbol != grid.get(i).get(j).getPlayer().getSymbol()){
                        flag = false;
                        break;
                    }
                }
            }
            i++;
            j++;
        }
        if(flag) return true;
        flag = true;
        i = 0;
        j = grid.size() - 1;
        symbol = null;
        while(i < grid.size() && j < grid.size()){
            if(grid.get(i).get(j).getCellStatus().equals(CellStatus.AVAILABLE)){
                flag = false;
                break;
            }
            else{
                if(symbol == null){
                    symbol = grid.get(i).get(j).getPlayer().getSymbol();
                }
                else{
                    if(symbol != grid.get(i).get(j).getPlayer().getSymbol()){
                        flag = false;
                        break;
                    }
                }
            }
            i++;
            j--;
        }
        return false;
    }

    private boolean checkForDraw(){
        return moves.size() == board.getSize() * board.getSize();
    }

    private boolean validateMove(Pair<Integer, Integer> nextMove){
        int row = nextMove.getKey();
        int col = nextMove.getValue();
        boolean isValidCell = row >= 0 && col >= 0 && row < board.getSize() && col < board.getSize();
        if(!isValidCell){
            return false;
        }
        Cell cell = board.getCell(row, col);
        return cell.getCellStatus().equals(CellStatus.AVAILABLE);
    }

    public void displayBoard(){
        board.displayBoard();
    }



    public static GameBuilder getBuilder(){
        return new GameBuilder();
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public static class GameBuilder{
        private Board board;
        private List<Player> players;

        private int currentPlayerIndex;
        private GameStatus gameStatus;
        private List<Move> moves;

        public Game build() throws InvalidGameConstructionException{
            if(players == null || players.size() == 0){
                throw new InvalidGameConstructionException("Players cannot be null or empty");
            }
            this.board = new Board(players.size()+1);
            this.currentPlayerIndex = 0;
            this.gameStatus = GameStatus.IN_PROGRESS;
            this.moves = new ArrayList<>();
            return new Game(this);
        }


        public GameBuilder setBoard(Board board) {
            this.board = board;
            return this;
        }

        public GameBuilder setPlayers(List<Player> players) {
            this.players = players;
            return this;
        }

        public GameBuilder setCurrentPlayerIndex(int currentPlayerIndex) {
            this.currentPlayerIndex = currentPlayerIndex;
            return this;
        }

        public GameBuilder setGameStatus(GameStatus gameStatus) {
            this.gameStatus = gameStatus;
            return this;
        }

        public GameBuilder setMoves(List<Move> moves) {
            this.moves = moves;
            return this;
        }
    }
}
