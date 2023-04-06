import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
public class TTT extends Frame implements MouseListener
{
    Board b;
    Image x,o;
    int first = 1;
    public static void main(String args[])
    {
        TTT h = new TTT(); 
        h.addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){System.exit(0);}});
    }
    void reset()
    {
        b = new Board();
        first = 1-first;
        if(first==1)
        {            
            b.callMinimax(0, 1);
            b.placeAMove(b.returnBestMove(), 1);
        }
    }
    TTT()
    {
        super("One Player Tic Tac Toe using Minimax");
        setBounds(500,200,400,400);
        setBackground(Color.black);
        setLayout(null);
        setVisible(true);        
        addMouseListener(this);
        b = new Board();
        reset();
        x = new ImageIcon("ximg.png").getImage();
        o = new ImageIcon("oimg.png").getImage();
    }
    public void paint(Graphics g)
    {
        setSize(400,500);
        for(int i=0;i<=2;i++)
            for(int j=0;j<=2;j++)
                if(b.board[i][j]==2)
                    g.drawImage(o,60+j*100,60+i*100,80,80,this);
                else if(b.board[i][j]==1)
                    g.drawImage(x,60+j*100,60+i*100,80,80,this);        
        g.setColor(Color.white);
        g.drawLine(50, 150, 350, 150);
        g.drawLine(50, 250, 350, 250);
        g.drawLine(150, 50, 150, 350);
        g.drawLine(250, 50, 250, 350);
        g.setFont(new Font("Comic Sans",Font.PLAIN,16));
        g.drawString("You are 'O' and computer is 'X'",100,400);
        if(b.hasXWon())
        { 
            g.drawString("You lose. Click to try again.",100,430);
        }
        else if(b.hasOWon())    //This will never happen
        {
            g.drawString("O Wins.",100,430);
        }
        else if(b.isGameOver())
        {
            g.drawString("Its a draw. Click to try again.",100,430);
        }
    }
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseClicked(MouseEvent e)
    {
        int x = (e.getX()-50)/100;
        int y = (e.getY()-50)/100;
        if(!b.isGameOver()&&b.board[y][x]==0)
        {
            Point userMove = new Point(y,x);
            b.placeAMove(userMove, 2);
            repaint();
            if(!b.isGameOver())
            {
                b.callMinimax(0, 1);
                b.placeAMove(b.returnBestMove(), 1);
            }
            repaint();
        }
        else if(b.isGameOver())
        {
            reset();
            repaint();
        }
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}
class Point 
{
    int x, y;
    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
class PointsAndScores
{
    int score;
    Point point;
    PointsAndScores(int score, Point point) 
    {
        this.score = score;
        this.point = point;
    }
}
class Board
{ 
    java.util.List<Point> availablePoints;
    int[][] board = new int[3][3];
    public Board() {}
    public boolean isGameOver() 
    {
        //Game is over is someone has won, or board is full (draw)
        return (hasXWon() || hasOWon() || getAvailableStates().isEmpty());
    }
    public boolean hasXWon()
    {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 1) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 1)) 
            return true;
        for (int i = 0; i < 3; ++i) 
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 1)|| (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 1))
                return true;
        return false;
    }
    public boolean hasOWon()
    {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 2) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 2)) 
            return true;
        for (int i = 0; i < 3; ++i) 
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 2)|| (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 2)) 
                return true;
        return false;
    }
    public java.util.List<Point> getAvailableStates() 
    {
        availablePoints = new ArrayList<Point>();
        for (int i = 0; i < 3; ++i) 
            for (int j = 0; j < 3; ++j) 
                if (board[i][j] == 0) 
                    availablePoints.add(new Point(i, j));
        return availablePoints;
    }
    public void placeAMove(Point point, int player) 
    {
        board[point.x][point.y] = player;   //player = 1 for X, 2 for O
    }
    public Point returnBestMove() 
    {
        int MAX = -100000;
        int best = -1;
        for (int i = 0; i < rootsChildrenScores.size(); ++i) 
        { 
            if (MAX < rootsChildrenScores.get(i).score) 
            {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }
        return rootsChildrenScores.get(best).point;
    }
    
    public int returnMin(java.util.List<Integer> list) 
    {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) 
        {
            if (list.get(i) < min) 
            {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }
    public int returnMax(java.util.List<Integer> list) 
    {
        int max = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); ++i) 
        {
            if (list.get(i) > max) 
            {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }
    java.util.List<PointsAndScores> rootsChildrenScores;
    public void callMinimax(int depth, int turn)
    {
        rootsChildrenScores = new ArrayList<PointsAndScores>();
        minimax(depth, turn);
    }    
    public int minimax(int depth, int turn) 
    {
        if (hasXWon()) return +1;
        if (hasOWon()) return -1;
        java.util.List<Point> pointsAvailable = getAvailableStates();
        if (pointsAvailable.isEmpty()) return 0; 
        java.util.List<Integer> scores = new ArrayList<Integer>(); 
        for (int i = 0; i < pointsAvailable.size(); ++i) 
        {
            Point point = pointsAvailable.get(i);  
            if (turn == 1) 
            { //X's turn select the highest from below minimax() call
                placeAMove(point, 1); 
                int currentScore = minimax(depth + 1, 2);
                scores.add(currentScore);
                if (depth == 0) 
                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));                
            } 
            else if (turn == 2) 
            {//O's turn select the lowest from below minimax() call
                placeAMove(point, 2); 
                scores.add(minimax(depth + 1, 1));
            }
            board[point.x][point.y] = 0; //Reset this point
        }
        return turn == 1 ? returnMax(scores) : returnMin(scores);
    }
}