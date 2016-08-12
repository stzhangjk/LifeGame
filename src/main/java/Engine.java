import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Grady on 2015.12.17.
 */
public class Engine implements Runnable{

    private Cell[][] cells;       //细胞集合
    private int width;              //世界宽
    private int height;             //世界高
    private int rate;               //更新频率，每次循环睡眠时间
    private int original;           //初代

    private Color liveColor;
    private Color deadColor;
    private int toLive;               //有几个为生，就转为生
    private int noChange;             //有几个为生，就不变

    //要提交更新的UI
    private JPanel[][] map;
    private JProgressBar count;
    private List<Cell> cellsToChange;       //要改变状态的细胞


    public void generateCell(){
        this.cells = new Cell[width][height];

        //生成细胞
        for(int x=0;x<width;x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = new Cell(x, y);
                cells[x][y] = cell;
            }
        }
    }

    public void generateOriginal(){
        //设置初代活细胞
        Random random = new Random();
        boolean[][] tag = new boolean[width][height];
        for(int i=0;i<original;){
            int randw = random.nextInt(width);
            int randh = random.nextInt(height);
            if(!tag[randw][randh]){
                i++;
                cells[randw][randh].setState(Cell.State.LIVE);
                map[randw][randh].setBackground(Color.BLACK);
                tag[randw][randh] = true;
            }
        }
    }

    public void setNeighbor(){
        //设置邻居
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                if(x > 0){
                    cells[x][y].getNeighbors().add(cells[x - 1][y]);
                    //cells[x - 1][y].getNeighbors().add(cells[x][y]);
                    if(y > 0){
                        cells[x][y].getNeighbors().add(cells[x-1][y-1]);
                        //cells[x-1][y-1].getNeighbors().add(cells[x][y]);
                    }
                    if(y<height-1){
                        cells[x][y].getNeighbors().add(cells[x-1][y+1]);
                        //cells[x-1][y+1].getNeighbors().add(cells[x][y]);
                    }
                }
                if(y > 0){
                    cells[x][y].getNeighbors().add(cells[x][y-1]);
                    //cells[x][y-1].getNeighbors().add(cells[x][y]);

                }
                if(x<width-1){
                    cells[x][y].getNeighbors().add(cells[x+1][y]);
                    //cells[x+1][y].getNeighbors().add(cells[x][y]);
                    if(y > 0){
                        cells[x][y].getNeighbors().add(cells[x+1][y-1]);
                        //cells[x+1][y-1].getNeighbors().add(cells[x][y]);
                    }
                    if(y<height-1){
                        cells[x][y].getNeighbors().add(cells[x+1][y+1]);
                        //cells[x+1][y+1].getNeighbors().add(cells[x][y]);
                    }
                }
                if(y<height-1){
                    cells[x][y].getNeighbors().add(cells[x][y+1]);
                    //cells[x][y+1].getNeighbors().add(cells[x][y]);
                }

            }
        }

    }

    /**
     *
     * @param rate 更新频率 ms/次
     * @param width 宽
     * @param height 高
     * @param original 初代细胞数，用来生成初代活细胞
     */
    public Engine(int rate, int width, int height, int original) {

        this.width = width;
        this.height = height;
        this.rate = rate;
        this.original = original;
        cellsToChange = new ArrayList<>();


        //init();


    }

    public void init(){
        generateCell();

        generateOriginal();

        setNeighbor();
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                Thread.sleep(getRate());
                for(Cell[] cs : cells){
                    for(Cell c : cs){
                        if(judge(c)){
                            //添加到要改变状态的集合
                            cellsToChange.add(c);
                        }
                    }
                }

                submitUI();
            }
        }catch(Exception e){
            //e.printStackTrace();
        }
    }

    public void submitUI(){
        for(Cell c : cellsToChange){
            if(c.getState() == Cell.State.LIVE){

                //生转死
                c.setState(Cell.State.DEAD);
                //SwingUtilities.invokeLater(()->{
                    map[c.getX()][c.getY()].setBackground(deadColor);
                    count.setValue(count.getValue()-1);
                //});
            }else{
                //死转生
                c.setState(Cell.State.LIVE);
                //SwingUtilities.invokeLater(()->{
                    map[c.getX()][c.getY()].setBackground(liveColor);
                    count.setValue(count.getValue()+1);
                //});
            }
        }
        cellsToChange.clear();
    }

    /**
     * 判断细胞下一代的生死状态
     * @param cell 细胞
     * @return 状态是否变化
     */
    public boolean judge(Cell cell){
        List<Cell> neis = cell.getNeighbors();  //将要判断的细胞的邻居集合。
        int count = 0;                          //邻居存活数
        boolean isChange = false;               //状态是否改变

        //统计邻居生死情况
        for(Cell nei : neis){
            if(nei.getState() == Cell.State.LIVE){
                count++;
            }
        }

        //生死判断
        if(count == toLive){
            if(cell.getState() == Cell.State.DEAD){
                isChange = true;
            }
        }else if(count == getNoChange()){
            isChange = false;
        }else{
            if(cell.getState() == Cell.State.LIVE) {
                isChange = true;
            }
        }

        return isChange;
    }




    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public JPanel[][] getMap() {
        return map;
    }

    public void setMap(JPanel[][] map) {
        this.map = map;
    }

    public Color getDeadColor() {
        return deadColor;
    }

    public void setDeadColor(Color deadColor) {
        this.deadColor = deadColor;
    }

    public Color getLiveColor() {
        return liveColor;
    }

    public void setLiveColor(Color liveColor) {
        this.liveColor = liveColor;
    }


    public int getToLive() {
        return toLive;
    }

    public void setToLive(int toLive) {
        this.toLive = toLive;
    }

    public int getNoChange() {
        return noChange;
    }

    public void setNoChange(int noChange) {
        this.noChange = noChange;
    }

    public JProgressBar getCount() {
        return count;
    }

    public void setCount(JProgressBar count) {
        this.count = count;
    }
}
